package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpExpectationFailedException extends ResponseStatusException {
    public HttpExpectationFailedException(int status, Object content, String logText) {
        super(status, content, logText)
    }
}
