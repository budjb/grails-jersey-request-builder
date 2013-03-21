package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpForbiddenException extends ResponseStatusException {
    public HttpForbiddenException(int status, Object content, String logText) {
        super(status, content, logText)
    }
}
