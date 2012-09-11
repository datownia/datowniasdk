//
//  DLOfflineApiManager.m
//  datownialibrary
//
//  Created by Ian Cox on 07/09/2012.
//  Copyright (c) 2012 datownia. All rights reserved.
//

#import "DLOfflineApiDataSync.h"
//#import "GCDSingleton.h"
#import "DLAppService.h"

@interface DLOfflineApiDataSync()

@property (nonatomic, strong) NSTimer *timer;

@end

@implementation DLOfflineApiDataSync

@synthesize timer;

//+ (id) sharedInstance
//{
//    DEFINE_SHARED_INSTANCE_USING_BLOCK(^{
//        return [[self alloc] init];
//    });
//}

- (id)init
{
    self = [super init];
    if (self) {
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(appDownloaded:) name:DLAppDownloadedNotification object:nil];
    }
    return self;
}

- (void) dealloc
{
    dispatch_release(backgroundQueue);
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

//start sync process
- (void) startSync:(NSTimer *)aTimer
{
    DLAppConfiguration* configuration = aTimer.userInfo;
    DLAppService *service = [[DLAppService alloc] initWithConfiguration:configuration];
    
    //check if database exists at the path
   if (![[NSFileManager defaultManager] fileExistsAtPath:configuration.dbPath])
    {
        //if no database then call app service to create initial database
        
        
        [service downloadApp];
        
        //TODO:when done get the seq value for each table and store
        //TODO:api will change to incorporate seq without these additional calls
        
        [self createTimer:configuration freq:configuration.checkChangesFrequencySeconds];
        return;
    }

    //if database then for each table, get current seq value and call the delta api to get the changes, then apply the changes
    [service synchronizeTables];
    
    
    //TODO: a method to check if our local copy is really up to date
    //possibly try to create the overall checksum based on the row _ids
    
    [self createTimer:configuration freq:configuration.checkChangesFrequencySeconds];
}

- (void) createTimer:(DLAppConfiguration *)aConfiguration freq:(NSTimeInterval)interval
{
    timer = [NSTimer scheduledTimerWithTimeInterval:interval target:self selector:@selector(startSync:) userInfo:aConfiguration repeats:NO];
}

- (void) start:(DLAppConfiguration *)aConfiguration onAppDownloaded:(event_block_t)onAppDownloadedHandler
{
    NSAssert(aConfiguration, @"Must supply configuration");
    
    onAppDownloadedBlock = onAppDownloadedHandler;
    
    if (backgroundQueue)
        dispatch_release(backgroundQueue);
    
    self.running = true;
    
    backgroundQueue = dispatch_queue_create("com.datownia.offlineapi.queue", NULL);
    
    dispatch_async(backgroundQueue, ^void{
        [self createTimer:aConfiguration freq:0.5f];
        
        [[NSRunLoop currentRunLoop] run];  
    });
    
}

- (void) stop
{
    //invalidate the timer. the run loop run command will then exit if nothing else attached to it and a background thread that was created will exit.
    if (timer)
        [timer invalidate];
    
    self.running = false;
}

- (void) appDownloaded:(NSNotification *)notification
{
    if (onAppDownloadedBlock)
    {
        onAppDownloadedBlock();
    }
}



@end
