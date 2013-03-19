package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpRequistUriTooLongException extends ResponseStatusException {
    public HttpRequistUriTooLongException(int status, Object content) {
        super(status, content)
    }
}
