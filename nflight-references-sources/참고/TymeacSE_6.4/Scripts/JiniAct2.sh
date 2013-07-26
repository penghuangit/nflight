#! /bin/sh
#/*
# 
# Copyright 1998 - 2004 Cooperative Software Systems, Inc. All Rights Reserved. 
# 
# Jini Activatable Tymeac Server stage 2
#
#*/

# Get the environment variables setup
. scripts/setenv.sh

set -ex      

java  \
-Djava.security.manager \
-Djava.security.policy=%TYMEAC_HOME%/Security/policy.all \
-Djava.security.auth.login.config=config/tymeac-ssl-server.login \
-Djavax.net.ssl.trustStore=config/tymeac.truststore \  
-cp %TYMEAC_HOME% \
-Djava.rmi.server.codebase=http://%computername%:%TYMEAC_HTTP_PORT%/ \
com.tymeac.jini.base.JiniActivationCreate2 & \
-config  config/tymeac-jini-activatable-ssl-server.config com.tymeac.TymeacServer
