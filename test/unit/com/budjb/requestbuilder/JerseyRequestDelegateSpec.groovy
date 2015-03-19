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

import com.budjb.requestbuilder.httpexception.HttpNotFoundException
import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.ClientResponse
import com.sun.jersey.api.client.WebResource
import com.sun.jersey.api.client.config.ClientConfig
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter
import com.sun.jersey.api.representation.Form
import groovy.json.JsonBuilder
import groovy.util.slurpersupport.GPathResult
import groovy.xml.XmlUtil
import org.codehaus.groovy.grails.commons.GrailsApplication
import spock.lang.Specification

import javax.ws.rs.core.Cookie
import javax.ws.rs.core.MediaType

class JerseyRequestDelegateSpec extends Specification {
    JerseyRequestDelegate jerseyRequestDelegate
    JerseyClientFactory jerseyClientFactory
    GrailsApplication grailsApplication

    RequestProperties requestProperties
    ClientResponse clientResponse
    WebResource.Builder builder
    WebResource webResource
    Client client

    def setup() {
        grailsApplication = Mock(GrailsApplication)
        grailsApplication.getConfig() >> new ConfigObject()

        jerseyClientFactory = Mock(JerseyClientFactory)
        jerseyRequestDelegate = new JerseyRequestDelegate(jerseyClientFactory)

        requestProperties = new RequestProperties(grailsApplication).build { uri = 'http://example.com' }

        clientResponse = createResponse(200, MediaType.TEXT_PLAIN_TYPE, "Hello, world!")

        builder = Mock(WebResource.Builder)
        builder.get(ClientResponse) >> { return clientResponse }

        webResource = Mock(WebResource)
        webResource.getRequestBuilder() >> builder

        client = Mock(Client)
        client.resource(_) >> webResource

        jerseyClientFactory.createClient() >> client
    }

    def 'Validate JSON conversion'() {
        setup:
        clientResponse = createJsonResponse()

        when:
        def response = jerseyRequestDelegate.get(requestProperties)

        then:
        response == getJsonSample()
    }

    def 'Validate XML conversion'() {
        setup:
        clientResponse = createXmlResponse()

        when:
        def response = jerseyRequestDelegate.get(requestProperties)

        then:
        response instanceof GPathResult
        XmlUtil.serialize(response) == getXmlSample()
    }

    def 'Validate response with no conversion available'() {
        setup:
        clientResponse = createResponse(200, MediaType.TEXT_PLAIN_TYPE, "hi there")

        when:
        def response = jerseyRequestDelegate.get(requestProperties)

        then:
        response == "hi there"
    }

    def 'Validate basic auth interactions'() {
        setup:
        requestProperties.build {
            useBasicAuth = true
            basicAuthUserName = 'foo'
            basicAuthPassword = 'bar'
        }

        when:
        jerseyRequestDelegate.get(requestProperties)

        then:
        1 * client.addFilter(_ as HTTPBasicAuthFilter)
    }

    def 'Validate client interactions'() {
        setup:
        requestProperties.build {
            connectionTimeout = 5000
            readTimeout = 50000
            followRedirects = false
            chunkSize = 8192
        }

        when:
        jerseyRequestDelegate.get(requestProperties)

        then:
        1 * client.setConnectTimeout(5000)
        1 * client.setReadTimeout(50000)
        1 * client.setFollowRedirects(false)
        1 * client.setChunkedEncodingSize(8192)
    }

    def 'Validate web resource interactions'() {
        setup:
        requestProperties.build {
            query = [
                'foo': 'bar',
                'hi' : 'there'
            ]
        }

        when:
        jerseyRequestDelegate.get(requestProperties)

        then:
        1 * webResource.queryParam('foo', 'bar') >> webResource
        1 * webResource.queryParam('hi', 'there') >> webResource
    }

    def 'Validate builder interactions'() {
        setup:
        requestProperties.build {
            accept = 'application/json'
            contentType = 'text/plain'
            cookies = [
                'foo': 'bar'
            ]
            headers = [
                'foo': 'bar',
                'hi' : 'there'
            ]
        }

        when:
        jerseyRequestDelegate.get(requestProperties)

        then:
        1 * builder.accept('application/json')
        1 * builder.type('text/plain')
        1 * builder.cookie({ (it as Cookie).name == 'foo' && (it as Cookie).value == 'bar' })
        1 * builder.header('foo', 'bar')
        1 * builder.header('hi', 'there')
    }

