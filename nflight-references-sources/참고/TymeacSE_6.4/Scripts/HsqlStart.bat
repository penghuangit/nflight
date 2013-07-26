@rem  Copyright 1998 - 2004 Cooperative Software Systems, Inc. All Rights Reserved. 
@rem
@rem HypersonicSQL

@echo off
call SET_HSQL
@echo on

java ^
-cp %HSQLDB_PATH% ^
org.hsqldb.Server -database.0 file:TYMEAC -dbname.0 xdb
