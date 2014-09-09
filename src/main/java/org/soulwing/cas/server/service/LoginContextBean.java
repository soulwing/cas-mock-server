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

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

import org.soulwing.cas.server.MutableLoginContext;

/**
 * A {@link MutableLoginContext} implemented as an injectable session-scoped
 * bean.
 *
 * @author Carl Harris
 */
@SessionScoped
public class LoginContextBean implements MutableLoginContext, Serializable {

  private static final long serialVersionUID = -9017077425503417020L;

  private volatile String username;
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAuthentic() {
    return username != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getUsername() {
    return username;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAuthenticUsername(String username) {
    this.username = username;
  }

}
