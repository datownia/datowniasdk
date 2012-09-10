//
//  DbManager.m
//  RockGuitarMadeEasy
//
//  Created by Ian Cox on 01/07/2010.
//  Copyright 2010 Release Consulting Ltd. All rights reserved.
//

#import "DLDbManager.h"
#import "FMDatabase.h"

@implementation DLDbManager

//+ (NSString *) getLibraryDbPath
//{
//	NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
//	NSString *documentsDir = [paths objectAtIndex:0];
//	return [documentsDir stringByAppendingPathComponent:@"Library.db"];
//}
//
//+(NSString *) getContentDbPath:(NSString *)dbName
//{
//	if ([dbName rangeOfString:@"/"].location != NSNotFound)
//		return dbName;
//		
//	NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
//	NSString *documentsDir = [paths objectAtIndex:0];
//	
//	NSString *dbPath = [[NSBundle mainBundle] pathForResource:dbName ofType:@"db"];
//	if (!dbPath)
//	{
//		
//		dbPath = [documentsDir stringByAppendingPathComponent:[NSString stringWithFormat:@"%@.db", dbName]];
//	}
//	
//	
//	return dbPath;
//}


+ (NSString *) getDatabasePath 
{
	NSString *userPath;
	userPath = [self getUserCataloguePath];
	
    return userPath;
	
}

+ (NSString *) getUserCataloguePath 
{
	NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
	NSString *documentsDir = [paths objectAtIndex:0];
	NSString *userPath = [documentsDir stringByAppendingPathComponent:@"test.sqlite"]; //catalogue.db
	return userPath;
}

+ (FMDatabase *) openDb:(NSString *)dbPath  
{
	FMDatabase* db = [FMDatabase databaseWithPath:dbPath];
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
