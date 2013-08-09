package com.tymeac.client.jframe;

/* 
 * Copyright (c) 1998 - 2011 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

import java.awt.*;
import javax.swing.*;
import com.tymeac.base.*;

/**
 * The Main Menu display frame.
 */
public class TyMenu extends javax.swing.JFrame
{
  private static final long serialVersionUID = 1L;

  // Used by addNotify
  boolean frameSizeAdjusted = false;

  //{{DECLARE_CONTROLS
	javax.swing.JButton TyVButton = new javax.swing.JButton();
	javax.swing.JLabel TyVLabel = new javax.swing.JLabel();
	javax.swing.JLabel DBMSLabel = new javax.swing.JLabel();
	javax.swing.JLabel UserLabel = new javax.swing.JLabel();
	javax.swing.JLabel UserQueLabel = new javax.swing.JLabel();
	javax.swing.JLabel UserFuncLabel = new javax.swing.JLabel();
	javax.swing.JLabel TyUserVarLabel = new javax.swing.JLabel();
	javax.swing.JButton TyUserQueButton = new javax.swing.JButton();
	javax.swing.JButton TyUserFuncButton = new javax.swing.JButton();
	javax.swing.JButton TyUserVarButton = new javax.swing.JButton();
	javax.swing.JButton TyQueThdButton = new javax.swing.JButton();
	javax.swing.JButton TyWlDataButton = new javax.swing.JButton();
	javax.swing.JButton TyStalledButton = new javax.swing.JButton();
	javax.swing.JButton TyQueDataButton3 = new javax.swing.JButton();
	javax.swing.JButton TyShutdownButton = new javax.swing.JButton();
  
  javax.swing.JButton TyNewRTNotifyButton = new javax.swing.JButton();
  javax.swing.JLabel TyNewRTNotifyLabel = new javax.swing.JLabel();
  
  javax.swing.JButton TyNewRTLogButton = new javax.swing.JButton();
  javax.swing.JLabel TyNewRTLogLabel = new javax.swing.JLabel();
  
  javax.swing.JButton TyNewRTStatsButton = new javax.swing.JButton();
  javax.swing.JLabel TyNewRTStatsLabel = new javax.swing.JLabel();  
  
  javax.swing.JButton TyAltSvrButton = new javax.swing.JButton();
  javax.swing.JLabel TyAltSvrLabel = new javax.swing.JLabel();  
  
	javax.swing.JButton TyReqStatusButton = new javax.swing.JButton();
	javax.swing.JButton TyFuncDataButton = new javax.swing.JButton();
	javax.swing.JButton TyStatsButton = new javax.swing.JButton();
	javax.swing.JButton TyOverallButton = new javax.swing.JButton();
	javax.swing.JButton TyNewCopyButton = new javax.swing.JButton();
	javax.swing.JButton TyQueMaintButton = new javax.swing.JButton();
	javax.swing.JButton TyFuncMaintButton = new javax.swing.JButton();
	javax.swing.JButton TyCfgButton = new javax.swing.JButton();
	javax.swing.JLabel TyQueThdLabel = new javax.swing.JLabel();
	javax.swing.JLabel TyWlDataLabel = new javax.swing.JLabel();
	javax.swing.JLabel TyStalledLabel = new javax.swing.JLabel();
	javax.swing.JLabel TyQueDataLabel = new javax.swing.JLabel();
	javax.swing.JLabel TyShutdownLabel = new javax.swing.JLabel();
	javax.swing.JLabel TyReqStatusLabel = new javax.swing.JLabel();
	javax.swing.JLabel TyFuncDataLabel = new javax.swing.JLabel();
	javax.swing.JLabel TyStatsLabel = new javax.swing.JLabel();
	javax.swing.JLabel TyOverallLabel = new javax.swing.JLabel();
	javax.swing.JLabel TyNewCopyLabel = new javax.swing.JLabel();
	javax.swing.JLabel TyQueMaintLabel = new javax.swing.JLabel();
	javax.swing.JLabel TyFuncMaintLabel = new javax.swing.JLabel();
	javax.swing.JLabel TyCfgLabel = new javax.swing.JLabel();
	javax.swing.JMenuBar JMenuBar1 = new javax.swing.JMenuBar();
	javax.swing.JMenu fileMenu = new javax.swing.JMenu();
	javax.swing.JMenuItem exitItem = new javax.swing.JMenuItem();
	javax.swing.JSeparator JSeparator1 = new javax.swing.JSeparator();
	javax.swing.JMenu helpMenu = new javax.swing.JMenu();
	javax.swing.JMenuItem Help = new javax.swing.JMenuItem();
	javax.swing.JMenuItem about = new javax.swing.JMenuItem();
	//}}

	TyMenuBean2 TyMenuBean21 = null;
	TymeacInterface Ti = null;

