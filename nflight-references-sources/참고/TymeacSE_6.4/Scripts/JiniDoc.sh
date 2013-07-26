#! /bin/sh
#/*
# 
# Copyright 1998 - 2004 Cooperative Software Systems, Inc. All Rights Reserved. 
# 
# Jini Document Service
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
com.tymeac.demo.jini2.base.TyDemoJiniDocService & \
-config config\tymeac-jini-ssl-server.config com.tymeac.TymeacServer
