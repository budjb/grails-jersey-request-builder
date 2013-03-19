package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpNotAcceptableException extends ResponseStatusException {
    public HttpNotAcceptableException(int status, Object content) {
        super(status, content)
    }
}
