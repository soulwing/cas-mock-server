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
 * An enumeration of error codes specified for the CAS protocol.
 *
 * @author Carl Harris
 */
public enum ProtocolError {

  /** not all of the required request parameters were present */
  INVALID_REQUEST,

  /** failure to meet the requirements of validation specification */
  INVALID_TICKET_SPEC,

  /** the service is not authorized to perform proxy authentication */
  UNAUTHORIZED_SERVICE_PROXY,

  /** The proxy callback specified is invalid. The credentials specified
   *  for proxy authentication do not meet the security requirements */
  INVALID_PROXY_CALLBACK,

  /**
   * the ticket provided was not valid, or the ticket did not come from an
   * initial login and renew was set on validation. The body of the
   * {@code <cas:authenticationFailure>} block of the XML response SHOULD
   * describe the exact details.
   */
  INVALID_TICKET,

  /**
   * the ticket provided was valid, but the service specified did not match
   * the service associated with the ticket. CAS MUST invalidate the ticket
   * and disallow future validation of that same ticket.
   */
  INVALID_SERVICE,

  /**
   * An internal error occurred during ticket validation.
   */
  INTERNAL_ERROR

}
