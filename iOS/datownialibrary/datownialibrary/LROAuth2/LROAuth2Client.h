//
//  LROAuth2Client.h
//  LROAuth2Client
//
//  Created by Luke Redpath on 14/05/2010.
//  Copyright 2010 LJR Software Limited. All rights reserved.
//
// Modified to support client_credentials grant flow by Ian Cox 2012. Release Consulting Ltd

#import <UIKit/UIKit.h>
#import "LROAuth2ClientDelegate.h"

@class LROAuth2AccessToken;

@interface LROAuth2Client : NSObject {
  NSString *clientID;
  NSString *clientSecret;
  NSURL *redirectURL;
  NSURL *cancelURL;
  NSURL *userURL;
  NSURL *tokenURL;
  LROAuth2AccessToken *accessToken;
  NSMutableArray *requests;
//  id<LROAuth2ClientDelegate> delegate;
  BOOL debug;
  
 @private
  BOOL isVerifying;
}
@property (nonatomic, copy) NSString *clientID;
@property (nonatomic, copy) NSString *clientSecret;
@property (nonatomic, copy) NSURL *redirectURL;
@property (nonatomic, copy) NSURL *cancelURL;
@property (nonatomic, copy) NSURL *userURL;
@property (nonatomic, copy) NSURL *tokenURL;
@property (nonatomic, strong) LROAuth2AccessToken *accessToken;
@property (nonatomic, unsafe_unretained) id<LROAuth2ClientDelegate> delegate;
@property (nonatomic, assign) BOOL debug;

- (id)initWithClientID:(NSString *)_clientID 
                secret:(NSString *)_secret 
           redirectURL:(NSURL *)url;

- (NSURLRequest *)userAuthorizationRequestWithParameters:(NSDictionary *)additionalParameters;
- (void)verifyAuthorizationWithAccessCode:(NSString *)accessCode;
- (void)refreshAccessToken:(LROAuth2AccessToken *)_accessToken;
- (void)requestAccessTokenClientCredentials:(NSString *)scope;
@end

@interface LROAuth2Client (UIWebViewIntegration) <UIWebViewDelegate>
- (void)authorizeUsingWebView:(UIWebView *)webView;
- (void)authorizeUsingWebView:(UIWebView *)webView additionalParameters:(NSDictionary *)additionalParameters;
- (void)extractAccessCodeFromCallbackURL:(NSURL *)url;
@end
