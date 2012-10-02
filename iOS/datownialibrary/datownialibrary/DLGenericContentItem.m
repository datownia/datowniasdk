//
//  Created by iancox on 27/02/2012.
//
// To change the template use AppCode | Preferences | File Templates.
//


#import "DLGenericContentItem.h"


@implementation DLGenericContentItem {

@private
    NSString *_row;
}

@synthesize row = _row;

- (NSString *) displayTitle
{
    return self.rowId;
}

- (NSString *) displayDetail
{
    NSMutableString *result = [NSMutableString string];
    for (NSString *key in self.row) {
        [result appendFormat:@"%@ = %@ \n", key, [self.row objectForKey:key]];
    }

    return result;
}


@end