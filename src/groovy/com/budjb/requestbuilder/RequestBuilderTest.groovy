package com.budjb.requestbuilder

abstract class RequestBuilderTest extends GroovyTestCase {
    /**
     * Returns the base URL of the running server.
     *
     * @return
     */
    protected String getBaseUrl() {
        // Get the base URL property
        String baseUrl = System.getProperty('grails.functional.test.baseURL')

        // Strip trailing slash
        if (baseUrl[-1] == '/') {
            baseUrl = baseUrl[0..-2]
        }

        return baseUrl
    }

    /**
     * Convenience method to return the constructed URL to hit on the internal server.
     *
     * @param path
     * @return
     */
    protected String getUri(String path) {
        if (!path) {
            return getBaseUrl()
        }
        if (path[0] == '/') {
            path = path[1..-1]
        }
        return "${getBaseUrl()}/${path}"
    }
}
