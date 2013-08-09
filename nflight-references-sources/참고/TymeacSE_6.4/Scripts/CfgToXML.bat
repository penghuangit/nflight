@rem  Copyright 2007 Cooperative Software Systems, Inc. All Rights Reserved. 
@rem
@rem Tymeac Main Menu 

@echo off
call SET_ENV
@echo on

java ^
-cp %TYMEAC_HOME% ^
ConvertCfg
