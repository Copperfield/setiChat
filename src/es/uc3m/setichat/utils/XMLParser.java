package es.uc3m.setichat.utils;

import java.util.Random;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


public class XMLParser {
	
	public XMLParser(){
		
	}
	
	public String setRequest(String sourceId,
			String destinationId,
			String type,
			String [] content){
		
		// Set basic document
		Document document = DocumentHelper.createDocument();
		Element message = document.addElement("message");
		
		// Set header
		Element header = message.addElement("header");
		header.addElement("idSource").addText(sourceId);
		header.addElement("idDestination").addText(destinationId);
		header.addElement("idMessage").addText(getRandomHex(16));
		header.addElement("type").addText(type);
		header.addElement("encrypted").addText("false");
		header.addElement("signed").addText("false");
		
		// Set content 
		switch (Integer.parseInt(type)) {
			case 1:
				Element signup = message.addElement("content").addElement("signup");
					signup.addElement("nick").addText(content[0]);
					signup.addElement("mobile").addText(content[1]);
				break;
		}
		
		return document.asXML();
		
	}
	 
	 private String getRandomHex(int numchars){
	        Random r = new Random();
	        StringBuffer sb = new StringBuffer();
	        while(sb.length() < numchars){
	            sb.append(Integer.toHexString(r.nextInt()));
	        }

	        return sb.toString().substring(0, numchars);
	 }
	
}
