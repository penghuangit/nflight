package com.abreqadhabra.nflight.common.concurrency.executor.test;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.abreqadhabra.nflight.common.logging.LoggingHelper;

public class TestTask implements Runnable {
	private static Class<TestTask> THIS_CLAZZ = TestTask.class;
	private static Logger LOGGER = LoggingHelper.getLogger(THIS_CLAZZ);
	
    String taskName;
 
    public TestTask() {
    }
 
    public TestTask(String taskName) {
        this.taskName = taskName;
    }
 
    public void run() {
		String METHOD_NAME = Thread.currentThread().getStackTrace()[1]
				.getMethodName();
		
        try {
        	LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,this.taskName + " : is started.");
            Thread.sleep(10000);
            LOGGER.logp(Level.FINER, THIS_CLAZZ.getSimpleName(), METHOD_NAME,this.taskName + " : is completed.");
        } catch (InterruptedException e) {
			LOGGER.logp(Level.SEVERE, THIS_CLAZZ.getSimpleName(), METHOD_NAME, this.taskName + " : is not completed!");
            e.printStackTrace();
        }
    }
 
    @Override
    public String toString() {
        return (getTaskName());
    }
 
    public String getTaskName() {
        return taskName;
    }
 
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}