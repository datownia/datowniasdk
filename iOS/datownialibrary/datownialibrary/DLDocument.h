//
//  EAApi.h
//  RSSReader
//
//  Created by Ian Cox on 22/02/2012.
//  Copyright (c) 2012 Release Consulting Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface DLDocument : NSObject
{
    NSString *docId;
    NSString *name;
}

@property (nonatomic, strong) NSString *docId;
@property (nonatomic, strong) NSString *name;
@property (nonatomic, strong) NSMutableArray *contents;
@property (nonatomic) NSUInteger numRows;
@property (nonatomic) NSUInteger seq;

- (id)initWithJSONObject:(id)aJsonObj;
+ (DLDocument *) documentFromJSONObject:(id)jsonObj;

@end
