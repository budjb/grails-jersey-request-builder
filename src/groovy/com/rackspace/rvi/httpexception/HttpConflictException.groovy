package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpConflictException extends ResponseStatusException {
    public HttpConflictException(int status, Object content) {
        super(status, content)
    }
}
