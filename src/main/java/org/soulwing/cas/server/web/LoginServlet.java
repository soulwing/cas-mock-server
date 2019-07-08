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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.inject.Inject;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.UriBuilder;

import org.soulwing.cas.server.LoginContext;
import org.soulwing.cas.server.domain.CredentialBean;
import org.soulwing.cas.server.protocol.ProtocolConstants;
import org.soulwing.cas.server.service.AuthenticationException;
import org.soulwing.cas.server.service.LoginService;
import org.soulwing.cas.server.service.NotAuthenticException;
import org.soulwing.cas.server.service.TicketService;

/**
 * A {@link Servlet} that handles CAS login requests.
 *
 * @author Carl Harris
 */
public class LoginServlet extends HttpServlet {

  private static final long serialVersionUID = 4060870488735584997L;

  static final String LOGIN_FORM_PATH = "/login/index.xhtml";
  
  static final String LOGIN_URL_ATTR = "loginUrl";
  
  @Inject
  LoginContext loginContext;
  
  @Inject
  TicketService ticketService;

  @Inject
  LoginService loginService;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException, ServletException {
    if (loginContext.isAuthentic()) {
      response.sendRedirect(createServiceUriBuilder(
          request.getParameter(ProtocolConstants.SERVICE_PARAM))
          .queryParam(ProtocolConstants.TICKET_PARAM, "{ticket}")
          .build(ticketService.issue()).toASCIIString());
    }
    else {
      request.setAttribute(LOGIN_URL_ATTR, request.getRequestURL()
          .append("?").append(request.getQueryString())
          .toString());
      request.getRequestDispatcher(LOGIN_FORM_PATH).forward(request, response);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    final CredentialBean credential = new CredentialBean();
    credential.setUsername(request.getParameter("username"));
    credential.setPassword(request.getParameter("password"));
    try {
      loginService.authenticate(credential);
      response.sendRedirect(request.getRequestURL()
          .append("?").append(request.getQueryString())
          .toString());
    }
    catch (NotAuthenticException ex) {
      response.setStatus(401);
      response.setContentType("text/plain");
      response.getWriter().write("Unauthorized\n");
    }
    catch (AuthenticationException ex) {
      throw new ServletException(ex);
    }
  }

  private UriBuilder createServiceUriBuilder(String service)
      throws ServletException {
    if (service == null) {
      throw new ServletException("service URL is required");
    }
    try {
      URI uri = new URI(service);
      return UriBuilder.fromUri(uri);
    }
    catch (URISyntaxException ex) {
      throw new ServletException("invalid service URL", ex);
    }
  }
  
}
