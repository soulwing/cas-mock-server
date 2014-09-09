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

import javax.enterprise.context.ApplicationScoped;

import org.soulwing.cas.server.AuthenticationFailureResponseBuilder;
import org.soulwing.cas.server.AuthenticationSuccessResponseBuilder;
import org.soulwing.cas.server.ProxyFailureResponseBuilder;
import org.soulwing.cas.server.ProxySuccessResponseBuilder;
import org.soulwing.cas.server.ServiceResponseBuilderFactory;

/**
 * A {@link ServiceResponseBuilderFactory} implemented as an injectable bean.
 *
 * @author Carl Harris
 */
@ApplicationScoped
public class ServiceResponseBuilderFactoryBean
    implements ServiceResponseBuilderFactory {

  /**
   * {@inheritDoc}
   */
  @Override
  public AuthenticationSuccessResponseBuilder createAuthenticationSuccessBuilder() {
    return new SimpleAuthenticationSuccessResponseBuilder();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AuthenticationFailureResponseBuilder createAuthenticationFailureBuilder() {
    return new SimpleAuthenticationFailureResponseBuilder();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProxySuccessResponseBuilder createProxySuccessBuilder() {
    return new SimpleProxySuccessResponseBuilder();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProxyFailureResponseBuilder createProxyFailureBuilder() {
    return new SimpleProxyFailureResponseBuilder();
  }

}
