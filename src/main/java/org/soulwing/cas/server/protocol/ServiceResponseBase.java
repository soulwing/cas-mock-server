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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;

import org.soulwing.cas.server.ServiceResponse;

/**
 * An abstract base for validation response models.
 *
 * @author Carl Harris
 */
@XmlRootElement(name = "serviceResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceResponseBase implements ServiceResponse {

  @XmlElementRefs({
      @XmlElementRef(type = AuthenticationSuccess.class),
      @XmlElementRef(type = AuthenticationFailure.class),
      @XmlElementRef(type = ProxySuccess.class),
      @XmlElementRef(type = ProxyFailure.class)
  })
  public ServiceResult result;
  
}
