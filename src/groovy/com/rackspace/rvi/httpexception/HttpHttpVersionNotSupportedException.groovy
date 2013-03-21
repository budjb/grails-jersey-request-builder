package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpHttpVersionNotSupportedException extends ResponseStatusException {
    public HttpHttpVersionNotSupportedException(int status, Object content, String logText) {
        super(status, content, logText)
    }
}
