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

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.codec.binary.Base64;
import org.soulwing.cas.server.LoginContext;
import org.soulwing.cas.server.ServiceResponse;
import org.soulwing.cas.server.ServiceResponseBuilderFactory;
import org.soulwing.cas.server.Ticket;

/**
 * A {@link TicketService} implemented as an injectable bean.
 *
 * @author Carl Harris
 */
@ApplicationScoped
public class TicketServiceBean implements TicketService {

  private final SecureRandom secureRandom = new SecureRandom();
  
  private final ConcurrentHashMap<String, TicketValue> ticketCache = 
      new ConcurrentHashMap<>();
  
  @Inject
  LoginContext loginContext;
  
  @Inject
  ServiceResponseBuilderFactory builderFactory;
  
  /**
   * {@inheritDoc}
   */
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

  /**
   * {@inheritDoc}
   */
  @Override
  public ServiceResponse validate(String ticket, String service) {
    TicketValue target = ticketCache.get(ticket);
    System.out.println("found ticket " + target);
    if (target == null) {
      return builderFactory.createAuthenticationFailureBuilder()
          .code(401)
          .message("invalid ticket")
          .build();
    }

    ticketCache.remove(ticket);
    return builderFactory.createAuthenticationSuccessBuilder()
        .user(target.getUsername())
        .build();
  
  }

  static class TicketValue implements Ticket {
    private final String value;
    private final String username;

    /**
     * Constructs a new instance.
     * @param value
     */
    public TicketValue(String value, String username) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
      return value.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
      if (this == obj) return true;
      if (!(this instanceof TicketValue)) return false;
      return this.value.equals(((TicketValue) obj).value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
      return value.toString();
    }
    
  }

}