  class SymWindow extends java.awt.event.WindowAdapter
  {
    public void windowClosing(java.awt.event.WindowEvent event)
    {
      Object object = event.getSource();
      if (object == TyMenu.this)
        TyMenu_windowClosing(event);
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
			else if (object == TyQueThdButton)
				TyQueThdButton_actionPerformed(event);
			else if (object == TyWlDataButton)
				TyWlDataButton_actionPerformed(event);
			else if (object == TyStalledButton)
				TyStalledButton_actionPerformed(event);
			else if (object == TyQueDataButton3)
				TyQueDataButton3_actionPerformed(event);
			else if (object == TyShutdownButton)
				TyShutdownButton_actionPerformed(event);
			else if (object == TyReqStatusButton)
				TyReqStatusButton_actionPerformed(event);
			else if (object == TyFuncDataButton)
				TyFuncDataButton_actionPerformed(event);
			else if (object == TyStatsButton)
				TyStatsButton_actionPerformed(event);
			else if (object == TyOverallButton)
				TyOverallButton_actionPerformed(event);
			else if (object == TyNewCopyButton)
				TyNewCopyButton_actionPerformed(event);
			else if (object == TyQueMaintButton)
				TyQueMaintButton_actionPerformed(event);
			else if (object == TyFuncMaintButton)
				TyFuncMaintButton_actionPerformed(event);
			else if (object == TyCfgButton)
				TyCfgButton_actionPerformed(event);
			else if (object == TyUserQueButton)
				TyUserQueButton_actionPerformed(event);
			else if (object == TyUserFuncButton)
				TyUserFuncButton_actionPerformed(event);
			else if (object == TyUserVarButton)
				TyUserVarButton_actionPerformed(event);
			else if (object == TyVButton)
				TyVButton_actionPerformed(event);  
      else if (object == TyNewRTNotifyButton)
        TyNewRTNotifyButton_actionPerformed(event);  
      else if (object == TyNewRTLogButton)
        TyNewRTLogButton_actionPerformed(event);  
      else if (object == TyNewRTStatsButton)
        TyNewRTStatsButton_actionPerformed(event);   
      else if (object == TyAltSvrButton)
        TyAltSvrButton_actionPerformed(event); 
    }
  }

