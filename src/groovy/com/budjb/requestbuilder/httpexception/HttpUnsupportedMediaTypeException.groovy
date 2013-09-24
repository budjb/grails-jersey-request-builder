package com.budjb.requestbuilder.httpexception

import com.budjb.requestbuilder.ResponseStatusException
import com.sun.jersey.api.client.ClientResponse

class HttpUnsupportedMediaTypeException extends ResponseStatusException {
    HttpUnsupportedMediaTypeException(int status, content, ClientResponse response, String logText) {
        super(status, content, response, logText)
    }
}
