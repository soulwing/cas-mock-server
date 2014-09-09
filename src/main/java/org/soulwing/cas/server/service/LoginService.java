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

import org.soulwing.cas.server.Credential;

/**
 * A service that provides login authentication.
 *
 * @author Carl Harris
 */
public interface LoginService {

  /**
   * Authenticates the given credential.
   * @param credential the credential to authenticate
   * @throws NotAuthenticException if the credential is not valid
   * @throws AuthenticationException if authentication fails for some other
   *    reason
   */
  void authenticate(Credential credential)
      throws NotAuthenticException, AuthenticationException;

}
