##API Usage Quickstart
A list of datasets can be found on the Parly Hack developer portal
https://www.datownia.com/developer/parlyhack2012

To get going really quickly you can use HTTP basic authentication and the [CURL](http://curl.haxx.se) command. (Note depending on how your curl is set up you may need the -k parameter to ignore cert checking).

### Get first 5 records in a dataset:
```
curl "https://519b825f00:62faf3ab03@www.datownia.com/api/doc/parlyhack2012/v1/registermpsinterests2012-04-30/data?offset=0&limit=5"
```

Results:
```json
{
    "_id": "registermpsinterests2012-04-30^~^data_1.0",
    "_rev": "1-61d4a7f9bfac178e0e97e80c70b76b01",
    "fieldListDocOrder": [
        "TWFY_Member_id",
        "membername",
        "TWFY_Person_id",
        "categoryid",
        "categoryname",
        "categorytype",
        "categoryitem"
    ],
    "fieldList": [
        "categoryid",
        "categoryitem",
        "categoryname",
        "categorytype",
        "membername",
        "TWFY_Member_id",
        "TWFY_Person_id"
    ],
    "name": "registermpsinterests2012-04-30^~^data",
    "published": true,
    "fileSize": 0,
    "fileCreated": "2012-11-19 18:26:30Z",
    "fileModified": "2012-11-19 19:18:51Z",
    "fileRevision": "60bca9a4c",
    "fileName": "registermpsinterests2012-04-30",
    "rows": 8420,
    "contentChecksum": "687e282c411e8be1f35fb998866391c3",
    "timestamp": "2012-11-19 19:18:51Z",
    "seq": 0,
    "root": "registermpsinterests2012-04-30^~^data",
    "apiVersion": "1.0",
    "type": "document",
    "limit": 5,
    "offset": 0,
    "pageNumber": 1,
    "pageCount": 1684,
    "numRowsInContents": 5,
    "contents": [
        [
            1,
            "Fees received for co-presenting BBC’s \"This Week\" TV programme. Address: BBC Television Centre, Wood Lane, London W12 7RJ.",
            "Remunerated employment, office, profession etc",
            2,
            "Diane Abbott",
            40289,
            10001,
            "c8d87045323e522da1a616c9591cdf03"
        ],
        ...
    ]
}
```
* All document api calls return documents with this structure.
* fieldList gives you fields in the order that they appear in the contents. 
* It is recommended to always supply limit and offset. The max, and default, limit is 200. You can easily loop through all records by checking if numRowsInContents = limit, as soon as it does not you must be on the last page.


You may also use OAuth 2 authentication with a client credentials grant, using your app key and secret. The examples in the rest of this repository use OAuth 2 but it may be simpler to use basic auth for the hack if you are unfamiliar with OAuth 2.

### Search a dataset:
David Cameron's gifts
```
curl "https://519b825f00:62faf3ab03@www.datownia.com/api/doc/parlyhack2012/v1/registermpsinterests2012-04-30/data?limit=5&offset=0" -G --data-urlencode "q=membername:\"David Cameron\" AND categoryname:gifts" -k
```

Iain Duncan Smith's expenses
```
curl "https://519b825f00:62faf3ab03@www.datownia.com/api/doc/parlyhack2012/v1/mpsexpenses_2012?limit=5&offset=0" -G --data-urlencode "q=MP's\ Name:\"Iain Duncan Smith\"" -k
```
Note that here we are using the --data-encodeurl and -G parameters in curl so we do not have to url encode the request url and make it less readable for these docs.

### Get entire dataset as a sqlite database
```
curl "https://519b825f00:62faf3ab03@www.datownia.com/api/doc/parlyhack2012/v1/registermpsinterests2012-04-30/data.sqlite" > test.sqlite
```
This will create a sqlite db file with two tables.

1. parlyhack2012/registermpsinterests2012-04-30/data_1.0. This contains your dataset
2. table_def.  This tracks the last seq number for each table in the database. This can be used to call the delta api passing in the seq value in this table

### Get several datasets at once using the App API

Api subscriptions are placed into an app. This app is where the app key and secret come from.
If you have multiple subscriptions you may download all your datasets as one database by use the App Api.
For Parly Hack we have already set up an app with subscriptions so you do not need to register, but you may also register if you wish to subscribe to just a subset of the data and therefore download a smaller sqlite database file.

```
curl "https://519b825f00:62faf3ab03@www.datownia.com/api/app/parlyhack2012datownia/519b825f00.sqlite" > testapp.sqlite
```

Here example is the developers tag rather than the publishers. i.e. the account to which the app belongs.

You can then use the Delta API to synchronise each table in the database

### Keep your database up to date with the delta api
You are unlikely to need this for the hack but it's worth knowing it is there for the future as it is a good feature, as it enables you to keep a local database in sync with the source data.
```
curl "https://519b825f00:62faf3ab03@www.datownia.com/api/doc/parlyhack2012/v1/delta/registermpsinterests2012-04-30/data?seq=0"
```

Note: The ```seq``` field is a unique point in the datasets history. Use the ```seq``` parameter when calling the delta api to get all changes since that seq number

OR, you can get the sql statements you need to apply to your database direcly:
```
curl "https://519b825f00:62faf3ab03@www.datownia.com/api/doc/parlyhack2012/v1/delta/registermpsinterests2012-04-30/data.sql?seq=0"
```

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
