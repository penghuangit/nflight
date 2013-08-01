package com.abreqadhabra.nflight.application.server.aio;

public interface SocketServer {
        
        /**
         * @throws NetException
         */
        public void start() throws Exception;
        
        /**
         * @throws NetException
         */
        public void stop() throws Exception;
        
        /**
         * @param sessionId
         * @return Session
         */
        public Session getSession(long sessionId);
        
}
