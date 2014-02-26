package es.uc3m.setichat.utils;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class XMLParser {
	 private Document createBasicDocument() {
	        Document document = DocumentHelper.createDocument();
	        document.addElement( "hearder" );
	        document.addElement( "content" );
	        document.addElement( "signature" );

	        return document;
	}
}
