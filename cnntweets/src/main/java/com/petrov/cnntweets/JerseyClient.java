package com.petrov.cnntweets;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.oauth.client.OAuthClientFilter;
import com.sun.jersey.oauth.signature.OAuthParameters;
import com.sun.jersey.oauth.signature.OAuthSecrets;

/**
 * Singleton JerseyClient class, responsible for the Twitter synchronous request.
 * Delegates all requests to the static Client object.
 * 
 * @author andrey
 * 
 */
public class JerseyClient {
	private static final String USER_TIMELINE_URL = "http://api.twitter.com/1/statuses/user_timeline.xml?id=cnnbrk&count=199";
	private static final String CONSUMER_KEY = "QV23XV0SdRjWqvJD5zmLfw";
	private static final String CONSUMER_SECRET = "sGsGbuRssCAfnPcqddU1ZbZPScY62rzOnursxlzwI";
	private static final String ACCESS_TOKEN = "382442953-nyewcdHMAkotRvVnMTmGO7iNWgaSrVNemZnc6Ga2";

	private static JerseyClient instance;
	private static Client client;

	private JerseyClient() {
		client = Client.create();
	}

	public static JerseyClient getInstance() {
		if (instance == null) {
			instance = new JerseyClient();
		}
		return instance;
	}

	/**
	 * Synchronous request to get the tweets from the Twitter RESTful API 1.1. 
	 * Throws Jersey Client exceptions.
	 * 
	 * @return list of the tweet strings.
	 */
	public String getTweetsXml() {
		client.removeAllFilters();
		OAuthClientFilter oauthFilter = getOAuthClientFilter(client);
		WebResource webResource = client.resource(USER_TIMELINE_URL);

		webResource.addFilter(oauthFilter);

		// Add for debugging only.
		// webResource.addFilter(new LoggingFilter());

		webResource.accept(MediaType.TEXT_XML);

		String tweetsXml = webResource.get(String.class);
		return tweetsXml;
	}

	/**
	 * Helper method responsible for the configuration of an OAuthClientFilter. 
	 * Not really needed since the Twitter user timeline is publicly accessible.
	 * 
	 * @param client
	 *            the client from which to get providers for the filter constructor
	 * @return the filter loaded with the required credentials for 2 legged OAuth access
	 */
	private OAuthClientFilter getOAuthClientFilter(Client client) {
		OAuthParameters parameters = new OAuthParameters().signatureMethod("HMAC-SHA1").consumerKey(CONSUMER_KEY).version("1.1").token(ACCESS_TOKEN);
		OAuthSecrets secrets = new OAuthSecrets().consumerSecret(CONSUMER_SECRET);
		OAuthClientFilter filter = new OAuthClientFilter(client.getProviders(), parameters, secrets);
		return filter;
	}

}
