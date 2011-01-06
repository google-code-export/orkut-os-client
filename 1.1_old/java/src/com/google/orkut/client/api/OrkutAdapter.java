package com.google.orkut.client.api;

import net.oauth.OAuthConsumer;
import net.oauth.OAuthServiceProvider;
import net.oauth.OAuthAccessor;
import net.oauth.OAuth;
import net.oauth.OAuthMessage;
import net.oauth.client.OAuthClient;
import net.oauth.client.httpclient4.HttpClient4;

import com.google.orkut.client.transport.HttpRequest;
import com.google.orkut.client.transport.OrkutHttpRequestFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;
import java.util.Map;
import java.io.InputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;

public class OrkutAdapter {
   protected OrkutAdapterDebugListener debugListener = null;

   protected String consumerKey = "";
   protected String consumerSecret = "";
   protected String callbackURL = "";
   protected boolean isProduction = false;
   protected String requestURL = "";

   protected static final String OAUTH_REQUEST_URL =
                "https://www.google.com/accounts/OAuthGetRequestToken";
   protected static final String OAUTH_AUTHORIZATION_URL =
                "https://www.google.com/accounts/OAuthAuthorizeToken";
   protected static final String OAUTH_ACCESS_URL = 
                "https://www.google.com/accounts/OAuthGetAccessToken";
   protected static final String OAUTH_SCOPE = 
                "http://orkut.gmodules.com/social";
   protected static final String SERVER_URL_SANDBOX =
                "http://sandbox.orkut.com/social/rpc";
   protected static final String SERVER_URL_PROD =
                "http://www.orkut.com/social/rpc";

   protected OAuthConsumer consumer = null;
   protected OAuthAccessor accessor = null;
   protected OAuthClient client = null;

   // Easy access to factories
   public final ActivityTxFactory activityTxFactory =
            new ActivityTxFactory();
   public final AlbumsTxFactory   albumsTxFactory =
            new AlbumsTxFactory();
   public final CaptchaTxFactory  captchaTxFactory =
            new CaptchaTxFactory();
   public final CommentsTxFactory commentsTxFactory =
            new CommentsTxFactory();
   public final FriendTxFactory   friendTxFactory =
            new FriendTxFactory();
   public final PhotosTxFactory   photosTxFactory =
            new PhotosTxFactory();
   public final ProfileTxFactory  profileTxFactory =
            new ProfileTxFactory();
   public final ScrapTxFactory    scrapTxFactory =
            new ScrapTxFactory();
   public final VideoTxFactory    videoTxFactory =
            new VideoTxFactory();
   
   protected void say(String s) { 
      if (debugListener != null)
         debugListener.printOrkutAdapterMessage("[orkut-adapter]: " + s);
   }

   public OrkutAdapter(String consumerKey, String consumerSecret,
                       String callbackURL, boolean isProduction,
                       OrkutAdapterDebugListener l) 
                       throws OrkutAdapterException {

      this.consumerKey = consumerKey;
      this.consumerSecret = consumerSecret;
      this.callbackURL = callbackURL;
      this.isProduction = isProduction;
      this.debugListener = l;
      this.requestURL = isProduction ? SERVER_URL_PROD : SERVER_URL_SANDBOX;

      say("Initting OAuth.");
      say("Consumer key     : " + consumerKey);
      say("Consumer secret  : " + consumerSecret);
      say("Callback URL     : " + callbackURL);
      say("Request URL is   : " + requestURL);

      try {
         say("Setting up oauth consumer.");
         consumer = new OAuthConsumer(null,
                   consumerKey, consumerSecret, new OAuthServiceProvider(
                   OAUTH_REQUEST_URL, OAUTH_AUTHORIZATION_URL, 
                   OAUTH_ACCESS_URL));
         consumer.setProperty(OAuthClient.PARAMETER_STYLE,
                   net.oauth.ParameterStyle.QUERY_STRING);
         
         say("Setting up oauth accessor and client.");
         accessor = new OAuthAccessor(consumer);
         client = new OAuthClient(new HttpClient4());
      }
      catch (Exception ex) {
         throw new OrkutAdapterException(
                "OrkutAdapter: Failed to initialize OAuth.", ex);
      }
   }

   public void setDebugListener(OrkutAdapterDebugListener l) {
      debugListener = l;
      say("Debug listener attached.");
   }

