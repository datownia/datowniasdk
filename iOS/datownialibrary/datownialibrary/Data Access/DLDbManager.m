//
//  DbManager.m
//  RockGuitarMadeEasy
//
//  Created by Ian Cox on 01/07/2010.
//  Copyright 2010 Release Consulting Ltd. All rights reserved.
//

#import "DLDbManager.h"
#import "FMSyncedDatabase.h"

@implementation DLDbManager

//+ (NSString *) getDatabasePath 
//{
//	NSString *userPath;
//	userPath = [self getDocumentsDbPath];
//	
//    return userPath;
//	
//}
//
//+ (NSString *) getDocumentsDbPath 
//{
//	NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
//	NSString *documentsDir = [paths objectAtIndex:0];
//	NSString *userPath = [documentsDir stringByAppendingPathComponent:@"test.sqlite"]; //catalogue.db
//	return userPath;
//}

+ (FMDatabase *) openSyncedDb:(NSString *)dbPath  
{
	FMDatabase* db = [FMSyncedDatabase databaseWithPath:dbPath];
	if (![db open]) {
		[NSException raise:@"DbOpenError" format:@"Could not open db."];
		[db setShouldCacheStatements:NO];
		return nil;
	}
	
	[db setBusyRetryTimeout:1000];
	[db setCrashOnErrors:YES];
	
#ifdef DEBUG
	[db setLogsErrors:YES];
	//[db setTraceExecution:YES];
	
#endif
	
	return db;
}

@end
