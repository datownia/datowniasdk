//
//  FileDownloader.h
//  TabReader
//
//  Created by Ian Cox on 17/02/2010.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "DownloaderDelegate.h"

@interface TextDownloader : NSObject {
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
@property (nonatomic, assign) id<DownloaderDelegate> delegate;
@property (nonatomic, readonly) BOOL complete;
@property (nonatomic, readonly) NSError *lastError;
@property (nonatomic, strong) NSMutableURLRequest *request;
@property (nonatomic, strong) NSMutableData *receivedData;

-(id) initWithUrl:(NSURL *)aUrl withDelegate:(id<DownloaderDelegate>)aDelegate;
-(id) initWithUrl:(NSURL *)aUrl postData:(NSString *)postData withDelegate:(id<DownloaderDelegate>)aDelegate;

-(void) download;

@end





