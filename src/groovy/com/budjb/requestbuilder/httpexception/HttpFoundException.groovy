package com.budjb.requestbuilder.httpexception

import com.budjb.requestbuilder.ResponseStatusException
import com.sun.jersey.api.client.ClientResponse

class HttpFoundException extends ResponseStatusException {
    HttpFoundException(int status, content, ClientResponse response, String logText) {
        super(status, content, response, logText)
    }
}
