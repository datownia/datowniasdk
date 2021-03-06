//
//  FileDownloader.h
//  Created by Ian Cox on 17/02/2010.
//  Copyright 2010 Release Consulting. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "DownloaderDelegate.h"

@interface DLFileDownloader : NSObject {
	NSString *urlString;
	NSString *outputPath;
//	NSMutableData *receivedData;
	long long expectedContentLength;
	long long receivedLength;
	//id<DownloaderDelegate> delegate;
	BOOL complete;
	NSError *__weak lastError;
}

@property (nonatomic, strong) NSURL *url;
@property (nonatomic, strong) NSString *outputPath;
@property (nonatomic, assign) id<DLDownloaderDelegate> delegate; //weak
@property (nonatomic, readonly) BOOL complete;
@property (weak, nonatomic, readonly) NSError *lastError;
@property (nonatomic, strong) NSMutableURLRequest *request;

-(id) initWithUrl:(NSURL *)aUrl downloadTo:(NSString *)aOutputPath withDelegate:(id<DLDownloaderDelegate>)aDelegate;
-(void) download;
-(void) deleteFileAtPath:(NSString *)filePath; 

@end





