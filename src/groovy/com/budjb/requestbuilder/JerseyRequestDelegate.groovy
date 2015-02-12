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
    protected Logger log = Logger.getLogger(JerseyRequestDelegate)

    /**
     * Logging output stream
     */
    protected ByteArrayOutputStream loggingBuffer

    /**
     * Request properties.
     */
    protected RequestProperties requestProperties

    /**
     * Jersey client factory.
     */
    protected JerseyClientFactory jerseyClientFactory

    /**
     * Sets the jersey client factory bean.
     *
     * @param jerseyClientFactory
     */
    public void setJerseyClientFactory(JerseyClientFactory jerseyClientFactory) {
        this.jerseyClientFactory = jerseyClientFactory
    }

    /**
     * Sets the logging output stream.
     *
     * @param loggingBuffer
     */
    public void setLoggingBuffer(ByteArrayOutputStream loggingBuffer) {
        this.loggingBuffer = loggingBuffer
    }

    /**
     * Sets the request properties.
     *
     * @param requestProperties
     */
    public void setRequestProperties(RequestProperties requestProperties) {
        this.requestProperties = requestProperties.clone()
    }

    /**
     * Does any cleanup after the request is done.
     */
    protected void cleanUp() {
        // Dump debug if requested
        if (requestProperties.debug) {
            String logString = "HTTP Conversation:\n" + "${formatLoggingOutput(new String(loggingBuffer.toByteArray()))}"
            log.debug(logString)
        }
    }

    /**
     * Formats the log output of the <code>LoggingFilter</code>
     * slightly.
     *
     * @param logText The log text from the
     *                      <code>OutputStream</code>
     *
     * @return A formatted string of the original output but with
     *          more indentation.
     */
    protected final String formatLoggingOutput(String logText) {
        logText = logText.readLines().collect { "    ${it}" }.join('\n')
    }

    /**
     * Performs a DELETE request.
     *
     * @param requestProperties
     * @return
     * @throws IllegalArgumentException
     */
    public Object delete(RequestProperties requestProperties) throws IllegalArgumentException {
        performRequest(requestProperties) { WebResource.Builder request -> request.delete(ClientResponse) }
    }

    /**
     * Performs a GET request.
     *
     * @param requestProperties
     * @return
     * @throws IllegalArgumentException
     */
    public Object get(RequestProperties requestProperties) throws IllegalArgumentException {
        performRequest(requestProperties) { WebResource.Builder request -> request.get(ClientResponse) }
    }

    /**
     * Performs a HEAD request.
     *
     * @param requestProperties
     * @return
     * @throws IllegalArgumentException
     */
    public ClientResponse head(RequestProperties requestProperties) throws IllegalArgumentException {
        requestProperties = requestProperties.clone()
        requestProperties.rawClientResponse = true
        performRequest(requestProperties) { WebResource.Builder request -> request.head() }
    }

    /**
     * Performs an OPTIONS request.
     *
     * @param requestProperties
     * @return
     * @throws IllegalArgumentException
     */
    public Object options(RequestProperties requestProperties) throws IllegalArgumentException {
        performRequest(requestProperties) { WebResource.Builder request -> request.options(ClientResponse) }
    }

    /**
     * Performs a POST request.
     *
     * @param requestProperties
     * @return
     * @throws IllegalArgumentException
     */
    public Object post(RequestProperties requestProperties) throws IllegalArgumentException {
        performRequest(requestProperties) { WebResource.Builder request -> request.post(ClientResponse, getRequestBody()) }
    }

    /**
     * Performs a PUT request.
     *
     * @param requestProperties
     * @return
     * @throws IllegalArgumentException
     */
    public Object put(RequestProperties requestProperties) throws IllegalArgumentException {
        performRequest(requestProperties) { WebResource.Builder request -> request.put(ClientResponse, getRequestBody()) }
    }

    /**
     * Performs a TRACE request.
     *
     * @param requestProperties
     * @return
     * @throws IllegalArgumentException
     */
    public Object trace(RequestProperties requestProperties) throws IllegalArgumentException {
        performRequest(requestProperties) { WebResource.Builder request -> request.method('TRACE', ClientResponse) }
    }

    /**
     * Builds a jersey request object from the request parameters.
     *
     * @return A fully configured jersey web resource builder.
     */
    protected WebResource.Builder buildRequest() {
        // Do any body conversion here, so we can set other parameters as necessary
        prepareRequestBody()

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
            uri = requestProperties.uri
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
            resource = resource.queryParam(param as String, value as String)
        }

        // Get the builder
        WebResource.Builder builder = resource.getRequestBuilder()

        // Set the accept type
        if (requestProperties.accept) {
            builder = builder.accept(requestProperties.accept)
        }

        // Set the content-type
        if (requestProperties.contentType) {
            builder = builder.type(requestProperties.contentType)
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
            builder = builder.header(param, value)
        }

        return builder
    }

    /**
     * Does any conversion necessary on the body of the request.
     */
    protected void prepareRequestBody() {
        // Don't do anything if the form is present
        if (requestProperties.form.size() > 0) {
            return
        }

        // Check if the body is a map or list
        if (requestProperties.body instanceof Map || requestProperties.body instanceof List) {
            // Convert the map/list
            requestProperties.body = new JsonBuilder(requestProperties.body).toString()

            // Set the content type of the request
            if (!requestProperties.contentType) {
                requestProperties.contentType = 'application/json'
            }
        }
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
        // If SSL requests should go through the trust chain, just create a default client
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
    protected handleResponse(ClientResponse response) throws IllegalArgumentException {
        // Result holder
        def result

        // Check what kind of return we want
        if (requestProperties.rawClientResponse) {
            // Just return the raw ClientResponse object
            result = response
        }
        else if (!response.hasEntity()) {
            result = null
        }
        else if (requestProperties.binaryResponse) {
            // Return the byte array response
            result = response.getEntity(byte[])
        }
        else {
            // Get the content type
            MediaType contentType = response.getType()

            // Get the response entity as a string
            result = (String)response.getEntity(String)

            // Attempt to auto-conversions
            if (requestProperties.convertJson && MediaType.APPLICATION_JSON_TYPE.isCompatible(contentType)) {
                if (result.size() > 0) {
                    result = new JsonSlurper().parseText(result)
                }
                else {
                    result = null
                }
            }
            if (requestProperties.convertXML && (MediaType.APPLICATION_XML_TYPE.isCompatible(contentType) || MediaType.TEXT_XML_TYPE.isCompatible(contentType))) {
                if (result.size() > 0) {
                    result = new XmlSlurper().parseText(result)
                }
                else {
                    result = null
                }
            }
        }

        // Check the status
        if (!requestProperties.skipStatusCheck && Math.floor(response.status / 100) != 2) {
            String logString = new String(loggingBuffer.toByteArray())
            throw ResponseStatusException.build(
                response.status, result, response, logString)
        }

        return result
    }

    /**
     * Performs the request as executed from the provided closure.
     *
     * @param closure
     * @return
     */
    protected Object performRequest(RequestProperties properties, Closure closure) {
        // Store the request properties
        setRequestProperties(properties)

        // Validate the request
        requestProperties.validate()

        try {
            // Build the request
            WebResource.Builder request = buildRequest()

            // Do the request
            ClientResponse response = closure(request)

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
        if (requestProperties.form.size() > 0) {
            Form f = new Form()
            requestProperties.form.each { key, value ->
                f.add(key, value)
            }

            return f
        }

        // Use the basic provided body, even if it's empty
        return requestProperties.body
    }
}
