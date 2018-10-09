package org.soulwing.cas.server.web;

import static org.junit.Assert.*;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Unit test for the {@link LogoutBean} class.
 *
 * @author Michael Irwin
 */
public class LogoutBeanTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  {{
    context.setImposteriser(ClassImposteriser.INSTANCE);
  }}

  private LogoutBean bean = new LogoutBean();

  @Mock
  FacesContext facesContext;

  @Mock
  ExternalContext externalContext;

  @Before
  public void setUp() {
    bean.facesContext = facesContext;
  }

  @Test
  public void testLogout() {
    context.checking(new Expectations() { {
      oneOf(facesContext).getExternalContext();
      will(returnValue(externalContext));
      oneOf(externalContext).invalidateSession();
    } });

    bean.logout();
  }
}