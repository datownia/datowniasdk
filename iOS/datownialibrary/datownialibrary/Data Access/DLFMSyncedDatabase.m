//
//  FMSyncedDatabase.m
//  datownialibrary
//
//  Created by Ian Cox on 29/10/2012.
//  Copyright (c) 2012 datownia. All rights reserved.
//

#import "DLFMSyncedDatabase.h"

@implementation DLFMSyncedDatabase


- (BOOL) open
{
    BOOL __block result = NO;
    
    dispatch_sync(dispatch_get_main_queue(), ^void{
        result = [super open];
    });
    
    return result;
}

- (id) executeQuery:(NSString*)sql, ...
{
    id __block result = NULL;
    
    va_list vl;
    va_start(vl, sql);
    
    dispatch_sync(dispatch_get_main_queue(), ^void{
        
        result = [super executeQuery:sql withArgumentsInArray:nil orVAList:vl];
        

    });
    
    va_end(vl);
    
    return result;
}

- (BOOL) executeUpdate:(NSString*)sql, ...
{
    BOOL __block result = NO;
    
    va_list vl;
    va_start(vl, sql);
    
    dispatch_sync(dispatch_get_main_queue(), ^void{
        
        result = [super executeUpdate:sql withArgumentsInArray:nil orVAList:vl];
        
        
    });
    
    va_end(vl);
    
    return result;
}
@end
