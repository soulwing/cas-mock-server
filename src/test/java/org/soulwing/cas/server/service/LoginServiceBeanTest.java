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

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.cas.server.Credential;
import org.soulwing.cas.server.MutableLoginContext;

/**
 * Unit tests for {@link LoginServiceBean}.
 *
 * @author Carl Harris
 */
public class LoginServiceBeanTest {

  private static final String USERNAME = "somebody";

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();
  
  @Mock
  private Credential credential;
  
  @Mock
  private MutableLoginContext loginContext;
  
  private LoginServiceBean service = new LoginServiceBean();
  
  @Before
  public void setUp() throws Exception {
    service.loginContext = loginContext;
  }

  @Test
  public void testWhenUsernameEqualsPassword() throws Exception {
    context.checking(new Expectations() {
      {
        atLeast(1).of(credential).getUsername();
        will(returnValue(USERNAME));
        atLeast(1).of(credential).getPassword();
        will(returnValue(USERNAME));
        oneOf(loginContext).setAuthenticUsername(USERNAME);
      }
    });
    
    service.authenticate(credential);
  }

  @Test(expected = NotAuthenticException.class)
  public void testWhenUsernameNotEqualToPassword() throws Exception {
    context.checking(new Expectations() {
      {
        atLeast(1).of(credential).getUsername();
        will(returnValue(USERNAME));
        atLeast(1).of(credential).getPassword();
        will(returnValue("something else"));
      }
    });
    
    service.authenticate(credential);
  }

  @Test(expected = AuthenticationException.class)
  public void testWhenPasswordEqualsFail() throws Exception {
    context.checking(new Expectations() {
      {
        atLeast(1).of(credential).getUsername();
        will(returnValue(USERNAME));
        atLeast(1).of(credential).getPassword();
        will(returnValue("fail"));
      }
    });
    
    service.authenticate(credential);
  }

  @Test(expected = NotAuthenticException.class)
  public void testWhenUsernameIsNull() throws Exception {
    context.checking(new Expectations() {
      {
        atLeast(1).of(credential).getUsername();
        will(returnValue(null));
        allowing(credential).getPassword();
        will(returnValue(USERNAME));
      }
    });

    service.authenticate(credential);
  }

  @Test(expected = NotAuthenticException.class)
  public void testWhenPasswordIsNull() throws Exception {
    context.checking(new Expectations() {
      {
        allowing(credential).getUsername();
        will(returnValue(USERNAME));
        atLeast(1).of(credential).getPassword();
        will(returnValue(null));
      }
    });

    service.authenticate(credential);
  }

}
