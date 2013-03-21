package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpUnsupportedMediaTypeException extends ResponseStatusException {
    public HttpUnsupportedMediaTypeException(int status, Object content, String logText) {
        super(status, content, logText)
    }
}
