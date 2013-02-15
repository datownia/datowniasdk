package application;
import model.Document;

/**
 * 
 */

/**
 * @author Release Mobile
 *
 */
public class Application {
	
	ApiClient client = null;
	
	public Application (String publisher, String appKey, String appSecret) {
		client = new ApiClient(publisher, appKey, appSecret);
	}
	
	public Document GetDocument(String documentId){
		return client.GetDocument(documentId);
	}
	
	public Document GetDocument(String documentId, int offset, int limit) {
		return client.GetDocument(documentId, offset, limit);
	}
	
	public Document GetMetadataOnly(String documentId) {
		return client.GetMetadataOnly(documentId);
	}
	
	public Document GetDocumentSample(String documentId) {
		return client.GetDocumentSample(documentId);
	}
	
	public Document SearchField(String documentId, String field, String value) {
		return client.SearchField(documentId, field, value);
	}
	
	public Document SearchRange(String documentId, String field, String from, String to) {
		return client.SearchRange(documentId, field, from, to);
	}
}
