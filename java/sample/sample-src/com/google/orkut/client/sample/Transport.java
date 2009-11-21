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
import com.google.orkut.client.api.Util;

import net.oauth.OAuthConsumer;
import net.oauth.OAuthMessage;
import net.oauth.OAuthServiceProvider;
import net.oauth.client.OAuthClient;
import net.oauth.client.httpclient4.HttpClient4;
import net.oauth.example.desktop.DesktopClient;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author Sachin Shenoy
 */
public class Transport {
  private static final String TOKEN_SECRET = "token_secret";
  private static final String SERVER_URL = "serverUrl";
  private static final String ACCESS_TOKEN = "accessToken";
  Properties props;
  private final String propFilename;
  private DesktopClient client;
  private BatchTransaction batchTransaction;

  public Transport(String propfile) {
    this.propFilename = propfile;
    this.props = new Properties();
    batchTransaction =  new BatchTransaction();
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
    client = new DesktopClient(consumer);
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
    String response = sendRequest(
        batchTransaction.getContentType(),
        batchTransaction.getRequestBody(),
        null,
        Util.getHttpVersionHeaderName(),
        Util.getHttpVersionHeaderValue());
    batchTransaction.setResponse(response);

    // create a new batch now.
    batchTransaction = new BatchTransaction();
    return this;
  }

  public String sendRequest(String contentType, byte[] body,
      Collection<? extends Map.Entry> parameters, String versionHeaderName,
      String versionHeaderValue) throws IOException {
    if (body.length < 512) {
      System.out.println("Request:" + new String(body));
    }
    OAuthMessage message;
    try {
      message = client.access(OAuthMessage.POST,
              props.getProperty(SERVER_URL), parameters, contentType, versionHeaderName,
              versionHeaderValue, body);
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
}
