package com.tymeac.client.jframe;

/* 
 * Copyright (c) 1998 - 2008 Cooperative Software Systems, Inc. 
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
 * The New Stats display frame.
 * @since 6.2
 */
public class TyNewRTStats extends javax.swing.JFrame
{
  private static final long serialVersionUID = 1L;

  // Used by addNotify
  boolean frameSizeAdjusted = false;
  
  javax.swing.JLabel ResetLabel = new javax.swing.JLabel();
  
  javax.swing.JLabel StopLabel = new javax.swing.JLabel();
  
  javax.swing.JLabel DashLabel = new javax.swing.JLabel();
  javax.swing.JLabel NewLabel = new javax.swing.JLabel();
  
	javax.swing.JTextField DBMSText = new javax.swing.JTextField();
	javax.swing.JTextField DirText  = new javax.swing.JTextField();
  javax.swing.JTextField FileText = new javax.swing.JTextField();
  javax.swing.JTextField AltText  = new javax.swing.JTextField();  
  
	javax.swing.JLabel DBMSLabel = new javax.swing.JLabel();
	javax.swing.JLabel DirLabel  = new javax.swing.JLabel();
  javax.swing.JLabel FileLabel = new javax.swing.JLabel();
  javax.swing.JLabel AltLabel  = new javax.swing.JLabel();
  
	javax.swing.JLabel Msg = new javax.swing.JLabel();
  
	javax.swing.JButton ResetButton  = new javax.swing.JButton();
  javax.swing.JButton StopButton  = new javax.swing.JButton();
  javax.swing.JButton ImportButton = new javax.swing.JButton();
  javax.swing.JButton UpdateButton = new javax.swing.JButton();
  
	javax.swing.JMenuBar JMenuBar1 = new javax.swing.JMenuBar();
	javax.swing.JMenu fileMenu = new javax.swing.JMenu();
	javax.swing.JMenuItem exitItem = new javax.swing.JMenuItem();
	javax.swing.JSeparator JSeparator1 = new javax.swing.JSeparator();
	javax.swing.JMenu helpMenu = new javax.swing.JMenu();
	javax.swing.JMenuItem Help = new javax.swing.JMenuItem();
	javax.swing.JMenuItem about = new javax.swing.JMenuItem();
	
  // logic module
  TyNewRTStatsBean statsBean = null;
  
  // connection when embedded
	TymeacInterface Ti = null;

  class SymWindow extends java.awt.event.WindowAdapter
  {
	public void windowClosing(java.awt.event.WindowEvent event)
	{
	  Object object = event.getSource();
	  if (object == TyNewRTStats.this)
		TyNewCopy_windowClosing(event);
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
	  else if (object == ResetButton)
		ResetButton_actionPerformed(event);	
    else if (object == StopButton)
    StopButton_actionPerformed(event); 
    else if (object == ImportButton)
    ImportButton_actionPerformed(event);
    else if (object == UpdateButton)
    UpdateButton_actionPerformed(event);
	  
	}
  }

	static java.util.ResourceBundle tyNewRTStatsBundle = java.util.ResourceBundle.getBundle("com.tymeac.client.jframe.TyNewRTStatsBundle");
 
