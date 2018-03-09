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

import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.codec.binary.Base64;
import org.soulwing.cas.server.LoginContext;
import org.soulwing.cas.server.Ticket;
import org.soulwing.cas.server.TicketState;

/**
 * A {@link TicketService} implemented as an injectable bean.
 *
 * @author Carl Harris
 */
@ApplicationScoped
class TicketServiceBean implements TicketService {

  private final SecureRandom secureRandom = new SecureRandom();
  
  private final ConcurrentMap<String, TicketValue> ticketCache =
      new ConcurrentHashMap<>();
  
  @Inject
  LoginContext loginContext;
  
  @Override
  public Ticket issue() {
    
    TicketValue prior = null;
    TicketValue ticket = null;
    byte[] data = new byte[18];
    
    do {
      secureRandom.nextBytes(data);
      String id = Base64.encodeBase64URLSafeString(data);
      ticket = new TicketValue(id, loginContext.getUsername());
      prior = ticketCache.putIfAbsent(id, ticket);
    }
    while (prior != null);
    
    return ticket;
  }

  @Override
  public TicketState validate(String ticket) {
    return ticketCache.remove(ticket);
  }

  static class TicketValue implements Ticket, TicketState {
    private final String value;
    private final String username;

    /**
     * Constructs a new instance.
     * @param value ticket string
     * @param username username
     */
    TicketValue(String value, String username) {
      if (value == null) {
        throw new NullPointerException("value is required");
      }
      if (username == null) {
        throw new NullPointerException("username is required");
      }
      this.value = value;
      this.username = username;
    }

    /**
     * Gets the {@code username} property.
     * @return property value
     */
    public String getUsername() {
      return username;
    }

    @Override
    public int hashCode() {
      return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
      return this == obj ||
          obj instanceof TicketValue
              && this.value.equals(((TicketValue) obj).value);
    }

    @Override
    public String toString() {
      return value;
    }
    
  }

}
