@rem  Copyright 1998 - 2004 Cooperative Software Systems, Inc. All Rights Reserved. 
@rem
@rem Tymeac Demo T3

@echo off
call SET_ENV
@echo on

start javaw ^
-Djava.security.manager  ^
-Djava.security.policy=%TYMEAC_HOME%\Security\policy.all ^
-Djava.naming.factory.initial=com.sun.jndi.cosnaming.CNCtxFactory ^
-Djava.naming.provider.url=iiop://%computername%:%TYMEAC_IIOP_PORT% ^
-Djava.security.auth.login.config=config\tymeac-ssl-client.login ^
-Djavax.net.ssl.trustStore=config\tymeac.truststore ^
-cp %TYMEAC_HOME%;%TYMEAC_HOME%\lib\swing-layout-1.0.jar ^
com.tymeac.demo.jframe.TyDemoT3