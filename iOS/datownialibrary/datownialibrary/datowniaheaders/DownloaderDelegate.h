//
//  FileDownloader.h
//  TabReader
//
//  Created by Ian Cox on 17/02/2010.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>

enum DownloadResult {
	DownloadResult_Success, 
	DownloadResult_ConnectionFailed, 
	DownloadResult_HttpError, 
	DownloadResult_NotFound,
	DownloadResult_NotAuthorised
};

@protocol DownloaderDelegate <NSObject> 

-(void) fileDownloadComplete:(id)source contentFound:(BOOL)contentFound;
-(void) fileDownloadProgress:(NSNumber *)progressFraction source:(id)source;
-(void) fileDownloadError:(enum DownloadResult) result source:(id)source;

@end