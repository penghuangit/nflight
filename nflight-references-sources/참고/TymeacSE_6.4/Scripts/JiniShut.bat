@rem  Copyright 1998 - 2004 Cooperative Software Systems, Inc. All Rights Reserved. 
@rem
@rem Jini Client shut down

@echo off
call SET_ENV
@echo on

java ^
-Djava.security.manager ^  -Djava.security.policy=%TYMEAC_HOME%\Security\policy.all  ^
-Djava.security.auth.login.config=config\tymeac-ssl-client.login   ^
-Djavax.net.ssl.trustStore=config\tymeac.truststore ^
-Djava.rmi.server.codebase=http://%computername%:%TYMEAC_HTTP_PORT%/ ^
com.tymeac.jini.client.JiniClientShutDown ^
-config  config\tymeac-jrmp-client.config -secure
