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

import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.ClientHandlerException
import com.sun.jersey.api.client.ClientResponse
import com.sun.jersey.api.client.WebResource
import com.sun.jersey.api.client.WebResource.Builder
import com.sun.jersey.api.client.config.ClientConfig
import com.sun.jersey.api.client.config.DefaultClientConfig
import com.sun.jersey.api.client.filter.GZIPContentEncodingFilter
import com.sun.jersey.api.client.filter.LoggingFilter
import com.sun.jersey.api.representation.Form
import com.sun.jersey.client.urlconnection.HTTPSProperties
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter

import javax.ws.rs.core.Cookie
import javax.ws.rs.core.MediaType

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

import javax.net.ssl.SSLSession
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager
import javax.net.ssl.TrustManager
import javax.net.ssl.SSLContext
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.HostnameVerifier

import org.apache.log4j.Logger

import java.security.SecureRandom
import java.security.cert.X509Certificate

import grails.util.Holders

import groovy.util.ConfigObject

/**
 * The <code>RequestBuilder</code> abstracts the underlying code to
 * build a Jersey Client request into a closure.
 *
 * @author Bud Byrd <bud.byrd@gmail.com>
 */
class RequestBuilder {
    /**
     * Logger
     */
    private Logger log = Logger.getLogger(getClass().name)

    /**
     * The Connection Timeout for the Client.
     */
    Integer connectionTimeout = config.connectionTimeout ?: null

    /**
     * The Read Timeout for the Client.
     */
    Integer readTimeout = config.readTimeout ?: null

    /**
     * The URI to hit.
     *
     * Can either be a String or an URI.
     */
    Object uri

    /**
     * Query parameters.
     */
    Map query = [:]

    /**
     * Headers
     */
    Map headers = [:]

    /**
     * Form parameters
     *
     * Note that if this is set, this is used and body is ignored.
     */
    Map form = [:]

    /**
     * Content-Type header.
     */
    String contentType = config.contentType ?: null

    /**
     * Accept header.
     */
    String accept = config.accept ?: null

    /**
     * Cookies to include with the request.
     */
    Map cookies = [:]

    /**
     * Whether to attempt to slurp JSON automatically.
     */
    boolean convertJson = (config.convertJson.getClass() != ConfigObject) ? config.convertJson : true

    /**
     * Attempt to use xmlslurpler to parse xml.
     */
    boolean convertXML = (config.convertXML.getClass() != ConfigObject) ? config.convertXML : true

    /**
     * Whether the caller expects a binary return.
     *
     * If true, the returned object will be a byte array.
     *
     * Note that this is a workaround to jersey not providing
     * automatic content conversion based on mime type.
     */
    boolean binaryResponse = config.binaryResponse ?: false

    /**
     * If true, do not check the response status code, and
     * thus don't throw an exception for codes > 2xx.
     */
    boolean skipStatusCheck = config.skipStatusCheck ?: false

    /**
     * If true, do not response handling, and return the raw response object.
     */
    boolean rawClientResponse = config.rawClientResponse ?: false

    /**
     * Whether to automatically follow redirects.
     */
    boolean followRedirects = (config.followRedirects.getClass() != ConfigObject) ? config.followRedirects : true

    /**
     * Body of the request - only useful on POST or PUT.
     */
    Object body

    /**
     * Whether to log the request and response.
     */
    boolean debug = config.debug ?: false

    /**
     * Whether to ignore SSL cert validation.
     */
    boolean ignoreInvalidSSL = config.ignoreInvalidSSL ?: false

    /**
     * Logging output stream
     */
    private ByteArrayOutputStream loggingBuffer

    /**
     * If true uses BasicAuth
     */
    boolean useBasicAuth = config.useBasicAuth ?: false

    /**
     * basic auth user name
     */
    String basicAuthUserName = config.basicAuthUserName ?: null

    /**
     * basic auth password
     */
    String basicAuthPassword = config.basicAuthPassword ?: null

    /**
     * Size (in bytes) to chunk the request.
     */
    Integer chunkSize = config.chunkSize ?: null

