package com.budjb.requestbuilder

import spock.lang.Specification

class JerseyClientFactorySpec extends Specification {
    def 'When createDelegate() is called, a new delgate is injected with the factory'() {
        setup:
        JerseyClientFactory factory = new JerseyClientFactory()

        when:
        JerseyRequestDelegate delegate = factory.createDelegate()

        then:
        delegate.jerseyClientFactory == factory
    }
}
