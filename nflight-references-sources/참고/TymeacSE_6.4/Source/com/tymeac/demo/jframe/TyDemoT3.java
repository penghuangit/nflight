/* 
 * Copyright (c) 2002 - 2007 Cooperative Software Systems, Inc. 
 *
 * The contents of this file are subject to the Common Public License   
 * ("License"). You may not use this file except in compliance with
 * the License. A copy of the License is part of this distribution and
 * is also available at:
 * http://www.opensource.org/licenses/cpl.php
 */

package com.tymeac.demo.jframe;
import javax.swing.*;

/**
 * T3
 * 
 */
public class TyDemoT3 extends javax.swing.JFrame {
  
  private static final long serialVersionUID = 1L;

  /** Creates new form TyDemoT3 */
  public TyDemoT3() {
    initComponents();
  }
  
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
  private void initComponents() {
    jLabel1 = new javax.swing.JLabel();
    jTextField1 = new javax.swing.JTextField();
    jButton1 = new javax.swing.JButton();
    jLabel2 = new javax.swing.JLabel();
    jScrollPane1 = new javax.swing.JScrollPane();
    jList1 = new javax.swing.JList();
    jButton2 = new javax.swing.JButton();
    jButton3 = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle(java.util.ResourceBundle.getBundle("com/tymeac/demo/jframe/TyDemoT3Properties").getString("Tymeac_Multi-Thread_Test"));
    setFocusCycleRoot(false);
    jLabel1.setFont(new java.awt.Font("Arial", 0, 12));
    jLabel1.setText(java.util.ResourceBundle.getBundle("com/tymeac/demo/jframe/TyDemoT3Properties").getString("Number_of_Threads:"));

    jTextField1.setFont(new java.awt.Font("Arial Black", 0, 12));
    jTextField1.setHorizontalAlignment(SwingConstants.CENTER);
    jTextField1.setToolTipText(java.util.ResourceBundle.getBundle("com/tymeac/demo/jframe/TyDemoT3Properties").getString("Max_threads_to_start"));
    jTextField1.setFocusCycleRoot(true);

    jButton1.setFont(new java.awt.Font("Arial", 0, 12));
    jButton1.setText(java.util.ResourceBundle.getBundle("com/tymeac/demo/jframe/TyDemoT3Properties").getString("Start"));
    jButton1.setToolTipText(java.util.ResourceBundle.getBundle("com/tymeac/demo/jframe/TyDemoT3Properties").getString("Start_the_multi_threads"));
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });

    jLabel2.setText(java.util.ResourceBundle.getBundle("com/tymeac/demo/jframe/TyDemoT3Properties").getString("___Status_______RC______Times_Used"));

    jList1.setFont(new java.awt.Font("Arial", 0, 12));
    jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    jList1.setDragEnabled(true);
    jScrollPane1.setViewportView(jList1);

    jButton2.setFont(new java.awt.Font("Arial", 0, 12));
    jButton2.setForeground(new java.awt.Color(255, 0, 0));
    jButton2.setText(java.util.ResourceBundle.getBundle("com/tymeac/demo/jframe/TyDemoT3Properties").getString("Stop"));
    jButton2.setToolTipText(java.util.ResourceBundle.getBundle("com/tymeac/demo/jframe/TyDemoT3Properties").getString("Stop_all_processing"));
    jButton2.setFocusPainted(false);
    jButton2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton2ActionPerformed(evt);
      }
    });

    jButton3.setFont(new java.awt.Font("Arial", 0, 12));
    jButton3.setText(java.util.ResourceBundle.getBundle("com/tymeac/demo/jframe/TyDemoT3Properties").getString("Refresh"));
    jButton3.setToolTipText(java.util.ResourceBundle.getBundle("com/tymeac/demo/jframe/TyDemoT3Properties").getString("Refresh_the_display"));
    jButton3.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton3ActionPerformed(evt);
      }
    });

    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(layout.createSequentialGroup()
        .addContainerGap()
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
          .add(layout.createSequentialGroup()
            .add(jLabel1)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 38, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(45, 45, 45)
            .add(jButton1)
            .add(53, 53, 53)
            .add(jButton2)
            .add(0, 0, Short.MAX_VALUE))
          .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 220, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(49, 49, 49)
            .add(jButton3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 95, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(0, 0, Short.MAX_VALUE))
          .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 201, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        .add(40, 40, 40))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
      .add(layout.createSequentialGroup()
        .add(21, 21, 21)
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
          .add(jLabel1)
          .add(jButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
          .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
          .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        .add(38, 38, 38)
        .add(jLabel2)
        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
          .add(jButton3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
          .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE))
        .addContainerGap())
    );
    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
// TODO add your handling code here:
    
    try {
      // 
      bean.doRefresh(jList1);

    } catch (java.lang.Exception e) {
    }
  }//GEN-LAST:event_jButton3ActionPerformed

  private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
// TODO add your handling code here:
    
    try {
      // 
      bean.doStop();
    
    } catch (java.lang.Exception e) {
    }
  }//GEN-LAST:event_jButton2ActionPerformed

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
// TODO add your handling code here:
    
    if  (bean == null) {
      
        bean = new TyDemoT3Bean2(savedArgs);
        
    }
    
    try {
      // 
      bean.doStart(jTextField1, jList1);

      // When completed normally
      if  (bean.getStartResult() == 1) {
          
          // the start button is now disabled
          jButton1.setEnabled(false);
    } // endif
            
    } catch (java.lang.Exception e) {
    }
  }//GEN-LAST:event_jButton1ActionPerformed
  
  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    
    try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } 
      catch (Exception e) { 
      }
    
    savedArgs = args;
    
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new TyDemoT3().setVisible(true);
      }
    });
  }
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton jButton1;
  private javax.swing.JButton jButton2;
  private javax.swing.JButton jButton3;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JList jList1;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JTextField jTextField1;
  // End of variables declaration//GEN-END:variables
  
  private com.tymeac.demo.jframe.TyDemoT3Bean2 bean;
  
  // saved from main()
  private static String[] savedArgs = null;
  
}
