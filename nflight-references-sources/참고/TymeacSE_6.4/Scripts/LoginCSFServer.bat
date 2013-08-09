@rem  Copyright 1998 - 2004 Cooperative Software Systems, Inc. All Rights Reserved. 
@rem
@rem Tymeac RMI Server set up 

@echo off
call SET_ENV
@echo on

start c:\"program files\java\jdk1.5.0_07\jre\bin"\java -server ^
-Djava.security.manager  ^
-Djava.security.policy=%TYMEAC_HOME%\Security\policy.all ^
-Djava.security.auth.login.config=config\tymeac-ssl-server.login ^
-Djavax.net.ssl.trustStore=config\tymeac.truststore ^
-cp %TYMEAC_HOME% ^
com.tymeac.base.LoginCSFServer -port %TYMEAC_RMI_PORT% -s