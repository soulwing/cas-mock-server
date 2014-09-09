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

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.startsWith;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.cas.server.LoginContext;
import org.soulwing.cas.server.Ticket;
import org.soulwing.cas.server.service.TicketService;

/**
 * Unit tests for {@link LoginServlet}.
 *
 * @author Carl Harris
 */
public class LoginServletTest {

  private static final String SERVICE_URL =
      "http://app.somewhere.net/myapp";
  
  private static final String LOGIN_URL = "someLoginUrl";

  private static final String QUERY_STRING = "someQuery";
  
  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();
  
  @Mock
  private LoginContext loginContext;
  
  @Mock
  private TicketService ticketService;
  
  @Mock
  private Ticket ticket;
  
  @Mock
  private HttpServletRequest request;
  
  @Mock
  private HttpServletResponse response;
  
  @Mock
  private RequestDispatcher dispatcher;
  
  private StringBuffer requestUrl = new StringBuffer();
  
  private LoginServlet servlet = new LoginServlet();
  
  @Before
  public void setUp() throws Exception {
    servlet.loginContext = loginContext;
    servlet.ticketService = ticketService;
    requestUrl.append(LOGIN_URL);
  }

  @Test
  public void testContextIsAuthentic() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(loginContext).isAuthentic();
        will(returnValue(true));
        oneOf(request).getParameter(ProtocolConstants.SERVICE_PARAM);
        will(returnValue(SERVICE_URL));
        oneOf(ticketService).issue();
        will(returnValue(ticket));
        oneOf(response).sendRedirect(
            with(allOf(startsWith(SERVICE_URL), 
                endsWith("?" + ProtocolConstants.TICKET_PARAM 
                    + "=" + ticket.toString()))));
      }
    });
    
    servlet.doGet(request, response);
  }
  
  @Test
  public void testContextIsNotAuthentic() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(loginContext).isAuthentic();
        will(returnValue(false));
        oneOf(request).getRequestURL();
        will(returnValue(requestUrl));
        oneOf(request).getQueryString();
        will(returnValue(QUERY_STRING));
        oneOf(request).setAttribute(LoginServlet.LOGIN_URL_ATTR, 
            LOGIN_URL + "?" + QUERY_STRING);
        oneOf(request).getRequestDispatcher(LoginServlet.LOGIN_FORM_PATH);
        will(returnValue(dispatcher));
        oneOf(dispatcher).forward(request, response);       
      }
    });

    servlet.doGet(request, response);
  }
  
}
