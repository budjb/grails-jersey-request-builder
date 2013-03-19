package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpServiceUnavailableException extends ResponseStatusException {
    public HttpServiceUnavailableException(int status, Object content) {
        super(status, content)
    }
}
