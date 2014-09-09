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
package org.soulwing.cas.server.protocol;

import org.soulwing.cas.server.ProxySuccessResponseBuilder;
import org.soulwing.cas.server.ServiceResponse;

/**
 * A simple {@link ProxySuccessResponseBuilder}.
 *
 * @author Carl Harris
 */
class SimpleProxySuccessResponseBuilder
    extends AbstractServiceResponseBuilder
    implements ProxySuccessResponseBuilder {

  private final ProxySuccess result = new ProxySuccess();
  
  /**
   * {@inheritDoc}
   */
  @Override
  public ProxySuccessResponseBuilder proxyTicket(String ticket) {
    result.proxyTicket = ticket;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ServiceResponse build() {
    ServiceResponseBase response = new ServiceResponseBase();
    response.result = result;
    return response;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ServiceResult getResult() {
    return result;
  }

}
