package com.budjb.requestbuilder

import com.sun.jersey.api.client.ClientResponse
import org.codehaus.groovy.grails.commons.GrailsApplication

class JerseyRequestBuilder {
    /**
     * Jersey client factory bean.
     */
    protected JerseyClientFactory jerseyClientFactory

    /**
     * Grails application bean.
     */
    protected GrailsApplication grailsApplication

    /**
     * Performs a DELETE request to the given URI.
     *
     * @param uri
     * @return
     */
    public Object delete(String uri) {
        return delete(new RequestProperties(grailsApplication).build { delegate.uri = uri })
    }

    /**
     * Performs a DELETE request with the request properties configured in the given closure.
     *
     * @param closure
     * @return
     */
    public Object delete(Closure closure) {
        return delete(new RequestProperties(grailsApplication).build(closure))
    }

    /**
     * Performs a DELETE request with the given request properties.
     *
     * @param requestProperties
     * @return
     */
    public Object delete(RequestProperties requestProperties) {
        return createDelegate().delete(requestProperties)
    }

    /**
     * Performs a GET request to the given URI.
     *
     * @param uri
     * @return
     */
    public Object get(String uri) {
        return get(new RequestProperties(grailsApplication).build { delegate.uri = uri })
    }

    /**
     * Performs a GET request with the request properties configured in the given closure.
     *
     * @param closure
     * @return
     */
    public Object get(Closure closure) {
        return get(new RequestProperties(grailsApplication).build(closure))
    }

    /**
     * Performs a GET request with the given request properties.
     *
     * @param requestProperties
     * @return
     */
    public Object get(RequestProperties requestProperties) {
        return createDelegate().get(requestProperties)
    }

    /**
     * Performs a HEAD request to the given URI.
     *
     * @param uri
     * @return
     */
    public ClientResponse head(String uri) {
        return head(new RequestProperties(grailsApplication).build { delegate.uri = uri })
    }

    /**
     * Performs a HEAD request with the request properties configured in the given closure.
     *
     * @param closure
     * @return
     */
    public ClientResponse head(Closure closure) {
        return head(new RequestProperties(grailsApplication).build(closure))
    }

    /**
     * Performs a HEAD request with the given request properties.
     *
     * @param requestProperties
     * @return
     */
    public ClientResponse head(RequestProperties requestProperties) {
        return createDelegate().head(requestProperties)
    }

    /**
     * Performs an OPTIONS request to the given URI.
     *
     * @param uri
     * @return
     */
    public Object options(String uri) {
        return options(new RequestProperties(grailsApplication).build { delegate.uri = uri })
    }

    /**
     * Performs an OPTIONS request with the request properties configured in the given closure.
     *
     * @param closure
     * @return
     */
    public Object options(Closure closure) {
        return options(new RequestProperties(grailsApplication).build(closure))
    }

    /**
     * Performs an OPTIONS request with the given request properties.
     *
     * @param requestProperties
     * @return
     */
    public Object options(RequestProperties requestProperties) {
        return createDelegate().options(requestProperties)
    }

    /**
     * Performs a POST request to the given URI.
     *
     * @param uri
     * @return
     */
    public Object post(String uri) {
        return post(new RequestProperties(grailsApplication).build { delegate.uri = uri })
    }

    /**
     * Performs a POST request to the given URI.
     *
     * @param uri
     * @param body
     * @return
     */
    public Object post(String uri, Object body) {
        return post(new RequestProperties(grailsApplication).build {
            delegate.uri = uri
            delegate.body = body
        })
    }

    /**
     * Performs a POST request with the request properties configured in the given closure.
     *
     * @param closure
     * @return
     */
    public Object post(Closure closure) {
        return post(new RequestProperties(grailsApplication).build(closure))
    }

    /**
     * Performs a POST request with the given request properties.
     *
     * @param requestProperties
     * @return
     */
    public Object post(RequestProperties requestProperties) {
        return createDelegate().post(requestProperties)
    }

    /**
     * Performs a PUT request to the given URI.
     *
     * @param uri
     * @return
     */
    public Object put(String uri) {
        return put(new RequestProperties(grailsApplication).build { delegate.uri = uri })
    }

    /**
     * Performs a PUT request to the given URI.
     *
     * @param uri
     * @param body
     * @return
     */
    public Object put(String uri, Object body) {
        return put(new RequestProperties(grailsApplication).build {
            delegate.uri = uri
            delegate.body = body
        })
    }

    /**
     * Performs a PUT request with the request properties configured in the given closure.
     *
     * @param closure
     * @return
     */
    public Object put(Closure closure) {
        return put(new RequestProperties(grailsApplication).build(closure))
    }

    /**
     * Performs a PUT request with the given request properties.
     *
     * @param requestProperties
     * @return
     */
    public Object put(RequestProperties requestProperties) {
        return createDelegate().put(requestProperties)
    }

    /**
     * Performs a TRACE request to the given URI.
     *
     * @param uri
     * @return
     */
    public Object trace(String uri) {
        return trace(new RequestProperties(grailsApplication).build { delegate.uri = uri })
    }

    /**
     * Performs a TRACE request with the request properties configured in the given closure.
     *
     * @param closure
     * @return
     */
    public Object trace(Closure closure) {
        return trace(new RequestProperties(grailsApplication).build(closure))
    }

    /**
     * Performs a TRACE request with the given request properties.
     *
     * @param requestProperties
     * @return
     */
    public Object trace(RequestProperties requestProperties) {
        return createDelegate().trace(requestProperties)
    }

    /**
     * Creates a jersey client delegate.
     *
     * @return
     */
    protected JerseyRequestDelegate createDelegate() {
        JerseyRequestDelegate delegate = new JerseyRequestDelegate()
        delegate.setJerseyClientFactory(jerseyClientFactory)
        return delegate
    }

    /**
     * Sets the jersey client factory bean.
     *
     * @param jerseyClientFactory
     * @return
     */
    protected setJerseyClientFactory(JerseyClientFactory jerseyClientFactory) {
        this.jerseyClientFactory = jerseyClientFactory
    }

    /**
     * Sets the grails application bean.
     *
     * @param grailsApplication
     * @return
     */
    protected setGrailsApplication(GrailsApplication grailsApplication) {
        this.grailsApplication = grailsApplication
    }
}
