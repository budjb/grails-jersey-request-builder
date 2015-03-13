/*
 * Copyright 2013-2015 Bud Byrd
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

class BasicRequestBuilderFunctionalTests extends RequestBuilderFunctionalTest {
    /**
     * Test simple usage of the GET method.
     */
    void testGetSimple() {
        def result = new RequestBuilder().get(getUri('/test/testBasicGet'))
        assert result == 'The quick brown fox jumps over the lazy dog.'
    }

    /**
     * Test builder usage of the GET method.
     */
    void testGetBuilder() {
        RequestBuilder builder = new RequestBuilder()
        builder.uri = getUri('/test/testBasicGet')
        def result = builder.get()
        assert result == 'The quick brown fox jumps over the lazy dog.'
    }

    /**
     * Test DSL usage of the GET method.
     */
    void testGetDSL() {
        def result = new RequestBuilder().get {
            uri = getUri('/test/testBasicGet')
        }
        assert result == 'The quick brown fox jumps over the lazy dog.'
    }

    /**
     * Test simple usage of the DELETE method.
     */
    void testDeleteSimple() {
        def result = new RequestBuilder().delete(getUri('/test/testBasicDelete'))
        assert result == "Please don't hurt me!"
    }

    /**
     * Test builder usage of the DELETE method.
     */
    void testDeleteBuilder() {
        RequestBuilder builder = new RequestBuilder()
        builder.uri = getUri('/test/testBasicDelete')
        def result = builder.delete()
        assert result == "Please don't hurt me!"
    }

    /**
     * Test DSL usage of the DELETE method.
     */
    void testDeleteDSL() {
        def result = new RequestBuilder().delete {
            uri = getUri('/test/testBasicDelete')
        }
        assert result == "Please don't hurt me!"
    }

    /**
     * Test simple usage of the POST method.
     */
    void testPostSimple() {
        def result = new RequestBuilder().post(getUri('/test/testBasicPost'), "Please don't play the repeating game!")
        assert result == "Please don't play the repeating game!"
    }

    /**
     * Test builder usage of the POST method.
     */
    void testPostBuilder() {
        RequestBuilder builder = new RequestBuilder()
        builder.uri = getUri('/test/testBasicPost')
        builder.body = "Please don't play the repeating game!"
        def result = builder.post()
        assert result == "Please don't play the repeating game!"
    }

    /**
     * Test DSL usage of the POST method.
     */
    void testPostDSL() {
        def result = new RequestBuilder().post {
            uri = getUri('/test/testBasicPost')
            body = "Please don't play the repeating game!"
        }
        assert result == "Please don't play the repeating game!"
    }

    /**
     * Test simple usage of the PUT method.
     */
    void testPutSimple() {
        def result = new RequestBuilder().put(getUri('/test/testBasicPut'), "Please don't play the repeating game!")
        assert result == "Please don't play the repeating game!"
    }

    /**
     * Test builder usage of the PUT method.
     */
    void testPutBuilder() {
        RequestBuilder builder = new RequestBuilder()
        builder.uri = getUri('/test/testBasicPut')
        builder.body = "Please don't play the repeating game!"
        def result = builder.put()
        assert result == "Please don't play the repeating game!"
    }

    /**
     * Test DSL usage of the PUT method.
     */
    void testPutDSL() {
        def result = new RequestBuilder().put {
            uri = getUri('/test/testBasicPut')
            body = "Please don't play the repeating game!"
        }
        assert result == "Please don't play the repeating game!"
    }
}
