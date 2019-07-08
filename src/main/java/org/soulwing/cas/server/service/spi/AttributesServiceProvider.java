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
package org.soulwing.cas.server.service.spi;

import org.soulwing.cas.server.service.AttributesService;

/**
 * A provider for an {@link AttributesService}.
 *
 * @author Carl Harris
 */
public interface AttributesServiceProvider {

  /**
   * Gets the provider name.
   * @return provider name
   */
  String getName();

  /**
   * Gets an instance of {@link AttributesService} from this provider.
   * @return service instance or {@code null} of this provider is not
   *    configured
   */
  AttributesService getInstance();

}
