package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpMovedPermanentlyException extends ResponseStatusException {
    public HttpMovedPermanentlyException(int status, Object content, String logText) {
        super(status, content, logText)
    }
}
