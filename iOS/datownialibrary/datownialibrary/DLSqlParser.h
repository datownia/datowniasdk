//
//  DLSqlParser.h
//  datownialibrary
//
//  Created by Ian Cox on 10/04/2013.
//  Copyright (c) 2013 datownia. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface DLSqlParser : NSObject

- (NSMutableArray *)parse:(NSString *)sql;

@end