	static java.util.ResourceBundle tyMenuBundle = 
    java.util.ResourceBundle.getBundle("com.tymeac.client.jframe.TyMenuBundle");
  public TyMenu()
  {
    // This code is automatically generated by Visual Cafe when you add
    // components to the visual environment. It instantiates and initializes
    // the components. To modify the code, only use code syntax that matches
    // what Visual Cafe can generate, or Visual Cafe may be unable to back
    // parse your Java file into its visual environment.
    //{{INIT_CONTROLS
    setJMenuBar(JMenuBar1);
    setTitle(tyMenuBundle.getString("TyMenu_title"));
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    getContentPane().setLayout(null);
    setSize(640,490);
		setVisible(false);   
    
    //$$ JMenuBar1.move(108,456);
    fileMenu.setText(tyMenuBundle.getString("fileMenu_text"));
    fileMenu.setActionCommand(tyMenuBundle.getString("fileMenu_actionCommand"));
		fileMenu.setToolTipText(tyMenuBundle.getString("fileMenu_toolTipText").length() != 0 
        ? tyMenuBundle.getString("fileMenu_toolTipText") : null);
    fileMenu.setMnemonic((int)'F');
    JMenuBar1.add(fileMenu);
    
    fileMenu.add(JSeparator1);
    
    exitItem.setText(tyMenuBundle.getString("exitItem_text"));
    exitItem.setActionCommand(tyMenuBundle.getString("exitItem_actionCommand"));
		exitItem.setToolTipText(tyMenuBundle.getString("exitItem_toolTipText").length() != 0 
        ? tyMenuBundle.getString("exitItem_toolTipText") : null);
    exitItem.setMnemonic((int)'X');
    fileMenu.add(exitItem);
		JSeparator1.setToolTipText(tyMenuBundle.getString("JSeparator1_toolTipText").length() != 0 
        ? tyMenuBundle.getString("JSeparator1_toolTipText") : null);
    
    helpMenu.setText(tyMenuBundle.getString("helpMenu_text"));
    helpMenu.setActionCommand(tyMenuBundle.getString("helpMenu_actionCommand"));
		helpMenu.setToolTipText(tyMenuBundle.getString("helpMenu_toolTipText").length() != 0 
        ? tyMenuBundle.getString("helpMenu_toolTipText") : null);
    helpMenu.setMnemonic((int)'H');
    JMenuBar1.add(helpMenu);
    
    fileMenu.add(JSeparator1);
       
    Help.setText(tyMenuBundle.getString("Help_text"));
    Help.setActionCommand(tyMenuBundle.getString("Help_actionCommand"));
		Help.setToolTipText(tyMenuBundle.getString("Help_toolTipText").length() != 0 
        ? tyMenuBundle.getString("Help_toolTipText") : null);
    Help.setMnemonic((int)'D');
    helpMenu.add(Help);
    
    fileMenu.add(JSeparator1);
    
    about.setText(tyMenuBundle.getString("about_text"));
    about.setActionCommand(tyMenuBundle.getString("about_actionCommand"));
		about.setToolTipText(tyMenuBundle.getString("about_toolTipText").length() != 0 
        ? tyMenuBundle.getString("about_toolTipText") : null);
    about.setMnemonic((int)'A');
    helpMenu.add(about); 
    
// line 1    
    TyQueThdButton.setText(tyMenuBundle.getString("TyQueThdButton_text"));
    TyQueThdButton.setActionCommand(tyMenuBundle.getString("TyQueThdButton_actionCommand"));
    TyQueThdButton.setToolTipText(tyMenuBundle.getString("TyQueThdButton_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyQueThdButton_toolTipText") : null);
    getContentPane().add(TyQueThdButton);
    TyQueThdButton.setBackground(java.awt.Color.white);
    TyQueThdButton.setBounds(20,24,110,35);
    
    TyQueThdLabel.setText(tyMenuBundle.getString("TyQueThdLabel_text"));
    TyQueThdLabel.setToolTipText(tyMenuBundle.getString("TyQueThdLabel_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyQueThdLabel_toolTipText") : null);
    getContentPane().add(TyQueThdLabel);
    TyQueThdLabel.setBounds(27,60,110,20);    
    
    TyWlDataButton.setText(tyMenuBundle.getString("TyWlDataButton_text"));
    TyWlDataButton.setActionCommand(tyMenuBundle.getString("TyWlDataButton_actionCommand"));
    TyWlDataButton.setToolTipText(tyMenuBundle.getString("TyWlDataButton_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyWlDataButton_toolTipText") : null);
    getContentPane().add(TyWlDataButton);
    TyWlDataButton.setBackground(java.awt.Color.white);
    TyWlDataButton.setBounds(141,24,110,35);
    
    TyWlDataLabel.setText(tyMenuBundle.getString("TyWlDataLabel_text"));
    TyWlDataLabel.setToolTipText(tyMenuBundle.getString("TyWlDataLabel_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyWlDataLabel_toolTipText") : null);
    getContentPane().add(TyWlDataLabel);
    TyWlDataLabel.setBounds(148,60,110,20);     
    
    TyStalledButton.setText(tyMenuBundle.getString("TyStalledButton_text"));
    TyStalledButton.setActionCommand(tyMenuBundle.getString("TyStalledButton_actionCommand"));
    TyStalledButton.setToolTipText(tyMenuBundle.getString("TyStalledButton_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyStalledButton_toolTipText") : null);
    getContentPane().add(TyStalledButton);
    TyStalledButton.setBackground(java.awt.Color.white);
    TyStalledButton.setBounds(262,24,110,35);
    
    TyStalledLabel.setText(tyMenuBundle.getString("TyStalledLabel_text"));
    TyStalledLabel.setToolTipText(tyMenuBundle.getString("TyStalledLabel_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyStalledLabel_toolTipText") : null);
    getContentPane().add(TyStalledLabel);
    TyStalledLabel.setBounds(269,60,110,20);
    
    TyQueDataButton3.setText(tyMenuBundle.getString("TyQueDataButton3_text"));
    TyQueDataButton3.setActionCommand(tyMenuBundle.getString("TyQueDataButton3_actionCommand"));
    TyQueDataButton3.setToolTipText(tyMenuBundle.getString("TyQueDataButton3_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyQueDataButton3_toolTipText") : null);
    getContentPane().add(TyQueDataButton3);
    TyQueDataButton3.setBackground(java.awt.Color.white);
    TyQueDataButton3.setBounds(383,24,110,35);
    
    TyQueDataLabel.setText(tyMenuBundle.getString("TyQueDataLabel_text"));
    TyQueDataLabel.setToolTipText(tyMenuBundle.getString("TyQueDataLabel_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyQueDataLabel_toolTipText") : null);
    getContentPane().add(TyQueDataLabel);
    TyQueDataLabel.setBounds(390,60,110,20);
    
    TyShutdownButton.setText(tyMenuBundle.getString("TyShutdownButton_text"));
    TyShutdownButton.setActionCommand(tyMenuBundle.getString("TyShutdownButton_actionCommand"));
    TyShutdownButton.setToolTipText(tyMenuBundle.getString("TyShutdownButton_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyShutdownButton_toolTipText") : null);
    getContentPane().add(TyShutdownButton);
    TyShutdownButton.setFont(new java.awt.Font("Arial", 0, 11));
    TyShutdownButton.setForeground(java.awt.Color.red);
    TyShutdownButton.setBounds(504,24,110,35);
    
    TyShutdownLabel.setText(tyMenuBundle.getString("TyShutdownLabel_text"));
    TyShutdownLabel.setToolTipText(tyMenuBundle.getString("TyShutdownLabel_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyShutdownLabel_toolTipText") : null);
    getContentPane().add(TyShutdownLabel);
    TyShutdownLabel.setBounds(511,60,110,20);
    
// line 2 from 24 / 60  
    
    TyReqStatusButton.setText(tyMenuBundle.getString("TyReqStatusButton_text"));
    TyReqStatusButton.setActionCommand(tyMenuBundle.getString("TyReqStatusButton_actionCommand"));
    TyReqStatusButton.setToolTipText(tyMenuBundle.getString("TyReqStatusButton_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyReqStatusButton_toolTipText") : null);
    getContentPane().add(TyReqStatusButton);
    TyReqStatusButton.setBackground(java.awt.Color.white);
    TyReqStatusButton.setBounds(20,100,110,35); //20,120,110,35
    
    TyReqStatusLabel.setText(tyMenuBundle.getString("TyReqStatusLabel_text"));
    TyReqStatusLabel.setToolTipText(tyMenuBundle.getString("TyReqStatusLabel_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyReqStatusLabel_toolTipText") : null);
    getContentPane().add(TyReqStatusLabel);
    TyReqStatusLabel.setBounds(27,136,110,20); //20,156,110,20
    
    TyFuncDataButton.setText(tyMenuBundle.getString("TyFuncDataButton_text"));
    TyFuncDataButton.setActionCommand(tyMenuBundle.getString("TyFuncDataButton_actionCommand"));
    TyFuncDataButton.setToolTipText(tyMenuBundle.getString("TyFuncDataButton_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyFuncDataButton_toolTipText") : null);
    getContentPane().add(TyFuncDataButton);
    TyFuncDataButton.setBackground(java.awt.Color.white);
    TyFuncDataButton.setBounds(141,100,110,35);
    
    TyFuncDataLabel.setText(tyMenuBundle.getString("TyFuncDataLabel_text"));
    TyFuncDataLabel.setToolTipText(tyMenuBundle.getString("TyFuncDataLabel_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyFuncDataLabel_toolTipText") : null);
    getContentPane().add(TyFuncDataLabel);
    TyFuncDataLabel.setBounds(148,136,110,20);
    
    TyStatsButton.setText(tyMenuBundle.getString("TyStatsButton_text"));
    TyStatsButton.setActionCommand(tyMenuBundle.getString("TyStatsButton_actionCommand"));
    TyStatsButton.setToolTipText(tyMenuBundle.getString("TyStatsButton_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyStatsButton_toolTipText") : null);
    getContentPane().add(TyStatsButton);
    TyStatsButton.setBackground(java.awt.Color.white);
    TyStatsButton.setBounds(262,100,110,35);
    
    TyStatsLabel.setText(tyMenuBundle.getString("TyStatsLabel_text"));
    TyStatsLabel.setToolTipText(tyMenuBundle.getString("TyStatsLabel_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyStatsLabel_toolTipText") : null);
    getContentPane().add(TyStatsLabel);
    TyStatsLabel.setBounds(269,136,110,20);
    
    TyNewCopyButton.setText(tyMenuBundle.getString("TyNewCopyButton_text"));
    TyNewCopyButton.setActionCommand(tyMenuBundle.getString("TyNewCopyButton_actionCommand"));
    TyNewCopyButton.setToolTipText(tyMenuBundle.getString("TyNewCopyButton_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyNewCopyButton_toolTipText") : null);
    getContentPane().add(TyNewCopyButton);
    TyNewCopyButton.setBackground(java.awt.Color.white);
    TyNewCopyButton.setBounds(383,100,110,35);
    
    TyNewCopyLabel.setText(tyMenuBundle.getString("TyNewCopyLabel_text"));
    TyNewCopyLabel.setToolTipText(tyMenuBundle.getString("TyNewCopyLabel_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyNewCopyLabel_toolTipText") : null);
    getContentPane().add(TyNewCopyLabel);    
    TyNewCopyLabel.setBounds(390,136,110,20);
    
    TyOverallButton.setText(tyMenuBundle.getString("TyOverallButton_text"));
    TyOverallButton.setActionCommand(tyMenuBundle.getString("TyOverallButton_actionCommand"));
    TyOverallButton.setToolTipText(tyMenuBundle.getString("TyOverallButton_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyOverallButton_toolTipText") : null);
    getContentPane().add(TyOverallButton);
    TyOverallButton.setBackground(java.awt.Color.white);
    TyOverallButton.setBounds(504,100,110,35);
    
    TyOverallLabel.setText(tyMenuBundle.getString("TyOverallLabel_text"));
    TyOverallLabel.setToolTipText(tyMenuBundle.getString("TyOverallLabel_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyOverallLabel_toolTipText") : null);
    getContentPane().add(TyOverallLabel);
    TyOverallLabel.setBounds(511,136,110,20);

// line 3     
    TyNewRTNotifyButton.setText(tyMenuBundle.getString("TyNewRTNotifyButton_text"));
    TyNewRTNotifyButton.setActionCommand(tyMenuBundle.getString("TyNewRTNotifyButton_actionCommand"));
    TyNewRTNotifyButton.setToolTipText(tyMenuBundle.getString("TyNewRTNotifyButton_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyNewRTNotifyButton_toolTipText") : null);
    getContentPane().add(TyNewRTNotifyButton);
    TyNewRTNotifyButton.setBackground(java.awt.Color.white);
    TyNewRTNotifyButton.setBounds(20,176,110,35); //20,120,110,35
    
    TyNewRTNotifyLabel.setText(tyMenuBundle.getString("TyNewRTNotifyLabel_text"));
    getContentPane().add(TyNewRTNotifyLabel);
    TyNewRTNotifyLabel.setBounds(27,212,110,20); 
    
    TyNewRTLogButton.setText(tyMenuBundle.getString("TyNewRTLogButton_text"));
    TyNewRTLogButton.setActionCommand(tyMenuBundle.getString("TyNewRTLogButton_actionCommand"));
    TyNewRTNotifyButton.setToolTipText(tyMenuBundle.getString("TyNewRTLogButton_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyNewRTLogButton_toolTipText") : null);
    getContentPane().add(TyNewRTLogButton);
    TyNewRTLogButton.setBackground(java.awt.Color.white);
    TyNewRTLogButton.setBounds(141,176,110,35); //20,120,110,35
    
    TyNewRTLogLabel.setText(tyMenuBundle.getString("TyNewRTLogLabel_text"));
    getContentPane().add(TyNewRTLogLabel);
    TyNewRTLogLabel.setBounds(148,212,110,20);
    
    TyNewRTStatsButton.setText(tyMenuBundle.getString("TyNewRTStatsButton_text"));
    TyNewRTStatsButton.setActionCommand(tyMenuBundle.getString("TyNewRTStatsButton_actionCommand"));
    TyNewRTStatsButton.setToolTipText(tyMenuBundle.getString("TyNewRTStatsButton_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyNewRTStatsButton_toolTipText") : null);
    getContentPane().add(TyNewRTStatsButton);
    TyNewRTStatsButton.setBackground(java.awt.Color.white);
    TyNewRTStatsButton.setBounds(262,176,110,35); //20,120,110,35
    
    TyNewRTStatsLabel.setText(tyMenuBundle.getString("TyNewRTStatsLabel_text"));
    getContentPane().add(TyNewRTStatsLabel);
    TyNewRTStatsLabel.setBounds(269,212,110,20);
    
    // alt server opts
    TyAltSvrButton.setText(tyMenuBundle.getString("TyAltSvrButton_text"));
    TyAltSvrButton.setActionCommand(tyMenuBundle.getString("TyAltSvrButton_actionCommand"));
    TyAltSvrButton.setToolTipText(tyMenuBundle.getString("TyAltSvrButton_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyAltSvrButton_toolTipText") : null);
    getContentPane().add(TyAltSvrButton);
    TyAltSvrButton.setBackground(java.awt.Color.white);
    TyAltSvrButton.setBounds(383,176,110,35); //(262,176,110,35)
    
    TyAltSvrLabel.setText(tyMenuBundle.getString("TyAltSvrLabel_text"));
    getContentPane().add(TyAltSvrLabel);
    TyAltSvrLabel.setBounds(390,212,110,20); //(269,212,110,20)
    
    
//dbms <--- --->   
    DBMSLabel.setText(tyMenuBundle.getString("DBMSLabel_text"));
    DBMSLabel.setToolTipText(tyMenuBundle.getString("DBMSLabel_toolTipText").length() != 0 
        ? tyMenuBundle.getString("DBMSLabel_toolTipText") : null);
    getContentPane().add(DBMSLabel);
    DBMSLabel.setForeground(java.awt.Color.blue);
    DBMSLabel.setFont(new Font("Dialog", Font.BOLD, 14));
    DBMSLabel.setBounds(72,254,252,24); //72,204,252,24
    
// dbms line        
    TyQueMaintButton.setText(tyMenuBundle.getString("TyQueMaintButton_text"));
    TyQueMaintButton.setActionCommand(tyMenuBundle.getString("TyQueMaintButton_actionCommand"));
    TyQueMaintButton.setToolTipText(tyMenuBundle.getString("TyQueMaintButton_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyQueMaintButton_toolTipText") : null);
    getContentPane().add(TyQueMaintButton);
    TyQueMaintButton.setBackground(java.awt.Color.white);
    TyQueMaintButton.setBounds(20,290,110,35); //24,240,110,35
    
    TyQueMaintLabel.setText(tyMenuBundle.getString("TyQueMaintLabel_text"));
    TyQueMaintLabel.setToolTipText(tyMenuBundle.getString("TyQueMaintLabel_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyQueMaintLabel_toolTipText") : null);
    getContentPane().add(TyQueMaintLabel);
    TyQueMaintLabel.setBounds(27,326,110,20); //24,276,110,20
    
    TyFuncMaintButton.setText(tyMenuBundle.getString("TyFuncMaintButton_text"));
    TyFuncMaintButton.setActionCommand(tyMenuBundle.getString("TyFuncMaintButton_actionCommand"));
    TyFuncMaintButton.setToolTipText(tyMenuBundle.getString("TyFuncMaintButton_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyFuncMaintButton_toolTipText") : null);
    getContentPane().add(TyFuncMaintButton);
    TyFuncMaintButton.setBackground(java.awt.Color.white);
    TyFuncMaintButton.setBounds(141,290,110,35);
    
    TyFuncMaintLabel.setText(tyMenuBundle.getString("TyFuncMaintLabel_text"));
    TyFuncMaintLabel.setToolTipText(tyMenuBundle.getString("TyFuncMaintLabel_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyFuncMaintLabel_toolTipText") : null);
    getContentPane().add(TyFuncMaintLabel);
    TyFuncMaintLabel.setBounds(147,326,110,20);
    
    TyCfgButton.setText(tyMenuBundle.getString("TyCfgButton_text"));
    TyCfgButton.setActionCommand(tyMenuBundle.getString("TyCfgButton_actionCommand"));
    TyCfgButton.setToolTipText(tyMenuBundle.getString("TyCfgButton_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyCfgButton_toolTipText") : null);
    getContentPane().add(TyCfgButton);
    TyCfgButton.setBackground(java.awt.Color.white);
    TyCfgButton.setBounds(262,290,110,35); //262,280,110,35
    
    TyCfgLabel.setText(tyMenuBundle.getString("TyCfgLabel_text"));
    TyCfgLabel.setToolTipText(tyMenuBundle.getString("TyCfgLabel_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyCfgLabel_toolTipText") : null);
    getContentPane().add(TyCfgLabel);
    TyCfgLabel.setBounds(269,326,110,20); //269,316,110,20
    
// user <--- --->    
    
    UserLabel.setText(tyMenuBundle.getString("UserLabel_text"));
    UserLabel.setToolTipText(tyMenuBundle.getString("UserLabel_toolTipText").length() != 0 
        ? tyMenuBundle.getString("UserLabel_toolTipText") : null);
    getContentPane().add(UserLabel);
    UserLabel.setForeground(java.awt.Color.blue);
    UserLabel.setFont(new Font("Dialog", Font.BOLD, 14));
    UserLabel.setBounds(120,364,276,24); //120,324,276,24
    
// user line    
    TyUserQueButton.setText(tyMenuBundle.getString("TyUserQueButton_text"));
    TyUserQueButton.setActionCommand(tyMenuBundle.getString("TyUserQueButton_actionCommand"));
    TyUserQueButton.setToolTipText(tyMenuBundle.getString("TyUserQueButton_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyUserQueButton_toolTipText") : null);
    getContentPane().add(TyUserQueButton);
    TyUserQueButton.setBackground(java.awt.Color.white);
    TyUserQueButton.setBounds(20,400,110,35); //24,360,110,35
    
    UserQueLabel.setText(tyMenuBundle.getString("UserQueLabel_text"));
    UserQueLabel.setToolTipText(tyMenuBundle.getString("UserQueLabel_toolTipText").length() != 0 
        ? tyMenuBundle.getString("UserQueLabel_toolTipText") : null);
    getContentPane().add(UserQueLabel);
    UserQueLabel.setBounds(27,436,110,20); //24,396,110,20
    
    TyUserFuncButton.setText(tyMenuBundle.getString("TyUserFuncButton_text"));
    TyUserFuncButton.setActionCommand(tyMenuBundle.getString("TyUserFuncButton_actionCommand"));
    TyUserFuncButton.setToolTipText(tyMenuBundle.getString("TyUserFuncButton_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyUserFuncButton_toolTipText") : null);
    getContentPane().add(TyUserFuncButton);
    TyUserFuncButton.setBackground(java.awt.Color.white);
    TyUserFuncButton.setBounds(141,400,110,35);
    
    UserFuncLabel.setText(tyMenuBundle.getString("UserFuncLabel_text"));
    UserFuncLabel.setToolTipText(tyMenuBundle.getString("UserFuncLabel_toolTipText").length() != 0 
        ? tyMenuBundle.getString("UserFuncLabel_toolTipText") : null);
    getContentPane().add(UserFuncLabel);
    UserFuncLabel.setBounds(148,436,110,20);
    
    TyUserVarButton.setText(tyMenuBundle.getString("TyUserVarButton_text"));
    TyUserVarButton.setActionCommand(tyMenuBundle.getString("TyUserVarButton_actionCommand"));
    TyUserVarButton.setToolTipText(tyMenuBundle.getString("TyUserVarButton_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyUserVarButton_toolTipText") : null);
    getContentPane().add(TyUserVarButton);
    TyUserVarButton.setBackground(java.awt.Color.white);
    TyUserVarButton.setBounds(262,400,110,35);
    
    TyUserVarLabel.setText(tyMenuBundle.getString("TyUserVarLabel_text"));
    TyUserVarLabel.setToolTipText(tyMenuBundle.getString("TyUserVarLabel_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyUserVarLabel_toolTipText") : null);
    getContentPane().add(TyUserVarLabel);
    TyUserVarLabel.setBounds(269,436,108,24);        
    
		TyVButton.setActionCommand(tyMenuBundle.getString("TyVButton_actionCommand"));
    
		TyVButton.setToolTipText(tyMenuBundle.getString("TyVButton_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyVButton_toolTipText") : null);   
		
    TyVButton.setText(tyMenuBundle.getString("TyVButton_text"));
    getContentPane().add(TyVButton);
    TyVButton.setBackground(java.awt.Color.white);
    TyVButton.setBounds(383,400,110,35); 
    
    TyVLabel.setText(tyMenuBundle.getString("TyVLabel_text"));
    TyVLabel.setToolTipText(tyMenuBundle.getString("TyVLabel_toolTipText").length() != 0 
        ? tyMenuBundle.getString("TyVLabel_toolTipText") : null);
    getContentPane().add(TyVLabel);
    TyVLabel.setBounds(390,436,108,24); 
    
		JMenuBar1.setToolTipText(tyMenuBundle.getString("JMenuBar1_toolTipText").length() != 0 
        ? tyMenuBundle.getString("JMenuBar1_toolTipText") : null);
    

    //{{REGISTER_LISTENERS
    SymWindow aSymWindow = new SymWindow();
    this.addWindowListener(aSymWindow);
    
    SymAction lSymAction = new SymAction();
    exitItem.addActionListener(lSymAction);
    Help.addActionListener(lSymAction);
    about.addActionListener(lSymAction);
		TyQueThdButton.addActionListener(lSymAction);
		TyWlDataButton.addActionListener(lSymAction);
		TyStalledButton.addActionListener(lSymAction);
		TyQueDataButton3.addActionListener(lSymAction);
		TyShutdownButton.addActionListener(lSymAction);
		TyReqStatusButton.addActionListener(lSymAction);
		TyFuncDataButton.addActionListener(lSymAction);
		TyStatsButton.addActionListener(lSymAction);
		TyOverallButton.addActionListener(lSymAction);
		TyNewCopyButton.addActionListener(lSymAction);
		TyQueMaintButton.addActionListener(lSymAction);
		TyFuncMaintButton.addActionListener(lSymAction);
		TyCfgButton.addActionListener(lSymAction);
		TyUserQueButton.addActionListener(lSymAction);
		TyUserFuncButton.addActionListener(lSymAction);
		TyUserVarButton.addActionListener(lSymAction);
		TyVButton.addActionListener(lSymAction);
    TyNewRTNotifyButton.addActionListener(lSymAction);
    TyNewRTLogButton.addActionListener(lSymAction);
    TyNewRTStatsButton.addActionListener(lSymAction);
    TyAltSvrButton.addActionListener(lSymAction);
		//}}

		TyMenuBean21 = new TyMenuBean2();
  }
public TyMenu(TymeacInterface TyI)
    {
			this();
      Ti = TyI;
      try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }
       catch (Exception e) {
       }
		
