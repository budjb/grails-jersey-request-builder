package com.budjb.requestbuilder

import com.budjb.requestbuilder.RequestBuilder
import com.budjb.requestbuilder.RequestBuilderTest

class BasicFunctionalTests extends RequestBuilderTest {
    /**
     * Test simple usage of the GET method.
     */
    void testGetSimple() {
        def result = new RequestBuilder().get("${getBaseUrl()}/test/testBasicGet")
        assert result == 'The quick brown fox jumps over the lazy dog.'
    }

    /**
     * Test builder usage of the GET method.
     */
    void testGetBuilder() {
        RequestBuilder builder = new RequestBuilder()
        builder.uri = "${getBaseUrl()}/test/testBasicGet"
        def result = builder.get()
        assert result == 'The quick brown fox jumps over the lazy dog.'
    }

    /**
     * Test DSL usage of the GET method.
     */
    void testGetDSL() {
        def result = new RequestBuilder().get {
            uri = "${getBaseUrl()}/test/testBasicGet"
        }
        assert result == 'The quick brown fox jumps over the lazy dog.'
    }

    /**
     * Test simple usage of the DELETE method.
     */
    void testDeleteSimple() {
        def result = new RequestBuilder().delete("${getBaseUrl()}/test/testBasicDelete")
        assert result == "Please don't hurt me!"
    }

    /**
     * Test builder usage of the DELETE method.
     */
    void testDeleteBuilder() {
        RequestBuilder builder = new RequestBuilder()
        builder.uri = "${getBaseUrl()}/test/testBasicDelete"
        def result = builder.delete()
        assert result == "Please don't hurt me!"
    }

    /**
     * Test DSL usage of the DELETE method.
     */
    void testDeleteDSL() {
        def result = new RequestBuilder().delete {
            uri = "${getBaseUrl()}/test/testBasicDelete"
        }
        assert result == "Please don't hurt me!"
    }

    /**
     * Test simple usage of the POST method.
     */
    void testPostSimple() {
        def result = new RequestBuilder().post("${getBaseUrl()}/test/testBasicPost", "Please don't play the repeating game!")
        assert result == "Please don't play the repeating game!"
    }

    /**
     * Test builder usage of the POST method.
     */
    void testPostBuilder() {
        RequestBuilder builder = new RequestBuilder()
        builder.uri = "${getBaseUrl()}/test/testBasicPost"
        builder.body = "Please don't play the repeating game!"
        def result = builder.post()
        assert result == "Please don't play the repeating game!"
    }

    /**
     * Test DSL usage of the POST method.
     */
    void testPostDSL() {
        def result = new RequestBuilder().post {
            uri = "${getBaseUrl()}/test/testBasicPost"
            body = "Please don't play the repeating game!"
        }
        assert result == "Please don't play the repeating game!"
    }

    /**
     * Test simple usage of the PUT method.
     */
    void testPutSimple() {
        def result = new RequestBuilder().put("${getBaseUrl()}/test/testBasicPut", "Please don't play the repeating game!")
        assert result == "Please don't play the repeating game!"
    }

    /**
     * Test builder usage of the PUT method.
     */
    void testPutBuilder() {
        RequestBuilder builder = new RequestBuilder()
        builder.uri = "${getBaseUrl()}/test/testBasicPut"
        builder.body = "Please don't play the repeating game!"
        def result = builder.put()
        assert result == "Please don't play the repeating game!"
    }

    /**
     * Test DSL usage of the PUT method.
     */
    void testPutDSL() {
        def result = new RequestBuilder().put {
            uri = "${getBaseUrl()}/test/testBasicPut"
            body = "Please don't play the repeating game!"
        }
        assert result == "Please don't play the repeating game!"
    }
}
