java -cp target\nflight-core\0.0.1-SNAPSHOT\nflight-core-0.0.1-SNAPSHOT.jar;..\nflight-common\target\nflight-common\0.0.1-SNAPSHOT\nflight-common-0.0.1-SNAPSHOT.jar;..\nflight-server\target\nflight-server\0.0.1-SNAPSHOT\nflight-server-0.0.1-SNAPSHOT.jar com.abreqadhabra.nflight.Boot --host localhost --port 9999 --service rmiserver:com.abreqadhabra.nflight.server.rmi.RMIServer;startup

pause