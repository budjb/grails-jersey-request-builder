package com.budjb.requestbuilder

import com.grailsrocks.functionaltest.*
import com.budjb.requestbuilder.RequestBuilder
import com.budjb.requestbuilder.RequestBuilderTest

class DeleteFunctionalTests extends RequestBuilderTest {
    /**
     * Test simple usage of the DELETE method.
     */
    void testSimple() {
        def result = new RequestBuilder().delete("${getBaseUrl()}/test/testBasicDelete")
        assert result == "Please don't hurt me!"
    }

    /**
     * Test builder usage of the DELETE method.
     */
    void testBuilder() {
        RequestBuilder builder = new RequestBuilder()
        builder.uri = "${getBaseUrl()}/test/testBasicDelete"
        def result = builder.delete()
        assert result == "Please don't hurt me!"
    }

    /**
     * Test DSL usage of the DELETE method.
     */
    void testDSL() {
        def result = new RequestBuilder().delete {
            uri = "${getBaseUrl()}/test/testBasicDelete"
        }
        assert result == "Please don't hurt me!"
    }
}
