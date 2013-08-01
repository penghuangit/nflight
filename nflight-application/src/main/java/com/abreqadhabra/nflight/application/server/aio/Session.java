package com.abreqadhabra.nflight.application.server.aio;

public interface Session {

    public Long getId();
    
    public void init(Configure config);
    
    public void open();
    
    public void read();
    

    
}
