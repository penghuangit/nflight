package com.tymeac.jini.base;

import net.jini.security.AccessPermission;

/**
 * Represents permissions used to express the access control policy for the
 * Server class. The name specifies the names of the method which you have
 * permission to call using the matching rules provided by AccessPermission.
 *
 * @author Sun Microsystems, Inc.
 * 
 */
public class JiniServerPermission extends AccessPermission {

/**
	 * 
	 */
	private static final long serialVersionUID = 1594879371039477595L;

/**
 * Creates an instance with the specified target name.
 *
 * @param name the target name
 */
public JiniServerPermission(String name) {
  super(name);
    }
}
