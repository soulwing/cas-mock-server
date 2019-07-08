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
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.soulwing.cas.server.AttributeValue;

/**
 * Unit tests for {@link JsonAttributesServiceProvider}.
 *
 * @author Carl Harris
 */
public class JsonAttributesServiceProviderTest {

  private static final String URL = "classpath:attributes.json";
  private final Map<String, String> env = new HashMap<>();

  private Environment environment = Environment.getInstance(
      new Environment.Provider() {
        @Override
        public String getEnv(String name) {
          return env.get(name);
        }
      });

  private JsonAttributesServiceProvider provider =
      new JsonAttributesServiceProvider(environment);

  @Test
  public void testGetInstanceWhenNotConfigured() throws Exception {
    assertThat(provider.getInstance(), is(nullValue()));
  }

  @Test
  public void testGetInstanceWhenResourceNotFound() throws Exception {
    env.put(JsonAttributesServiceProvider.ATTRIBUTES_JSON_URL, URL + "-DOES_NOT_EXIST");
    assertThat(provider.getInstance(), is(nullValue()));
  }


  @Test
  public void testGetInstanceWhenConfigured() throws Exception {
    env.put(JsonAttributesServiceProvider.ATTRIBUTES_JSON_URL, URL);
    final AttributesService service = provider.getInstance();
    assertThat(service, is(not(nullValue())));

    final List<AttributeValue> attributes = service.getAttributes("fletcher");
    assertThat(attributes.contains(
        AttributeValue.of("uid", 1L)), is(true));
    assertThat(attributes.contains(
        AttributeValue.of("displayName", "Jordan Fletcher")), is(true));
    assertThat(attributes.contains(
        AttributeValue.of("affiliation", "EMPLOYEE")), is(true));
    assertThat(attributes.contains(
        AttributeValue.of("groupMembership", "valid-user")), is(true));
    assertThat(attributes.contains(
        AttributeValue.of("groupMembership", "admin")), is(true));
    assertThat(attributes.contains(
        AttributeValue.of("groupMembership", "power-user")), is(true));
  }

}
