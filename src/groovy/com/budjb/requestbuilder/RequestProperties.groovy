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

/**
 * A data object containing configuration options for an HTTP request to be executed by
 * the Jersey Request Builder facade.
 */
class RequestProperties implements Cloneable {
    /**
     * Grails application bean.
     */
    GrailsApplication grailsApplication

    /**
     * The Connection Timeout for the Client.
     */
    private Integer connectionTimeout

    /**
     * The Read Timeout for the Client.
     */
    private Integer readTimeout

    /**
     * The URI to hit.
     *
     * Can either be a String or an URI.
     */
    private Object uri

    /**
     * Query parameters.
     */
    private Map<String, Object> query

    /**
     * Headers
     */
    private Map<String, Object> headers

    /**
     * Form parameters
     *
     * Note that if this is set, this is used and body is ignored.
     */
    private Map<String, Object> form

    /**
     * Content-Type header.
     */
    private String contentType

    /**
     * Accept header.
     */
    private String accept

    /**
     * Cookies to include with the request.
     */
    private Map cookies

    /**
     * Whether to attempt to slurp JSON automatically.
     */
    private Boolean convertJson

    /**
     * Attempt to use XmlSlurper to parse xml.
     */
    private Boolean convertXML

    /**
     * Whether the caller expects a binary return.
     *
     * If true, the returned object will be a byte array.
     *
     * Note that this is a workaround to jersey not providing
     * automatic content conversion based on mime type.
     */
    private Boolean binaryResponse

    /**
     * If true, do not check the response status code, and
     * thus don't throw an exception for codes > 2xx.
     */
    private Boolean skipStatusCheck

    /**
     * If true, do not response handling, and return the raw response object.
     */
    private Boolean rawClientResponse

    /**
     * Whether to automatically follow redirects.
     */
    private Boolean followRedirects

    /**
     * Body of the request - only useful on POST or PUT.
     */
    private Object body

    /**
     * Whether to log the request and response.
     */
    private Boolean debug

    /**
     * Whether to ignore SSL cert validation.
     */
    private Boolean ignoreInvalidSSL

    /**
     * If true uses BasicAuth
     */
    private Boolean useBasicAuth

    /**
     * basic auth user name
     */
    private String basicAuthUserName

    /**
     * basic auth password
     */
    private String basicAuthPassword

    /**
     * Size (in bytes) to chunk the request.
     */
    private Integer chunkSize

    /**
     * Encode the request with gzip compression.
     */
    private Boolean encodeGzip

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

        if (!config) {
            config = new ConfigObject()
        }

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

    /**
     * Adds a header to the request.  Multiple values per header name are supported.
     *
     * @param key
     * @param value
     */
    void addHeader(String key, String value) {
        addMultivaluedMap(headers, key, value)
    }

    /**
     * Sets a header with a single value.  Any existing headers with the same name will be lost.
     *
     * @param key
     * @param value
     */
    void setHeader(String key, String value) {
        headers[key] = value
    }

    /**
     * Adds a query parameter to the request.  Multiple values per query parameter name are supported.
     *
     * @param key
     * @param value
     */
    void addQuery(String key, String value) {
        addMultivaluedMap(query, key, value)
    }

    /**
     * Sets a query parameter with a single value.  Any existing query parameters with the same name will be lost.
     *
     * @param key
     * @param value
     */
    void setQuery(String key, String value) {
        query[key] = value
    }

    /**
     * Adds a form field to the request.  Multiple values per form field name are supported.
     *
     * @param key
     * @param value
     */
    void addFormField(String key, String value) {
        addMultivaluedMap(form, key, value)
    }

    /**
     * Sets a form field with a single value.  Any existing form fields with the same name will be lost.
     *
     * @param key
     * @param value
     */
    void setFormField(String key, String value) {
        form[key] = value
    }

