package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpRequestedRangeNotSatisfiableException extends ResponseStatusException {
    public HttpRequestedRangeNotSatisfiableException(int status, Object content) {
        super(status, content)
    }
}