    def 'If form is set, ensure it is marshalled correctly'() {
        setup:
        requestProperties = Mock(RequestProperties)
        requestProperties.getUri() >> 'http://example.com'
        requestProperties.getForm() >> ['foo': 'bar', 'hi': 'there']
        requestProperties.clone() >> { return requestProperties }

        Form form

        when:
        jerseyRequestDelegate.get(requestProperties)

        then:
        1 * requestProperties.setBody({
            if (it instanceof Form) {
                form = it
                return true
            }
            return false
        })
        1 * requestProperties.setContentType('application/x-www-form-urlencoded')

        form == ['foo': ['bar'], 'hi': ['there']]
    }

    def 'If form is set and a content type is provided, ensure it is not overwritten'() {
        setup:
        requestProperties = Mock(RequestProperties)
        requestProperties.getUri() >> 'http://example.com'
        requestProperties.getForm() >> ['foo': 'bar']
        requestProperties.getContentType() >> 'foo/bar'
        requestProperties.clone() >> { return requestProperties }

        when:
        jerseyRequestDelegate.get(requestProperties)

        then:
        0 * requestProperties.setContentType(_)
    }

    def 'If the body is a map, ensure it is marshalled to JSON correctly'() {
        setup:
        requestProperties = Mock(RequestProperties)
        requestProperties.getUri() >> 'http://example.com'
        requestProperties.getBody() >> ['foo': 'bar']
        requestProperties.getForm() >> [:]
        requestProperties.clone() >> { return requestProperties }

        when:
        jerseyRequestDelegate.get(requestProperties)

        then:
        1 * requestProperties.setBody({ it == '{"foo":"bar"}' })
        1 * requestProperties.setContentType('application/json')
    }

    def 'If the body is a list, ensure it is marshalled to JSON correctly'() {
        setup:
        requestProperties = Mock(RequestProperties)
        requestProperties.getUri() >> 'http://example.com'
        requestProperties.getBody() >> ['foo', 'bar']
        requestProperties.getForm() >> [:]
        requestProperties.clone() >> { return requestProperties }

        when:
        jerseyRequestDelegate.get(requestProperties)

        then:
        1 * requestProperties.setBody({ it == '["foo","bar"]' })
        1 * requestProperties.setContentType('application/json')
    }

    def 'If the body is a map and a content type is provided, ensure it is not overwritten'() {
        setup:
        requestProperties = Mock(RequestProperties)
        requestProperties.getUri() >> 'http://example.com'
        requestProperties.getBody() >> ['foo': 'bar']
        requestProperties.getForm() >> [:]
        requestProperties.getContentType() >> 'foo/bar'
        requestProperties.clone() >> { return requestProperties }

        when:
        jerseyRequestDelegate.get(requestProperties)

        then:
        0 * requestProperties.setContentType(_)
    }

    def 'If all-trusting SSL trust is requested, the jersey client is build with the correct TLS trust instance'() {
        setup:
        requestProperties.build {
            ignoreInvalidSSL = true
        }

        when:
        jerseyRequestDelegate.get(requestProperties)

        then:
        1 * jerseyClientFactory.createClient(_ as ClientConfig) >> client
    }

    def 'If rawClientResponse is true, the unprocessed response object is returned'() {
        setup:
        requestProperties.rawClientResponse = true
        clientResponse = createResponse(200, MediaType.TEXT_PLAIN_TYPE, "foobar")

        when:
        def response = jerseyRequestDelegate.get(requestProperties)

        then:
        response instanceof ClientResponse
        (response as ClientResponse).getEntity(String) == "foobar"
    }

    def 'If binaryResponse is true, a byte array is returned'() {
        requestProperties.binaryResponse = true
        clientResponse = createResponse(200, MediaType.TEXT_PLAIN_TYPE, "foobar")

        when:
        def response = jerseyRequestDelegate.get(requestProperties)

        then:
        response instanceof byte[]
        response == "foobar".getBytes()
    }

    def 'If a non-2xx response code is returned, by default an exception is thrown'() {
        setup:
        clientResponse = createResponse(404, MediaType.TEXT_PLAIN_TYPE, "object not found")

        when:
        jerseyRequestDelegate.get(requestProperties)

        then:
        thrown HttpNotFoundException
    }

