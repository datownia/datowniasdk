//
//  DLService.m
//  datownialibrary
//
//  Created by Ian Cox on 10/09/2012.
//  Copyright (c) 2012 Release Consulting Ltd. All rights reserved.
//

#import "DLService.h"
#import "LROAuth2/LROAuth2AccessToken.h"
#import "DLTextDownloader.h"
#import <JSONKit/JSONKit.h>

@interface DLService()


@end

@implementation DLService

@synthesize configuration;
@synthesize requesting;

- (id)init
{
    self = [super init];
    if (self) {
        
    }
    return self;
}

- (id) initWithConfiguration:(DLAppConfiguration *)aConfiguration
{
    self = [self init];
    if (self) {
        self.configuration = aConfiguration;
        [self ensureClient];
    }
    return self;
}

- (void)ensureClient
{
    if (!self.configuration)
    {
        DLog(@"CONFIGURATION MISSING");
        return;
    }
    
    if (!client)
    {
        client = [[LROAuth2Client alloc] initWithClientID:self.configuration.appKey secret:self.configuration.appSecret redirectURL:nil];
        client.delegate = self;
        client.tokenURL = [NSURL URLWithString:[NSString stringWithFormat:@"https://%@/oauth2/token", self.configuration.host]];
    }
}

- (NSString *) accessTokenKeyName:(NSString *)scope
{
    return [NSString stringWithFormat:@"accessToken_%@", scope];
}


- (BOOL)requestAccessTokenIfNeeded:(NSString *)scope
{
    //try and restore access token from defaults
    //if it has not expired then use it
    id savedValue = [[NSUserDefaults standardUserDefaults] objectForKey:[self accessTokenKeyName:scope]];
    
    if (savedValue)
    {
        client.accessToken = [NSKeyedUnarchiver unarchiveObjectWithData:savedValue];
        
        if (!client.accessToken.hasExpired)
            return YES;
    }

    
    requesting = true;
    [client requestAccessTokenClientCredentials:scope];
    
    //save to documents
    
    while (requesting) {
        [[NSRunLoop currentRunLoop] runUntilDate:[[NSDate date] dateByAddingTimeInterval:2]];
    }
    
    if (client.accessToken == NULL)
    {
        DLog(@"An error must have occurred getting the token");
        return NO;;
    }
    
    
    //save the token for later use
    NSData *accessTokenAsNSData = [NSKeyedArchiver archivedDataWithRootObject:client.accessToken];
    [[NSUserDefaults standardUserDefaults] setObject:accessTokenAsNSData forKey:[self accessTokenKeyName:scope]];
    
    return YES;
}

- (NSString *)getAuth:(NSString *)scope
{
    if (!self.configuration.dbPath)
    {
        DLog(@"dbPath required in configuration");
        return nil;
    }
   
    [self requestAccessTokenIfNeeded:scope];
    
    requesting = true;
    
    NSString *auth = [NSString stringWithFormat:@"Bearer %@",client.accessToken.accessToken];
    return auth;
}

- (NSString *) httpGetRawString:(NSURL *)endpoint scope:(NSString *)scope
{
    NSData *data = [self httpGetData:endpoint scope:scope];

    return [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
}

- (NSData *) httpGetData:(NSURL *)endpoint scope:(NSString *)scope
{
    lastError = nil;
    lastScope = scope;
    
    NSString *auth = [self getAuth:scope];
    
    DLTextDownloader *downloader = [[DLTextDownloader alloc] initWithUrl:endpoint withDelegate:self];
    [downloader.request setHTTPMethod:@"GET"];
    [downloader.request setValue:self.configuration.appKey forHTTPHeaderField:@"client_id"];
    [downloader.request setValue:auth forHTTPHeaderField:@"Authorization"];
    
    [downloader download];
    
    while (requesting) {
        [[NSRunLoop currentRunLoop] runUntilDate:[[NSDate date] dateByAddingTimeInterval:2]];
    }
    
    return downloader.receivedData;
}

- (id) httpGetJson:(NSURL *)endpoint scope:(NSString *)scope
{
    return [[self httpGetData:endpoint scope:scope] objectFromJSONData];
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
    NSLog(@"wow! a token refresh");
    requesting = false;
    
}

- (void)oauthClientError:(LROAuth2Client *)client
{
    NSLog(@"auth error");
    requesting = false;
}



#pragma mark -
#pragma mark DownloaderDelegate

-(void) fileDownloadComplete:(id)source contentFound:(BOOL)contentFound
{
    requesting = false;
}

-(void) fileDownloadProgress:(NSNumber *)progressFraction source:(id)source
{
    
}

-(void) fileDownloadError:(enum DLDownloadResult) result source:(id)source
{
    requesting = false;

    lastError = [source lastError];
    
    //if unauthorised remove access token stored for this scope so that next time it will generate a new one
    if ([lastError code] == 401)
    {
        [[NSUserDefaults standardUserDefaults] removeObjectForKey:[self accessTokenKeyName:lastScope]];

    }
    
    //TODO: notification that we errored, so client can decide to requery if it wants
    
    
}

@end
