package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpInternalServerErrorException extends ResponseStatusException {
    public HttpInternalServerErrorException(int status, Object content, String logText) {
        super(status, content, logText)
    }
}
