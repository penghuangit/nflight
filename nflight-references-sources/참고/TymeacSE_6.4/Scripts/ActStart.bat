@rem  Copyright 1998 - 2008 Cooperative Software Systems, Inc. All Rights Reserved. 
@rem
@rem Activatable RMI Tymeac Server Start up

@echo off
call SET_ENV
@echo on

java -Djava.security.manager ^
-Djava.security.policy=%TYMEAC_HOME%\Security\rmid_policy.all ^
-Djava.security.auth.login.config=config\tymeac-ssl-server.login ^
-Djavax.net.ssl.trustStore=config\tymeac.truststore ^
-cp %TYMEAC_HOME% ^
com.tymeac.demo.TyDemoBaseRMI
