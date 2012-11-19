The repository contains the datownia SDK and code samples showing how to use datownia directly without the SDK. 

The following documentation is for general use of datownia apis rather than using the SDKs. SDK documentation will be found in the relevant SDK folder.

##API Usage Quickstart

TODO CURL examples here

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

<table>
<tr><th>Method</th><th>Parameters</th><th>Purpose</th></tr>

<tr><td>Metadata</td><td>?metadataonly=y</td><td>Returns the metadata of an API,
- Header information
- Field name list (original order and doc order)</td></tr>

<tr><td>Sample Data</td><td>?sampledata=y</td><td>Returns the first two rows of data along with the
metadata of an API</td></tr>

<tr><td>Search By Field Value</td><td>?field=a&value=x</td><td>Returns rows of data from an API where a field
name equals a particular value</td></tr>

<tr><td>Search By Field Range</td><td>?field=a&from=x&to=y</td><td>Returns rows of data from an API where a field
name value is within a particular range</td></tr>

<tr><td>Metadata</td><td>?metadataonly=y</td><td>Returns the metadata of an API,
- Header information
- Field name list (original order and doc order)</td></tr>

<tr><td>Paged Query</td><td>?offset=x&limit=y</td><td>Returns a subset of data from an API
Data row start point specified by offset
Number of rows returned specified by limit</td></tr>

<tr><td>Delta API</td><td>?offset=x&limit=y&seq=z</td><td>Contains a list of all the changes that have been
to the data in an API.
See the Detla API section for more detail</td></tr>

</table>