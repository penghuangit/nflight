package com.abreqadhabra.nflight.common.exception.examples;

import java.util.ArrayList;
import java.util.Collections;

public class ExceptionList {

    static ArrayList<Class> exceptionList = null;

    public static void main(String[] arg) {

	ExceptionList el = new ExceptionList();

	exceptionList = new ArrayList<Class>();

	// el.disp();

	el.add();

	System.out.println(exceptionList.size() + "\n\n");

	// Collections.sort(exceptionList);

	for (int i = 0; i < exceptionList.size(); i++) {

	    Class tempClass = exceptionList.get(i);

	    String tempStr = tempClass.getCanonicalName();

	    StringBuilder sb = new StringBuilder();

	    String superClassStr0 = "";
	    String superClassStr1 = "";
	    String superClassStr2 = "";
	    String superClassStr3 = "";
	    String superClassStr4 = "";

	    if (tempClass.getSuperclass() != null) {
		if (tempClass.getSuperclass().getSuperclass() != null) {
		    if (tempClass.getSuperclass().getSuperclass()
			    .getSuperclass() != null) {
			if (tempClass.getSuperclass().getSuperclass()
				.getSuperclass().getSuperclass() != null) {
			    if (tempClass.getSuperclass().getSuperclass()
				    .getSuperclass().getSuperclass()
				    .getSuperclass() != null) {

				superClassStr0 = tempClass.getSuperclass()
					.getSuperclass().getSuperclass()
					.getSuperclass().getSuperclass()
					.getName();
			    } else {
				superClassStr0 = "/";
			    }
			    superClassStr1 = tempClass.getSuperclass()
				    .getSuperclass().getSuperclass()
				    .getSuperclass().getName();
			} else {
			    superClassStr1 = "/";
			}
			superClassStr2 = tempClass.getSuperclass()
				.getSuperclass().getSuperclass().getName();
		    } else {
			superClassStr2 = "/";
		    }
		    superClassStr3 = tempClass.getSuperclass().getSuperclass()
			    .getName();
		} else {
		    superClassStr3 = "/";
		}
		superClassStr4 = tempClass.getSuperclass().getName();
	    } else {
		superClassStr4 = "/";
	    }

	    sb.append(superClassStr0);
	    sb.append("/");
	    sb.append(superClassStr1);
	    sb.append("/");
	    sb.append(superClassStr2);
	    sb.append("/");
	    sb.append(superClassStr3);
	    sb.append("/");
	    sb.append(superClassStr4);
	    sb.append("/");

	    sb.append(tempStr.substring(0, tempStr.lastIndexOf(".")));
	    sb.append("/");
	    sb.append(tempStr.substring(tempStr.lastIndexOf(".") + 1,
		    tempStr.length()));

	    System.out.println(sb.toString());

	}
    }

