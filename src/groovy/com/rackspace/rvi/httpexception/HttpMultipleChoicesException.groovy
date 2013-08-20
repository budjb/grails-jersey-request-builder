package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException
import com.sun.jersey.api.client.ClientResponse

class HttpMultipleChoicesException extends ResponseStatusException {
    public HttpMultipleChoicesException(int status, Object content, ClientResponse response, String logText) {
        super(status, content, response, logText)
    }
}
