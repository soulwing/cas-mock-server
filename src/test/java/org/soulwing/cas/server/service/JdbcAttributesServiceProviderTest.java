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
import static org.hamcrest.Matchers.nullValue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.soulwing.cas.server.AttributeValue;

/**
 * Unit tests for {@link JdbcAttributesServiceProvider}.
 *
 * @author Carl Harris
 */
public class JdbcAttributesServiceProviderTest {

  private static final String DB_URL = "jdbc:derby:memory:test";

  private static final Long USER_ID = 1L;
  private static final String USER_NAME = "fletcher";
  private static final String USER_DISPLAY_NAME = "Jordan Fletcher";
  private static final String USER_AFFILIATION = "EMPLOYEE";

  private static final Long GROUP_ID_ADMIN = 1L;
  private static final String GROUP_NAME_ADMIN = "admin";
  private static final Long GROUP_ID_POWER_USER = 2L;
  private static final String GROUP_NAME_POWER_USER = "power-user";

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    try (final Connection connection = DriverManager.getConnection(DB_URL + ";create=true")) {
      createTables(connection);
      createUsers(connection);
      createGroups(connection);
      createGroupMembers(connection);
    }
  }

  private static void createTables(Connection connection) throws SQLException {
    try (final Statement statement = connection.createStatement()) {
      statement.execute("CREATE TABLE app_user (id BIGINT PRIMARY KEY, name VARCHAR(255) UNIQUE NOT NULL, display_name VARCHAR(255), affiliation VARCHAR(255))");
      statement.execute("CREATE TABLE app_group (id BIGINT PRIMARY KEY, name VARCHAR(255) UNIQUE NOT NULL)");
      statement.execute("CREATE TABLE app_group_member (group_id BIGINT NOT NULL REFERENCES app_group (id), user_id BIGINT NOT NULL REFERENCES app_user (id))");
    }
  }

  private static void createUsers(Connection connection) throws SQLException {
    try (final PreparedStatement statement =
        connection.prepareStatement("INSERT INTO app_user(id, name, display_name, affiliation) VALUES(?, ?, ?, ?)")) {

      statement.setLong(1, USER_ID);
      statement.setString(2, USER_NAME);
      statement.setString(3, USER_DISPLAY_NAME);
      statement.setString(4, USER_AFFILIATION);
      assertThat(statement.executeUpdate(), is(equalTo(1)));
    }
  }

  private static void createGroups(Connection connection) throws SQLException {
    try (final PreparedStatement statement =
             connection.prepareStatement("INSERT INTO app_group(id, name) VALUES(?, ?)")) {

      statement.setLong(1, GROUP_ID_ADMIN);
      statement.setString(2, GROUP_NAME_ADMIN);
      assertThat(statement.executeUpdate(), is(equalTo(1)));

      statement.setLong(1, GROUP_ID_POWER_USER);
      statement.setString(2, GROUP_NAME_POWER_USER);
      assertThat(statement.executeUpdate(), is(equalTo(1)));

    }
  }

  private static void createGroupMembers(Connection connection) throws SQLException {
    try (final PreparedStatement statement =
             connection.prepareStatement("INSERT INTO app_group_member(group_id, user_id) VALUES(?, ?)")) {

      statement.setLong(1, GROUP_ID_ADMIN);
      statement.setLong(2, USER_ID);
      assertThat(statement.executeUpdate(), is(equalTo(1)));

      statement.setLong(1, GROUP_ID_POWER_USER);
      statement.setLong(2, USER_ID);
      assertThat(statement.executeUpdate(), is(equalTo(1)));

    }
  }

  private final Map<String, String> env = new HashMap<>();

  private Environment environment = Environment.getInstance(
      new Environment.Provider() {
        @Override
        public String getEnv(String name) {
          return env.get(name);
        }
      });

  private JdbcAttributesServiceProvider provider =
      new JdbcAttributesServiceProvider(environment);

  @Test
  public void testGetInstanceWhenNoUrl() throws Exception {
    assertThat(provider.getInstance(), is(nullValue()));
  }

  @Test
  public void testGetInstanceWhenNoUserQuery() throws Exception {
    env.put(JdbcAttributesServiceProvider.ATTRIBUTES_JDBC_URL, DB_URL);
    assertThat(provider.getInstance(), is(nullValue()));
  }

  @Test
  public void testGetInstanceWhenNoUserColumns() throws Exception {
    env.put(JdbcAttributesServiceProvider.ATTRIBUTES_JDBC_URL, DB_URL);
    env.put(JdbcAttributesServiceProvider.ATTRIBUTES_JDBC_USER_QUERY,
        "SELECT id, name, display_name, affiliation FROM app_users");
    assertThat(provider.getInstance(), is(nullValue()));
  }

  @Test
  public void testGetInstanceWithMinimalConfiguration() throws Exception {
    env.put(JdbcAttributesServiceProvider.ATTRIBUTES_JDBC_URL, DB_URL);
    env.put(JdbcAttributesServiceProvider.ATTRIBUTES_JDBC_USER_QUERY,
        "SELECT id, name, display_name, affiliation FROM app_user WHERE name = ?");
    env.put(JdbcAttributesServiceProvider.ATTRIBUTES_JDBC_USER_COLUMNS,
        "uid, username, displayName, affiliation");

    final AttributesService provider = this.provider.getInstance();
    final List<AttributeValue> attributes = provider.getAttributes("fletcher");

    validateUserAttributes(attributes);

  }

  @Test
  @SuppressWarnings("unchecked")
  public void testGetInstanceWithFullConfiguration() throws Exception {
    env.put(JdbcAttributesServiceProvider.ATTRIBUTES_JDBC_URL, DB_URL);
    env.put(JdbcAttributesServiceProvider.ATTRIBUTES_JDBC_USER_QUERY,
        "SELECT id, name, display_name, affiliation FROM app_user WHERE name = ?");
    env.put(JdbcAttributesServiceProvider.ATTRIBUTES_JDBC_USER_COLUMNS,
        "uid, username, displayName, affiliation");
    env.put(JdbcAttributesServiceProvider.ATTRIBUTES_JDBC_GROUP_QUERY,
        "SELECT g.name FROM app_group g " +
            "INNER JOIN app_group_member gm ON gm.group_id = g.id " +
            "INNER JOIN app_user u ON gm.user_id = u.id " +
            "WHERE u.name = ?");
    env.put(JdbcAttributesServiceProvider.ATTRIBUTES_JDBC_GROUP_COLUMNS,
        "groupMembership");

    final AttributesService provider = this.provider.getInstance();
    final List<AttributeValue> attributes = provider.getAttributes("fletcher");

    validateUserAttributes(attributes);
    assertThat(attributes.contains(AttributeValue.of("groupMembership", GROUP_NAME_ADMIN)), is(true));
    assertThat(attributes.contains(AttributeValue.of("groupMembership", GROUP_NAME_POWER_USER)), is(true));
  }

  private void validateUserAttributes(List<AttributeValue> attributes) {
    assertThat(attributes.contains(AttributeValue.of("uid", USER_ID)), is(true));
    assertThat(attributes.contains(AttributeValue.of("username", USER_NAME)), is(true));
    assertThat(attributes.contains(AttributeValue.of("displayName", USER_DISPLAY_NAME)), is(true));
    assertThat(attributes.contains(AttributeValue.of("affiliation", USER_AFFILIATION)), is(true));
  }


}