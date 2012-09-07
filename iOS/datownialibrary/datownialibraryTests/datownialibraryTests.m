//
//  datownialibraryTests.m
//  datownialibraryTests
//
//  Created by Ian Cox on 06/09/2012.
//  Copyright (c) 2012 datownia. All rights reserved.
//

#import "datownialibraryTests.h"
#import "DLAppService.h"
#import "testmacros.h"

@implementation datownialibraryTests

- (void)setUp
{
    [super setUp];
    
    // Set-up code here.
}

- (void)tearDown
{
    // Tear-down code here.
    
    [super tearDown];
}

- (void)testAppService
{
    DLAppService *service = [[DLAppService alloc] init];
    service.user = TESTAPPLITERAL;
    service.appKey = TESTAPPKEYLITERAL;
    service.appSecret = TESTAPPSECRETLITERAL;
    
    [service downloadApp];

    //TODO:enter tests here for your data
}

@end
