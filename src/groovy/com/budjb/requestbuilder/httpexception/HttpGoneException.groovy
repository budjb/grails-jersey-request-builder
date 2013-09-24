package com.budjb.requestbuilder.httpexception

import com.budjb.requestbuilder.ResponseStatusException
import com.sun.jersey.api.client.ClientResponse

class HttpGoneException extends ResponseStatusException {
    HttpGoneException(int status, content, ClientResponse response, String logText) {
        super(status, content, response, logText)
    }
}
