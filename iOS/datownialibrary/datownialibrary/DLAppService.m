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
#import "DLFileDownloader.h"
#import "DLDbManager.h"
#import "DLTextDownloader.h"
//#import <JSONKit/JSONKit.h>
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
    DLFMDatabase *db = [DLDbManager openSyncedDb:configuration.dbPath];
    
    NSString *sql = @"SELECT name FROM sqlite_master WHERE type='table'";
    
    DLFMResultSet *rs = [db executeQuery:sql];
    
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
    DLFMDatabase *db = [DLDbManager openSyncedDb:configuration.dbPath];
    
    NSString *sql = @"SELECT name FROM sqlite_master WHERE type='table' and name = 'table_def';";
    
    DLFMResultSet *rs = [db executeQuery:sql];
    
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
    DLFMDatabase *db = [DLDbManager openSyncedDb:configuration.dbPath];
    
    NSString *sql = @"SELECT tablename, seq FROM table_def";
    
    DLFMResultSet *rs = [db executeQuery:sql];
    
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
        
        //FIX FOR PUBNAME ISSUE - strip out the publisher name from the tablename for use in the delta call
		NSArray *tablePathParts = [tableName componentsSeparatedByString:@"/"];
        NSString *publisherName = [tablePathParts objectAtIndex:0]; //save the publisher name for later
        
        NSRange tablerange = {.location = 1, .length = tablePathParts.count - 1};
		tablePathParts = [tablePathParts objectsAtIndexes:[NSIndexSet indexSetWithIndexesInRange:tablerange]];
		tableName = [tablePathParts componentsJoinedByString:@"/"];
		//END
        
        
        
        NSArray *parts = [tableName componentsSeparatedByString:@"_"];
        
        NSString *version = [parts objectAtIndex:parts.count - 1];
        NSRange range = {.location = 0, .length = parts.count - 1};
        
        NSArray *pathParts = [parts objectsAtIndexes:[NSIndexSet indexSetWithIndexesInRange:range]];
        NSString *doc = [pathParts componentsJoinedByString:@"_"];
                
        NSString *sql = [docService httpGetDeltaSql:doc version:version seq:seq];
        
        if ([sql length] > 0)
        {
            
            DLog(@"******");
            DLog(@"sql: %@", sql);
            DLog(@"******");
            
//            NSMutableArray *lines = [NSMutableArray arrayWithArray:[sql componentsSeparatedByString:@"\n"]];
//            NSMutableArray *modifiedLines = [NSMutableArray arrayWithCapacity:lines.count];
            
//            //split on ;
//            NSMutableArray *candidatelines = [NSMutableArray arrayWithArray:[sql componentsSeparatedByString:@";"]];
//            if ([[candidatelines objectAtIndex:0] componentsSeparatedByString:@"'"] % 2 == 0)
//            {
//                //merge 0 and 1, replace in array and redo
//                NSString *merged = [[candidatelines objectAtIndex:0] stringByAppendingString:[candidatelines objectAtIndex:1]];
//                NSRange range;
//                range.location = 0;
//                range.length = 2;
//                NSIndexSet *set = [NSIndexSet indexSetWithIndexesInRange:range];
//                [candidatelines removeObjectsAtIndexes:set];
//                [candidatelines insertObject:merged atIndex:0];
//            }
//

            
                //find the ; to split on.
                //i.e. find occurances of ; and split if the number of apostrophes is even.
            
            
                int apostropheCount = 0;
                int thisLineCharCount = 0;
                int adjustment = 2;
                NSMutableArray *lines = [[NSMutableArray alloc] init];
                NSMutableArray *modifiedLines = [[NSMutableArray alloc] init];
            
                for (int i=0;i<[sql length];i++) //loop through every character in the sql.
                {
                    unichar ch;
                    ch = [sql  characterAtIndex:i];
                    //DLog(@"Processing character: %c", ch);
                    
                    if (ch == '\'') //apostrophe
                    {
                        apostropheCount++;
                    }
                    else
                    {
                        if ((ch == ';') && (apostropheCount % 2 == 0))
                        {
                            //we found where to split
                            
                            //save all the characters so far into a string
                            //NSString *newLine = [sql substringToIndex:i]
                            NSString *newLine = [sql substringWithRange:NSMakeRange(i-thisLineCharCount-1, thisLineCharCount+2)];
                            
                            
                            //get last char,
                            //while last char is /r or /n
                            //remove it
                            
                            unichar firstCh = [newLine characterAtIndex: 0];
                            unichar lastCh = [newLine characterAtIndex: [newLine length] - 1];
                            //DLog(@"firstCh: %c", firstCh);
                            //DLog(@"lastCh: %c", lastCh);
                            
                            if (([[NSCharacterSet newlineCharacterSet] characterIsMember:firstCh]) || ([[NSCharacterSet newlineCharacterSet] characterIsMember:lastCh]))
                            {
                                
                                DLog(@"first char - carriage return or new line found!");
                                //newLine = [string substringToIndex:[string length] - 1];
                                //newLine = [newLine substringWithRange:NSMakeRange(1, [newLine length]-1)];
                            }
                            
//                            if ([[NSCharacterSet newlineCharacterSet] characterIsMember:lastCh])
//                            {
//                                
//                                DLog(@"last char - carriage return or new line found!");
//                                //newLine = [string substringToIndex:[string length] - 1];
//                                //newLine = [newLine substringWithRange:NSMakeRange(1, [newLine length]-1)];
//                            }
                            
                            //[newLine stringByTrimmingCharactersInSet:[NSCharacterSet newlineCharacterSet]];
                            [lines addObject:newLine];
                            //DLog(@"newLine: %@", newLine);
                            
                            //reset count for the next line.
                            thisLineCharCount = 0;
                            
                            //i ++; // to skip the newline character
                            
                            //remove all the characters so far from the original sql string
                            //sql = [sql substringWithRange:NSMakeRange(i, [sql length]-i)];
                            //i += [sql length]; //-i
                            

//                            while ([[NSCharacterSet whitespaceAndNewlineCharacterSet] characterIsMember:ch])
//                            {
//                                i++;
//                                ch = [sql  characterAtIndex:i];
//                            }
                        }
                        else
                        {
                            thisLineCharCount++;
                        }
                    }
                }

            DLog(@"lines.count: %i", lines.count);
            modifiedLines = [NSMutableArray arrayWithCapacity:lines.count];

            
            for (int i=0; i<lines.count; i++)
            {
                
                NSMutableString *lineString = [[NSMutableString alloc]initWithString:[lines objectAtIndex:i]];
                
                if ([lineString rangeOfString:publisherName].location == NSNotFound)
                {
                    DLog(@"Publisher name not found!");
                    
                    //find the table name and replace it with the publisher name + table name.
                    if ([lineString rangeOfString:tableName].location != NSNotFound)
                    {
                        NSString *stringToInsert = [NSString stringWithFormat:@"%@/%@", publisherName, tableName];
                        [lineString replaceOccurrencesOfString:tableName withString:stringToInsert options:NSCaseInsensitiveSearch range:(NSRange){0,[lineString length]}];
                    
                        //DLog(@"lineString: %@", lineString);
                        
                        [modifiedLines addObject:lineString];
                    }
                }
                else
                {
                    [modifiedLines addObject:lineString];
                }
            
            }
            
            
//            if (modifiedLines.count > 0)
//            {
//                for (int i=0; i<=modifiedLines.count-1; i++)
//                {
//                    DLog(@"%@", [modifiedLines objectAtIndex:i]);
//                }
//            }
            
            if (![modifiedLines lastObject]|| [[modifiedLines lastObject] length] == 0)
                [modifiedLines removeLastObject];
            
            [sqlLines addObjectsFromArray:modifiedLines];

//            DLog(@"%@",sql);

        }
        
        DLog(@"*** sqlLines.count: %i", sqlLines.count);
    }

    

//    if (sqlLines.count > 0)
//    {
//        for (int i=0; i<sqlLines.count; i++)
//        {
//            DLog(@"** %@", [sqlLines objectAtIndex:i]);
//        }
//    }
    
    
    if (sqlLines.count > 1) //table_def always included if there were actual changes, so 1 line should never occur - if it does this is due to bug in datownia (fixed but not on live)
    {
        for (NSString *sqlLine in sqlLines) {
            BOOL success = [db executeUpdate:sqlLine];
            
            //something has changed so...
            NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
            [defaults setInteger:1 forKey:@"newContent"];
            
            if (!success)
            {
                //TODO: if fails, what do we do
                DLog(@"update failed %@", [db lastErrorMessage]);
                
            }
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
    
    
    DLFileDownloader *downloader = [[DLFileDownloader alloc] initWithUrl:endpoint downloadTo:self.configuration.dbPath withDelegate:self];
    [downloader.request setHTTPMethod:@"GET"];
    [downloader.request setValue:self.configuration.appKey forHTTPHeaderField:@"client_id"];
    [downloader.request setValue:auth forHTTPHeaderField:@"Authorization"];
    
    [downloader download];
}


@end
