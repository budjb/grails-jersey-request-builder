package com.budjb.requestbuilder.httpexception

import com.budjb.requestbuilder.ResponseStatusException
import com.sun.jersey.api.client.ClientResponse

class HttpTemporaryRedirectException extends ResponseStatusException {
    HttpTemporaryRedirectException(int status, content, ClientResponse response, String logText) {
        super(status, content, response, logText)
    }
}
