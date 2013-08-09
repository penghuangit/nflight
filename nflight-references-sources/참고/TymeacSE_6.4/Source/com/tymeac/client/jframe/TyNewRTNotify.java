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
 * The New Notify display frame.
 * @since 6.2
 */
public class TyNewRTNotify extends javax.swing.JFrame
{
  private static final long serialVersionUID = 1L;

  // Used by addNotify
  boolean frameSizeAdjusted = false;
  
  javax.swing.JLabel ResetLabel = new javax.swing.JLabel();
  
  javax.swing.JLabel StopLabel = new javax.swing.JLabel();
  
  javax.swing.JLabel DashLabel = new javax.swing.JLabel();
  javax.swing.JLabel NewLabel = new javax.swing.JLabel();
  
	javax.swing.JTextField FuncText = new javax.swing.JTextField(); 
  
	javax.swing.JLabel FuncLabel = new javax.swing.JLabel();
  
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
  TyNewRTNotifyBean notifyBean = null;
  
  // connection when embedded
	TymeacInterface Ti = null;

  class SymWindow extends java.awt.event.WindowAdapter
  {
	public void windowClosing(java.awt.event.WindowEvent event)
	{
	  Object object = event.getSource();
	  if (object == TyNewRTNotify.this)
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

	static java.util.ResourceBundle tyNewRTNotifyBundle = java.util.ResourceBundle.getBundle("com.tymeac.client.jframe.TyNewRTNotifyBundle");
 
  public TyNewRTNotify()
  {	
  
	//{{INIT_CONTROLS
	setJMenuBar(JMenuBar1);
	setTitle(tyNewRTNotifyBundle.getString("TyNewRTNotify_title"));
	setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	getContentPane().setLayout(null);
	setSize(500,300); //500,440
	setVisible(false);  
		
	//$$ JMenuBar1.move(168,312);
	fileMenu.setText(tyNewRTNotifyBundle.getString("fileMenu_text"));
	fileMenu.setActionCommand(tyNewRTNotifyBundle.getString("fileMenu_actionCommand"));
	fileMenu.setToolTipText(tyNewRTNotifyBundle.getString("fileMenu_toolTipText").length() != 0 ? tyNewRTNotifyBundle.getString("fileMenu_toolTipText") : null);
	fileMenu.setMnemonic((int)'F');
	JMenuBar1.add(fileMenu);
	
	fileMenu.add(JSeparator1);
	
	exitItem.setText(tyNewRTNotifyBundle.getString("exitItem_text"));
	exitItem.setActionCommand(tyNewRTNotifyBundle.getString("exitItem_actionCommand"));
	exitItem.setToolTipText(tyNewRTNotifyBundle.getString("exitItem_toolTipText").length() != 0 ? tyNewRTNotifyBundle.getString("exitItem_toolTipText") : null);
	exitItem.setMnemonic((int)'X');
	fileMenu.add(exitItem);
  
	JSeparator1.setToolTipText(tyNewRTNotifyBundle.getString("JSeparator1_toolTipText").length() != 0 ? tyNewRTNotifyBundle.getString("JSeparator1_toolTipText") : null);
	
	helpMenu.setText(tyNewRTNotifyBundle.getString("helpMenu_text"));
	helpMenu.setActionCommand(tyNewRTNotifyBundle.getString("helpMenu_actionCommand"));
	helpMenu.setToolTipText(tyNewRTNotifyBundle.getString("helpMenu_toolTipText").length() != 0 ? tyNewRTNotifyBundle.getString("helpMenu_toolTipText") : null);
	helpMenu.setMnemonic((int)'H');
	JMenuBar1.add(helpMenu);
	
	fileMenu.add(JSeparator1);
	   
	Help.setText(tyNewRTNotifyBundle.getString("Help_text"));
	Help.setActionCommand(tyNewRTNotifyBundle.getString("Help_actionCommand"));
	Help.setToolTipText(tyNewRTNotifyBundle.getString("Help_toolTipText").length() != 0 ? tyNewRTNotifyBundle.getString("Help_toolTipText") : null);
	Help.setMnemonic((int)'D');
	helpMenu.add(Help);
	
	fileMenu.add(JSeparator1);
	
	about.setText(tyNewRTNotifyBundle.getString("about_text"));
	about.setActionCommand(tyNewRTNotifyBundle.getString("about_actionCommand"));
	about.setToolTipText(tyNewRTNotifyBundle.getString("about_toolTipText").length() != 0 ? tyNewRTNotifyBundle.getString("about_toolTipText") : null);
	about.setMnemonic((int)'A');
	helpMenu.add(about); 
  
  ResetLabel.setText(tyNewRTNotifyBundle.getString("ResetLabel_text"));
  ResetLabel.setFont(new java.awt.Font("Dialog", 0, 12));
  getContentPane().add(ResetLabel);
  ResetLabel.setBounds(24,28,210,30);
  
  ResetButton.setText(tyNewRTNotifyBundle.getString("ResetButton_text"));
  ResetButton.setActionCommand(tyNewRTNotifyBundle.getString("ResetButton_actionCommand"));
  ResetButton.setToolTipText(tyNewRTNotifyBundle.getString("ResetButton_toolTipText").length() != 0 
      ? tyNewRTNotifyBundle.getString("ResetButton_toolTipText") : null);
  getContentPane().add(ResetButton);
  ResetButton.setBounds(170,24,75,34); //348,24,63,34
  
  StopLabel.setText(tyNewRTNotifyBundle.getString("StopLabel_text"));
  StopLabel.setFont(new java.awt.Font("Dialog", 0, 12));
  getContentPane().add(StopLabel);
  StopLabel.setBounds(253,28,210,30);
  
  StopButton.setText(tyNewRTNotifyBundle.getString("StopButton_text"));
  StopButton.setActionCommand(tyNewRTNotifyBundle.getString("StopButton_actionCommand"));
  StopButton.setToolTipText(tyNewRTNotifyBundle.getString("StopButton_toolTipText").length() != 0 
      ? tyNewRTNotifyBundle.getString("StopButton_toolTipText") : null);
  getContentPane().add(StopButton);
  StopButton.setBounds(390,24,75,34); //348,24,63,34 
  
  DashLabel.setText(tyNewRTNotifyBundle.getString("DashLabel_text"));
  DashLabel.setFont(new java.awt.Font("Dialog", 1, 14));
  getContentPane().add(DashLabel);
  DashLabel.setBounds(24,60,468,22);//348,24,63,34  
  
  NewLabel.setText(tyNewRTNotifyBundle.getString("NewLabel_text"));
  NewLabel.setFont(new java.awt.Font("Dialog", 0, 14));
  getContentPane().add(NewLabel);
  NewLabel.setBounds(24,80,113,22);//348,24,63,34    
  
  FuncLabel.setText(tyNewRTNotifyBundle.getString("FuncLabel_text"));
  getContentPane().add(FuncLabel);
  FuncLabel.setBounds(35,115,123,22);
  
	FuncText.setToolTipText(tyNewRTNotifyBundle.getString("FuncText_toolTipText").length() != 0 
      ? tyNewRTNotifyBundle.getString("FuncText_toolTipText") : null);
	getContentPane().add(FuncText);
	FuncText.setBounds(100,115,300,30);
      
  ImportButton.setText(tyNewRTNotifyBundle.getString("ImportButton_text"));
  ImportButton.setActionCommand(tyNewRTNotifyBundle.getString("ImportButton_actionCommand"));
  ImportButton.setToolTipText(tyNewRTNotifyBundle.getString("ImportButton_toolTipText").length() != 0 
        ? tyNewRTNotifyBundle.getString("ImportButton_toolTipText") : null);
  getContentPane().add(ImportButton);
  ImportButton.setBounds(100,170,75,34); //348,24,63,34
  
  UpdateButton.setText(tyNewRTNotifyBundle.getString("UpdateButton_text"));
  UpdateButton.setActionCommand(tyNewRTNotifyBundle.getString("UpdateButton_actionCommand"));
  UpdateButton.setToolTipText(tyNewRTNotifyBundle.getString("UpdateButton_toolTipText").length() != 0 
      ? tyNewRTNotifyBundle.getString("UpdateButton_toolTipText") : null);
  getContentPane().add(UpdateButton);
  UpdateButton.setBounds(220,170,75,34); //348,24,63,34
  
  Msg.setText(tyNewRTNotifyBundle.getString("Msg_text"));
  Msg.setToolTipText(tyNewRTNotifyBundle.getString("Msg_toolTipText").length() != 0 
      ? tyNewRTNotifyBundle.getString("Msg_toolTipText") : null);
  getContentPane().add(Msg);
  Msg.setForeground(new java.awt.Color(128,0,0));
  Msg.setBounds(20,250,386,30);  
  
	JMenuBar1.setToolTipText(tyNewRTNotifyBundle.getString("JMenuBar1_toolTipText").length() != 0 
      ? tyNewRTNotifyBundle.getString("JMenuBar1_toolTipText") : null);

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
public TyNewRTNotify(TymeacInterface TyI)
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
  public TyNewRTNotify(String sTitle)
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

	th.putOut(tyNewRTNotifyBundle.getString("th_putOut"));
	
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
	  if  (notifyBean == null) {

				// When internal
		    if  (Ti != null) {

    		     // get the obj
        		 notifyBean = new TyNewRTNotifyBean(Ti);
    		}
    		else {
        		// get the obj
        		notifyBean = new TyNewRTNotifyBean();

    		} // endif
	  } // endif

	  // do function
	  notifyBean.resetButton();
    
   // clear screen fields
   FuncText.setText("");
   
	  // result
	  switch (notifyBean.getResetResult()) {

		  case SetUpNotify.NotifySuccess: 
        Msg.setText(tyNewRTNotifyBundle.getString("case_NotifySuccess"));
				return;                   
			
		  case -1: 
        Msg.setText(tyNewRTNotifyBundle.getString("case__1__Msg_text")); 
				return;
		
		  case SetUpNotify.NotifyNoCurr: 
        Msg.setText(tyNewRTNotifyBundle.getString("case_NotifyNoCurr")); 
				return;         
				  
		  case SetUpNotify.NotifyNotNec: 
        Msg.setText(tyNewRTNotifyBundle.getString("case_NotifyNotNec")); 
			  return;
          
      default: 
        Msg.setText("OOPS CameBack=" + notifyBean.getResetResult());    		
								
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
    if  (notifyBean == null) {

        // When internal
        if  (Ti != null) {

             // get the obj
             notifyBean = new TyNewRTNotifyBean(Ti);
        }
        else {
            // get the obj
            notifyBean = new TyNewRTNotifyBean();

        } // endif
    } // endif

    // do function
    notifyBean.stopButton();
    
   // clear screen fields
   FuncText.setText("");

    // result
    switch (notifyBean.getStopResult()) {

      case SetUpNotify.NotifySuccess: 
        Msg.setText(tyNewRTNotifyBundle.getString("case_NotifySuccess"));
        return;                   
      
      case -1: 
        Msg.setText(tyNewRTNotifyBundle.getString("case__1__Msg_text")); 
        return;
    
      case SetUpNotify.NotifyNoCurr: 
        Msg.setText(tyNewRTNotifyBundle.getString("case_NotifyNoCurr")); 
        return;         
          
      case SetUpNotify.NotifyNotNec: 
        Msg.setText(tyNewRTNotifyBundle.getString("case_NotifyNotNec")); 
        return;
        
      case SetUpNotify.NotifyStopped: 
        Msg.setText(tyNewRTNotifyBundle.getString("case_NotifyStopped")); 
        return;
          
      default: 
        Msg.setText("OOPS CameBack=" + notifyBean.getStopResult());        
                
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
    if  (notifyBean == null) {

        // When internal
        if  (Ti != null) {

             // get the obj
             notifyBean = new TyNewRTNotifyBean(Ti);
        }
        else {
            // get the obj
            notifyBean = new TyNewRTNotifyBean();

        } // endif
    } // endif

    // do function
    notifyBean.importButton(FuncText);

    switch (notifyBean.getImportResult()) {

      case SetUpNotify.NotifySuccess: 
        Msg.setText(tyNewRTNotifyBundle.getString("case_NotifySuccess"));
        return;                   
      
      case -1: 
        Msg.setText(tyNewRTNotifyBundle.getString("case__1__Msg_text")); 
        return;
    
      case SetUpNotify.NotifyNoCurr: 
        Msg.setText(tyNewRTNotifyBundle.getString("case_NotifyNoCurr")); 
        return;         
          
      case SetUpNotify.NotifyNotNec: 
        Msg.setText(tyNewRTNotifyBundle.getString("case_NotifyNotNec")); 
        return;
        
      case SetUpNotify.NotifyNotInUse:
        Msg.setText(tyNewRTNotifyBundle.getString("case_NotifyNotInUse")); 
        return;
          
      default: 
        Msg.setText("OOPS CameBack=" + notifyBean.getImportResult());       
                
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
    String Func = FuncText.getText();

    // When no data
    if  (((Func == null) ||
          (Func.length() < 1)))  {

        // say so
        Msg.setText(tyNewRTNotifyBundle.getString("Msg_text_1"));

        // done
        return;

    } // endif
  
    // When no obj 
    if  (notifyBean == null) {

        // When internal
        if  (Ti != null) {

             // get the obj
             notifyBean = new TyNewRTNotifyBean(Ti);
        }
        else {
            // get the obj
            notifyBean = new TyNewRTNotifyBean();

        } // endif
    } // endif

    // do function
    notifyBean.newButton(FuncText);

    switch (notifyBean.getNewResult()) {

      case SetUpNotify.NotifySuccess: 
        Msg.setText(tyNewRTNotifyBundle.getString("case_NotifySuccess"));
        return;                   
      
      case -1: 
        Msg.setText(tyNewRTNotifyBundle.getString("case__1__Msg_text")); 
        return;
    
      case SetUpNotify.NotifyNoCurr: 
        Msg.setText(tyNewRTNotifyBundle.getString("case_NotifyNoCurr")); 
        return;         
          
      case SetUpNotify.NotifyNotNec: 
        Msg.setText(tyNewRTNotifyBundle.getString("case_NotifyNotNec")); 
        return;
        
      case SetUpNotify.NotifyInvFunc: 
        Msg.setText(tyNewRTNotifyBundle.getString("case_NotifyInvFunc")); 
        return;  
          
      default: 
        Msg.setText("OOPS CameBack=" + notifyBean.getNewResult());       
                
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
	  (new TyNewRTNotify()).setVisible(true);
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
