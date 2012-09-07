//
//  AppService.h
//  datownia sdk
//
//  Created by Simon McFarlane on 16/08/2012.
//  Copyright (c) 2012 Release Consulting. All rights reserved.
//

#import "DLAppService.h"
#import "LROAuth2Client.h"
#import "LROAuth2AccessToken.h"
#import "LRURLRequestOperation.h"
#import "FileDownloader.h"

@interface DLAppService()

- (void)httpGet:(NSURL *)endpoint accesstoken:(LROAuth2AccessToken *)accessToken;

@end

@implementation DLAppService

@synthesize user, appKey, appSecret;

- (void) downloadApp
{
    //TODO: call the api maker app service and download the result as a sqlite database
        //function getDatabase()
    //create oauth2 client and request a token
    LROAuth2Client *client = [[LROAuth2Client alloc] initWithClientID:self.appKey secret:self.appSecret redirectURL:nil];
    client.delegate = self;
    client.tokenURL = [NSURL URLWithString:@"https://192.168.42.211/oauth2/token"]; //@"https://apimakersandbox.cloudapp.net/oauth2/token"];
    //client.tokenURL = [NSURL URLWithString:@"https://192.168.42.11/oauth2/token"]; //@"https://apimakersandbox.cloudapp.net/oauth2/token"];
    
    NSString *scope = [NSString stringWithFormat:@"Read|app|%@", self.appKey];
    requesting = true;
    [client requestAccessTokenClientCredentials:scope];
    //save to documents
    
    while (requesting) {
        [[NSRunLoop currentRunLoop] runUntilDate:[[NSDate date] dateByAddingTimeInterval:2]];
    }

    NSAssert(client.accessToken, @"An error must have occurred getting the token");
    
    requesting = true;
    
    NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"https://192.168.42.211/api/app/%@/%@.sqlite", self.user, self.appKey]];
    //NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"https://192.168.42.11/api/app/%@/%@.sqlite", self.user, self.appKey]];
    //NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"https://apimakersandbox.cloudapp.net/api/app/%@/%@.sqlite", self.user, self.appKey]];
    
    [self httpGet:url accesstoken:client.accessToken];

    while (requesting) {
        [[NSRunLoop currentRunLoop] runUntilDate:[[NSDate date] dateByAddingTimeInterval:2]];
    }
}


- (void) synchronizeTable:(NSString *)tableName  dbPath:(NSString *)dbPath
{
    //TODO: call the delta api and synchronize the supplied table


    //get oauth2 access token (getAccessToken)
        //function getAccessToken($scope) 
    
    
    //call app service to get database (getDatabase)
        //https://apimakersandbox.cloudapp.net/api/app/leiths/bd4d9s3j8rjfnc9d33.sqlite
    
}

- (void) synchronizeDatabase:(NSString *)dbPath
{
    //TODO: call the delta api and synchronize all tables in the database
    
}

- (void)httpGet:(NSURL *)endpoint accesstoken:(LROAuth2AccessToken *)accessToken
{
    NSString *auth = [NSString stringWithFormat:@"Bearer %@",accessToken.accessToken];
    
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *pathToFile = [NSString stringWithFormat:@"%@/test.sqlite", documentsDirectory];
    
    FileDownloader *downloader = [[FileDownloader alloc] initWithUrl:endpoint downloadTo:pathToFile withDelegate:self];
    [downloader.request setHTTPMethod:@"GET"];
    [downloader.request setValue:self.appKey forHTTPHeaderField:@"client_id"];
    [downloader.request setValue:auth forHTTPHeaderField:@"Authorization"];
    
    [downloader download];
}

#pragma mark -
#pragma mark LROAuth2ClientDelegate

- (void)oauthClientDidReceiveAccessToken:(LROAuth2Client *)client
{
    NSLog(@"wow! a token");
    requesting = false;
}

- (void)oauthClientDidRefreshAccessToken:(LROAuth2Client *)client
{
    //ignore for now
}

- (void)oauthClientError:(LROAuth2Client *)client
{
    NSLog(@"auth error");
    requesting = false;
}

//#pragma mark -
//#pragma mark NSURLConnectionDownloadDelegate
//- (void)connectionDidFinishDownloading:(NSURLConnection *)connection destinationURL:(NSURL *)destinationURL
//{
//    NSLog(@"finished downloading %@", destinationURL);
//    
//    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
//	NSString *documentsDirectory = [paths objectAtIndex:0];
//    NSString *pathToFile = [NSString stringWithFormat:@"%@/test.sqlite", documentsDirectory];
//    NSURL *fileUrl = [NSURL fileURLWithPath:pathToFile];
//    
//    NSError *error = nil;
//    [[NSFileManager defaultManager] copyItemAtURL:destinationURL toURL:fileUrl error:&error ];
//    if (error)
//    {
//        NSLog(@"Error: %@", [error localizedDescription]);
//    }
//    
//    requesting = false;
//}
//
//- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data
//{  
//    [self.fileHandle writeData:data];
//	
//	receivedLength += [data length];
//
//	}
//}
//
//- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error
//{
//    NSLog(@"failed downloading");
//    [self.fileHandle closeFile];
//    
//    //delete temp file
//    [self deleteFileAtPath:[self tempFileName]];
//    
//    requesting = false;
//}

#pragma mark -
#pragma mark DownloaderDelegate

-(void) fileDownloadComplete:(id)source contentFound:(BOOL)contentFound
{
    requesting = false;
}

-(void) fileDownloadProgress:(NSNumber *)progressFraction source:(id)source
{

}

-(void) fileDownloadError:(enum DownloadResult) result source:(id)source
{
    requesting = false;
}
@end
