//
//  DLAppConfiguration.h
//  datownialibrary
//
//  Created by Ian Cox on 07/09/2012.
//  Copyright (c) 2012 datownia. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface DLAppConfiguration : NSObject

@property (nonatomic, strong) NSString *userName;
@property (nonatomic, strong) NSString *appKey;
@property (nonatomic, strong) NSString *appSecret;
@property (nonatomic, strong) NSString *dbPath;
@property (nonatomic) double checkChangesFrequencySeconds;
@property (nonatomic, strong) NSString *host;

@end