    /**
     * Encode the request with gzip compression.
     */
    boolean encodeGzip = encodeGzip ?: false

    /**
     * Retrieve Jersey RequestBuilder portion of the Configuration.
     */
    private ConfigObject getConfig() {
        return Holders.config.jerseyRequestBuilder
    }

    /**
     * Performs a GET request.
     *
     * @throws IllegalArgumentException
     * @return
     */
    private doGet() throws IllegalArgumentException {
        if (uri == null) {
            throw new IllegalArgumentException('uri is required')
        }

        try {
            // Build the request
            WebResource.Builder request = buildRequest()

            // Do the request
            ClientResponse response = request.get(ClientResponse)

            // Handle the response
            return handleResponse(response)
        }
        catch (ClientHandlerException e) {
            if (e.cause) {
                throw e.cause
            }
            else {
                throw e
            }
        }
        finally {
            cleanUp()
        }
    }

    /**
     * Performs a GET request.
     *
     * @throws IllegalArgumentException
     * @return
     */
    Object get() throws IllegalArgumentException {
        return doGet()
    }

    /**
     * Performs a GET request.
     *
     * @param uri Request URI
     * @throws IllegalArgumentException
     * @return
     */
    Object get(String uri) throws IllegalArgumentException {
        // Set the uri
        this.uri = uri

        // Run the command
        return doGet()
    }

    /**
     * Performs a GET request.
     *
     * @param closure
     * @throws IllegalArgumentException
     * @return
     */
    Object get(Closure closure) throws IllegalArgumentException {
        // Run the provided closure
        run closure

        // Run the command
        return doGet()
    }

    /**
     * Performs an OPTIONS request.
     *
     * @throws IllegalArgumentException
     * @return
     */
    ClientResponse options() throws IllegalArgumentException {
        return doOptions()
    }

    /**
     * Performs an OPTIONS request.
     *
     * @param uri Request URI
     * @throws IllegalArgumentException
     * @return
     */
    ClientResponse options(String uri) throws IllegalArgumentException {
        // Set the uri
        this.uri = uri

        // Run the command
        return doOptions()
    }

    /**
     * Performs an OPTIONS request.
     *
     * @param closure
     * @throws IllegalArgumentException
     * @return
     */
    ClientResponse options(Closure closure) throws IllegalArgumentException {
        // Run the provided closure
        run closure

        // Run the command
        return doOptions()
    }

    /**
     * Performs an OPTIONS request.
     *
     * @throws IllegalArgumentException
     * @return
     */
    private ClientResponse doOptions() throws IllegalArgumentException {
        if (uri == null) {
            throw new IllegalArgumentException('uri is required')
        }

        try {
            // Build the request
            WebResource.Builder request = buildRequest()

            // Do the request
            return request.options(ClientResponse)
        }
        finally {
            cleanUp()
        }
    }

    /**
     * Performs a TRACE request.
     *
     * @throws IllegalArgumentException
     * @return
     */
    Object trace() throws IllegalArgumentException {
        return doTrace()
    }

    /**
     * Performs a TRACE request.
     *
     * @param uri Request URI
     * @throws IllegalArgumentException
     * @return
     */
    Object trace(String uri) throws IllegalArgumentException {
        // Set the uri
        this.uri = uri

        // Run the command
        return doTrace()
    }

    /**
     * Performs a TRACE request.
     *
     * @param closure
     * @throws IllegalArgumentException
     * @return
     */
    Object trace(Closure closure) throws IllegalArgumentException {
        // Run the provided closure
        run closure

        // Run the command
        return doTrace()
    }

    /**
     * Performs a TRACE request.
     *
     * @throws IllegalArgumentException
     * @return
     */
    private doTrace() throws IllegalArgumentException {
        if (uri == null) {
            throw new IllegalArgumentException('uri is required')
        }

        try {
            // Build the request
            WebResource.Builder request = buildRequest()

            // Do the request
            ClientResponse response = request.method('TRACE', ClientResponse)

            // Handle the response
            return handleResponse(response)
        }
        finally {
            cleanUp()
        }
    }

