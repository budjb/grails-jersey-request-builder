package com.rackspace.rvi.httpexception

import com.rackspace.rvi.ResponseStatusException

class HttpUseProxyException extends ResponseStatusException {
    public HttpUseProxyException(int status, Object content, String logText) {
        super(status, content, logText)
    }
}
