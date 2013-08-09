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

import java.awt.*;
import javax.swing.*;
import java.util.*;

/**
 * The Configuration File display frame.
 */
public class TyCfg extends javax.swing.JFrame
{
  private static final long serialVersionUID = 1L;

  // Used by addNotify
  boolean frameSizeAdjusted = false;

  //{{DECLARE_CONTROLS
	javax.swing.JTabbedPane TabbedPane = new javax.swing.JTabbedPane();
  
	javax.swing.JPanel GeneralPanel = new javax.swing.JPanel();
	javax.swing.JLabel MonLabel = new javax.swing.JLabel();
	javax.swing.JTextField monText = new javax.swing.JTextField();
	javax.swing.JTextField actText = new javax.swing.JTextField();
	javax.swing.JLabel ActLabel = new javax.swing.JLabel();
	javax.swing.JTextField notifyText = new javax.swing.JTextField();
	javax.swing.JLabel NotifyLabel = new javax.swing.JLabel();
  
  javax.swing.JLabel FileLabel = new javax.swing.JLabel();
	javax.swing.JButton ImportButton = new javax.swing.JButton();
	javax.swing.JButton FinishButton = new javax.swing.JButton();
  
  javax.swing.JLabel PrefLabel = new javax.swing.JLabel();
  javax.swing.JButton GetButton = new javax.swing.JButton();
  javax.swing.JButton PutButton = new javax.swing.JButton();
  
  javax.swing.JPanel SecurityPanel = new javax.swing.JPanel();
  javax.swing.JTextField LoginContextText = new javax.swing.JTextField();
  javax.swing.JLabel LoginContextLabel = new javax.swing.JLabel();
  
	javax.swing.JCheckBox SysExit = new javax.swing.JCheckBox();
	javax.swing.JPanel ExitsPanel = new javax.swing.JPanel();
	javax.swing.JScrollPane StartupScrollPane = new javax.swing.JScrollPane();
	javax.swing.JList StartupList = new javax.swing.JList();
	javax.swing.JScrollPane Shut1ScrollPane = new javax.swing.JScrollPane();
	javax.swing.JList Shut1List = new javax.swing.JList();
	javax.swing.JScrollPane Shut2ScrollPane = new javax.swing.JScrollPane();
	javax.swing.JList Shut2List = new javax.swing.JList();
	javax.swing.JLabel StartupLabel = new javax.swing.JLabel();
	javax.swing.JLabel Shut1Label = new javax.swing.JLabel();
	javax.swing.JLabel Shut2Label = new javax.swing.JLabel();
	javax.swing.JTextField StartupText = new javax.swing.JTextField();
	javax.swing.JTextField Shut1Text = new javax.swing.JTextField();
	javax.swing.JTextField Shut2Text = new javax.swing.JTextField();
	javax.swing.JButton StartupAddButton = new javax.swing.JButton();
	javax.swing.JButton StartupRemoveButton = new javax.swing.JButton();
	javax.swing.JButton Shut1AddButton = new javax.swing.JButton();
	javax.swing.JButton Shut1RemoveButton = new javax.swing.JButton();
	javax.swing.JButton Shut2AddButton = new javax.swing.JButton();
	javax.swing.JButton Shut2RemoveButton = new javax.swing.JButton();
	javax.swing.JLabel StartupFuncLabel = new javax.swing.JLabel();
	javax.swing.JScrollPane StartupFuncScrollPane = new javax.swing.JScrollPane();
	javax.swing.JList StartupFuncList = new javax.swing.JList();
	javax.swing.JTextField StartupFuncText = new javax.swing.JTextField();
	javax.swing.JButton StartupAddFuncButton = new javax.swing.JButton();
	javax.swing.JButton StartupRemoveFuncButton = new javax.swing.JButton();
	javax.swing.JPanel DBMSPanel = new javax.swing.JPanel();
	javax.swing.JTextField urlText = new javax.swing.JTextField();
	javax.swing.JTextField managerText = new javax.swing.JTextField();
	javax.swing.JTextField userText = new javax.swing.JTextField();
	javax.swing.JLabel URlLabel = new javax.swing.JLabel();
	javax.swing.JLabel ManagerLabel = new javax.swing.JLabel();
	javax.swing.JLabel UserLabel = new javax.swing.JLabel();
	javax.swing.JLabel passLabel = new javax.swing.JLabel();
	javax.swing.JPasswordField passText = new javax.swing.JPasswordField();
	javax.swing.JTextField queText = new javax.swing.JTextField();
	javax.swing.JTextField funcText = new javax.swing.JTextField();
	javax.swing.JTextField listText = new javax.swing.JTextField();
	javax.swing.JLabel QueLabel = new javax.swing.JLabel();
	javax.swing.JLabel FuncLabel = new javax.swing.JLabel();
	javax.swing.JLabel ListLabel = new javax.swing.JLabel();
	javax.swing.JTextField statsText = new javax.swing.JTextField();
	javax.swing.JTextField logText = new javax.swing.JTextField();
	javax.swing.JLabel StatsLabel = new javax.swing.JLabel();
	javax.swing.JLabel LogLabel = new javax.swing.JLabel();
	javax.swing.JLabel JLabel1 = new javax.swing.JLabel();
	javax.swing.JPanel AltPanel = new javax.swing.JPanel();
	javax.swing.JLabel HeaderLabel = new javax.swing.JLabel();
	javax.swing.JTextField statsDirText = new javax.swing.JTextField();
	javax.swing.JTextField statsFileText = new javax.swing.JTextField();
	javax.swing.JTextField logDirText = new javax.swing.JTextField();
	javax.swing.JTextField logFileText = new javax.swing.JTextField();
	javax.swing.JLabel StatsDirLabel = new javax.swing.JLabel();
	javax.swing.JLabel StatsFileLabel = new javax.swing.JLabel();
	javax.swing.JLabel LogDirLabel = new javax.swing.JLabel();
	javax.swing.JLabel LogFileLabel = new javax.swing.JLabel();
	javax.swing.JLabel AltLabel = new javax.swing.JLabel();
	javax.swing.JTextField statsAltText = new javax.swing.JTextField();
	javax.swing.JTextField logAltText = new javax.swing.JTextField();
	javax.swing.JLabel StatsAltLabel = new javax.swing.JLabel();
	javax.swing.JLabel LogAltLabel = new javax.swing.JLabel();
	java.awt.FileDialog ReadCfgFile = new java.awt.FileDialog(this);
	java.awt.FileDialog WriteCfgFile = new java.awt.FileDialog(this);
	javax.swing.JScrollPane JScrollPane1 = new javax.swing.JScrollPane();
	javax.swing.JScrollPane JScrollPane2 = new javax.swing.JScrollPane();
	javax.swing.JScrollPane JScrollPane3 = new javax.swing.JScrollPane();
	javax.swing.JMenuBar JMenuBar1 = new javax.swing.JMenuBar();
	javax.swing.JMenu fileMenu = new javax.swing.JMenu();
	javax.swing.JMenuItem exitItem = new javax.swing.JMenuItem();
	javax.swing.JSeparator JSeparator1 = new javax.swing.JSeparator();
	javax.swing.JMenu helpMenu = new javax.swing.JMenu();
	javax.swing.JMenuItem Help = new javax.swing.JMenuItem();
	javax.swing.JMenuItem about = new javax.swing.JMenuItem();
	//}}

	TyCfgBean2 TyCfgBean21 = new TyCfgBean2();

	
  class SymWindow extends java.awt.event.WindowAdapter
  {
    public void windowClosing(java.awt.event.WindowEvent event)
    {
      Object object = event.getSource();
      if (object == TyCfg.this)
        TyCfg_windowClosing(event);
    }
  }

