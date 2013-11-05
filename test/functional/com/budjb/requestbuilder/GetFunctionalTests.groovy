package com.budjb.requestbuilder

import com.grailsrocks.functionaltest.*
import com.budjb.requestbuilder.RequestBuilder
import com.budjb.requestbuilder.RequestBuilderTest

class GetFunctionalTests extends RequestBuilderTest {
    /**
     * Test simple usage of the GET method.
     */
    void testSimple() {
        def result = new RequestBuilder().get("${getBaseUrl()}/test/testBasicGet")
        assert result == 'The quick brown fox jumps over the lazy dog.'
    }

    /**
     * Test builder usage of the GET method.
     */
    void testBuilder() {
        RequestBuilder builder = new RequestBuilder()
        builder.uri = "${getBaseUrl()}/test/testBasicGet"
        def result = builder.get()
        assert result == 'The quick brown fox jumps over the lazy dog.'
    }

    /**
     * Test DSL usage of the GET method.
     */
    void testDSL() {
        def result = new RequestBuilder().get {
            uri = "${getBaseUrl()}/test/testBasicGet"
        }
        assert result == 'The quick brown fox jumps over the lazy dog.'
    }
}
