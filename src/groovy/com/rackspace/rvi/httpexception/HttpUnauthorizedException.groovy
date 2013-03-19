package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpUnauthorizedException extends ResponseStatusException {
    public HttpUnauthorizedException(int status, Object content) {
        super(status, content)
    }
}