  class SymAction implements java.awt.event.ActionListener
  {
    public void actionPerformed(java.awt.event.ActionEvent event)
    { 
      Object object = event.getSource();
      if (object == exitItem)
        exitItem_actionPerformed(event);
      else if (object == Help)
        Help_actionPerformed(event);
      else  if (object == about)
        about_actionPerformed(event);
			else if (object == ImportButton)
				ImportButton_actionPerformed(event);
			else if (object == FinishButton)
				FinishButton_actionPerformed(event);
        
      else if (object == GetButton)
        GetButton_actionPerformed(event);
      else if (object == PutButton)
        PutButton_actionPerformed(event);  
        
			else if (object == StartupAddButton)
				StartupAddButton_actionPerformed(event);
			else if (object == StartupRemoveButton)
				StartupRemoveButton_actionPerformed(event);
			else if (object == Shut1AddButton)
				Shut1AddButton_actionPerformed(event);
			else if (object == Shut1RemoveButton)
				Shut1RemoveButton_actionPerformed(event);
			else if (object == Shut2AddButton)
				Shut2AddButton_actionPerformed(event);
			else if (object == Shut2RemoveButton)
				Shut2RemoveButton_actionPerformed(event);
			else if (object == StartupAddFuncButton)
				StartupAddFuncButton_actionPerformed(event);
			else if (object == StartupRemoveFuncButton)
				StartupRemoveFuncButton_actionPerformed(event);          
      
    }
  }

	class SymListSelection implements javax.swing.event.ListSelectionListener
	{
		public void valueChanged(javax.swing.event.ListSelectionEvent event)
		{
			Object object = event.getSource();
			if (object == StartupList)
				StartupList_valueChanged(event);
			else if (object == Shut1List)
				Shut1List_valueChanged(event);
			else if (object == Shut2List)
				Shut2List_valueChanged(event);
			else if (object == StartupFuncList)
				StartupFuncList_valueChanged(event);
		}
	}

