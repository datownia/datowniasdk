//
//  EAContentItem.h
//  RSSReader
//
//  Created by Ian Cox on 22/02/2012.
//  Copyright (c) 2012 Release Consulting Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface DLContentItem : NSObject


@property (nonatomic, strong) NSString *rowId;
@property (nonatomic, readonly) NSString *displayTitle; //used for general title for the item. customize in concrete class
@property (nonatomic, readonly) NSString *displayDetail; //used for general detail for the item. customize in concrete class

@end
