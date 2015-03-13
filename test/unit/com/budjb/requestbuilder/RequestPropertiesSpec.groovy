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
}
