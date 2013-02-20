package com.rackspace.rvi

import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.ClientResponse
import com.sun.jersey.api.client.WebResource
import com.sun.jersey.api.client.WebResource.Builder
import com.sun.jersey.api.representation.Form
import javax.ws.rs.core.MediaType
import groovy.json.JsonSlurper

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
     * Accept header.
     */
    String accept = null
    
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
     * Body of the request - only useful on POST or PUT.
     */
    Object body = null
    
    /**
     * Performs a GET request.
     *
     * @return
     */
    private Object doGet() {
        // Build the request
        WebResource.Builder request = buildRequest()
        
        // Do the request
        ClientResponse response = request.get(ClientResponse)

        // Handle the response
        return handleResponse(response)
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
        // Build the request
        WebResource.Builder request = buildRequest()
        
        // Do the request
        ClientResponse response = request.put(ClientResponse, getRequestBody())

        // Handle the response
        return handleResponse(response)
    }
    
    /**
     * Performs a PUT request.
     *
     * @param uri Request URI
     * @return
     */
    Object put(String uri) {
        // Set the uri
        this.uri = uri
        
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
        // Build the request
        WebResource.Builder request = buildRequest()
        
        // Do the request
        ClientResponse response = request.post(ClientResponse, getRequestBody())

        // Handle the response
        return handleResponse(response)
    }
    
    /**
     * Performs a POST request.
     *
     * @param uri Request URI
     * @return
     */
    Object post(String uri) {
        // Set the uri
        this.uri = uri
        
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
        // Build the request
        WebResource.Builder request = buildRequest()
        
        // Do the request
        ClientResponse response = request.delete(ClientResponse)

        // Handle the response
        return handleResponse(response)
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
        def result = null
        
        // Check if the caller wants the raw output
        if (binaryResponse) {
            result = response.getEntity(byte[])
        }
        else {
            // Get the content type
            MediaType contentType = response.getType()
    
            // Get the response entity
            result = response.getEntity(String)
            
            // Attempt to auto-convert JSON if enabled
            if (convertJson && MediaType.APPLICATION_JSON_TYPE.isCompatible(contentType)) {
                result = new JsonSlurper().parseText(result)
            }
        }
        
        // Check the status
        if (Math.floor(response.status / 100) != 2) {
            throw new ResponseStatusException(response.status, result)
        }
        
        return result
    }
    
    /**
     * Builds the request.
     *
     * @return Built request object.
     */
    private WebResource.Builder buildRequest() {
        // Create the client with the uri
        WebResource resource = Client.create().resource(uri)
        
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
        
        // Set any headers
        headers.each { param, value ->
            builder = builder.header(param, value)
        }
        
        return builder
    }
    
    /**
     * Runs a passed closure to implement builder-style operation.
     *
     * @param closure
     */
    private void run(Closure closure) {
        Closure clone = closure.clone()
        clone.delegate = this
        clone.resolveStrategy = Closure.DELEGATE_ONLY
        clone()
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
        if (form.size()) {
            Form f = new Form()
            form.each { key, value ->
                f.add(key, value)
            }

            return f
        }
        
        // Use the basic provided body, even if it's empty
        return body
    }
}

/**
 * Exception thrown when a non-200 status is returned from the web service.
 */
class ResponseStatusException extends Exception {
    int status
    Object content
    
    public ResponseStatusException(int status, Object content) {
        super(content as String)
        
        this.status = status
        this.content = content
    }
}
