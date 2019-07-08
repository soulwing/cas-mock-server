/*
 * File created on Jul 2, 2019
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.soulwing.cas.server.AttributeValue;
import org.soulwing.cas.server.service.spi.AttributesServiceProvider;

/**
 * An {@link AttributesServiceProvider} that loads attributes from an SQL
 * database using JDBC.
 *
 * @author Carl Harris
 */
public class JdbcAttributesServiceProvider
    implements AttributesServiceProvider {

  private static final Logger logger = Logger.getLogger(
      JdbcAttributesServiceProvider.class.getName());

  public static final String ATTRIBUTES_JDBC_URL = "ATTRIBUTES_JDBC_URL";
  public static final String ATTRIBUTES_JDBC_USERNAME = "ATTRIBUTES_JDBC_USERNAME";
  public static final String ATTRIBUTES_JDBC_PASSWORD = "ATTRIBUTES_JDBC_PASSWORD";
  public static final String ATTRIBUTES_JDBC_USER_QUERY = "ATTRIBUTES_JDBC_USER_QUERY";
  public static final String ATTRIBUTES_JDBC_USER_COLUMNS = "ATTRIBUTES_JDBC_USER_COLUMNS";
  public static final String ATTRIBUTES_JDBC_GROUP_QUERY = "ATTRIBUTES_JDBC_GROUP_QUERY";
  public static final String ATTRIBUTES_JDBC_GROUP_COLUMNS = "ATTRIBUTES_JDBC_GROUP_COLUMNS";

  private final Environment environment;

  public JdbcAttributesServiceProvider() {
    this(Environment.getInstance());
  }

  JdbcAttributesServiceProvider(Environment environment) {
    this.environment = environment;
  }

  @Override
  public String getName() {
    return "JDBC";
  }

  @Override
  public AttributesService getInstance() {
    final String url = environment.getEnv(ATTRIBUTES_JDBC_URL);
    final String username = environment.getEnv(ATTRIBUTES_JDBC_USERNAME);
    final String password = environment.getEnv(ATTRIBUTES_JDBC_PASSWORD);
    final String userQuery = environment.getEnv(ATTRIBUTES_JDBC_USER_QUERY);
    final String userColumns = environment.getEnv(ATTRIBUTES_JDBC_USER_COLUMNS);
    final String groupQuery = environment.getEnv(ATTRIBUTES_JDBC_GROUP_QUERY);
    final String groupColumns = environment.getEnv(ATTRIBUTES_JDBC_GROUP_COLUMNS);

    if (url == null || userQuery == null || userColumns == null) {
      return null;
    }

    try {
      final Connection connection =
          DriverManager.getConnection(url, username, password);
      connection.close();
    }
    catch (SQLException ex) {
      logger.warning("cannot create connection using JDBC url " + url
          + " and provided username and password");
      return null;
    }

    return new JdbcAttributesProvider(url, username, password, userQuery,
        userColumns, groupQuery, groupColumns);
  }

  static class JdbcAttributesProvider implements AttributesService {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;
    private final String userQuery;
    private final String[] userColumns;
    private final String groupQuery;
    private final String[] groupColumns;

    JdbcAttributesProvider(String dbUrl, String dbUser, String dbPassword,
        String userQuery, String userColumns,
        String groupQuery, String groupColumns) {
      this.dbUrl = dbUrl;
      this.dbUser = dbUser;
      this.dbPassword = dbPassword;
      this.userQuery = userQuery;
      this.userColumns = userColumns.split("\\s*,\\s*");
      this.groupQuery = groupQuery;
      this.groupColumns = groupColumns != null ?
          groupColumns.split("\\s*,\\s*") : null;
    }

    @Override
    public List<AttributeValue> getAttributes(String username) {
      try {
        return queryAttributes(username);
      }
      catch (SQLException ex) {
        logger.warning("SQL error: " + ex.getMessage());
        return Collections.emptyList();
      }
    }

    private List<AttributeValue> queryAttributes(String username)
        throws SQLException {
      final List<AttributeValue> attributes = new ArrayList<>();

      try (final Connection connection =
          DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
        attributes.addAll(queryUserAttributes(connection, username));
        if (groupQuery != null && groupColumns != null) {
          attributes.addAll(queryGroupMemberships(connection, username));
        }
        return attributes;
      }
    }

    private List<AttributeValue> queryUserAttributes(Connection connection,
        String username) throws SQLException {
      final List<AttributeValue> attributes = new ArrayList<>();
      try (final PreparedStatement statement =
          connection.prepareStatement(userQuery)) {
        statement.setString(1, username);
        try (final ResultSet rs = statement.executeQuery()) {
          int columnCount = rs.getMetaData().getColumnCount();
          if (rs.next()) {
            for (int i = 0; i < columnCount; i++) {
              attributes.add(AttributeValue.of(userColumns[i],
                  rs.getObject(i + 1)));
            }
          }
        }
      }
      return attributes;
    }

    private List<AttributeValue> queryGroupMemberships(Connection connection,
        String username) throws SQLException {
      final List<AttributeValue> attributes = new ArrayList<>();
      try (final PreparedStatement statement =
          connection.prepareStatement(groupQuery)) {
        statement.setString(1, username);
        try (final ResultSet rs = statement.executeQuery()) {
          while (rs.next()) {
            attributes.add(AttributeValue.of(groupColumns[0], rs.getString(1)));
          }
        }
      }
      return attributes;
    }

  }


}
