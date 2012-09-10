//
//  NSString+.m
//  TabReader
//
//  Created by Ian Cox on 12/10/2010.
//  Copyright 2010 Release Consulting Ltd. All rights reserved.
//

#import "NSString+Extras.h"


@implementation NSString (Extras)

- (BOOL) endsWith:(NSString *)end
{
	NSString *endBit = [self substringFromIndex:self.length - end.length];
	
	if ([endBit isEqualToString:end])
		return YES;
	
	return NO;
	
}

- (BOOL) startsWith:(NSString *)start
{
	if (self.length == 0 || !start || start.length > self.length)
		return NO;
	
	NSString *startBit = [self substringToIndex:start.length];
	
	if ([startBit isEqualToString:start])
		return YES;
	
	return NO;
	
}

- (NSString *) fileNameMinusExt
{
	NSString *lastPathComponent = [self lastPathComponent];
	
	NSRange dotRange = [lastPathComponent rangeOfString:@"." options:NSBackwardsSearch];
    
    if (dotRange.location == NSNotFound)
        return self;
    
	return [lastPathComponent substringToIndex:dotRange.location];
		
}

+ (NSString *) stringByConvertingToSafeSql:(NSString *)stringToConvert
{
	return stringToConvert == nil ? @"null" : [NSString stringWithFormat:@"'%@'", [self stringByEscapingSql:stringToConvert]];
}

+ (NSString *) stringByEscapingSql:(NSString *)theString
{
	return [theString stringByReplacingOccurrencesOfString:@"'" withString:@"''"];
}

-(NSString*) stringWithSentenceCapitalization
{
	
	
	NSString *firstCharacterInString = [[self substringToIndex:1] capitalizedString];
	NSString *sentenceString = [self stringByReplacingCharactersInRange:NSMakeRange(0,1) withString: firstCharacterInString];
	
	
	return sentenceString;
}

- (UIFont *) fontToMakeTextFitInSize:(CGSize)constrainedSize font:(UIFont *)font
{
	UIFont *newFont = font;

	for(int i = 28; i > 10; i=i-2)
	{
		// Set the new font size.
		newFont = [newFont fontWithSize:i];
		// You can log the size you're trying: NSLog(@"Trying size: %u", i);
		
		/* This step is important: We make a constraint box 
		 using only the fixed WIDTH of the UILabel. The height will
		 be checked later. */ 
		CGSize constraintSize = CGSizeMake(constrainedSize.width, MAXFLOAT);
		
		// This step checks how tall the label would be with the desired font.
		CGSize labelSize = [self sizeWithFont:newFont constrainedToSize:constraintSize lineBreakMode:UILineBreakModeWordWrap];
		
		/* Here is where you use the height requirement!
		 Set the value in the if statement to the height of your UILabel
		 If the label fits into your required height, it will break the loop
		 and use that font size. */
//        DLog(@"%@  - label size %@, constrainedSize %@", self, NSStringFromCGSize(labelSize),  NSStringFromCGSize(constrainedSize));
        
        //hack :for some reason althugh sizeWithFont indicates its ok if the text is within a certain size of max then it has a high chance of having a long word that wraps
        if (constrainedSize.width - labelSize.width < 15 && (labelSize.height/ constrainedSize.height) > 0.8f)
            continue;
        
		if ((labelSize.height <= constrainedSize.height))
			break;
        
	}
	
	return newFont;
}

+ (NSString *) nullToEmpty:(NSString *)string
{
    if (!string)
        return @"";
    
    return string;
}

- (NSString *)flattenHtml
{
    NSString *html = self;
    NSScanner *theScanner = [NSScanner scannerWithString:html];

    NSString *text = nil;

    while ([theScanner isAtEnd] == NO) {

        // find start of tag
        [theScanner scanUpToString:@"<" intoString:nil] ;

        // find end of tag
        [theScanner scanUpToString:@">" intoString:&text] ;

        // replace the found tag with a space
        //(you can filter multi-spaces out later if you wish)
        html = [html stringByReplacingOccurrencesOfString:
                [ NSString stringWithFormat:@"%@>", text]
                                               withString:@" "];

    } // while //

    return html;
}
@end
