//
//  FileDownloader.h
//  TabReader
//
//  Created by Ian Cox on 17/02/2010.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "DownloaderDelegate.h"

@interface FileDownloader : NSObject {
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
@property (nonatomic, unsafe_unretained) id<DownloaderDelegate> delegate; //weak
@property (nonatomic, readonly) BOOL complete;
@property (weak, nonatomic, readonly) NSError *lastError;
@property (nonatomic, strong) NSMutableURLRequest *request;

-(id) initWithUrl:(NSURL *)aUrl downloadTo:(NSString *)aOutputPath withDelegate:(id<DownloaderDelegate>)aDelegate;
-(void) download;
-(void) deleteFileAtPath:(NSString *)filePath; 

@end





