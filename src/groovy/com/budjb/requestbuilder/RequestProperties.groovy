package com.budjb.requestbuilder

import grails.util.Holders
import org.codehaus.groovy.grails.commons.GrailsApplication

class RequestProperties implements Cloneable {
    /**
     * Grails application bean.
     */
    private GrailsApplication grailsApplication

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
    private Map query

    /**
     * Headers
     */
    private Map headers

    /**
     * Form parameters
     *
     * Note that if this is set, this is used and body is ignored.
     */
    private Map form

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
    public RequestProperties() {
        applyDefaults()
    }

    /**
     * Constructor.
     *
     * @param grailsApplication
     * @return
     */
    public RequestProperties(GrailsApplication grailsApplication) {
        setGrailsApplication(grailsApplication)
    }

    /**
     * Sets the grails application bean.
     *
     * @param grailsApplication
     */
    public void setGrailsApplication(GrailsApplication grailsApplication) {
        this.grailsApplication = grailsApplication

        applyDefaults()
    }

    /**
     * Returns the grails application bean.
     *
     * @return
     */
    public GrailsApplication getGrailsApplication() {
        if (!grailsApplication) {
            grailsApplication = Holders.grailsApplication
        }
        return grailsApplication
    }

    /**
     * Configures the request properties object from a closure.
     */
    public RequestProperties build(Closure closure) {
        closure.delegate = this
        closure.resolveStrategy = Closure.OWNER_FIRST
        closure()

        validate()

        return this
    }

    /**
     * Validates that all parameters are valid.
     */
    public void validate() throws IllegalArgumentException {
        if (!uri) {
            throw new IllegalArgumentException("URI to request must not be null")
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

    Map getQuery() {
        return query
    }

    void setQuery(Map query) {
        this.query = query
    }

    Map getHeaders() {
        return headers
    }

    void setHeaders(Map headers) {
        this.headers = headers
    }

    Map getForm() {
        return form
    }

    void setForm(Map form) {
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
        values = values.collect {
            if (it instanceof ConfigObject && it.isEmpty()) {
                return null
            }
            try {
                return it.asType(type)
            }
            catch (Exception e) {
                return null
            }
        }

        return values.find { it != null }
    }
}
