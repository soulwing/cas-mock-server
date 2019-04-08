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
package org.soulwing.cas.server;

import java.util.List;

/**
 * A builder for a successful authentication response.
 *
 * @author Carl Harris
 */
public interface AuthenticationSuccessResponseBuilder 
    extends ServiceResponseBuilder {

  /**
   * Sets the user for this response.
   * @param user the user to set
   * @return this builder
   */
  AuthenticationSuccessResponseBuilder user(String user);
  
  /**
   * Sets the proxy granting ticket IOU for this response.
   * @param pgtiou the ticket IOU to set
   * @return this builder
   */
  AuthenticationSuccessResponseBuilder proxyGrantingTicket(String pgtiou);
  
  /**
   * Adds a named proxy to the response.
   * @param proxy the proxy to add
   * @return this builder
   */
  AuthenticationSuccessResponseBuilder proxy(String proxy);

  /**
   * Adds a collection of named attributes to the response.
   * @param attributes the attributes to add
   * @return this builder
   */
  AuthenticationSuccessResponseBuilder attributes(List<AttributeValue> attributes);


}