   public String requestAuthURL() throws OrkutAdapterException {
      try {
         return requestAuthURL_inner();
      }
      catch (Exception ex) {
         throw new OrkutAdapterException(
              "OrkutAdapter: Error requesting OAuth authorization URL", ex);
      }
   }

   private String requestAuthURL_inner() throws Exception {
      say("Getting oauth request token.");
      List<OAuth.Parameter> callback = OAuth.newList(
                OAuth.OAUTH_CALLBACK, callbackURL,
                "scope", OAUTH_SCOPE);

      OAuthMessage response = 
                client.getRequestTokenResponse(accessor,null,callback);
      
      say("Response obtained.");
      String authorizationURL = OAuth.addParameters(
                accessor.consumer.serviceProvider.userAuthorizationURL,
                OAuth.OAUTH_TOKEN, accessor.requestToken,
                "scope", OAUTH_SCOPE);
      
      if (response.getParameter(OAuth.OAUTH_CALLBACK_CONFIRMED) == null) {
         say("No callback confirm - service provider is using 1.0, not 1.0a.");
         say("Adding callback as bare parameter.");
         authorizationURL = OAuth.addParameters(authorizationURL, callback);
      }
      else {
         authorizationURL = OAuth.addParameters(
                accessor.consumer.serviceProvider.userAuthorizationURL,
                OAuth.OAUTH_TOKEN, accessor.requestToken,
                "scope", OAUTH_SCOPE);
      }
      
      say("Request token: " + accessor.requestToken);
      say("Authorization URL: " + authorizationURL);
      return authorizationURL;
   }

   public void authenticate(String verifier) throws OrkutAdapterException {
      say("Trying to authenticate with verifier: " + verifier);
      try {
         authenticate_inner(verifier);
      }
      catch (Exception ex) {
         throw new OrkutAdapterException(
                "Orkut Adapter: Error authenticating.", ex);
      }
   }

   private void authenticate_inner(String verifier) throws Exception {
      say("Verifier code provided: " + verifier);
      say("Obtaining access token...");
      client.getAccessToken(accessor, null,
           OAuth.newList(OAuth.OAUTH_VERIFIER, verifier));
      say("Got access token   : " + accessor.accessToken);
      say("Access token secret: " + accessor.tokenSecret);
   }

   public String getAccessPass() {
      return accessor.accessToken + " " + accessor.tokenSecret;
   }
   
   public void setAccessPass(String accessPass) throws OrkutAdapterException {
      say("Access pass provided: '" + accessPass + "'");
      String[] p = accessPass.split(" ");
      if (p.length != 2)
         throw new OrkutAdapterException(
           "Access pass does not have correct format (token and secret)",null);

      say("Literal access token and secret supplied:");
      say("Access token  : " + p[0]);
      say("Token secret  : " + p[1]);
      accessor.accessToken = p[0];
      accessor.tokenSecret = p[1];
   }

   public BatchTransaction newBatch() throws OrkutAdapterException {
      try {
         return new BatchTransaction(new OrkutHttpRequestFactory(),
           new com.google.orkut.client.config.Config() {
              public String getRequestBaseUrl() { return requestURL; }
           });
       }
       catch (Exception ex) {
          throw new OrkutAdapterException("OrkutAdapter: error creating " +
              "batch transaction.", ex);
       }
   }

