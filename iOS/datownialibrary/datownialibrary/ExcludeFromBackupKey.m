//
//  ExcludeFromBackupKey.m
//  GuitarBuddy
//
//  Created by RCL Admin on 28/06/2012.
//  Copyright (c) 2012 Release Consulting. All rights reserved.
//

#import "ExcludeFromBackupKey.h"
#import <sys/xattr.h>

@implementation ExcludeFromBackupKey

+ (BOOL)addSkipBackupAttributeToItemAtURL:(NSURL *)URL
{
    
    NSString *iOSVersion = [[UIDevice currentDevice] systemVersion];
    float version = [iOSVersion floatValue];
    
    
    //DLog(@"XXXXXXXXX");
    //DLog(@"version:%f", version);
    
    
    //if iOS 5.1 and greater
    if (version >= 5.09) //i.e.5.1
    {
        assert([[NSFileManager defaultManager] fileExistsAtPath: [URL path]]);
        
        NSError *error = nil;
        BOOL success = [URL setResourceValue: [NSNumber numberWithBool: YES]
                                      forKey: NSURLIsExcludedFromBackupKey error: &error];
        if(!success)
        {
            NSLog(@"Error excluding %@ from backup %@", [URL lastPathComponent], error);
        }
        return success;
    }
    
    //else if iOS 5.0.1
    else //if ((version < 5.1)&&(version>=5.0))
    {
        assert([[NSFileManager defaultManager] fileExistsAtPath: [URL path]]);
        
        const char* filePath = [[URL path] fileSystemRepresentation];
        
        const char* attrName = "com.apple.MobileBackup";
        u_int8_t attrValue = 1;
        
        int result = setxattr(filePath, attrName, &attrValue, sizeof(attrValue), 0, 0);
        return result == 0;
    }
    
    
    
    
    //else iOS 5.0 and lower
    //{
    //      cache stuff here.
    //}
    
}

@end
