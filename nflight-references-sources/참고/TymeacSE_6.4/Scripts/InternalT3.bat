@rem  Copyright 1998 - 2004 Cooperative Software Systems, Inc. All Rights Reserved. 
@rem
@rem Internal Tymeac Server Demonstation Start up

@echo off
call SET_ENV
@echo on

start java ^
-Djava.security.manager ^  -Djava.security.policy=%TYMEAC_HOME%\Security\policy.all  ^
-Djava.security.auth.login.config=config\tymeac-ssl-server.login ^
-Djavax.net.ssl.trustStore=config\tymeac.truststore ^
-cp %TYMEAC_HOME%;%TYMEAC_HOME%\lib\swing-layout-1.0.jar ^
com.tymeac.demo.jframe.TyDemoInternalTest3