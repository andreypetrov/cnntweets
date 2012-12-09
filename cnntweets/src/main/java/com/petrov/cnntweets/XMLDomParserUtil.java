package com.petrov.cnntweets;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * XML Dom Parser utility. Non-instantiable. SAX parser may provide slightly better performance.
 */

public class XMLDomParserUtil {
	private static final String EMPTY_STRING = "";

	private XMLDomParserUtil() {
	}

	/**
	 * Convenience method. Redirects to parseNodesFromXml(String xmlString, String nodeName, String trimString), passing empty string as trimString
	 * 
	 * @param xmlString
	 * @param nodeName
	 * @return
	 */
	public static List<String> parseNodesFromXml(String xmlString, String nodeName) {
		return parseNodesFromXml(xmlString, nodeName, EMPTY_STRING);
	}

	/**
	 * DOM Parser method. 
	 * Converts the input xmlString into List<String> of all the text in the nodes named nodeName.
	 * The memory footprint is small enough when we deal with less than 200 tweets.
	 * 
	 * @param xmlString
	 *            The xml string
	 * @param nodeName
	 *            the name of the node, we are intersted in (e.g. "text")
	 * @param trimString
	 *            if we want to trim a particular pattern from every node's text, we pass it as trimString, e.g. "(https?://[^\\s]+)"
	 * @return List of all the text strings from every node named nodeName. trimString text is trimmed from the result strings.
	 */
	public static List<String> parseNodesFromXml(String xmlString, String nodeName, String trimString) {
		List<String> nodeTexts = new ArrayList<String>();

		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource inputSource = new InputSource();
			inputSource.setCharacterStream(new StringReader(xmlString));

			try {
				Document document = db.parse(inputSource);
				NodeList nodes = document.getElementsByTagName(nodeName);
				for (int i = 0; i < nodes.getLength(); i++) {
					String nodeText = nodes.item(i).getTextContent();
					String trimmedNodeText = trimStringFromText(trimString, nodeText);
					nodeTexts.add(trimmedNodeText);
				}

			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		return nodeTexts;
	}

	/**
	 * Helper method trimming the trimString pattern from the text input parameter
	 * 
	 * @param trimString
	 *            regex pattern
	 * @param text
	 *            text to be cleaned
	 * @return the input text with any occurrences of the trimString pattern removed.
	 */
	private static String trimStringFromText(String trimString, String text) {
		if (trimString != EMPTY_STRING) {
			Pattern trimPattern = Pattern.compile(trimString);
			Matcher matcher = trimPattern.matcher(text);

			while (matcher.find()) {
				// System.out.println(matcher.group());
				text = text.replace(matcher.group(), EMPTY_STRING);
			}
		}
		return text;
	}
}
