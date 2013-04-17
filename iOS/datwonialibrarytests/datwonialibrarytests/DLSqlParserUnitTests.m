//
//  DLSqlParserUnitTests.m
//  datwonialibrarytests
//
//  Created by Ian Cox on 10/04/2013.
//  Copyright (c) 2013 datownia. All rights reserved.
//

#import "DLSqlParserUnitTests.h"
#import <datownialibrary/DLSqlParser.h>

@implementation DLSqlParserUnitTests

- (void) testParse
{

    NSString *sqlCrLf = [[NSBundle bundleForClass:[self class]]
                           localizedStringForKey:@"sql_crlf"
                           value:@"missing" table:@"sqlLines"];
    
    NSLog(@"%@", sqlCrLf);
    
    [self doParserTest:sqlCrLf];
    
    NSString *sqlLf = [[NSBundle bundleForClass:[self class]]
                         localizedStringForKey:@"sql_lf"
                         value:@"missing" table:@"sqlLines"];
    
    [self doParserTest:sqlLf];
}

- (void) doParserTest:(NSString *)sql
{
    DLSqlParser *parser = [[DLSqlParser alloc] init];
    
    NSArray *lines = [parser parse:sql];
    
    STAssertEquals(4U, [lines count], @"num lines");
    
    NSString *line1 = [[NSBundle bundleForClass:[self class]]
                       localizedStringForKey:@"line1"
                       value:@"missing" table:@"sqlLines"];
    NSString *line2 = [[NSBundle bundleForClass:[self class]]
                       localizedStringForKey:@"line2"
                       value:@"missing" table:@"sqlLines"];
    NSString *line3 = [[NSBundle bundleForClass:[self class]]
                       localizedStringForKey:@"line3"
                       value:@"missing" table:@"sqlLines"];
    NSString *line4 = [[NSBundle bundleForClass:[self class]]
                       localizedStringForKey:@"line4"
                       value:@"missing" table:@"sqlLines"];
    
    STAssertEqualObjects([lines objectAtIndex:0], line1, @"line1");
    STAssertEqualObjects([lines objectAtIndex:1], line2, @"line2");
    STAssertEqualObjects([lines objectAtIndex:2], line3, @"line3");
    STAssertEqualObjects([lines objectAtIndex:3], line4, @"line4");
}

//
- (void) testFailingUpdate
{
    NSString *sql = [[NSBundle bundleForClass:[self class]]
                         localizedStringForKey:@"failingupdate"
                         value:@"missing" table:@"sqlLines"];
    
    NSLog(@"Orig");
    NSLog(@"%@", sql);
    
    DLSqlParser *parser = [[DLSqlParser alloc] init];
    
    NSArray *lines = [parser parse:sql];
    
    NSString *line1 = [[NSBundle bundleForClass:[self class]]
                       localizedStringForKey:@"failingupdate_line1"
                       value:@"missing" table:@"sqlLines"];
    NSString *lineLast = [[NSBundle bundleForClass:[self class]]
                       localizedStringForKey:@"failingupdate_lastline"
                       value:@"missing" table:@"sqlLines"];
    
    STAssertEqualObjects([lines objectAtIndex:0], line1, @"line1");
    STAssertEqualObjects([lines lastObject], lineLast, @"lineLast");
//    NSLog(@"Results");
//    for (NSString *line in lines) {
//        NSLog(@"%@", line);
//    }
}

@end
