##API Usage Quickstart

To get going really quickly you can use HTTP basic authentication and the [CURL](http://curl.haxx.se) command.

### Get first 5 records in a dataset:
```
curl "https://b317eac00b:5156a8e80e@www.datownia.com/api/doc/example/v1/willstoyscatalogue/catalogue?offset=0&limit=5"
```

returns
```json
{
    "_id": "willstoyscatalogue^~^catalogue_1.0",
    "_rev": "4-cc64a12b2c7b681b140b5bac0b50f76f",
    "fieldListDocOrder": [
        "UPC",
        "Product name",
        "Product Description",
        "Image URL",
        "Stock level",
        "Price",
        "Address of Store",
        "Geocode of store",
        "Available for pick up"
    ],
    "fieldList": [
        "Address of Store",
        "Available for pick up",
        "Geocode of store",
        "Image URL",
        "Price",
        "Product Description",
        "Product name",
        "Stock level",
        "UPC"
    ],
    "name": "willstoyscatalogue^~^catalogue",
    "published": true,
    "fileSize": 0,
    "fileCreated": "2012-11-06 15:11:02Z",
    "fileModified": "2012-11-19 14:42:44Z",
    "fileRevision": "260af68265",
    "fileName": "willstoyscatalogue",
    "rows": 12,
    "contentChecksum": "394f90f6f444bf77f7a3c64ce2af2273",
    "timestamp": "2012-11-19 14:42:44Z",
    "seq": 2,
    "root": "willstoyscatalogue^~^catalogue",
    "apiVersion": "1.0",
    "type": "document",
    "limit": 5,
    "offset": 0,
    "pageNumber": 1,
    "pageCount": 3,
    "numRowsInContents": 5,
    "contents": [
        [
            "373 Vineyard Drive, Mayfield Heights, OH 44124",
            "yes",
            "41.475136, -81.369677",
            "http://www.wills-toys.com/images/abd.jpg",
            54.99,
            "Five activation points start the excitement all over the Batcave",
            "Fisher-Price Imaginext Bat Cave",
            10,
            2311835075,
            "d4203af75b2ee5a431e3e4306d931caf"
        ],
        [
            "373 Vineyard Drive, Mayfield Heights, OH 44124",
            "yes",
            "41.475136, -81.369677",
            "http://www.wills-toys.com/images/cde.jpg",
            17.99,
            "New sonic screwdriver is larger than previous ones",
            "Dr. Who: The Eleventh Doctor's Sonic Screwdriver",
            4,
            1349876583,
            "b49bc161e3f4f03e07d49e4264be6dd0"
        ],
        [
            "2178 Thompson Drive, Oakland, CA 94601",
            "yes",
            "37.821722, -122.208125",
            "http://www.wills-toys.com/images/fdg.jpg",
            11.99,
            "Hollywood has never seen a spud stud like this before",
            "Playskool Toy Story 3 Classic Mr. Potato Head",
            24,
            8477853853,
            "eaf1f697f73ee25318dff424dd5d8bdc"
        ],
        [
            "2178 Thompson Drive, Oakland, CA 94601",
            "yes",
            "37.821722, -122.208125",
            "http://www.wills-toys.com/images/das.jpg",
            17.99,
            "Prepare for battle just like the Iron Man character does",
            "Iron Man Arc Chest Light",
            5,
            1374737813,
            "46504e6061a35d8e25f9792d5a0aea6c"
        ],
        [
            "2178 Thompson Drive, Oakland, CA 94601",
            "yes",
            "37.821722, -122.208125",
            "http://www.wills-toys.com/images/4gf.jpg",
            69.99,
            "Minecraft was the fastest growing online game in 2012",
            "LEGO Minecraft 21102",
            12,
            9583638582,
            "d3e5e17d78d86eb6a05290771d1fcdbe"
        ]
    ]
}
```

Note: in this example app key = b317eac00b and app secret = 5156a8e80e . In a real world use you will have your own app key and secret

You may also use OAuth 2 authentication with a client credentials grant, using your app key and secret.

### Search a dataset:
```
curl "https://b317eac00b:5156a8e80e@www.datownia.com/api/doc/example/v1/willstoyscatalogue/catalogue" -G --data-urlencode "q=price:[10.99 TO 20.00] AND address\ of\ store:oakland&offset=0&limit=5"
```

returns
```json
{
    "_id": "willstoyscatalogue^~^catalogue_1.0",
    "_rev": "4-cc64a12b2c7b681b140b5bac0b50f76f",
    "fieldListDocOrder": [
        "UPC",
        "Product name",
        "Product Description",
        "Image URL",
        "Stock level",
        "Price",
        "Address of Store",
        "Geocode of store",
        "Available for pick up"
    ],
    "fieldList": [
        "Address of Store",
        "Available for pick up",
        "Geocode of store",
        "Image URL",
        "Price",
        "Product Description",
        "Product name",
        "Stock level",
        "UPC"
    ],
    "name": "willstoyscatalogue^~^catalogue",
    "published": true,
    "fileSize": 0,
    "fileCreated": "2012-11-06 15:11:02Z",
    "fileModified": "2012-11-19 14:42:44Z",
    "fileRevision": "260af68265",
    "fileName": "willstoyscatalogue",
    "rows": 12,
    "contentChecksum": "394f90f6f444bf77f7a3c64ce2af2273",
    "timestamp": "2012-11-19 14:42:44Z",
    "seq": 2,
    "root": "willstoyscatalogue^~^catalogue",
    "apiVersion": "1.0",
    "type": "document",
    "limit": 200,
    "offset": 0,
    "pageNumber": 1,
    "pageCount": 1,
    "numRowsInContents": 3,
    "contents": [
        [
            "2178 Thompson Drive, Oakland, CA 94601",
            "yes",
            "37.821722, -122.208125",
            "http://www.wills-toys.com/images/g44.jpg",
            12.99,
            "Classic Jenga now in an easy \"put away\" package for easy clean up",
            "Jenga",
            2,
            5447386443,
            "9528237b5b8d2c9d2cdfb46a4a0cb0cf"
        ],
        [
            "2178 Thompson Drive, Oakland, CA 94601",
            "yes",
            "37.821722, -122.208125",
            "http://www.wills-toys.com/images/fdg.jpg",
            11.99,
            "Hollywood has never seen a spud stud like this before",
            "Playskool Toy Story 3 Classic Mr. Potato Head",
            24,
            8477853853,
            "eaf1f697f73ee25318dff424dd5d8bdc"
        ],
        [
            "2178 Thompson Drive, Oakland, CA 94601",
            "yes",
            "37.821722, -122.208125",
            "http://www.wills-toys.com/images/das.jpg",
            17.99,
            "Prepare for battle just like the Iron Man character does",
            "Iron Man Arc Chest Light",
            5,
            1374737813,
            "46504e6061a35d8e25f9792d5a0aea6c"
        ]
    ]
}
```

### Get entire dataset as a sqlite database
```
curl "https://b317eac00b:5156a8e80e@www.datownia.com/api/doc/example/v2/willstoyscatalogue/catalogue.sqlite" > test.sqlite
```
This will create a sqlite db file with two tables.
1. example/willstoyscatalogue/catalogue_2.0. This contains your dataset
2. table_def.  This tracks the last seq number for each table in the database. This can be used to call the delta api passing in the seq value in this table

### Keep your database up to date with the delta api
```
curl "https://b317eac00b:5156a8e80e@www.datownia.com/api/doc/example/v2/delta/willstoyscatalogue/catalogue?seq=0"
```

returns
```json
[
    {
        "_id": "willstoyscatalogue^~^catalogue_2.0_delta_1",
        "_rev": "1-edc64811fd6ce7e92a12f48b52d3f587",
        "type": "delta",
        "parent": "willstoyscatalogue^~^catalogue_2.0",
        "seq": 1,
        "action": "insert",
        "data": [
            "373 Vineyard Drive, Mayfield Heights, OH 44124",
            "yes",
            "Five activation points start the excitement all over the Batcave",
            "41.475136, -81.369677",
            "http://www.wills-toys.com/images/abd.jpg",
            "Fisher-Price Imaginext Bat Cave",
            50.99,
            10,
            2311835075,
            "950739696db9aa48dfd37c06cbfe3c46"
        ]
    },
    {
        "_id": "willstoyscatalogue^~^catalogue_2.0_delta_2",
        "_rev": "1-eaf91d9f27691a2844e1e73c96cf2472",
        "type": "delta",
        "parent": "willstoyscatalogue^~^catalogue_2.0",
        "seq": 2,
        "action": "insert",
        "data": [
            "373 Vineyard Drive, Mayfield Heights, OH 44124",
            "no",
            "This mesmerizing maze of safe, soft, continuous tubes is perfect for a teething baby to chew on",
            "41.475136, -81.369677",
            "http://www.wills-toys.com/images/gr2.jpg",
            "Manhattan Toy Winkel",
            11.5,
            10,
            9054297547,
            "4c7ec7f51c7eda0f1e854a240436e210"
        ]
    },
    {
        "_id": "willstoyscatalogue^~^catalogue_2.0_delta_3",
        "_rev": "1-a49898ede6670445033d49e45239ff6c",
        "type": "delta",
        "parent": "willstoyscatalogue^~^catalogue_2.0",
        "seq": 3,
        "action": "insert",
        "data": [
            "373 Vineyard Drive, Mayfield Heights, OH 44124",
            "yes",
            "Explore the potential of solar power with this neat science kit",
            "41.475136, -81.369677",
            "http://www.wills-toys.com/images/ef6.jpg",
            "OWI Frightened Grasshopper Kit - Solar Powered",
            1.99,
            22,
            1682277334,
            "41142d168acd857313dbfa64e73e62d5"
        ]
    },
    {
        "_id": "willstoyscatalogue^~^catalogue_2.0_delta_4",
        "_rev": "1-fec13ddee2188e07d7295b26de4b43c3",
        "type": "delta",
        "parent": "willstoyscatalogue^~^catalogue_2.0",
        "seq": 4,
        "action": "delete",
        "data": "3f30ab289441af1b95cd1bcdedce3cbc"
    },
    {
        "_id": "willstoyscatalogue^~^catalogue_2.0_delta_5",
        "_rev": "1-70b65ff969153960f17f842492cf9505",
        "type": "delta",
        "parent": "willstoyscatalogue^~^catalogue_2.0",
        "seq": 5,
        "action": "delete",
        "data": "7afad753056737c44c90c1767b1e5de3"
    },
    {
        "_id": "willstoyscatalogue^~^catalogue_2.0_delta_6",
        "_rev": "1-39e9cff02e5d6d3c1f8bb76641f39883",
        "type": "delta",
        "parent": "willstoyscatalogue^~^catalogue_2.0",
        "seq": 6,
        "action": "delete",
        "data": "7c738a17d0019be20f5bf9595954cc9e"
    }
]
```
Note: The ```seq``` field is a unique point in the datasets history. Use the ```seq``` parameter when calling the delta api to get all changes since that seq number

OR, you can get the sql statements you need to apply to your database direcly:
```
curl "https://b317eac00b:5156a8e80e@www.datownia.com/api/doc/example/v2/delta/willstoyscatalogue/catalogue.sql?seq=0"
```
```sql
replace into [example/willstoyscatalogue/catalogue_2.0] ([addressOfStore],[availableForPickup],[description],[geocodeOfStore],[imageUrl],[name],[price],[stockLevel],[upc], [_id]) values ('373 Vineyard Drive, Mayfield Heights, OH 44124','yes','Five activation points start the excitement all over the Batcave','41.475136, -81.369677','http://www.wills-toys.com/images/abd.jpg','Fisher-Price Imaginext Bat Cave','50.99','10','2311835075','950739696db9aa48dfd37c06cbfe3c46');
replace into [example/willstoyscatalogue/catalogue_2.0] ([addressOfStore],[availableForPickup],[description],[geocodeOfStore],[imageUrl],[name],[price],[stockLevel],[upc], [_id]) values ('373 Vineyard Drive, Mayfield Heights, OH 44124','no','This mesmerizing maze of safe, soft, continuous tubes is perfect for a teething baby to chew on','41.475136, -81.369677','http://www.wills-toys.com/images/gr2.jpg','Manhattan Toy Winkel','11.5','10','9054297547','4c7ec7f51c7eda0f1e854a240436e210');
replace into [example/willstoyscatalogue/catalogue_2.0] ([addressOfStore],[availableForPickup],[description],[geocodeOfStore],[imageUrl],[name],[price],[stockLevel],[upc], [_id]) values ('373 Vineyard Drive, Mayfield Heights, OH 44124','yes','Explore the potential of solar power with this neat science kit','41.475136, -81.369677','http://www.wills-toys.com/images/ef6.jpg','OWI Frightened Grasshopper Kit - Solar Powered','1.99','22','1682277334','41142d168acd857313dbfa64e73e62d5');
delete from [example/willstoyscatalogue/catalogue_2.0] where _id = '3f30ab289441af1b95cd1bcdedce3cbc';
delete from [example/willstoyscatalogue/catalogue_2.0] where _id = '7afad753056737c44c90c1767b1e5de3';
delete from [example/willstoyscatalogue/catalogue_2.0] where _id = '7c738a17d0019be20f5bf9595954cc9e';
replace into [table_def] (tablename, seq) values ('willstoyscatalogue/catalogue_2.0', 6);

```

### Get several datasets at once using the App API

Developers subscriptions are placed into an app. This app is where the app key and secret come from.
If you have multiple subscriptions you may download all your datasets as one database by use the App Api.

```
curl "https://b317eac00b:5156a8e80e@www.datownia.com/api/app/example/b317eac00b.sqlite" > testapp.sqlite
```

Here example is the developers "my api tag" rather than the publishers tag. i.e. the account to which the app belongs.

You can then use the above Delta API to synchronise each table in the database

##API Information Guide

This section contains more detailed information for using the API’s that API Maker generates from the source
files uploaded by the Publishers (data owners)

### 1. Overview

Each API is constructed from a source (csv,xls,xlsx) file and “wrapped” in JSON, where the:
* First row (column headings) of the csv file become the field names in the API
* Remaining rows of the csv file become the data in the API

Each API contains metadata:
* File header type information such as version, timestamps, row count…etc
* Data record type information such field name list, field order…etc

All API’s are RESTful and use common API practices such as offset and limit parameters for paging.

All API’s use either OAuth2 or HTTP basic authentication. Authentication keys are required to verify API calls, these will be
provided when registering an approved application.

Each application requires only one set of keys and those will cover all the API’s that have been
approved for use by that application.

Note: There is currently a limit of 200 for the number of data rows that can be returned with a single
request. Therefore, multiple requests may be needed in order to fulfil a particular function in an
application. Use limit and offset to page through results. An exception to this are the sqlite database responses which will return all records in the generated database

### 2. API URL Format

The URL of the API is formatted as follows:

https://www.datownia.com/api/doc/{publisher}/v{version}/{filename}

<table>
<tr>
<td>Publisher</td>
<td>Name of the organisation that is publishing the data in the API</td>
</tr>
<tr>
<td>Version</td>
<td>Version number of the file – format = v1

The version number will increment when a structure change has been made
to the API such as new or removed fields

The data publisher will be responsible for notifying developers of new API
version numbers
</td>
</tr>

<tr>
<td>Filename</td>
<td>The name of the source data file that was used to create the API

Will usually be meaningful and describe the data that is in the API

This is the same name that appears on the dataset list in the data publishers
branded API website</td>
</tr>

</table>

### 3. API Metadata

API Maker uses the following metadata fields to describe an API:

<table>
<tr><th>Field</th><th>Meaning</th></tr>
<tr><td>_id</td><td>Filename and version (internal id)</td></tr>
<tr><td>_rev</td><td>Internal revision number</td></tr>
<tr><td>fieldList</td><td>Lists the API fields in the order that they appear in the API.
The columns in the source file are alphabetically sorted when the API
is created</td></tr>
<tr><td>fieldListDocOrder</td><td>Lists the API fields in the original column order of the source file</td></tr>
<tr><td>name</td><td>Friendly name of the API</td></tr>
<tr><td>published</td><td>Has the api been published? Always true, but used internally</td></tr>
<tr><td>fileSize</td><td>Size of the original source file in it’s original format</td></tr>
<tr><td>fileCreated</td><td>The date and timestamp of when an API was first created</td></tr>
<tr><td>fileModified</td><td>The date and timestamp of when an API was last updated</td></tr>
<tr><td>fileRevision</td><td>Internal file revision number</td></tr>
<tr><td>fileName</td><td>The name and extension of the original source file used to create an
API</td></tr>
<tr><td>rows</td><td>The number of data rows in the API</td></tr>
<tr><td>contentChecksum</td><td>Internal checksum</td></tr>
<tr><td>timestamp</td><td>The date and timestamp of the last time anything changed for an API</td></tr>
<tr><td>seq</td><td>The latest delta sequence number.
This is to be used in conjunction with the Delta API described below
in the delta section</td></tr>
<tr><td>root</td><td>Internal reference field</td></tr>
<tr><td>apiVersion</td><td>The version number of the API</td></tr>
<tr><td>type</td><td>The type of API this is. Used internally. Always 'document' currently</td></tr>
<tr><td>limit</td><td>The number of rows requested. This may be greater than the number of rows returned if there are < limit rows. Currently defaults to 200</td></tr>
<tr><td>offset</td><td>The first row number requested. </td></tr>
<tr><td>pageNumber</td><td>The current page number within an API based on limit and offset
EG pageCount = 3 if API contains 1000 rows
and limit = 200 and offset = 400</td></tr>
<tr><td>pageCount</td><td>The number of pages in data in the API based on the limit specified or
defaulted.
EG. pageNumber = 5 if API contains 1000 rows and limit = 200</td></tr>
<tr><td>numRowsInContents</td><td>The number of rows returned in the result contents array for an API
method request</td></tr>

</table>

### 4. API Methods

#### Dataset method

To call the dataset method the url is constructed like:

> https://www.datownia.com/api/doc/example/v1/willstoyscatalogue/catalogue

Optional query string parameters are:

<table><tr><th>Parameter</th><th>Example</th><th>Detail</th></tr>
<tr><td>metadataonly</td><td>?metadataonly=y</td><td>Returns only the metadata of an API and none of the rows</td></tr>
<tr><td>sampledata</td><td>?sampledata=y</td><td>Returns the first two rows of data along with the
metadata of an API</td></tr>
<tr><td>field and value</td><td>?field=a&value=x</td><td>Returns rows where a=x</td></tr>
<tr><td>field, from and to</td><td>?field=a&from=x&to=y/td><td>Returns rows where x&lt;=a&lt;=y</td></tr>
<tr><td>q (query)</td><td>?q=price:[17.99 to 21.99] AND address:"Vineyard"</td><td>Lucene style search. See <a href="http://lucene.apache.org/core/old_versioned_docs/versions/2_9_1/queryparsersyntax.html">here</a> for syntax</td></tr>
<tr><td>offset and limit</td><td>?offset=x&limit=y</td><td>Returns a subset of data from an API
Data row start point specified by offset
Number of rows returned specified by limit</td></tr>
</table>

### 5. Delta API

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
```
https://www.datownia.com/api/doc/example/v2/delta/willstoyscatalogue/catalogue?seq=0
```
### 6. Error Return Codes

<table>
<tr><th>Error Code</th><th>Reason</th></tr>
<tr><td>200</td><td>Okay</td></tr>
<tr><td>400</td><td>Problem with request – in terms of bad method call</td></tr>
<tr><td>401</td><td>Not authorised</td></tr>
<tr><td>404</td><td>Not Found</td></tr>
<tr><td>500</td><td>Server error - could be a temporal issue, such as a network issue, so may be resolved by repeating the request</td></tr>
</table>

### 7. SQlite Download

The data in an API can be downloaded as SQlite instead of JSON, this is done by appending “sqlite”
to the filename in the API Maker URL as follows:

https://www.datownia.com/api/doc/example/version/willstoyscatalogue/catalogue.sqlite

The returned database will contain a table named the same as the filename with the version number
appended. The primary key of the table is the “_id” field
