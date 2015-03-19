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

class RequestPropertiesSpec extends Specification {
    GrailsApplication grailsApplication

    def setup() {
        grailsApplication = Mock(GrailsApplication)
    }

    def 'Validate default values when no grails configuration is present'() {
        setup:
        grailsApplication.getConfig() >> new ConfigObject()

        when:
        RequestProperties properties = new RequestProperties(grailsApplication)

        then:
        properties.accept == null
        properties.basicAuthPassword == null
        properties.basicAuthUserName == null
        properties.binaryResponse == false
        properties.chunkSize == null
        properties.connectionTimeout == null
        properties.contentType == null
        properties.convertJson == true
        properties.convertXML == true
        properties.cookies == [:]
        properties.debug == false
        properties.encodeGzip == false
        properties.followRedirects == true
        properties.form == [:]
        properties.headers == [:]
        properties.ignoreInvalidSSL == false
        properties.query == [:]
        properties.rawClientResponse == false
        properties.readTimeout == null
        properties.skipStatusCheck == false
        properties.useBasicAuth == false
    }

    def 'Validate defaults when they are overridden in the Grails application configuration'() {
        setup:
        grailsApplication.getConfig() >> new ConfigObject([
            jerseyRequestBuilder: [
                accept           : 'application/json',
                basicAuthPassword: 'foo',
                basicAuthUserName: 'bar',
                binaryResponse   : true,
                chunkSize        : 50,
                connectionTimeout: 70,
                contentType      : 'text/plain',
                convertJson      : false,
                convertXML       : false,
                debug            : true,
                encodeGzip       : true,
                followRedirects  : false,
                ignoreInvalidSSL : true,
                rawClientResponse: true,
                readTimeout      : 60,
                skipStatusCheck  : true,
                useBasicAuth     : true
            ]
        ])

        when:
        RequestProperties properties = new RequestProperties(grailsApplication)

        then:
        properties.accept == 'application/json'
        properties.basicAuthPassword == 'foo'
        properties.basicAuthUserName == 'bar'
        properties.binaryResponse == true
        properties.chunkSize == 50
        properties.connectionTimeout == 70
        properties.contentType == 'text/plain'
        properties.convertJson == false
        properties.convertXML == false
        properties.cookies == [:]
        properties.debug == true
        properties.encodeGzip == true
        properties.followRedirects == false
        properties.form == [:]
        properties.headers == [:]
        properties.ignoreInvalidSSL == true
        properties.query == [:]
        properties.rawClientResponse == true
        properties.readTimeout == 60
        properties.skipStatusCheck == true
        properties.useBasicAuth == true
    }

    def 'Validate building via closure sets correct properties'() {
        setup:
        grailsApplication.getConfig() >> new ConfigObject([
            jerseyRequestBuilder: [
            ]
        ])

        when:
        RequestProperties properties = new RequestProperties(grailsApplication).build {
            accept = 'application/json'
            basicAuthPassword = 'foo'
            basicAuthUserName = 'bar'
            binaryResponse = true
            chunkSize = 50
            connectionTimeout = 70
            contentType = 'text/plain'
            convertJson = false
            convertXML = false
            debug = true
            encodeGzip = true
            followRedirects = false
            ignoreInvalidSSL = true
            rawClientResponse = true
            readTimeout = 60
            skipStatusCheck = true
            useBasicAuth = true
        }

        then:
        properties.accept == 'application/json'
        properties.basicAuthPassword == 'foo'
        properties.basicAuthUserName == 'bar'
        properties.binaryResponse == true
        properties.chunkSize == 50
        properties.connectionTimeout == 70
        properties.contentType == 'text/plain'
        properties.convertJson == false
        properties.convertXML == false
        properties.cookies == [:]
        properties.debug == true
        properties.encodeGzip == true
        properties.followRedirects == false
        properties.form == [:]
        properties.headers == [:]
        properties.ignoreInvalidSSL == true
        properties.query == [:]
        properties.rawClientResponse == true
        properties.readTimeout == 60
        properties.skipStatusCheck == true
        properties.useBasicAuth == true
    }

    def 'If a header already has a value and a value is added to that header, it should be multivalued'() {
        setup:
        grailsApplication.getConfig() >> new ConfigObject()
        RequestProperties requestProperties = new RequestProperties(grailsApplication)

        when:
        requestProperties.addHeader('foo', 'bar')

        then:
        requestProperties.headers == ['foo': 'bar']

        when:
        requestProperties.addHeader('foo', 'baz')

        then:
        requestProperties.headers == ['foo': ['bar', 'baz']]
    }

    def 'If a query parameter already has a value and a value is added to that query parameter, it should be multivalued'() {
        setup:
        grailsApplication.getConfig() >> new ConfigObject()
        RequestProperties requestProperties = new RequestProperties(grailsApplication)

        when:
        requestProperties.addQuery('foo', 'bar')

        then:
        requestProperties.query == ['foo': 'bar']

        when:
        requestProperties.addQuery('foo', 'baz')

        then:
        requestProperties.query == ['foo': ['bar', 'baz']]
    }

    def 'If a form field already has a value and a value is added to that form field, it should be multivalued'() {
        setup:
        grailsApplication.getConfig() >> new ConfigObject()
        RequestProperties requestProperties = new RequestProperties(grailsApplication)

        when:
        requestProperties.addFormField('foo', 'bar')

        then:
        requestProperties.form == ['foo': 'bar']

        when:
        requestProperties.addFormField('foo', 'baz')

        then:
        requestProperties.form == ['foo': ['bar', 'baz']]
    }

    def 'If a header is multivalued and the header is set, it is no longer multivalued'() {
        setup:
        grailsApplication.getConfig() >> new ConfigObject()
        RequestProperties requestProperties = new RequestProperties(grailsApplication)

        requestProperties.headers = ['foo': ['bar', 'baz']]

        when:
        requestProperties.setHeader('foo', 'meh')

        then:
        requestProperties.headers == ['foo': 'meh']
    }

    def 'If a query parameter is multivalued and the query parameter is set, it is no longer multivalued'() {
        setup:
        grailsApplication.getConfig() >> new ConfigObject()
        RequestProperties requestProperties = new RequestProperties(grailsApplication)

        requestProperties.query = ['foo': ['bar', 'baz']]

        when:
        requestProperties.setQuery('foo', 'meh')

        then:
        requestProperties.query == ['foo': 'meh']
    }

    def 'If a form field is multivalued and the form field is set, it is no longer multivalued'() {
        setup:
        grailsApplication.getConfig() >> new ConfigObject()
        RequestProperties requestProperties = new RequestProperties(grailsApplication)

        requestProperties.form = ['foo': ['bar', 'baz']]

        when:
        requestProperties.setFormField('foo', 'meh')

        then:
        requestProperties.form == ['foo': 'meh']
    }
}