			TyMenuBean21 = new TyMenuBean2(Ti);	

			// kill the unused DBMS buttons
			TyQueMaintButton.setText(tyMenuBundle.getString("TyQueMaintButton_text_1"));
			TyFuncMaintButton.setText(tyMenuBundle.getString("TyFuncMaintButton_text_1"));
			TyCfgButton.setText(tyMenuBundle.getString("TyCfgButton_text_1"));
      
      // kill the unused User Class buttons
      TyVButton.setText(tyMenuBundle.getString("TyQueMaintButton_text_1"));
      TyUserVarButton.setText(tyMenuBundle.getString("TyFuncMaintButton_text_1"));
      TyUserFuncButton.setText(tyMenuBundle.getString("TyFuncMaintButton_text_1"));
      TyUserQueButton.setText(tyMenuBundle.getString("TyCfgButton_text_1"));
					
    }
    /**
     * Creates a new instance of JFrame1 with the given title.
     * @param sTitle the title for the new frame.
     */
  public TyMenu(String sTitle)
  {
    this();
    setTitle(sTitle);
  }
   void about_actionPerformed(java.awt.event.ActionEvent event)
  {
    // to do: code goes here.
       
    about_actionPerformed_Interaction1(event);
  }
  void about_actionPerformed_Interaction1(java.awt.event.ActionEvent event) { 
  
    try {
      // TymeacAbout Create with owner and show as modal
      {
        TyAbout TyAbout1 = new TyAbout(this);
        TyAbout1.setModal(true);
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
    setSize(insets.left + insets.right + size.width, 
            insets.top + insets.bottom + size.height + menuBarHeight);
  }
  //{{DECLARE_MENUS
	//}}

  void exitApplication()
  {    
    this.setVisible(false);    // hide the Frame
    this.dispose();            // free the system resources
		if  (Ti == null) {
    		System.exit(0);   // close the application 
    } // endif
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
  void Help_actionPerformed(java.awt.event.ActionEvent event)
  {
    // to do: code goes here.
       
    Help_actionPerformed_Interaction1(event);
  }
  void Help_actionPerformed_Interaction1(java.awt.event.ActionEvent event) {
    
    TyHelp th = new TyHelp();

    th.putOut(tyMenuBundle.getString("th_putOut"));
    
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
      (new TyMenu()).setVisible(true);
    } 
    catch (Throwable t) {
      t.printStackTrace();
      //Ensure the application exits with an error condition.
      System.exit(1);
    }
  }
	void TyAltSvrButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		TyAltSvrButton_actionPerformed_Interaction1(event);
	}
	void TyAltSvrButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
		// function
			TyMenuBean21.doAltSvr();

		} catch (java.lang.Exception e) {
		}
	}
	
	void TyCfgButton_actionPerformed(java.awt.event.ActionEvent event)
  {
    // to do: code goes here.
       
    TyCfgButton_actionPerformed_Interaction1(event);
  }
  void TyCfgButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
  {
    try {
    // function
      TyMenuBean21.doCfg();

    } catch (java.lang.Exception e) {
    }
  }
	void TyFuncDataButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		TyFuncDataButton_actionPerformed_Interaction1(event);
	}
	void TyFuncDataButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// function
			TyMenuBean21.doFuncData();

		} catch (java.lang.Exception e) {
		}
	}
	void TyFuncMaintButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		TyFuncMaintButton_actionPerformed_Interaction1(event);
	}
	void TyFuncMaintButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// function
			TyMenuBean21.doFuncMaint();

		} catch (java.lang.Exception e) {
		}
	}
  void TyMenu_windowClosing(java.awt.event.WindowEvent event)
  {
    // to do: code goes here.
       
    TyMenu_windowClosing_Interaction1(event);
  }
  void TyMenu_windowClosing_Interaction1(java.awt.event.WindowEvent event) {
    try {
      this.exitApplication();
    } catch (Exception e) {
    }
  }
	void TyNewCopyButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		TyNewCopyButton_actionPerformed_Interaction1(event);
	}
	void TyNewCopyButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// function
			TyMenuBean21.doNewCopy();

		} catch (java.lang.Exception e) {
		}
	}
  
  void TyNewRTNotifyButton_actionPerformed(java.awt.event.ActionEvent event)
  {
    // to do: code goes here.
       
    TyNewRTNotifyButton_actionPerformed_Interaction1(event);
  }
  void TyNewRTNotifyButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
  {
    try {
      // function
      TyMenuBean21.doRTNotify();

    } catch (java.lang.Exception e) {
    }
  }
  
  void TyNewRTLogButton_actionPerformed(java.awt.event.ActionEvent event)
  {
    // to do: code goes here.
       
    TyNewRTLogButton_actionPerformed_Interaction1(event);
  }
  void TyNewRTLogButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
  {
    try {
      // function
      TyMenuBean21.doRTLog();

    } catch (java.lang.Exception e) {
    }
  }
  
  void TyNewRTStatsButton_actionPerformed(java.awt.event.ActionEvent event)
  {
    // to do: code goes here.
       
    TyNewRTStatsButton_actionPerformed_Interaction1(event);
  }
  void TyNewRTStatsButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
  {
    try {
      // function
      TyMenuBean21.doRTStats();

    } catch (java.lang.Exception e) {
    }
  }  
  
	void TyOverallButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		TyOverallButton_actionPerformed_Interaction1(event);
	}
	void TyOverallButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// function
			TyMenuBean21.doOverall();

		} catch (java.lang.Exception e) {
		}
	}
	void TyQueDataButton3_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		TyQueDataButton3_actionPerformed_Interaction1(event);
	}
	void TyQueDataButton3_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// function
			TyMenuBean21.doQueData();

		} catch (java.lang.Exception e) {
		}
	}
	void TyQueMaintButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		TyQueMaintButton_actionPerformed_Interaction1(event);
	}
	void TyQueMaintButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// function
			TyMenuBean21.doQueMaint();

		} catch (java.lang.Exception e) {
		}
	}
	void TyQueThdButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// 			 
		TyQueThdButton_actionPerformed_Interaction1(event);
	}
	void TyQueThdButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// function
			TyMenuBean21.doQueThd();

		} catch (java.lang.Exception e) {
		}
	}
	void TyReqStatusButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		TyReqStatusButton_actionPerformed_Interaction1(event);
	}
	void TyReqStatusButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// function
			TyMenuBean21.doReqStatus();

		} catch (java.lang.Exception e) {
		}
	}
	void TyShutdownButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		TyShutdownButton_actionPerformed_Interaction1(event);
	}
	void TyShutdownButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {// function
			TyMenuBean21.doShutdown();

		} catch (java.lang.Exception e) {
		}
	}
	void TyStalledButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		TyStalledButton_actionPerformed_Interaction1(event);
	}
	void TyStalledButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// function
			TyMenuBean21.doStalled();

		} catch (java.lang.Exception e) {
		}
	}
	void TyStatsButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		TyStatsButton_actionPerformed_Interaction1(event);
	}
	void TyStatsButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// function
			TyMenuBean21.doStats();

		} catch (java.lang.Exception e) {
		}
	}
	void TyUserFuncButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		TyUserFuncButton_actionPerformed_Interaction1(event);
	}
	void TyUserFuncButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// function
				TyMenuBean21.doUserFunctions();

		} catch (java.lang.Exception e) {
		}
	}
	void TyUserQueButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		TyUserQueButton_actionPerformed_Interaction1(event);
	}
	void TyUserQueButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// function
				TyMenuBean21.doUserQueues();

		} catch (java.lang.Exception e) {
		}
	}
	void TyUserVarButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		TyUserVarButton_actionPerformed_Interaction1(event);
	}
	void TyUserVarButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// function
				TyMenuBean21.doUserVariables();

		} catch (java.lang.Exception e) {
		}
	}
	void TyVButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		TyVButton_actionPerformed_Interaction1(event);
	}
	void TyVButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// function
				TyMenuBean21.doTyVariables();

		} catch (java.lang.Exception e) {
		}
	}
	void TyWlDataButton_actionPerformed(java.awt.event.ActionEvent event)
	{
		// to do: code goes here.
			 
		TyWlDataButton_actionPerformed_Interaction1(event);
	}
	void TyWlDataButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
	{
		try {
			// function
			TyMenuBean21.doWlData();

		} catch (java.lang.Exception e) {
		}
	}
}
