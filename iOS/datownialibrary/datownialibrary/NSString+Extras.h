//
//  NSString+.h
//  TabReader
//
//  Created by Ian Cox on 12/10/2010.
//  Copyright 2010 Release Consulting Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSString (Extras)

- (BOOL) endsWith:(NSString *)end;
- (BOOL) startsWith:(NSString *)start;
- (NSString *) fileNameMinusExt;
-(NSString*) stringWithSentenceCapitalization;
+ (NSString *) stringByConvertingToSafeSql:(NSString *)stringToConvert;
+ (NSString *) stringByEscapingSql:(NSString *)theString;
- (UIFont *) fontToMakeTextFitInSize:(CGSize)constrainedSize font:(UIFont *)font;
+ (NSString *) nullToEmpty:(NSString *)string;
- (NSString *)flattenHtml;

@end
