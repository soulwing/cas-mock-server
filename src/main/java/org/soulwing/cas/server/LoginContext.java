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
 * A login context.
 *
 * @author Carl Harris
 */
public interface LoginContext {

  /**
   * Tests whether this login context represents an authentic user.
   * @return {@code true} if the user associated with this context is 
   *    authentic
   */
  boolean isAuthentic();
  
  /**
   * Gets the user name associated with this login context.
   * @return user name or {@code null} if there is no authentic user 
   *    associated with this context
   */
  String getUsername();
  
}
