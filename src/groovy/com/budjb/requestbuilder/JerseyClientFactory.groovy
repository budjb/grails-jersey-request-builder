package com.budjb.requestbuilder

import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.config.ClientConfig

class JerseyClientFactory {
    /**
     * Create a Jersey client.
     *
     * @return
     */
    public Client createClient() {
        return Client.create()
    }

    /**
     * Create a Jersey client with the given configuration.
     *
     * @param config
     * @return
     */
    public Client createClient(ClientConfig config) {
        return Client.create(config)
    }

    /**
     * Creates a new request delegate.
     *
     * @return
     */
    public JerseyRequestDelegate createDelegate() {
        return new JerseyRequestDelegate(this)
    }
}
