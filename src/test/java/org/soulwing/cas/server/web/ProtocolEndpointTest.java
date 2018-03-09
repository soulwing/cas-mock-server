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
package org.soulwing.cas.server.web;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import javax.ws.rs.core.Response;

import org.hamcrest.Matchers;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.cas.server.ServiceResponse;
import org.soulwing.cas.server.ValidationRequest;
import org.soulwing.cas.server.service.ValidationService;

/**
 * Unit tests for {@link ProtocolEndpoint}.
 *
 * @author Carl Harris
 */
public class ProtocolEndpointTest {

  private static final String TICKET = "someTicket";

  private static final String SERVICE = "someService";

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();
  
  @Mock
  private ValidationService validationService;
  
  @Mock
  private ServiceResponse serviceResponse;
  
  private ProtocolEndpoint endpoint = new ProtocolEndpoint();
  
  @Before
  public void setUp() throws Exception {
    endpoint.validationService = validationService;
  }

  @Test
  public void testServiceValidate() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(validationService).validate(with(
            Matchers.<ValidationRequest>allOf(
                hasProperty("ticket", equalTo(TICKET)),
                hasProperty("service", equalTo(SERVICE)))));
        will(returnValue(serviceResponse));
      }
    });
    
    Response response = endpoint.serviceValidate(TICKET, SERVICE);
    assertThat(response.getStatus(), is(equalTo(200)));
    assertThat((ServiceResponse) response.getEntity(),
        is(sameInstance(serviceResponse)));
  }

  @Test
  public void testProxyValidate() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(validationService).validate(with(
            Matchers.<ValidationRequest>allOf(
                hasProperty("ticket", equalTo(TICKET)),
                hasProperty("service", equalTo(SERVICE)))));
        will(returnValue(serviceResponse));
      }
    });
    
    Response response = endpoint.proxyValidate(TICKET, SERVICE);
    assertThat(response.getStatus(), is(equalTo(200)));
    assertThat((ServiceResponse) response.getEntity(),
        is(sameInstance(serviceResponse)));
  }
  

}
