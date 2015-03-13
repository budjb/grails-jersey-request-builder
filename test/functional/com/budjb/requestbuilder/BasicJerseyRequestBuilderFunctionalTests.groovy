/*
 * Copyright 2015 Bud Byrd
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

import grails.util.Holders

class BasicJerseyRequestBuilderFunctionalTests extends RequestBuilderFunctionalTest {
    /**
     * Jersey request builder bean.
     */
    JerseyRequestBuilder jerseyRequestBuilder = Holders.applicationContext.getBean('jerseyRequestBuilder')

    /**
     * Test simple usage of the GET method.
     */
    void testGetSimple() {
        def result = jerseyRequestBuilder.get(getUri('/test/testBasicGet'))
        assert result == 'The quick brown fox jumps over the lazy dog.'
    }

    /**
     * Test builder usage of the GET method.
     */
    void testGetBuilder() {
        RequestProperties requestProperties = new RequestProperties()
        requestProperties.uri = getUri('/test/testBasicGet')
        def result = jerseyRequestBuilder.get(requestProperties)
        assert result == 'The quick brown fox jumps over the lazy dog.'
    }

    /**
     * Test DSL usage of the GET method.
     */
    void testGetDSL() {
        def result = jerseyRequestBuilder.get {
            uri = getUri('/test/testBasicGet')
        }
        assert result == 'The quick brown fox jumps over the lazy dog.'
    }

    /**
     * Test simple usage of the DELETE method.
     */
    void testDeleteSimple() {
        def result = jerseyRequestBuilder.delete(getUri('/test/testBasicDelete'))
        assert result == "Please don't hurt me!"
    }

    /**
     * Test builder usage of the DELETE method.
     */
    void testDeleteBuilder() {
        RequestProperties requestProperties = new RequestProperties()
        requestProperties.uri = getUri('/test/testBasicDelete')
        def result = jerseyRequestBuilder.delete(requestProperties)
        assert result == "Please don't hurt me!"
    }

    /**
     * Test DSL usage of the DELETE method.
     */
    void testDeleteDSL() {
        def result = jerseyRequestBuilder.delete {
            uri = getUri('/test/testBasicDelete')
        }
        assert result == "Please don't hurt me!"
    }

    /**
     * Test simple usage of the POST method.
     */
    void testPostSimple() {
        def result = jerseyRequestBuilder.post(getUri('/test/testBasicPost'), "Please don't play the repeating game!")
        assert result == "Please don't play the repeating game!"
    }

    /**
     * Test builder usage of the POST method.
     */
    void testPostBuilder() {
        RequestProperties requestProperties = new RequestProperties()
        requestProperties.uri = getUri('/test/testBasicPost')
        requestProperties.body = "Please don't play the repeating game!"
        def result = jerseyRequestBuilder.post(requestProperties)
        assert result == "Please don't play the repeating game!"
    }

    /**
     * Test DSL usage of the POST method.
     */
    void testPostDSL() {
        def result = jerseyRequestBuilder.post {
            uri = getUri('/test/testBasicPost')
            body = "Please don't play the repeating game!"
        }
        assert result == "Please don't play the repeating game!"
    }

    /**
     * Test simple usage of the PUT method.
     */
    void testPutSimple() {
        def result = jerseyRequestBuilder.put(getUri('/test/testBasicPut'), "Please don't play the repeating game!")
        assert result == "Please don't play the repeating game!"
    }

    /**
     * Test builder usage of the PUT method.
     */
    void testPutBuilder() {
        RequestProperties requestProperties = new RequestProperties()
        requestProperties.uri = getUri('/test/testBasicPut')
        requestProperties.body = "Please don't play the repeating game!"
        def result = jerseyRequestBuilder.put(requestProperties)
        assert result == "Please don't play the repeating game!"
    }

    /**
     * Test DSL usage of the PUT method.
     */
    void testPutDSL() {
        def result = jerseyRequestBuilder.put {
            uri = getUri('/test/testBasicPut')
            body = "Please don't play the repeating game!"
        }
        assert result == "Please don't play the repeating game!"
    }
}
