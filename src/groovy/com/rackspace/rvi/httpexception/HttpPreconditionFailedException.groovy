package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpPreconditionFailedException extends ResponseStatusException {
    public HttpPreconditionFailedException(int status, Object content) {
        super(status, content)
    }
}
