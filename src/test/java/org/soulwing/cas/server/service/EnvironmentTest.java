/*
 * File created on Jul 3, 2019
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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Unit tests for {@link Environment}.
 *
 * @author Carl Harris
 */
public class EnvironmentTest {

  private final Map<String, String> env = new HashMap<>();

  private Environment environment = Environment.getInstance(
      new Environment.Provider() {
    @Override
    public String getEnv(String name) {
      return env.get(name);
    }
  });

  @Test
  public void testGetEnvString() {
    env.put("name", "value");
    assertThat(environment.getEnv("name"), is(equalTo("value")));
  }

  @Test
  public void testGetEnvLong() {
    env.put("name", "42");
    assertThat(environment.getEnv("name", 0L), is(equalTo(42L)));
  }

  @Test
  public void testGetEnvInt() {
    env.put("name", "42");
    assertThat(environment.getEnv("name", 0), is(equalTo(42)));
  }

  @Test
  public void testGetEnvDouble() {
    env.put("name", "42.0");
    assertThat(environment.getEnv("name", 0.0), is(equalTo(42.0)));
  }

  @Test
  public void testGetEnvDefaultString() {
    assertThat(environment.getEnv("name", "defaultValue"),
        is(equalTo("defaultValue")));
  }

  @Test
  public void testGetEnvDefaultLong() {
    assertThat(environment.getEnv("name", 42L),
        is(equalTo(42L)));
  }

  @Test
  public void testGetEnvDefaultInt() {
    assertThat(environment.getEnv("name", 42),
        is(equalTo(42)));
  }

  @Test
  public void testGetEnvDefaultDouble() {
    assertThat(environment.getEnv("name", 42.0),
        is(equalTo(42.0)));
  }

  @Test
  public void testGetInstanceDefaultProvider() throws Exception {
    assertThat(Environment.getInstance().getEnv("USER", "defaultUser"),
        is(not(nullValue())));
  }

}
