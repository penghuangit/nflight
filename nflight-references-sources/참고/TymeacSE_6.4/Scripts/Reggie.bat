@rem  Copyright 1998 - 2004 Cooperative Software Systems, Inc. All Rights Reserved. 
@rem
@rem Reggie

@echo off
call SET_ENV
call SET_JINI
@echo on

%jini_home%\lib\reggie.jar config\jrmp-reggie.config com.sun.jini.reggie 