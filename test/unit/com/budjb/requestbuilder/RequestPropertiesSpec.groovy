package com.budjb.requestbuilder

import org.codehaus.groovy.grails.commons.GrailsApplication
import spock.lang.Specification

class RequestPropertiesSpec extends Specification {
    GrailsApplication grailsApplication

    def setup() {
        grailsApplication = Mock(GrailsApplication)
    }

    def 'Validate default properties with no Grails configuration'() {
        setup:
        grailsApplication.getConfig() >> new ConfigObject()

        when:
        RequestProperties requestProperties = new RequestProperties(grailsApplication)

        then:
        requestProperties.accept == null
        requestProperties.basicAuthPassword == null
        requestProperties.basicAuthUserName == null
        requestProperties.binaryResponse == false
        requestProperties.body == null
        requestProperties.chunkSize == null
        requestProperties.connectionTimeout == null
        requestProperties.contentType == null
        requestProperties.convertJson == true
        requestProperties.convertXML == true
        requestProperties.cookies == [:]
        requestProperties.debug == false
        requestProperties.encodeGzip == false
        requestProperties.followRedirects == true
        requestProperties.form == [:]
        requestProperties.headers == [:]
        requestProperties.ignoreInvalidSSL == false
        requestProperties.query == [:]
        requestProperties.rawClientResponse == false
        requestProperties.readTimeout == null
        requestProperties.skipStatusCheck == false
        requestProperties.uri == null
        requestProperties.useBasicAuth == false
    }

    def 'Validate default values when configured from the Grails configuration'() {
        setup:
        ConfigObject config = new ConfigObject()
        config.putAll([
            'jerseyRequestBuilder': [
                accept: 'text/plain',
                basicAuthPassword: 'password',
                basicAuthUserName: 'username',
                binaryResponse: true,
                body: "test body",
                chunkSize: 1234,
                connectionTimeout: 10000,
                contentType: "text/xml",
                convertJson: false,
                convertXML: false,
                cookies: [foo: 'bar'],
                debug: true,
                encodeGzip: true,
                followRedirects: false,
                form: [bar: 'baz'],
                headers: [hi: 'there'],
                ignoreInvalidSSL: true,
                query: [good: 'bye'],
                rawClientResponse: true,
                readTimeout: 5000,
                skipStatusCheck: true,
                uri: "http://example.com",
                useBasicAuth: true
            ]
        ])
        grailsApplication.getConfig() >> config

        when:
        RequestProperties requestProperties = new RequestProperties(grailsApplication)

        then:
        requestProperties.accept == 'text/plain'
        requestProperties.basicAuthPassword == 'password'
        requestProperties.basicAuthUserName == 'username'
        requestProperties.binaryResponse == true
        requestProperties.body == null
        requestProperties.chunkSize == 1234
        requestProperties.connectionTimeout == 10000
        requestProperties.contentType == 'text/xml'
        requestProperties.convertJson == false
        requestProperties.convertXML == false
        requestProperties.cookies == [:]
        requestProperties.debug == true
        requestProperties.encodeGzip == true
        requestProperties.followRedirects == false
        requestProperties.form == [:]
        requestProperties.headers == [:]
        requestProperties.ignoreInvalidSSL == true
        requestProperties.query == [:]
        requestProperties.rawClientResponse == true
        requestProperties.readTimeout == 5000
        requestProperties.skipStatusCheck == true
        requestProperties.uri == null
        requestProperties.useBasicAuth == true
    }

    def 'Validate properties when built from a closure'() {
        setup:
        grailsApplication.getConfig() >> new ConfigObject()

        when:
        RequestProperties requestProperties = new RequestProperties(grailsApplication).build {
            accept = 'text/plain'
            basicAuthPassword = 'password'
            basicAuthUserName = 'username'
            binaryResponse = true
            body = "test body"
            chunkSize = 1234
            connectionTimeout = 10000
            contentType = "text/xml"
            convertJson = false
            convertXML = false
            cookies = [foo: 'bar']
            debug = true
            encodeGzip = true
            followRedirects = false
            form = [bar: 'baz']
            headers = [hi: 'there']
            ignoreInvalidSSL = true
            query = [good: 'bye']
            rawClientResponse = true
            readTimeout = 5000
            skipStatusCheck = true
            uri = "http://example.com"
            useBasicAuth = true
        }

        then:
        requestProperties.accept == 'text/plain'
        requestProperties.basicAuthPassword == 'password'
        requestProperties.basicAuthUserName == 'username'
        requestProperties.binaryResponse == true
        requestProperties.body == "test body"
        requestProperties.chunkSize == 1234
        requestProperties.connectionTimeout == 10000
        requestProperties.contentType == 'text/xml'
        requestProperties.convertJson == false
        requestProperties.convertXML == false
        requestProperties.cookies == [foo: 'bar']
        requestProperties.debug == true
        requestProperties.encodeGzip == true
        requestProperties.followRedirects == false
        requestProperties.form == [bar: 'baz']
        requestProperties.headers == [hi: 'there']
        requestProperties.ignoreInvalidSSL == true
        requestProperties.query == [good: 'bye']
        requestProperties.rawClientResponse == true
        requestProperties.readTimeout == 5000
        requestProperties.skipStatusCheck == true
        requestProperties.uri == "http://example.com"
        requestProperties.useBasicAuth == true

    }
}
