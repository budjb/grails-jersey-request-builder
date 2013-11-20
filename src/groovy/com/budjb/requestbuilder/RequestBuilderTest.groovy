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

abstract class RequestBuilderTest extends GroovyTestCase {
    /**
     * Returns the base URL of the running server.
     *
     * @return
     */
    protected String getBaseUrl() {
        // Get the base URL property
        String baseUrl = System.getProperty('grails.functional.test.baseURL')

        // Strip trailing slash
        if (baseUrl[-1] == '/') {
            baseUrl = baseUrl[0..-2]
        }

        return baseUrl
    }

    /**
     * Convenience method to return the constructed URL to hit on the internal server.
     *
     * @param path
     * @return
     */
    protected String getUri(String path) {
        if (!path) {
            return getBaseUrl()
        }
        if (path[0] == '/') {
            path = path[1..-1]
        }
        return "${getBaseUrl()}/${path}"
    }
}
