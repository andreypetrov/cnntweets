package com.petrov.cnntweets;

import java.util.List;

import com.sun.jersey.api.client.UniformInterfaceException;

/**
 * Main application class.
 * 
 * The application is downloading all not-deleted/suspended from the last 199 CNN Breaking News tweets. Actual number of tweets varies. After getting them, it
 * finds the two of them with the longest common substring in between. The comparison of strings is case-sensitive. URL addresses starting with
 * http/https are trimmed away from the tweets before the comparison. 
 * 
 */
public class App {
	private static JerseyClient jerseyClient;

	private static final String NODE_NAME = "text";
	// URL pattern with no whitespace and at least one character. Effectively removes all http and https links.
	// Also removes if there is a whitespace before that.
	private static final String URL_PATTERN = "( https?://[^\\s]+)|(https?://[^\\s]+)";

	/**
	 * Applications's main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		jerseyClient = JerseyClient.getInstance();

		try {
			String tweetsXml = jerseyClient.getTweetsXml();

			List<String> tweets = XMLDomParserUtil.parseNodesFromXml(tweetsXml, NODE_NAME, URL_PATTERN);

			// For debugging only
			// printList(tweets);

			String longestCommonSubstringMessage = StringComparatorUtil.findLongestCommonSubstring(tweets);

			System.out.println(longestCommonSubstringMessage);
		} catch (UniformInterfaceException e) { // thrown by the jerseyClient if the page requested returns error code, e.g. 404
			System.out.println(e.getLocalizedMessage());
		}

	}

	// For debugging only.
	// private static void printList(List<String> list) {
	// int i = 0;
	// for (String listElement : list) {
	// System.out.println(i + ": " + listElement);
	// i++;
	// }
	// }

}
