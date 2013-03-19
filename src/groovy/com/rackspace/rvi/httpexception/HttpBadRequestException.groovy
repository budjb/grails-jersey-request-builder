package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpBadRequestException extends ResponseStatusException {
    public HttpBadRequestException(int status, Object content) {
        super(status, content)
    }
}
