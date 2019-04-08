/*
 * File created on Apr 8, 2019
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Static utility methods for accessing file-like resources.
 *
 * @author Carl Harris
 */
public class ResourceAccessor {

  /**
   * Gets a URL for a resource.
   * <p>
   * Classpath resources accessed by this method will use the thread
   * context classloader.
   * 
   * @param uri string representation of a URI which can be a just a path 
   *    (to a classpath resource), a {@code classpath:} URI, or any URL
   *    supported by the JRE's URL loading mechanism
   * @return URL for the subject resource
   * @throws IOException if the resource cannot be located
   */
  public static URL getResource(String uri) throws IOException {
    return getResource(URI.create(uri));
  }

  /**
   * Gets a URL for a resource.
   * <p>
   * Classpath resources accessed by this method will use the thread
   * context classloader.
   * 
   * @param uri string representation of a URI which can be a just a path 
   *    (to a filesystem resource), a {@code classpath:} URI, or any URL
   *    supported by the JRE's URL loading mechanism
   * @return URL for the subject resource
   * @throws MalformedURLException if the URI cannot be successfully converted
   *    to a URL
   * @throws IOException if the resource cannot be found
   */
  public static URL getResource(URI uri)
      throws MalformedURLException, IOException {
    if (uri.getScheme() == null || uri.getScheme().equals("classpath")) {
      String path = uri.getSchemeSpecificPart();
      while (path.startsWith("/")) {
        path = path.substring(1);
      }
      return getResource(path, 
          Thread.currentThread().getContextClassLoader());
    }
    else {
      return uri.toURL();
    }
  }

  /**
   * Gets a URL for a resource.
   * @param name name of the resource
   * @param relativeToClass class which will be used as the basis for locating
   *   the resource
   * @return resource URL
   * @throws IOException if the resource cannot be located
   */
  public static URL getResource(String name, Class<?> relativeToClass)
      throws IOException {
    final URL resource = relativeToClass.getResource(name);
    assertNotNull(resource, new FileNotFoundException(
        name + " in package " + relativeToClass.getPackage().getName()));
    return resource;
  }

  /**
   * Gets a URL for a resource.
   * @param name name of the resource
   * @param classLoader classLoader that will be used to locate the resource
   * @return resource URL
   * @throws IOException if the resource cannot be located
   */
  public static URL getResource(String name, ClassLoader classLoader)
      throws IOException {
    final URL resource = classLoader.getResource(name);
    assertNotNull(resource, new FileNotFoundException(
        name + " at root of " + classLoader));
    return resource;
  }

  /**
   * Gets a resource as an input stream.
   * <p>
   * Classpath resources accessed by this method will use the thread
   * context classloader.
   * 
   * @param uri string representation of a URI which can be a just a path 
   *    (to a filesystem resource), a {@code classpath:} URI, or any URL
   *    supported by the JRE's URL loading mechanism
   * @return input stream
   * @throws IOException
   */
  public static InputStream getResourceAsStream(String uri) throws IOException {
    return getResourceAsStream(URI.create(uri));
  }

  /**
   * Gets a resource as an input stream.
   * <p>
   * Classpath resources accessed by this method will use the thread
   * context classloader.
   * 
   * @param uri a URI which can be a just a path (to a classpath resource), 
   *    a {@code classpath:} URI, or any URL supported by the JRE's URL.
   *    loading mechanism
   * @return input stream
   * @throws MalformedURLException
   * @throws IOException
   */
  public static InputStream getResourceAsStream(URI uri)
      throws MalformedURLException, IOException {
    URL location = getResource(uri);
    assertNotNull(location, new FileNotFoundException(uri.toString()));
    return location.openStream();
  }
  
  /**
   * Gets a resource as an input stream.
   * <p>
   * @param location any URL supported by the JRE's URL loading system
   * @return input stream
   * @throws MalformedURLException
   * @throws IOException
   */
  public static InputStream getResourceAsStream(URL location)
      throws MalformedURLException, IOException {
    assertNotNull(location, new FileNotFoundException("url is required"));
    return location.openStream();
  }

  /**
   * Gets a resource as an input stream.
   * @param name name of the resource
   * @param relativeToClass class which will be used as the basis for locating
   *   the resource
   * @return input stream input stream
   * @throws IOException
   */
  public static InputStream getResourceAsStream(String name, 
      Class<?> relativeToClass) throws IOException {
    URL location = getResource(name, relativeToClass);
    assertNotNull(location, new FileNotFoundException(
        relativeToClass.getPackage().getName() + "." + name));
    return location.openStream();
  }

  /**
   * Gets a resource as an input stream.
   * @param name name of the resource
   * @param classLoader classloader that will be used to locate the resource
   * @return input stream input stream
   * @throws IOException
   */
  public static InputStream getResourceAsStream(String name, 
      ClassLoader classLoader) throws IOException {
    URL location = getResource(name, classLoader);
    assertNotNull(location, new FileNotFoundException(name));
    return location.openStream();
  }

  private static void assertNotNull(URL location, IOException ex) 
      throws IOException {
    if (location == null) throw ex;
  }
  
}
