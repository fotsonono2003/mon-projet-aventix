package projet_consultation.Calcul.demoCalcul;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import projet_consultation.ConnexionBDD.Connexion_BDDGenerale;
import projet_consultation.creation_dossiers.mes_documents;

public class RegionCongo extends javax.swing.JFrame
{
    Connexion_BDDGenerale cnGen=null;
    mes_documents mes=new mes_documents();

    /** Creates new form RegionCongo */
    public RegionCongo()
    {
        initComponents();
        this.setLocationRelativeTo(null);
        try
        {
            cnGen = Connexion_BDDGenerale.getInstance();
            String requete="select distinct(adm2) from table_geometrique order by adm2";
            ResultSet result=cnGen.getResultset(requete);
            jComboBoxRegion.removeAllItems();
            while (result.next())
            {
                jComboBoxRegion.addItem(result.getString("adm2"));
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(RegionCongo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jComboBoxRegion = new javax.swing.JComboBox();
        jScrollPaneRegion = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("Form"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        jComboBoxRegion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxRegion.setName("jComboBoxRegion"); // NOI18N
        jComboBoxRegion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxRegionActionPerformed(evt);
            }
        });

        jScrollPaneRegion.setName("jScrollPaneRegion"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jComboBoxRegion, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(549, Short.MAX_VALUE))
            .addComponent(jScrollPaneRegion, javax.swing.GroupLayout.DEFAULT_SIZE, 705, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jComboBoxRegion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneRegion, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxRegionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxRegionActionPerformed
        // TODO add your handling code here:
        CarteKPIFiltre carteKPIFiltre = new CarteKPIFiltre();
        try
        {
            if (jComboBoxRegion.getSelectedItem()!=null)
            {
                jScrollPaneRegion.removeAll();
                jScrollPaneRegion.add(carteKPIFiltre.carteKPI_FilterParregion(mes.get_CheminParametre() + "stylebts.xml", mes.get_CheminLog(), jComboBoxRegion.getSelectedItem().toString()));
            }
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(RegionCongo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jComboBoxRegionActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RegionCongo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBoxRegion;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPaneRegion;
    // End of variables declaration//GEN-END:variables

}
