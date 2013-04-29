package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpRequestEntityTooLargeException extends ResponseStatusException {
    public HttpRequestEntityTooLargeException(int status, Object content, String logText) {
        super(status, content, logText)
    }
}