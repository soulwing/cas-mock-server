package org.soulwing.cas.server.web;

import static org.junit.Assert.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

/**
 * Unit test for the {@link LogoutServlet} class.
 *
 * @author Michael Irwin
 */
public class LogoutServletTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  HttpServletRequest request;

  @Mock
  HttpServletResponse response;

  @Mock
  RequestDispatcher requestDispatcher;

  private LogoutServlet servlet = new LogoutServlet();

  @Test
  public void testDoGet() throws Exception {
    context.checking(new Expectations() { {
      oneOf(request).getRequestDispatcher("/logout/index.xhtml");
      will(returnValue(requestDispatcher));
      oneOf(requestDispatcher).forward(request, response);
    } });

    servlet.doGet(request, response);
  }

}