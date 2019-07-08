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

/**
 * Abstraction of the runtime configuration environment.
 *
 * @author Carl Harris
 */
@SuppressWarnings({ "WeakerAccess", "unused" })
public class Environment {

  /**
   * Environment provider function.
   */
  public interface Provider {
    String getEnv(String name);
  }

  /**
   * Default singleton instance using {@link System#getenv(String)} as
   * the provider function.
   */
  private static final Environment INSTANCE = new Environment(new Provider() {
    @Override
    public String getEnv(String name) {
      return System.getenv(name);
    }
  });

  /**
   * Provider function
   */
  private final Provider provider;

  /**
   * Constructs a new instance with the specified provider.
   * @param provider the subject provider
   */
  private Environment(Provider provider) {
    this.provider = provider;
  }

  /**
   * Gets the value of an environment variable.
   * @param name name of the variable
   * @return value for {@code name} from the environment or {@code null}
   *    if {@code name} has no value
   */
  public String getEnv(String name) {
    return getEnv(name, null);
  }

  /**
   * Gets the value of an environment variable.
   * @param name name of the variable
   * @param defaultValue default value
   * @return value for {@code name} from the environment or
   *    {@code defaultValue} if {@code name} has no value in the environment
   */
  public String getEnv(String name, String defaultValue) {
    final String value = provider.getEnv(name);
    if (value == null) return defaultValue;
    return value;
  }

  /**
   * Gets the value of an environment variable.
   * @param name name of the variable
   * @param defaultValue default value
   * @return value for {@code name} from the environment or
   *    {@code defaultValue} if {@code name} has no value in the environment
   */
  public int getEnv(String name, int defaultValue) {
    final String value = provider.getEnv(name);
    if (value == null) return defaultValue;
    return Integer.parseInt(value);
  }

  /**
   * Gets the value of an environment variable.
   * @param name name of the variable
   * @param defaultValue default value
   * @return value for {@code name} from the environment or
   *    {@code defaultValue} if {@code name} has no value in the environment
   */
  public long getEnv(String name, long defaultValue) {
    final String value = provider.getEnv(name);
    if (value == null) return defaultValue;
    return Long.parseLong(value);
  }

  /**
   * Gets the value of an environment variable.
   * @param name name of the variable
   * @param defaultValue default value
   * @return value for {@code name} from the environment or
   *    {@code defaultValue} if {@code name} has no value in the environment
   */
  public double getEnv(String name, double defaultValue) {
    final String value = provider.getEnv(name);
    if (value == null) return defaultValue;
    return Double.parseDouble(value);
  }

  /**
   * Gets the default instance that uses {@link System#getenv(String)} as
   * the provider.
   * @return environment instance
   */
  public static Environment getInstance() {
    return INSTANCE;
  }

  /**
   * Gets an instance that uses the specified provider.
   * @param provider environment provider
   * @return environment instance
   */
  public static Environment getInstance(Provider provider) {
    return new Environment(provider);
  }

}
