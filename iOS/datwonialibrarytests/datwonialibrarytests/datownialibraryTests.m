//
//  datownialibraryTests.m
//  datownialibraryTests
//
//  Created by Ian Cox on 06/09/2012.
//  Copyright (c) 2012 datownia. All rights reserved.
//

#import "datownialibraryTests.h"
#import <datownialibrary/DLAppService.h>

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

- (void)testAppService
{
    DLAppService *service = [[DLAppService alloc] init];
    service.user = testApp;
    service.appKey = testAppKey;
    service.appSecret = testAppSecret;
    
    [service downloadApp];

    //TODO:enter tests here for your data
}

@end
