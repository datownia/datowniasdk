//
//  FileDownloader.h
//
//  Created by Ian Cox on 17/02/2010.
//  Copyright 2010 Release Consulting Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>

enum DLDownloadResult {
	DLDownloadResult_Success, 
	DLDownloadResult_ConnectionFailed, 
	DLDownloadResult_HttpError, 
	DLDownloadResult_NotFound,
	DLDownloadResult_NotAuthorised
};

@protocol DLDownloaderDelegate <NSObject> 

-(void) fileDownloadComplete:(id)source contentFound:(BOOL)contentFound;
-(void) fileDownloadProgress:(NSNumber *)progressFraction source:(id)source;
-(void) fileDownloadError:(enum DLDownloadResult) result source:(id)source;

@end