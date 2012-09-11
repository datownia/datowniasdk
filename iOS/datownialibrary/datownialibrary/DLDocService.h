//
//  DLDocService.h
//  datownialibrary
//
//  Created by Ian Cox on 10/09/2012.
//  Copyright (c) 2012 datownia. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "DLDocument.h"
#import "DLService.h"

@interface DLDocService : DLService

- (DLDocument *)httpGetDocumentByUrl:(NSURL *)endpoint scope:(NSString *)scope;
- (DLDocument *)httpGetDocument:(NSString *)doc version:(NSString *)version;
- (NSString *)httpGetDeltaSql:(NSString *)doc version:(NSString *)version seq:(NSUInteger)seq;

@end
