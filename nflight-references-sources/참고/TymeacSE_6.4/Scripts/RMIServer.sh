#! /bin/sh
#/*
# 
# Copyright 1998 - 2004 Cooperative Software Systems, Inc. All Rights Reserved. 
# 
# RMI Tymeac Server Start up
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
com.tymeac.base.RMIServer -port %TYMEAC_RMI_PORT% -s &