	static java.util.ResourceBundle tyCfgBundle = java.util.ResourceBundle.getBundle("com.tymeac.client.jframe.TyCfgBundle");
  public TyCfg()
  {
    // This code is automatically generated by Visual Cafe when you add
    // components to the visual environment. It instantiates and initializes
    // the components. To modify the code, only use code syntax that matches
    // what Visual Cafe can generate, or Visual Cafe may be unable to back
    // parse your Java file into its visual environment.
    //{{INIT_CONTROLS
    setJMenuBar(JMenuBar1);
    setTitle(tyCfgBundle.getString("TyCfg_title"));
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    getContentPane().setLayout(null);
    setSize(650,420);
		setVisible(false);
		TabbedPane.setToolTipText(tyCfgBundle.getString("TabbedPane_toolTipText").length() != 0 ? tyCfgBundle.getString("TabbedPane_toolTipText") : null);
		getContentPane().add(TabbedPane);
		TabbedPane.setBounds(0,0,650,420);
		GeneralPanel.setToolTipText(tyCfgBundle.getString("GeneralPanel_toolTipText").length() != 0 ? tyCfgBundle.getString("GeneralPanel_toolTipText") : null);
    //$$ JMenuBar1.move(156,432);
    fileMenu.setText(tyCfgBundle.getString("fileMenu_text"));
    fileMenu.setActionCommand(tyCfgBundle.getString("fileMenu_actionCommand"));
		fileMenu.setToolTipText(tyCfgBundle.getString("fileMenu_toolTipText").length() != 0 ? tyCfgBundle.getString("fileMenu_toolTipText") : null);
    fileMenu.setMnemonic((int)'F');
    JMenuBar1.add(fileMenu);
    
    fileMenu.add(JSeparator1);
    
    exitItem.setText(tyCfgBundle.getString("exitItem_text"));
    exitItem.setActionCommand(tyCfgBundle.getString("exitItem_actionCommand"));
		exitItem.setToolTipText(tyCfgBundle.getString("exitItem_toolTipText").length() != 0 ? tyCfgBundle.getString("exitItem_toolTipText") : null);
    exitItem.setMnemonic((int)'X');
    fileMenu.add(exitItem);
		JSeparator1.setToolTipText(tyCfgBundle.getString("JSeparator1_toolTipText").length() != 0 ? tyCfgBundle.getString("JSeparator1_toolTipText") : null);
    
    helpMenu.setText(tyCfgBundle.getString("helpMenu_text"));
    helpMenu.setActionCommand(tyCfgBundle.getString("helpMenu_actionCommand"));
		helpMenu.setToolTipText(tyCfgBundle.getString("helpMenu_toolTipText").length() != 0 ? tyCfgBundle.getString("helpMenu_toolTipText") : null);
    helpMenu.setMnemonic((int)'H');
    JMenuBar1.add(helpMenu);
    
    fileMenu.add(JSeparator1);
       
    Help.setText(tyCfgBundle.getString("Help_text"));
    Help.setActionCommand(tyCfgBundle.getString("Help_actionCommand"));
		Help.setToolTipText(tyCfgBundle.getString("Help_toolTipText").length() != 0 ? tyCfgBundle.getString("Help_toolTipText") : null);
    Help.setMnemonic((int)'D');
    helpMenu.add(Help);
    
    fileMenu.add(JSeparator1);
    
    about.setText(tyCfgBundle.getString("about_text"));
    about.setActionCommand(tyCfgBundle.getString("about_actionCommand"));
		about.setToolTipText(tyCfgBundle.getString("about_toolTipText").length() != 0 ? tyCfgBundle.getString("about_toolTipText") : null);
    about.setMnemonic((int)'A');
    helpMenu.add(about); 

		//$$ OpenCfgFile.move(0,421);
		//$$ OpenFileDialog.move(0,421);
		//$$ WiteCfgFile.move(24,421);
		//$$ SaveFileDialog.move(24,421);


		GeneralPanel.setLayout(null);
		TabbedPane.add(GeneralPanel);
		GeneralPanel.setBounds(2,27,645,390);
		GeneralPanel.setVisible(false);


		MonLabel.setText(tyCfgBundle.getString("MonLabel_text"));
		MonLabel.setToolTipText(tyCfgBundle.getString("MonLabel_toolTipText").length() != 0 ? tyCfgBundle.getString("MonLabel_toolTipText") : null);
		GeneralPanel.add(MonLabel);
		MonLabel.setBounds(82,105,288,40);
		monText.setText(tyCfgBundle.getString("monText_text"));
		monText.setToolTipText(tyCfgBundle.getString("monText_toolTipText").length() != 0 ? tyCfgBundle.getString("monText_toolTipText") : null);
		GeneralPanel.add(monText);
		monText.setBounds(46,115,30,24);
		actText.setText(tyCfgBundle.getString("actText_text"));
		actText.setToolTipText(tyCfgBundle.getString("actText_toolTipText").length() != 0 ? tyCfgBundle.getString("actText_toolTipText") : null);
		GeneralPanel.add(actText);
		actText.setBounds(46,190,30,24);
		ActLabel.setText(tyCfgBundle.getString("ActLabel_text"));
		ActLabel.setToolTipText(tyCfgBundle.getString("ActLabel_toolTipText").length() != 0 ? tyCfgBundle.getString("ActLabel_toolTipText") : null);
		GeneralPanel.add(ActLabel);
		ActLabel.setBounds(82,180,288,40);
		notifyText.setText(tyCfgBundle.getString("notifyText_text"));
		notifyText.setToolTipText(tyCfgBundle.getString("notifyText_toolTipText").length() != 0 ? tyCfgBundle.getString("notifyText_toolTipText") : null);
		GeneralPanel.add(notifyText);
		notifyText.setBounds(46,255,420,30);
		NotifyLabel.setText(tyCfgBundle.getString("NotifyLabel_text"));
		NotifyLabel.setToolTipText(tyCfgBundle.getString("NotifyLabel_toolTipText").length() != 0 ? tyCfgBundle.getString("NotifyLabel_toolTipText") : null);
		GeneralPanel.add(NotifyLabel);
		NotifyLabel.setBounds(46,285,180,24);
    
    FileLabel.setText(tyCfgBundle.getString("FileLabel_text"));
    GeneralPanel.add(FileLabel);
    FileLabel.setBounds(46,310,288,20);
    
		ImportButton.setText(tyCfgBundle.getString("ImportButton_text"));
		ImportButton.setActionCommand(tyCfgBundle.getString("ImportButton_actionCommand"));
		ImportButton.setToolTipText(tyCfgBundle.getString("ImportButton_toolTipText").length() != 0 ? tyCfgBundle.getString("ImportButton_toolTipText") : null);
		GeneralPanel.add(ImportButton);    
		ImportButton.setBounds(46,333,75,25);
    
		FinishButton.setText(tyCfgBundle.getString("FinishButton_text"));
		FinishButton.setActionCommand(tyCfgBundle.getString("FinishButton_actionCommand"));
		FinishButton.setToolTipText(tyCfgBundle.getString("FinishButton_toolTipText").length() != 0 ? tyCfgBundle.getString("FinishButton_toolTipText") : null);
		GeneralPanel.add(FinishButton);
		FinishButton.setBounds(130,333,75,25);
    
    PrefLabel.setText(tyCfgBundle.getString("PrefLabel_text"));
    GeneralPanel.add(PrefLabel);
    PrefLabel.setBounds(320,310,288,20);
    
    GetButton.setText(tyCfgBundle.getString("GetButton_text"));
    GetButton.setActionCommand(tyCfgBundle.getString("GetButton_actionCommand"));
    GetButton.setToolTipText(tyCfgBundle.getString("GetButton_toolTipText").length() != 0 ? tyCfgBundle.getString("ImportButton_toolTipText") : null);
    GeneralPanel.add(GetButton);    
    GetButton.setBounds(320,333,75,25);
    
    PutButton.setText(tyCfgBundle.getString("PutButton_text"));
    PutButton.setActionCommand(tyCfgBundle.getString("PutButton_actionCommand"));
    PutButton.setToolTipText(tyCfgBundle.getString("PutButton_toolTipText").length() != 0 ? tyCfgBundle.getString("ImportButton_toolTipText") : null);
    GeneralPanel.add(PutButton);    
    PutButton.setBounds(410,333,75,25);
    
    
    SecurityPanel.setToolTipText(tyCfgBundle.getString("GeneralPanel_toolTipText").length() != 0 ? tyCfgBundle.getString("GeneralPanel_toolTipText") : null);
    SecurityPanel.setLayout(null);
    TabbedPane.add(SecurityPanel);
    SecurityPanel.setBounds(2,27,645,390);
    SecurityPanel.setVisible(false);
    
    LoginContextLabel.setText(tyCfgBundle.getString("LoginContextLabel_text"));
    SecurityPanel.add(LoginContextLabel);
    LoginContextLabel.setBounds(46,285,180,24);
    
    LoginContextText.setText(tyCfgBundle.getString("LoginContextText_text"));
    LoginContextText.setToolTipText(tyCfgBundle.getString("LoginContextText_toolTipText").length() != 0 ? tyCfgBundle.getString("LoginContextText_toolTipText") : null);
    SecurityPanel.add(LoginContextText);
    LoginContextText.setBounds(46,255,420,30);
    
    
		SysExit.setText(tyCfgBundle.getString("SysExit_text"));
		SysExit.setActionCommand(tyCfgBundle.getString("SysExit_actionCommand"));
		SysExit.setSelected(true);
		SysExit.setToolTipText(tyCfgBundle.getString("SysExit_toolTipText").length() != 0 ? tyCfgBundle.getString("SysExit_toolTipText") : null);
		GeneralPanel.add(SysExit);
		SysExit.setFont(new Font("Dialog", Font.PLAIN, 12));
		SysExit.setBounds(55,50,200,40);
		ExitsPanel.setToolTipText(tyCfgBundle.getString("ExitsPanel_toolTipText").length() != 0 ? tyCfgBundle.getString("ExitsPanel_toolTipText") : null);
		ExitsPanel.setLayout(null);
		TabbedPane.add(ExitsPanel);
		ExitsPanel.setBounds(2,27,645,390);
		ExitsPanel.setVisible(false);
		StartupScrollPane.setOpaque(true);
		StartupScrollPane.setToolTipText(tyCfgBundle.getString("StartupScrollPane_toolTipText").length() != 0 ? tyCfgBundle.getString("StartupScrollPane_toolTipText") : null);
		ExitsPanel.add(StartupScrollPane);
		StartupScrollPane.setBounds(106,33,155,60);
		StartupList.setToolTipText(tyCfgBundle.getString("StartupList_toolTipText").length() != 0 ? tyCfgBundle.getString("StartupList_toolTipText") : null);
		StartupScrollPane.getViewport().add(StartupList);
		StartupList.setBounds(0,0,152,57);
		Shut1ScrollPane.setOpaque(true);
		Shut1ScrollPane.setToolTipText(tyCfgBundle.getString("Shut1ScrollPane_toolTipText").length() != 0 ? tyCfgBundle.getString("Shut1ScrollPane_toolTipText") : null);
		ExitsPanel.add(Shut1ScrollPane);
		Shut1ScrollPane.setBounds(106,237,155,60);
		Shut1List.setToolTipText(tyCfgBundle.getString("Shut1List_toolTipText").length() != 0 ? tyCfgBundle.getString("Shut1List_toolTipText") : null);
		Shut1ScrollPane.getViewport().add(Shut1List);
		Shut1List.setBounds(0,0,152,57);
		Shut2ScrollPane.setOpaque(true);
		Shut2ScrollPane.setToolTipText(tyCfgBundle.getString("Shut2ScrollPane_toolTipText").length() != 0 ? tyCfgBundle.getString("Shut2ScrollPane_toolTipText") : null);
		ExitsPanel.add(Shut2ScrollPane);
		Shut2ScrollPane.setBounds(322,238,155,60);
		Shut2List.setToolTipText(tyCfgBundle.getString("Shut2List_toolTipText").length() != 0 ? tyCfgBundle.getString("Shut2List_toolTipText") : null);
		Shut2ScrollPane.getViewport().add(Shut2List);
		Shut2List.setBounds(0,0,152,57);
		StartupLabel.setText(tyCfgBundle.getString("StartupLabel_text"));
		StartupLabel.setToolTipText(tyCfgBundle.getString("StartupLabel_toolTipText").length() != 0 ? tyCfgBundle.getString("StartupLabel_toolTipText") : null);
		ExitsPanel.add(StartupLabel);
		StartupLabel.setBounds(106,9,120,24);
		Shut1Label.setText(tyCfgBundle.getString("Shut1Label_text"));
		Shut1Label.setToolTipText(tyCfgBundle.getString("Shut1Label_toolTipText").length() != 0 ? tyCfgBundle.getString("Shut1Label_toolTipText") : null);
		ExitsPanel.add(Shut1Label);
		Shut1Label.setBounds(106,213,160,24);
		Shut2Label.setText(tyCfgBundle.getString("Shut2Label_text"));
		Shut2Label.setToolTipText(tyCfgBundle.getString("Shut2Label_toolTipText").length() != 0 ? tyCfgBundle.getString("Shut2Label_toolTipText") : null);
		ExitsPanel.add(Shut2Label);
		Shut2Label.setBounds(322,213,160,24);
		StartupText.setText(tyCfgBundle.getString("StartupText_text"));
		StartupText.setToolTipText(tyCfgBundle.getString("StartupText_toolTipText").length() != 0 ? tyCfgBundle.getString("StartupText_toolTipText") : null);
		ExitsPanel.add(StartupText);
		StartupText.setBounds(107,105,155,29);
		Shut1Text.setText(tyCfgBundle.getString("Shut1Text_text"));
		Shut1Text.setToolTipText(tyCfgBundle.getString("Shut1Text_toolTipText").length() != 0 ? tyCfgBundle.getString("Shut1Text_toolTipText") : null);
		ExitsPanel.add(Shut1Text);
		Shut1Text.setBounds(107,309,155,29);
		Shut2Text.setText(tyCfgBundle.getString("Shut2Text_text"));
		Shut2Text.setToolTipText(tyCfgBundle.getString("Shut2Text_toolTipText").length() != 0 ? tyCfgBundle.getString("Shut2Text_toolTipText") : null);
		ExitsPanel.add(Shut2Text);
		Shut2Text.setBounds(323,309,155,29);
		StartupAddButton.setText(tyCfgBundle.getString("StartupAddButton_text"));
		StartupAddButton.setActionCommand(tyCfgBundle.getString("StartupAddButton_actionCommand"));
		StartupAddButton.setToolTipText(tyCfgBundle.getString("StartupAddButton_toolTipText").length() != 0 ? tyCfgBundle.getString("StartupAddButton_toolTipText") : null);
		ExitsPanel.add(StartupAddButton);
		StartupAddButton.setBounds(107,141,60,25);
		StartupRemoveButton.setText(tyCfgBundle.getString("StartupRemoveButton_text"));
		StartupRemoveButton.setActionCommand(tyCfgBundle.getString("StartupRemoveButton_actionCommand"));
		StartupRemoveButton.setToolTipText(tyCfgBundle.getString("StartupRemoveButton_toolTipText").length() != 0 ? tyCfgBundle.getString("StartupRemoveButton_toolTipText") : null);
		StartupRemoveButton.setEnabled(false);
		ExitsPanel.add(StartupRemoveButton);
		StartupRemoveButton.setBounds(182,141,80,25);
		Shut1AddButton.setText(tyCfgBundle.getString("Shut1AddButton_text"));
		Shut1AddButton.setActionCommand(tyCfgBundle.getString("Shut1AddButton_actionCommand"));
		Shut1AddButton.setToolTipText(tyCfgBundle.getString("Shut1AddButton_toolTipText").length() != 0 ? tyCfgBundle.getString("Shut1AddButton_toolTipText") : null);
		ExitsPanel.add(Shut1AddButton);
		Shut1AddButton.setBounds(107,345,60,25);
		Shut1RemoveButton.setText(tyCfgBundle.getString("Shut1RemoveButton_text"));
		Shut1RemoveButton.setActionCommand(tyCfgBundle.getString("Shut1RemoveButton_actionCommand"));
		Shut1RemoveButton.setToolTipText(tyCfgBundle.getString("Shut1RemoveButton_toolTipText").length() != 0 ? tyCfgBundle.getString("Shut1RemoveButton_toolTipText") : null);
		Shut1RemoveButton.setEnabled(false);
		ExitsPanel.add(Shut1RemoveButton);
		Shut1RemoveButton.setBounds(182,345,80,25);
		Shut2AddButton.setText(tyCfgBundle.getString("Shut2AddButton_text"));
		Shut2AddButton.setActionCommand(tyCfgBundle.getString("Shut2AddButton_actionCommand"));
		Shut2AddButton.setToolTipText(tyCfgBundle.getString("Shut2AddButton_toolTipText").length() != 0 ? tyCfgBundle.getString("Shut2AddButton_toolTipText") : null);
		ExitsPanel.add(Shut2AddButton);
		Shut2AddButton.setBounds(323,345,60,25);
		Shut2RemoveButton.setText(tyCfgBundle.getString("Shut2RemoveButton_text"));
		Shut2RemoveButton.setActionCommand(tyCfgBundle.getString("Shut2RemoveButton_actionCommand"));
		Shut2RemoveButton.setToolTipText(tyCfgBundle.getString("Shut2RemoveButton_toolTipText").length() != 0 ? tyCfgBundle.getString("Shut2RemoveButton_toolTipText") : null);
		Shut2RemoveButton.setEnabled(false);
		ExitsPanel.add(Shut2RemoveButton);
		Shut2RemoveButton.setBounds(398,345,80,25);
		StartupFuncLabel.setText(tyCfgBundle.getString("StartupFuncLabel_text"));
		StartupFuncLabel.setToolTipText(tyCfgBundle.getString("StartupFuncLabel_toolTipText").length() != 0 ? tyCfgBundle.getString("StartupFuncLabel_toolTipText") : null);
		ExitsPanel.add(StartupFuncLabel);
		StartupFuncLabel.setBounds(322,9,120,24);
		StartupFuncScrollPane.setOpaque(true);
		StartupFuncScrollPane.setToolTipText(tyCfgBundle.getString("StartupFuncScrollPane_toolTipText").length() != 0 ? tyCfgBundle.getString("StartupFuncScrollPane_toolTipText") : null);
		ExitsPanel.add(StartupFuncScrollPane);
		StartupFuncScrollPane.setBounds(322,33,155,60);
		StartupFuncList.setToolTipText(tyCfgBundle.getString("StartupFuncList_toolTipText").length() != 0 ? tyCfgBundle.getString("StartupFuncList_toolTipText") : null);
		StartupFuncScrollPane.getViewport().add(StartupFuncList);
		StartupFuncList.setBounds(0,0,152,57);
		StartupFuncText.setText(tyCfgBundle.getString("StartupFuncText_text"));
		StartupFuncText.setToolTipText(tyCfgBundle.getString("StartupFuncText_toolTipText").length() != 0 ? tyCfgBundle.getString("StartupFuncText_toolTipText") : null);
		ExitsPanel.add(StartupFuncText);
		StartupFuncText.setBounds(323,105,155,29);
		StartupAddFuncButton.setText(tyCfgBundle.getString("StartupAddFuncButton_text"));
		StartupAddFuncButton.setActionCommand(tyCfgBundle.getString("StartupAddFuncButton_actionCommand"));
		StartupAddFuncButton.setToolTipText(tyCfgBundle.getString("StartupAddFuncButton_toolTipText").length() != 0 ? tyCfgBundle.getString("StartupAddFuncButton_toolTipText") : null);
		ExitsPanel.add(StartupAddFuncButton);
		StartupAddFuncButton.setBounds(323,141,60,25);
		StartupRemoveFuncButton.setText(tyCfgBundle.getString("StartupRemoveFuncButton_text"));
		StartupRemoveFuncButton.setActionCommand(tyCfgBundle.getString("StartupRemoveFuncButton_actionCommand"));
		StartupRemoveFuncButton.setToolTipText(tyCfgBundle.getString("StartupRemoveFuncButton_toolTipText").length() != 0 ? tyCfgBundle.getString("StartupRemoveFuncButton_toolTipText") : null);
		StartupRemoveFuncButton.setEnabled(false);
		ExitsPanel.add(StartupRemoveFuncButton);
		StartupRemoveFuncButton.setBounds(398,141,80,25);
		DBMSPanel.setToolTipText(tyCfgBundle.getString("DBMSPanel_toolTipText").length() != 0 ? tyCfgBundle.getString("DBMSPanel_toolTipText") : null);


		DBMSPanel.setLayout(null);
		TabbedPane.add(DBMSPanel);
		DBMSPanel.setBounds(2,27,645,390);
		DBMSPanel.setVisible(false);
		urlText.setText(tyCfgBundle.getString("urlText_text"));
		urlText.setToolTipText(tyCfgBundle.getString("urlText_toolTipText").length() != 0 ? tyCfgBundle.getString("urlText_toolTipText") : null);
		DBMSPanel.add(urlText);
		urlText.setBounds(10,21,119,30);
		managerText.setText(tyCfgBundle.getString("managerText_text"));
		managerText.setToolTipText(tyCfgBundle.getString("managerText_toolTipText").length() != 0 ? tyCfgBundle.getString("managerText_toolTipText") : null);
		DBMSPanel.add(managerText);
		managerText.setBounds(174,21,119,30);
		userText.setText(tyCfgBundle.getString("userText_text"));
		userText.setToolTipText(tyCfgBundle.getString("userText_toolTipText").length() != 0 ? tyCfgBundle.getString("userText_toolTipText") : null);
		DBMSPanel.add(userText);
		userText.setBounds(338,21,119,30);
		URlLabel.setText(tyCfgBundle.getString("URlLabel_text"));
		URlLabel.setToolTipText(tyCfgBundle.getString("URlLabel_toolTipText").length() != 0 ? tyCfgBundle.getString("URlLabel_toolTipText") : null);
		DBMSPanel.add(URlLabel);
		URlLabel.setBounds(10,50,100,24);
		ManagerLabel.setText(tyCfgBundle.getString("ManagerLabel_text"));
		ManagerLabel.setToolTipText(tyCfgBundle.getString("ManagerLabel_toolTipText").length() != 0 ? tyCfgBundle.getString("ManagerLabel_toolTipText") : null);
		DBMSPanel.add(ManagerLabel);
		ManagerLabel.setBounds(174,50,100,24);
		UserLabel.setText(tyCfgBundle.getString("UserLabel_text"));
		UserLabel.setToolTipText(tyCfgBundle.getString("UserLabel_toolTipText").length() != 0 ? tyCfgBundle.getString("UserLabel_toolTipText") : null);
		DBMSPanel.add(UserLabel);
		UserLabel.setBounds(338,50,100,24);
		passLabel.setText(tyCfgBundle.getString("passLabel_text"));
		passLabel.setToolTipText(tyCfgBundle.getString("passLabel_toolTipText").length() != 0 ? tyCfgBundle.getString("passLabel_toolTipText") : null);
		DBMSPanel.add(passLabel);
		passLabel.setBounds(502,50,100,24);
		passText.setText(tyCfgBundle.getString("passText_text"));
		passText.setToolTipText(tyCfgBundle.getString("passText_toolTipText").length() != 0 ? tyCfgBundle.getString("passText_toolTipText") : null);
		DBMSPanel.add(passText);
		passText.setBounds(502,21,119,30);
		queText.setText(tyCfgBundle.getString("queText_text"));
		queText.setToolTipText(tyCfgBundle.getString("queText_toolTipText").length() != 0 ? tyCfgBundle.getString("queText_toolTipText") : null);
		DBMSPanel.add(queText);
		queText.setBounds(10,129,119,30);
		funcText.setText(tyCfgBundle.getString("funcText_text"));
		funcText.setToolTipText(tyCfgBundle.getString("funcText_toolTipText").length() != 0 ? tyCfgBundle.getString("funcText_toolTipText") : null);
		DBMSPanel.add(funcText);
		funcText.setBounds(174,129,119,30);
		listText.setText(tyCfgBundle.getString("listText_text"));
		listText.setToolTipText(tyCfgBundle.getString("listText_toolTipText").length() != 0 ? tyCfgBundle.getString("listText_toolTipText") : null);
		DBMSPanel.add(listText);
		listText.setBounds(338,129,119,30);
		QueLabel.setText(tyCfgBundle.getString("QueLabel_text"));
		QueLabel.setToolTipText(tyCfgBundle.getString("QueLabel_toolTipText").length() != 0 ? tyCfgBundle.getString("QueLabel_toolTipText") : null);
		DBMSPanel.add(QueLabel);
		QueLabel.setBounds(10,160,100,24);
		FuncLabel.setText(tyCfgBundle.getString("FuncLabel_text"));
		FuncLabel.setToolTipText(tyCfgBundle.getString("FuncLabel_toolTipText").length() != 0 ? tyCfgBundle.getString("FuncLabel_toolTipText") : null);
		DBMSPanel.add(FuncLabel);
		FuncLabel.setBounds(174,160,100,24);
		ListLabel.setText(tyCfgBundle.getString("ListLabel_text"));
		ListLabel.setToolTipText(tyCfgBundle.getString("ListLabel_toolTipText").length() != 0 ? tyCfgBundle.getString("ListLabel_toolTipText") : null);
		DBMSPanel.add(ListLabel);
		ListLabel.setBounds(338,160,100,24);
		statsText.setText(tyCfgBundle.getString("statsText_text"));
		statsText.setToolTipText(tyCfgBundle.getString("statsText_toolTipText").length() != 0 ? tyCfgBundle.getString("statsText_toolTipText") : null);
		DBMSPanel.add(statsText);
		statsText.setBounds(10,237,119,30);
		logText.setText(tyCfgBundle.getString("logText_text"));
		logText.setToolTipText(tyCfgBundle.getString("logText_toolTipText").length() != 0 ? tyCfgBundle.getString("logText_toolTipText") : null);
		DBMSPanel.add(logText);
		logText.setBounds(174,237,119,30);
		StatsLabel.setText(tyCfgBundle.getString("StatsLabel_text"));
		StatsLabel.setToolTipText(tyCfgBundle.getString("StatsLabel_toolTipText").length() != 0 ? tyCfgBundle.getString("StatsLabel_toolTipText") : null);
		DBMSPanel.add(StatsLabel);
		StatsLabel.setBounds(10,266,100,24);
		LogLabel.setText(tyCfgBundle.getString("LogLabel_text"));
		LogLabel.setToolTipText(tyCfgBundle.getString("LogLabel_toolTipText").length() != 0 ? tyCfgBundle.getString("LogLabel_toolTipText") : null);
		DBMSPanel.add(LogLabel);
		LogLabel.setBounds(174,266,100,24);
		JLabel1.setText(tyCfgBundle.getString("JLabel1_text"));
		JLabel1.setToolTipText(tyCfgBundle.getString("JLabel1_toolTipText").length() != 0 ? tyCfgBundle.getString("JLabel1_toolTipText") : null);
		DBMSPanel.add(JLabel1);
		JLabel1.setForeground(java.awt.Color.blue);
		JLabel1.setFont(new Font("Dialog", Font.BOLD, 13));
		JLabel1.setBounds(10,309,324,24);
		AltPanel.setToolTipText(tyCfgBundle.getString("AltPanel_toolTipText").length() != 0 ? tyCfgBundle.getString("AltPanel_toolTipText") : null);
		AltPanel.setLayout(null);
		TabbedPane.add(AltPanel);
		AltPanel.setFont(new Font("Dialog", Font.PLAIN, 12));
		AltPanel.setBounds(2,27,645,390);
		AltPanel.setVisible(false);
		HeaderLabel.setText(tyCfgBundle.getString("HeaderLabel_text"));
		HeaderLabel.setToolTipText(tyCfgBundle.getString("HeaderLabel_toolTipText").length() != 0 ? tyCfgBundle.getString("HeaderLabel_toolTipText") : null);
		AltPanel.add(HeaderLabel);
		HeaderLabel.setForeground(java.awt.Color.blue);
		HeaderLabel.setFont(new Font("Dialog", Font.BOLD, 14));
		HeaderLabel.setBounds(238,21,168,12);
		statsDirText.setText(tyCfgBundle.getString("statsDirText_text"));
		statsDirText.setToolTipText(tyCfgBundle.getString("statsDirText_toolTipText").length() != 0 ? tyCfgBundle.getString("statsDirText_toolTipText") : null);
		AltPanel.add(statsDirText);
		statsDirText.setBounds(10,69,144,30);
		statsFileText.setText(tyCfgBundle.getString("statsFileText_text"));
		statsFileText.setToolTipText(tyCfgBundle.getString("statsFileText_toolTipText").length() != 0 ? tyCfgBundle.getString("statsFileText_toolTipText") : null);
		AltPanel.add(statsFileText);
		statsFileText.setBounds(166,69,144,30);
		logDirText.setText(tyCfgBundle.getString("logDirText_text"));
		logDirText.setToolTipText(tyCfgBundle.getString("logDirText_toolTipText").length() != 0 ? tyCfgBundle.getString("logDirText_toolTipText") : null);
		AltPanel.add(logDirText);
		logDirText.setBounds(334,69,144,30);
		logFileText.setText(tyCfgBundle.getString("logFileText_text"));
		logFileText.setToolTipText(tyCfgBundle.getString("logFileText_toolTipText").length() != 0 ? tyCfgBundle.getString("logFileText_toolTipText") : null);
		AltPanel.add(logFileText);
		logFileText.setBounds(490,69,144,30);
		StatsDirLabel.setText(tyCfgBundle.getString("StatsDirLabel_text"));
		StatsDirLabel.setToolTipText(tyCfgBundle.getString("StatsDirLabel_toolTipText").length() != 0 ? tyCfgBundle.getString("StatsDirLabel_toolTipText") : null);
		AltPanel.add(StatsDirLabel);
		StatsDirLabel.setBounds(10,93,120,30);
		StatsFileLabel.setText(tyCfgBundle.getString("StatsFileLabel_text"));
		StatsFileLabel.setToolTipText(tyCfgBundle.getString("StatsFileLabel_toolTipText").length() != 0 ? tyCfgBundle.getString("StatsFileLabel_toolTipText") : null);
		AltPanel.add(StatsFileLabel);
		StatsFileLabel.setBounds(166,93,120,30);
		LogDirLabel.setText(tyCfgBundle.getString("LogDirLabel_text"));
		LogDirLabel.setToolTipText(tyCfgBundle.getString("LogDirLabel_toolTipText").length() != 0 ? tyCfgBundle.getString("LogDirLabel_toolTipText") : null);
		AltPanel.add(LogDirLabel);
		LogDirLabel.setBounds(334,93,120,30);
		LogFileLabel.setText(tyCfgBundle.getString("LogFileLabel_text"));
		LogFileLabel.setToolTipText(tyCfgBundle.getString("LogFileLabel_toolTipText").length() != 0 ? tyCfgBundle.getString("LogFileLabel_toolTipText") : null);
		AltPanel.add(LogFileLabel);
		LogFileLabel.setBounds(490,93,120,30);
		AltLabel.setText(tyCfgBundle.getString("AltLabel_text"));
		AltLabel.setToolTipText(tyCfgBundle.getString("AltLabel_toolTipText").length() != 0 ? tyCfgBundle.getString("AltLabel_toolTipText") : null);
		AltPanel.add(AltLabel);
		AltLabel.setForeground(java.awt.Color.blue);
		AltLabel.setFont(new Font("Dialog", Font.BOLD, 14));
		AltLabel.setBounds(226,189,192,12);
		statsAltText.setText(tyCfgBundle.getString("statsAltText_text"));
		statsAltText.setToolTipText(tyCfgBundle.getString("statsAltText_toolTipText").length() != 0 ? tyCfgBundle.getString("statsAltText_toolTipText") : null);
		AltPanel.add(statsAltText);
		statsAltText.setBounds(10,237,264,30);
		logAltText.setText(tyCfgBundle.getString("logAltText_text"));
		logAltText.setToolTipText(tyCfgBundle.getString("logAltText_toolTipText").length() != 0 ? tyCfgBundle.getString("logAltText_toolTipText") : null);
		AltPanel.add(logAltText);
		logAltText.setBounds(334,237,264,30);
		StatsAltLabel.setText(tyCfgBundle.getString("StatsAltLabel_text"));
		StatsAltLabel.setToolTipText(tyCfgBundle.getString("StatsAltLabel_toolTipText").length() != 0 ? tyCfgBundle.getString("StatsAltLabel_toolTipText") : null);
		AltPanel.add(StatsAltLabel);
		StatsAltLabel.setBounds(10,261,120,30);
		LogAltLabel.setText(tyCfgBundle.getString("LogAltLabel_text"));
		LogAltLabel.setToolTipText(tyCfgBundle.getString("LogAltLabel_toolTipText").length() != 0 ? tyCfgBundle.getString("LogAltLabel_toolTipText") : null);
		AltPanel.add(LogAltLabel);
		LogAltLabel.setBounds(334,261,120,30);

		
		TabbedPane.setSelectedIndex(0);
		TabbedPane.setSelectedComponent(GeneralPanel);
		TabbedPane.setTitleAt(0,tyCfgBundle.getString("TabbedPane_titleAt0"));
		TabbedPane.setTitleAt(1,tyCfgBundle.getString("TabbedPane_titleAt1"));
		TabbedPane.setTitleAt(2,tyCfgBundle.getString("TabbedPane_titleAt2"));
		TabbedPane.setTitleAt(3,tyCfgBundle.getString("TabbedPane_titleAt3"));
    TabbedPane.setTitleAt(4,tyCfgBundle.getString("TabbedPane_titleAt4"));
    
		ReadCfgFile.setMode(FileDialog.LOAD);
		ReadCfgFile.setTitle(tyCfgBundle.getString("ReadCfgFile_title"));
		ReadCfgFile.setDirectory(tyCfgBundle.getString("ReadCfgFile_directory"));
		ReadCfgFile.setFile(tyCfgBundle.getString("ReadCfgFile_file"));
		//$$ ReadCfgFile.move(0,421);
		WriteCfgFile.setMode(FileDialog.SAVE);
		WriteCfgFile.setTitle(tyCfgBundle.getString("WriteCfgFile_title"));
		WriteCfgFile.setDirectory(tyCfgBundle.getString("WriteCfgFile_directory"));
		WriteCfgFile.setFile(tyCfgBundle.getString("WriteCfgFile_file"));
		//$$ WriteCfgFile.move(24,421);
		JScrollPane1.setToolTipText(tyCfgBundle.getString("JScrollPane1_toolTipText").length() != 0 ? tyCfgBundle.getString("JScrollPane1_toolTipText") : null);
		getContentPane().add(JScrollPane1);
		JScrollPane1.setBounds(119,33,155,60);
		JScrollPane2.setToolTipText(tyCfgBundle.getString("JScrollPane2_toolTipText").length() != 0 ? tyCfgBundle.getString("JScrollPane2_toolTipText") : null);
		getContentPane().add(JScrollPane2);
		JScrollPane2.setBounds(0,0,155,60);
		JScrollPane3.setToolTipText(tyCfgBundle.getString("JScrollPane3_toolTipText").length() != 0 ? tyCfgBundle.getString("JScrollPane3_toolTipText") : null);
		getContentPane().add(JScrollPane3);
		JScrollPane3.setBounds(0,0,155,60);
		JMenuBar1.setToolTipText(tyCfgBundle.getString("JMenuBar1_toolTipText").length() != 0 ? tyCfgBundle.getString("JMenuBar1_toolTipText") : null);

    //}}

    //{{INIT_MENUS
    //}}

    //{{REGISTER_LISTENERS
    SymWindow aSymWindow = new SymWindow();
    this.addWindowListener(aSymWindow);
    
    SymAction lSymAction = new SymAction();
    exitItem.addActionListener(lSymAction);
    Help.addActionListener(lSymAction);
    about.addActionListener(lSymAction);
    
		ImportButton.addActionListener(lSymAction);
		FinishButton.addActionListener(lSymAction);
    
    GetButton.addActionListener(lSymAction);
    PutButton.addActionListener(lSymAction);
    
		StartupAddButton.addActionListener(lSymAction);
		StartupRemoveButton.addActionListener(lSymAction);
		Shut1AddButton.addActionListener(lSymAction);
		Shut1RemoveButton.addActionListener(lSymAction);
		Shut2AddButton.addActionListener(lSymAction);
		Shut2RemoveButton.addActionListener(lSymAction);
		SymListSelection lSymListSelection = new SymListSelection();
		StartupList.addListSelectionListener(lSymListSelection);
		Shut1List.addListSelectionListener(lSymListSelection);
		Shut2List.addListSelectionListener(lSymListSelection);
		StartupAddFuncButton.addActionListener(lSymAction);
		StartupRemoveFuncButton.addActionListener(lSymAction);
		StartupFuncList.addListSelectionListener(lSymListSelection);
		//}}
  }
    /**
     * Creates a new instance of JFrame1 with the given title.
     * @param sTitle the title for the new frame.
     */
  public TyCfg(String sTitle)
  {
    this();
    setTitle(sTitle);
  }
   void about_actionPerformed(java.awt.event.ActionEvent event)
  {
    // to do: code goes here.
       
    about_actionPerformed_Interaction1(event);

  } // end-method
  void about_actionPerformed_Interaction1(java.awt.event.ActionEvent event) { 
  
    try {
      // TymeacAbout Create with owner and show as modal
      {
        TyAbout TyAbout1 = new TyAbout(this);
        TyAbout1.setModal(true);
        //TyAbout1.show();
        TyAbout1.setVisible(true);
      }
    } catch (Exception e) {
    }
  }
    /**
     * Notifies this component that it has been added to a container
     * This method should be called by <code>Container.add</code>, and 
     * not by user code directly.
     * Overridden here to adjust the size of the frame if needed.
     * @see java.awt.Container#removeNotify
     */
  public void addNotify()
  {
    // Record the size of the window prior to calling parents addNotify.
    Dimension size = getSize();
    
    super.addNotify();
    
    if (frameSizeAdjusted)
      return;
    frameSizeAdjusted = true;
    
    // Adjust size of frame according to the insets and menu bar
    javax.swing.JMenuBar menuBar = getRootPane().getJMenuBar();
    int menuBarHeight = 0;
    if (menuBar != null)
        menuBarHeight = menuBar.getPreferredSize().height;
    Insets insets = getInsets();
    setSize(insets.left + insets.right + size.width, insets.top + insets.bottom + size.height + menuBarHeight);
  }
  //{{DECLARE_MENUS
	//}}

