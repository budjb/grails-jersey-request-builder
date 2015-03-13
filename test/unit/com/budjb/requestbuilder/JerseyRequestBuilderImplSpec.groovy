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

import org.codehaus.groovy.grails.commons.GrailsApplication
import spock.lang.Specification

class JerseyRequestBuilderImplSpec extends Specification {
    JerseyClientFactory jerseyClientFactory
    GrailsApplication grailsApplication
    JerseyRequestBuilderImpl jerseyRequestBuilder
    JerseyRequestDelegate jerseyRequestDelegate

    def setup() {
        grailsApplication = Mock(GrailsApplication)
        grailsApplication.getConfig() >> new ConfigObject()

        jerseyRequestDelegate = Mock(JerseyRequestDelegate)

        jerseyClientFactory = Mock(JerseyClientFactory)
        jerseyClientFactory.createDelegate() >> jerseyRequestDelegate

        jerseyRequestBuilder = new JerseyRequestBuilderImpl()
        jerseyRequestBuilder.grailsApplication = grailsApplication
        jerseyRequestBuilder.jerseyClientFactory = jerseyClientFactory
    }

    def 'Validate delete(String) interactions'() {
        when:
        jerseyRequestBuilder.delete('http://example.com')

        then:
        1 * jerseyRequestDelegate.delete({ it instanceof RequestProperties && it.uri == 'http://example.com' })
    }

    def 'Validate delete(Closure) interactions'() {
        when:
        jerseyRequestBuilder.delete {
            uri = 'http://example.com'
        }

        then:
        1 * jerseyRequestDelegate.delete({
            it instanceof RequestProperties && it.uri == 'http://example.com'
        })
    }

    def 'Validate delete(RequestProperties interactions'() {
        setup:
        RequestProperties requestProperties = new RequestProperties(grailsApplication).build {
            uri = 'http://example.com'
        }

        when:
        jerseyRequestBuilder.delete(requestProperties)

        then:
        1 * jerseyRequestDelegate.delete(requestProperties)
    }

    def 'Validate get(String) interactions'() {
        when:
        jerseyRequestBuilder.get('http://example.com')

        then:
        1 * jerseyRequestDelegate.get({ it instanceof RequestProperties && it.uri == 'http://example.com' })
    }

    def 'Validate get(Closure) interactions'() {
        when:
        jerseyRequestBuilder.get {
            uri = 'http://example.com'
        }

        then:
        1 * jerseyRequestDelegate.get({
            it instanceof RequestProperties && it.uri == 'http://example.com'
        })
    }

    def 'Validate get(RequestProperties interactions'() {
        setup:
        RequestProperties requestProperties = new RequestProperties(grailsApplication).build {
            uri = 'http://example.com'
        }

        when:
        jerseyRequestBuilder.get(requestProperties)

        then:
        1 * jerseyRequestDelegate.get(requestProperties)
    }

    def 'Validate head(String) interactions'() {
        when:
        jerseyRequestBuilder.head('http://example.com')

        then:
        1 * jerseyRequestDelegate.head({ it instanceof RequestProperties && it.uri == 'http://example.com' })
    }

    def 'Validate head(Closure) interactions'() {
        when:
        jerseyRequestBuilder.head {
            uri = 'http://example.com'
        }

        then:
        1 * jerseyRequestDelegate.head({
            it instanceof RequestProperties && it.uri == 'http://example.com'
        })
    }

    def 'Validate head(RequestProperties interactions'() {
        setup:
        RequestProperties requestProperties = new RequestProperties(grailsApplication).build {
            uri = 'http://example.com'
        }

        when:
        jerseyRequestBuilder.head(requestProperties)

        then:
        1 * jerseyRequestDelegate.head(requestProperties)
    }

    def 'Validate options(String) interactions'() {
        when:
        jerseyRequestBuilder.options('http://example.com')

        then:
        1 * jerseyRequestDelegate.options({ it instanceof RequestProperties && it.uri == 'http://example.com' })
    }

