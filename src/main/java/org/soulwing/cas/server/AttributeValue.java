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
package org.soulwing.cas.server;

import java.util.Objects;

import org.w3c.dom.Attr;

/**
 * A simple attribute value.
 *
 * @author Carl Harris
 */
public class AttributeValue {

  private final String name;
  private final Object value;

  public static AttributeValue of(String name, Object value) {
    return new AttributeValue(name, value);
  }

  private AttributeValue(String name, Object value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public Object getValue() {
    return value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, value);
  }

  @Override
  public boolean equals(Object obj) {
    return obj == this
        || (obj instanceof AttributeValue
            && Objects.equals(((AttributeValue) obj).name, this.name)
            && Objects.equals(((AttributeValue) obj).value, this.value));
  }

  @Override
  public String toString() {
    return name + "=" + value;
  }
}
