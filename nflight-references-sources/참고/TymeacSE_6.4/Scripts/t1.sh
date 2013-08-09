##! /bin/sh
#/*
# 
# Copyright 1998 - 2004 Cooperative Software Systems, Inc. All Rights Reserved. 
# 
# Tymeac Demo T1
#
#*/

# Get the environment variables setup
. scripts/setenv.sh

set -ex      

java  \
-Djava.security.manager \
-Djava.security.policy=%TYMEAC_HOME%/Security/policy.all \
-Djava.security.auth.login.config=config/tymeac-ssl-client.login \
-Djavax.net.ssl.trustStore=config/tymeac.truststore \
-Djava.naming.factory.initial=com.sun.jndi.cosnaming.CNCtxFactory \
-Djava.naming.provider.url=iiop://%computername%:%TYMEAC_IIOP_PORT% \
-cp %TYMEAC_HOME%;%TYMEAC_HOME%/lib/swing-layout-1.0.4.jar \
com.tymeac.demo.jframe.TyDemoT1 &
