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

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.soulwing.cas.server.service.TicketService;

/**
 * A JAX-RS endpoint that handles CAS protocol requests.
 *
 * @author Carl Harris
 */
@Path("/")
public class ProtocolEndpoint {

  @Inject
  protected TicketService ticketService;
    
  @GET
  @Path("/serviceValidate")
  public Response serviceValidate(@QueryParam("ticket") String ticket,
      @QueryParam("service") String service) {
    return Response.ok(ticketService.validate(ticket, service)).build();
  }

  @GET
  @Path("/proxyValidate")
  public Response proxyValidate(@QueryParam("ticket") String ticket,
      @QueryParam("service") String service) {
    return Response.ok(ticketService.validate(ticket, service)).build();
  }

  @GET
  @Path("/proxy")
  public Response proxy() {
    return null;
  }

  @GET
  @Path("/logout")
  public Response logout() {
    return null;
  }

}
