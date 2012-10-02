//
//  EAContentItem.m
//  RSSReader
//
//  Created by Ian Cox on 22/02/2012.
//  Copyright (c) 2012 Release Consulting Ltd. All rights reserved.
//

#import "DLContentItem.h"

@interface DLContentItem()


@end


@implementation DLContentItem

@synthesize rowId;


//override to provide concrete title
@dynamic displayTitle;
- (NSString *)displayTitle
{
    return @"";
}

//override to provide concrete detail
@dynamic displayDetail;
- (NSString *)displayDetail
{
    return @"";
}

@end
