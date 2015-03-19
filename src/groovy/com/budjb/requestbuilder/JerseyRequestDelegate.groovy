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

import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.ClientHandlerException
import com.sun.jersey.api.client.ClientResponse
import com.sun.jersey.api.client.WebResource
import com.sun.jersey.api.client.config.ClientConfig
import com.sun.jersey.api.client.config.DefaultClientConfig
import com.sun.jersey.api.client.filter.GZIPContentEncodingFilter
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter
import com.sun.jersey.api.client.filter.LoggingFilter
import com.sun.jersey.api.representation.Form
import com.sun.jersey.client.urlconnection.HTTPSProperties
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.apache.log4j.Logger

import javax.net.ssl.*
import javax.ws.rs.core.Cookie
import javax.ws.rs.core.MediaType
import java.security.SecureRandom
import java.security.cert.X509Certificate

/**
 * Created by bud on 2/10/15.
 */
class JerseyRequestDelegate {
    /**
     * Logger.
     */
    Logger log = Logger.getLogger(JerseyRequestDelegate)

    /**
     * Logging output stream
     */
    ByteArrayOutputStream loggingBuffer

    /**
     * Request properties.
     */
    RequestProperties requestProperties

    /**
     * Jersey client factory.
     */
    JerseyClientFactory jerseyClientFactory

    /**
     * Constructor.
     *
     * @param factory
     */
    JerseyRequestDelegate(JerseyClientFactory factory) {
        jerseyClientFactory = factory
    }

    /**
     * Does any cleanup after the request is done.
     */
    void cleanUp() {
        // Dump debug if requested
        if (requestProperties.debug) {
            String logString = "HTTP Conversation:\n" + "${formatLoggingOutput(new String(loggingBuffer.toByteArray()))}"
            log.debug(logString)
        }
    }

    /**
     * Formats the log output of the LoggingFilter slightly.
     *
     * @param logText The log text from the OutputStream.
     * @return A formatted string of the original output but with more indentation.
     */
    protected String formatLoggingOutput(String logText) {
        logText = logText.readLines().collect { "    ${it}" }.join('\n')
    }

    /**
     * Performs a DELETE request.
     *
     * @param requestProperties
     * @return
     * @throws IllegalArgumentException
     */
    Object delete(RequestProperties requestProperties) throws IllegalArgumentException {
        performRequest(requestProperties) { WebResource.Builder request -> request.delete(ClientResponse) }
    }

    /**
     * Performs a GET request.
     *
     * @param requestProperties
     * @return
     * @throws IllegalArgumentException
     */
    Object get(RequestProperties requestProperties) throws IllegalArgumentException {
        performRequest(requestProperties) { WebResource.Builder request -> request.get(ClientResponse) }
    }

    /**
     * Performs a HEAD request.
     *
     * @param requestProperties
     * @return
     * @throws IllegalArgumentException
     */
    ClientResponse head(RequestProperties requestProperties) throws IllegalArgumentException {
        setRequestProperties(requestProperties)

        this.requestProperties.rawClientResponse = true

        performRequest(this.requestProperties) { WebResource.Builder request -> request.head() }
    }

    /**
     * Performs an OPTIONS request.
     *
     * @param requestProperties
     * @return
     * @throws IllegalArgumentException
     */
    Object options(RequestProperties requestProperties) throws IllegalArgumentException {
        performRequest(requestProperties) { WebResource.Builder request -> request.options(ClientResponse) }
    }

    /**
     * Performs a POST request.
     *
     * @param requestProperties
     * @return
     * @throws IllegalArgumentException
     */
    Object post(RequestProperties requestProperties) throws IllegalArgumentException {
        performRequest(requestProperties) { WebResource.Builder request -> request.post(ClientResponse, this.requestProperties.body) }
    }

    /**
     * Performs a PUT request.
     *
     * @param requestProperties
     * @return
     * @throws IllegalArgumentException
     */
    Object put(RequestProperties requestProperties) throws IllegalArgumentException {
        performRequest(requestProperties) { WebResource.Builder request -> request.put(ClientResponse, this.requestProperties.body) }
    }

    /**
     * Performs a TRACE request.
     *
     * @param requestProperties
     * @return
     * @throws IllegalArgumentException
     */
    Object trace(RequestProperties requestProperties) throws IllegalArgumentException {
        performRequest(requestProperties) { WebResource.Builder request -> request.method('TRACE', ClientResponse) }
    }

