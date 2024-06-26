
import sifen.Sifen;
import sifen.core.SifenConfig;
import sifen.core.beans.response.RespuestaConsultaDE;
import sifen.core.exceptions.SifenException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sifen.core.beans.response.RespuestaConsultaRUC;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ADM
 */
public class NewDialog extends java.awt.Dialog {

    /**
     * Creates new form NewDialog
     */
    public NewDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton2.setText("jButton2");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 170, -1, -1));

        add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       // try {
            //  try {
            // TODO add your handling code here:
    SifenConfig config = new SifenConfig(
    SifenConfig.TipoAmbiente.PROD,
    SifenConfig.TipoCertificadoCliente.PFX,"C:\\Users\\ADM\\Documents\\granvia.pfx","granviaelid");
    String cdc = "01800550242003019001953122023041714237981652";
        try {
            Sifen.setSifenConfig(config);
            //RespuestaConsultaRUC resp = Sifen.consultaRUC("5537745",config);
            RespuestaConsultaDE resp = Sifen.consultaDE(cdc);
            System.err.println(resp.getdMsgRes());
            System.err.println(resp.getRespuestaBruta());
            System.err.println(resp.getxContenDE());
            System.err.println(resp.getCodigoEstado());
            if (resp.getdMsgRes().equals("CDC encontrado")) {
                System.err.println("TRUE");
            } else {
                System.err.println("FALSE");
            }
            
//            Sifen.setSifenConfig(config);
//            SOAPTests rr = new SOAPTests();
//            rr.testRecepcionDE();
            
        } catch (SifenException ex) {
            Logger.getLogger(NewDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
           
        
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                NewDialog dialog = new NewDialog(new java.awt.Frame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
