//
//  FileDownloader.h
//  TabReader
//
//  Created by Ian Cox on 17/02/2010.
//  Copyright 2010 Release Consultng Ltd.. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "DownloaderDelegate.h"

@interface DLTextDownloader : NSObject {
	NSString *urlString;
	NSString *outputString;
	NSString *postData;
	long long expectedContentLength;
	long long receivedLength;
	BOOL complete;
	NSError *lastError;
}

//@property (nonatomic, strong) NSString *outputString;
@property (nonatomic, strong) NSString *postData;
@property (nonatomic, assign) id<DLDownloaderDelegate> delegate;
@property (nonatomic, readonly) BOOL complete;
@property (nonatomic, readonly) NSError *lastError;
@property (nonatomic, strong) NSMutableURLRequest *request;
@property (nonatomic, strong) NSMutableData *receivedData;

-(id) initWithUrl:(NSURL *)aUrl withDelegate:(id<DLDownloaderDelegate>)aDelegate;
-(id) initWithUrl:(NSURL *)aUrl postData:(NSString *)postData withDelegate:(id<DLDownloaderDelegate>)aDelegate;

-(void) download;

@end





