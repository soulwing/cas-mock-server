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
package org.soulwing.cas.server.service;

import org.soulwing.cas.server.ServiceResponse;
import org.soulwing.cas.server.Ticket;

/**
 * A service that issues and validates authentication tickets.
 *
 * @author Carl Harris
 */
public interface TicketService {

  /**
   * Issues an authentication ticket.
   * @return authentication ticket.
   */
  Ticket issue();
  
  /**
   * Validates an authentication ticket presented by a user to a service.
   * @param ticket the ticket to validate
   * @param service the service to which the ticket was presented
   */
  ServiceResponse validate(String ticket, String service);
  
}
