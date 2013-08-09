@rem  Copyright 1998 - 2004 Cooperative Software Systems, Inc. All Rights Reserved. 
@rem
@rem Tymeac Main Menu 

@echo off
call SET_ENV
@echo on

start javaw ^
-Djava.security.manager  ^
-Djava.naming.factory.initial=com.sun.jndi.cosnaming.CNCtxFactory ^
-Djava.naming.provider.url=iiop://%computername%:%TYMEAC_IIOP_PORT% ^
-Djava.security.policy=%TYMEAC_HOME%\Security\policy.all ^
-Djava.security.auth.login.config=config\tymeac-ssl-client.login ^
-Djavax.net.ssl.trustStore=config\tymeac.truststore ^
-cp %TYMEAC_HOME% ^
com.tymeac.client.jframe.TyMenu
