/*
 * Copyright 2013-2015 Bud Byrd
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
import grails.util.Holders
import org.apache.log4j.Logger

/**
 * The <code>RequestBuilder</code> abstracts the underlying code to
 * build a Jersey Client request into a closure.
 *
 * @author Bud Byrd <bud.byrd@gmail.com>
 */
@Deprecated
class RequestBuilder {
    /**
     * Logger
     */
    private Logger log = Logger.getLogger(RequestBuilder)

    /**
     * Jersey request builder bean.
     */
    protected JerseyRequestBuilder jerseyRequestBuilder

    /**
     * The Connection Timeout for the Client.
     */
    public Integer connectionTimeout

    /**
     * The Read Timeout for the Client.
     */
    public Integer readTimeout

    /**
     * The URI to hit.
     *
     * Can either be a String or an URI.
     */
    public Object uri

    /**
     * Query parameters.
     */
    public Map query = [:]

    /**
     * Headers
     */
    public Map headers = [:]

    /**
     * Form parameters
     *
     * Note that if this is set, this is used and body is ignored.
     */
    public Map form = [:]

    /**
     * Content-Type header.
     */
    public String contentType

    /**
     * Accept header.
     */
    public String accept

    /**
     * Cookies to include with the request.
     */
    public Map cookies = [:]

    /**
     * Whether to attempt to slurp JSON automatically.
     */
    public Boolean convertJson

    /**
     * Attempt to use xmlslurpler to parse xml.
     */
    public Boolean convertXML

    /**
     * Whether the caller expects a binary return.
     *
     * If true, the returned object will be a byte array.
     *
     * Note that this is a workaround to jersey not providing
     * automatic content conversion based on mime type.
     */
    public Boolean binaryResponse

    /**
     * If true, do not check the response status code, and
     * thus don't throw an exception for codes > 2xx.
     */
    public Boolean skipStatusCheck

    /**
     * If true, do not response handling, and return the raw response object.
     */
    public Boolean rawClientResponse

    /**
     * Whether to automatically follow redirects.
     */
    public Boolean followRedirects

    /**
     * Body of the request - only useful on POST or PUT.
     */
    public Object body

    /**
     * Whether to log the request and response.
     */
    public Boolean debug

    /**
     * Whether to ignore SSL cert validation.
     */
    public Boolean ignoreInvalidSSL

    /**
     * If true uses BasicAuth
     */
    public Boolean useBasicAuth

    /**
     * basic auth user name
     */
    public String basicAuthUserName

    /**
     * basic auth password
     */
    public String basicAuthPassword

    /**
     * Size (in bytes) to chunk the request.
     */
    public Integer chunkSize

    /**
     * Encode the request with gzip compression.
     */
    public Boolean encodeGzip

    /**
     * Performs a GET request.
     *
     * @throws IllegalArgumentException
     * @return
     */
    @Deprecated
    public Object get() throws IllegalArgumentException {
        return getJerseyRequestBuilder().get(buildRequestProperties())
    }

    /**
     * Performs a GET request.
     *
     * @param uri Request URI
     * @throws IllegalArgumentException
     * @return
     */
    @Deprecated
    public Object get(String uri) throws IllegalArgumentException {
        // Set the uri
        this.uri = uri

        // Run the command
        return get()
    }

    /**
     * Performs a GET request.
     *
     * @param closure
     * @throws IllegalArgumentException
     * @return
     */
    @Deprecated
    public Object get(Closure closure) throws IllegalArgumentException {
        // Run the provided closure
        run closure

        // Run the command
        return get()
    }

    /**
     * Performs an OPTIONS request.
     *
     * @throws IllegalArgumentException
     * @return
     */
    @Deprecated
    public ClientResponse options() throws IllegalArgumentException {
        return getJerseyRequestBuilder().options(buildRequestProperties())
    }

