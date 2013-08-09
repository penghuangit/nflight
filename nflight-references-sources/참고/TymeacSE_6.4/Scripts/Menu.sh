##! /bin/sh
#/*
# 
# Copyright 1998 - 2004 Cooperative Software Systems, Inc. All Rights Reserved. 
# 
# Tymeac Client Main Menu
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
-cp %TYMEAC_HOME% \
com.tymeac.demo.jframe.TyMenu &
