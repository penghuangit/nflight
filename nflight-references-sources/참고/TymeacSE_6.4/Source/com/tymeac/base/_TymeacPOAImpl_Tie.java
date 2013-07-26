// Tie class generated by rmic, do not edit.
// Contents subject to change without notice.

package com.tymeac.base;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.rmi.CORBA.Tie;
import javax.rmi.CORBA.Util;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.ORB;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;
import org.omg.CORBA.portable.UnknownException;
import org.omg.PortableServer.Servant;


public class _TymeacPOAImpl_Tie extends Servant implements Tie {
    
    private TymeacPOAImpl target = null;
    
    private static final String[] _type_ids = {
        "RMI:com.tymeac.base.TymeacInterface:0000000000000000"
    };
    
    public void setTarget(Remote target) {
        this.target = (TymeacPOAImpl) target;
    }
    
    public Remote getTarget() {
        return target;
    }
    
    public org.omg.CORBA.Object thisObject() {
        return _this_object();
    }
    
    public void deactivate() {
        try{
        _poa().deactivate_object(_poa().servant_to_id(this));
        }catch (org.omg.PortableServer.POAPackage.WrongPolicy exception){
        
        }catch (org.omg.PortableServer.POAPackage.ObjectNotActive exception){
        
        }catch (org.omg.PortableServer.POAPackage.ServantNotActive exception){
        
        }
    }
    
    public ORB orb() {
        return _orb();
    }
    
    public void orb(ORB orb) {
        try {
            ((org.omg.CORBA_2_3.ORB)orb).set_delegate(this);
        }
        catch(ClassCastException e) {
            throw new org.omg.CORBA.BAD_PARAM
                ("POA Servant requires an instance of org.omg.CORBA_2_3.ORB");
        }
    }
    
