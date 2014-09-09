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
package org.soulwing.cas.server.domain;

import java.io.Serializable;

import org.soulwing.cas.server.Credential;

/**
 * A {@link Credential} implemented as a simple bean.
 *
 * @author Carl Harris
 */
public class CredentialBean implements Serializable, Credential {

  private static final long serialVersionUID = -4860356834288449163L;
  
  private String username;
  private String password;
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String getUsername() {
    return username;
  }

  /**
   * Sets the {@code username} property.
   * @param username the value to set
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPassword() {
    return password;
  }

  /**
   * Sets the {@code password} property.
   * @param password the value to set
   */
  public void setPassword(String password) {
    this.password = password;
  }

}