    /**
     * Performs a HEAD request.
     *
     * @throws IllegalArgumentException
     * @return
     */
    ClientResponse head() throws IllegalArgumentException {
        return doHead()
    }

    /**
     * Performs a HEAD request.
     *
     * @param uri Request URI
     * @throws IllegalArgumentException
     * @return
     */
    ClientResponse head(String uri) throws IllegalArgumentException {
        // Set the uri
        this.uri = uri

        // Run the command
        return doHead()
    }

    /**
     * Performs a HEAD request.
     *
     * @param closure
     * @throws IllegalArgumentException
     * @return
     */
    ClientResponse head(Closure closure) throws IllegalArgumentException {
        // Run the provided closure
        run closure

        // Run the command
        return doHead()
    }

    /**
     * Performs a HEAD request.
     *
     * @throws IllegalArgumentException
     * @return
     */
    private ClientResponse doHead() throws IllegalArgumentException {
        if (uri == null) {
            throw new IllegalArgumentException('uri is required')
        }

        try {
            // Build the request
            WebResource.Builder request = buildRequest()

            // Do the request
            return request.head()
        }
        finally {
            cleanUp()
        }
    }

    /**
     * Performs a PUT request.
     *
     * @throws IllegalArgumentException
     * @return
     */
    private doPut() throws IllegalArgumentException {
        if (uri == null) {
            throw new IllegalArgumentException('uri is required')
        }

        try {
            // Build the request
            WebResource.Builder request = buildRequest()

            // Do the request
            ClientResponse response = request.put(ClientResponse, getRequestBody())

            // Handle the response
            return handleResponse(response)
        }
        finally {
            cleanUp()
        }
    }

    /**
     * Performs a PUT request.
     *
     * @throws IllegalArgumentException
     * @return
     */
    Object put() throws IllegalArgumentException {
        return doPut()
    }

    /**
     * Performs a PUT request.
     *
     * @param uri Request URI
     * @throws IllegalArgumentException
     * @return
     */
    Object put(String uri, body = null) throws IllegalArgumentException {
        // Set the uri
        this.uri = uri

        // Set the request body
        this.body = body

        // Run the command
        return doPut()
    }

    /**
     * Performs a PUT request.
     *
     * @param closure
     * @throws IllegalArgumentException
     * @return
     */
    Object put(Closure closure) throws IllegalArgumentException {
        // Run the provided closure
        run closure

        // Run the command
        return doPut()
    }

    /**
     * Performs a POST request.
     *
     * @throws IllegalArgumentException
     * @return
     */
    private doPost() throws IllegalArgumentException {
        if (uri == null) {
            throw new IllegalArgumentException('uri is required')
        }

        try {
            // Build the request
            WebResource.Builder request = buildRequest()

            // Do the request
            ClientResponse response = request.post(ClientResponse, getRequestBody())

            // Handle the response
            return handleResponse(response)
        }
        finally {
            cleanUp()
        }
    }

    /**
     * Performs a POST request.
     *
     * @throws IllegalArgumentException
     * @return
     */
    Object post() throws IllegalArgumentException {
        return doPost()
    }

    /**
     * Performs a POST request.
     *
     * @param uri Request URI
     * @throws IllegalArgumentException
     * @return
     */
    Object post(String uri, body = null) throws IllegalArgumentException {
        // Set the uri
        this.uri = uri

        // Set the request body
        this.body = body

        // Run the command
        return doPost()
    }

    /**
     * Performs a POST request.
     *
     * @param closure
     * @throws IllegalArgumentException
     * @return
     */
    Object post(Closure closure) throws IllegalArgumentException {
        // Run the provided closure
        run closure

        // Run the command
        return doPost()
    }

