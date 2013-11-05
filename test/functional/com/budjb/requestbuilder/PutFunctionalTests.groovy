package com.budjb.requestbuilder

import com.grailsrocks.functionaltest.*
import com.budjb.requestbuilder.RequestBuilder
import com.budjb.requestbuilder.RequestBuilderTest

class PutFunctionalTests extends RequestBuilderTest {
    /**
     * Test simple usage of the PUT method.
     */
    void testSimple() {
        def result = new RequestBuilder().put("${getBaseUrl()}/test/testBasicPut", "Please don't play the repeating game!")
        assert result == "Please don't play the repeating game!"
    }

    /**
     * Test builder usage of the PUT method.
     */
    void testBuilder() {
        RequestBuilder builder = new RequestBuilder()
        builder.uri = "${getBaseUrl()}/test/testBasicPut"
        builder.body = "Please don't play the repeating game!"
        def result = builder.put()
        assert result == "Please don't play the repeating game!"
    }

    /**
     * Test DSL usage of the PUT method.
     */
    void testDSL() {
        def result = new RequestBuilder().put {
            uri = "${getBaseUrl()}/test/testBasicPut"
            body = "Please don't play the repeating game!"
        }
        assert result == "Please don't play the repeating game!"
    }
}
