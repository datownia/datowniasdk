//
//  DLSeqExtractorTests.m
//  datwonialibrarytests
//
//  Created by RCL Admin on 16/04/2013.
//  Copyright (c) 2013 datownia. All rights reserved.
//

#import "DLSeqExtractorTests.h"
#import "DLSeqExtractor.h"

@implementation DLSeqExtractorTests

- (void) testExtract
{
    DLSeqExtractor *extractor = [[DLSeqExtractor alloc] init];
    
    STAssertEquals([extractor extract:@"replace into [table_def] (tablename, seq) values ('leiths/recipe/ids_1.0', 0);"], 0, @"positive case 1");
    STAssertEquals([extractor extract:@"replace into [table_def] (tablename, seq) values ('leiths/recipe/ids_1.0', 12);"], 12, @"positive case 2");
    STAssertEquals([extractor extract:@"replace into [table_def] (tablename, seq) values ('leiths/recipe/ids_1.0', 123);"], 123, @"positive case 3");
    STAssertEquals([extractor extract:@"delete from [leiths/recipe/ids_1.0] where _id = 'f8f1a192350830d17309dff478d7c879';"], NSNotFound, @"negative case 1");
}
@end
