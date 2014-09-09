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

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.soulwing.cas.server.Credential;
import org.soulwing.cas.server.MutableLoginContext;

/**
 * A {@link LoginService} implemented as a simple injectable bean.
 *
 * @author Carl Harris
 */
@ApplicationScoped
public class LoginServiceBean implements LoginService {

  static final String FAIL = "fail";
  
  @Inject
  protected MutableLoginContext loginContext;
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void authenticate(Credential credential)
      throws NotAuthenticException, AuthenticationException {
    if (StringUtils.isEmpty(credential.getUsername())) {
      throw new NotAuthenticException();
    }
    if (StringUtils.isEmpty(credential.getPassword())) {
      throw new NotAuthenticException();
    }
    if (FAIL.equals(credential.getPassword())) {
      throw new AuthenticationException();
    }
    if (!credential.getPassword().equals(credential.getUsername())) {
      throw new NotAuthenticException();
    }
    
    loginContext.setAuthenticUsername(credential.getUsername());
  }

}
