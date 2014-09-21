grails $UPGRADE --non-interactive && grails test-app -Dserver.port=8099 --refresh-dependencies --non-interactive --stacktrace && grails package-plugin
