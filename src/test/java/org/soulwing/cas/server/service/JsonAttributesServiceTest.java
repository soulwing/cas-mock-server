/*
 * File created on Apr 8, 2019
 *
 * Copyright (c) 2019 Carl Harris, Jr
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
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.soulwing.cas.server.AttributeValue;

/**
 * Unit tests for {@link JsonAttributesService}.
 *
 * @author Carl Harris
 */
public class JsonAttributesServiceTest {

  private JsonAttributesService service;

  @Before
  public void setUp() throws Exception {
    service = new JsonAttributesService();
    service.init();
  }

  @Test
  public void testGetAttributesForFletcher() throws Exception {
    final List<AttributeValue> attributes = service.getAttributes("fletcher");
    assertThat(attributes.contains(
        AttributeValue.of("uid", 13L)), is(true));
    assertThat(attributes.contains(
        AttributeValue.of("active", true)), is(true));
    assertThat(attributes.contains(
        AttributeValue.of("mailPreferredAddress", "fletcher@crest.edu")), is(true));
    assertThat(attributes.contains(
        AttributeValue.of("displayName", "Jordan Fletcher")), is(true));
    assertThat(attributes.contains(
        AttributeValue.of("groupMembership", "valid-user")), is(true));
    assertThat(attributes.contains(
        AttributeValue.of("groupMembership", "research.summit.app.pre-award")), is(true));
    assertThat(attributes.contains(
        AttributeValue.of("groupMembership", "research.summit.app.pre-award-manager")), is(true));
    assertThat(attributes.contains(
        AttributeValue.of("groupMembership", "research.summit.app.app-admin")), is(true));
  }

}
