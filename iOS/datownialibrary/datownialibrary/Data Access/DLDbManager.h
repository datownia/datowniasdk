//
//  DbManager.h
//  RockGuitarMadeEasy
//
//  Created by Ian Cox on 01/07/2010.
//  Copyright 2010 Release Consulting Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "DLFMDatabase.h"

@interface DLDbManager : NSObject {


}

//+ (NSString *) getLibraryDbPath;
//+ (NSString *) getContentDbPath:(NSString *)dbName;
//+ (NSString *) getDocumentsDbPath;
//+ (NSString *) getDatabasePath;
+ (DLFMDatabase *) openSyncedDb: (NSString *) dbPath ;
@end
