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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * An {@link ServiceResult} that represents a successful outcome.
 *
 * @author Carl Harris
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AuthenticationSuccess implements ServiceResult {

  @XmlElement
  public String user;
  
  @XmlElement
  public String proxyGrantingTicket;
  
  @XmlElementWrapper(name = "proxies")
  @XmlElement(name = "proxy")
  public List<String> proxies = new ArrayList<>();

  @XmlElementWrapper(name = "attributes")
  @XmlAnyElement(lax = true)
  public List<Object> attributes = new ArrayList<>();

}
