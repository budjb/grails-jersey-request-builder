package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpFoundException extends ResponseStatusException {
    public HttpFoundException(int status, Object content, String logText) {
        super(status, content, logText)
    }
}
