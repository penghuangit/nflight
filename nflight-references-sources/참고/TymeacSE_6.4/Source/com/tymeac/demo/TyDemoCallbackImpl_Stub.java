package com.tymeac.demo;

// Stub class generated by rmic, do not edit.
// Contents subject to change without notice.

public final class TyDemoCallbackImpl_Stub
	extends java.rmi.server.RemoteStub
	implements com.tymeac.demo.TyDemoCallback
{
	private static final long serialVersionUID = 2;
	
	private static java.lang.reflect.Method $method_giveBack_0;
	
	static {
	try {
	    $method_giveBack_0 = com.tymeac.demo.TyDemoCallback.class.getMethod("giveBack", new java.lang.Class[] {java.lang.Object.class});
	} catch (java.lang.NoSuchMethodException e) {
	    throw new java.lang.NoSuchMethodError(
		"stub class initialization failed");
	}
	}
	
    // constructors
    public TyDemoCallbackImpl_Stub(java.rmi.server.RemoteRef ref) {
	super(ref);
    }
    // methods from remote interfaces
    
    // implementation of giveBack(Object)
    public void giveBack(java.lang.Object $param_Object_1)
	throws java.rmi.RemoteException
    {
	try {
	    ref.invoke(this, $method_giveBack_0, new java.lang.Object[] {$param_Object_1}, -2482465561071277234L);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
}
