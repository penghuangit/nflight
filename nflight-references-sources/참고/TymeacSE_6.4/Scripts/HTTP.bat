@rem  Copyright 1998 - 2004 Cooperative Software Systems, Inc. All Rights Reserved. 
@rem
@rem Jini Client shut down

@echo off
call SET_ENV
call SET_JINI
@echo on

%jini_home%\lib\tools.jar -port %TYMEAC_HTTP_PORT%