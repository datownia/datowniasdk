//
//  DLOfflineApiManager.h
//  datownialibrary
//
//  Created by Ian Cox on 07/09/2012.
//  Copyright (c) 2012 Release Consulting Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "DLAppConfiguration.h"

typedef void (^event_block_t)();
@interface DLOfflineApiDataSync : NSObject
{
    dispatch_queue_t backgroundQueue;
    event_block_t onAppDownloadedBlock ;
}

@property (nonatomic) BOOL running;

//+ (id)sharedInstance;

//- (void) configure:(DLAppConfiguration *)configuration;
- (void) start:(DLAppConfiguration *)aConfiguration onAppDownloaded:(event_block_t)onAppDownloadedHandler;
- (void) stop;

@end
