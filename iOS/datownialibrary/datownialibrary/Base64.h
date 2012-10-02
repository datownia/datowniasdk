//
//  Base64.h
//
//  Based on code found on the internet. Not sure who original author was
//

#import <Foundation/Foundation.h>


@interface Base64 : NSObject {

}

+ (NSString*) encode:(const uint8_t*) input length:(NSInteger) length;
+ (NSString*) encode:(NSData*) rawBytes;
+ (NSData*) decode:(const char*) string length:(NSInteger) inputLength;
+ (NSData*) decode:(NSString*) string;

@end
