/*
 * Copyright 2015 Bud Byrd
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

import com.sun.jersey.api.client.ClientResponse

interface JerseyRequestBuilder {
    /**
     * Performs a DELETE request to the given URI.
     *
     * @param uri
     * @return
     */
    Object delete(String uri)

    /**
     * Performs a DELETE request with the request properties configured in the given closure.
     *
     * @param closure
     * @return
     */
    Object delete(Closure closure)

    /**
     * Performs a DELETE request with the given request properties.
     *
     * @param requestProperties
     * @return
     */
    Object delete(RequestProperties requestProperties)

    /**
     * Performs a GET request to the given URI.
     *
     * @param uri
     * @return
     */
    Object get(String uri)

    /**
     * Performs a GET request with the request properties configured in the given closure.
     *
     * @param closure
     * @return
     */
    Object get(Closure closure)

    /**
     * Performs a GET request with the given request properties.
     *
     * @param requestProperties
     * @return
     */
    Object get(RequestProperties requestProperties)

    /**
     * Performs a HEAD request to the given URI.
     *
     * @param uri
     * @return
     */
    ClientResponse head(String uri)

    /**
     * Performs a HEAD request with the request properties configured in the given closure.
     *
     * @param closure
     * @return
     */
    ClientResponse head(Closure closure)

    /**
     * Performs a HEAD request with the given request properties.
     *
     * @param requestProperties
     * @return
     */
    ClientResponse head(RequestProperties requestProperties)

    /**
     * Performs an OPTIONS request to the given URI.
     *
     * @param uri
     * @return
     */
    Object options(String uri)

    /**
     * Performs an OPTIONS request with the request properties configured in the given closure.
     *
     * @param closure
     * @return
     */
    Object options(Closure closure)

    /**
     * Performs an OPTIONS request with the given request properties.
     *
     * @param requestProperties
     * @return
     */
    Object options(RequestProperties requestProperties)

    /**
     * Performs a POST request to the given URI.
     *
     * @param uri
     * @return
     */
    Object post(String uri)

    /**
     * Performs a POST request to the given URI.
     *
     * @param uri
     * @param body
     * @return
     */
    Object post(String uri, Object body)

    /**
     * Performs a POST request with the request properties configured in the given closure.
     *
     * @param closure
     * @return
     */
    Object post(Closure closure)

    /**
     * Performs a POST request with the given request properties.
     *
     * @param requestProperties
     * @return
     */
    Object post(RequestProperties requestProperties)

    /**
     * Performs a PUT request to the given URI.
     *
     * @param uri
     * @return
     */
    Object put(String uri)

    /**
     * Performs a PUT request to the given URI.
     *
     * @param uri
     * @param body
     * @return
     */
    Object put(String uri, Object body)

    /**
     * Performs a PUT request with the request properties configured in the given closure.
     *
     * @param closure
     * @return
     */
    Object put(Closure closure)

    /**
     * Performs a PUT request with the given request properties.
     *
     * @param requestProperties
     * @return
     */
    Object put(RequestProperties requestProperties)

    /**
     * Performs a TRACE request to the given URI.
     *
     * @param uri
     * @return
     */
    Object trace(String uri)

    /**
     * Performs a TRACE request with the request properties configured in the given closure.
     *
     * @param closure
     * @return
     */
    Object trace(Closure closure)

    /**
     * Performs a TRACE request with the given request properties.
     *
     * @param requestProperties
     * @return
     */
    Object trace(RequestProperties requestProperties)
}
