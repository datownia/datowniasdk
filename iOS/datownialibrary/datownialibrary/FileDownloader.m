//
//  FileDownloader.h
//  Created by Ian Cox on 17/02/2010.
//  Copyright 2010 Release Consulting. All rights reserved.
//

#import "FileDownloader.h"
#import "Base64.h"
#import "ExcludeFromBackupKey.h"

@interface FileDownloader()

@property (nonatomic, strong) NSFileHandle *fileHandle;
//@property (nonatomic, retain) NSDate *lastProgressDate;

@end

@implementation FileDownloader

@synthesize url;
@synthesize outputPath;
@synthesize delegate;
@synthesize complete, lastError, fileHandle;
@synthesize request;

-(id) initWithUrl:(NSURL *)aUrl downloadTo:(NSString *)aOutputPath withDelegate:(id<DownloaderDelegate>)aDelegate
{
	self.url = aUrl;
	self.delegate = aDelegate;
	self.outputPath = aOutputPath;
    
    self.request = [NSMutableURLRequest requestWithURL:url
                                                              cachePolicy:NSURLRequestUseProtocolCachePolicy
                                                          timeoutInterval:60.0];
	
	return [super init];
}

//-(void) dealloc
//{
//	[urlString release];
//	[outputPath release];
//	[lastError release];
//    [fileHandle release];
//	//[lastProgressDate release];
//	[super dealloc];
//}

- (NSString *) tempFileName
{
    return [NSString stringWithFormat:@"%@.tmp",outputPath];
}

-(void) deleteFileAtPath:(NSString *)filePath
{
    NSError *error;
    
    // Create file manager
    NSFileManager *fileMgr = [NSFileManager defaultManager];
    
//    // Point to Document directory
//    NSString *documentsDirectory = [NSHomeDirectory() 
//                                    stringByAppendingPathComponent:@"Documents"];
    
    if ([fileMgr removeItemAtPath:filePath error:&error] != YES)
    {
        NSLog(@"Unable to delete file: %@", [error localizedDescription]);
    }
    else
    {
        NSLog(@"!!! .tmp file was deleted successfully !!!");
    }
}

-(void) download
{
	NSURL *serviceUrl = [NSURL URLWithString:urlString];
	
	


//	NSString *userPass = @"Basic digiclefclient:fhX6t5n";
//	NSData* data = [userPass dataUsingEncoding:NSUTF8StringEncoding];
//	NSString *encodedUserPass = [Base64 encode:data];					  
//	[theRequest addValue:encodedUserPass forHTTPHeaderField:@"Authorization"];

	//send credentials with request
	//note: this isn't working and we are handling the authentication challenge to send credentials
	//NSURLCredential *credential = [NSURLCredential credentialWithUser:@"digiclefclient"
//															 password:@"fhX6t5n"
//														  persistence:NSURLCredentialPersistenceForSession];
//	
//	NSURLProtectionSpace *protectionSpace = [[NSURLProtectionSpace alloc]
//											 initWithHost:[serviceUrl host]
//											 port:0
//											 protocol:@"https"
//											 realm:nil
//											 authenticationMethod:NSURLAuthenticationMethodHTTPBasic];
//	
//	
//	[[NSURLCredentialStorage sharedCredentialStorage]  setDefaultCredential:credential
//														forProtectionSpace:protectionSpace];

	NSLog(@"starting request: %@", serviceUrl);
    //self.lastProgressDate = [NSDate date];
    
	NSURLConnection *theConnection = [NSURLConnection  connectionWithRequest:self.request delegate:self];
	
	if (theConnection) {
		
		// Create the NSMutableData that will hold
		// the received data
		
		// receivedData is declared as a method instance elsewhere

        
        //TODO: delete temp file
        //delete the temp file if it already exists
        [self deleteFileAtPath:[self tempFileName]]; //Simon
        
        NSLog(@"Creating temp download file: %@", [self tempFileName]);
        
        [[NSFileManager defaultManager] createFileAtPath:[self tempFileName] contents:nil attributes:nil];
        self.fileHandle = [NSFileHandle fileHandleForWritingAtPath:[self tempFileName]];
//		receivedData=[[NSMutableData data] retain];
		
	} else {
		
		// inform the user that the download could not be made
		NSLog(@"Connection could not be made");
		complete = YES;
		[delegate fileDownloadError:DownloadResult_ConnectionFailed source:self];
		
	}
}

- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response
{
	NSHTTPURLResponse *httpResponse =
    (NSHTTPURLResponse *)response;
    NSLog(@"%@", [httpResponse allHeaderFields]);
    
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
	
    //[receivedData appendData:data];
    
    [self.fileHandle writeData:data];
	
	receivedLength += [data length];
	
	if (!delegate)
		return;
	
	if (expectedContentLength > 0)
	{
        //if ([[NSDate date] timeIntervalSinceDate:self.lastProgressDate] > 1)
        //{
            float receivedFraction = receivedLength / (float)expectedContentLength;
            NSLog(@"receivedFraction: %f", receivedFraction);
            NSNumber *fractionNumber = [NSNumber numberWithFloat:receivedFraction]; 
            
            [delegate fileDownloadProgress:fractionNumber source:self];
        //}
		
        
        //self.lastProgressDate = [NSDate date];
	}
}

- (void)connection:(NSURLConnection *)connection
  didFailWithError:(NSError *)error
{

    // receivedData is declared as a method instance elsewhere
	
//    [receivedData release];
	[self.fileHandle closeFile];
    
    //delete temp file
    [self deleteFileAtPath:[self tempFileName]];
    
    // inform the user
	complete = YES;
	lastError = error;
	[delegate fileDownloadError:DownloadResult_HttpError source:self];

    NSLog(@"Connection failed! Error - %i %@ %@",
		 [error code], [error localizedDescription], [[error userInfo] objectForKey:NSURLErrorFailingURLStringErrorKey]);
	
}

- (void)connectionDidFinishLoading:(NSURLConnection *)connection
{
	
    // do something with the data
	
    // receivedData is declared as a method instance elsewhere
	
    
	
	//if was no changes since last date then stream will be empty
	BOOL contentFound = NO;
	if (receivedLength > 0)	
	{
        [self.fileHandle closeFile];
		//DLog(@"Succeeded! Received %d bytes of data",[receivedData length]);
//		[receivedData writeToFile:outputPath atomically:YES];
        
        //if the file already exists in the documents directory - e.g. the normal pack has been downloaded and we are now downloading the pack plus...
        
        NSError *error2 = nil;
        if ([[NSFileManager defaultManager] fileExistsAtPath: outputPath])
        {
            [[NSFileManager defaultManager] removeItemAtPath: outputPath error: &error2];
        }
        
        //copy temp file to output file
        NSError *error = nil;
        [[NSFileManager defaultManager] copyItemAtPath:[self tempFileName] toPath:outputPath error:&error ];
        
        //TODO: since the download has finished, delete temp file as we no longer have any use for it.
        [self deleteFileAtPath:[self tempFileName]];
        
        if (error)
        {
            NSLog(@"error copying temp file to output file: %@", outputPath);
            [delegate fileDownloadError:DownloadResult_HttpError source:self];
            return;
        }

		contentFound = YES;

        NSURL *itemFileURL = [NSURL fileURLWithPath:outputPath];
        [ExcludeFromBackupKey addSkipBackupAttributeToItemAtURL:itemFileURL];
	}
	else 
	{
		NSLog(@"Zero byte download ! Received %lld bytes of data",receivedLength);
	}

	[delegate fileDownloadComplete:self contentFound:contentFound];

    complete = YES;
	// release the connection, and the data object
//    [receivedData release];
	
	
	
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
