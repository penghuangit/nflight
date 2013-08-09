@rem  Copyright 1998 - 2004 Cooperative Software Systems, Inc. All Rights Reserved. 
@rem
@rem Custom Socket Factory Tymeac Server Start up

@echo off
call SET_ENV
@echo on

start java ^
-Djava.security.manager ^  -Djava.security.policy=%TYMEAC_HOME%\Security\policy.all  ^
-Djava.security.auth.login.config=config\tymeac-ssl-server.login ^
-Djavax.net.ssl.trustStore=config\tymeac.truststore ^
-cp %TYMEAC_HOME% ^
com.tymeac.demo.CSFServer -port %TYMEAC_RMI_PORT% -s