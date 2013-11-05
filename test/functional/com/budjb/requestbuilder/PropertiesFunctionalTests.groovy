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
            uri = "${getBaseUrl()}/test/testAccept"
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
                accept = 'foo/bar'
                uri = "${getBaseUrl()}/test/testAccept"
            }
        }
    }
}
