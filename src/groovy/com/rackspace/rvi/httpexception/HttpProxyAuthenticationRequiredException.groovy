package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpProxyAuthenticationRequiredException extends ResponseStatusException {
    public HttpProxyAuthenticationRequiredException(int status, Object content, String logText) {
        super(status, content, logText)
    }
}
