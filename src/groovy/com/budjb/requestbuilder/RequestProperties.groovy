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

import grails.util.Holders
import org.codehaus.groovy.grails.commons.GrailsApplication

class RequestProperties implements Cloneable {
    /**
     * Grails application bean.
     */
    GrailsApplication grailsApplication

    /**
     * The Connection Timeout for the Client.
     */
    Integer connectionTimeout

    /**
     * The Read Timeout for the Client.
     */
    Integer readTimeout

    /**
     * The URI to hit.
     *
     * Can either be a String or an URI.
     */
    Object uri

    /**
     * Query parameters.
     */
    Map query

    /**
     * Headers
     */
    Map headers

    /**
     * Form parameters
     *
     * Note that if this is set, this is used and body is ignored.
     */
    Map form

    /**
     * Content-Type header.
     */
    String contentType

    /**
     * Accept header.
     */
    String accept

    /**
     * Cookies to include with the request.
     */
    Map cookies

    /**
     * Whether to attempt to slurp JSON automatically.
     */
    Boolean convertJson

    /**
     * Attempt to use XmlSlurper to parse xml.
     */
    Boolean convertXML

    /**
     * Whether the caller expects a binary return.
     *
     * If true, the returned object will be a byte array.
     *
     * Note that this is a workaround to jersey not providing
     * automatic content conversion based on mime type.
     */
    Boolean binaryResponse

    /**
     * If true, do not check the response status code, and
     * thus don't throw an exception for codes > 2xx.
     */
    Boolean skipStatusCheck

    /**
     * If true, do not response handling, and return the raw response object.
     */
    Boolean rawClientResponse

    /**
     * Whether to automatically follow redirects.
     */
    Boolean followRedirects

    /**
     * Body of the request - only useful on POST or PUT.
     */
    Object body

    /**
     * Whether to log the request and response.
     */
    Boolean debug

    /**
     * Whether to ignore SSL cert validation.
     */
    Boolean ignoreInvalidSSL

    /**
     * If true uses BasicAuth
     */
    Boolean useBasicAuth

    /**
     * basic auth user name
     */
    String basicAuthUserName

    /**
     * basic auth password
     */
    String basicAuthPassword

    /**
     * Size (in bytes) to chunk the request.
     */
    Integer chunkSize

    /**
     * Encode the request with gzip compression.
     */
    Boolean encodeGzip

    /**
     * Constructor.
     */
    RequestProperties() {
        applyDefaults()
    }

    /**
     * Constructor.
     *
     * @param grailsApplication
     * @return
     */
    RequestProperties(GrailsApplication grailsApplication) {
        this.grailsApplication = grailsApplication
        applyDefaults()
    }

    /**
     * Configures the request properties object from a closure.
     */
    RequestProperties build(Closure closure) {
        closure = closure.clone()
        closure.delegate = this
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure()

        return this
    }

    /**
     * Validates that all parameters are valid.
     */
    void validate() throws IllegalArgumentException {
        if (!uri) {
            throw new IllegalArgumentException("URI to request must not be null")
        }
    }

    /**
     * Applies default values to each property.
     *
     * @param properties
     */
    protected void applyDefaults() {
        ConfigObject config = getGrailsApplication().config.jerseyRequestBuilder

        accept = applyDefault(String, config.accept)
        basicAuthPassword = applyDefault(String, config.basicAuthPassword)
        basicAuthUserName = applyDefault(String, config.basicAuthUserName)
        binaryResponse = applyDefault(Boolean, config.binaryResponse, false)
        chunkSize = applyDefault(Integer, config.chunkSize)
        connectionTimeout = applyDefault(Integer, config.connectionTimeout)
        contentType = applyDefault(String, config.contentType)
        convertJson = applyDefault(Boolean, config.convertJson, true)
        convertXML = applyDefault(Boolean, config.convertXML, true)
        cookies = [:]
        debug = applyDefault(Boolean, config.debug, false)
        encodeGzip = applyDefault(Boolean, config.encodeGzip, false)
        followRedirects = applyDefault(Boolean, config.followRedirects, true)
        form = [:]
        headers = [:]
        ignoreInvalidSSL = applyDefault(Boolean, config.ignoreInvalidSSL, false)
        query = [:]
        rawClientResponse = applyDefault(Boolean, config.rawClientResponse, false)
        readTimeout = applyDefault(Integer, config.readTimeout)
        skipStatusCheck = applyDefault(Boolean, config.skipStatusCheck, false)
        useBasicAuth = applyDefault(Boolean, config.useBasicAuth, false)
    }

    /**
     * Returns the first non-null value in a list of default values.
     *
     * @param values
     * @return
     */
    protected Object applyDefault(Class<?> type, Object... values) {
        for (Object value : values) {
            if (value instanceof ConfigObject && value.isEmpty()) {
                continue
            }
            if (value == null) {
                continue
            }
            try {
                return value.asType(type)
            }
            catch (Exception e) {
                continue
            }
        }

        return null
    }

    /**
     * Returns the grails application bean.
     *
     * This method provides a convenience so that users are
     * not responsible for injecting the Grails application bean
     * themselves. This also does allow the Grails application bean
     * to be injected for testing.
     *
     * @return
     */
    protected GrailsApplication getGrailsApplication() {
        if (!grailsApplication) {
            grailsApplication = Holders.grailsApplication
        }
        return grailsApplication
    }

    /**
     * Does a 1-level deep clone of the properties object.
     *
     * @return
     */
    Object clone() {
        RequestProperties clone = (RequestProperties) super.clone()

        clone.query = (Map) query.clone()
        clone.headers = (Map) headers.clone()
        clone.form = (Map) form.clone()
        clone.cookies = (Map) cookies.clone()

        return clone
    }
}
