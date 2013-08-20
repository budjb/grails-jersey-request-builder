package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException
import com.sun.jersey.api.client.ClientResponse

class HttpNotAcceptableException extends ResponseStatusException {
    public HttpNotAcceptableException(int status, Object content, ClientResponse response, String logText) {
        super(status, content, response, logText)
    }
}
