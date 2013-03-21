package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpMultipleChoicesException extends ResponseStatusException {
    public HttpMultipleChoicesException(int status, Object content, String logText) {
        super(status, content, logText)
    }
}
