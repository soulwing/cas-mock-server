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

import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

import org.soulwing.cas.server.AttributeValue;
import org.soulwing.cas.server.service.spi.AttributesServiceProvider;

/**
 * An {@link AttributesService} that at startup finds a
 * {@link org.soulwing.cas.server.service.spi.AttributesServiceProvider} using
 * the {@link java.util.ServiceLoader} and delegates to it for subsequent
 * attribute requests.
 *
 * @author Carl Harris
 */
@ApplicationScoped
class AttributesServiceBean implements AttributesService {

  private static final Logger logger =
      Logger.getLogger(AttributesServiceBean.class.getName());

  private AttributesServiceProvider provider;
  private AttributesService delegate;

  @PostConstruct
  public void init() {
    logger.info("initializing attributes service");
    for (final AttributesServiceProvider provider :
      ServiceLoader.load(AttributesServiceProvider.class)) {
      this.delegate = provider.getInstance();
      if (delegate != null) {
        this.provider = provider;
        logger.info("attributes will be obtained from the `"
            + provider.getName() + "` provider");
        break;
      }
    }
    if (delegate == null) {
      delegate = new NoOpAttributesService();
      logger.info("cannot find an attributes provider; "
          + "no attributes will be included in responses");
    }
  }

  @PreDestroy
  public void destroy() {
    if (provider != null) {
      provider.destroy();
    }
  }

  @Override
  public List<AttributeValue> getAttributes(String username) {
    final List<AttributeValue> attributes = delegate.getAttributes(username);
    logger.info("attributes released for " + username + ": " + attributes);
    return attributes;
  }

  @Alternative
  private static class NoOpAttributesService implements AttributesService {

    @Override
    public List<AttributeValue> getAttributes(String username) {
      return Collections.emptyList();
    }

  }

}
