package test;

import model.Document;
import application.Application;

public class ApiClientTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String docName = "DOCUMENT ID"; 
		Application app = new Application("PUBLISHER USERNAME", "APP KEY", "APP SECRET");
		Document result = app.GetDocument(docName);
		if (result != null) System.out.println(String.format("1 %s", result.toString()));
		Document result2 = app.GetDocument(docName, 2, 2);
		if (result2 != null) System.out.println(String.format("2 %s", result2.toString()));
		Document result3 = app.GetDocumentSample(docName);
		if (result3 != null) System.out.println(String.format("3 %s", result3.toString()));
		Document result4 = app.GetMetadataOnly(docName);
		if (result4 != null) System.out.println(String.format("4 %s", result4.toString()));
		Document result5 = app.SearchField(docName, "COLUMN NAME", "VAL");
		if (result5 != null) System.out.println(String.format("5 %s", result5.toString()));
		Document result6 = app.SearchRange(docName, "COLUMN NAME", "FROM", "TO");
		if (result6 != null) System.out.println(String.format("6 %s", result6.toString()));
	}

}
