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
}
