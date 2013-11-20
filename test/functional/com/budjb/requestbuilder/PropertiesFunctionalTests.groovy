package com.budjb.requestbuilder

import com.budjb.requestbuilder.RequestBuilder
import com.budjb.requestbuilder.RequestBuilderTest
import com.budjb.requestbuilder.httpexception.*

class PropertiesFunctionalTests extends RequestBuilderTest {
    /**
     * Tests that the accept header is received and respected by the server.
     */
    void testAcceptSuccess() {
        def response = new RequestBuilder().get {
            uri = getUri('test/testAccept')
            accept = 'text/plain'
        }
        assert response == 'I am plain text.'
    }

    /**
     * Tests that an unacceptable accept type is handled and the correct exception thrown.
     */
    void testAcceptFail() {
        shouldFail(HttpNotAcceptableException) {
            new RequestBuilder().get {
                uri = getUri('/test/testAccept')
                accept = 'foo/bar'
            }
        }
    }

    /**
     * Tests the read timeout handling.
     */
    void testReadTimeout() {
        shouldFail(SocketTimeoutException) {
            new RequestBuilder().get {
                uri = getUri('/test/testReadTimeout')
                readTimeout = 1000
            }
        }
    }

    /**
     * Tests the binary response option.
     */
    void testBinaryResponse() {
        def response = new RequestBuilder().get {
            uri = getUri('/test/testBasicGet')
            binaryResponse = true
        }
        assert response == [84, 104, 101, 32, 113, 117, 105, 99, 107, 32, 98, 114, 111, 119, 110, 32, 102, 111, 120, 32, 106, 117, 109, 112, 115, 32, 111, 118, 101, 114, 32, 116, 104, 101, 32, 108, 97, 122, 121, 32, 100, 111, 103, 46]
    }

    /**
     * Tests the output of a redirect.
     */
    void testFollowRedirect() {
        def response = new RequestBuilder().get {
            uri = getUri('/test/testRedirect')
            followRedirects = true
        }
        assert response == 'The quick brown fox jumps over the lazy dog.'
    }

    /**
     * Tests that the appropriate exception is thrown on a redirect.
     */
    void testDoNotFollowRedirect() {
        shouldFail(HttpMovedPermanentlyException) {
            new RequestBuilder().get {
                uri = getUri('/test/testRedirect')
                followRedirects = false
            }
        }
    }

    /**
     * Tests the form parameter of the request builder.
     */
    void testForm() {
        def response = new RequestBuilder().post {
            uri = getUri('/test/testParams')
            form = ['foo': 'bar', 'key': 'value']
        }
        assert response == ['foo': 'bar', 'key': 'value']
    }

    /**
     * Test the headers parameter.
     */
    void testHeaders() {
        def response = new RequestBuilder().get {
            uri = getUri('/test/testHeaders')
            headers = ['foo': 'bar', 'key': 'value']
        }
        assert response == ['foo': 'bar', 'key': 'value']
    }

    /**
     * Test the query parameter.
     */
    void testQuery() {
        def response = new RequestBuilder().get {
            uri = getUri('/test/testParams')
            query = ['foo': 'bar', 'key': 'value']
        }
        assert response == ['foo': 'bar', 'key': 'value']
    }

    /**
     * Test the skipStatusCheck parameter.
     */
    void testSkipStatusCheck() {
        shouldFail(HttpInternalServerErrorException) {
            new RequestBuilder().get {
                uri = getUri('/test/test500')
            }
        }
        new RequestBuilder().get {
            uri = getUri('/test/test500')
            skipStatusCheck = true
        }
    }

    /**
     * Test basic auth functionality.
     */
    void testBasicAuth() {
        shouldFail(HttpUnauthorizedException) {
            new RequestBuilder().get {
                uri = getUri('/test/testAuth')
            }
        }
        new RequestBuilder().get {
            uri = getUri('/test/testAuth')
            useBasicAuth = true
            basicAuthUserName = 'foo'
            basicAuthPassword = 'bar'
        }
    }
}
