/*
 * File created on Sep 9, 2014 
 *
 * Copyright (c) Carl Harris, Jr.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.soulwing.cas.server.protocol;

import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.soulwing.cas.server.AttributeValue;
import org.soulwing.cas.server.AuthenticationSuccessResponseBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A simple {@link AuthenticationSuccessResponseBuilder}.
 *
 * @author Carl Harris
 */
class SimpleAuthenticationSuccessResponseBuilder
    extends AbstractServiceResponseBuilder
    implements AuthenticationSuccessResponseBuilder {

  private final AuthenticationSuccess result = new AuthenticationSuccess();

  @Override
  public AuthenticationSuccessResponseBuilder user(String user) {
    result.user = user;
    return this;
  }

  @Override
  public AuthenticationSuccessResponseBuilder proxyGrantingTicket(
      String pgtiou) {
    result.proxyGrantingTicket = pgtiou;
    return this;
  }

  @Override
  public AuthenticationSuccessResponseBuilder proxy(String proxy) {
    result.proxies.add(proxy);
    return this;
  }

  @Override
  public AuthenticationSuccessResponseBuilder attributes(
      List<AttributeValue> attributes) {

    try {
      final Document document = DocumentBuilderFactory.newInstance()
          .newDocumentBuilder().newDocument();
      final List<Element> elements = new ArrayList<>();
      for (final AttributeValue attribute : attributes) {
        final Element element = document.createElementNS(Namespace.CAS,
            attribute.getName());
        element.setPrefix(Namespace.PREFIX);
        element.setTextContent(attribute.getValue().toString());
        elements.add(element);
      }
      result.attributes.addAll(elements);
      return this;
    }
    catch (ParserConfigurationException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  protected ServiceResult getResult() {
    return result;
  }

}