    public void add() {
	exceptionList.add(java.nio.channels.AcceptPendingException.class);
	exceptionList.add(java.security.AccessControlException.class);
	exceptionList.add(java.nio.file.AccessDeniedException.class);
	exceptionList.add(java.rmi.AccessException.class);
	exceptionList.add(javax.security.auth.login.AccountException.class);
	exceptionList
		.add(javax.security.auth.login.AccountExpiredException.class);
	exceptionList
		.add(javax.security.auth.login.AccountLockedException.class);
	exceptionList
		.add(javax.security.auth.login.AccountNotFoundException.class);
	exceptionList.add(java.security.acl.AclNotFoundException.class);
	exceptionList.add(java.rmi.activation.ActivateFailedException.class);
	exceptionList.add(java.rmi.activation.ActivationException.class);
	exceptionList.add(javax.activity.ActivityCompletedException.class);
	exceptionList.add(javax.activity.ActivityRequiredException.class);
	exceptionList.add(javax.crypto.AEADBadTagException.class);
	exceptionList.add(java.nio.channels.AlreadyBoundException.class);
	exceptionList.add(java.rmi.AlreadyBoundException.class);
	exceptionList.add(java.nio.channels.AlreadyConnectedException.class);
	exceptionList
		.add(java.lang.annotation.AnnotationTypeMismatchException.class);
	exceptionList.add(org.omg.CORBA.portable.ApplicationException.class);
	exceptionList.add(java.lang.ArithmeticException.class);
	exceptionList.add(java.lang.ArrayIndexOutOfBoundsException.class);
	exceptionList.add(java.lang.ArrayStoreException.class);
	exceptionList.add(java.nio.channels.AsynchronousCloseException.class);
	exceptionList.add(java.nio.file.AtomicMoveNotSupportedException.class);
	// exceptionList.add(javax.print.AttributeException.class);
	exceptionList.add(javax.naming.directory.AttributeInUseException.class);
	exceptionList
		.add(javax.naming.directory.AttributeModificationException.class);
	exceptionList.add(javax.management.AttributeNotFoundException.class);
	exceptionList.add(javax.naming.AuthenticationException.class);
	exceptionList.add(javax.security.sasl.AuthenticationException.class);
	exceptionList
		.add(javax.naming.AuthenticationNotSupportedException.class);
	exceptionList.add(java.awt.AWTException.class);
	exceptionList.add(java.util.prefs.BackingStoreException.class);
	exceptionList.add(javax.management.BadAttributeValueExpException.class);
	exceptionList.add(javax.management.BadBinaryOpValueExpException.class);
	exceptionList.add(javax.swing.text.BadLocationException.class);
	exceptionList.add(javax.crypto.BadPaddingException.class);
	exceptionList.add(javax.management.BadStringOperationException.class);
	exceptionList.add(java.sql.BatchUpdateException.class);
	exceptionList.add(java.net.BindException.class);
	exceptionList.add(java.util.concurrent.BrokenBarrierException.class);
	exceptionList.add(java.nio.BufferOverflowException.class);
	exceptionList.add(java.nio.BufferUnderflowException.class);
	exceptionList.add(java.util.concurrent.CancellationException.class);
	exceptionList.add(java.nio.channels.CancelledKeyException.class);
	exceptionList.add(javax.naming.CannotProceedException.class);
	exceptionList.add(javax.swing.undo.CannotRedoException.class);
	exceptionList.add(javax.swing.undo.CannotUndoException.class);
	exceptionList
		.add(java.security.cert.CertificateEncodingException.class);
	exceptionList
		.add(javax.security.cert.CertificateEncodingException.class);
	exceptionList.add(java.security.cert.CertificateException.class);
	exceptionList.add(javax.security.cert.CertificateException.class);
	exceptionList.add(java.security.cert.CertificateExpiredException.class);
	exceptionList
		.add(javax.security.cert.CertificateExpiredException.class);
	exceptionList
		.add(java.security.cert.CertificateNotYetValidException.class);
	exceptionList
		.add(javax.security.cert.CertificateNotYetValidException.class);
	exceptionList.add(java.security.cert.CertificateParsingException.class);
	exceptionList
		.add(javax.security.cert.CertificateParsingException.class);
	exceptionList.add(java.security.cert.CertificateRevokedException.class);
	exceptionList.add(java.security.cert.CertPathBuilderException.class);
	exceptionList.add(java.security.cert.CertPathValidatorException.class);
	// exceptionList
	// .add(java.security.cert.CertPathValidatorException.BasicReason.class);
	// exceptionList
	// .add(java.security.cert.CertPathValidatorException.Reason.class);
	exceptionList.add(java.security.cert.CertStoreException.class);
	exceptionList.add(javax.swing.text.ChangedCharSetException.class);
	exceptionList.add(java.nio.charset.CharacterCodingException.class);
	exceptionList.add(java.io.CharConversionException.class);
	exceptionList.add(java.lang.ClassCastException.class);
	exceptionList.add(java.lang.ClassNotFoundException.class);
	exceptionList.add(java.lang.CloneNotSupportedException.class);
	exceptionList.add(java.nio.channels.ClosedByInterruptException.class);
	exceptionList.add(java.nio.channels.ClosedChannelException.class);
	exceptionList.add(java.nio.file.ClosedDirectoryStreamException.class);
	exceptionList.add(java.nio.file.ClosedFileSystemException.class);
	exceptionList.add(java.nio.channels.ClosedSelectorException.class);
	exceptionList.add(java.nio.file.ClosedWatchServiceException.class);
	exceptionList.add(java.awt.color.CMMException.class);
	exceptionList.add(javax.naming.CommunicationException.class);
	exceptionList.add(java.util.ConcurrentModificationException.class);
	exceptionList.add(javax.naming.ConfigurationException.class);
	exceptionList.add(java.net.ConnectException.class);
	exceptionList.add(java.rmi.ConnectException.class);
	exceptionList.add(java.rmi.ConnectIOException.class);
	exceptionList.add(java.nio.channels.ConnectionPendingException.class);
	exceptionList.add(javax.naming.ContextNotEmptyException.class);
	exceptionList.add(javax.security.auth.login.CredentialException.class);
	exceptionList
		.add(javax.security.auth.login.CredentialExpiredException.class);
	exceptionList
		.add(javax.security.auth.login.CredentialNotFoundException.class);
	exceptionList.add(java.security.cert.CRLException.class);
	exceptionList.add(javax.xml.bind.DataBindingException.class);
	exceptionList.add(java.util.zip.DataFormatException.class);
	exceptionList
		.add(javax.xml.datatype.DatatypeConfigurationException.class);
	exceptionList.add(javax.security.auth.DestroyFailedException.class);

	exceptionList.add(java.security.DigestException.class);

	exceptionList.add(java.nio.file.DirectoryIteratorException.class);
	exceptionList.add(java.nio.file.DirectoryNotEmptyException.class);
	exceptionList.add(org.w3c.dom.DOMException.class);
	exceptionList.add(java.util.DuplicateFormatFlagsException.class);
	exceptionList.add(java.util.EmptyStackException.class);
	exceptionList.add(java.lang.EnumConstantNotPresentException.class);
	exceptionList.add(java.io.EOFException.class);
	exceptionList.add(org.w3c.dom.events.EventException.class);
	exceptionList.add(java.lang.Exception.class);
	// exceptionList.add(org.omg.IOP.ExceptionDetailMessage.class);
	// exceptionList.add(java.lang.ExceptionInInitializerError.class);
	// exceptionList.add(org.omg.CORBA.ExceptionList.class);
	// exceptionList.add(java.beans.ExceptionListener.class);
	exceptionList.add(java.util.concurrent.ExecutionException.class);
	exceptionList.add(javax.crypto.ExemptionMechanismException.class);
	exceptionList.add(javax.swing.tree.ExpandVetoException.class);
	exceptionList.add(java.rmi.server.ExportException.class);
	exceptionList.add(javax.security.auth.login.FailedLoginException.class);
	exceptionList.add(java.nio.file.FileAlreadyExistsException.class);
	exceptionList
		.add(java.nio.channels.FileLockInterruptionException.class);
	exceptionList.add(java.io.FileNotFoundException.class);
	exceptionList.add(javax.annotation.processing.FilerException.class);
	exceptionList.add(java.nio.file.FileSystemAlreadyExistsException.class);
	exceptionList.add(java.nio.file.FileSystemException.class);
	exceptionList.add(java.nio.file.FileSystemLoopException.class);
	exceptionList.add(java.nio.file.FileSystemNotFoundException.class);
	// exceptionList.add(javax.print.FlavorException.class);
	exceptionList.add(java.awt.FontFormatException.class);
	exceptionList
		.add(java.util.FormatFlagsConversionMismatchException.class);
	exceptionList.add(java.util.FormatterClosedException.class);
	exceptionList.add(java.security.GeneralSecurityException.class);
	exceptionList.add(org.ietf.jgss.GSSException.class);
	exceptionList.add(java.awt.HeadlessException.class);
	exceptionList.add(javax.xml.ws.http.HTTPException.class);
	exceptionList.add(java.net.HttpRetryException.class);
	exceptionList.add(javax.imageio.IIOException.class);
	exceptionList.add(javax.imageio.metadata.IIOInvalidTreeException.class);
	exceptionList.add(java.lang.IllegalAccessException.class);
	exceptionList.add(java.lang.IllegalArgumentException.class);
	exceptionList.add(java.nio.channels.IllegalBlockingModeException.class);
	exceptionList.add(javax.crypto.IllegalBlockSizeException.class);
	exceptionList.add(java.nio.channels.IllegalChannelGroupException.class);
	exceptionList.add(java.nio.charset.IllegalCharsetNameException.class);
	exceptionList
		.add(java.lang.instrument.IllegalClassFormatException.class);
	exceptionList.add(java.awt.IllegalComponentStateException.class);
	exceptionList.add(java.util.IllegalFormatCodePointException.class);
	exceptionList.add(java.util.IllegalFormatConversionException.class);
	exceptionList.add(java.util.IllegalFormatException.class);
	exceptionList.add(java.util.IllegalFormatFlagsException.class);
	exceptionList.add(java.util.IllegalFormatPrecisionException.class);
	exceptionList.add(java.util.IllegalFormatWidthException.class);
	exceptionList.add(java.lang.IllegalMonitorStateException.class);
	exceptionList.add(java.awt.geom.IllegalPathStateException.class);
	exceptionList.add(java.nio.channels.IllegalSelectorException.class);
	exceptionList.add(java.lang.IllegalStateException.class);
	exceptionList.add(java.lang.IllegalThreadStateException.class);
	exceptionList.add(java.util.IllformedLocaleException.class);
	exceptionList.add(java.awt.image.ImagingOpException.class);
	exceptionList
		.add(java.lang.annotation.IncompleteAnnotationException.class);
	exceptionList.add(java.lang.IndexOutOfBoundsException.class);
	exceptionList.add(org.omg.CORBA.portable.IndirectionException.class);
	exceptionList.add(java.util.InputMismatchException.class);
	exceptionList
		.add(javax.management.InstanceAlreadyExistsException.class);
	exceptionList.add(javax.management.InstanceNotFoundException.class);
	exceptionList.add(java.lang.InstantiationException.class);
	exceptionList.add(javax.naming.InsufficientResourcesException.class);
	exceptionList
		.add(java.nio.channels.InterruptedByTimeoutException.class);
	exceptionList.add(java.lang.InterruptedException.class);
	exceptionList.add(java.io.InterruptedIOException.class);
	exceptionList.add(javax.naming.InterruptedNamingException.class);
	exceptionList.add(java.beans.IntrospectionException.class);
	exceptionList.add(javax.management.IntrospectionException.class);
	exceptionList.add(javax.activity.InvalidActivityException.class);
	exceptionList
		.add(java.security.InvalidAlgorithmParameterException.class);
	exceptionList.add(javax.management.InvalidApplicationException.class);
	exceptionList
		.add(javax.naming.directory.InvalidAttributeIdentifierException.class);
	exceptionList
		.add(javax.naming.directory.InvalidAttributesException.class);
	exceptionList
		.add(javax.management.InvalidAttributeValueException.class);
	exceptionList
		.add(javax.naming.directory.InvalidAttributeValueException.class);
	exceptionList.add(java.io.InvalidClassException.class);
	exceptionList.add(java.awt.dnd.InvalidDnDOperationException.class);
	exceptionList.add(java.security.InvalidKeyException.class);
	exceptionList.add(javax.management.openmbean.InvalidKeyException.class);
	exceptionList.add(java.security.spec.InvalidKeySpecException.class);
	exceptionList.add(java.nio.InvalidMarkException.class);
	exceptionList.add(javax.sound.midi.InvalidMidiDataException.class);
	exceptionList.add(javax.naming.InvalidNameException.class);
	exceptionList.add(java.io.InvalidObjectException.class);
	exceptionList
		.add(javax.management.openmbean.InvalidOpenTypeException.class);
	exceptionList.add(java.security.InvalidParameterException.class);
	exceptionList
		.add(java.security.spec.InvalidParameterSpecException.class);
	exceptionList.add(java.nio.file.InvalidPathException.class);
	exceptionList
		.add(java.util.prefs.InvalidPreferencesFormatException.class);
	exceptionList.add(java.util.InvalidPropertiesFormatException.class);
	exceptionList
		.add(javax.management.relation.InvalidRelationIdException.class);
	exceptionList
		.add(javax.management.relation.InvalidRelationServiceException.class);
	exceptionList
		.add(javax.management.relation.InvalidRelationTypeException.class);
	exceptionList
		.add(javax.management.relation.InvalidRoleInfoException.class);
	exceptionList
		.add(javax.management.relation.InvalidRoleValueException.class);
	exceptionList
		.add(javax.naming.directory.InvalidSearchControlsException.class);
	exceptionList
		.add(javax.naming.directory.InvalidSearchFilterException.class);
	exceptionList
		.add(javax.management.modelmbean.InvalidTargetObjectTypeException.class);
	exceptionList.add(javax.transaction.InvalidTransactionException.class);
	exceptionList.add(java.lang.reflect.InvocationTargetException.class);
	exceptionList.add(java.io.IOException.class);
	exceptionList.add(java.util.jar.JarException.class);
	exceptionList.add(javax.xml.bind.JAXBException.class);
	exceptionList.add(javax.management.JMException.class);
	exceptionList.add(javax.management.JMRuntimeException.class);
	exceptionList.add(javax.management.remote.JMXProviderException.class);
	exceptionList
		.add(javax.management.remote.JMXServerErrorException.class);
	exceptionList
		.add(javax.management.openmbean.KeyAlreadyExistsException.class);
	exceptionList.add(java.security.KeyException.class);
	exceptionList.add(java.security.KeyManagementException.class);
	exceptionList.add(javax.xml.crypto.KeySelectorException.class);
	exceptionList.add(java.security.KeyStoreException.class);
	exceptionList.add(java.security.acl.LastOwnerException.class);
	exceptionList.add(javax.naming.ldap.LdapReferralException.class);
	exceptionList.add(javax.naming.LimitExceededException.class);
	exceptionList.add(javax.sound.sampled.LineUnavailableException.class);
	exceptionList.add(javax.naming.LinkException.class);
	exceptionList.add(javax.naming.LinkLoopException.class);
	exceptionList.add(javax.management.ListenerNotFoundException.class);
	exceptionList.add(javax.security.auth.login.LoginException.class);
	exceptionList.add(org.w3c.dom.ls.LSException.class);
	exceptionList.add(java.nio.charset.MalformedInputException.class);
	exceptionList.add(javax.naming.MalformedLinkException.class);
	exceptionList.add(javax.management.MalformedObjectNameException.class);
	exceptionList
		.add(java.lang.reflect.MalformedParameterizedTypeException.class);
	exceptionList.add(java.net.MalformedURLException.class);
	exceptionList.add(java.rmi.MarshalException.class);
	exceptionList.add(javax.xml.bind.MarshalException.class);
	exceptionList.add(javax.xml.crypto.MarshalException.class);
	exceptionList.add(javax.management.MBeanException.class);
	exceptionList.add(javax.management.MBeanRegistrationException.class);
	exceptionList.add(javax.sound.midi.MidiUnavailableException.class);
	exceptionList.add(java.awt.datatransfer.MimeTypeParseException.class);
	exceptionList.add(javax.activation.MimeTypeParseException.class);
	exceptionList.add(javax.lang.model.type.MirroredTypeException.class);
	exceptionList.add(javax.lang.model.type.MirroredTypesException.class);
	exceptionList.add(java.util.MissingFormatArgumentException.class);
	exceptionList.add(java.util.MissingFormatWidthException.class);
	exceptionList.add(java.util.MissingResourceException.class);
	exceptionList
		.add(javax.management.monitor.MonitorSettingException.class);
	exceptionList.add(javax.naming.NameAlreadyBoundException.class);
	exceptionList.add(javax.naming.NameNotFoundException.class);
	exceptionList.add(javax.naming.NamingException.class);
	// exceptionList.add(javax.naming.event.NamingExceptionEvent.class);
	exceptionList.add(javax.naming.NamingSecurityException.class);
	exceptionList.add(java.lang.NegativeArraySizeException.class);
	exceptionList.add(java.nio.channels.NoConnectionPendingException.class);
	exceptionList.add(javax.naming.NoInitialContextException.class);
	exceptionList.add(java.awt.geom.NoninvertibleTransformException.class);
	exceptionList.add(java.nio.channels.NonReadableChannelException.class);
	exceptionList.add(java.nio.channels.NonWritableChannelException.class);
	exceptionList.add(javax.naming.NoPermissionException.class);
	exceptionList.add(java.net.NoRouteToHostException.class);
	exceptionList.add(java.security.NoSuchAlgorithmException.class);
	exceptionList
		.add(javax.naming.directory.NoSuchAttributeException.class);
	exceptionList.add(java.util.NoSuchElementException.class);
	exceptionList.add(java.lang.NoSuchFieldException.class);
	exceptionList.add(java.nio.file.NoSuchFileException.class);
	exceptionList.add(javax.xml.crypto.NoSuchMechanismException.class);
	exceptionList.add(java.lang.NoSuchMethodException.class);
	exceptionList.add(java.rmi.NoSuchObjectException.class);
	exceptionList.add(javax.crypto.NoSuchPaddingException.class);
	exceptionList.add(java.security.NoSuchProviderException.class);
	exceptionList.add(java.io.NotActiveException.class);
	exceptionList.add(java.rmi.NotBoundException.class);
	exceptionList.add(javax.management.NotCompliantMBeanException.class);
	exceptionList.add(javax.naming.NotContextException.class);
	exceptionList.add(java.nio.file.NotDirectoryException.class);
	exceptionList.add(java.nio.file.NotLinkException.class);
	exceptionList.add(java.security.acl.NotOwnerException.class);
	exceptionList.add(java.io.NotSerializableException.class);
	exceptionList.add(java.nio.channels.NotYetBoundException.class);
	exceptionList.add(java.nio.channels.NotYetConnectedException.class);
	exceptionList.add(java.lang.NullPointerException.class);
	exceptionList.add(java.lang.NumberFormatException.class);
	exceptionList.add(java.io.ObjectStreamException.class);
	exceptionList.add(javax.management.openmbean.OpenDataException.class);
	exceptionList.add(javax.naming.OperationNotSupportedException.class);
	exceptionList.add(javax.management.OperationsException.class);
	exceptionList.add(java.io.OptionalDataException.class);
	exceptionList.add(java.nio.channels.OverlappingFileLockException.class);
	exceptionList.add(java.text.ParseException.class);
	exceptionList.add(javax.xml.parsers.ParserConfigurationException.class);
	exceptionList.add(javax.naming.PartialResultException.class);
	exceptionList.add(java.util.regex.PatternSyntaxException.class);
	exceptionList.add(java.net.PortUnreachableException.class);
	exceptionList.add(java.awt.print.PrinterAbortException.class);
	exceptionList.add(java.awt.print.PrinterException.class);
	exceptionList.add(java.awt.print.PrinterIOException.class);
	exceptionList.add(javax.print.PrintException.class);
	exceptionList.add(java.security.PrivilegedActionException.class);
	// exceptionList.add(java.security.PrivilegedExceptionAction.class);
	exceptionList.add(java.awt.color.ProfileDataException.class);
	exceptionList.add(javax.xml.bind.PropertyException.class);
	exceptionList.add(java.beans.PropertyVetoException.class);
	exceptionList.add(java.net.ProtocolException.class);
	exceptionList.add(javax.xml.ws.ProtocolException.class);
	exceptionList.add(java.security.ProviderException.class);
	exceptionList.add(java.nio.file.ProviderMismatchException.class);
	exceptionList.add(java.nio.file.ProviderNotFoundException.class);
	exceptionList.add(java.awt.image.RasterFormatException.class);
	exceptionList.add(java.nio.ReadOnlyBufferException.class);
	exceptionList.add(java.nio.file.ReadOnlyFileSystemException.class);
	exceptionList.add(java.nio.channels.ReadPendingException.class);
	exceptionList.add(javax.naming.ReferralException.class);
	exceptionList.add(javax.management.ReflectionException.class);
	exceptionList.add(java.lang.ReflectiveOperationException.class);
	exceptionList.add(javax.security.auth.RefreshFailedException.class);
	exceptionList
		.add(java.util.concurrent.RejectedExecutionException.class);
	exceptionList.add(javax.management.relation.RelationException.class);
	exceptionList
		.add(javax.management.relation.RelationNotFoundException.class);
	exceptionList
		.add(javax.management.relation.RelationServiceNotRegisteredException.class);
	exceptionList
		.add(javax.management.relation.RelationTypeNotFoundException.class);
	exceptionList.add(org.omg.CORBA.portable.RemarshalException.class);
	exceptionList.add(java.rmi.RemoteException.class);
	exceptionList.add(java.rmi.RMISecurityException.class);
	exceptionList
		.add(javax.management.relation.RoleInfoNotFoundException.class);
	exceptionList
		.add(javax.management.relation.RoleNotFoundException.class);
	exceptionList.add(javax.management.RuntimeErrorException.class);
	exceptionList.add(java.lang.RuntimeException.class);
	exceptionList.add(javax.management.RuntimeMBeanException.class);
	exceptionList.add(javax.management.RuntimeOperationsException.class);
	exceptionList.add(javax.security.sasl.SaslException.class);
	exceptionList.add(org.xml.sax.SAXException.class);
	exceptionList.add(org.xml.sax.SAXNotRecognizedException.class);
	exceptionList.add(org.xml.sax.SAXNotSupportedException.class);
	exceptionList.add(org.xml.sax.SAXParseException.class);
	exceptionList
		.add(javax.naming.directory.SchemaViolationException.class);
	exceptionList.add(javax.script.ScriptException.class);
	exceptionList.add(java.lang.SecurityException.class);
	exceptionList.add(javax.sql.rowset.serial.SerialException.class);
	exceptionList.add(java.rmi.server.ServerCloneException.class);
	exceptionList.add(java.rmi.ServerException.class);
	exceptionList.add(java.rmi.server.ServerNotActiveException.class);
	exceptionList.add(java.rmi.ServerRuntimeException.class);
	exceptionList.add(javax.management.ServiceNotFoundException.class);
	exceptionList.add(javax.naming.ServiceUnavailableException.class);
	exceptionList.add(javax.crypto.ShortBufferException.class);
	exceptionList
		.add(java.nio.channels.ShutdownChannelGroupException.class);
	exceptionList.add(java.security.SignatureException.class);
	exceptionList.add(javax.naming.SizeLimitExceededException.class);
	exceptionList.add(java.rmi.server.SkeletonMismatchException.class);
	exceptionList.add(java.rmi.server.SkeletonNotFoundException.class);
	exceptionList.add(javax.xml.soap.SOAPException.class);
	exceptionList.add(javax.xml.ws.soap.SOAPFaultException.class);
	exceptionList.add(java.net.SocketException.class);
	exceptionList.add(java.rmi.server.SocketSecurityException.class);
	exceptionList.add(java.net.SocketTimeoutException.class);
	exceptionList.add(java.sql.SQLClientInfoException.class);
	exceptionList.add(java.sql.SQLDataException.class);
	exceptionList.add(java.sql.SQLException.class);
	exceptionList.add(java.sql.SQLFeatureNotSupportedException.class);
	exceptionList
		.add(java.sql.SQLIntegrityConstraintViolationException.class);
	exceptionList.add(java.sql.SQLInvalidAuthorizationSpecException.class);
	exceptionList.add(java.sql.SQLNonTransientConnectionException.class);
	exceptionList.add(java.sql.SQLNonTransientException.class);
	exceptionList.add(java.sql.SQLRecoverableException.class);
	exceptionList.add(java.sql.SQLSyntaxErrorException.class);
	exceptionList.add(java.sql.SQLTimeoutException.class);
	exceptionList.add(java.sql.SQLTransactionRollbackException.class);
	exceptionList.add(java.sql.SQLTransientConnectionException.class);
	exceptionList.add(java.sql.SQLTransientException.class);
	exceptionList.add(javax.net.ssl.SSLException.class);
	exceptionList.add(javax.net.ssl.SSLHandshakeException.class);
	exceptionList.add(javax.net.ssl.SSLKeyException.class);
	exceptionList.add(javax.net.ssl.SSLPeerUnverifiedException.class);
	exceptionList.add(javax.net.ssl.SSLProtocolException.class);
	exceptionList.add(java.io.StreamCorruptedException.class);
	exceptionList.add(java.lang.StringIndexOutOfBoundsException.class);
	exceptionList.add(java.rmi.StubNotFoundException.class);
	exceptionList.add(javax.sql.rowset.spi.SyncFactoryException.class);
	exceptionList.add(java.io.SyncFailedException.class);
	exceptionList.add(javax.sql.rowset.spi.SyncProviderException.class);
	// exceptionList.add(org.omg.PortableInterceptor.SYSTEM_EXCEPTION.class);
	exceptionList.add(org.omg.CORBA.SystemException.class);
	// exceptionList.add(java.lang.Thread.UncaughtExceptionHandler.class);
	exceptionList.add(javax.naming.TimeLimitExceededException.class);
	exceptionList.add(java.util.concurrent.TimeoutException.class);
	exceptionList.add(java.util.TooManyListenersException.class);
	exceptionList.add(javax.transaction.TransactionRequiredException.class);
	exceptionList
		.add(javax.transaction.TransactionRolledbackException.class);
	exceptionList
		.add(javax.xml.transform.TransformerConfigurationException.class);
	exceptionList.add(javax.xml.transform.TransformerException.class);
	exceptionList.add(javax.xml.crypto.dsig.TransformException.class);
	exceptionList.add(javax.xml.bind.TypeConstraintException.class);
	exceptionList.add(java.lang.TypeNotPresentException.class);
	exceptionList.add(java.lang.reflect.UndeclaredThrowableException.class);
	exceptionList.add(java.rmi.UnexpectedException.class);
	exceptionList
		.add(javax.lang.model.element.UnknownAnnotationValueException.class);
	exceptionList
		.add(javax.lang.model.element.UnknownElementException.class);
	exceptionList.add(javax.lang.model.UnknownEntityException.class);
	exceptionList.add(org.omg.CORBA.portable.UnknownException.class);
	exceptionList.add(java.util.UnknownFormatConversionException.class);
	exceptionList.add(java.util.UnknownFormatFlagsException.class);
	exceptionList.add(java.rmi.activation.UnknownGroupException.class);
	exceptionList.add(java.net.UnknownHostException.class);
	exceptionList.add(java.rmi.UnknownHostException.class);
	exceptionList.add(java.rmi.activation.UnknownObjectException.class);
	exceptionList.add(java.net.UnknownServiceException.class);
	exceptionList.add(javax.lang.model.type.UnknownTypeException.class);
	exceptionList.add(org.omg.CORBA.UnknownUserException.class);
	// exceptionList.add(org.omg.CORBA.UnknownUserExceptionHelper.class);
	// exceptionList.add(org.omg.CORBA.UnknownUserExceptionHolder.class);
	exceptionList.add(java.nio.charset.UnmappableCharacterException.class);
	exceptionList.add(java.rmi.UnmarshalException.class);
	exceptionList.add(javax.xml.bind.UnmarshalException.class);
	exceptionList
		.add(java.lang.instrument.UnmodifiableClassException.class);
	exceptionList.add(javax.print.attribute.UnmodifiableSetException.class);
	exceptionList.add(java.security.UnrecoverableEntryException.class);
	exceptionList.add(java.security.UnrecoverableKeyException.class);
	exceptionList.add(java.nio.channels.UnresolvedAddressException.class);
	exceptionList
		.add(java.nio.channels.UnsupportedAddressTypeException.class);
	exceptionList
		.add(javax.sound.sampled.UnsupportedAudioFileException.class);
	exceptionList
		.add(javax.security.auth.callback.UnsupportedCallbackException.class);
	exceptionList.add(java.nio.charset.UnsupportedCharsetException.class);
	exceptionList.add(javax.activation.UnsupportedDataTypeException.class);
	exceptionList.add(java.io.UnsupportedEncodingException.class);
	exceptionList
		.add(java.awt.datatransfer.UnsupportedFlavorException.class);
	exceptionList.add(javax.swing.UnsupportedLookAndFeelException.class);
	exceptionList.add(java.lang.UnsupportedOperationException.class);
	// exceptionList.add(javax.print.URIException.class);
	exceptionList.add(javax.xml.crypto.URIReferenceException.class);
	exceptionList.add(java.net.URISyntaxException.class);
	// exceptionList.add(org.omg.PortableInterceptor.USER_EXCEPTION.class);
	exceptionList.add(org.omg.CORBA.UserException.class);
	exceptionList
		.add(java.nio.file.attribute.UserPrincipalNotFoundException.class);
	exceptionList.add(java.io.UTFDataFormatException.class);
	exceptionList.add(javax.xml.bind.ValidationException.class);
	exceptionList.add(javax.xml.ws.WebServiceException.class);
	exceptionList.add(java.io.WriteAbortedException.class);
	exceptionList.add(java.nio.channels.WritePendingException.class);
	exceptionList.add(java.lang.invoke.WrongMethodTypeException.class);
	exceptionList.add(javax.transaction.xa.XAException.class);
	exceptionList.add(javax.management.modelmbean.XMLParseException.class);
	exceptionList.add(javax.xml.crypto.dsig.XMLSignatureException.class);
	exceptionList.add(javax.xml.stream.XMLStreamException.class);
	exceptionList.add(javax.xml.xpath.XPathException.class);
	exceptionList.add(javax.xml.xpath.XPathExpressionException.class);
	exceptionList
		.add(javax.xml.xpath.XPathFactoryConfigurationException.class);
	exceptionList.add(javax.xml.xpath.XPathFunctionException.class);
	exceptionList.add(java.util.zip.ZipException.class);

    }
}
