package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpMovedPermanentlyException extends ResponseStatusException {
    public HttpMovedPermanentlyException(int status, Object content) {
        super(status, content)
    }
}
