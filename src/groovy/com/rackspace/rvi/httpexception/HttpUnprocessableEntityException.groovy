package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException
import com.sun.jersey.api.client.ClientResponse

class HttpUnprocessableEntityException extends ResponseStatusException {
    public HttpUnprocessableEntityException(int status, Object content, ClientResponse response, String logText) {
        super(status, content, response, logText)
    }
}