    def 'Validate options(Closure) interactions'() {
        when:
        jerseyRequestBuilder.options {
            uri = 'http://example.com'
        }

        then:
        1 * jerseyRequestDelegate.options({
            it instanceof RequestProperties && it.uri == 'http://example.com'
        })
    }

    def 'Validate options(RequestProperties interactions'() {
        setup:
        RequestProperties requestProperties = new RequestProperties(grailsApplication).build {
            uri = 'http://example.com'
        }

        when:
        jerseyRequestBuilder.options(requestProperties)

        then:
        1 * jerseyRequestDelegate.options(requestProperties)
    }

    def 'Validate post(String) interactions'() {
        when:
        jerseyRequestBuilder.post('http://example.com')

        then:
        1 * jerseyRequestDelegate.post({ it instanceof RequestProperties && it.uri == 'http://example.com' })
    }

    def 'Validate post(String, Object) interactions'() {
        when:
        jerseyRequestBuilder.post('http://example.com', 'foobar')

        then:
        1 * jerseyRequestDelegate.post({
            it instanceof RequestProperties && it.uri == 'http://example.com' && it.body == 'foobar'
        })
    }

    def 'Validate post(Closure) interactions'() {
        when:
        jerseyRequestBuilder.post {
            uri = 'http://example.com'
            body = 'foobar'
        }

        then:
        1 * jerseyRequestDelegate.post({
            it instanceof RequestProperties && it.uri == 'http://example.com' && it.body == 'foobar'
        })
    }

    def 'Validate post(RequestProperties interactions'() {
        setup:
        RequestProperties requestProperties = new RequestProperties(grailsApplication).build {
            uri = 'http://example.com'
            body = 'foobar'
        }

        when:
        jerseyRequestBuilder.post(requestProperties)

        then:
        1 * jerseyRequestDelegate.post(requestProperties)
    }

    def 'Validate put(String) interactions'() {
        when:
        jerseyRequestBuilder.put('http://example.com')

        then:
        1 * jerseyRequestDelegate.put({ it instanceof RequestProperties && it.uri == 'http://example.com' })
    }

    def 'Validate put(String, Object) interactions'() {
        when:
        jerseyRequestBuilder.put('http://example.com', 'foobar')

        then:
        1 * jerseyRequestDelegate.put({
            it instanceof RequestProperties && it.uri == 'http://example.com' && it.body == 'foobar'
        })
    }

    def 'Validate put(Closure) interactions'() {
        when:
        jerseyRequestBuilder.put {
            uri = 'http://example.com'
            body = 'foobar'
        }

        then:
        1 * jerseyRequestDelegate.put({
            it instanceof RequestProperties && it.uri == 'http://example.com' && it.body == 'foobar'
        })
    }

    def 'Validate put(RequestProperties interactions'() {
        setup:
        RequestProperties requestProperties = new RequestProperties(grailsApplication).build {
            uri = 'http://example.com'
            body = 'foobar'
        }

        when:
        jerseyRequestBuilder.put(requestProperties)

        then:
        1 * jerseyRequestDelegate.put(requestProperties)
    }

    def 'Validate trace(String) interactions'() {
        when:
        jerseyRequestBuilder.trace('http://example.com')

        then:
        1 * jerseyRequestDelegate.trace({ it instanceof RequestProperties && it.uri == 'http://example.com' })
    }

    def 'Validate trace(Closure) interactions'() {
        when:
        jerseyRequestBuilder.trace {
            uri = 'http://example.com'
        }

        then:
        1 * jerseyRequestDelegate.trace({
            it instanceof RequestProperties && it.uri == 'http://example.com'
        })
    }

    def 'Validate trace(RequestProperties interactions'() {
        setup:
        RequestProperties requestProperties = new RequestProperties(grailsApplication).build {
            uri = 'http://example.com'
        }

        when:
        jerseyRequestBuilder.trace(requestProperties)

        then:
        1 * jerseyRequestDelegate.trace(requestProperties)
    }
}
