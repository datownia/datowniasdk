//
//  DLService.h
//  datownialibrary
//
//  subclasses handle calling the api
//  manages auth
//  intended to be used sequentially, so one service instance can be used for multiple request but only sequentially
//  if you want multiple requests simultaneously then create multple instances of the service
//
//  Created by Ian Cox on 10/09/2012.
//  Copyright (c) 2012 Release Consulting Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "DLAppConfiguration.h"
#import "LROAuth2Client.h"
#import "DownloaderDelegate.h"

@interface DLService : NSObject  <LROAuth2ClientDelegate, DownloaderDelegate>
{
    LROAuth2Client *client;
    BOOL requesting;
    BOOL errored;
    NSError *lastError;
    NSString *lastScope;
}

@property (nonatomic,strong) DLAppConfiguration *configuration;
@property (nonatomic) BOOL requesting;

- (id) initWithConfiguration:(DLAppConfiguration *)configuration;

- (void)requestAccessTokenIfNeeded:(NSString *)scope;
- (void)ensureClient;

- (id) httpGetJson:(NSURL *)endpoint scope:(NSString *)scope;
- (NSString *) httpGetRawString:(NSURL *)endpoint scope:(NSString *)scope;
- (NSData *) httpGetData:(NSURL *)endpoint scope:(NSString *)scope;

- (NSString *)getAuth:(NSString *)scope;

@end
