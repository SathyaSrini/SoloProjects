

package libraryproj;


/**
 *
 * @author Sathya
 */
public class MainForm extends javax.swing.JFrame {

    /**
     * Creates new form Mainwindow
     */
    public MainForm() {
        
        initComponents();
        this.setLocationRelativeTo(null);
    }
    
    public void loginOK(int iFlag){
        if (iFlag == 1){
            this.siginButton.setVisible(false);
            this.checkinButton.setEnabled(true);
            this.searchButton.setEnabled(true);
            this.checkoutButton.setEnabled(true);
            this.borrowerButton.setEnabled(true);
            this.paymentButton.setEnabled(true);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        searchButton = new javax.swing.JButton();
        checkoutButton = new javax.swing.JButton();
        titleLabel = new javax.swing.JLabel();
        checkinButton = new javax.swing.JButton();
        borrowerButton = new javax.swing.JButton();
        paymentButton = new javax.swing.JButton();
        exitButton = new javax.swing.JButton();
        siginButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setMinimumSize(new java.awt.Dimension(39, 39));
        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 1000));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        searchButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        searchButton.setText("Search");
        searchButton.setEnabled(false);
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });
        jPanel1.add(searchButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 210, 30));

        checkoutButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        checkoutButton.setText("Check - Out");
        checkoutButton.setEnabled(false);
        checkoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkoutButtonActionPerformed(evt);
            }
        });
        jPanel1.add(checkoutButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 203, 210, 30));

        titleLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("CS6360.501 Library Management");
        jPanel1.add(titleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 480, 60));

        checkinButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        checkinButton.setText("Check - In");
        checkinButton.setEnabled(false);
        checkinButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkinButtonActionPerformed(evt);
            }
        });
        jPanel1.add(checkinButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 150, 210, 30));

        borrowerButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        borrowerButton.setText("Add Borrower");
        borrowerButton.setEnabled(false);
        borrowerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrowerButtonActionPerformed(evt);
            }
        });
        jPanel1.add(borrowerButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 270, 220, 40));

        paymentButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        paymentButton.setText("Manage Fines");
        paymentButton.setEnabled(false);
        paymentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paymentButtonActionPerformed(evt);
            }
        });
        jPanel1.add(paymentButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 203, 210, 30));

        exitButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        exitButton.setText("EXIT");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });
        jPanel1.add(exitButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 350, 130, 40));

        siginButton.setBackground(new java.awt.Color(153, 255, 255));
        siginButton.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        siginButton.setText("Login");
        siginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                siginButtonActionPerformed(evt);
            }
        });
        jPanel1.add(siginButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, 220, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        System.exit(0);
        // TODO add your handling code here:
    }//GEN-LAST:event_exitButtonActionPerformed

    private void paymentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymentButtonActionPerformed
        PaymentForm pw = new PaymentForm(this,true);
        pw.setVisible(true);
    }//GEN-LAST:event_paymentButtonActionPerformed

    private void borrowerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrowerButtonActionPerformed
        AddBorrowerForm bw = new AddBorrowerForm(this,true);
        bw.setVisible(true);
    }//GEN-LAST:event_borrowerButtonActionPerformed

    private void checkinButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkinButtonActionPerformed
        CheckInForm cinw = new CheckInForm(this,true);
        cinw.setVisible(true);
    }//GEN-LAST:event_checkinButtonActionPerformed

    private void checkoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkoutButtonActionPerformed
        CheckOutForm cw = new CheckOutForm(this,true);
        cw.setVisible(true);
    }//GEN-LAST:event_checkoutButtonActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        SearchBooksForm sw = new SearchBooksForm(this,true);
        sw.setVisible(true);
    }//GEN-LAST:event_searchButtonActionPerformed

    private void siginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_siginButtonActionPerformed
        // TODO add your handling code here:
        SignInForm si = new SignInForm(this,true);
        si.setVisible(true);
        this.loginOK(SignInForm.iLoginOK);
    }//GEN-LAST:event_siginButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                new MainForm().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton borrowerButton;
    private javax.swing.JButton checkinButton;
    private javax.swing.JButton checkoutButton;
    private javax.swing.JButton exitButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton paymentButton;
    private javax.swing.JButton searchButton;
    private javax.swing.JButton siginButton;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