   public void submitBatch(BatchTransaction btx) throws OrkutAdapterException {
      try {
         HttpRequest req   = btx.build();
         byte[] body       = req.getRequestBody();
         String method     = req.getMethod();
         String baseURL    = req.getRequestBaseUrl();

         Collection reqParams = req.getParameters();
         ArrayList<Map.Entry<String,String>> oauthParams = null;
         if (reqParams != null && reqParams.size() > 0) {
            oauthParams = new ArrayList<Map.Entry<String,String>>();
            Iterator it = reqParams.iterator();
            while (it.hasNext()) {
               HttpRequest.Parameter parameter = 
                                (HttpRequest.Parameter) it.next();
               oauthParams.add(new OAuth.Parameter(parameter.getKey(),
                   parameter.getValue()));
            }
         }

         Collection reqHeaders = req.getHeaders();
         ArrayList<Map.Entry<String,String>> oauthHeaders = null;
         if (reqHeaders != null && reqHeaders.size() > 0) {
            oauthHeaders = new ArrayList<Map.Entry<String,String>>();
            Iterator it = reqHeaders.iterator();
            while (it.hasNext()) {
               HttpRequest.Header header = (HttpRequest.Header) it.next();
               oauthHeaders.add(new OAuth.Parameter(header.getName(),
                                        header.getValue()));
            }
         }

         OAuthMessage msg = new PostOAuthMessage(method, baseURL, 
                                        oauthParams, body);
         msg.addRequiredParameters(accessor);
         Iterator it = oauthHeaders.iterator();
         while (it.hasNext()) msg.getHeaders().add(
                (Map.Entry)it.next());

         Object accepted = accessor.consumer.getProperty(
                                        OAuthConsumer.ACCEPT_ENCODING);
         if (accepted != null)
            msg.getHeaders().add(new OAuth.Parameter(
                net.oauth.http.HttpMessage.ACCEPT_ENCODING, 
                accepted.toString()));

         OAuthMessage resp = client.invoke(msg, 
                                net.oauth.ParameterStyle.QUERY_STRING);

         String respBody = resp.readBodyAsString();
         btx.setResponse(respBody);
      }
      catch (Exception ex) {
         throw new OrkutAdapterException("OrkutAdapter: error sending " +
              "transaction batch.", ex);
      }
   }

   public void submitSingle(Transaction tx) throws OrkutAdapterException {
      BatchTransaction b = newBatch();
      b.add(tx);
      submitBatch(b);
   }

   public byte[] getCaptchaImage(OrkutError er) throws OrkutAdapterException {
      try {
         String url = "http://www.orkut.com" + er.captchaUrl();
         OAuthMessage msg = new OAuthMessage("GET", url, null);
         msg.addRequiredParameters(accessor);
         OAuthMessage resp = client.invoke(msg, 
                                net.oauth.ParameterStyle.QUERY_STRING);
         java.io.InputStream is = resp.getBodyAsStream();
         if (is == null)
            throw new OrkutAdapterException("No input stream in response.",
                                null);
         return readAllFromIS(is);
      }
      catch (Exception ex) {
         throw new OrkutAdapterException("Error getting captcha image", ex);
      }
   }

   public void saveCaptchaToFile(OrkutError er, String filePath)
                                             throws OrkutAdapterException {
      try {
         byte[] b = getCaptchaImage(er);
         java.io.FileOutputStream fos = new java.io.FileOutputStream(filePath);
         fos.write(b);
         fos.close();
      }
      catch (Exception ex) {
         throw new OrkutAdapterException("Error saving captcha to file " +
                        filePath, ex);
      }
   }

   private byte[] readAllFromIS(java.io.InputStream is) 
                        throws IOException {
      byte[] buf = new byte[8192];
      int i = 0;
      int n = 0, r;
      final int SIZE = 4096;

      while (true) {
         // invariant: buf[0..n-1] has the valid data
         // and buffer capacity is >= n

         // make sure buffer has capacity at least n + SIZE
         while (buf.length <= n + SIZE + 1)
            buf = resizeVec(buf, 2 * buf.length);

         // read into buf
         System.err.println("buf size " + String.valueOf(buf.length) +
                        " n = " + String.valueOf(n));
         r = is.read(buf, n, SIZE);
         System.err.println("r = " + r);

         if (r <= 0) break; // end of stream

         n += r;
      }

      // resize the byte array to its correct size
      return resizeVec(buf, n);
   }

   private static byte[] resizeVec(byte[] b, int newSize) {
      byte[] c = new byte[newSize];
      System.arraycopy(b,0,c,0,b.length > newSize ? newSize : b.length);
      return c;
   }


   public void submitBatchWithCaptcha(BatchTransaction btx, 
                                      OrkutError error, String answer)
                                      throws OrkutAdapterException {
      Transaction tx = 
                captchaTxFactory.answerCaptcha(error.captchaToken(),answer);
      btx.add(tx);
      submitBatch(btx);
   }

   class PostOAuthMessage extends OAuthMessage {
      private final byte[] body;
      public PostOAuthMessage(String method, String url,
                   Collection<? extends Map.Entry> parameters, byte[] body) {
         super(method, url, parameters);
         this.body = body;
      }

      public InputStream getBodyAsStream() throws IOException {
         return (body == null) ? null : new ByteArrayInputStream(body);
      }
   }
}
