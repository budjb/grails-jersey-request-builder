package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpMethodNotAllowedException extends ResponseStatusException {
    public HttpMethodNotAllowedException(int status, Object content, String logText) {
        super(status, content, logText)
    }
}
