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
package org.soulwing.cas.server.service;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.soulwing.cas.server.AttributeValue;
import org.soulwing.cas.server.ProtocolError;
import org.soulwing.cas.server.ServiceResponse;
import org.soulwing.cas.server.ServiceResponseBuilderFactory;
import org.soulwing.cas.server.TicketState;
import org.soulwing.cas.server.ValidationRequest;

/**
 * A {@link ValidationService} implemented as an injectable bean.
 *
 * @author Carl Harris
 */
@ApplicationScoped
class ValidationServiceBean implements ValidationService {

  @Inject
  TicketService ticketService;

  @Inject
  ServiceResponseBuilderFactory builderFactory;

  @Inject
  AttributesService attributesService;

  @Override
  public ServiceResponse validate(ValidationRequest request) {
    final TicketState state = ticketService.validate(request.getTicket());
    if (state == null) {
      return builderFactory.createAuthenticationFailureBuilder()
          .code(ProtocolError.INVALID_TICKET)
          .message("invalid ticket")
          .build();
    }

    final String username = state.getUsername();
    final List<AttributeValue> attributes =
        attributesService.getAttributes(username);

    return builderFactory.createAuthenticationSuccessBuilder()
        .user(username)
        .attributes(attributes)
        .build();
  }

}
