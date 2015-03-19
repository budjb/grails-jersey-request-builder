/*
 * Copyright 2013 Bud Byrd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.budjb.requestbuilder

import groovy.json.JsonBuilder

class TestController {
    static allowedMethods = [
        testBasicGet:    'GET',
        testBasicPost:   'POST',
        testBasicPut:    'PUT',
        testBasicDelete: 'DELETE',
    ]

    def testBasicGet = {
        render "The quick brown fox jumps over the lazy dog."
    }

    def testBasicPost = {
        render request.reader.text
    }

    def testBasicPut = {
        render request.reader.text
    }

    def testBasicDelete = {
        render "Please don't hurt me!"
    }

    def testAccept = {
        String accept = request.getHeader("Accept")

        switch (accept) {
            case "application/json":
                render text: '{"foo":"bar"}', contentType: 'application/json'
                break
            case "text/plain":
                render text: 'I am plain text.', contentType: 'text/plain'
                break
            case "text/xml":
                render text: '<foo>bar</foo>', contentType: 'text/xml'
                break
            default:
                render text: "I can't handle ${accept}", contentType: 'text/plain', status: 406
        }
    }

    def testReadTimeout = {
        sleep(5000)
        render "Hello, world!"
    }

    def testRedirect = {
        redirect action: 'testBasicGet', permanent: true
    }

    def testParams = {
        params.remove('action')
        params.remove('controller')
        render text: new JsonBuilder(params).toString(), contentType: 'application/json'
    }

    def testHeaders = {
        Map<String, Object> headers = [:]

        for (Enumeration<String> headerNames = request.headerNames; headerNames.hasMoreElements();) {
            String headerName = headerNames.nextElement()

            for (Enumeration<String> headerValues = request.getHeaders(headerName); headerValues.hasMoreElements();) {
                String headerValue = headerValues.nextElement()

                if (!headers.containsKey(headerName)) {
                    headers[headerName] = headerValue
                }
                else if (headers[headerName] instanceof List) {
                    headers[headerName] << headerValue
                }
                else {
                    headers[headerName] = [headers[headerName], headerValue]
                    throw new Exception(headers)
                }
            }
        }

        render text: new JsonBuilder(headers).toString(), contentType: 'application/json'
    }

    def test500 = {
        render text: 'something bad happened', status: 500
    }

    def testAuth = {
        if (!request.getHeader('Authorization')) {
            render text: 'authentication required', status: 401
        }
        else {
            render 'welcome'
        }
    }
}
