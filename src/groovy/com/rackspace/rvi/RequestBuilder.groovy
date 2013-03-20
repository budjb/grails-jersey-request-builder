package com.rackspace.rvi

import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.ClientResponse
import com.sun.jersey.api.client.WebResource
import com.sun.jersey.api.client.WebResource.Builder
import com.sun.jersey.api.client.config.ClientConfig
import com.sun.jersey.api.client.config.DefaultClientConfig
import com.sun.jersey.api.representation.Form
import com.sun.jersey.client.urlconnection.HTTPSProperties

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

import java.security.SecureRandom
import java.security.cert.X509Certificate

class RequestBuilder {
    /**
     * The URI to hit.
     */
    String uri = null

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
    String contentType = null

    /**
     * Accept header.
     */
    String accept = null

    /**
     * Cookies to include with the request.
     */
    Map cookies = [:]

    /**
     * Whether to attempt to slurp JSON automatically.
     */
    boolean convertJson = true

    /**
     * Whether the caller expects a binary return.
     *
     * If true, the returned object will be a byte array.
     *
     * Note that this is a workaround to jersey not providing
     * automatic content conversion based on mime type.
     */
    boolean binaryResponse = false

    /**
     * If true, do not check the response status code, and
     * thus don't throw an exception for codes > 2xx.
     */
    boolean skipStatusCheck = false

    /**
     * If true, do not response handling, and return the raw response object.
     */
    boolean rawClientResponse = false

    /**
     * Body of the request - only useful on POST or PUT.
     */
    Object body = null

    /**
     * Whether to ignore SSL cert validation.
     */
    boolean ignoreInvalidSSL = false

    /**
     * Backed up SSL socket factory
     */
    private SSLSocketFactory _sslSocketBackup = null

    /**
     * Performs a GET request.
     *
     * @return
     */
    private Object doGet() {
        try {
            // Build the request
            WebResource.Builder request = buildRequest()

            // Do the request
            ClientResponse response = request.get(ClientResponse)

            // Handle the response
            return handleResponse(response)
        }
        finally {
            cleanUp()
        }
    }

    /**
     * Performs a GET request
     *
     * @param uri Request URI
     * @return
     */
    Object get(String uri) {
        // Set the uri
        this.uri = uri

        // Run the command
        return doGet()
    }

    /**
     * Performs a GET request.
     *
     * @param closure
     * @return
     */
    Object get(Closure closure) {
        // Run the provided closure
        run closure

        // Run the command
        return doGet()
    }

    /**
     * Performs a PUT request.
     *
     * @return
     */
    private Object doPut() {
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
     * @param uri Request URI
     * @return
     */
    Object put(String uri, Object body = null) {
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
     * @return
     */
    Object put(Closure closure) {
        // Run the provided closure
        run closure

        // Run the command
        return doPut()
    }

    /**
     * Performs a POST request.
     *
     * @return
     */
    private Object doPost() {
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
     * @param uri Request URI
     * @return
     */
    Object post(String uri, Object body = null) {
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
     * @return
     */
    Object post(Closure closure) {
        // Run the provided closure
        run closure

        // Run the command
        return doPost()
    }

    /**
     * Performs a DELETE request.
     *
     * @return
     */
    private Object doDelete() {
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
     * @param uri Request URI
     * @return
     */
    Object delete(String uri) {
        // Set the uri
        this.uri = uri

        // Run the command
        return doDelete()
    }

    /**
     * Performs a DELETE request.
     *
     * @param closure
     * @return
     */
    Object delete(Closure closure) {
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
    private Object handleResponse(ClientResponse response) {
        // Result holder
        def result = null

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
            result = response.getEntity(String)

            // Attempt to auto-convert JSON if enabled
            if (convertJson && MediaType.APPLICATION_JSON_TYPE.isCompatible(contentType)) {
                result = new JsonSlurper().parseText(result)
            }
        }

        // Check the status
        if (!skipStatusCheck && Math.floor(response.status / 100) != 2) {
            throw ResponseStatusException.build(response.status, result)
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

        // Create the resource with the uri
        WebResource resource = client.resource(uri)

        // Set any query params
        query.each { param, value ->
            resource = resource.queryParam(param, value)
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
     * Code to implement this was gathered from:
     *     * http://stackoverflow.com/questions/6047996/ignore-self-signed-ssl-cert-using-jersey-client
     *     * http://stackoverflow.com/questions/2145431/https-using-jersey-client
     *
     * @return Configured jersey client
     */
    private Client getClient() {
        // Check whether we can just skip this altogether
        if (!ignoreInvalidSSL) {
            return Client.create()
        }

        // Back up the existing trust settings
        _sslSocketBackup = HttpsURLConnection.getDefaultSSLSocketFactory()

        // Create a trust manager that does not validate certificate chains
        TrustManager[] certs = [new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() { return null }
            public void checkClientTrusted(X509Certificate[] certs, String authType) { }
            public void checkServerTrusted(X509Certificate[] certs, String authType) { }
        }]

        // SSL context
        SSLContext ctx = null
        try {
            ctx = SSLContext.getInstance("TLS")
            ctx.init(null, certs, new SecureRandom())
        }
        catch (java.security.GeneralSecurityException ex) { }

        // Set the default socket factory
        HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory())

        // Create the config
        ClientConfig config = new DefaultClientConfig()
        try {
            config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(
                new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        return true
                    }
                },
                ctx
            ))
        } catch(Exception e) { }

        return Client.create(config)
    }

    /**
     * Does any environment cleanup after the request is done.
     */
    private void cleanUp() {
        if (ignoreInvalidSSL) {
            HttpsURLConnection.setDefaultSSLSocketFactory(_sslSocketBackup)
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
    private Object getRequestBody() {
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
}
