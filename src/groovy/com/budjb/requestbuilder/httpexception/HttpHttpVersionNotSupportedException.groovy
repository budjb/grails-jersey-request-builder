package com.budjb.requestbuilder.httpexception

import com.budjb.requestbuilder.ResponseStatusException
import com.sun.jersey.api.client.ClientResponse

class HttpHttpVersionNotSupportedException extends ResponseStatusException {
    HttpHttpVersionNotSupportedException(int status, content, ClientResponse response, String logText) {
        super(status, content, response, logText)
    }
}