    /**
     * Builds a jersey request object from the request parameters.
     *
     * @return A fully configured jersey web resource builder.
     */
    protected WebResource.Builder buildRequest() {
        // Do any body conversion here, so we can set other parameters as necessary
        marshallRequestBody()

        // Get the client
        Client client = createClient()

        // Apply basic authentication to the client if requested
        if (requestProperties.useBasicAuth) {
            client.addFilter(new HTTPBasicAuthFilter(requestProperties.basicAuthUserName, requestProperties.basicAuthPassword))
        }

        // Set connection timeout
        if (requestProperties.connectionTimeout) {
            client.setConnectTimeout(requestProperties.connectionTimeout)
        }

        // Set read timeout
        if (requestProperties.readTimeout) {
            client.setReadTimeout(requestProperties.readTimeout)
        }

        // Whether to follow redirects
        client.setFollowRedirects(requestProperties.followRedirects)

        // Set the chunk size (and header)
        if (requestProperties.chunkSize != null) {
            requestProperties.headers['Transfer-Encoding'] = 'chunked'
            client.setChunkedEncodingSize(requestProperties.chunkSize)
        }

        // Set up the gzip filter (and header)
        if (requestProperties.encodeGzip) {
            requestProperties.headers['Content-Encoding'] = 'gzip'
            client.addFilter(new GZIPContentEncodingFilter())
        }

        // Set up the logging filter. If debug is enabled this will log to application logger.
        if (!loggingBuffer) {
            loggingBuffer = new ByteArrayOutputStream()
        }
        client.addFilter(new LoggingFilter(new PrintStream(loggingBuffer)))

        // If the uri is a string, run it through the UriBuilder
        URI uri
        if (requestProperties.uri instanceof URI) {
            uri = (URI) requestProperties.uri
        }
        else {
            uri = requestProperties.uri = UriBuilder.build {
                base = requestProperties.uri
            }
        }

        // Create the resource with the uri
        WebResource resource = client.resource(uri)

        // Set any query params
        requestProperties.query.each { param, value ->
            if (value instanceof List) {
                (value as List).each {
                    resource = resource.queryParam(param as String, it as String)
                }
            }
            else {
                resource = resource.queryParam(param as String, value as String)
            }
        }

        // Get the builder
        WebResource.Builder builder = resource.getRequestBuilder()

        // Set the accept type
        if (requestProperties.accept) {
            builder.accept(requestProperties.accept)
        }

        // Set the content-type
        if (requestProperties.contentType) {
            builder.type(requestProperties.contentType)
        }

        // Set cookies
        requestProperties.cookies.each { key, value ->
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
        requestProperties.headers.each { param, value ->
            if (value instanceof List) {
                (value as List).each {
                    builder.header(param as String, it as String)
                }
            }
            else {
                builder.header(param as String, value as String)
            }
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
    protected Client createClient() {
        if (!requestProperties.ignoreInvalidSSL) {
            return jerseyClientFactory.createClient()
        }

        // Create a trust manager that does not validate certificate chains
        TrustManager[] certs = [new X509TrustManager() {
            X509Certificate[] getAcceptedIssuers() {}

            void checkClientTrusted(X509Certificate[] certs, String authType) {}

            void checkServerTrusted(X509Certificate[] certs, String authType) {}
        }]

        // Create a new TLS ssl context
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

        return jerseyClientFactory.createClient(config)
    }

    /**
     * Checks for bad status codes, and does auto conversion of responses if necessary.
     *
     * @param response Response object to handle.
     * @return The content of the response.
     */
    protected Object handleResponse(ClientResponse response) throws IllegalArgumentException {
        Object result

        if (requestProperties.rawClientResponse) {
            result = response
        }
        else if (!response.hasEntity()) {
            result = null
        }
        else if (requestProperties.binaryResponse) {
            result = response.getEntity(byte[])
        }
        else {
            result = marshallResponseBody((String) response.getEntity(String), response.getType())
        }

        if (!requestProperties.skipStatusCheck && Math.floor(response.status / 100) != 2) {
            String logString = new String(loggingBuffer.toByteArray())
            throw ResponseStatusException.build(response.status, result, response, logString)
        }

        return result
    }

    /**
     * Performs the request as executed from the provided closure.
     *
     * @param closure
     * @return
     */
    protected Object performRequest(RequestProperties properties, Closure closure) throws IllegalArgumentException {
        setRequestProperties(properties)

        try {
            WebResource.Builder request = buildRequest()

            ClientResponse response = closure(request)

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
     * Marshalls the body of the request into the correct form based on the
     * properties of the request.
     *
     * This function will check if values were entered for a POST form,
     * and if present build the form object and return it, ignoring
     * any body that was added.  If no form values are found, body is
     * either marshalled or used unaltered.
     *
     * @return
     */
    protected void marshallRequestBody() {
        if (requestProperties.form.size() > 0) {
            Form form = new Form()

            requestProperties.form.each { key, value ->
                if (value instanceof List) {
                    (value as List).each {
                        form.add(key as String, it as String)
                    }
                }
                else {
                    form.add(key as String, value as String)
                }
            }

            requestProperties.setBody(form)

            if (!requestProperties.contentType) {
                requestProperties.setContentType('application/x-www-form-urlencoded')
            }

            return
        }

        if (requestProperties.getBody() instanceof Map || requestProperties.getBody() instanceof List) {
            requestProperties.setBody(new JsonBuilder(requestProperties.getBody()).toString())

            if (!requestProperties.contentType) {
                requestProperties.setContentType('application/json')
            }

            return
        }
    }

    /**
     * Marshalls the response body from a string to a Map, List, or XML object.
     * Empty responses are returned as null.
     *
     * @param body
     * @param contentType
     * @return
     */
    protected Object marshallResponseBody(String body, MediaType contentType) {
        if (requestProperties.convertJson && MediaType.APPLICATION_JSON_TYPE.isCompatible(contentType)) {
            if (body.size() > 0) {
                return new JsonSlurper().parseText(body)
            }

            return null
        }

        if (requestProperties.convertXML && (MediaType.APPLICATION_XML_TYPE.isCompatible(contentType) || MediaType.TEXT_XML_TYPE.isCompatible(contentType))) {
            if (body.size() > 0) {
                return new XmlSlurper().parseText(body)
            }

            return null
        }

        return body
    }

    /**
     * Sets the request properties (after validating them).
     *
     * @param requestProperties
     * @throws IllegalArgumentException
     */
    void setRequestProperties(RequestProperties requestProperties) throws IllegalArgumentException {
        requestProperties.validate()
        this.requestProperties = requestProperties.clone()
    }
}
