//
//  datownialibraryTests.m
//  datownialibraryTests
//
//  Created by Ian Cox on 06/09/2012.
//  Copyright (c) 2012 datownia. All rights reserved.
//

#import "datownialibraryTests.h"
#import <datownialibrary/datownialibrary.h>
#import <datownialibrary/FMDatabase.h>
#import <datownialibrary/DLDbManager.h>

@implementation datownialibraryTests

- (void)setUp
{
    [super setUp];
    
    
    NSArray *arguments = [[NSProcessInfo processInfo] arguments];
    int numArgs = arguments.count;
    
    for (int i = 0; i <numArgs; i++) {
        NSString *arg = [arguments objectAtIndex:i];
        if ([arg isEqualToString:@"TESTAPP"])
        {
            testApp = [arguments objectAtIndex:i+1];
        }
        
        if ([arg isEqualToString:@"TESTAPPKEY"])
        {
            testAppKey = [arguments objectAtIndex:i+1];
        }
        
        if ([arg isEqualToString:@"TESTAPPSECRET"])
        {
            testAppSecret = [arguments objectAtIndex:i+1];
        }
    }
    

    STAssertFalse(!testApp || !testAppKey || !testAppSecret, @"Must specify args TESTAPP, TESTAPPKEY, TESTAPPSECRET when running test");

}

- (void)tearDown
{
    // Tear-down code here.
    
    [super tearDown];
}

- (NSString *)getDbPath
{
    //TODO: when have proper test account to call then we can do proper tests to ensure we are getting correct data back
    //as well as doing update tests with expected results
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *pathToFile = [NSString stringWithFormat:@"%@/test.sqlite", documentsDirectory];
    return pathToFile;
}

//- (void)ignoretestAppService
//{
//    DLAppService *service = [[DLAppService alloc] init];
//    service.user = testApp;
//    service.appKey = testAppKey;
//    service.appSecret = testAppSecret;
//    
//    [service downloadApp];
//    
////    while (service.requesting) {
////        [[NSRunLoop currentRunLoop] runUntilDate:[[NSDate date] dateByAddingTimeInterval:2]];
////    }
//
//    //TODO:enter tests here for your data
//}

- (void) testOfflineApiDownloadOnce
{
    NSString *pathToFile = [self getDbPath];
    
    DLOfflineApiDataSync *manager = [[DLOfflineApiDataSync alloc] init];
    
    DLAppConfiguration *configuration = [[DLAppConfiguration alloc] init];
    configuration.userName = testApp;
    configuration.appKey = testAppKey;
    configuration.appSecret = testAppSecret;
    configuration.checkChangesFrequencySeconds = 1;
    configuration.dbPath = pathToFile;
    configuration.host = @"192.168.42.47";
    
    [manager start:configuration onAppDownloaded:^(){[manager stop];}];
    
    while(manager.running)
    {
        [[NSRunLoop currentRunLoop] runUntilDate:[NSDate dateWithTimeIntervalSinceNow:2]];
    }
    
    //now test sequence
    [self sequenceTest];
}

- (void) sequenceTest
{
    FMDatabase *db = [DLDbManager openDb:[self getDbPath]];
    
    NSString *sql = @"update table_def set seq = 0;";

    [db executeUpdate:sql];
}

@end
