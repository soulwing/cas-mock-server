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
package org.soulwing.cas.server.web;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.Collections;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.cas.server.Credential;
import org.soulwing.cas.server.CredentialFactory;
import org.soulwing.cas.server.service.AuthenticationException;
import org.soulwing.cas.server.service.LoginService;
import org.soulwing.cas.server.service.NotAuthenticException;

/**
 * Unit tests for {@link LoginBean}.
 *
 * @author Carl Harris
 */
public class LoginBeanTest {

  private static final String LOGIN_URL = "someLoginUrl";
  
  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery() {
    {
      setImposteriser(ClassImposteriser.INSTANCE);
    }
  };
  
  @Mock
  private CredentialFactory credentialFactory;
  
  @Mock
  private Credential credential;
  
  @Mock
  private LoginService loginService;
  
  @Mock
  private Errors errors;
  
  @Mock
  private FacesContext facesContext;
  
  @Mock
  private ExternalContext externalContext;
  
  private LoginBean bean = new LoginBean();
  
  @Before
  public void setUp() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(credentialFactory).newCredential();
        will(returnValue(credential));
      }
    });
    
    bean.credentialFactory = credentialFactory;
    bean.loginService = loginService;
    bean.errors = errors;
    bean.facesContext = facesContext;
    bean.init();
  }

  @Test
  public void testWhenAuthentic() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(loginService).authenticate(credential);
        will(returnValue(null));
        oneOf(facesContext).getExternalContext();
        will(returnValue(externalContext));
        oneOf(externalContext).redirect(with(LOGIN_URL));
        oneOf(facesContext).responseComplete();
      }
    });
    
    bean.setLoginUrl(LOGIN_URL);
    assertThat(bean.login(), is(nullValue()));
  }
  
  @Test
  public void testWhenNotAuthentic() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(loginService).authenticate(credential);
        will(throwException(new NotAuthenticException()));
        oneOf(errors).addError(with("invalidUsernameOrPassword"),
            with(emptyArray()));
      }
    });
    
    assertThat(bean.login(), is(nullValue()));
  }
  
  @Test
  public void testWhenAuthenticationFailure() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(loginService).authenticate(credential);
        will(throwException(new AuthenticationException()));
      }
    });

    assertThat(bean.login(), is(equalTo(LoginBean.FAILURE_OUTCOME_ID)));
  }
  
  @Test
  public void testGetLoginUrl() throws Exception {
    context.checking(new Expectations() {
      {
        oneOf(facesContext).getExternalContext();
        will(returnValue(externalContext));
        oneOf(externalContext).getRequestMap();
        will(returnValue(Collections.singletonMap(LoginServlet.LOGIN_URL_ATTR, 
            LOGIN_URL)));
      }
    });
    
    assertThat(bean.getLoginUrl(), is(equalTo(LOGIN_URL)));
    assertThat(bean.getLoginUrl(), is(equalTo(LOGIN_URL)));
  }
  
}
