/*
 * File created on Mar 9, 2018
 *
 * Copyright (c) 2018 Carl Harris, Jr
 * and others as noted
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.soulwing.cas.server.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import java.util.Collections;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.cas.server.AttributeValue;
import org.soulwing.cas.server.AuthenticationFailureResponseBuilder;
import org.soulwing.cas.server.AuthenticationSuccessResponseBuilder;
import org.soulwing.cas.server.ProtocolError;
import org.soulwing.cas.server.ServiceResponse;
import org.soulwing.cas.server.ServiceResponseBuilderFactory;
import org.soulwing.cas.server.TicketState;
import org.soulwing.cas.server.ValidationRequest;

/**
 * Unit tests for {@link ValidationServiceBean}.
 *
 * @author Carl Harris
 */
public class ValidationServiceBeanTest {

  private static final String TICKET = "ticket";
  private static final String SERVICE = "service";
  private static final String USERNAME = "username";
  private static final AttributeValue ATTRIBUTE = AttributeValue.of("foo", "bar");

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private TicketService ticketService;

  @Mock
  private TicketState ticketState;

  @Mock
  private ServiceResponseBuilderFactory builderFactory;

  @Mock
  private AuthenticationSuccessResponseBuilder successBuilder;

  @Mock
  private AuthenticationFailureResponseBuilder failureBuilder;

  @Mock
  private ServiceResponse response;

  @Mock
  private AttributesService attributesService;

  private ValidationRequest request = new ValidationRequest();

  private ValidationServiceBean service = new ValidationServiceBean();

  @Before
  public void setUp() throws Exception {
    service.ticketService = ticketService;
    service.builderFactory = builderFactory;
    service.attributesService = attributesService;
    request.setTicket(TICKET);
    request.setService(SERVICE);
  }

  @Test
  public void testValidateValidTicket() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(ticketService).validate(TICKET);
        will(returnValue(ticketState));
        allowing(ticketState).getUsername();
        will(returnValue(USERNAME));
        oneOf(attributesService).getAttributes(USERNAME);
        will(returnValue(Collections.singletonList(ATTRIBUTE)));
        oneOf(builderFactory).createAuthenticationSuccessBuilder();
        will(returnValue(successBuilder));
        oneOf(successBuilder).user(USERNAME);
        will(returnValue(successBuilder));
        oneOf(successBuilder).attributes(Collections.singletonList(ATTRIBUTE));
        will(returnValue(successBuilder));
        oneOf(successBuilder).build();
        will(returnValue(response));
      }
    });

    assertThat(service.validate(request), is(sameInstance(response)));
  }

  @Test
  public void testValidateInvalidTicket() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(ticketService).validate(TICKET);
        will(returnValue(null));
        oneOf(builderFactory).createAuthenticationFailureBuilder();
        will(returnValue(failureBuilder));
        oneOf(failureBuilder).code(ProtocolError.INVALID_TICKET);
        will(returnValue(failureBuilder));
        oneOf(failureBuilder).message(with(any(String.class)));
        will(returnValue(failureBuilder));
        oneOf(failureBuilder).build();
        will(returnValue(response));
      }
    });

    assertThat(service.validate(request), is(sameInstance(response)));
  }


}
