java -cp ..\nflight-server\target\nflight-server\0.0.1-SNAPSHOT\nflight-server-0.0.1-SNAPSHOT.jar;target\nflight-common\0.0.1-SNAPSHOT\nflight-common-0.0.1-SNAPSHOT.jar com.abreqadhabra.nflight.Boot --port 9999 --host 127.0.0.1 --service rmiserver:com.abreqadhabra.nflight.server.rmi.NFlightServerImpl;shutdown

pause