    /**
     * Performs a DELETE request.
     *
     * @throws IllegalArgumentException
     * @return
     */
    private doDelete() throws IllegalArgumentException {
        if (uri == null) {
            throw new IllegalArgumentException('uri is required')
        }

        try {
            // Build the request
            WebResource.Builder request = buildRequest()

            // Do the request
            ClientResponse response = request.delete(ClientResponse)

            // Handle the response
            return handleResponse(response)
        }
        finally {
            cleanUp()
        }
    }

    /**
     * Performs a DELETE request.
     *
     * @throws IllegalArgumentException
     * @return
     */
    Object delete() throws IllegalArgumentException {
        if (uri == null) {
            throw new IllegalStateException("URI must not be null")
        }

        return doDelete()
    }

    /**
     * Performs a DELETE request.
     *
     * @param uri Request URI
     * @throws IllegalArgumentException
     * @return
     */
    Object delete(String uri) throws IllegalArgumentException {
        // Set the uri
        this.uri = uri

        // Run the command
        return doDelete()
    }

    /**
     * Performs a DELETE request.
     *
     * @param closure
     * @throws IllegalArgumentException
     * @return
     */
    Object delete(Closure closure) throws IllegalArgumentException {
        // Run the provided closure
        run closure

        // Run the command
        return doDelete()
    }

    /**
     * Checks for bad status codes, and does auto conversion of responses if necessary.
     *
     * @param response Response object to handle.
     * @return The content of the response.
     */
    private handleResponse(ClientResponse response) throws IllegalArgumentException {
        // Result holder
        def result

        // Check what kind of return we want
        if (rawClientResponse) {
            // Just return the raw ClientResponse object
            result = response
        }
        else if (binaryResponse) {
            // Return the byte array response
            result = response.getEntity(byte[])
        }
        else {
            // Get the content type
            MediaType contentType = response.getType()

            // Get the response entity as a string
            result = (String)response.getEntity(String)

            // Attempt to auto-conversions
            if (convertJson && MediaType.APPLICATION_JSON_TYPE.isCompatible(contentType)) {
                if (result.size() > 0 ) {
                    result = new JsonSlurper().parseText(result)
                }
                else {
                    result = null
                }
            }
            if (convertXML && (MediaType.APPLICATION_XML_TYPE.isCompatible(contentType) || MediaType.TEXT_XML_TYPE.isCompatible(contentType))) {
                if (result.size() > 0 ) {
                    result = new XmlSlurper().parseText(result)
                }
                else {
                    result = null
                }
            }
        }

        // Check the status
        if (!skipStatusCheck && Math.floor(response.status / 100) != 2) {
            String logString = new String(loggingBuffer.toByteArray())
            throw ResponseStatusException.build(
                response.status, result, response, logString)
        }

        return result
    }

    /**
     * Builds the request.
     *
     * @return Built request object.
     */
    private WebResource.Builder buildRequest() {
        // Do any body conversion here, so we can set other parameters as necessary
        convertBody()

        // Get the client
        Client client = getClient()

        // Create the byte array stream
        loggingBuffer = new ByteArrayOutputStream()

        // Set up the logging filter.
        //
        // If debug is enabled this will log to application logger.
        client.addFilter(new LoggingFilter(new PrintStream(loggingBuffer)))

        // If the uri is a string, run it through the UriBuilder
        if (uri instanceof String) {
            uri = UriBuilder.build {
                base = uri
            }
        }

        // Create the resource with the uri
        WebResource resource = client.resource(uri)

        // Set any query params
        query.each { param, value ->
            resource = resource.queryParam(param as String, value as String)
        }

        // Get the builder
        WebResource.Builder builder = resource.getRequestBuilder()

        // Set the accept type
        if (accept) {
            builder = builder.accept(accept)
        }

        // Set the content-type
        if (contentType) {
            builder = builder.type(contentType)
        }

        // Set cookies
        cookies.each { key, value ->
            // Cookie var
            Cookie cookie

            // If the value is a cookie, use its values
            // I'm making the choice to force the name to match
            if (value instanceof Cookie) {
                // Cast it
                value = value as Cookie

                // Create the new cookie
                cookie = new Cookie(key, value.value, value.path, value.domain, value.version)
            }
            else {
                cookie = new Cookie(key, value)
            }

            // Add it to the request
            builder.cookie(cookie)
        }

        // Set any headers
        headers.each { param, value ->
            builder = builder.header(param, value)
        }

        return builder
    }

