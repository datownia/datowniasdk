//
//  DLDocService.m
//  datownialibrary
//
//  Created by Ian Cox on 10/09/2012.
//  Copyright (c) 2012 Release Consulting Ltd. All rights reserved.
//

#import "DLDocService.h"
#import "NSString+Extras.h"

@implementation DLDocService


- (NSString *) scope:(NSString *)doc
{
    DLog(@"doc: %@", doc);
    NSString *nameMinusExt = [doc fileNameMinusExt];
    
    return [NSString stringWithFormat:@"Read|%@|%@", self.configuration.userName, nameMinusExt];
}

- (DLDocument *)httpGetDocumentByUrl:(NSURL *)endpoint scope:(NSString *)scope
{
    id json = [self httpGetJson:endpoint scope:scope];
    
    DLDocument *document = [DLDocument documentFromJSONObject:json];
    
    return document;
}

- (DLDocument *)httpGetDocument:(NSString *)doc version:(NSString *)version
{
    NSString *scope = [self scope:doc];
    NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"https://%@/api/doc/%@/v%@/%@?metadataonly=true", self.configuration.host, self.configuration.userName, version, doc]];
    
    return [self httpGetDocumentByUrl:url scope:scope];
}

- (NSString *)httpGetDeltaSql:(NSString *)doc version:(NSString *)version seq:(NSUInteger)seq
{
    NSString *scope = [self scope:doc];
    
    NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"https://%@/api/doc/%@/v%@/delta/%@.sql?seq=%i", self.configuration.host, self.configuration.userName, version, doc, seq]];
    
    return [self httpGetRawString:url scope:scope];
}

@end
