@rem  Copyright 1998 - 2004 Cooperative Software Systems, Inc. All Rights Reserved. 
@rem
@rem IIOP Tymeac Server Start up

@echo off
call SET_ENV
@echo on

start java ^
-Djava.security.manager  ^-Djava.security.policy=%TYMEAC_HOME%\Security\policy.all  ^
-Djava.naming.factory.initial=com.sun.jndi.cosnaming.CNCtxFactory ^
-Djava.naming.provider.url=iiop://%computername%:%TYMEAC_IIOP_PORT% ^
-Djava.security.auth.login.config=config\tymeac-ssl-server.login ^
-Djavax.net.ssl.trustStore=config\tymeac.truststore ^
-cp %TYMEAC_HOME% ^
com.tymeac.base.IIOPServer -s