  void exitApplication()
  {    
    this.setVisible(false);    // hide the Frame
    this.dispose();            // free the system resources
    System.exit(0);            // close the application 
    
  }
  void exitItem_actionPerformed(java.awt.event.ActionEvent event)
  {
    // to do: code goes here.
       
    exitItem_actionPerformed_Interaction1(event);
  }
  void exitItem_actionPerformed_Interaction1(java.awt.event.ActionEvent event) {
    try {
      this.exitApplication();
    } catch (Exception e) {
    }
  }
	void FinishButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		FinishButton_actionPerformed_Interaction1(event);


		String ty_dir = null;
		String ty_file = null;				

		try {
			ty_dir = WriteCfgFile.getDirectory();
			ty_file = WriteCfgFile.getFile();
		} 
		catch (java.lang.Exception e) {
		}

		if  (TyCfgBean21 == null) {

				// get a new obj
				TyCfgBean21 = new TyCfgBean2();

		} // endif

		
		TyCfgBean21.writeConfig( ty_dir, 
														ty_file,
														urlText,
														managerText,
														userText,
														passText,
														queText,
														funcText,
														listText,
														statsText,
														logText,
														SysExit,
														monText,
														actText,
														notifyText,
                            LoginContextText,
														StartupList,
														StartupFuncList,														
														Shut1List,
														Shut2List,
														statsDirText,
														statsFileText,
														logDirText,
														logFileText,
														statsAltText,
														logAltText);
														
	}
	void FinishButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// WriteCfgFile Show the FileDialog
			WriteCfgFile.setVisible(true);
		} catch (java.lang.Exception e) {
		}
	}
  void Help_actionPerformed(java.awt.event.ActionEvent event)
  {
    // to do: code goes here.
       
    Help_actionPerformed_Interaction1(event);
  }
  void Help_actionPerformed_Interaction1(java.awt.event.ActionEvent event) {
    
    TyHelp th = new TyHelp();

    th.putOut(tyCfgBundle.getString("th_putOut"));
    
  }
	void ImportButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		ImportButton_actionPerformed_Interaction1(event);
		

		String ty_dir = null;
		String ty_file = null;				

		try {
			ty_dir = ReadCfgFile.getDirectory();

			ty_file = ReadCfgFile.getFile();

		} // end-try 

		catch (java.lang.Exception e) {
		} // end-catch
	
		// when first
		if  (TyCfgBean21 == null) {

				// get a new obj
				TyCfgBean21 = new TyCfgBean2();

		} // endif

		// go get the data
		TyCfgBean21.readConfig( ty_dir, 
														ty_file,
														urlText,
														managerText,
														userText,
														passText,
														queText,
														funcText,
														listText,
														statsText,
														logText,
														monText,
														actText,
														SysExit,
														notifyText,
                            LoginContextText,
														StartupList,
														StartupFuncList,
														Shut1List,
														Shut2List,
														statsDirText,
														statsFileText,
														logDirText,
														logFileText,
														statsAltText,
														logAltText);

	}
	void ImportButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// ReadCfgFile Show the FileDialog
			ReadCfgFile.setVisible(true);
		} catch (java.lang.Exception e) {
		}
	}
  
  void GetButton_actionPerformed(java.awt.event.ActionEvent event)
  {
    // to do: code goes here.
      
    // when first
    if  (TyCfgBean21 == null) {

        // get a new obj
        TyCfgBean21 = new TyCfgBean2();

    } // endif

    // go get the data
    TyCfgBean21.GetConfig(  urlText,
                            managerText,
                            userText,
                            passText,
                            queText,
                            funcText,
                            listText,
                            statsText,
                            logText,
                            monText,
                            actText,
                            SysExit,
                            notifyText,
                            LoginContextText,
                            StartupList,
                            StartupFuncList,
                            Shut1List,
                            Shut2List,
                            statsDirText,
                            statsFileText,
                            logDirText,
                            logFileText,
                            statsAltText,
                            logAltText);

  }

  void PutButton_actionPerformed(java.awt.event.ActionEvent event)
  {
    // to do: code goes here.
      
    // when first
    if  (TyCfgBean21 == null) {

        // get a new obj
        TyCfgBean21 = new TyCfgBean2();

    } // endif

    // go get the data
    TyCfgBean21.PutConfig(  urlText,
                            managerText,
                            userText,
                            passText,
                            queText,
                            funcText,
                            listText,
                            statsText,
                            logText,
                            SysExit,
                            monText,
                            actText,
                            notifyText,
                            LoginContextText,
                            StartupList,
                            StartupFuncList,                            
                            Shut1List,
                            Shut2List,
                            statsDirText,
                            statsFileText,
                            logDirText,
                            logFileText,
                            statsAltText,
                            logAltText);

  }

  /**
   * The entry point for this application.
   * Sets the Look and Feel to the System Look and Feel.
   * Creates a new JFrame1 and makes it visible.
   */
  static public void main(String args[])
  {
    try {
        // Add the following code if you want the Look and Feel
        // to be set to the Look and Feel of the native system.
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } 
        catch (Exception e) { 
        }
        

      //Create a new instance of our application's frame, and make it visible.
      (new TyCfg()).setVisible(true);
    } 
    catch (Throwable t) {

			System.out.println(t.getMessage());
      // t.printStackTrace();
      //Ensure the application exits with an error condition.
      System.exit(1);
    }
  }
	void Shut1AddButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		Shut1AddButton_actionPerformed_Interaction1(event);
	}
	void Shut1AddButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// text field to string
			String addData = Shut1Text.getText();

			int i;
			
			// When no data
			if  ((addData == null) ||
					 (addData.length() < 1)) {
		
					// done
					return;

			} // endif
			

			// total number in list
			int len = Shut1List.getModel().getSize();

			// new array
			String[] data = new String[len + 1];

			// add the old list to the new array
			for  (i = 0; i < len; i++) {

					// each queue
					data[i] = (String) Shut1List.getModel().getElementAt(i);

			} // end-for

			// add the new queue name
			data[i] = addData;

			// kill the old list
			Shut1List.removeAll();

			// create a new list
			Shut1List.setListData(data);

			// ADD data null
			Shut1Text.setText(tyCfgBundle.getString("Shut1Text_text_1"));	

			// must first select something
			Shut1RemoveButton.setEnabled(false);
		
		} catch (java.lang.Exception e) {
		}
	}
	void Shut1List_valueChanged(javax.swing.event.ListSelectionEvent event)
	{
		// to do: code goes here.
			 
		Shut1List_valueChanged_Interaction1(event);
	}
	void Shut1List_valueChanged_Interaction1(javax.swing.event.ListSelectionEvent event)
	{
		try {
      // When something selected
      if  (Shut1List.getSelectedIndex() == -1) {

          // nothing selected
          Shut1RemoveButton.setEnabled(false);
      }
      else {
          Shut1RemoveButton.setEnabled(true);
      } // endif

		} catch (java.lang.Exception e) {
		}
	}
	void Shut1RemoveButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		Shut1RemoveButton_actionPerformed_Interaction1(event);
	}
	void Shut1RemoveButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
				// index selected
				int the_index = Shut1List.getSelectedIndex(); 

				// When something selected
				if  (the_index == -1) {

					// nothing selected
					Shut1RemoveButton.setEnabled(false);

				} // endif

				// total number in list
				int len = Shut1List.getModel().getSize();

				// When only one,
				if  (len == 1) {

						// new vector
						Vector data = new Vector();

						// create a new list
						Shut1List.setListData(data);

						// display
						Shut1List.repaint();

						// must first select something
						Shut1RemoveButton.setEnabled(false);

						// done
						return;

				} // endif

				// new array
				String[] data = new String[len - 1];

				// add the old list to the new array without the selected one
				for  (int i = 0, j = 0; i < len; i++) {

							// When it's not the one to skip
							if  (i != the_index) {

									// each queue
									data[j] = (String) Shut1List.getModel().getElementAt(i);

									// bump new
									j++;

							} // endif
				} // end-for

				// kill the old list
				Shut1List.removeAll();

				// create a new list
				Shut1List.setListData(data);

				// must first select something
				Shut1RemoveButton.setEnabled(false);

		} catch (java.lang.Exception e) {
		}
	}
	void Shut2AddButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		Shut2AddButton_actionPerformed_Interaction1(event);
	}
	void Shut2AddButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// text field to string
			String addData = Shut2Text.getText();

			int i;
			
			// When no data
			if  ((addData == null) ||
					 (addData.length() < 1)) {
		
					// done
					return;

			} // endif
			
			// total number in list
			int len = Shut2List.getModel().getSize();

			// new array
			String[] data = new String[len + 1];

			// add the old list to the new array
			for  (i = 0; i < len; i++) {

					// each queue
					data[i] = (String) Shut2List.getModel().getElementAt(i);

			} // end-for

			// add the new queue name
			data[i] = addData;

			// kill the old list
			Shut2List.removeAll();

			// create a new list
			Shut2List.setListData(data);

			// ADD data null
			Shut2Text.setText(tyCfgBundle.getString("Shut2Text_text_1"));	

			// must first select something
			Shut2RemoveButton.setEnabled(false);
		
		} catch (java.lang.Exception e) {
		}
	}
	void Shut2List_valueChanged(javax.swing.event.ListSelectionEvent event)
	{
		// to do: code goes here.
			 
		Shut2List_valueChanged_Interaction1(event);
	}
	void Shut2List_valueChanged_Interaction1(javax.swing.event.ListSelectionEvent event)
	{
		try {
      // When something selected
      if  (Shut2List.getSelectedIndex() == -1) {

          // nothing selected
          Shut2RemoveButton.setEnabled(false);
      }
      else {
          Shut2RemoveButton.setEnabled(true);
      } // endif

		} catch (java.lang.Exception e) {
		}
	}
	void Shut2RemoveButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		Shut2RemoveButton_actionPerformed_Interaction1(event);
	}
	void Shut2RemoveButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
				// index selected
				int the_index = Shut2List.getSelectedIndex(); 

				// When something selected
				if  (the_index == -1) {

					// nothing selected
					Shut2RemoveButton.setEnabled(false);

				} // endif

				// total number in list
				int len = Shut2List.getModel().getSize();

				// When only one,
				if  (len == 1) {

						// new vector
						Vector data = new Vector();

						// create a new list
						Shut2List.setListData(data);

						// display
						Shut2List.repaint();

						// must first select something
						Shut2RemoveButton.setEnabled(false);

						// done
						return;

				} // endif

				// new array
				String[] data = new String[len - 1];

				// add the old list to the new array without the selected one
				for  (int i = 0, j = 0; i < len; i++) {

							// When it's not the one to skip
							if  (i != the_index) {

									// each queue
									data[j] = (String) Shut2List.getModel().getElementAt(i);

									// bump new
									j++;

							} // endif
				} // end-for

				// kill the old list
				Shut2List.removeAll();

				// create a new list
				Shut2List.setListData(data);

				// must first select something
				Shut2RemoveButton.setEnabled(false);

		} catch (java.lang.Exception e) {
		}
	}
	void StartupAddButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		StartupAddButton_actionPerformed_Interaction1(event);
	}
	void StartupAddButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
	try {
			// text field to string
			String addData = StartupText.getText();

			int i;
			
			// When no data
			if  ((addData == null) ||
					 (addData.length() < 1)) {
		
					// done
					return;

			} // endif
			

			// total number in list
			int len = StartupList.getModel().getSize();

			// new array
			String[] data = new String[len + 1];

			// add the old list to the new array
			for  (i = 0; i < len; i++) {

					// each queue
					data[i] = (String) StartupList.getModel().getElementAt(i);

			} // end-for

			// add the new queue name
			data[i] = addData;

			// kill the old list
			StartupList.removeAll();

			// create a new list
			StartupList.setListData(data);

			// ADD data null
			StartupText.setText(tyCfgBundle.getString("StartupText_text_1"));	

			// must first select something
			StartupRemoveButton.setEnabled(false);
			
		} catch (java.lang.Exception e) {
		}

	}
	void StartupAddFuncButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		StartupAddFuncButton_actionPerformed_Interaction1(event);
	}
	void StartupAddFuncButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// text field to string
			String addData = StartupFuncText.getText();

			int i;
			
			// When no data
			if  ((addData == null) ||
					 (addData.length() < 1)) {
		
					// done
					return;

			} // endif
			

			// total number in list
			int len = StartupFuncList.getModel().getSize();

			// new array
			String[] data = new String[len + 1];

			// add the old list to the new array
			for  (i = 0; i < len; i++) {

					// each queue
					data[i] = (String) StartupFuncList.getModel().getElementAt(i);

			} // end-for

			// add the new queue name
			data[i] = addData;

			// kill the old list
			StartupFuncList.removeAll();

			// create a new list
			StartupFuncList.setListData(data);

			// ADD data null
			StartupFuncText.setText(tyCfgBundle.getString("StartupFuncText_text_1"));	

			// must first select something
			StartupRemoveFuncButton.setEnabled(false);
			
		} catch (java.lang.Exception e) {
		}
	}
	void StartupFuncList_valueChanged(javax.swing.event.ListSelectionEvent event)
	{
		// to do: code goes here.
			 
		StartupFuncList_valueChanged_Interaction1(event);
	}
	void StartupFuncList_valueChanged_Interaction1(javax.swing.event.ListSelectionEvent event)
	{
		try {
      // When something selected
      if  (StartupFuncList.getSelectedIndex() == -1) {

          // nothing selected
          StartupRemoveFuncButton.setEnabled(false);
      }
      else {
          StartupRemoveFuncButton.setEnabled(true);
      } // endif
      
    } catch (java.lang.Exception e) {
    }
	}
	void StartupList_valueChanged(javax.swing.event.ListSelectionEvent event)
	{
		// to do: code goes here.
			 
		StartupList_valueChanged_Interaction1(event);
	}
	void StartupList_valueChanged_Interaction1(javax.swing.event.ListSelectionEvent event)
	{
		try {
      // When something selected
      if  (StartupList.getSelectedIndex() == -1) {

          // nothing selected
          StartupRemoveButton.setEnabled(false);
      }
      else {
          StartupRemoveButton.setEnabled(true);
      } // endif
      
    } catch (java.lang.Exception e) {
    }
	}
	void StartupRemoveButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		StartupRemoveButton_actionPerformed_Interaction1(event);
	}
	void StartupRemoveButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
				// index selected
				int the_index = StartupList.getSelectedIndex(); 

				// When something selected
				if  (the_index == -1) {

					// nothing selected
					StartupRemoveButton.setEnabled(false);

				} // endif

				// total number in list
				int len = StartupList.getModel().getSize();

				// When only one,
				if  (len == 1) {

						// new vector
						Vector data = new Vector();

						// create a new list
						StartupList.setListData(data);

						// display
						StartupList.repaint();

						// must first select something
						StartupRemoveButton.setEnabled(false);

						// done
						return;

				} // endif

				// new array
				String[] data = new String[len - 1];

				// add the old list to the new array without the selected one
				for  (int i = 0, j = 0; i < len; i++) {

							// When it's not the one to skip
							if  (i != the_index) {

									// each queue
									data[j] = (String) StartupList.getModel().getElementAt(i);

									// bump new
									j++;

							} // endif
				} // end-for

				// kill the old list
				StartupList.removeAll();

				// create a new list
				StartupList.setListData(data);

				// must first select something
				StartupRemoveButton.setEnabled(false);

		} catch (java.lang.Exception e) {
		}
	}
	void StartupRemoveFuncButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		StartupRemoveFuncButton_actionPerformed_Interaction1(event);
	}
	void StartupRemoveFuncButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
				// index selected
				int the_index = StartupFuncList.getSelectedIndex(); 

				// When something selected
				if  (the_index == -1) {

					// nothing selected
					StartupRemoveFuncButton.setEnabled(false);

				} // endif

				// total number in list
				int len = StartupFuncList.getModel().getSize();

				// When only one,
				if  (len == 1) {

						// new vector
						Vector data = new Vector();

						// create a new list
						StartupFuncList.setListData(data);

						// display
						StartupFuncList.repaint();

						// must first select something
						StartupRemoveFuncButton.setEnabled(false);

						// done
						return;

				} // endif

				// new array
				String[] data = new String[len - 1];

				// add the old list to the new array without the selected one
				for  (int i = 0, j = 0; i < len; i++) {

							// When it's not the one to skip
							if  (i != the_index) {

									// each queue
									data[j] = (String) StartupFuncList.getModel().getElementAt(i);

									// bump new
									j++;

							} // endif
				} // end-for

				// kill the old list
				StartupFuncList.removeAll();

				// create a new list
				StartupFuncList.setListData(data);

				// must first select something
				StartupRemoveFuncButton.setEnabled(false);

		} catch (java.lang.Exception e) {
		}
	}
  void TyCfg_windowClosing(java.awt.event.WindowEvent event)
  {
    // to do: code goes here.
       
    TyCfg_windowClosing_Interaction1(event);
  }
  void TyCfg_windowClosing_Interaction1(java.awt.event.WindowEvent event) {
    try {
      this.exitApplication();
    } catch (Exception e) {
    }
  }
}