    /**
     * Adds a an entry to the given map, creating a multivalued entry if necessary.
     *
     * @param map
     * @param key
     * @param value
     */
    protected void addMultivaluedMap(Map<String, Object> map, String key, String value) {
        if (map.containsKey(key)) {
            if (map[key] instanceof List) {
                map[key] << value
            }
            else {
                map[key] = [map[key], value]
            }
        }
        else {
            map[key] = value
        }
    }

    Integer getConnectionTimeout() {
        return connectionTimeout
    }

    void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout
    }

    Integer getReadTimeout() {
        return readTimeout
    }

    void setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout
    }

    Object getUri() {
        return uri
    }

    void setUri(Object uri) {
        this.uri = uri
    }

    Map<String, Object> getQuery() {
        return query
    }

    void setQuery(Map<String, Object> query) {
        this.query = query
    }

    Map<String, Object> getHeaders() {
        return headers
    }

    void setHeaders(Map<String, Object> headers) {
        this.headers = headers
    }

    Map<String, Object> getForm() {
        return form
    }

    void setForm(Map<String, Object> form) {
        this.form = form
    }

    String getContentType() {
        return contentType
    }

    void setContentType(String contentType) {
        this.contentType = contentType
    }

    String getAccept() {
        return accept
    }

    void setAccept(String accept) {
        this.accept = accept
    }

    Map getCookies() {
        return cookies
    }

    void setCookies(Map cookies) {
        this.cookies = cookies
    }

    Boolean getConvertJson() {
        return convertJson
    }

    void setConvertJson(Boolean convertJson) {
        this.convertJson = convertJson
    }

    Boolean getConvertXML() {
        return convertXML
    }

    void setConvertXML(Boolean convertXML) {
        this.convertXML = convertXML
    }

    Boolean getBinaryResponse() {
        return binaryResponse
    }

    void setBinaryResponse(Boolean binaryResponse) {
        this.binaryResponse = binaryResponse
    }

    Boolean getSkipStatusCheck() {
        return skipStatusCheck
    }

    void setSkipStatusCheck(Boolean skipStatusCheck) {
        this.skipStatusCheck = skipStatusCheck
    }

    Boolean getRawClientResponse() {
        return rawClientResponse
    }

    void setRawClientResponse(Boolean rawClientResponse) {
        this.rawClientResponse = rawClientResponse
    }

    Boolean getFollowRedirects() {
        return followRedirects
    }

    void setFollowRedirects(Boolean followRedirects) {
        this.followRedirects = followRedirects
    }

    Object getBody() {
        return body
    }

    void setBody(Object body) {
        this.body = body
    }

    Boolean getDebug() {
        return debug
    }

    void setDebug(Boolean debug) {
        this.debug = debug
    }

    Boolean getIgnoreInvalidSSL() {
        return ignoreInvalidSSL
    }

    void setIgnoreInvalidSSL(Boolean ignoreInvalidSSL) {
        this.ignoreInvalidSSL = ignoreInvalidSSL
    }

    Boolean getUseBasicAuth() {
        return useBasicAuth
    }

    void setUseBasicAuth(Boolean useBasicAuth) {
        this.useBasicAuth = useBasicAuth
    }

    String getBasicAuthUserName() {
        return basicAuthUserName
    }

    void setBasicAuthUserName(String basicAuthUserName) {
        this.basicAuthUserName = basicAuthUserName
    }

    String getBasicAuthPassword() {
        return basicAuthPassword
    }

    void setBasicAuthPassword(String basicAuthPassword) {
        this.basicAuthPassword = basicAuthPassword
    }

    Integer getChunkSize() {
        return chunkSize
    }

    void setChunkSize(Integer chunkSize) {
        this.chunkSize = chunkSize
    }

    Boolean getEncodeGzip() {
        return encodeGzip
    }

    void setEncodeGzip(Boolean encodeGzip) {
        this.encodeGzip = encodeGzip
    }
}
