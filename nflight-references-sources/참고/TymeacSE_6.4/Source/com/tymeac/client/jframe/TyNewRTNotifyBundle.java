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

/**
 * The resource bundle for the Strings associated with the class = Name minus "Bundle". 
 * @since 6.2
 */
public class TyNewRTNotifyBundle extends java.util.ListResourceBundle
{
	static final Object[][] contents =
	{
		//Changing tags in the following block will
		//break the parsing of the related file
		//{{Start Automatic Code Generation Block
		{"TyNewRTNotify_title", "Tymeac -- New Notification Environment"},
    
    {"ResetLabel_text", "Reset existing Notification:"},
    
    {"StopLabel_text", "Stop existing Notification:"},   
    
    {"DashLabel_text", "-------------------------------------------------------------------------------------------"},    
    {"NewLabel_text", "New Notification:"},
    
    {"ResetButton_text", "Reset"},
    {"ResetButton_actionCommand", "Reset"},
    {"ResetButton_toolTipText", "Reset existing notification"},
    
    {"StopButton_text", "Stop"},
    {"StopButton_actionCommand", "Stop"},
    {"StopButton_toolTipText", "Stop existing notification"},   
    
    {"ImportButton_text", "Import"},
    {"ImportButton_actionCommand", "Import"},
    {"ImportButton_toolTipText", "Get existing notification"},
    
    {"UpdateButton_text", "Update"},
    {"UpdateButton_actionCommand", "Update"},
    {"UpdateButton_toolTipText", "Update with above notification"},    
    
		{"FuncText_text", ""},
		{"FuncText_toolTipText", "Function Name"},
    
		{"FuncLabel_text", "  Func Name:"},
		{"FuncLabel_toolTipText", ""},    
    
		{"Msg_text", " Reset current, Import existing or Enter new data"},
		{"Msg_toolTipText", ""},
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
		{"Help_toolTipText", ""},
		{"about_text", "About..."},
		{"about_actionCommand", "About..."},
		{"about_toolTipText", ""},
		//}}End Automatic Code Generation Block
		{"th_putOut", "TyNewRTNotify.html"},
		{"Msg_text_1", " Enter Function Name"},
		{"case_NotifySuccess", " Successful"},
		{"case__1__Msg_text", " Connection error"},
		{"case_NotifyNoCurr", " No notification in use"},
		{"case_NotifyNotNec", " Notification doesn't need resetting"},
		{"case_NotifyWriteOldFail", "Write to original notification failed"},
		{"case_NotifyFailVeryOld", "Failed to verify original"},
		{"case_NotifyCreateFail", "New notification failed to create"},
		{"case_NotifyWriteNewFail", "Write to new notificaion failed"},
		{"case_NotifyFailVeryNew", "Failed to verify new notification"}, 
    {"case_NotifyInvFunc", "Function Name does not exist on Server"},
    {"case_NotifyNotInUse", "Notification stopped on Server"},
    {"case_NotifyStopped", "Requested Notification stopped on server"},
    
	};
	public Object[][] getContents()
	{
		return contents;
	}
}
