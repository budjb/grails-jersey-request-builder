package com.budjb.requestbuilder

import com.sun.jersey.api.client.ClientResponse
import org.codehaus.groovy.grails.commons.GrailsApplication

class JerseyRequestBuilderImpl implements JerseyRequestBuilder {
    /**
     * Jersey client factory bean.
     */
    JerseyClientFactory jerseyClientFactory

    /**
     * Grails application bean.
     */
    GrailsApplication grailsApplication

    /**
     * Performs a DELETE request to the given URI.
     *
     * @param uri
     * @return
     */
    Object delete(String uri) {
        return delete(new RequestProperties(grailsApplication).build { delegate.uri = uri })
    }

    /**
     * Performs a DELETE request with the request properties configured in the given closure.
     *
     * @param closure
     * @return
     */
    Object delete(Closure closure) {
        return delete(new RequestProperties(grailsApplication).build(closure))
    }

    /**
     * Performs a DELETE request with the given request properties.
     *
     * @param requestProperties
     * @return
     */
    Object delete(RequestProperties requestProperties) {
        return jerseyClientFactory.createDelegate().delete(requestProperties)
    }

    /**
     * Performs a GET request to the given URI.
     *
     * @param uri
     * @return
     */
    Object get(String uri) {
        return get(new RequestProperties(grailsApplication).build { delegate.uri = uri })
    }

    /**
     * Performs a GET request with the request properties configured in the given closure.
     *
     * @param closure
     * @return
     */
    Object get(Closure closure) {
        return get(new RequestProperties(grailsApplication).build(closure))
    }

    /**
     * Performs a GET request with the given request properties.
     *
     * @param requestProperties
     * @return
     */
    Object get(RequestProperties requestProperties) {
        return jerseyClientFactory.createDelegate().get(requestProperties)
    }

    /**
     * Performs a HEAD request to the given URI.
     *
     * @param uri
     * @return
     */
    ClientResponse head(String uri) {
        return head(new RequestProperties(grailsApplication).build { delegate.uri = uri })
    }

    /**
     * Performs a HEAD request with the request properties configured in the given closure.
     *
     * @param closure
     * @return
     */
    ClientResponse head(Closure closure) {
        return head(new RequestProperties(grailsApplication).build(closure))
    }

    /**
     * Performs a HEAD request with the given request properties.
     *
     * @param requestProperties
     * @return
     */
    ClientResponse head(RequestProperties requestProperties) {
        return jerseyClientFactory.createDelegate().head(requestProperties)
    }

    /**
     * Performs an OPTIONS request to the given URI.
     *
     * @param uri
     * @return
     */
    Object options(String uri) {
        return options(new RequestProperties(grailsApplication).build { delegate.uri = uri })
    }

    /**
     * Performs an OPTIONS request with the request properties configured in the given closure.
     *
     * @param closure
     * @return
     */
    Object options(Closure closure) {
        return options(new RequestProperties(grailsApplication).build(closure))
    }

    /**
     * Performs an OPTIONS request with the given request properties.
     *
     * @param requestProperties
     * @return
     */
    Object options(RequestProperties requestProperties) {
        return jerseyClientFactory.createDelegate().options(requestProperties)
    }

    /**
     * Performs a POST request to the given URI.
     *
     * @param uri
     * @return
     */
    Object post(String uri) {
        return post(new RequestProperties(grailsApplication).build { delegate.uri = uri })
    }

    /**
     * Performs a POST request to the given URI.
     *
     * @param uri
     * @param body
     * @return
     */
    Object post(String uri, Object body) {
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
    Object post(Closure closure) {
        return post(new RequestProperties(grailsApplication).build(closure))
    }

    /**
     * Performs a POST request with the given request properties.
     *
     * @param requestProperties
     * @return
     */
    Object post(RequestProperties requestProperties) {
        return jerseyClientFactory.createDelegate().post(requestProperties)
    }

    /**
     * Performs a PUT request to the given URI.
     *
     * @param uri
     * @return
     */
    Object put(String uri) {
        return put(new RequestProperties(grailsApplication).build { delegate.uri = uri })
    }

    /**
     * Performs a PUT request to the given URI.
     *
     * @param uri
     * @param body
     * @return
     */
    Object put(String uri, Object body) {
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
    Object put(Closure closure) {
        return put(new RequestProperties(grailsApplication).build(closure))
    }

    /**
     * Performs a PUT request with the given request properties.
     *
     * @param requestProperties
     * @return
     */
    Object put(RequestProperties requestProperties) {
        return jerseyClientFactory.createDelegate().put(requestProperties)
    }

    /**
     * Performs a TRACE request to the given URI.
     *
     * @param uri
     * @return
     */
    Object trace(String uri) {
        return trace(new RequestProperties(grailsApplication).build { delegate.uri = uri })
    }

    /**
     * Performs a TRACE request with the request properties configured in the given closure.
     *
     * @param closure
     * @return
     */
    Object trace(Closure closure) {
        return trace(new RequestProperties(grailsApplication).build(closure))
    }

    /**
     * Performs a TRACE request with the given request properties.
     *
     * @param requestProperties
     * @return
     */
    Object trace(RequestProperties requestProperties) {
        return jerseyClientFactory.createDelegate().trace(requestProperties)
    }
}
