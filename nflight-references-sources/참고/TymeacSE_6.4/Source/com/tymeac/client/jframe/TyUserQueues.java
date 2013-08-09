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
/**
 * The Tymeac User Queues display frame.
 */
public class TyUserQueues extends javax.swing.JFrame
{
  private static final long serialVersionUID = 1L;

  // Used by addNotify
  boolean frameSizeAdjusted = false;

  //{{DECLARE_CONTROLS
	javax.swing.JLabel Msg = new javax.swing.JLabel();
	javax.swing.JTextField QueText = new javax.swing.JTextField();
	javax.swing.JButton ImportButton = new javax.swing.JButton();
	javax.swing.JButton DeleteButton = new javax.swing.JButton();
	javax.swing.JButton FinishButton = new javax.swing.JButton();
	javax.swing.JLabel QueLabel = new javax.swing.JLabel();
	javax.swing.JTextField ApplText = new javax.swing.JTextField();
	javax.swing.JLabel ApplLabel = new javax.swing.JLabel();
	javax.swing.JCheckBox AgentBox = new javax.swing.JCheckBox();
	javax.swing.JTextField ThreadsText = new javax.swing.JTextField();
	javax.swing.JLabel ThreadsLabel = new javax.swing.JLabel();
	javax.swing.JCheckBox StartBox = new javax.swing.JCheckBox();
	javax.swing.JTextField IdleText = new javax.swing.JTextField();
	javax.swing.JLabel IdleLabel = new javax.swing.JLabel();
	javax.swing.JTextField KillText = new javax.swing.JTextField();
	javax.swing.JLabel KillLabel = new javax.swing.JLabel();
	javax.swing.JTextField NbrWLText = new javax.swing.JTextField();
	javax.swing.JLabel NbrWlLabel = new javax.swing.JLabel();
  
	javax.swing.JTextField NbrInText = new javax.swing.JTextField();
	javax.swing.JLabel NbrInLabel = new javax.swing.JLabel();
  
  javax.swing.JTextField TNbrInText = new javax.swing.JTextField();
  javax.swing.JLabel TNbrInLabel = new javax.swing.JLabel();
    
	javax.swing.JTextField OverallText = new javax.swing.JTextField();
	javax.swing.JTextField IndividualText = new javax.swing.JTextField();
	javax.swing.JTextField FactorText = new javax.swing.JTextField();
	javax.swing.JTextField AverageText = new javax.swing.JTextField();
	javax.swing.JLabel OverallLabel = new javax.swing.JLabel();
	javax.swing.JLabel IndividualLabel = new javax.swing.JLabel();
	javax.swing.JLabel ThreshLabel = new javax.swing.JLabel();
	javax.swing.JLabel FactorLabel = new javax.swing.JLabel();
	javax.swing.JLabel AverageLabel = new javax.swing.JLabel();
	java.awt.FileDialog ReadJavaFile = new java.awt.FileDialog(this);
	java.awt.FileDialog SaveJavaFile = new java.awt.FileDialog(this);
	javax.swing.JTextField TimeoutText = new javax.swing.JTextField();
	javax.swing.JLabel TimeoutLabel = new javax.swing.JLabel();
	javax.swing.JMenuBar JMenuBar1 = new javax.swing.JMenuBar();
	javax.swing.JMenu fileMenu = new javax.swing.JMenu();
	javax.swing.JMenuItem exitItem = new javax.swing.JMenuItem();
	javax.swing.JSeparator JSeparator1 = new javax.swing.JSeparator();
	javax.swing.JMenu helpMenu = new javax.swing.JMenu();
	javax.swing.JMenuItem Help = new javax.swing.JMenuItem();
	javax.swing.JMenuItem about = new javax.swing.JMenuItem();
	//}}

  TyUserQueMaintBean TyUserQueMaintBean21 = new TyUserQueMaintBean();  

  class SymWindow extends java.awt.event.WindowAdapter
  {
    public void windowClosing(java.awt.event.WindowEvent event)
    {
      Object object = event.getSource();
      if (object == TyUserQueues.this)
        TyQueMaint_windowClosing(event);
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
      else if (object == DeleteButton)
        DeleteButton_actionPerformed(event);
      else if (object == FinishButton)
        FinishButton_actionPerformed(event);                                   
    }
  }