    /**
     * Performs an OPTIONS request.
     *
     * @param uri Request URI
     * @throws IllegalArgumentException
     * @return
     */
    @Deprecated
    public ClientResponse options(String uri) throws IllegalArgumentException {
        // Set the uri
        this.uri = uri

        // Run the command
        return options()
    }

    /**
     * Performs an OPTIONS request.
     *
     * @param closure
     * @throws IllegalArgumentException
     * @return
     */
    @Deprecated
    public ClientResponse options(Closure closure) throws IllegalArgumentException {
        // Run the provided closure
        run closure

        // Run the command
        return options()
    }

    /**
     * Performs a TRACE request.
     *
     * @throws IllegalArgumentException
     * @return
     */
    @Deprecated
    public Object trace() throws IllegalArgumentException {
        return getJerseyRequestBuilder().trace(buildRequestProperties())
    }

    /**
     * Performs a TRACE request.
     *
     * @param uri Request URI
     * @throws IllegalArgumentException
     * @return
     */
    @Deprecated
    public Object trace(String uri) throws IllegalArgumentException {
        // Set the uri
        this.uri = uri

        // Run the command
        return trace()
    }

    /**
     * Performs a TRACE request.
     *
     * @param closure
     * @throws IllegalArgumentException
     * @return
     */
    @Deprecated
    public Object trace(Closure closure) throws IllegalArgumentException {
        // Run the provided closure
        run closure

        // Run the command
        return trace()
    }

    /**
     * Performs a HEAD request.
     *
     * @throws IllegalArgumentException
     * @return
     */
    @Deprecated
    public ClientResponse head() throws IllegalArgumentException {
        return getJerseyRequestBuilder().head(buildRequestProperties())
    }

    /**
     * Performs a HEAD request.
     *
     * @param uri Request URI
     * @throws IllegalArgumentException
     * @return
     */
    @Deprecated
    public ClientResponse head(String uri) throws IllegalArgumentException {
        // Set the uri
        this.uri = uri

        // Run the command
        return head()
    }

    /**
     * Performs a HEAD request.
     *
     * @param closure
     * @throws IllegalArgumentException
     * @return
     */
    @Deprecated
    public ClientResponse head(Closure closure) throws IllegalArgumentException {
        // Run the provided closure
        run closure

        // Run the command
        return head()
    }

    /**
     * Performs a PUT request.
     *
     * @throws IllegalArgumentException
     * @return
     */
    @Deprecated
    public Object put() throws IllegalArgumentException {
        return getJerseyRequestBuilder().put(buildRequestProperties())
    }

    /**
     * Performs a PUT request.
     *
     * @param uri Request URI
     * @throws IllegalArgumentException
     * @return
     */
    @Deprecated
    public Object put(String uri, body = null) throws IllegalArgumentException {
        // Set the uri
        this.uri = uri

        // Set the request body
        this.body = body

        // Run the command
        return put()
    }

    /**
     * Performs a PUT request.
     *
     * @param closure
     * @throws IllegalArgumentException
     * @return
     */
    @Deprecated
    public Object put(Closure closure) throws IllegalArgumentException {
        // Run the provided closure
        run closure

        // Run the command
        return put()
    }

    /**
     * Performs a POST request.
     *
     * @throws IllegalArgumentException
     * @return
     */
    @Deprecated
    public Object post() throws IllegalArgumentException {
        return getJerseyRequestBuilder().post(buildRequestProperties())
    }

    /**
     * Performs a POST request.
     *
     * @param uri Request URI
     * @throws IllegalArgumentException
     * @return
     */
    @Deprecated
    public Object post(String uri, body = null) throws IllegalArgumentException {
        // Set the uri
        this.uri = uri

        // Set the request body
        this.body = body

        // Run the command
        return post()
    }

    /**
     * Performs a POST request.
     *
     * @param closure
     * @throws IllegalArgumentException
     * @return
     */
    @Deprecated
    public Object post(Closure closure) throws IllegalArgumentException {
        // Run the provided closure
        run closure

        // Run the command
        return post()
    }

