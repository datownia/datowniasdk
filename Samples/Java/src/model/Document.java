package model;
import com.fasterxml.jackson.databind.JsonNode;

/*
 * This is an Api result.
 * */
public class Document {
	
	public String _id;
	public String _rev;
	public String[] fieldList;
	public String[] fieldListDocOrder;
	public String name;
	public String published;
	public String fileSize;
	public String fileCreated;
	public String fileModified;
	public String fileRevision;
	public String fileName;
	public String rows;
	public String contentChecksum;
	public String timestamp;
	public String seq;
	public String root;
	public String apiVersion;
	public String type;
	public String limit;
	public String offset;
	public String pageNumber;
	public String pageCount;
	public JsonNode[] contents;
	public String numRowsInContents;
	
	public String getNumRowsInContents() {
		return numRowsInContents;
	}

	public void setNumRowsInContents(String numRowsInContents) {
		this.numRowsInContents = numRowsInContents;
	}

	public String[] getFieldListDocOrder() {
		return fieldListDocOrder;
	}

	public void setFieldListDocOrder(String[] fieldListDocOrder) {
		this.fieldListDocOrder = fieldListDocOrder;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public String getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getPageCount() {
		return pageCount;
	}

	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String get_rev() {
		return _rev;
	}

	public void set_rev(String _rev) {
		this._rev = _rev;
	}

	public String getPublished() {
		return published;
	}

	public void setPublished(String published) {
		this.published = published;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileCreated() {
		return fileCreated;
	}

	public void setFileCreated(String fileCreated) {
		this.fileCreated = fileCreated;
	}

	public String getFileModified() {
		return fileModified;
	}

	public void setFileModified(String fileModified) {
		this.fileModified = fileModified;
	}

	public String getFileRevision() {
		return fileRevision;
	}

	public void setFileRevision(String fileRevision) {
		this.fileRevision = fileRevision;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String[] getFieldList() {
		return fieldList;
	}

	public void setFieldList(String[] fieldList) {
		this.fieldList = fieldList;
	}

	public String getContentChecksum() {
		return contentChecksum;
	}

	public void setContentChecksum(String contentChecksum) {
		this.contentChecksum = contentChecksum;
	}

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public JsonNode[] getContents() {
		return contents;
	}

	public void setContents(JsonNode[] contents) {
		this.contents = contents;
	}

	public String getName() {
		return name;
	}

	public void setName(String value) {
		name = value;
	} 

}
