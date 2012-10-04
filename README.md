The repository contains the datownia SDK and code samples showing how to use datownia directly without the SDK.

##API Information Guide

This section contains information for using the API’s that API Maker generates from the source
files uploaded by the Publishers (data owners)

### 1. Overview

Each API is constructed from a source (csv,xls,xlsx) file and “wrapped” in JSON, where the:
* First row (column headings) of the csv file become the field names in the API
* Remaining rows of the csv file become the data in the API

Each API contains metadata:
* File header type information such as version, timestamps, row count…etc
* Data record type information such field name list, field order…etc

All API’s are RESTful and use common API practices such as offset and limit parameters for paging.

All API’s use OAuth 2 for authentication purposes and will require OAuth 2 client integration to be
used when calling the API. Authentication keys are required to verify each API call, these will be
provided when registering an approved application.

Each application requires only one set of keys and those will cover all the API’s that have been
approved for use by that application.

Note: There is currently a limit of 200 for the number of data rows that can be returned with a single
request. Therefore, multiple requests may be needed in order to fulfil a particular function in an
application

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


