package com.budjb.requestbuilder.httpexception

import com.budjb.requestbuilder.ResponseStatusException
import com.sun.jersey.api.client.ClientResponse

class HttpForbiddenException extends ResponseStatusException {
    public HttpForbiddenException(int status, Object content, ClientResponse response, String logText) {
        super(status, content, response, logText)
    }
}
