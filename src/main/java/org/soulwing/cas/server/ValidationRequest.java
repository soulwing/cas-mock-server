/*
 * File created on Mar 9, 2018
 *
 * Copyright (c) 2018 Carl Harris, Jr
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

/**
 * A request for CAS ticket validation.
 *
 * @author Carl Harris
 */
public class ValidationRequest {

  private String ticket;
  private String service;
  private String proxyCallbackUrl;
  private boolean renew;
  private String format;

  /**
   * Gets the authentication ticket.
   * @return ticket
   */
  public String getTicket() {
    return ticket;
  }

  /**
   * Sets the authentication ticket.
   * @param ticket the ticket to set
   */
  public void setTicket(String ticket) {
    this.ticket = ticket;
  }

  /**
   * Gets the authenticating service.
   * @return service
   */
  public String getService() {
    return service;
  }

  /**
   * Sets the authenticating service.
   * @param service the service to set
   */
  public void setService(String service) {
    this.service = service;
  }

  /**
   * Gets the proxy callback URL.
   * @return proxy callback URL
   */
  public String getProxyCallbackUrl() {
    return proxyCallbackUrl;
  }

  /**
   * Sets the proxy callback URL.
   * @param proxyCallbackUrl proxy callback URL
   */
  public void setProxyCallbackUrl(String proxyCallbackUrl) {
    this.proxyCallbackUrl = proxyCallbackUrl;
  }

  /**
   * Gets the renew flag.
   * @return renew flag
   */
  public boolean isRenew() {
    return renew;
  }

  /**
   * Sets the renew flag.
   * @param renew the renew flag state to set
   */
  public void setRenew(boolean renew) {
    this.renew = renew;
  }

  /**
   * Gets the format.
   * @return format.
   */
  public String getFormat() {
    return format;
  }

  /**
   * Sets the format.
   * @param format the format to set
   */
  public void setFormat(String format) {
    this.format = format;
  }

}
