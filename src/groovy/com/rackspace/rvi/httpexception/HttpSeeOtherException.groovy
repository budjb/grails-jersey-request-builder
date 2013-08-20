package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException
import com.sun.jersey.api.client.ClientResponse

class HttpSeeOtherException extends ResponseStatusException {
    public HttpSeeOtherException(int status, Object content, ClientResponse response, String logText) {
        super(status, content, response, logText)
    }
}
