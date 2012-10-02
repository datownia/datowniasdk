//
//  DbManager.h
//  RockGuitarMadeEasy
//
//  Created by Ian Cox on 01/07/2010.
//  Copyright 2010 Release Consulting Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "FMDatabase.h"

@interface DLDbManager : NSObject {


}

//+ (NSString *) getLibraryDbPath;
//+ (NSString *) getContentDbPath:(NSString *)dbName;
+ (NSString *) getUserCataloguePath;
+ (NSString *) getDatabasePath;
+ (FMDatabase *) openDb: (NSString *) dbPath ;
@end
