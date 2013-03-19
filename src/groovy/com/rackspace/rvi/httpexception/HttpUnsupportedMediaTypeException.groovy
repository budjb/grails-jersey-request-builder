package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpUnsupportedMediaTypeException extends ResponseStatusException {
    public HttpUnsupportedMediaTypeException(int status, Object content) {
        super(status, content)
    }
}
