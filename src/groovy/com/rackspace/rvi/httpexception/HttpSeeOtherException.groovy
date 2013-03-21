package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpSeeOtherException extends ResponseStatusException {
    public HttpSeeOtherException(int status, Object content, String logText) {
        super(status, content, logText)
    }
}
