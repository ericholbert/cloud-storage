# Cloud Storage
A simple cloud storage accessible via a REST API. Files are stored in the server's file system while other data is stored in the database. A registered user can download files uploaded by himself or shared by another user.

## Requirements
* Java 17
* Maven
* MySQL

## Endpoints
### Public
|HTTP method|Endpoint|Description|Detail|
|:---|:---|:---|:---|
|`POST`|`/api/v1/register`|Register a user|The request body must consist of the `name` and `password` properties in the JSON format.|
### User-restricted
|HTTP method|Endpoint|Description|Detail|
|:---|:---|:---|:---|
|`POST`|`/api/v1/upload`|Upload one or multiple files|The files are send as form data using the `files` key.|
|`GET`|`/api/v1/download/{fileId}`|Download a file||
|`GET`|`/api/v1/list`|List all user files|The result can be sorted, filtered and paginated. Sorting is allowed by all valid file object properties in ascending or descending order. These are: `id`, `name`, `type`, `size` and `owner` (sorted by owner ID). The result can be filtered by the `ownerName` and `fileType` query parameters.|
|`DELETE`|`/api/v1/delete/{fileId}`|Delete a file||
|`POST`|`/api/v1/share/{fileId}/{userName}`|Share a file with a user||
|`DELETE`|`/api/v1/share/{fileId}/{userName}`|Stop sharing a file with a user||
