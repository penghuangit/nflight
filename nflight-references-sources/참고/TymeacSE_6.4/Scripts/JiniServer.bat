@rem  Copyright 1998 - 2004 Cooperative Software Systems, Inc. All Rights Reserved. 
@rem
@rem Jini Tymeac Server Start up

@echo off
call SET_ENV
@echo on

c:\"program files\java\jdk1.5.0_07\jre\bin"\java -server ^
-Djava.security.manager ^  -Djava.security.policy=%TYMEAC_HOME%\Security\policy.all  ^
-Djava.security.auth.login.config=config\tymeac-ssl-server.login   ^
-Djavax.net.ssl.trustStore=config\tymeac.truststore ^
-Djava.rmi.server.codebase=http://%computername%:%TYMEAC_HTTP_PORT%/ ^
com.tymeac.jini.base.JiniServer ^
-config config\tymeac-jini-ssl-server.config com.tymeac.TymeacServer