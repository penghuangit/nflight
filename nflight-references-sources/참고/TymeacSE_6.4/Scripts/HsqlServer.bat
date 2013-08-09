@rem  Copyright 1998 - 2007 Cooperative Software Systems, Inc. All Rights Reserved. 
@rem
@rem Tymeac RMI Server set up 

@echo off
call SET_ENV
call SET_HSQL
@echo on

java ^
-Djava.security.manager  ^
-Djava.security.policy=%TYMEAC_HOME%\Security\policy.all ^
-Djava.security.auth.login.config=config\tymeac-ssl-server.login ^
-Djavax.net.ssl.trustStore=config\tymeac.truststore ^
-cp %TYMEAC_HOME%;%HSQLDB_PATH% ^
com.tymeac.base.RMIServer -port %TYMEAC_RMI_PORT% -edbsd com.tymeac.serveruser.HsqlEmbeddedShutdown