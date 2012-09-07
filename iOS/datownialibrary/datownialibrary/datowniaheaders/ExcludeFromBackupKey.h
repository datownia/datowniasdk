//
//  ExcludeFromBackupKey.h
//  GuitarBuddy
//
//  Created by RCL Admin on 28/06/2012.
//  Copyright (c) 2012 Release Consulting. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ExcludeFromBackupKey : NSObject

+ (BOOL)addSkipBackupAttributeToItemAtURL:(NSURL *)URL;

@end
