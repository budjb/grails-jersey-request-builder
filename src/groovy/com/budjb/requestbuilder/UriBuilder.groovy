/*
 * Copyright 2013 Bud Byrd
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
 */
package com.budjb.requestbuilder

import javax.ws.rs.core.UriBuilder as JsrUriBuilder
import org.apache.log4j.Logger

/**
 * Convenience wrapper around JSR311's UriBuilder that provides
 * a groovy-like builder syntax.
 *
 * The class can be used in 3 ways:
 *
 * Builder-style
 * -------------
 * <code>
 * URI uri = UriBuilder.build {
 *     host = 'www.example.com'
 *     path = ['resource', someVariable, 'view']
 *     port = 80
 *     scheme = 'http'
 * }
 * </code>
 *
 * Object-style
 * ------------
 * <code>
 * UriBuilder builder = new UriBuilder()
 * builder.host = 'www.example.com'
 * builder.path = ['resource', someVariable, 'view']
 * builder.port = 80
 * builder.scheme = 'http'
 * Uri uri = builder.build()
 * </code>
 *
 * Template-style
 * --------------
 * <code>
 * Uri uri = UriBuilder.build('http://www.example.com/resource/{var1}/view', someVariable)
 * </code>
 *
 * Notes
 * -----
 * The class has a propery named <code>base</code>.  When set via the builder syntax or
 * object syntax, the uri will be parsed first, and then any other properties will override
 * those values parsed from the uri, except for path which will add to it.  As an example:
 *
 * <code>
 * URI uri = UriBuilder.build {
 *     base = 'https://www.example.com:8080/old/path'
 *     host = 'foo.bar'
 *     path = ['new', 'path']
 *     scheme = 'http'
 *     port = 8081
 * }
 * </code>
 * This code will result in the URI: <code>http://foo.bar:8081/old/path/new/path</code>.
 */
class UriBuilder {
    /**
     * Logger
     */
    private Logger log = Logger.getLogger(getClass().name)

    /**
     * Base URL
     */
    String base

    /**
     * Host name
     */
    String host

    /**
     * Port number (default 80)
     */
    Integer port

    /**
     * Path parts
     */
    List path = []

    /**
     * Query parameters
     */
    Map query = [:]

    /**
     * Protocol (default "http")
     */
    String scheme

    /**
     * URL fragment
     */
    String fragment

    /**
     * Creates a URI via a builder syntax.
     *
     * @param closure
     * @return
     */
    static URI build(Closure closure) {
        // Create the builder
        UriBuilder builder = new UriBuilder()

        // Run the closure
        builder.run(closure)

        // Build and return a URI object
        return builder.build()
    }

    /**
     * Convenience function that builds a URI based on a path template.
     *
     * Example:
     * <code>UriBuilder.build('http://www.example.com/api/resource/{var1}/{var2}', variable1, variable2)</code>
     *
     * @param template
     * @param pieces
     * @return
     */
    static URI build(String template, String... pieces) {
        // Run through as a template
        return JsrUriBuilder.fromPath(template).build(pieces)
    }

    /**
     * Runs a passed closure to implement builder-style operation.
     *
     * @param closure
     */
    private void run(Closure closure) {
        closure.delegate = this
        closure.resolveStrategy = Closure.OWNER_FIRST
        closure.call()
    }

    /**
     * Does the work of building the URI.
     *
     * @return
     */
    URI build() {
        JsrUriBuilder builder

        // If a base URL is set, parse it out
        if (base) {
            builder = JsrUriBuilder.fromUri(base)
        }
        else {
            // The host is absolutely required
            if (!host) {
                throw new IllegalArgumentException('host name is required')
            }

            // Create the builder
            builder = JsrUriBuilder.newInstance()

            // Make sure the scheme is set
            scheme = scheme ?: 'http'

            // Make sure the port is set
            port = port ?: 80
        }

        // Set the scheme
        if (scheme) {
            builder.scheme(scheme)
        }

        // Set the port
        if (port) {
            if ((scheme != 'http' || port != 80) && (scheme != 'https' || port != 443)) {
                builder.port(port)
            }
        }

        // Set the host
        if (host) {
            builder.host(host)
        }

        // Set the fragment
        if (fragment) {
            builder.fragment(fragment)
        }

        // Add path parts
        path.each {
            builder.path(it as String)
        }

        // Add query parts
        query.each { key, value ->
            builder.queryParam(key, value as String)
        }

        return builder.build()
    }
}