    /**
     * Performs a DELETE request.
     *
     * @throws IllegalArgumentException
     * @return
     */
    @Deprecated
    public Object delete() throws IllegalArgumentException {
        return getJerseyRequestBuilder().delete(buildRequestProperties())
    }

    /**
     * Performs a DELETE request.
     *
     * @param uri Request URI
     * @throws IllegalArgumentException
     * @return
     */
    @Deprecated
    public Object delete(String uri) throws IllegalArgumentException {
        // Set the uri
        this.uri = uri

        // Run the command
        return delete()
    }

    /**
     * Performs a DELETE request.
     *
     * @param closure
     * @throws IllegalArgumentException
     * @return
     */
    @Deprecated
    public Object delete(Closure closure) throws IllegalArgumentException {
        // Run the provided closure
        run closure

        // Run the command
        return delete()
    }

    /**
     * Runs a passed closure to implement builder-style operation.
     *
     * @param closure
     */
    protected void run(Closure closure) {
        closure.delegate = this
        closure.resolveStrategy = Closure.OWNER_FIRST
        closure()
    }

    /**
     * Sets the jersey request builder bean.
     *
     * @param jerseyRequestBuilder
     */
    public void setJerseyRequestBuilder(JerseyRequestBuilder jerseyRequestBuilder) {
        this.jerseyRequestBuilder = jerseyRequestBuilder
    }

    /**
     * Returns the jersey request builder, loading from the application context if necessary.
     *
     * @return
     */
    protected JerseyRequestBuilder getJerseyRequestBuilder() {
        if (!jerseyRequestBuilder) {
            jerseyRequestBuilder = Holders.applicationContext.getBean('jerseyRequestBuilder')
        }
        return jerseyRequestBuilder
    }

    /**
     * Creates a request properties object for use with the jersey request builder.
     *
     * @return
     */
    protected RequestProperties buildRequestProperties() {
        RequestProperties properties = new RequestProperties()

        if (accept != null) {
            properties.accept = accept
        }

        if (basicAuthPassword != null) {
            properties.basicAuthPassword = basicAuthPassword
        }

        if (basicAuthUserName != null) {
            properties.basicAuthUserName = basicAuthUserName
        }

        if (binaryResponse != null) {
            properties.binaryResponse = binaryResponse
        }

        if (body != null) {
            properties.body = body
        }

        if (chunkSize != null) {
            properties.chunkSize = chunkSize
        }

        if (connectionTimeout != null) {
            properties.connectionTimeout = connectionTimeout
        }

        if (contentType != null) {
            properties.contentType = contentType
        }

        if (convertJson != null) {
            properties.convertJson = convertJson
        }

        if (convertXML != null) {
            properties.convertXML = convertXML
        }

        if (!cookies.isEmpty()) {
            properties.cookies = cookies
        }

        if (debug != null) {
            properties.debug = debug
        }

        if (encodeGzip != null) {
            properties.encodeGzip = encodeGzip
        }

        if (followRedirects != null) {
            properties.followRedirects = followRedirects
        }

        if (!form.isEmpty()) {
            properties.form = form
        }

        if (!headers.isEmpty()) {
            properties.headers = headers
        }

        if (ignoreInvalidSSL != null) {
            properties.ignoreInvalidSSL = ignoreInvalidSSL
        }

        if (!query.isEmpty()) {
            properties.query = query
        }

        if (rawClientResponse != null) {
            properties.rawClientResponse = rawClientResponse
        }

        if (readTimeout != null) {
            properties.readTimeout = readTimeout
        }

        if (skipStatusCheck != null) {
            properties.skipStatusCheck = skipStatusCheck
        }

        if (uri != null) {
            properties.uri = uri
        }

        if (useBasicAuth != null) {
            properties.useBasicAuth = useBasicAuth
        }

        return properties
    }
}
