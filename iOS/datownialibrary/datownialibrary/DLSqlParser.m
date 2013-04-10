//
//  DLSqlParser.m
//  datownialibrary
//
//  Created by Ian Cox on 10/04/2013.
//  Copyright (c) 2013 datownia. All rights reserved.
//

#import "DLSqlParser.h"

@implementation DLSqlParser

- (NSMutableArray *)parse:(NSString *)sql
{
    NSMutableArray *sqlLines = [NSMutableArray array];
    
    int apostropheCount = 0;
    int thisLineCharCount = 0;

    int sqlLen = [sql length];
    for (int i=0;i<sqlLen;i++) //loop through every character in the sql.
    {
        unichar ch;
        ch = [sql  characterAtIndex:i];

        thisLineCharCount++;
        
        if (ch == '\'') //apostrophe
        {
            apostropheCount++;
        }

        if ((ch == ';') && (apostropheCount % 2 == 0))
        {
            //we found where to split
            
            //save all the characters so far into a string
            NSString *newLine = [sql substringWithRange:NSMakeRange(i-(thisLineCharCount-1), thisLineCharCount)];
            
            //if step over cr lf newline characters
            unichar nextChar = [sql  characterAtIndex:i+1 ];
            if (nextChar == '\r')
            {
                i++;
                nextChar = [sql  characterAtIndex:i+1 ];
            }
            
            if (nextChar == '\n')
            {
                i++;
            }

            [sqlLines addObject:newLine];

            //reset count for the next line.
            thisLineCharCount = 0;

        }
    }

    return sqlLines;
}

@end