    public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] objectId){
        return _type_ids;
    }
    
    public OutputStream  _invoke(String method, InputStream _in, ResponseHandler reply) throws SystemException {
        try {
            org.omg.CORBA_2_3.portable.InputStream in = 
                (org.omg.CORBA_2_3.portable.InputStream) _in;
            switch (method.charAt(3)) {
                case 49: 
                    if (method.equals("que1Request")) {
                        String arg0 = (String) in.read_value(String.class);
                        TyQueElements result = target.que1Request(arg0);
                        org.omg.CORBA_2_3.portable.OutputStream out = 
                            (org.omg.CORBA_2_3.portable.OutputStream) reply.createReply();
                        out.write_value(result,TyQueElements.class);
                        return out;
                    }
                case 50: 
                    if (method.equals("que2Request")) {
                        TyQueElements arg0 = (TyQueElements) in.read_value(TyQueElements.class);
                        int result = target.que2Request(arg0);
                        OutputStream out = reply.createReply();
                        out.write_long(result);
                        return out;
                    }
                case 51: 
                    if (method.equals("que3Request")) {
                        String arg0 = (String) in.read_value(String.class);
                        String[] result = target.que3Request(arg0);
                        org.omg.CORBA_2_3.portable.OutputStream out = 
                            (org.omg.CORBA_2_3.portable.OutputStream) reply.createReply();
                        out.write_value(cast_array(result),String[].class);
                        return out;
                    }
                case 52: 
                    if (method.equals("que4Request")) {
                        String arg0 = (String) in.read_value(String.class);
                        int arg1 = in.read_long();
                        int result = target.que4Request(arg0, arg1);
                        OutputStream out = reply.createReply();
                        out.write_long(result);
                        return out;
                    }
                case 53: 
                    if (method.equals("que5Request")) {
                        String arg0 = (String) in.read_value(String.class);
                        int result = target.que5Request(arg0);
                        OutputStream out = reply.createReply();
                        out.write_long(result);
                        return out;
                    }
                case 54: 
                    if (method.equals("que6Request")) {
                        String arg0 = (String) in.read_value(String.class);
                        TyWLElements[] result = target.que6Request(arg0);
                        org.omg.CORBA_2_3.portable.OutputStream out = 
                            (org.omg.CORBA_2_3.portable.OutputStream) reply.createReply();
                        out.write_value(cast_array(result),TyWLElements[].class);
                        return out;
                    }
                case 67: 
                    if (method.equals("newCopyRequest")) {
                        String arg0 = (String) in.read_value(String.class);
                        String arg1 = (String) in.read_value(String.class);
                        int result = target.newCopyRequest(arg0, arg1);
                        OutputStream out = reply.createReply();
                        out.write_long(result);
                        return out;
                    }
                case 82: 
                    if (method.equals("newRunTimeFunctions")) {
                        int arg0 = in.read_long();
                        String arg1 = (String) in.read_value(String.class);
                        String arg2 = (String) in.read_value(String.class);
                        int result = target.newRunTimeFunctions(arg0, arg1, arg2);
                        OutputStream out = reply.createReply();
                        out.write_long(result);
                        return out;
                    } else if (method.equals("newRTMaint")) {
                        int arg0 = in.read_long();
                        TyRunTimeElements result = target.newRTMaint(arg0);
                        org.omg.CORBA_2_3.portable.OutputStream out = 
                            (org.omg.CORBA_2_3.portable.OutputStream) reply.createReply();
                        out.write_value(result,TyRunTimeElements.class);
                        return out;
                    }
                case 99: 
                    if (method.equals("cancelSyncReq")) {
                        CancelParm arg0 = (CancelParm) in.read_value(CancelParm.class);
                        int result = target.cancelSyncReq(arg0);
                        OutputStream out = reply.createReply();
                        out.write_long(result);
                        return out;
                    } else if (method.equals("func1Request")) {
                        String arg0 = (String) in.read_value(String.class);
                        String[] result = target.func1Request(arg0);
                        org.omg.CORBA_2_3.portable.OutputStream out = 
                            (org.omg.CORBA_2_3.portable.OutputStream) reply.createReply();
                        out.write_value(cast_array(result),String[].class);
                        return out;
                    } else if (method.equals("syncRequest")) {
                        TymeacParm arg0 = (TymeacParm) in.read_value(TymeacParm.class);
                        Object[] result = target.syncRequest(arg0);
                        org.omg.CORBA_2_3.portable.OutputStream out = 
                            (org.omg.CORBA_2_3.portable.OutputStream) reply.createReply();
                        out.write_value(cast_array(result),Object[].class);
                        return out;
                    }
                case 101: 
                    if (method.equals("alterSvrOptions")) {
                        TyAltSvrElements arg0 = (TyAltSvrElements) in.read_value(TyAltSvrElements.class);
                        TyAltSvrElements result = target.alterSvrOptions(arg0);
                        org.omg.CORBA_2_3.portable.OutputStream out = 
                            (org.omg.CORBA_2_3.portable.OutputStream) reply.createReply();
                        out.write_value(result,TyAltSvrElements.class);
                        return out;
                    }
                case 103: 
                    if (method.equals("diagnoseRequest")) {
                        TyParm arg0 = (TyParm) in.read_value(TyParm.class);
                        String result = target.diagnoseRequest(arg0);
                        org.omg.CORBA_2_3.portable.OutputStream out = 
                            (org.omg.CORBA_2_3.portable.OutputStream) reply.createReply();
                        out.write_value(result,String.class);
                        return out;
                    }
                case 108: 
                    if (method.equals("stall1Request")) {
                        String[] result = target.stall1Request();
                        org.omg.CORBA_2_3.portable.OutputStream out = 
                            (org.omg.CORBA_2_3.portable.OutputStream) reply.createReply();
                        out.write_value(cast_array(result),String[].class);
                        return out;
                    } else if (method.equals("stall2Request")) {
                        long arg0 = in.read_longlong();
                        int result = target.stall2Request(arg0);
                        OutputStream out = reply.createReply();
                        out.write_long(result);
                        return out;
                    } else if (method.equals("stall3Request")) {
                        long arg0 = in.read_longlong();
                        String[] result = target.stall3Request(arg0);
                        org.omg.CORBA_2_3.portable.OutputStream out = 
                            (org.omg.CORBA_2_3.portable.OutputStream) reply.createReply();
                        out.write_value(cast_array(result),String[].class);
                        return out;
                    } else if (method.equals("stall4Request")) {
                        long arg0 = in.read_longlong();
                        int result = target.stall4Request(arg0);
                        OutputStream out = reply.createReply();
                        out.write_long(result);
                        return out;
                    }
                case 110: 
                    if (method.equals("asyncRequest")) {
                        TymeacParm arg0 = (TymeacParm) in.read_value(TymeacParm.class);
                        Object[] result = target.asyncRequest(arg0);
                        org.omg.CORBA_2_3.portable.OutputStream out = 
                            (org.omg.CORBA_2_3.portable.OutputStream) reply.createReply();
                        out.write_value(cast_array(result),Object[].class);
                        return out;
                    }
                case 114: 
                    if (method.equals("overallRequest")) {
                        int arg0 = in.read_long();
                        TyOverallObj result = target.overallRequest(arg0);
                        org.omg.CORBA_2_3.portable.OutputStream out = 
                            (org.omg.CORBA_2_3.portable.OutputStream) reply.createReply();
                        out.write_value(result,TyOverallObj.class);
                        return out;
                    }
                case 116: 
                    if (method.equals("idStatus1Request")) {
                        long arg0 = in.read_longlong();
                        long arg1 = in.read_longlong();
                        int arg2 = in.read_long();
                        int result = target.idStatus1Request(arg0, arg1, arg2);
                        OutputStream out = reply.createReply();
                        out.write_long(result);
                        return out;
                    } else if (method.equals("shutRequest__")) {
                        String result = target.shutRequest();
                        org.omg.CORBA_2_3.portable.OutputStream out = 
                            (org.omg.CORBA_2_3.portable.OutputStream) reply.createReply();
                        out.write_value(result,String.class);
                        return out;
                    } else if (method.equals("shutRequest__boolean")) {
                        boolean arg0 = in.read_boolean();
                        String result = target.shutRequest(arg0);
                        org.omg.CORBA_2_3.portable.OutputStream out = 
                            (org.omg.CORBA_2_3.portable.OutputStream) reply.createReply();
                        out.write_value(result,String.class);
                        return out;
                    } else if (method.equals("stats1Request")) {
                        int result = target.stats1Request();
                        OutputStream out = reply.createReply();
                        out.write_long(result);
                        return out;
                    }
            }
            throw new BAD_OPERATION();
        } catch (SystemException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new UnknownException(ex);
        }
    }
    
    // This method is required as a work-around for
    // a bug in the JDK 1.1.6 verifier.
    
    private Serializable cast_array(Object obj) {
        return (Serializable)obj;
    }
}
