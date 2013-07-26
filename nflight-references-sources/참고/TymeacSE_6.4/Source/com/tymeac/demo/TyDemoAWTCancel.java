package com.tymeac.demo;

/* 
 * Copyright (c) 2002 - 2008 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

/**
 * Demonstrate cancelling a syncRequest() 
 */
public class TyDemoAWTCancel extends java.awt.Frame {
  
  private static final long serialVersionUID = 1L;
  private java.awt.Button CancelButton;
  private java.awt.TextField ResultText;
  private java.awt.Button SendButton;
  private java.awt.Label label1;
  private java.awt.Label label2;
  private java.awt.Label label3;
  private java.awt.Label label4;
  private java.awt.Label label5;
    
  // work module
  private TyDemoAWTCancelBean bean = null;
  
  // STATIC args from main()
  private static String[] my_args = null;
  
/**  
 * Contructor
 */  
public TyDemoAWTCancel() {
  initComponents();
  
  // Demo work module
  bean = new TyDemoAWTCancelBean(my_args);
  
} // end-constructor
  
/** This method is called from within the constructor to
 * initialize the form.
 */
private void initComponents() {

  label1 = new java.awt.Label();
  ResultText = new java.awt.TextField();
  label2 = new java.awt.Label();
  SendButton = new java.awt.Button();
  CancelButton = new java.awt.Button();
  label3 = new java.awt.Label();
  label4 = new java.awt.Label();
  label5 = new java.awt.Label();

  setBounds(new java.awt.Rectangle(0, 0, 0, 0));
  setMinimumSize(new java.awt.Dimension(400, 240));
  setTitle("Demo Cancel SyncRequest");
  addWindowListener(new java.awt.event.WindowAdapter() {
    public void windowClosing(java.awt.event.WindowEvent evt) {
      exitForm(evt);
    }
  });
  setLayout(null);

  label1.setText("Press Send and");
  add(label1);
  label1.setBounds(90, 30, 100, 20);
  label1.getAccessibleContext().setAccessibleName("Send");

  add(ResultText);
  ResultText.setBounds(60, 160, 310, 20);

  label2.setFont(new java.awt.Font("Dialog", 1, 12));
  label2.setText("Result:");
  add(label2);
  label2.setBounds(20, 160, 43, 20);

  SendButton.setLabel("Send");
  SendButton.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      SendButtonActionPerformed(evt);
    }
  });
  add(SendButton);
  SendButton.setBounds(110, 90, 60, 30);
  SendButton.getAccessibleContext().setAccessibleName("SendButton");

  CancelButton.setLabel("Cancel");
  CancelButton.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      CancelButtonActionPerformed(evt);
    }
  });
  add(CancelButton);
  CancelButton.setBounds(250, 90, 60, 30);
  CancelButton.getAccessibleContext().setAccessibleName("CancelButton");

  label3.setText("  Wait for reply");
  add(label3);
  label3.setBounds(100, 50, 80, 20);

  label4.setText("Press Cancel to");
  add(label4);
  label4.setBounds(230, 30, 90, 20);

  label5.setText("  Kill wait");
  add(label5);
  label5.setBounds(240, 50, 50, 20);

  pack();
  
} // end-method

/** Exit the Application */
private void exitForm(java.awt.event.WindowEvent evt) {
  System.exit(0);
}// end-method

private void SendButtonActionPerformed(java.awt.event.ActionEvent evt) {
  
  ResultText.setText(" "); // reset field to nothing
  
  bean.sendButton(ResultText);
  
} // end-method

private void CancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
  
  bean.cancelButton();
  
}// end-method

/**
 * @param args the command line arguments
 */
public static void main(String args[]) {
  
  my_args = args; // save to pass  to bean

  new TyDemoAWTCancel().setVisible(true);
  
} // end-method
} // end-class
