@rem  Copyright 1998 - 2004 Cooperative Software Systems, Inc. All Rights Reserved. 
@rem
@rem Jini Activation Stage 2

@echo off
call SET_ENV
@echo on

start java ^
-Djava.security.manager ^  -Djava.security.policy=%TYMEAC_HOME%\Security\policy.all  ^
-Djava.security.auth.login.config=config\tymeac-ssl-server.login   ^
-Djavax.net.ssl.trustStore=config\tymeac.truststore ^
-Djava.rmi.server.codebase=http://%computername%:%TYMEAC_HTTP_PORT%/ ^
com.tymeac.jini.base.JiniActivationCreate2 ^
-config  config\tymeac-jini-activatable-ssl-server.config com.tymeac.TymeacServer
