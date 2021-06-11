/*
 * File created on Jun 11, 2021
 *
 * Copyright (c) 2021 Carl Harris, Jr
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

import java.io.IOException;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.StartTlsRequest;
import javax.naming.ldap.StartTlsResponse;

import org.soulwing.cas.server.AttributeValue;
import org.soulwing.cas.server.service.spi.AttributesServiceProvider;

/**
 * An {@link AttributesServiceProvider} that loads attributes from an LDAP
 * directory.
 *
 * @author Carl Harris
 */
public class LdapAttributesServiceProvider
    implements AttributesServiceProvider {

  private static final Logger logger = Logger.getLogger(
      LdapAttributesServiceProvider.class.getName());

  private static final String ATTRIBUTES_LDAP_URL = "ATTRIBUTES_LDAP_URL";
  private static final String ATTRIBUTES_LDAP_TLS = "ATTRIBUTES_LDAP_TLS";
  private static final String ATTRIBUTES_BIND_DN = "ATTRIBUTES_BIND_DN";
  private static final String ATTRIBUTES_BIND_PASSWORD = "ATTRIBUTES_BIND_PASSWORD";
  private static final String ATTRIBUTES_LDAP_BASE = "ATTRIBUTES_LDAP_BASE";
  private static final String ATTRIBUTES_LDAP_SCOPE = "ATTRIBUTES_LDAP_SCOPE";
  private static final String ATTRIBUTES_LDAP_FILTER = "ATTRIBUTES_LDAP_FILTER";
  private static final String ATTRIBUTES_LDAP_INCLUDE = "ATTRIBUTES_LDAP_INCLUDE";

  private final Environment environment;

  @SuppressWarnings("unused")
  public LdapAttributesServiceProvider() {
    this(Environment.getInstance());
  }

  private LdapAttributesServiceProvider(Environment environment) {
    this.environment = environment;
  }

  private Driver driver;

  @Override
  public String getName() {
    return "LDAP";
  }

  @Override
  public AttributesService getInstance() {
    final String url = environment.getEnv(ATTRIBUTES_LDAP_URL);
    final String startTLS = environment.getEnv(ATTRIBUTES_LDAP_TLS);
    final String bindDn = environment.getEnv(ATTRIBUTES_BIND_DN);
    final String password = environment.getEnv(ATTRIBUTES_BIND_PASSWORD);
    final String base = environment.getEnv(ATTRIBUTES_LDAP_BASE);
    final String scope = environment.getEnv(ATTRIBUTES_LDAP_SCOPE);
    final String filter = environment.getEnv(ATTRIBUTES_LDAP_FILTER);
    final String include = environment.getEnv(ATTRIBUTES_LDAP_INCLUDE);

    if (url == null || base == null || filter == null) {
      return null;
    }

    return new LdapAttributesProvider(url, startTLS, bindDn, password, base,
        scope, filter, include);
  }

  @Override
  public void destroy() {
  }

  static class LdapAttributesProvider implements AttributesService {
    private final String url;
    private final boolean startTLS;
    private final String bindDn;
    private final String password;
    private final String base;
    private final int scope;
    private final String filter;
    private final String[] include;

    private static int stringToScope(String scope) {
      if (scope == null) return SearchControls.SUBTREE_SCOPE;
      switch (scope.toUpperCase()) {
        case "SUBTREE":
          return SearchControls.SUBTREE_SCOPE;
        case "BASE":
          return SearchControls.OBJECT_SCOPE;
        case "ONELEVEL":
          return SearchControls.ONELEVEL_SCOPE;
        default:
          throw new IllegalArgumentException(
              "unrecognized scope; must be one of BASE, SUBTREE, ONELEVEL");
      }
    }

    LdapAttributesProvider(String url, String startTLS, String bindDn,
        String password, String base, String scope, String filter,
        String include) {
      this.url = url;
      this.startTLS = Boolean.parseBoolean(
          (startTLS != null ? startTLS : "false").toLowerCase());
      this.bindDn = bindDn;
      this.password = bindDn != null ? password : null;
      this.base = base;
      this.scope = stringToScope(scope);
      this.filter = filter;
      this.include = include != null ? include.split("\\s*,\\s*") : null;
    }

    @Override
    public List<AttributeValue> getAttributes(String username) {
      final Hashtable<String, Object> env = new Hashtable<>();
      env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
      env.put(Context.PROVIDER_URL, url);
      env.put("java.naming.ldap.version", "3");

      LdapContext ctx = null;
      StartTlsResponse tls = null;
      try {
        ctx = new InitialLdapContext(env, null);
        if (startTLS) {
          tls = (StartTlsResponse) ctx.extendedOperation(new StartTlsRequest());
          tls.negotiate();
        }

        if (!bind(ctx, username)) {
          return Collections.emptyList();
        }

        final SearchResult result =
            searchForUser(username, include, ctx);

        if (result == null) {
          return Collections.emptyList();
        }

        final NamingEnumeration<? extends Attribute> attributes =
            result.getAttributes().getAll();

        final List<AttributeValue> attributeValues = new ArrayList<>();
        while (attributes.hasMoreElements()) {
          final Attribute attribute = attributes.nextElement();
          final String name = attribute.getID();
          final NamingEnumeration<?> values = attribute.getAll();
          while (values.hasMoreElements()) {
            attributeValues.add(AttributeValue.of(name, values.next()));
          }
        }

        return attributeValues;

      }
      catch (IOException | NamingException ex) {
        throw new RuntimeException(ex);
      }
      finally {
        if (tls != null) {
          try {
            tls.close();
          }
          catch (IOException ex) {
            assert true;  // ignore it
          }
        }
        if (ctx != null) {
          try {
            ctx.close();
          }
          catch (NamingException ex) {
            assert true;  // ignore it
          }
        }
      }
    }

    private boolean bind(LdapContext ctx, String username)
      throws NamingException {
      if (this.bindDn != null) {
        ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, this.bindDn);
        ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, this.password);
      }
      else {
        final SearchResult anonymousResult =
            searchForUser(username, null, ctx);
        if (anonymousResult == null) {
          return false;
        }
        ctx.addToEnvironment(Context.SECURITY_PRINCIPAL,
            anonymousResult.getNameInNamespace());

        ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, username);
      }

      ctx.addToEnvironment(Context.SECURITY_AUTHENTICATION, "simple");
      ctx.reconnect(null);

      return true;
    }

    private SearchResult searchForUser(String username, String[] attrs,
        LdapContext ctx) throws NamingException {
      final SearchControls searchControls = new SearchControls();
      searchControls.setSearchScope(scope);
      searchControls.setReturningAttributes(attrs);
      searchControls.setCountLimit(1);

      final NamingEnumeration<SearchResult> results =
          ctx.search(base, filter, new String[]{ username }, searchControls);

      final SearchResult result = results.nextElement();
      if (result == null) {
        logger.warning("user '" + username + "' not found");
      }
      return result;
    }

  }

}