    def 'If a non-2xx response code is returned but skip status check is enabled, no exception is thrown'() {
        setup:
        requestProperties.skipStatusCheck = true
        clientResponse = createResponse(404, MediaType.TEXT_PLAIN_TYPE, "object not found")

        when:
        def response = jerseyRequestDelegate.get(requestProperties)

        then:
        notThrown HttpNotFoundException
        response == "object not found"
    }

    def 'Validate get() interactions'() {
        when:
        jerseyRequestDelegate.get(requestProperties)

        then:
        1 * builder.get(ClientResponse) >> clientResponse
    }

    def 'Validate post() interactions'() {
        setup:
        requestProperties.body = "foobar"

        when:
        jerseyRequestDelegate.post(requestProperties)

        then:
        1 * builder.post(ClientResponse, "foobar") >> clientResponse
    }

    def 'Validate put() interactions'() {
        setup:
        requestProperties.body = "foobar"

        when:
        jerseyRequestDelegate.put(requestProperties)

        then:
        1 * builder.put(ClientResponse, "foobar") >> clientResponse
    }

    def 'Validate delete() interactions'() {
        when:
        jerseyRequestDelegate.delete(requestProperties)

        then:
        1 * builder.delete(ClientResponse) >> clientResponse
    }

    def 'Validate head() interactions'() {
        when:
        jerseyRequestDelegate.head(requestProperties)

        then:
        1 * builder.head() >> clientResponse
    }

    def 'Validate trace() interactions'() {
        when:
        jerseyRequestDelegate.trace(requestProperties)

        then:
        1 * builder.method('TRACE', ClientResponse) >> clientResponse
    }

    def 'Validate options() interactions'() {
        when:
        jerseyRequestDelegate.options(requestProperties)

        then:
        1 * builder.options(ClientResponse) >> clientResponse
    }

    Map getJsonSample() {
        return [
            foo : 'bar',
            hi  : 'there',
            dogs: [
                'Penny',
                'Skip'
            ]
        ]
    }

    String getXmlSample() {
        return '<?xml version="1.0" encoding="UTF-8"?><foo what="bar">\n' +
            '<hi where="there">\n' +
            '<dog>Penny</dog>\n' +
            '<dog>Skip</dog>\n' +
            '</hi>\n' +
            '</foo>\n'
    }

    ClientResponse createJsonResponse() {
        return createResponse(200, MediaType.APPLICATION_JSON_TYPE, new JsonBuilder(getJsonSample()).toString())
    }

    ClientResponse createXmlResponse() {
        return createResponse(200, MediaType.APPLICATION_XML_TYPE, getXmlSample())
    }

    ClientResponse createResponse(int status, MediaType contentType, String body) {
        ClientResponse clientResponse = Mock(ClientResponse)
        clientResponse.hasEntity() >> true
        clientResponse.getEntity(String) >> body
        clientResponse.getEntity(byte[]) >> body.getBytes()
        clientResponse.getStatus() >> status
        clientResponse.getType() >> contentType

        return clientResponse
    }

    def 'Validate multivalued header interactions'() {
        setup:
        requestProperties.addHeader('foo', 'bar')
        requestProperties.addHeader('foo', 'baz')
        requestProperties.addHeader('hi', 'there')

        when:
        jerseyRequestDelegate.get(requestProperties)

        then:
        1 * builder.header('foo', 'bar')
        1 * builder.header('foo', 'baz')
        1 * builder.header('hi', 'there')
    }

    def 'Validate multivalued query parameter interactions'() {
        setup:
        requestProperties.addQuery('foo', 'bar')
        requestProperties.addQuery('foo', 'baz')
        requestProperties.addQuery('hi', 'there')

        when:
        jerseyRequestDelegate.get(requestProperties)

        then:
        1 * webResource.queryParam('foo', 'bar') >> webResource
        1 * webResource.queryParam('foo', 'baz') >> webResource
        1 * webResource.queryParam('hi', 'there') >> webResource

    }

    def 'Validate multivalued form field interactions'() {
        setup:
        requestProperties.addFormField('foo', 'bar')
        requestProperties.addFormField('foo', 'baz')
        requestProperties.addFormField('hi', 'there')

        Form form

        when:
        jerseyRequestDelegate.post(requestProperties)

        then:
        1 * builder.post(ClientResponse, {
            if (it instanceof Form) {
                form = it
                return true
            }
            return false
        }) >> clientResponse

        form == ['foo': ['bar', 'baz'], 'hi': ['there']]
    }
}
