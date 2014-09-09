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
package org.soulwing.cas.server.facelets;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 * An {@link Errors} object that delegates to the Faces Context.
 *
 * @author Carl Harris
 */
@RequestScoped
public class FacesErrors implements Errors {

  @Inject
  protected FacesContext facesContext;

  private ResourceBundle bundle;
  
  @PostConstruct
  public void init() {
    bundle = ResourceBundle.getBundle(
        "org.soulwing.cas.server.resources.messages", 
        facesContext.getViewRoot().getLocale());
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void addError(String message, Object... args) {
    String text = format(message, args);
    facesContext.addMessage(null, 
        new FacesMessage(FacesMessage.SEVERITY_ERROR, text, text));
  }

  private String format(String message, Object... args) {
    return MessageFormat.format(getMessage(message), args);
  }
  
  private String getMessage(String message) {
    try {
      return bundle.getString(message);
    }
    catch (MissingResourceException ex) {
      return message;
    }
  }
  
}
