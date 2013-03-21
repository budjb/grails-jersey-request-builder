package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpRequistUriTooLongException extends ResponseStatusException {
    public HttpRequistUriTooLongException(int status, Object content, String logText) {
        super(status, content, logText)
    }
}
