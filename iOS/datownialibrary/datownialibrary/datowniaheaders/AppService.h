//
//  AppService.h
//  LeithsCookeryApp
//
//  Created by Simon McFarlane on 16/08/2012.
//  Copyright (c) 2012 Release Consulting. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "LROAuth2ClientDelegate.h"
#import "DownloaderDelegate.h"
 
@interface AppService : NSObject <LROAuth2ClientDelegate, DownloaderDelegate>
{
    BOOL requesting;
}

@property (strong) NSString *appKey;
@property (strong) NSString *appSecret;
@property (strong) NSString *user;

- (void) downloadApp;

@end
