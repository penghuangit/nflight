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

public class TyReqStatusBundle extends java.util.ListResourceBundle
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
		{"TyReqStatus_title", "Tymeac - Request ID Status/Cancel"},
		{"MilliText_text", ""},
		{"MilliText_toolTipText", "Enter Tymeac Millitime"},
		{"IdText_text", ""},
		{"IdText_toolTipText", "Enter Async Request ID"},
		{"MilliLabel_text", "Tymeac MilliTime"},
		{"MilliLabel_toolTipText", ""},
		{"IdLabel_text", "Request ID"},
		{"IdLabel_toolTipText", ""},
		{"Msg_text", " Enter Tymeac MilliTime [start up], Request ID"},
		{"Msg_toolTipText", ""},
		{"GetButton_text", "Get"},
		{"GetButton_actionCommand", "Get"},
		{"GetButton_toolTipText", "Use to display a request\'s status"},
		{"JMenuBar1_toolTipText", ""},
		{"fileMenu_text", "File"},
		{"fileMenu_actionCommand", "File"},
		{"fileMenu_toolTipText", ""},
		{"exitItem_text", "Exit"},
		{"exitItem_actionCommand", "Exit"},
		{"exitItem_toolTipText", ""},
		{"JSeparator1_toolTipText", ""},
		{"helpMenu_text", "Help"},
		{"helpMenu_actionCommand", "Help"},
		{"helpMenu_toolTipText", ""},
		{"Help_text", "Document"},
		{"Help_actionCommand", "Doc..."},
		{"Help_toolTipText", "Window help"},
		{"about_text", "About..."},
		{"about_actionCommand", "About..."},
		{"about_toolTipText", "About the authors"},
		{"CancelButton_text", "Cancel"},
		{"CancelButton_actionCommand", "cancel"},
		{"CancelButton_toolTipText", "Use to cancel a request"},
		//}}End Automatic Code Generation Block
		{"th_putOut", "TyReqStatus.html"},
		{"case__1__Msg_text", " Connection error"},
		{"case_1__Msg_text", " Tymeac MilliTime is not a valid number"},
		{"case_2__Msg_text", " Request Id is not a valid number"},
		{"case_3__Msg_text", " Request id is invalid this session"},
		{"case_4__Msg_text", " Tymeac shutting down"},
		{"case_5__Msg_text", " Tymeac Millitime mismatched with current session"},
		{"case_6__Msg_text", " Request is Stalled"},
		{"case_7__Msg_text", " Request is not in the system"},
		{"case_8__Msg_text", " Request is awaiting execution"},
		{"case_9__Msg_text", " Request Executing at Output Agent Stage"},
		{"case_10__Msg_text", " Request Executing"},
		{"case_11__Msg_text", "Request cancelled. No processing took place."},
		{"case_12__Msg_text", "Request cancelled. Was in progress."},
	};
}