    /**
     * Creates the jersey client instance.
     *
     * This method is required to handle ignoring SSL cert validation.
     * Code to implement this is based on information gathered from:
     *     * http://stackoverflow.com/questions/6047996/ignore-self-signed-ssl-cert-using-jersey-client
     *     * http://stackoverflow.com/questions/2145431/https-using-jersey-client
     *
     * @return Configured jersey client
     */
    private Client getClient() {
        // Client object
        Client client

        // Check whether we can just skip this altogether
        if (ignoreInvalidSSL) {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] certs = [new X509TrustManager() {
                X509Certificate[] getAcceptedIssuers() {}
                void checkClientTrusted(X509Certificate[] certs, String authType) { }
                void checkServerTrusted(X509Certificate[] certs, String authType) { }
            }]

            // SSL context
            SSLContext ctx = SSLContext.getInstance("TLS")
            ctx.init(null, certs, new SecureRandom())

            // Create the config
            ClientConfig config = new DefaultClientConfig()
            config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(
                new HostnameVerifier() {
                    boolean verify(String hostname, SSLSession session) {
                        return true
                    }
                },
                ctx
            ))

            // Create the client with the config
            client = Client.create(config)
        }
        else {
            client = Client.create()
        }

        // If basic auth enabled set user name and password
        if (useBasicAuth) {
            client.addFilter(new HTTPBasicAuthFilter(basicAuthUserName,basicAuthPassword))
        }

        // Set connection timeout
        if (connectionTimeout) {
            client.setConnectTimeout(connectionTimeout)
        }

        // Set read timeout
        if (readTimeout) {
            client.setReadTimeout(readTimeout)
        }

        // Whether to follow redirects
        client.setFollowRedirects(followRedirects)

        // Set the chunk size (and header)
        if (chunkSize != null) {
            headers['Transfer-Encoding'] = 'chunked'
            client.setChunkedEncodingSize(chunkSize)
        }

        // Set up the gzip filter (and header)
        if (encodeGzip) {
            headers['Content-Encoding'] = 'gzip'
            client.addFilter(new GZIPContentEncodingFilter())
        }

        return client
    }

    /**
     * Does any cleanup after the request is done.
     */
    private void cleanUp() {
        // Dump debug if requested
        if (debug) {
            String logString = "HTTP Conversation:\n" +
                "${formatLoggingOutput(new String(loggingBuffer.toByteArray()))}"
            log.debug(logString)
        }
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
     * Returns the body entity to send with the request.
     *
     * This function will check if values were entered for a POST form,
     * and if present build the form object and return it, ignoring
     * any body that was added.  If no form values are found, body is
     * returned.
     *
     * @return
     */
    private getRequestBody() {
        // If we have items in the form map, make a Form object and return it
        if (form.size() > 0) {
            Form f = new Form()
            form.each { key, value ->
                f.add(key, value)
            }

            return f
        }

        // Use the basic provided body, even if it's empty
        return body
    }

    /**
     * Does any conversion necessary on the body of the request.
     */
    private void convertBody() {
        // Don't do anything if the form is present
        if (form.size() > 0) {
            return
        }

        // Check if the body is a map or list
        if (body instanceof Map || body instanceof List) {
            // Convert the map/list
            body = new JsonBuilder(body).toString()

            // Set the content type of the request
            if (!contentType) {
                contentType = 'application/json'
            }
        }
    }

    /**
     * Formats the log output of the <code>LoggingFilter</code>
     * slightly.
     *
     * @param   logText     The log text from the
     *                      <code>OutputStream</code>
     *
     * @return  A formatted string of the original output but with
     *          more indentation.
     */
    private final String formatLoggingOutput(String logText) {
        logText = logText.readLines().collect { "    ${it}" }.join('\n')
    }
}
