@rem  Copyright 1998 - 2008 Cooperative Software Systems, Inc. All Rights Reserved. 
@rem
@rem fork-join demo
@rem You Must Edit The -cp For The Place Where The jsr166y.zip Resides

@echo off
call SET_ENV
@echo on

java ^
-Djava.security.manager  ^
-Djava.security.policy=%TYMEAC_HOME%\Security\policy.all ^
-Djava.security.auth.login.config=config\tymeac-ssl-server.login ^
-Djavax.net.ssl.trustStore=config\tymeac.truststore ^
-cp %TYMEAC_HOME%;WHERE THE ZIP RESIDES \jsr166y.zip ^
com.tymeac.demo.LongCumulateDemo2