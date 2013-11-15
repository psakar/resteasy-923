/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jboss.as.tests.resteasy923;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.tests.resteasy923.model.EmailAleadyRegisteredException;
import org.jboss.as.tests.resteasy923.model.User;
import org.jboss.as.tests.resteasy923.model.UserResource;
import org.jboss.as.tests.resteasy923.model.UserResourceEJBImpl;
import org.jboss.as.tests.resteasy923.model.UserResourceImpl;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
@RunAsClient
public class JaxRSResourceValidationIT
{
  private static final String EJB = "EJB";
  private static final String POJO = "POJO";
  private static final String name = "resteasy-923";
  private HttpClient client;

  static String getServerBindAddress() {
    String serverBindAddress = System.getProperty("jboss.bind.address");
    return serverBindAddress == null || serverBindAddress.isEmpty() ? "localhost" : serverBindAddress;
  }

  static String getServerBindPort() {
    String serverBindport = System.getProperty("jboss.bind.port");
    return serverBindport == null || serverBindport.isEmpty() ? "8080" : serverBindport;
  }

  @Deployment(name = EJB, testable=false)
  static WebArchive createDeploymentEJB() throws Exception {

    String resourcePath = "src/main/webapp";
    WebArchive archive = ShrinkWrap
        .create(WebArchive.class, name + EJB + ".war")
        .addAsWebInfResource(new File(resourcePath + "/WEB-INF", "web.xml"))
        .addAsWebInfResource(new File(resourcePath + "/WEB-INF", "beans.xml"))

        .addClass(EmailAleadyRegisteredException.class)

        .addClass(User.class)
        .addClass(UserResource.class)
        .addClass(UserResourceEJBImpl.class)
         ;
    archive.as(ZipExporter.class).exportTo(new File("/tmp", archive.getName()), true);
    return archive;
  }

  @Deployment(name = POJO, testable=false)
  static WebArchive createDeploymentPOJO() throws Exception {

    String resourcePath = "src/main/webapp";
    WebArchive archive = ShrinkWrap
        .create(WebArchive.class, name + POJO + ".war")
        .addAsWebInfResource(new File(resourcePath + "/WEB-INF", "web.xml"))
        .addAsWebInfResource(new File(resourcePath + "/WEB-INF", "beans.xml"))

        .addClass(EmailAleadyRegisteredException.class)

        .addClass(User.class)
        .addClass(UserResource.class)
        .addClass(UserResourceImpl.class)
         ;
    archive.as(ZipExporter.class).exportTo(new File("/tmp", archive.getName()), true);
    return archive;
  }

  @Before
  public void before() throws Exception {
    client = new DefaultHttpClient();
  }

  @After
  public void after() {
    client.getConnectionManager().shutdown();
  }

  @Test
  @OperateOnDeployment(EJB)
  public void testEJBResourceInfo() throws Exception
  {
    String URL = "http://" + getServerBindAddress() + ":" + getServerBindPort() + "/" + name + EJB;
    HttpGet request = new HttpGet(URL + "/rest/user/");

    HttpResponse response = client.execute(request);

    assertEquals(UserResourceEJBImpl.class.getName(), EntityUtils.toString(response.getEntity()));
  }

  @Test
  @OperateOnDeployment(POJO)
  public void testPOJOResourceInfo() throws Exception
  {
    String URL = "http://" + getServerBindAddress() + ":" + getServerBindPort() + "/" + name + EJB;
    HttpGet request = new HttpGet(URL + "/rest/user/");

    HttpResponse response = client.execute(request);

    assertEquals(UserResourceEJBImpl.class.getName(), EntityUtils.toString(response.getEntity()));
  }

  @Test
  @OperateOnDeployment(POJO)
  public void testPOJOResourceIsValidated() throws Exception
  {
// curl -d email=foo@example.com -d password=bar http://localhost:8080/jboss-as-helloworld-rs/rest/user/

    List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
    parameters.add(new BasicNameValuePair("email", "foo@example.com"));
    parameters.add(new BasicNameValuePair("password", "bar"));
    String URL = "http://" + getServerBindAddress() + ":" + getServerBindPort() + "/" + name + POJO;
    HttpPost request = new HttpPost(URL + "/rest/user/");
    request.setEntity(new UrlEncodedFormEntity(parameters));

    HttpResponse response = client.execute(request);

    assertEquals("!!" + User.class.getName() + " {email: foo@example.com, password: bar}\n", EntityUtils.toString(response.getEntity()));
  }

  @Test
  @OperateOnDeployment(EJB)
  public void testEJBResourceIsValidated() throws Exception
  {
// curl -d email=foo@example.com -d password=bar http://localhost:8080/jboss-as-helloworld-rs/rest/user/

    List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
    parameters.add(new BasicNameValuePair("email", "foo@example.com"));
    parameters.add(new BasicNameValuePair("password", "bar"));
    String URL = "http://" + getServerBindAddress() + ":" + getServerBindPort() + "/" + name + EJB;
    HttpPost request = new HttpPost(URL + "/rest/user/");
    request.setEntity(new UrlEncodedFormEntity(parameters));

    HttpResponse response = client.execute(request);

    assertEquals("!!" + User.class.getName() + " {email: foo@example.com, password: bar}\n", EntityUtils.toString(response.getEntity()));
  }
}
