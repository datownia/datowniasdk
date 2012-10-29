//
//  AppService.h
//  datownia sdk
//
//  Created by Simon McFarlane on 16/08/2012.
//  Copyright (c) 2012 Release Consulting. All rights reserved.
//

#import "DLAppService.h"
#import "LROAuth2Client.h"
#import "LROAuth2AccessToken.h"
#import "LRURLRequestOperation.h"
#import "FileDownloader.h"
#import "DLDbManager.h"
#import "TextDownloader.h"
#import "JSONKit.h"
#import "DLDocument.h"
#import "DLDocService.h"

@interface DLAppService()

- (void)httpGetDownload:(NSURL *)endpoint;


- (void) storeSeq;

@end

@implementation DLAppService

@synthesize configuration;


- (NSString *) scope
{
     return [NSString stringWithFormat:@"Read|app|%@", self.configuration.appKey];
}



- (void) downloadApp
{
    [self ensureClient];
    
    if (![self requestAccessTokenIfNeeded:[self scope]])
    {
        [[NSNotificationCenter defaultCenter] postNotificationName:DLAppDownloadFailedNotification object:self];
        return;//no token so cannot continue
    }
    
    NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"https://%@/api/app/%@/%@.sqlite", self.configuration.host, self.configuration.userName, self.configuration.appKey]];
    
    [self httpGetDownload:url];

    while (requesting) {
        [[NSRunLoop currentRunLoop] runUntilDate:[[NSDate date] dateByAddingTimeInterval:2]];
    }
    
    
    [[NSNotificationCenter defaultCenter] postNotificationName:DLAppDownloadedNotification object:self];
}

//gets the current seq for all datasets in the database
//note: this is temporary until the api can support adding them to the sequence table itself
- (void) storeSeq
{
    FMDatabase *db = [DLDbManager openSyncedDb:configuration.dbPath];
    
    NSString *sql = @"SELECT name FROM sqlite_master WHERE type='table'";
    
    FMResultSet *rs = [db executeQuery:sql];
    
    NSMutableArray *docs = [NSMutableArray array];
    
    while ([rs next])
    {
        DLDocService *docService = [[DLDocService alloc] initWithConfiguration:self.configuration];
        
        NSString *tableName = [rs stringForColumnIndex:0];
        
        NSArray *parts = [tableName componentsSeparatedByString:@"_"];
        
        NSString *version = [parts objectAtIndex:parts.count - 1];
        NSRange range = {.location = 0, .length = parts.count - 1};
        
        NSArray *pathParts = [parts objectsAtIndexes:[NSIndexSet indexSetWithIndexesInRange:range]];
        NSString *doc = [pathParts componentsJoinedByString:@"_"];
 
        DLDocument *document = [docService httpGetDocument:doc version:version];
        
        [docs addObject:document];
        

    }
    
    [self ensureTableDefTable];
    
    for (DLDocument *doc in docs) {
        NSString *seqSql = @"replace into table_def (tablename, seq) values (?,?);";
        
        [db executeUpdate:seqSql, doc.docId, [NSNumber numberWithInt:doc.seq]];
    }
    
}

- (void) ensureTableDefTable
{
    FMDatabase *db = [DLDbManager openSyncedDb:configuration.dbPath];
    
    NSString *sql = @"SELECT name FROM sqlite_master WHERE type='table' and name = 'table_def';";
    
    FMResultSet *rs = [db executeQuery:sql];
    
    BOOL tableDefFound = NO;
    while ([rs next]) {
        tableDefFound = YES;
        break;
    }
    
    if (!tableDefFound)
    {
        NSString *insert = @"create table table_def (tablename TEXT PRIMARY KEY, seq INTEGER);";
        [db executeUpdate:insert];
        //TODO: what happens if this fails?
    }
}

- (void) synchronizeTables
{
    DLog(@"datownia: synchronizing tables");
    FMDatabase *db = [DLDbManager openSyncedDb:configuration.dbPath];
    
    NSString *sql = @"SELECT tablename, seq FROM table_def";
    
    FMResultSet *rs = [db executeQuery:sql];
    
    //read all the table first into an array so the db is not locked waiting for remote calls
    NSMutableArray *tableDef = [NSMutableArray array];
    while ([rs next])
    {
        NSString *tableName = [rs stringForColumnIndex:0];
        
        NSNumber *seq = [NSNumber numberWithInt:[rs intForColumnIndex:1]];
        
        NSArray *tableDefRow = [NSArray arrayWithObjects:tableName, seq, nil];
        
        [tableDef addObject:tableDefRow];
    
    }
    
    NSMutableArray *sqlLines = [NSMutableArray array];
    for (NSArray *tableDefRow in tableDef) {
        DLDocService *docService = [[DLDocService alloc] initWithConfiguration:self.configuration];
        
        NSString *tableName = [tableDefRow objectAtIndex:0];
        
        NSUInteger seq = [[tableDefRow objectAtIndex:1] intValue];
        
        DLog(@"datownia: synchronizing %@", tableName);
        
        NSArray *parts = [tableName componentsSeparatedByString:@"_"];
        
        NSString *version = [parts objectAtIndex:parts.count - 1];
        NSRange range = {.location = 0, .length = parts.count - 1};
        
        NSArray *pathParts = [parts objectsAtIndexes:[NSIndexSet indexSetWithIndexesInRange:range]];
        NSString *doc = [pathParts componentsJoinedByString:@"_"];
        
        NSString *sql = [docService httpGetDeltaSql:doc version:version seq:seq];
        
        if ([sql length] > 0)
        {
            NSMutableArray *lines = [NSMutableArray arrayWithArray:[sql componentsSeparatedByString:@"\n"]];
            if (![lines lastObject]|| [[lines lastObject] length] == 0)
                [lines removeLastObject];
            [sqlLines addObjectsFromArray:lines];
            //DLog(@"%@",sql);
        }
    }

    for (NSString *sqlLine in sqlLines) {
        BOOL success = [db executeUpdate:sqlLine];
        
        if (!success)
        {
           //TODO: if fails, what do we do
            DLog(@"update failed %@", [db lastErrorMessage]);

        }
        
    }
    
    DLog(@"datownia: done synchronizing tables");
}








- (void)httpGetDownload:(NSURL *)endpoint
{
    NSString *auth = [self getAuth:[self scope]];
    
    [self startDownload:endpoint auth:auth];
    
}

- (void) startDownload:(NSURL *)endpoint  auth:(NSString *)auth
{
    
    
    FileDownloader *downloader = [[FileDownloader alloc] initWithUrl:endpoint downloadTo:self.configuration.dbPath withDelegate:self];
    [downloader.request setHTTPMethod:@"GET"];
    [downloader.request setValue:self.configuration.appKey forHTTPHeaderField:@"client_id"];
    [downloader.request setValue:auth forHTTPHeaderField:@"Authorization"];
    
    [downloader download];
}


@end
