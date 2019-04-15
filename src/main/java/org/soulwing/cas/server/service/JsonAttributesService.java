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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;

import org.soulwing.cas.server.AttributeValue;

/**
 * A {@link AttributesService} implemented using a JSON document.
 *
 * @author Carl Harris
 */
@Dependent
class JsonAttributesService implements AttributesService {

  private static final Logger logger = Logger.getLogger(
      JsonAttributesService.class.getName());

  private static final String ATTRIBUTES_JSON_URL = "ATTRIBUTES_JSON_URL";
  private static final String DEFAULT_URI = "classpath:attributes.json";

  private final URI uri;

  private JsonObject attributes;

  public JsonAttributesService() {
    this(resourceUri());
  }

  JsonAttributesService(URI uri) {
    this.uri = uri;
  }

  private static URI resourceUri() {
    String uri = System.getenv(ATTRIBUTES_JSON_URL);
    if (uri == null) uri = DEFAULT_URI;
    return URI.create(uri);
  }

  @PostConstruct
  public void init() {
    try {
      try (final InputStream inputStream =
          ResourceAccessor.getResourceAsStream(uri)) {
        attributes = Json.createReader(inputStream).readObject();
        logger.info("loaded attributes from " + uri);
        if (logger.isLoggable(Level.FINE)) {
          logger.fine(attributes.toString());
        }
      }
    }
    catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public List<AttributeValue> getAttributes(String username) {
    final List<AttributeValue> list = new ArrayList<>();
    final JsonObject user = attributes.getJsonObject(username);
    if (user == null) return Collections.emptyList();
    final String inheritFrom = user.getString("inherit", null);
    if (inheritFrom != null) {
      if (inheritFrom.equals(username)) {
        throw new IllegalArgumentException("don't be an idiot");
      }
      list.addAll(getAttributes(inheritFrom));
    }
    list.addAll(getValues(user.getJsonObject("attributes")));
    return list;
  }

  private List<AttributeValue> getValues(JsonObject attrs) {
    final List<AttributeValue> list = new ArrayList<>();
    for (final String name : attrs.keySet()) {
      final JsonValue value = attrs.get(name);
      switch (value.getValueType()) {
        case NULL:
          break;
        case FALSE:
          list.add(AttributeValue.of(name, false));
          break;
        case TRUE:
          list.add(AttributeValue.of(name, true));
          break;
        case STRING:
          list.add(getValue(name, (JsonString) value));
          break;
        case NUMBER:
          list.add(getValue(name, (JsonNumber) value));
          break;
        case ARRAY:
          list.addAll(getValues(name, (JsonArray) value));
          break;
        case OBJECT:
          break;
        default:
          throw new AssertionError("unrecognized value type");
      }
    }
    return list;
  }

  private List<AttributeValue> getValues(String name, JsonArray array) {
    final List<AttributeValue> list = new ArrayList<>();
    for (int i = 0, max = array.size(); i < max; i++) {
      final JsonValue value = array.get(i);
      switch (value.getValueType()) {
        case NULL:
          break;
        case FALSE:
          list.add(AttributeValue.of(name, false));
          break;
        case TRUE:
          list.add(AttributeValue.of(name, true));
          break;
        case STRING:
          list.add(getValue(name, (JsonString) value));
          break;
        case NUMBER:
          list.add(getValue(name, (JsonNumber) value));
          break;
        case ARRAY:
          break;
        case OBJECT:
          break;
        default:
          throw new AssertionError("unrecognized value type");
      }
    }
    return list;
  }

  private AttributeValue getValue(String name, JsonString string) {
    return AttributeValue.of(name, string.getString());
  }

  private AttributeValue getValue(String name, JsonNumber number) {
    if (number.isIntegral()) {
      return AttributeValue.of(name, number.longValue());
    }
    else {
      return AttributeValue.of(name, number.doubleValue());
    }
  }

}
