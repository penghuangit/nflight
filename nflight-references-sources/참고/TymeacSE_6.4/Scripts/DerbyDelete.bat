@rem  Copyright 2007 Cooperative Software Systems, Inc. All Rights Reserved. 
@rem
@rem 

@echo off
call SET_ENV
call SET_DERBY
@echo on

java ^
-Djava.security.manager  ^
-Djava.security.policy=%TYMEAC_HOME%\Security\policy.all ^
-Djava.naming.factory.initial=com.sun.jndi.cosnaming.CNCtxFactory ^
-Djava.naming.provider.url=iiop://%computername%:%TYMEAC_IIOP_PORT% ^
-Djava.security.auth.login.config=config\tymeac-ssl-client.login ^
-Djavax.net.ssl.trustStore=config\tymeac.truststore ^
-cp %TYMEAC_HOME%;%DERBY_PATH% ^
com.tymeac.test.DerbyDelete -embedded