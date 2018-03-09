/*
 * File created on Mar 9, 2018
 *
 * Copyright (c) 2018 Carl Harris, Jr
 * and others as noted
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.soulwing.cas.server.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.soulwing.cas.server.LoginContext;
import org.soulwing.cas.server.Ticket;
import org.soulwing.cas.server.TicketState;

/**
 * Unit tests for {@link TicketServiceBean}.
 *
 * @author Carl Harris
 */
public class TicketServiceBeanTest {

  private static final String USERNAME = "username";

  @Rule
  public final JUnitRuleMockery context = new JUnitRuleMockery();

  @Mock
  private LoginContext loginContext;

  private TicketServiceBean service = new TicketServiceBean();

  @Before
  public void setUp() throws Exception {
    service.loginContext = loginContext;
  }

  @Test
  public void testIssueAndValidate() throws Exception {
    context.checking(new Expectations() {
      {
        atLeast(1).of(loginContext).getUsername();
        will(returnValue(USERNAME));
      }
    });

    final Ticket ticket = service.issue();
    assertThat(ticket.toString(), is(not(nullValue())));

    final TicketState state = service.validate(ticket.toString());
    assertThat(state, is(not(nullValue())));
    assertThat(state.getUsername(), is(equalTo(USERNAME)));

  }

  @Test
  public void testAttemptTicketReuse() throws Exception {
    context.checking(new Expectations() {
      {
        atLeast(1).of(loginContext).getUsername();
        will(returnValue(USERNAME));
      }
    });

    final Ticket ticket = service.issue();
    assertThat(ticket.toString(), is(not(nullValue())));

    final TicketState state = service.validate(ticket.toString());
    assertThat(state, is(not(nullValue())));

    assertThat(service.validate(ticket.toString()), is(nullValue()));
  }


}
