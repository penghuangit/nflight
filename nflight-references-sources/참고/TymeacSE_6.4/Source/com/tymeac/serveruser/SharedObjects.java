package com.tymeac.serveruser;

public class SharedObjects {  

  private static volatile int variable_1 = 0;
  
    public static Object main(Object args[])
          throws  java.lang.Throwable    { 
       
      try {
        if  (getVari() == 0) { 
          
            setVari(1);
            return null;      
        }
        
        setVari(Integer.parseInt((String)args[0]));
        
        return new Integer(getVari()); 
    
      }     
      catch (java.lang.Throwable e) {
        
        throw  e; 
      }   
    } 
    
    public static int getVari() {return variable_1; }
    public static synchronized void setVari(int vari) {variable_1 = vari;}
} 
