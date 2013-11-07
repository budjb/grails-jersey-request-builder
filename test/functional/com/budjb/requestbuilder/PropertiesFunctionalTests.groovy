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
}
