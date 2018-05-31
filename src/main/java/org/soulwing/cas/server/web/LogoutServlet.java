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
import org.soulwing.cas.server.protocol.ProtocolConstants;
import org.soulwing.cas.server.service.TicketService;

/**
 * A {@link Servlet} that handles CAS logout requests.
 *
 * @author Michael Irwin
 */
public class LogoutServlet extends HttpServlet {

  private static final long serialVersionUID = 847238932859235L;

  static final String LOGOUT_URL_PATH = "/logout/index.xhtml";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException, ServletException {
    request.getRequestDispatcher(LOGOUT_URL_PATH).forward(request, response);
  }

}
