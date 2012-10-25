datownia sdk
============

This repository contains the datownia SDK and code samples showing how to use datownia without the SDK.

API Information Guide
=====================

This document contains information for using the API’s that API Maker generates from the source
files uploaded by the Publishers (data owners)

1. Overview

Each API is constructed from a source csv file and “wrapped” in JSON, where the:
• First row (column headings) of the csv file become the field names in the API
• Remaining rows of the csv file become the data in the API

Each API contains metadata:
• File header type information such as version, timestamps, row count…etc
• Data record type information such field name list, field order…etc

All API’s are RESTful and use common API practices such as offset and limit parameters for paging.

All API’s use OAuth 2 for authentication purposes and will require OAuth 2 client integration to be
used when calling the API. Authentication keys are required to verify each API call, these will be
provided when registering an approved application.

Each application requires only one set of keys and those will cover all the API’s that have been
approved for use by that application.

Note: There is currently a limit of 200 for the number of data rows that can be returned with a single
request. Therefore, multiple requests may be needed in order to fulfil a particular function in an
application

2. API URL Format

The URL of the API is formatted as follows:

https://apimaker.releasemobile.com/api/doc/publisher/version/filename

Publisher

Version

Filename

Name of the organisation that is publishing the data in the API

Version number of the file – format = v1

The version number will increment when a structure change has been made
to the API such as new or removed fields

The data publisher will be responsible for notifying developers of new API
version numbers

The name of the source data file that was used to create the API

Will usually be meaningful and describe the data that is in the API

This is the same name that appears on the dataset list in the data publishers
branded API website

3. API Metadata

API Maker uses the following metadata fields to describe an API

Field
_id
_rev
fieldList

fieldListDocOrder
name
published
fileSize
fileCreated
fileModified
fileRevision
fileName

rows
contentChecksum
timestamp
seq

root
apiVersion
type

limit

offset

pageNumber

pageCount

numRowsInContents

Meaning
Filename and version
Internal checksum of file
Lists the API fields in the order that they appear in the API.
The columns in the source file are alphabetically sorted when the API
is created
Lists the API fields in the original column order of the source file
Friendly name of an API
Ignore – internal use only
Size of the original source file in it’s original format
The date and timestamp of when an API was first created
The date and timestamp of when an API was last updated
Ignore – internal use only
The name and extension of the original source file used to create an
API
The number of data rows in an API
Ignore – internal use only
The date and timestamp of the last time anything changed for an API
The latest delta sequence number.
This is to be used in conjunction with the Delta API described below
in section 5
Ignore – internal use only
The version number of an API
The type of source file used to generate and API
Currently set to “document” in this version of API Maker
The number of rows returned in an API Method
This will be based on:
• A limit that was specified when making the original call
• The actual number of rows in an API if less than 200
• The default maximum of 200 if no limit specified and more than
200 data rows in an API
The starting point row number of the returned results
This will be the same as specified on the original call or zero if not
specified
The number of pages in an API based on the limit specified or
defaulted.
EG. pageNumber = 5 if API contains 1000 rows and limit = 200
The current page number within an API based on limit and offset
EG pageCount = 3 if API contains 1000 rows
and limit = 200 and offset = 400
The number of rows returned in the result contents array for an API
method request

4. API Methods

Each API supports the following methods

Method
Metadata

Parameter
?metadataonly = y

Sample Data

?sampledata = y

Search By Field Value

Search By Field Range

Paged Query

?field = a
&value = x

?field = a
&from = x
&to = y

?offset = x
&limit = y

Purpose
Returns the metadata of an API,
- Header information
- Field name list (original order and doc order)

Returns the first two rows of data along with the
metadata of an API

Returns rows of data from an API where a field
name equals a particular value

Returns rows of data from an API where a field
name value is within a particular range

Delta API

?offset = x
&limit = y
&seq = z

Returns a subset of data from an API
Data row start point specified by offset
Number of rows returned specified by limit

Contains a list of all the changes that have been
to the data in an API.
Section 5 of this document has detailed
information on how this works

5. Delta API

Some applications keep a local cached copy of the data accessed from an API. This means that
any changes made to the data in an API may need to be applied to those local copies. API Maker
provides a “Delta API” to support making those changes.

The Delta API contains a list of every new or removed data row for an API since it was first published.
A change to a data row will be represented as a removal of the row with the old data values and an
addition of a data row with the new values.

The Delta API is ordered in the sequence that the changes were applied to an API and identified
by a sequence number “seq”. This sequence number should be used when calling the API (&seq
parameter) to return the correct set of Delta API records that need to be applied.

API Maker uses an internal key “_id” to uniquely identify each row of data in an API. This key is also
used in the Delta API. The Delta API will identify data rows to be deleted by “_id” only.

The Delta API can return either JSON or a set of SQL statements in the SQlite download (see section
7 below).

To access the Delta API the name “delta” needs to be added to the path in the API Maker URL as
follows:

https://apimaker.releasemobile.com/api/doc/publisher/version/delta/filename

5. Delta API (JSON Example)

/api/doc/customer99/v1/delta/testdocument1?seq=0 (give me all the deltas since seq 0)
returns:
[
{
"_id": "e51431cc-0114-4ea6-bca1-306b3f755771_1.0_delta_1",
"_rev": "1-68f3a51785506ce2e4f1807dc13d9948",
"type": "delta",
"parent": "e51431cc-0114-4ea6-bca1-306b3f755771_1.0",
"seq": 1,
"action": "delete",
"data": "a6a8580a6619cb36205d0a2c7b037be9"
},
{
"_id": "e51431cc-0114-4ea6-bca1-306b3f755771_1.0_delta_2",
"_rev": "1-541c1d9f71ebae277d1b04fe9ca64627",
"type": "delta",
"parent": "e51431cc-0114-4ea6-bca1-306b3f755771_1.0",
"seq": 2,
"action": "insert",
"data": {
"name": "Battery",
"sku": 328143,
"stock level": 10,
"warehouse": "york",
"_id": "f83b140aa934e650febeec76cb2c53f7"
}
}
]
IF action = “delete” THEN the data is the “_id “of the row that needs deleting
IF action = “insert” THEN the data is the data for the row that needs inserting

6. Error Return Codes

Error Code
200
400
401
404
500

Error Reason
Okay
Problem with request – in terms of bad method call
Not authorised – current version of API returns 400 for this (will be fixed later)
Not Found
Problem with request – in terms of temporal issue, may be resolved by repeat

7. SQlite Download

The data in an API can be downloaded as SQlite instead of JSON, this is done by appending “sqlite”
to the filename in the API Maker URL as follows:

https://apimaker.releasemobile.com/api/doc/publisher/version/filename.sqlite

The returned database will contain a table named the same as the filename with the version number
appended. The primary key of the table is the “_id” field