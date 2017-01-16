package org.recap.security;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.GitHubTokenResponse;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sheiks on 16/01/17.
 */
public class OAuthTest {

    private final String USER_AGENT = "Mozilla/5.0";

    @Test
    public void testOAuthAuthenticationUsingOAuthClient() throws OAuthSystemException, OAuthProblemException {
        OAuthClientRequest request = OAuthClientRequest
                .tokenProvider(OAuthProviderType.FACEBOOK)
                .setGrantType(GrantType.CLIENT_CREDENTIALS)
                .setClientId("622986091218293")
                .setClientSecret("9c78ac93eb5187fcaa5923572f20a482")
                .setRedirectURI("http://localhost")
                .buildQueryMessage();
        //create OAuth client that uses custom http client under the hood
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

        //Facebook is not fully compatible with OAuth 2.0 draft 10, access token response is
        //application/x-www-form-urlencoded, not json encoded so we use dedicated response class for that
        //Custom response classes are an easy way to deal with oauth providers that introduce modifications to
        //OAuth 2.0 specification
        GitHubTokenResponse oAuthResponse = oAuthClient.accessToken(request, GitHubTokenResponse.class);

        String accessToken = oAuthResponse.getAccessToken();
        System.out.println("Access Token : " + accessToken);
    }

    @Test
    public void testOAuthAuthenticationUsingGetRequest() throws Exception {
        String url = "https://graph.facebook.com/oauth/access_token?client_id=622986091218293&client_secret=9c78ac93eb5187fcaa5923572f20a482&grant_type=client_credentials";
        sendGet(url);
    }

    // HTTP GET request
    private void sendGet(String url) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println(response.toString());
    }

    @Test
    public void testOAuthAuthenticationUsingPostMethod() throws IOException {
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod("https://graph.facebook.com/oauth/access_token?");
        post.setParameter("grant_type","client_credentials");
        post.setParameter("client_id","622986091218293");
        post.setParameter("client_secret","9c78ac93eb5187fcaa5923572f20a482");
        post.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        client.executeMethod(post);
        String responseBody = post.getResponseBodyAsString();
        System.out.println(responseBody);
    }

    @Test
    public void testOAuthAuthenticationUsingPostMethodWithUserNameAndPassword() throws IOException {
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod("https://graph.facebook.com/oauth/access_token?");
        post.setParameter("grant_type","password");
        post.setParameter("client_id","622986091218293");
        post.setParameter("username",""); // Todo : Add fb username
        post.setParameter("password",""); // Todo : Add fb password
        post.setParameter("redirect_uri","http://www.test.com");
        post.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        client.executeMethod(post);
        String responseBody = post.getResponseBodyAsString();
        System.out.println(responseBody);

    }


}
