//
//  macro.h
//  datownialibrary
//
//  Created by Ian Cox on 07/09/2012.
//  Copyright (c) 2012 datownia. All rights reserved.
//

#ifndef datownialibrary_macro_h
#define datownialibrary_macro_h

#define STRINGIZE(x) #x
#define STRINGIZE2(x) STRINGIZE(x)
#define TESTAPPLITERAL @ STRINGIZE2(TESTAPP)
#define TESTAPPKEYLITERAL @ STRINGIZE2(TESTAPPKEY)
#define TESTAPPSECRETLITERAL @ STRINGIZE2(TESTAPPSECRET)

#endif
