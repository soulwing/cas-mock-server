/*
 * File created on Sep 9, 2014 
 *
 * Copyright (c) Carl Harris, Jr.
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
 *
 */
package org.soulwing.cas.server.protocol;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.Before;
import org.junit.Test;
import org.soulwing.cas.server.ServiceResponse;

/**
 * Unit tests for the marshaled protocol responses.
 *
 * @author Carl Harris
 */
public class XmlRoundTripTest {

  private static final String TICKET = "someTicket";
  private static final String MESSAGE = "someMessage";
  private static final int CODE = -1;
  private static final String PROXY = "someProxy";
  private static final String PGTIOU = "someTicketIOU";
  private static final String USER = "someUser";
  private Marshaller marshaller;
  private Unmarshaller unmarshaller;
  private Transformer transformer;
  
  private ServiceResponseBuilderFactoryBean builderFactory =
      new ServiceResponseBuilderFactoryBean();
  
  @Before
  public void setUp() throws Exception {
    JAXBContext jaxbContext = JAXBContextFactory.newContext();
    marshaller = jaxbContext.createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    unmarshaller = jaxbContext.createUnmarshaller();
    transformer = TransformerFactory.newInstance().newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
  }

  @Test
  public void testMarshalAuthenticationSuccessResponse() throws Exception {
    ServiceResponse outResponse = builderFactory.createAuthenticationSuccessBuilder()
        .user(USER)
        .proxyGrantingTicket(PGTIOU)
        .proxy(PROXY)
        .build();
    
    
    ServiceResponse inResponse = xmlRoundTrip(outResponse);
    assertThat(inResponse, is(instanceOf(ServiceResponseBase.class)));
    assertThat(((ServiceResponseBase) inResponse).result, 
        is(instanceOf(AuthenticationSuccess.class)));
    
    AuthenticationSuccess success = (AuthenticationSuccess) 
        ((ServiceResponseBase) inResponse).result;
    assertThat(success.user, is(equalTo(USER)));
    assertThat(success.proxyGrantingTicket, is(equalTo(PGTIOU)));
    assertThat(success.proxies, contains(PROXY));
  }

  @Test
  public void testMarshalAuthenticationFailureResponse() throws Exception {
    ServiceResponse outResponse = builderFactory.createAuthenticationFailureBuilder()
          .code(CODE)
          .message(MESSAGE)
          .build();
    
    ServiceResponse inResponse = xmlRoundTrip(outResponse);
    assertThat(inResponse, is(instanceOf(ServiceResponseBase.class)));
    assertThat(((ServiceResponseBase) inResponse).result, 
        is(instanceOf(AuthenticationFailure.class)));

    AuthenticationFailure failure = (AuthenticationFailure) 
        ((ServiceResponseBase) inResponse).result;
    assertThat(failure.code, is(equalTo(CODE)));
    assertThat(failure.message, is(equalTo(MESSAGE)));
  }

  @Test
  public void testMarshalProxySuccessResponse() throws Exception {
    ServiceResponse outResponse = builderFactory.createProxySuccessBuilder()
        .proxyTicket(TICKET)
        .build();
    
    ServiceResponse inResponse = xmlRoundTrip(outResponse);
    assertThat(inResponse, is(instanceOf(ServiceResponseBase.class)));
    assertThat(((ServiceResponseBase) inResponse).result, 
        is(instanceOf(ProxySuccess.class)));

    ProxySuccess success = (ProxySuccess) 
        ((ServiceResponseBase) inResponse).result;
    assertThat(success.proxyTicket, is(equalTo(TICKET)));
  }
  
  @Test
  public void testMarshalProxyFailureResponse() throws Exception {
    ServiceResponse outResponse = builderFactory.createProxyFailureBuilder()
        .code(CODE)
        .message(MESSAGE)
        .build();
    
    ServiceResponse inResponse = xmlRoundTrip(outResponse);
    assertThat(inResponse, is(instanceOf(ServiceResponseBase.class)));
    assertThat(((ServiceResponseBase) inResponse).result, 
        is(instanceOf(ProxyFailure.class)));

    ProxyFailure failure = (ProxyFailure) 
        ((ServiceResponseBase) inResponse).result;
    assertThat(failure.code, is(equalTo(CODE)));
    assertThat(failure.message, is(equalTo(MESSAGE)));
  }

  private ServiceResponse xmlRoundTrip(ServiceResponse response) 
      throws JAXBException, TransformerException {
    DOMResult output = new DOMResult();
    marshaller.marshal(response, output);
    transformer.transform(new DOMSource(output.getNode()),
        new StreamResult(System.out));
    return (ServiceResponse) unmarshaller.unmarshal(output.getNode());
  }

}
