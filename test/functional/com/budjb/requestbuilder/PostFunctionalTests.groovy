package com.budjb.requestbuilder

import com.grailsrocks.functionaltest.*
import com.budjb.requestbuilder.RequestBuilder
import com.budjb.requestbuilder.RequestBuilderTest

class PostFunctionalTests extends RequestBuilderTest {
    /**
     * Test simple usage of the POST method.
     */
    void testSimple() {
        def result = new RequestBuilder().post("${getBaseUrl()}/test/testBasicPost", "Please don't play the repeating game!")
        assert result == "Please don't play the repeating game!"
    }

    /**
     * Test builder usage of the POST method.
     */
    void testBuilder() {
        RequestBuilder builder = new RequestBuilder()
        builder.uri = "${getBaseUrl()}/test/testBasicPost"
        builder.body = "Please don't play the repeating game!"
        def result = builder.post()
        assert result == "Please don't play the repeating game!"
    }

    /**
     * Test DSL usage of the POST method.
     */
    void testDSL() {
        def result = new RequestBuilder().post {
            uri = "${getBaseUrl()}/test/testBasicPost"
            body = "Please don't play the repeating game!"
        }
        assert result == "Please don't play the repeating game!"
    }
}