	static java.util.ResourceBundle tyUserQueuesBundle = java.util.ResourceBundle.getBundle("com.tymeac.client.jframe.TyUserQueuesBundle");
  public TyUserQueues()
  {
    // This code is automatically generated by Visual Cafe when you add
    // components to the visual environment. It instantiates and initializes
    // the components. To modify the code, only use code syntax that matches
    // what Visual Cafe can generate, or Visual Cafe may be unable to back
    // parse your Java file into its visual environment.
    //{{INIT_CONTROLS
    setJMenuBar(JMenuBar1);
    setTitle(tyUserQueuesBundle.getString("TyUserQueues_title"));
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    getContentPane().setLayout(null);
    setSize(539,480);
    setVisible(false);
    //$$ JMenuBar1.move(120,480);
    fileMenu.setText(tyUserQueuesBundle.getString("fileMenu_text"));
    fileMenu.setActionCommand(tyUserQueuesBundle.getString("fileMenu_actionCommand"));
		fileMenu.setToolTipText(tyUserQueuesBundle.getString("fileMenu_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("fileMenu_toolTipText") : null);
    fileMenu.setMnemonic((int)'F');
    JMenuBar1.add(fileMenu);
    
    fileMenu.add(JSeparator1);
    
    exitItem.setText(tyUserQueuesBundle.getString("exitItem_text"));
    exitItem.setActionCommand(tyUserQueuesBundle.getString("exitItem_actionCommand"));
		exitItem.setToolTipText(tyUserQueuesBundle.getString("exitItem_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("exitItem_toolTipText") : null);
    exitItem.setMnemonic((int)'X');
    fileMenu.add(exitItem);
		JSeparator1.setToolTipText(tyUserQueuesBundle.getString("JSeparator1_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("JSeparator1_toolTipText") : null);
    
    helpMenu.setText(tyUserQueuesBundle.getString("helpMenu_text"));
    helpMenu.setActionCommand(tyUserQueuesBundle.getString("helpMenu_actionCommand"));
		helpMenu.setToolTipText(tyUserQueuesBundle.getString("helpMenu_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("helpMenu_toolTipText") : null);
    helpMenu.setMnemonic((int)'H');
    JMenuBar1.add(helpMenu);
    
    fileMenu.add(JSeparator1);
       
    Help.setText(tyUserQueuesBundle.getString("Help_text"));
    Help.setActionCommand(tyUserQueuesBundle.getString("Help_actionCommand"));
		Help.setToolTipText(tyUserQueuesBundle.getString("Help_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("Help_toolTipText") : null);
    Help.setMnemonic((int)'D');
    helpMenu.add(Help);
    
    fileMenu.add(JSeparator1);
    
    about.setText(tyUserQueuesBundle.getString("about_text"));
    about.setActionCommand(tyUserQueuesBundle.getString("about_actionCommand"));
		about.setToolTipText(tyUserQueuesBundle.getString("about_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("about_toolTipText") : null);
    about.setMnemonic((int)'A');
    helpMenu.add(about); 
    Msg.setText(tyUserQueuesBundle.getString("Msg_text"));
		Msg.setToolTipText(tyUserQueuesBundle.getString("Msg_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("Msg_toolTipText") : null);
    getContentPane().add(Msg);
    Msg.setForeground(new java.awt.Color(128,0,0));
    Msg.setBounds(20,440,400,30);
		QueText.setText(tyUserQueuesBundle.getString("QueText_text"));
    QueText.setToolTipText(tyUserQueuesBundle.getString("QueText_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("QueText_toolTipText") : null);
    getContentPane().add(QueText);
    QueText.setBounds(30,20,200,30);
    ImportButton.setText(tyUserQueuesBundle.getString("ImportButton_text"));
    ImportButton.setActionCommand(tyUserQueuesBundle.getString("ImportButton_actionCommand"));
    ImportButton.setToolTipText(tyUserQueuesBundle.getString("ImportButton_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("ImportButton_toolTipText") : null);
    getContentPane().add(ImportButton);
    ImportButton.setBounds(291,20,80,30);
    DeleteButton.setText(tyUserQueuesBundle.getString("DeleteButton_text"));
    DeleteButton.setActionCommand(tyUserQueuesBundle.getString("DeleteButton_actionCommand"));
    DeleteButton.setToolTipText(tyUserQueuesBundle.getString("DeleteButton_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("DeleteButton_toolTipText") : null);
    getContentPane().add(DeleteButton);
    DeleteButton.setBounds(432,20,80,30);
    FinishButton.setText(tyUserQueuesBundle.getString("FinishButton_text"));
    FinishButton.setActionCommand(tyUserQueuesBundle.getString("FinishButton_actionCommand"));
    FinishButton.setToolTipText(tyUserQueuesBundle.getString("FinishButton_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("FinishButton_toolTipText") : null);
    getContentPane().add(FinishButton);
    FinishButton.setBounds(432,420,80,30);
    QueLabel.setText(tyUserQueuesBundle.getString("QueLabel_text"));
		QueLabel.setToolTipText(tyUserQueuesBundle.getString("QueLabel_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("QueLabel_toolTipText") : null);
    getContentPane().add(QueLabel);
    QueLabel.setBounds(30,50,200,20);
		ApplText.setText(tyUserQueuesBundle.getString("ApplText_text"));
    ApplText.setToolTipText(tyUserQueuesBundle.getString("ApplText_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("ApplText_toolTipText") : null);
    getContentPane().add(ApplText);
    ApplText.setBounds(30,96,200,30);
    ApplLabel.setText(tyUserQueuesBundle.getString("ApplLabel_text"));
		ApplLabel.setToolTipText(tyUserQueuesBundle.getString("ApplLabel_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("ApplLabel_toolTipText") : null);
    getContentPane().add(ApplLabel);
    ApplLabel.setBounds(30,125,200,20);
    AgentBox.setText(tyUserQueuesBundle.getString("AgentBox_text"));
    AgentBox.setActionCommand(tyUserQueuesBundle.getString("AgentBox_actionCommand"));
    AgentBox.setToolTipText(tyUserQueuesBundle.getString("AgentBox_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("AgentBox_toolTipText") : null);
    getContentPane().add(AgentBox);
    AgentBox.setFont(new Font("Dialog", Font.PLAIN, 12));
    AgentBox.setBounds(252,96,120,16);
    
    StartBox.setText(tyUserQueuesBundle.getString("StartBox_text"));
    StartBox.setActionCommand(tyUserQueuesBundle.getString("StartBox_actionCommand"));
    StartBox.setToolTipText(tyUserQueuesBundle.getString("StartBox_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("StartBox_toolTipText") : null);
    getContentPane().add(StartBox);
    StartBox.setFont(new Font("Dialog", Font.PLAIN, 12));
    StartBox.setBounds(300,156,180,36);
		IdleText.setText(tyUserQueuesBundle.getString("IdleText_text"));
    IdleText.setToolTipText(tyUserQueuesBundle.getString("IdleText_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("IdleText_toolTipText") : null);
    getContentPane().add(IdleText);
    IdleText.setBounds(89,168,40,30);
    IdleLabel.setText(tyUserQueuesBundle.getString("IdleLabel_text"));
		IdleLabel.setToolTipText(tyUserQueuesBundle.getString("IdleLabel_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("IdleLabel_toolTipText") : null);
    getContentPane().add(IdleLabel);
    IdleLabel.setBounds(89,200,80,20);
		KillText.setText(tyUserQueuesBundle.getString("KillText_text"));
    KillText.setToolTipText(tyUserQueuesBundle.getString("KillText_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("KillText_toolTipText") : null);
    getContentPane().add(KillText);
    KillText.setBounds(204,168,40,30);
    KillLabel.setText(tyUserQueuesBundle.getString("KillLabel_text"));
		KillLabel.setToolTipText(tyUserQueuesBundle.getString("KillLabel_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("KillLabel_toolTipText") : null);
    getContentPane().add(KillLabel);
    KillLabel.setBounds(204,200,100,20);
    
    ThreadsText.setText(tyUserQueuesBundle.getString("ThreadsText_text"));
    ThreadsText.setToolTipText(tyUserQueuesBundle.getString("ThreadsText_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("ThreadsText_toolTipText") : null);
    getContentPane().add(ThreadsText);
    ThreadsText.setBounds(39,240,40,30);
    ThreadsLabel.setText(tyUserQueuesBundle.getString("ThreadsLabel_text"));
    ThreadsLabel.setToolTipText(tyUserQueuesBundle.getString("ThreadsLabel_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("ThreadsLabel_toolTipText") : null);
    getContentPane().add(ThreadsLabel);
    ThreadsLabel.setBounds(39,270,130,20);    
    
		NbrWLText.setText(tyUserQueuesBundle.getString("NbrWLText_text"));
    NbrWLText.setToolTipText(tyUserQueuesBundle.getString("NbrWLText_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("NbrWLText_toolTipText") : null);
    getContentPane().add(NbrWLText);
    NbrWLText.setBounds(152,240,40,30);  
    
    NbrWlLabel.setText(tyUserQueuesBundle.getString("NbrWlLabel_text"));
		NbrWlLabel.setToolTipText(tyUserQueuesBundle.getString("NbrWlLabel_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("NbrWlLabel_toolTipText") : null);
    getContentPane().add(NbrWlLabel);
    NbrWlLabel.setBounds(152,270,130,20);
    
		NbrInText.setText(tyUserQueuesBundle.getString("NbrInText_text"));
    NbrInText.setToolTipText(tyUserQueuesBundle.getString("NbrInText_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("NbrInText_toolTipText") : null);
    getContentPane().add(NbrInText);
    NbrInText.setBounds(268,240,40,30);
    
    NbrInLabel.setText(tyUserQueuesBundle.getString("NbrInLabel_text"));
    NbrInLabel.setToolTipText(tyUserQueuesBundle.getString("NbrInLabel_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("NbrInLabel_toolTipText") : null);
    getContentPane().add(NbrInLabel);
    NbrInLabel.setBounds(268,270,130,20); //369,270    
    
    TNbrInText.setText(tyUserQueuesBundle.getString("TNbrInText_text"));
    TNbrInText.setToolTipText(tyUserQueuesBundle.getString("TNbrInText_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("TNbrInText_toolTipText") : null);
    getContentPane().add(TNbrInText);
    TNbrInText.setBounds(384,240,40,30); //369,240
    
    TNbrInLabel.setText(tyUserQueuesBundle.getString("TNbrInLabel_text"));
		TNbrInLabel.setToolTipText(tyUserQueuesBundle.getString("TNbrInLabel_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("TNbrInLabel_toolTipText") : null);
    getContentPane().add(TNbrInLabel);
    TNbrInLabel.setBounds(384,270,130,20); //369,270
    
    OverallText.setText(tyUserQueuesBundle.getString("OverallText_text"));
    OverallText.setToolTipText(tyUserQueuesBundle.getString("OverallText_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("OverallText_toolTipText") : null);
    getContentPane().add(OverallText);
    OverallText.setBounds(36,348,40,30);
    
    IndividualText.setText(tyUserQueuesBundle.getString("IndividualText_text"));
    IndividualText.setToolTipText(tyUserQueuesBundle.getString("IndividualText_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("IndividualText_toolTipText") : null);
    getContentPane().add(IndividualText);
    IndividualText.setBounds(152,348,40,30);
    
    FactorText.setText(tyUserQueuesBundle.getString("FactorText_text"));
    FactorText.setToolTipText(tyUserQueuesBundle.getString("FactorText_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("FactorText_toolTipText") : null);
    getContentPane().add(FactorText);
    FactorText.setBounds(268,348,40,30);
    
    AverageText.setText(tyUserQueuesBundle.getString("AverageText_text"));
    AverageText.setToolTipText(tyUserQueuesBundle.getString("AverageText_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("AverageText_toolTipText") : null);
    getContentPane().add(AverageText);
    AverageText.setBounds(384,348,40,30);
    
    OverallLabel.setText(tyUserQueuesBundle.getString("OverallLabel_text"));
		OverallLabel.setToolTipText(tyUserQueuesBundle.getString("OverallLabel_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("OverallLabel_toolTipText") : null);
    getContentPane().add(OverallLabel);
    OverallLabel.setBounds(36,384,110,20);
    
    IndividualLabel.setText(tyUserQueuesBundle.getString("IndividualLabel_text"));
		IndividualLabel.setToolTipText(tyUserQueuesBundle.getString("IndividualLabel_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("IndividualLabel_toolTipText") : null);
    getContentPane().add(IndividualLabel);
    IndividualLabel.setBounds(152,384,110,20);
    
    ThreshLabel.setText(tyUserQueuesBundle.getString("ThreshLabel_text"));
		ThreshLabel.setToolTipText(tyUserQueuesBundle.getString("ThreshLabel_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("ThreshLabel_toolTipText") : null);
    getContentPane().add(ThreshLabel);
    ThreshLabel.setFont(new Font("Dialog", Font.BOLD, 14));
    ThreshLabel.setBounds(119,312,300,20);
    FactorLabel.setText(tyUserQueuesBundle.getString("FactorLabel_text"));
		FactorLabel.setToolTipText(tyUserQueuesBundle.getString("FactorLabel_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("FactorLabel_toolTipText") : null);
    getContentPane().add(FactorLabel);
    FactorLabel.setBounds(268,384,110,20);
    AverageLabel.setText(tyUserQueuesBundle.getString("AverageLabel_text"));
		AverageLabel.setToolTipText(tyUserQueuesBundle.getString("AverageLabel_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("AverageLabel_toolTipText") : null);
    getContentPane().add(AverageLabel);
    AverageLabel.setBounds(384,384,110,20);
    ReadJavaFile.setMode(FileDialog.LOAD);
    ReadJavaFile.setTitle(tyUserQueuesBundle.getString("ReadJavaFile_title"));
		ReadJavaFile.setDirectory(tyUserQueuesBundle.getString("ReadJavaFile_directory"));
    ReadJavaFile.setFile(tyUserQueuesBundle.getString("ReadJavaFile_file"));
		//$$ ReadJavaFile.move(0,481);
    //$$ OpenFile.move(0,481);
    SaveJavaFile.setMode(FileDialog.SAVE);
    SaveJavaFile.setTitle(tyUserQueuesBundle.getString("SaveJavaFile_title"));
		SaveJavaFile.setDirectory(tyUserQueuesBundle.getString("SaveJavaFile_directory"));
		SaveJavaFile.setFile(tyUserQueuesBundle.getString("SaveJavaFile_file"));
		//$$ SaveJavaFile.move(24,481);
		TimeoutText.setText(tyUserQueuesBundle.getString("TimeoutText_text"));
		TimeoutText.setToolTipText(tyUserQueuesBundle.getString("TimeoutText_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("TimeoutText_toolTipText") : null);
    //$$ SaveFile.move(24,481);
    //$$ SaveFileDialog.move(24,481);
    //$$ OpenCfgFile.move(0,481);
    getContentPane().add(TimeoutText);
    TimeoutText.setBounds(396,96,84,30);
    TimeoutLabel.setText(tyUserQueuesBundle.getString("TimeoutLabel_text"));
		TimeoutLabel.setToolTipText(tyUserQueuesBundle.getString("TimeoutLabel_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("TimeoutLabel_toolTipText") : null);
    getContentPane().add(TimeoutLabel);
    TimeoutLabel.setBounds(396,132,96,12);
		JMenuBar1.setToolTipText(tyUserQueuesBundle.getString("JMenuBar1_toolTipText").length() != 0 ? tyUserQueuesBundle.getString("JMenuBar1_toolTipText") : null);
    //$$ OpenFileDialog.move(0,481);

    //}}

    Msg.setBorder(new javax.swing.border.EtchedBorder());

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
    DeleteButton.addActionListener(lSymAction);
    FinishButton.addActionListener(lSymAction);
    //}}
  }
    /**
     * Creates a new instance of JFrame1 with the given title.
     * @param sTitle the title for the new frame.
     */
  public TyUserQueues(String sTitle)
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
  void DeleteButton_actionPerformed(java.awt.event.ActionEvent event)
  {
    // to do: code goes here.
       
    DeleteButton_actionPerformed_Interaction1(event);
  }
  void DeleteButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
  {
  
    // text field to string
    String queData = QueText.getText();  

    // When no data
     if  ((queData == null) ||
          (queData.length() < 1)) {

         // say so
         Msg.setText(tyUserQueuesBundle.getString("Msg_text_3"));

        // done
        return;
        
    } // endif
  
    String ty_dir = null;       

    try {
			SaveJavaFile.setVisible(true);
      ty_dir = SaveJavaFile.getDirectory();

    } // end-try 

    catch (java.lang.Exception e) {
    
        // say so
        Msg.setText(tyUserQueuesBundle.getString("Msg_text_4"));

        // done
        return;
        
    } // end-catch
    
    try {
       TyUserQueMaintBean21.deleteButton(ty_dir, QueText);

        // what came back
        switch (TyUserQueMaintBean21.getDelResult()) {

            case 0: Msg.setText(tyUserQueuesBundle.getString("case_0__Msg_text_1") + queData + tyUserQueuesBundle.getString("deleted"));      
                  
                    // null fields
                    QueText.setText(tyUserQueuesBundle.getString("QueText_text_1"));
                    ApplText.setText(tyUserQueuesBundle.getString("ApplText_text_1"));
                    TimeoutText.setText(tyUserQueuesBundle.getString("TimeoutText_text_1"));                    
                    AgentBox.setSelected(false);
                    StartBox.setSelected(false);
                    ThreadsText.setText(tyUserQueuesBundle.getString("ThreadsText_text_1"));
                    IdleText.setText(tyUserQueuesBundle.getString("IdleText_text_1"));
                    KillText.setText(tyUserQueuesBundle.getString("KillText_text_1"));
                    NbrWLText.setText(tyUserQueuesBundle.getString("NbrWLText_text_1"));
                    NbrInText.setText(tyUserQueuesBundle.getString("NbrInText_text_1"));
                    OverallText.setText(tyUserQueuesBundle.getString("OverallText_text_1"));
                    IndividualText.setText(tyUserQueuesBundle.getString("IndividualText_text_1"));
                    FactorText.setText(tyUserQueuesBundle.getString("FactorText_text_1"));
                    AverageText.setText(tyUserQueuesBundle.getString("AverageText_text_1"));
              
                    // done 
                    return;  
    
            case 1: Msg.setText(tyUserQueuesBundle.getString("case_1__Msg_text_1"));
                  return;
        
            case 2: Msg.setText(tyUserQueuesBundle.getString("case_2__Msg_text_1"));
                  return;

        } // end-switch

    } catch (java.lang.Exception e) {
    }
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
  }
  void FinishButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
  {
  
    // text field to string
    String queData = QueText.getText();  

    // When no data
     if  ((queData == null) ||
          (queData.length() < 1)) {

         // say so
         Msg.setText(tyUserQueuesBundle.getString("Msg_text_5"));

        // done
        return;
        
    } // endif
  
    String ty_dir = null;       

    try {
			SaveJavaFile.setVisible(true);
      ty_dir = SaveJavaFile.getDirectory();

    } // end-try 

    catch (java.lang.Exception e) {         
    } // end-catch
    
    try {
        TyUserQueMaintBean21.finishButton(ty_dir, 
                                      QueText,
                                      ApplText,
                                      TimeoutText,
                                      AgentBox,
                                      StartBox,
                                      ThreadsText,
                                      IdleText,
                                      KillText,
                                      NbrWLText,
                                      NbrInText,
                                      TNbrInText,
                                      OverallText,
                                      IndividualText,
                                      FactorText,
                                      AverageText);

        // what came back
        switch (TyUserQueMaintBean21.getUpdResult()) {

            case 0: Msg.setText(
                  queData
                  + " "
                  + tyUserQueuesBundle.getString("case_0__Msg_text_2"));
                  break;                    
            
            case 1: Msg.setText(tyUserQueuesBundle.getString("case_1__Msg_text_2")); 
                    break;
        
            case 2: Msg.setText(tyUserQueuesBundle.getString("case_2__Msg_text_2")); 
                  break;          
                  
            case 3: Msg.setText(tyUserQueuesBundle.getString("case_3__Msg_text_1")); 
                    break;  
        
            case 4: Msg.setText(tyUserQueuesBundle.getString("case_4__Msg_text")); 
                    break;
                
            case 5: Msg.setText(tyUserQueuesBundle.getString("case_5__Msg_text")); 
                    break;
                  
            case 6: Msg.setText(tyUserQueuesBundle.getString("case_6__Msg_text")); 
                    break;

            case 8: Msg.setText(tyUserQueuesBundle.getString("case_8__Msg_text")); 
                    break;

            case 9: Msg.setText(tyUserQueuesBundle.getString("case_9__Msg_text")); 
                    break;

            case 10:  Msg.setText(tyUserQueuesBundle.getString("case_10___Msg_text")); 
                    break;

            case 11:  Msg.setText(tyUserQueuesBundle.getString("case_11___Msg_text")); 
                    break;

            case 12:  Msg.setText(tyUserQueuesBundle.getString("case_12___Msg_text")); 
                    break;
              
        } // end-switch
                 
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

    th.putOut(tyUserQueuesBundle.getString("th_putOut"));
                                                                    
  }
  void ImportButton_actionPerformed(java.awt.event.ActionEvent event)
  {
    // to do: code goes here.
       
    ImportButton_actionPerformed_Interaction1(event);
  }
  void ImportButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event)
  {
  
    // text field to string
     String queData = QueText.getText();
                                              
    // When no data
    if  ((queData == null) ||
         (queData.length() < 1)) {
        
        // say so
        Msg.setText(tyUserQueuesBundle.getString("Msg_text_1"));

        // done
        return;
        
    } // endif
    
    String ty_dir = null;       

    try {
			ReadJavaFile.setVisible(true);
      ty_dir = ReadJavaFile.getDirectory();

    } // end-try 

    catch (java.lang.Exception e) {
    
        // say so
        Msg.setText(tyUserQueuesBundle.getString("Msg_text_2"));

        // done
        return;
        
    } // end-catch
  
    try {
        TyUserQueMaintBean21.importButton(ty_dir,
                                      QueText,
                                      ApplText,
                                      TimeoutText,
                                      AgentBox,
                                      StartBox,
                                      ThreadsText,
                                      IdleText,
                                      KillText,
                                      NbrWLText,
                                      NbrInText,
                                      TNbrInText,
                                      OverallText,
                                      IndividualText,
                                      FactorText,
                                      AverageText);

        // what came back
        switch (TyUserQueMaintBean21.getImpResult()) {

            case 0: Msg.setText(tyUserQueuesBundle.getString("case_0__Msg_text"));      
                  return;
                  
            case 1: Msg.setText(tyUserQueuesBundle.getString("case_1__Msg_text"));  
                  return;           
    
            case 2: Msg.setText(tyUserQueuesBundle.getString("case_2__Msg_text"));
                  return;
        
            case 3: Msg.setText(tyUserQueuesBundle.getString("case_3__Msg_text"));
                  return;
                  
            case 4: Msg.setText(tyUserQueuesBundle.getString("case_4__Msg_text_new"));
                  return;      

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
        // Add the following code if you want the Look and Feel
        // to be set to the Look and Feel of the native system.
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } 
        catch (Exception e) { 
        }
        

      //Create a new instance of our application's frame, and make it visible.
      (new TyUserQueues()).setVisible(true);
    } 
    catch (Throwable t) {
      t.printStackTrace();
      //Ensure the application exits with an error condition.
      System.exit(1);
    }
  }
  void TyQueMaint_windowClosing(java.awt.event.WindowEvent event)
  {
    // to do: code goes here.
       
    TyQueMaint_windowClosing_Interaction1(event);
  }
  void TyQueMaint_windowClosing_Interaction1(java.awt.event.WindowEvent event) {
    try {
      this.exitApplication();
    } catch (Exception e) {
    }
  }
}
