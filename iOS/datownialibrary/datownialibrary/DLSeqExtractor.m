//
//  DLSeqExtractor.m
//  datownialibrary
//
//  Created by RCL Admin on 16/04/2013.
//  Copyright (c) 2013 datownia. All rights reserved.
//

#import "DLSeqExtractor.h"

@implementation DLSeqExtractor

BOOL stringIsNumeric(NSString *str) {
    NSNumberFormatter *formatter = [[NSNumberFormatter alloc] init];
    NSNumber *number = [formatter numberFromString:str];

    return !!number; // If the string is not numeric, number will be nil
}

- (int) extract:(NSString *)sqlLine
{
    NSRange lastCommaRange = [sqlLine rangeOfCharacterFromSet:[NSCharacterSet characterSetWithCharactersInString:@","] options:NSBackwardsSearch];
    if (lastCommaRange.location == NSNotFound)
        return NSNotFound;
    
    NSRange lastBracketRange = [sqlLine rangeOfCharacterFromSet:[NSCharacterSet characterSetWithCharactersInString:@")"] options:NSBackwardsSearch];
    if (lastBracketRange.location == NSNotFound)
        return NSNotFound;

    int location = lastCommaRange.location + 1;
    if (location >= sqlLine.length)
        return NSNotFound;
    
    int length = lastBracketRange.location - lastCommaRange.location - 1;
    if (length < 0)
        return NSNotFound;
    
    NSRange seqRange;
    seqRange.location = location;
    seqRange.length = length;
    
    NSString *seqString = [[sqlLine substringWithRange:seqRange] stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
    
    if (!stringIsNumeric(seqString))
        return NSNotFound;
    
    int seq = [seqString intValue];
    
    return seq;
}
@end
