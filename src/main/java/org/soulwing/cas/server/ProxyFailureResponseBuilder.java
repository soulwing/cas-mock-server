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
package org.soulwing.cas.server;

/**
 * A builder for an unsuccessful proxy response.
 *
 * @author Carl Harris
 */
public interface ProxyFailureResponseBuilder extends ServiceResponseBuilder {

  /**
   * Sets the error code for the response.
   * @param code the error code to set
   * @return this builder
   */
  ProxyFailureResponseBuilder code(int code);
  
  /**
   * Sets the message for the response.
   * @param message the message to set
   * @return this builder
   */
  ProxyFailureResponseBuilder message(String message);

}