  public TyNewRTStats()
  {	
  
	//{{INIT_CONTROLS
	setJMenuBar(JMenuBar1);
	setTitle(tyNewRTStatsBundle.getString("TyNewRTStats_title"));
	setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	getContentPane().setLayout(null);
	setSize(500,400); //500,440
	setVisible(false);  
		
	//$$ JMenuBar1.move(168,312);
	fileMenu.setText(tyNewRTStatsBundle.getString("fileMenu_text"));
	fileMenu.setActionCommand(tyNewRTStatsBundle.getString("fileMenu_actionCommand"));
	fileMenu.setToolTipText(tyNewRTStatsBundle.getString("fileMenu_toolTipText").length() != 0 ? tyNewRTStatsBundle.getString("fileMenu_toolTipText") : null);
	fileMenu.setMnemonic((int)'F');
	JMenuBar1.add(fileMenu);
	
	fileMenu.add(JSeparator1);
	
	exitItem.setText(tyNewRTStatsBundle.getString("exitItem_text"));
	exitItem.setActionCommand(tyNewRTStatsBundle.getString("exitItem_actionCommand"));
	exitItem.setToolTipText(tyNewRTStatsBundle.getString("exitItem_toolTipText").length() != 0 ? tyNewRTStatsBundle.getString("exitItem_toolTipText") : null);
	exitItem.setMnemonic((int)'X');
	fileMenu.add(exitItem);
  
	JSeparator1.setToolTipText(tyNewRTStatsBundle.getString("JSeparator1_toolTipText").length() != 0 ? tyNewRTStatsBundle.getString("JSeparator1_toolTipText") : null);
	
	helpMenu.setText(tyNewRTStatsBundle.getString("helpMenu_text"));
	helpMenu.setActionCommand(tyNewRTStatsBundle.getString("helpMenu_actionCommand"));
	helpMenu.setToolTipText(tyNewRTStatsBundle.getString("helpMenu_toolTipText").length() != 0 ? tyNewRTStatsBundle.getString("helpMenu_toolTipText") : null);
	helpMenu.setMnemonic((int)'H');
	JMenuBar1.add(helpMenu);
	
	fileMenu.add(JSeparator1);
	   
	Help.setText(tyNewRTStatsBundle.getString("Help_text"));
	Help.setActionCommand(tyNewRTStatsBundle.getString("Help_actionCommand"));
	Help.setToolTipText(tyNewRTStatsBundle.getString("Help_toolTipText").length() != 0 ? tyNewRTStatsBundle.getString("Help_toolTipText") : null);
	Help.setMnemonic((int)'D');
	helpMenu.add(Help);
	
	fileMenu.add(JSeparator1);
	
	about.setText(tyNewRTStatsBundle.getString("about_text"));
	about.setActionCommand(tyNewRTStatsBundle.getString("about_actionCommand"));
	about.setToolTipText(tyNewRTStatsBundle.getString("about_toolTipText").length() != 0 ? tyNewRTStatsBundle.getString("about_toolTipText") : null);
	about.setMnemonic((int)'A');
	helpMenu.add(about); 
  
  ResetLabel.setText(tyNewRTStatsBundle.getString("ResetLabel_text"));
  ResetLabel.setFont(new java.awt.Font("DiaStats", 0, 12));
  getContentPane().add(ResetLabel);
  ResetLabel.setBounds(24,28,210,30);
  
  ResetButton.setText(tyNewRTStatsBundle.getString("ResetButton_text"));
  ResetButton.setActionCommand(tyNewRTStatsBundle.getString("ResetButton_actionCommand"));
  ResetButton.setToolTipText(tyNewRTStatsBundle.getString("ResetButton_toolTipText").length() != 0 
      ? tyNewRTStatsBundle.getString("ResetButton_toolTipText") : null);
  getContentPane().add(ResetButton);
  ResetButton.setBounds(160,24,75,34); //348,24,63,34
  
  StopLabel.setText(tyNewRTStatsBundle.getString("StopLabel_text"));
  StopLabel.setFont(new java.awt.Font("Dialog", 0, 12));
  getContentPane().add(StopLabel);
  StopLabel.setBounds(253,28,210,30);
  
  StopButton.setText(tyNewRTStatsBundle.getString("StopButton_text"));
  StopButton.setActionCommand(tyNewRTStatsBundle.getString("StopButton_actionCommand"));
  StopButton.setToolTipText(tyNewRTStatsBundle.getString("StopButton_toolTipText").length() != 0 
      ? tyNewRTStatsBundle.getString("StopButton_toolTipText") : null);
  getContentPane().add(StopButton);
  StopButton.setBounds(380,24,75,34); //348,24,63,34 
  
  DashLabel.setText(tyNewRTStatsBundle.getString("DashLabel_text"));
  DashLabel.setFont(new java.awt.Font("DiaStats", 1, 14));
  getContentPane().add(DashLabel);
  DashLabel.setBounds(24,60,468,22);//348,24,63,34  
  
  NewLabel.setText(tyNewRTStatsBundle.getString("NewLabel_text"));
  NewLabel.setFont(new java.awt.Font("DiaStats", 0, 14));
  getContentPane().add(NewLabel);
  NewLabel.setBounds(24,80,180,22);//348,24,63,34    
  
  DBMSLabel.setText(tyNewRTStatsBundle.getString("DBMSLabel_text"));
  getContentPane().add(DBMSLabel);
  DBMSLabel.setBounds(35,115,123,22);
  
	DBMSText.setToolTipText(tyNewRTStatsBundle.getString("DBMSText_toolTipText").length() != 0 
      ? tyNewRTStatsBundle.getString("DBMSText_toolTipText") : null);
	getContentPane().add(DBMSText);
	DBMSText.setBounds(100,115,300,30);
  
  DirLabel.setText(tyNewRTStatsBundle.getString("DirLabel_text"));
  getContentPane().add(DirLabel);
  DirLabel.setBounds(50,160,123,30);
  
	DirText.setText(tyNewRTStatsBundle.getString("DirText_text"));
	DirText.setToolTipText(tyNewRTStatsBundle.getString("DirText_toolTipText").length() != 0 
      ? tyNewRTStatsBundle.getString("DirText_toolTipText") : null);
	getContentPane().add(DirText);
	DirText.setBounds(100,160,78,30);
  
  FileLabel.setText(tyNewRTStatsBundle.getString("FileLabel_text"));
  getContentPane().add(FileLabel);
  FileLabel.setBounds(183,160,123,30);
  
  FileText.setText(tyNewRTStatsBundle.getString("FileText_text"));
  FileText.setToolTipText(tyNewRTStatsBundle.getString("FileText_toolTipText").length() != 0 
      ? tyNewRTStatsBundle.getString("FileText_toolTipText") : null);
  getContentPane().add(FileText);
  FileText.setBounds(240,160,240,30);
  
  AltLabel.setText(tyNewRTStatsBundle.getString("AltLabel_text"));
  getContentPane().add(AltLabel);
  AltLabel.setBounds(50,205,123,30);
  
  AltText.setText(tyNewRTStatsBundle.getString("AltText_text"));
  AltText.setToolTipText(tyNewRTStatsBundle.getString("AltText_toolTipText").length() != 0 
      ? tyNewRTStatsBundle.getString("AltText_toolTipText") : null);
  getContentPane().add(AltText);
  AltText.setBounds(100,205,300,30);
      
  ImportButton.setText(tyNewRTStatsBundle.getString("ImportButton_text"));
  ImportButton.setActionCommand(tyNewRTStatsBundle.getString("ImportButton_actionCommand"));
  ImportButton.setToolTipText(tyNewRTStatsBundle.getString("ImportButton_toolTipText").length() != 0 
        ? tyNewRTStatsBundle.getString("ImportButton_toolTipText") : null);
  getContentPane().add(ImportButton);
  ImportButton.setBounds(100,280,75,34); //348,24,63,34
  
  UpdateButton.setText(tyNewRTStatsBundle.getString("UpdateButton_text"));
  UpdateButton.setActionCommand(tyNewRTStatsBundle.getString("UpdateButton_actionCommand"));
  UpdateButton.setToolTipText(tyNewRTStatsBundle.getString("UpdateButton_toolTipText").length() != 0 
      ? tyNewRTStatsBundle.getString("UpdateButton_toolTipText") : null);
  getContentPane().add(UpdateButton);
  UpdateButton.setBounds(220,280,75,34); //348,24,63,34
  
  Msg.setText(tyNewRTStatsBundle.getString("Msg_text"));
  Msg.setToolTipText(tyNewRTStatsBundle.getString("Msg_toolTipText").length() != 0 
      ? tyNewRTStatsBundle.getString("Msg_toolTipText") : null);
  getContentPane().add(Msg);
  Msg.setForeground(new java.awt.Color(128,0,0));
  Msg.setBounds(20,350,386,30);  
  
	JMenuBar1.setToolTipText(tyNewRTStatsBundle.getString("JMenuBar1_toolTipText").length() != 0 
      ? tyNewRTStatsBundle.getString("JMenuBar1_toolTipText") : null);

	Msg.setBorder(new javax.swing.border.EtchedBorder());
  
	//{{REGISTER_LISTENERS
	SymWindow aSymWindow = new SymWindow();
	this.addWindowListener(aSymWindow);
	
	SymAction lSymAction = new SymAction();
	exitItem.addActionListener(lSymAction);
	Help.addActionListener(lSymAction);
	about.addActionListener(lSymAction);
	ResetButton.addActionListener(lSymAction);
  StopButton.addActionListener(lSymAction);
  ImportButton.addActionListener(lSymAction);
  UpdateButton.addActionListener(lSymAction);
	  
  }
 
/**
  * Constructor when embedded 
  * @param TyI embedded Tymeac server
  */
public TyNewRTStats(TymeacInterface TyI)
{
	this();
  
  Ti = TyI;
  
  try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
  } 
  catch (Exception e) { 
  }

 } // end-constructor
 
	/**
	 * Creates a new instance of JFrame1 with the given title.
	 * @param sTitle the title for the new frame.
	 */
  public TyNewRTStats(String sTitle)
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
	setSize(insets.left + insets.right + size.width, insets.top + insets.bottom + size.height + menuBarHeight);
  }
  //{{DECLARE_MENUS
	//}}

  void exitApplication()
  {    
	this.setVisible(false);    // hide the Frame
	this.dispose();            // free the system resources
	 if (Ti == null) {
      System.exit(0);            // close the application 
   }
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

	th.putOut(tyNewRTStatsBundle.getString("th_putOut"));
	
  }
  
  void ResetButton_actionPerformed(java.awt.event.ActionEvent event)
  {
	// to do: code goes here.
	   
	ResetButton_actionPerformed_Interaction1(event);
  }
  void ResetButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
  {
	 try {	
	  // When no obj 
	  if  (statsBean == null) {

				// When internal
		    if  (Ti != null) {

    		     // get the obj
        		 statsBean = new TyNewRTStatsBean(Ti);
    		}
    		else {
        		// get the obj
        		statsBean = new TyNewRTStatsBean();

    		} // endif
	  } // endif

	  // do function
	  statsBean.resetButton();
    
   // clear screen fields
   DBMSText.setText("");
   DirText.setText("");
   FileText.setText("");
   AltText.setText("");  

	  // result
	  switch (statsBean.getResetResult()) {

		  case SetUpStats.StatsSuccess: 
        Msg.setText(tyNewRTStatsBundle.getString("case_StatsSuccess"));
				return;                   
			
		  case -1: 
        Msg.setText(tyNewRTStatsBundle.getString("case__1__Msg_text")); 
				return;
		
		  case SetUpStats.StatsNoCurr: 
        Msg.setText(tyNewRTStatsBundle.getString("case_StatsNoCurr")); 
				return;         
				  
		  case SetUpStats.StatsNotNec: 
        Msg.setText(tyNewRTStatsBundle.getString("case_StatsNotNec")); 
			  return;
				  
		  case SetUpStats.StatsFailVeryOld: 
        Msg.setText(tyNewRTStatsBundle.getString("case_StatsFailVeryOld")); 
				return;

			case SetUpStats.StatsCreateFail: 
        Msg.setText(tyNewRTStatsBundle.getString("case_StatsCreateFail")); 
				return;

			case SetUpStats.StatsFailVeryNew: 
        Msg.setText(tyNewRTStatsBundle.getString("case_StatsFailVeryNew")); 
				return;
          
      default: 
        Msg.setText("OOPS CameBack=" + statsBean.getResetResult());    		
								
	} // end-switch

	} catch (java.lang.Exception e) {
	}
  }
  
  void StopButton_actionPerformed(java.awt.event.ActionEvent event)
  {
  // to do: code goes here.
     
  StopButton_actionPerformed_Interaction1(event);
  }
  void StopButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
  {
   try {  
    // When no obj 
    if  (statsBean == null) {

        // When internal
        if  (Ti != null) {

             // get the obj
             statsBean = new TyNewRTStatsBean(Ti);
        }
        else {
            // get the obj
            statsBean = new TyNewRTStatsBean();

        } // endif
    } // endif

    // do function
    statsBean.stopButton();
    
   // clear screen fields
   DBMSText.setText("");
   DirText.setText("");
   FileText.setText("");
   AltText.setText("");  

    // result
    switch (statsBean.getStopResult()) {

      case SetUpStats.StatsSuccess: 
        Msg.setText(tyNewRTStatsBundle.getString("case_StatsSuccess"));
        return;                   
      
      case -1: 
        Msg.setText(tyNewRTStatsBundle.getString("case__1__Msg_text")); 
        return;
    
      case SetUpStats.StatsNoCurr: 
        Msg.setText(tyNewRTStatsBundle.getString("case_StatsNoCurr")); 
        return;         
          
      case SetUpStats.StatsNotNec: 
        Msg.setText(tyNewRTStatsBundle.getString("case_StatsNotNec")); 
        return;
          
      case SetUpStats.StatsFailVeryOld: 
        Msg.setText(tyNewRTStatsBundle.getString("case_StatsFailVeryOld")); 
        return;

      case SetUpStats.StatsCreateFail: 
        Msg.setText(tyNewRTStatsBundle.getString("case_StatsCreateFail")); 
        return;

      case SetUpStats.StatsFailVeryNew: 
        Msg.setText(tyNewRTStatsBundle.getString("case_StatsFailVeryNew")); 
        return;
        
      case SetUpStats.StatsStopped: 
        Msg.setText(tyNewRTStatsBundle.getString("case_StatsStopped")); 
        return;
          
      default: 
        Msg.setText("OOPS CameBack=" + statsBean.getStopResult());        
                
  } // end-switch

  } catch (java.lang.Exception e) {
  }
  }
  
  void ImportButton_actionPerformed(java.awt.event.ActionEvent event)
  {
  // to do: code goes here.
     
  ImportButton_actionPerformed_Interaction1(event);
  }
  void ImportButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
  {
   try {  
    // When no obj 
    if  (statsBean == null) {

        // When internal
        if  (Ti != null) {

             // get the obj
             statsBean = new TyNewRTStatsBean(Ti);
        }
        else {
            // get the obj
            statsBean = new TyNewRTStatsBean();

        } // endif
    } // endif

    // do function
    statsBean.importButton(DBMSText, DirText, FileText, AltText);

    switch (statsBean.getImportResult()) {

      case SetUpStats.StatsSuccess: 
        Msg.setText(tyNewRTStatsBundle.getString("case_StatsSuccess"));
        return;                   
      
      case -1: 
        Msg.setText(tyNewRTStatsBundle.getString("case__1__Msg_text")); 
        return;
    
      case SetUpStats.StatsNoCurr: 
        Msg.setText(tyNewRTStatsBundle.getString("case_StatsNoCurr")); 
        return;         
          
      case SetUpStats.StatsNotNec: 
        Msg.setText(tyNewRTStatsBundle.getString("case_StatsNotNec")); 
        return;
          
      case SetUpStats.StatsFailVeryOld: 
        Msg.setText(tyNewRTStatsBundle.getString("case_StatsFailVeryOld")); 
        return;

      case SetUpStats.StatsCreateFail: 
        Msg.setText(tyNewRTStatsBundle.getString("case_StatsCreateFail")); 
        return;

      case SetUpStats.StatsFailVeryNew: 
        Msg.setText(tyNewRTStatsBundle.getString("case_StatsFailVeryNew")); 
        return;
        
      case SetUpStats.StatsNotInUse:
        Msg.setText(tyNewRTStatsBundle.getString("case_StatsNotInUse")); 
        return;  
          
      default: 
        Msg.setText("OOPS CameBack=" + statsBean.getImportResult());       
                
  } // end-switch

  } catch (java.lang.Exception e) {
  }
  }
  
  void UpdateButton_actionPerformed(java.awt.event.ActionEvent event)
  {
  // to do: code goes here.
     
  UpdateButton_actionPerformed_Interaction1(event);
  }
  void UpdateButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
  {
   try {
    // text field to string
    String DBMS = DBMSText.getText();
    String FileName = FileText.getText();
    String Alt = AltText.getText();

    // When no data
    if  (((DBMS == null) ||
          (DBMS.length() < 1)) 
    &&
         ((FileName == null) ||
          (FileName.length() < 1)) 
    &&
         ((Alt == null) ||
          (Alt.length() < 1))) {

        // say so
        Msg.setText(tyNewRTStatsBundle.getString("Msg_text_1"));

        // done
        return;

    } // endif
  
    // When no obj 
    if  (statsBean == null) {

        // When internal
        if  (Ti != null) {

             // get the obj
             statsBean = new TyNewRTStatsBean(Ti);
        }
        else {
            // get the obj
            statsBean = new TyNewRTStatsBean();

        } // endif
    } // endif

    // do function
    statsBean.newButton(DBMSText, DirText, FileText, AltText);

    switch (statsBean.getNewResult()) {

      case SetUpStats.StatsSuccess: 
        Msg.setText(tyNewRTStatsBundle.getString("case_StatsSuccess"));
        return;                   
      
      case -1: 
        Msg.setText(tyNewRTStatsBundle.getString("case__1__Msg_text")); 
        return;
    
      case SetUpStats.StatsNoCurr: 
        Msg.setText(tyNewRTStatsBundle.getString("case_StatsNoCurr")); 
        return;         
          
      case SetUpStats.StatsNotNec: 
        Msg.setText(tyNewRTStatsBundle.getString("case_StatsNotNec")); 
        return;
          
      case SetUpStats.StatsFailVeryOld: 
        Msg.setText(tyNewRTStatsBundle.getString("case_StatsFailVeryOld")); 
        return;

      case SetUpStats.StatsCreateFail: 
        Msg.setText(tyNewRTStatsBundle.getString("case_StatsCreateFail")); 
        return;

      case SetUpStats.StatsFailVeryNew: 
        Msg.setText(tyNewRTStatsBundle.getString("case_StatsFailVeryNew")); 
        return;
          
      default: 
        Msg.setText("OOPS CameBack=" + statsBean.getNewResult());       
                
  } // end-switch

  } catch (java.lang.Exception e) {
  }
  }  
  
  /**
   * The entry point for this application.
   * Sets the Look and Feel to the System Look and Feel.
   * Creates a new JFrame1 and makes it visible.
   */
  static public void main(String args[])
  {
	try {
    
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } 
    catch (Exception e) { 
    }
	  //Create a new instance of our application's frame, and make it visible.
	  (new TyNewRTStats()).setVisible(true);
	} 
	catch (Throwable t) {
	  t.printStackTrace();
	  //Ensure the application exits with an error condition.
	  System.exit(1);
	}
  }
  void TyNewCopy_windowClosing(java.awt.event.WindowEvent event)
  {
	// to do: code goes here.
	   
	TyNewCopy_windowClosing_Interaction1(event);
  }
  void TyNewCopy_windowClosing_Interaction1(java.awt.event.WindowEvent event) {
	try {
	  this.exitApplication();
	} catch (Exception e) {
	}
  }
}
