/* EXPERIMENTAL (really) */
/* Copyright (c) 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.orkut.client.sample;

import com.google.orkut.client.api.BatchTransaction;
import com.google.orkut.client.api.Transaction;
import com.google.orkut.client.config.Config;
import com.google.orkut.client.config.FileConfig;
import com.google.orkut.client.transport.HttpRequest;
import com.google.orkut.client.transport.OrkutHttpRequestFactory;
import com.google.orkut.client.transport.HttpRequest.Header;
import com.google.orkut.client.transport.HttpRequest.Parameter;

import net.oauth.OAuth;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthMessage;
import net.oauth.OAuthServiceProvider;
import net.oauth.client.OAuthClient;
import net.oauth.client.httpclient4.HttpClient4;
import net.oauth.example.desktop.DesktopClient;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;

/**
 *
 * @author Sachin Shenoy
 */
public class Transport {
  private static final String TOKEN_SECRET = "token_secret";
  private static final String ACCESS_TOKEN = "accessToken";
  Properties props;
  private final String propFilename;
  private DesktopClient client;
  private BatchTransaction batchTransaction;
  private final OrkutHttpRequestFactory requestFactory;
  private final Config config;

  public Transport(String propfile) throws IOException {
    this.propFilename = propfile;
    this.props = new Properties();
    requestFactory = new OrkutHttpRequestFactory();
    config = new FileConfig();
    batchTransaction =  new BatchTransaction(requestFactory, config);
  }

  public void init() throws Exception {
    loadProperties();

    OAuthConsumer consumer = new OAuthConsumer(null,
        props.getProperty("consumerKey"),
        props.getProperty("consumerSecret"),
        new OAuthServiceProvider(
            props.getProperty("requestUrl"),
            props.getProperty("authorizationUrl"),
            props.getProperty("accessUrl")));
    consumer.setProperty(OAuthClient.PARAMETER_STYLE, net.oauth.ParameterStyle.QUERY_STRING);
    client = new DesktopClient(consumer, props.getProperty("scope"));
    client.setOAuthClient(new OAuthClient(new HttpClient4()));

    if (props.getProperty(ACCESS_TOKEN) != null) {
      client.setAccessToken(props.getProperty(ACCESS_TOKEN));
      client.setTokenSecret(props.getProperty(TOKEN_SECRET));
    }

    client.login();

    if (props.getProperty("saveAccessToken", "false").equals("true")) {
      props.put(ACCESS_TOKEN, client.getAccessToken());
      props.put(TOKEN_SECRET, client.getTokenSecret());
      updateProperties();
    }
  }

  synchronized public Transport add(Transaction transaction) {
    batchTransaction.add(transaction);
    return this;
  }

  synchronized public Transport run() throws IOException {
    HttpRequest request = batchTransaction.build();

    String response = sendRequest(request);
    batchTransaction.setResponse(response);

    // create a new batch now.
    batchTransaction = new BatchTransaction(requestFactory, config);
    return this;
  }

  public String sendRequest(HttpRequest request) throws IOException {
    byte[] body = request.getRequestBody();
    String method = request.getMethod();
    if (body.length < 512) {
      System.out.println("Request:" + new String(body));
    }
    OAuthMessage message;
    try {
      message = client.access(method, request.getRequestBaseUrl(),
          getParams(request), getHeaders(request), body);
    } catch (Exception e) {
      return "";
    }
    String response = message.readBodyAsString();
    System.out.println("Response:" + response);
    return response;
  }

  private void loadProperties() throws IOException {
    FileInputStream fileInputStream = new FileInputStream(propFilename);
    props.load(fileInputStream);
    fileInputStream.close();
  }

  private void updateProperties() throws IOException {
    FileOutputStream fileOutputStream = new FileOutputStream(propFilename);
    props.store(fileOutputStream, "writing access Token");
    fileOutputStream.close();
  }

  private Collection getParams(HttpRequest request) {
    Collection params = request.getParameters();
    if (params == null || params.size() == 0) {
      return null;
    }

    ArrayList<Entry<String, String>> oauthParams = new ArrayList<Entry<String, String>>();
    Iterator it = params.iterator();
    while (it.hasNext()) {
      Parameter parameter = (Parameter) it.next();
      oauthParams.add(new OAuth.Parameter(parameter.getKey(), parameter.getValue()));
    }
    return oauthParams;
  }

  private Collection getHeaders(HttpRequest request) {
    Collection params = request.getHeaders();
    if (params == null || params.size() == 0) {
      return null;
    }

    ArrayList<Entry<String, String>> headers = new ArrayList<Entry<String, String>>();
    Iterator it = params.iterator();
    while (it.hasNext()) {
      Header header = (Header) it.next();
      headers.add(new OAuth.Parameter(header.getName(), header.getValue()));
    }
    return headers;
  }
}
