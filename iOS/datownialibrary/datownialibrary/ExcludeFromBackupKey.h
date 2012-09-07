//
//  ExcludeFromBackupKey.h
//
//  Copyright (c) 2012 Release Consulting. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ExcludeFromBackupKey : NSObject

+ (BOOL)addSkipBackupAttributeToItemAtURL:(NSURL *)URL;

@end
