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
import java.net.*;

/**
 * The About Tymeac display JDialog class.
 */
public class TyAbout extends javax.swing.JDialog
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 6698423786106826483L;
	//{{DECLARE_CONTROLS
  javax.swing.JButton okButton = new javax.swing.JButton();
  javax.swing.JLabel coopLabel = new javax.swing.JLabel();
  javax.swing.JLabel tyLable = new javax.swing.JLabel("Line", SwingConstants.CENTER);
  javax.swing.JLabel copyLable = new javax.swing.JLabel("Line", SwingConstants.CENTER);
  static java.util.ResourceBundle TyAboutBundle = java.util.ResourceBundle.getBundle("com.tymeac.client.jframe.TyAboutBundle");
  //}}

  class SymWindow extends java.awt.event.WindowAdapter
  {
  public void windowClosing(java.awt.event.WindowEvent event)
  {
    Object object = event.getSource();
    if (object == TyAbout.this)
    jAbout_windowClosing(event);
  }
  }

  class SymAction implements java.awt.event.ActionListener
  {
  public void actionPerformed(java.awt.event.ActionEvent event)
  {
    Object object = event.getSource();
    if (object == okButton)
    okButton_actionPerformed(event);
  }
  }

  public TyAbout(Frame parentFrame)
  {
  super(parentFrame);
  
  setTitle(TyAboutBundle.getString("TyAbout_title"));
  setModal(true);
  getContentPane().setLayout(null);
//  getContentPane().setFont(new Font(TyAboutBundle.getString("TyAbout_Dialog"), Font.PLAIN, 12));

  setSize(350,220);
  setVisible(false);
  
  okButton.setText(TyAboutBundle.getString("OK"));
  okButton.setActionCommand(TyAboutBundle.getString("OK"));
  okButton.setOpaque(false); 
  getContentPane().add(okButton);
  okButton.setBounds(152,150,51,25);
  
  coopLabel.setText(TyAboutBundle.getString("CoopSoft"));
  getContentPane().add(coopLabel);
  coopLabel.setBounds(0,0,204,60);
    
  tyLable.setText(TyAboutBundle.getString("TyVersion"));
  getContentPane().add(tyLable);
  tyLable.setBounds(48,82,250,24);
  //tyLable.setOpaque(true);


  copyLable.setText(TyAboutBundle.getString("copyright")); 
  getContentPane().add(copyLable);
  copyLable.setBounds(0,105,340,24);

  // default class loader  
  ClassLoader cl = getClass().getClassLoader();
  
  try {
      // find the coopsoft image
      URL url = cl.getResource(TyAboutBundle.getString("look"));
      
      // When found
      if(url != null) {

          // set image                
          coopLabel.setIcon(new ImageIcon(url)); 
          
      } // endif
    } // end-try
    
     catch(Exception e) {
            // ignore
     } // end-catch 
  
  
  //{{REGISTER_LISTENERS
  SymWindow aSymWindow = new SymWindow();
  this.addWindowListener(aSymWindow);
  SymAction lSymAction = new SymAction();
  okButton.addActionListener(lSymAction);
  //}}
  }
  void jAbout_windowClosing(java.awt.event.WindowEvent event)
  {
    // to do: code goes here.
       
    jAbout_windowClosing_Interaction1(event);
  }
  void jAbout_windowClosing_Interaction1(java.awt.event.WindowEvent event) {
    try {
      // JAboutDialog Hide the JAboutDialog
      this.setVisible(false);
    } catch (Exception e) {
    }
  }
  void okButton_actionPerformed(java.awt.event.ActionEvent event)
  {
    // to do: code goes here.
       
    okButton_actionPerformed_Interaction1(event);
  }
  void okButton_actionPerformed_Interaction1(java.awt.event.ActionEvent event) {
    try {
      // JAboutDialog Hide the JAboutDialog
      this.setVisible(false);
    } catch (Exception e) {
    }
  }
}
