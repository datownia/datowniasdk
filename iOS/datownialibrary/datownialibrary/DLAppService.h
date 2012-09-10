//
//  AppService.h
//  datownialibrary
//
//  Created by Ian Cox on 07/09/2012.
//  Copyright (c) 2012 Release Consulting. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "LROAuth2ClientDelegate.h"
#import "DownloaderDelegate.h"
#import "DLAppConfiguration.h"
#import "DLService.h"
 
@interface DLAppService : DLService
{
    
    
}

- (void) synchronizeTables;
- (void) downloadApp;

@end
