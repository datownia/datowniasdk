//
//  EAApi.m
//  RSSReader
//
//  Created by Ian Cox on 22/02/2012.
//  Copyright (c) 2012 Release Consulting Ltd. All rights reserved.
//

#import "DLDocument.h"
#import "JSONKit.h"
#import "DLContentItem.h"
#import "DLGenericContentItem.h"
#import "NSString+Extras.h"

@interface DLDocument()

- (DLContentItem *) contentItemForRow:(id)row;




@end

@implementation DLDocument

@synthesize contents, numRows;

@dynamic docId;

- (NSString *) docId
{
    return docId;
}

- (void) setDocId:(NSString *)value
{
    docId = [value stringByReplacingOccurrencesOfString:@"^~^" withString:@"/"];
}

@dynamic name;

- (NSString *) name
{
    return name;
}

- (void) setName:(NSString *)value
{
    name = [value stringByReplacingOccurrencesOfString:@"^~^" withString:@"/"];
}

- (id)initWithJSONObject:(id)aJsonObj 
{
    self = [self init];
    if (self) {
        self.docId = [aJsonObj objectForKey:@"_id"];
        self.name = [aJsonObj objectForKey:@"name"];
        self.numRows = [[aJsonObj objectForKey:@"rows"] intValue];
        self.seq = [[aJsonObj objectForKey:@"seq"] intValue];
        
        id rows = [aJsonObj objectForKey:@"contents"];
        self.contents = [NSMutableArray arrayWithCapacity:[rows count]];
        
        //TODO:perf - we iterate all the rows here on creation. should this be differred execution?
        //i.e. create contentItems for row on enumeration if not already created?
        for(id row in rows)
        {
            DLContentItem *contentItem = [self contentItemForRow:row];
            contentItem.rowId = [row objectForKey:@"_id"];
            [self.contents addObject:contentItem];

        }
    }
    return self;
}

- (DLContentItem *) contentItemForRow:(id)row
{
    //default implementation. override in concrete class
    DLGenericContentItem *item = [[DLGenericContentItem alloc] init];

    item.row = row;
    return item;
}

+ (DLDocument *) documentFromJSONObject:(id)jsonObj
{
    DLDocument *document = [[self alloc] initWithJSONObject:jsonObj];
    
    return document;
}

@end
