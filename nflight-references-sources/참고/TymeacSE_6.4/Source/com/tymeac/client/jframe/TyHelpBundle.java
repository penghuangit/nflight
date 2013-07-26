package com.tymeac.client.jframe;
/* 
 * Copyright (c) 1998 - 2004 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */
 
public class TyHelpBundle extends java.util.ListResourceBundle
{
	public Object[][] getContents()
	{
		return contents;
	}

	static final Object[][] contents =
	{
		//Changing tags in the following block will
		//break the parsing of the related file
		//{{Start Automatic Code Generation Block
		{"TyHelp_title", "Tymeac Component Help"},
		{"JEditorPane1_contentType", "text/plain"},
		{"JEditorPane1_text", ""},
		{"JEditorPane1_toolTipText", ""},
		//}}End Automatic Code Generation Block
		{"Error", "Error "},
		{"error___tyHelpBundle_getString", " cannot continue with Help function"},
		{"help", "com/tymeac/client/jframe/help/"},
		{"Error_1", "Could not find the document "},
		{"error____tyHelpBundle_getString", " cannot continue with Help function"},
		{"MalformedURLException__cannot_continue", "MalformedURLException, cannot continue with Help function"},
		{"Error_2", "Error: "},
		{"error___tyHelpBundle_getString_1", " cannot continue with Help function"},
	};
}

