//
//  datownialibraryTests.m
//  datownialibraryTests
//
//  Created by Ian Cox on 06/09/2012.
//  Copyright (c) 2012 datownia. All rights reserved.
//

#import "datownialibraryTests.h"
#import "AppService.h"

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

- (void)testExample
{
    AppService *service = [[AppService alloc] init];
    service.user = @"leiths";
    service.appKey = @"bd4d9s3j8rjfnc9d33";
    service.appSecret = @"jd8d74htkggf7f73jc";
    
    [service downloadApp];

    
}

@end
