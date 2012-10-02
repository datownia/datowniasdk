//
//  FileDownloader.m
//  TabReader
//
//  Created by Ian Cox on 17/02/2010.
//  Copyright 2010 Release Consultng Ltd.. All rights reserved.
//

#import "TextDownloader.h"

@implementation TextDownloader

@synthesize postData;
@synthesize receivedData;
@synthesize delegate;
@synthesize complete;
@synthesize lastError;
@synthesize request;

-(id) initWithUrl:(NSURL *)aUrl withDelegate:(id<DownloaderDelegate>)aDelegate
{
	self.delegate = aDelegate;
    
    self.request = [NSMutableURLRequest requestWithURL:aUrl
                                           cachePolicy:NSURLRequestUseProtocolCachePolicy
                                       timeoutInterval:60.0];
	
	return [self init];
}

-(id) initWithUrl:(NSURL *)aUrl postData:(NSString *)thePostData withDelegate:(id<DownloaderDelegate>)aDelegate
{
	self.postData = thePostData;
    
    self.request = [NSMutableURLRequest requestWithURL:aUrl
                                           cachePolicy:NSURLRequestUseProtocolCachePolicy
                                       timeoutInterval:60.0];
    
	return [self initWithUrl:aUrl withDelegate:aDelegate];
}


-(void) download
{
	DLog(@"starting request: %@", self.request.URL);
	
	if (postData) //for now it only supports xml bodies
	{
		//self.postData = [postData stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
		//NSData *requestData = [NSData dataWithBytes:postData length:postData.length];
		NSData *requestData = [postData dataUsingEncoding:NSUTF8StringEncoding];
		[self.request setHTTPMethod:@"POST"];
		[self.request setValue:[NSString stringWithFormat:@"text/xml"] forHTTPHeaderField:@"Content-Type"];
		[self.request setHTTPBody:requestData];
		DLog(@"post data:%@", postData);
	}
	
	NSURLConnection *theConnection = [NSURLConnection  connectionWithRequest:self.request delegate:self];
	
	if (theConnection) {
		
		// Create the NSMutableData that will hold
		
		// the received data
		
		// receivedData is declared as a method instance elsewhere
		
		receivedData=[NSMutableData data];
		
	} else {
		
		complete = YES;
		// inform the user that the download could not be made
		DLog(@"Connection could not be made");
		[delegate fileDownloadError:DownloadResult_ConnectionFailed source:self];
		
	}
}

- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response
{
	
    // this method is called when the server has determined that it
	
    // has enough information to create the NSURLResponse
	
	
	
    // it can be called multiple times, for example in the case of a
	
    // redirect, so each time we reset the data.
	
    // receivedData is declared as a method instance elsewhere
	
    [receivedData setLength:0];
	
	if ([response respondsToSelector:@selector(statusCode)])
	{
		int statusCode = [((NSHTTPURLResponse *)response) statusCode];
		if (statusCode >= 400)
		{
			[connection cancel];  // stop connecting; no more delegate messages
			NSDictionary *errorInfo
			= [NSDictionary dictionaryWithObject:[NSString stringWithFormat:
												  NSLocalizedString(@"Server returned status code %d",@""),
												  statusCode]
										  forKey:NSLocalizedDescriptionKey];
			NSError *statusError
			= [NSError errorWithDomain:NSURLErrorDomain
								  code:statusCode
							  userInfo:errorInfo];
			[self connection:connection didFailWithError:statusError];
		}
		
		//note: work-around as content-length always missing
		//expectedContentLength = [response expectedContentLength];
		NSDictionary *headers = [(NSHTTPURLResponse *)response allHeaderFields];
		NSString *lengthField = [headers objectForKey:@"Size"];
		
		if (lengthField)
			expectedContentLength = [lengthField longLongValue];
	}
	
	
}

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data
{
	
    // append the new data to the receivedData
	
    // receivedData is declared as a method instance elsewhere
	
    [receivedData appendData:data];
	
	receivedLength = [receivedData length];
	
	if (!delegate)
		return;
	
	if (expectedContentLength > 0)
	{
		float receivedFraction = receivedLength / (float)expectedContentLength;
		NSNumber *fractionNumber = [NSNumber numberWithFloat:receivedFraction]; 
		
		[delegate fileDownloadProgress:fractionNumber source:self];
	}
}

- (void)connection:(NSURLConnection *)connection
  didFailWithError:(NSError *)error
{

    // receivedData is declared as a method instance elsewhere
	
    // inform the user
	complete = YES;
	lastError = error;
	[delegate fileDownloadError:DownloadResult_HttpError source:self];

	
    DLog(@"Connection failed! Error - %i %@ %@",
		 [error code], [error localizedDescription], [[error userInfo] objectForKey:NSURLErrorFailingURLStringErrorKey]);
	
}

- (void)connectionDidFinishLoading:(NSURLConnection *)connection
{
	
    // do something with the data
	
    // receivedData is declared as a method instance elsewhere
	
    DLog(@"Succeeded! Received %d bytes of data",[receivedData length]);
	
	complete = YES;
	
	BOOL contentFound = NO;
	if ([receivedData length] > 0)	
		contentFound = YES;
	
//	outputString = [[NSString alloc] initWithData:receivedData encoding:NSUTF8StringEncoding];
    // release the connection, and the data object

	[delegate fileDownloadComplete:self contentFound:contentFound];
	
}

- (NSCachedURLResponse *)connection:(NSURLConnection *)connection willCacheResponse:(NSCachedURLResponse *)cachedResponse
{  return nil;  }


- (BOOL)connection:(NSURLConnection *)connection canAuthenticateAgainstProtectionSpace:(NSURLProtectionSpace *)protectionSpace {
	return [protectionSpace.authenticationMethod isEqualToString:NSURLAuthenticationMethodServerTrust] || 
	[protectionSpace.authenticationMethod isEqualToString:NSURLAuthenticationMethodHTTPBasic];
}

- (void)connection:(NSURLConnection *)connection didReceiveAuthenticationChallenge:(NSURLAuthenticationChallenge *)challenge {
	
	//DLog(@"%@", challenge.protectionSpace.authenticationMethod);
	
	if ([challenge.protectionSpace.authenticationMethod isEqualToString:NSURLAuthenticationMethodServerTrust])
		[challenge.sender useCredential:[NSURLCredential credentialForTrust:challenge.protectionSpace.serverTrust] forAuthenticationChallenge:challenge];
	
	
	if ([challenge.protectionSpace.authenticationMethod isEqualToString:NSURLAuthenticationMethodHTTPBasic])
		[challenge.sender useCredential:[NSURLCredential credentialWithUser:@"digiclefclient"
																   password:@"fhX6t5n"
																persistence:NSURLCredentialPersistenceForSession] forAuthenticationChallenge:challenge];
	
}
@end
