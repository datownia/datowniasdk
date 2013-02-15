//
//  DLOfflineApiManager.m
//  datownialibrary
//
//  Created by Ian Cox on 07/09/2012.
//  Copyright (c) 2012 Release Consulting Ltd. All rights reserved.
//

#import "DLOfflineApiDataSync.h"
//#import "GCDSingleton.h"
#import "DLAppService.h"

@interface DLOfflineApiDataSync()

@property (nonatomic, strong) NSTimer *timer;
@property (nonatomic) NSUInteger failCount;

@end

@implementation DLOfflineApiDataSync

@synthesize timer, running, failCount;

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
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(appDownloadFailed:) name:DLAppDownloadFailedNotification object:nil];
    }
    return self;
}

- (void) dealloc
{
    dispatch_release(backgroundQueue);
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (NSTimeInterval)getNextCheckTime:(DLAppConfiguration *)configuration
{
    //TODO: a method to check if our local copy is really up to date
    //possibly try to create the overall checksum based on the row _ids
    
    failCount = MIN(failCount, 16); //constrain failCount
    NSTimeInterval nextCheck = configuration.checkChangesFrequencySeconds * pow(2,failCount);
    nextCheck = MIN(nextCheck, 60*60*24); //min recheck every day, even if failing
    DLog(@"datownia: checking again in %f seconds", nextCheck);
    return nextCheck;
}

//start sync process
- (void) startSync:(NSTimer *)aTimer
{
    //TODO: add error handling so sync errors do not crash the app
    
    DLAppConfiguration* configuration = aTimer.userInfo;
    DLAppService *service = [[DLAppService alloc] initWithConfiguration:configuration];
    
    //check if database exists at the path
   if (![[NSFileManager defaultManager] fileExistsAtPath:configuration.dbPath])
    {
        //if no database then call app service to create initial database
        //usually the database will already exist as the app will have been bundled with seed database
        
        DLog(@"datownia: starting database download");
        [service downloadApp];
        
        NSTimeInterval nextCheck = [self getNextCheckTime:configuration];
        [self createTimer:configuration freq:nextCheck];
        return;
    }

    //if database then for each table, get current seq value and call the delta api to get the changes, then apply the changes
    [service synchronizeTables];
    
    
    NSTimeInterval nextCheck = [self getNextCheckTime:configuration];
    [self createTimer:configuration freq:nextCheck];
}

- (void) createTimer:(DLAppConfiguration *)aConfiguration freq:(NSTimeInterval)interval
{
    timer = [NSTimer scheduledTimerWithTimeInterval:interval target:self selector:@selector(startSync:) userInfo:aConfiguration repeats:NO];
}

- (void) start:(DLAppConfiguration *)aConfiguration onAppDownloaded:(event_block_t)onAppDownloadedHandler onFailure:(event_block_t)onFailureHandler
{
    if (!aConfiguration)
    {
        DLog(@"Must supply configuration");
        return;
    }
    
    onAppDownloadedBlock = onAppDownloadedHandler;
    onFailureBlock = onFailureHandler;
    
    if (backgroundQueue)
        dispatch_release(backgroundQueue);
    
    self.running = true;
    
    backgroundQueue = dispatch_queue_create("com.datownia.offlineapi.queue", NULL);
    
    dispatch_async(backgroundQueue, ^void{
        [self createTimer:aConfiguration freq:0.5f]; //fire the first one straight away
        
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
    DLog(@"datownia: done database download");
    failCount = 0;
    if (onAppDownloadedBlock)
    {
        onAppDownloadedBlock();
    }
}

- (void) appDownloadFailed:(NSNotification *)notification
{
    DLog(@"datownia: failed database download");
    failCount++;
    if (onFailureBlock)
    {
        onFailureBlock();
    }

}


@end
