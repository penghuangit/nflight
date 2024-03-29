// Stub class generated by rmic, do not edit.
// Contents subject to change without notice.

package com.tymeac.base;

public final class TymeacStandardImpl_Stub
    extends java.rmi.server.RemoteStub
    implements com.tymeac.base.TymeacInterface
{
    private static final long serialVersionUID = 2;
    
    private static java.lang.reflect.Method $method_alterSvrOptions_0;
    private static java.lang.reflect.Method $method_asyncRequest_1;
    private static java.lang.reflect.Method $method_cancelSyncReq_2;
    private static java.lang.reflect.Method $method_diagnoseRequest_3;
    private static java.lang.reflect.Method $method_func1Request_4;
    private static java.lang.reflect.Method $method_idStatus1Request_5;
    private static java.lang.reflect.Method $method_newCopyRequest_6;
    private static java.lang.reflect.Method $method_newRTMaint_7;
    private static java.lang.reflect.Method $method_newRunTimeFunctions_8;
    private static java.lang.reflect.Method $method_overallRequest_9;
    private static java.lang.reflect.Method $method_que1Request_10;
    private static java.lang.reflect.Method $method_que2Request_11;
    private static java.lang.reflect.Method $method_que3Request_12;
    private static java.lang.reflect.Method $method_que4Request_13;
    private static java.lang.reflect.Method $method_que5Request_14;
    private static java.lang.reflect.Method $method_que6Request_15;
    private static java.lang.reflect.Method $method_shutRequest_16;
    private static java.lang.reflect.Method $method_shutRequest_17;
    private static java.lang.reflect.Method $method_stall1Request_18;
    private static java.lang.reflect.Method $method_stall2Request_19;
    private static java.lang.reflect.Method $method_stall3Request_20;
    private static java.lang.reflect.Method $method_stall4Request_21;
    private static java.lang.reflect.Method $method_stats1Request_22;
    private static java.lang.reflect.Method $method_syncRequest_23;
    
    static {
	try {
	    $method_alterSvrOptions_0 = com.tymeac.base.TymeacInterface.class.getMethod("alterSvrOptions", new java.lang.Class[] {com.tymeac.base.TyAltSvrElements.class});
	    $method_asyncRequest_1 = com.tymeac.base.TymeacInterface.class.getMethod("asyncRequest", new java.lang.Class[] {com.tymeac.base.TymeacParm.class});
	    $method_cancelSyncReq_2 = com.tymeac.base.TymeacInterface.class.getMethod("cancelSyncReq", new java.lang.Class[] {com.tymeac.base.CancelParm.class});
	    $method_diagnoseRequest_3 = com.tymeac.base.TymeacInterface.class.getMethod("diagnoseRequest", new java.lang.Class[] {com.tymeac.base.TyParm.class});
	    $method_func1Request_4 = com.tymeac.base.TymeacInterface.class.getMethod("func1Request", new java.lang.Class[] {java.lang.String.class});
	    $method_idStatus1Request_5 = com.tymeac.base.TymeacInterface.class.getMethod("idStatus1Request", new java.lang.Class[] {long.class, long.class, int.class});
	    $method_newCopyRequest_6 = com.tymeac.base.TymeacInterface.class.getMethod("newCopyRequest", new java.lang.Class[] {java.lang.String.class, java.lang.String.class});
	    $method_newRTMaint_7 = com.tymeac.base.TymeacInterface.class.getMethod("newRTMaint", new java.lang.Class[] {int.class});
	    $method_newRunTimeFunctions_8 = com.tymeac.base.TymeacInterface.class.getMethod("newRunTimeFunctions", new java.lang.Class[] {int.class, java.lang.String.class, java.lang.String.class});
	    $method_overallRequest_9 = com.tymeac.base.TymeacInterface.class.getMethod("overallRequest", new java.lang.Class[] {int.class});
	    $method_que1Request_10 = com.tymeac.base.TymeacInterface.class.getMethod("que1Request", new java.lang.Class[] {java.lang.String.class});
	    $method_que2Request_11 = com.tymeac.base.TymeacInterface.class.getMethod("que2Request", new java.lang.Class[] {com.tymeac.base.TyQueElements.class});
	    $method_que3Request_12 = com.tymeac.base.TymeacInterface.class.getMethod("que3Request", new java.lang.Class[] {java.lang.String.class});
	    $method_que4Request_13 = com.tymeac.base.TymeacInterface.class.getMethod("que4Request", new java.lang.Class[] {java.lang.String.class, int.class});
	    $method_que5Request_14 = com.tymeac.base.TymeacInterface.class.getMethod("que5Request", new java.lang.Class[] {java.lang.String.class});
	    $method_que6Request_15 = com.tymeac.base.TymeacInterface.class.getMethod("que6Request", new java.lang.Class[] {java.lang.String.class});
	    $method_shutRequest_16 = com.tymeac.base.TymeacInterface.class.getMethod("shutRequest", new java.lang.Class[] {});
	    $method_shutRequest_17 = com.tymeac.base.TymeacInterface.class.getMethod("shutRequest", new java.lang.Class[] {boolean.class});
	    $method_stall1Request_18 = com.tymeac.base.TymeacInterface.class.getMethod("stall1Request", new java.lang.Class[] {});
	    $method_stall2Request_19 = com.tymeac.base.TymeacInterface.class.getMethod("stall2Request", new java.lang.Class[] {long.class});
	    $method_stall3Request_20 = com.tymeac.base.TymeacInterface.class.getMethod("stall3Request", new java.lang.Class[] {long.class});
	    $method_stall4Request_21 = com.tymeac.base.TymeacInterface.class.getMethod("stall4Request", new java.lang.Class[] {long.class});
	    $method_stats1Request_22 = com.tymeac.base.TymeacInterface.class.getMethod("stats1Request", new java.lang.Class[] {});
	    $method_syncRequest_23 = com.tymeac.base.TymeacInterface.class.getMethod("syncRequest", new java.lang.Class[] {com.tymeac.base.TymeacParm.class});
	} catch (java.lang.NoSuchMethodException e) {
	    throw new java.lang.NoSuchMethodError(
		"stub class initialization failed");
	}
    }
    
    // constructors
    public TymeacStandardImpl_Stub(java.rmi.server.RemoteRef ref) {
	super(ref);
    }
    
    // methods from remote interfaces
    
    // implementation of alterSvrOptions(TyAltSvrElements)
    public com.tymeac.base.TyAltSvrElements alterSvrOptions(com.tymeac.base.TyAltSvrElements $param_TyAltSvrElements_1)
	throws java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_alterSvrOptions_0, new java.lang.Object[] {$param_TyAltSvrElements_1}, -773013257747382130L);
	    return ((com.tymeac.base.TyAltSvrElements) $result);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of asyncRequest(TymeacParm)
    public java.lang.Object[] asyncRequest(com.tymeac.base.TymeacParm $param_TymeacParm_1)
	throws java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_asyncRequest_1, new java.lang.Object[] {$param_TymeacParm_1}, 5271632224096044053L);
	    return ((java.lang.Object[]) $result);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of cancelSyncReq(CancelParm)
    public int cancelSyncReq(com.tymeac.base.CancelParm $param_CancelParm_1)
	throws java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_cancelSyncReq_2, new java.lang.Object[] {$param_CancelParm_1}, 1819598590250514980L);
	    return ((java.lang.Integer) $result).intValue();
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of diagnoseRequest(TyParm)
    public java.lang.String diagnoseRequest(com.tymeac.base.TyParm $param_TyParm_1)
	throws java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_diagnoseRequest_3, new java.lang.Object[] {$param_TyParm_1}, -6676683973130083305L);
	    return ((java.lang.String) $result);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of func1Request(String)
    public java.lang.String[] func1Request(java.lang.String $param_String_1)
	throws java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_func1Request_4, new java.lang.Object[] {$param_String_1}, -3276828540821298820L);
	    return ((java.lang.String[]) $result);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of idStatus1Request(long, long, int)
    public int idStatus1Request(long $param_long_1, long $param_long_2, int $param_int_3)
	throws java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_idStatus1Request_5, new java.lang.Object[] {new java.lang.Long($param_long_1), new java.lang.Long($param_long_2), new java.lang.Integer($param_int_3)}, 4929667035821305914L);
	    return ((java.lang.Integer) $result).intValue();
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of newCopyRequest(String, String)
    public int newCopyRequest(java.lang.String $param_String_1, java.lang.String $param_String_2)
	throws java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_newCopyRequest_6, new java.lang.Object[] {$param_String_1, $param_String_2}, -4011426375690164228L);
	    return ((java.lang.Integer) $result).intValue();
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of newRTMaint(int)
    public com.tymeac.base.TyRunTimeElements newRTMaint(int $param_int_1)
	throws java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_newRTMaint_7, new java.lang.Object[] {new java.lang.Integer($param_int_1)}, 3460674108370480099L);
	    return ((com.tymeac.base.TyRunTimeElements) $result);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of newRunTimeFunctions(int, String, String)
    public int newRunTimeFunctions(int $param_int_1, java.lang.String $param_String_2, java.lang.String $param_String_3)
	throws java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_newRunTimeFunctions_8, new java.lang.Object[] {new java.lang.Integer($param_int_1), $param_String_2, $param_String_3}, -1648164315435939021L);
	    return ((java.lang.Integer) $result).intValue();
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of overallRequest(int)
    public com.tymeac.base.TyOverallObj overallRequest(int $param_int_1)
	throws java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_overallRequest_9, new java.lang.Object[] {new java.lang.Integer($param_int_1)}, -2513360856486489512L);
	    return ((com.tymeac.base.TyOverallObj) $result);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of que1Request(String)
    public com.tymeac.base.TyQueElements que1Request(java.lang.String $param_String_1)
	throws java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_que1Request_10, new java.lang.Object[] {$param_String_1}, 5472430503274645186L);
	    return ((com.tymeac.base.TyQueElements) $result);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of que2Request(TyQueElements)
    public int que2Request(com.tymeac.base.TyQueElements $param_TyQueElements_1)
	throws java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_que2Request_11, new java.lang.Object[] {$param_TyQueElements_1}, 1423232015124060632L);
	    return ((java.lang.Integer) $result).intValue();
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of que3Request(String)
    public java.lang.String[] que3Request(java.lang.String $param_String_1)
	throws java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_que3Request_12, new java.lang.Object[] {$param_String_1}, 3696198378445528271L);
	    return ((java.lang.String[]) $result);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of que4Request(String, int)
    public int que4Request(java.lang.String $param_String_1, int $param_int_2)
	throws java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_que4Request_13, new java.lang.Object[] {$param_String_1, new java.lang.Integer($param_int_2)}, 2163546933638397700L);
	    return ((java.lang.Integer) $result).intValue();
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of que5Request(String)
    public int que5Request(java.lang.String $param_String_1)
	throws java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_que5Request_14, new java.lang.Object[] {$param_String_1}, 3349181642490825104L);
	    return ((java.lang.Integer) $result).intValue();
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of que6Request(String)
    public com.tymeac.base.TyWLElements[] que6Request(java.lang.String $param_String_1)
	throws java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_que6Request_15, new java.lang.Object[] {$param_String_1}, 7188262717977997910L);
	    return ((com.tymeac.base.TyWLElements[]) $result);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of shutRequest()
    public java.lang.String shutRequest()
	throws java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_shutRequest_16, null, -5010161552643161364L);
	    return ((java.lang.String) $result);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of shutRequest(boolean)
    public java.lang.String shutRequest(boolean $param_boolean_1)
	throws java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_shutRequest_17, new java.lang.Object[] {($param_boolean_1 ? java.lang.Boolean.TRUE : java.lang.Boolean.FALSE)}, -472729798129736796L);
	    return ((java.lang.String) $result);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of stall1Request()
    public java.lang.String[] stall1Request()
	throws java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_stall1Request_18, null, 6025857080891410987L);
	    return ((java.lang.String[]) $result);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of stall2Request(long)
    public int stall2Request(long $param_long_1)
	throws java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_stall2Request_19, new java.lang.Object[] {new java.lang.Long($param_long_1)}, 4520400411798626060L);
	    return ((java.lang.Integer) $result).intValue();
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of stall3Request(long)
    public java.lang.String[] stall3Request(long $param_long_1)
	throws java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_stall3Request_20, new java.lang.Object[] {new java.lang.Long($param_long_1)}, 4910302371480807590L);
	    return ((java.lang.String[]) $result);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of stall4Request(long)
    public int stall4Request(long $param_long_1)
	throws java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_stall4Request_21, new java.lang.Object[] {new java.lang.Long($param_long_1)}, -453243582017128563L);
	    return ((java.lang.Integer) $result).intValue();
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of stats1Request()
    public int stats1Request()
	throws java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_stats1Request_22, null, 7023452796284733977L);
	    return ((java.lang.Integer) $result).intValue();
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
    
    // implementation of syncRequest(TymeacParm)
    public java.lang.Object[] syncRequest(com.tymeac.base.TymeacParm $param_TymeacParm_1)
	throws java.rmi.RemoteException
    {
	try {
	    Object $result = ref.invoke(this, $method_syncRequest_23, new java.lang.Object[] {$param_TymeacParm_1}, -4226563213980363395L);
	    return ((java.lang.Object[]) $result);
	} catch (java.lang.RuntimeException e) {
	    throw e;
	} catch (java.rmi.RemoteException e) {
	    throw e;
	} catch (java.lang.Exception e) {
	    throw new java.rmi.UnexpectedException("undeclared checked exception", e);
	}
    }
}
