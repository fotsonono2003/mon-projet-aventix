package projet_consultation.Principale;

import projet_consultation.creation_dossiers.mes_documents;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import projet_consultation.ConnexionBDD.ConnexionBDDOperateur;
import projet_consultation.ConnexionBDD.Connexion_BDDGenerale;
import projet_consultation.Calcul.CalculBox;
import projet_consultation.ClassesGenerales.Fichier;
import projet_consultation.ClassesGenerales.Operateur;
import projet_consultation.ClassesGenerales.ParametreKPI;
import projet_consultation.Projet_ConsultationAboutBox;
import projet_consultation.Projet_ConsultationApp;
import projet_consultation.genererCartes.CarteKPI;
import projet_consultation.genererCartes.DefStyle;
import projet_consultation.genererCartes.StyleCarte;

public class Interface_Principale extends javax.swing.JFrame
{
    private JDialog aboutBox;
    public static boolean CalculFini=false;
    private Operateur operateur;
    private Operateur operateurCalcule=null;
    private JDialog jd1=null;
    private JDialog jd2=null;
    private JDialog jd3=null;
    private JDialog jd4=null;
    private JDialog jd5=null;
    private JDialog jd6=null;
    private JDialog jd7=null;
    private JDialog jd8=null;
    private String cheminFichierlog=null;
    private String cheminParametres=null;
    private int pageTrafic=1;
    private int pageTCH=1;
    private int pageSDCCH=1;
    private int pageMaint=1;
    private int pageHandOver=1;
    private int pageSMS=1;
    private Dimension dRect = new Dimension(900, 270);
    private Dimension dCar = new Dimension(300, 290);
    
    private List <Operateur> ListOperateur=new ArrayList<Operateur>() ;
    private mes_documents mes=new mes_documents();

    /** Creates new form Interface_Principale */

    private void showCalculBox(Operateur op,String datedebut,String datefin,boolean  ImpRpt)
    {
        try
        {
            JFrame mainFrame = Projet_ConsultationApp.getApplication().getMainFrame();
            JDialog calculbox = new CalculBox(mainFrame, op, datedebut, datefin, ImpRpt);
            calculbox.setLocationRelativeTo(mainFrame);
            Projet_ConsultationApp.getApplication().show(calculbox);
        }
        catch (Exception ex)
        { 
        }
    }
    

    @Override
    protected void finalize()
    {
        try
        {
            super.finalize();
        }
        catch (Throwable throwable)
        {
        }
    }
    
    private void SupprimerImage(String chemin)
    {
        File file = new File(chemin);
        if (file.exists() && file.isDirectory())
        {
            File[] files = file.listFiles();
            int dircount = 0;
            int filecount = 0;
            boolean recursivePath = true;

            if (files != null)
            {
                for (int i = 0; i < files.length; i++)
                {
                    if (files[i].isDirectory() == true)
                    {
                        System.out.println("Dossier:" + files[i].getAbsolutePath());
                        dircount++;
                    } else
                    {
                        System.out.println("Fichier:" + files[i].getName());
                        filecount++;
                        files[i].delete();
                    }
                    if (files[i].isDirectory() == true && recursivePath == true)
                    {
                        this.SupprimerImage(files[i].getAbsolutePath());
                        files[i].delete();
                    }
                }
            }
        }
    }

    private Operateur getSelectedOperateur()
    {
        int n=ListOperateur.size();
        for (int i=0;i<n;i++)
        {
            if(ListOperateur.get(i).getNomOperateur().trim().equals(jComboOprateur.getSelectedItem().toString().trim()))
            {
                return ListOperateur.get(i);
            }
        }
        return null;
    }


    private void init_interface_Principale()
    {
        try
        {
            //jTabbedPaneMain.setEnabledAt(0, false);
            jTabbedPane2G.setEnabledAt(1, false);
            jTabbedPane2G.setEnabledAt(2, false);
            jTabbedPane2G.setEnabledAt(3, false);
            jTabbedPane2G.setEnabledAt(4, false);
            jTabbedPane2G.setEnabledAt(5, false);
            //jTabbedPaneMain.setEnabledAt(6, false);
            jTabbedPane2G.setEnabledAt(7, false);
            jTabbedPane2G.setEnabledAt(8, false);

            jTabbedPane3G.setEnabledAt(1, false);
            jTabbedPane3G.setEnabledAt(2, false);
            jTabbedPane3G.setEnabledAt(3, false);
            jTabbedPane3G.setEnabledAt(4, false);
            jTabbedPane3G.setEnabledAt(5, false);
            //jTabbedPaneMain.setEnabledAt(6, false);
            jTabbedPane3G.setEnabledAt(7, false);
            jTabbedPane3G.setEnabledAt(8, false);

            jButtonPrecTr.setEnabled(false);
            jButtonSuivTr.setEnabled(false);
            jButtonPrecAcces.setEnabled(false);
            jButtonPrecMaint.setEnabled(false);
            jButtonSuivMaint.setEnabled(false);
            jButtonPrecHO.setEnabled(false);
            jButtonPrecSMS.setEnabled(false);
            jButtonSuivSMS.setEnabled(false);
            
            cheminFichierlog=mes.get_CheminLog();
            cheminParametres=mes.get_CheminParametre();
            org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(projet_consultation.Projet_ConsultationApp.class).getContext().getResourceMap(projet_consultation.Principale.Interface_Principale.class);
            ImageIcon im1=resourceMap.getImageIcon("logo_regulateur");
            Image im=im1.getImage();
            im=im.getScaledInstance(140, 70, Image.SCALE_DEFAULT);
            jLabelLogo.setIcon(new ImageIcon(im));

            ResultSet resultat = null;
            try
            {
                Connexion_BDDGenerale cnbdd = Connexion_BDDGenerale.getInstance();
                jComboOprateur.removeAllItems();
                String requete1 = "select * from operateur where nom_operateur is not null";
                resultat = cnbdd.getResultset(requete1);
                while (resultat.next())
                {
                    String str = null;
                    Operateur op = new Operateur();

                    str = resultat.getString("code_operateur");
                    if (str != null)
                    {
                        op.setCodeOperateur(str.trim());
                    } else {
                        op.setCodeOperateur("");
                    }
                    str = resultat.getString("nom_bdd");
                    if (str != null) {
                        op.setBddOperateur(str.trim());
                    } else {
                        op.setBddOperateur("");
                    }
                    try {
                        op.setIdOperateur(resultat.getInt("id_operateur"));
                    } catch (Exception ex) {
                    }

                    str = resultat.getString("adresse_bdd");
                    if (str != null) {
                        op.setAdresseBDDOperateur(str.trim());

                    } else {
                        op.setAdresseBDDOperateur("");
                    }
                    str = resultat.getString("nom_operateur").toUpperCase().trim();
                    if (str != null) {
                        op.setNomOperateur(str.trim());
                    } else {
                        op.setNomOperateur("");
                    }
                    str = resultat.getString("adresseipmv");
                    if (str != null) {
                        op.setAdresseMV(str.trim());

                    } else {
                        op.setAdresseMV("");
                    }
                    str = resultat.getString("loginmv");
                    if (str != null) {
                        op.setLoginmv(str.trim());

                    } else {
                        op.setLoginmv("");
                    }
                    str = resultat.getString("motdepassemv");
                    if (str != null) {
                        op.setPasswdmv(str.trim());

                    } else {
                        op.setPasswdmv("");
                    }
                    int portsc =21;
                    try
                    {
                        portsc = resultat.getInt("portmv");
                        op.setPortmv(portsc);
                    } catch (Exception ex)
                    {
                        op.setPortmv(21);
                    }
                    
                    str = resultat.getString("logindcs");
                    if (str != null) {
                        op.setLogindcs(str.trim());

                    } else {
                        op.setLogindcs("");
                    }
                    str = resultat.getString("motdepassedcs");
                    if (str != null) {
                        op.setPasswddcs(str.trim());
                    } else {
                        op.setPasswddcs("");
                    }
                    try
                    {
                        int portdcs = resultat.getInt("portdcs");
                        op.setPortdcs(portdcs);
                    } catch (Exception ex)
                    {
                        op.setPortdcs(21);
                    }
                    str = resultat.getString("adressedcs");
                    if (str != null) {
                        op.setAdressesc(str.trim());

                    } else {
                        op.setAdressesc("");
                    }
                    str = resultat.getString("nom_machine");
                    if (str != null) {
                        op.setNomMV(str.trim());
                    } else {
                        op.setNomMV("");
                    }
                    try
                    {
                        op.setEquipement(resultat.getString("nom_equipement"));
                    } catch (Exception e) {
                        op.setEquipement("");
                    }
                    try
                    {
                        op.setCode_eq(resultat.getInt("code_eq"));
                    } catch (Exception e) {
                        op.setCode_eq(0);
                    }
                    try
                    {
                        op.setGeneration(resultat.getString("generation"));
                    } catch (Exception e) {
                        op.setGeneration("");
                    }
                    ListOperateur.add(op);
                    jComboOprateur.addItem(op.getNomOperateur().trim());
                }
            } catch (SQLException ex)
            {
                Logger.getLogger(Interface_Principale.class.getName()).log(Level.SEVERE, null, ex);
            }
            ///////////
            try
            {
                resultat.close();
            } catch (Exception ex){}
            SetImageDefautOngletSynthese();
        }
        catch (Exception ex)
        {
            Logger.getLogger(Interface_Principale.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Interface_Principale()
    {
        initComponents();
        GraphicsEnvironment graphicsEnvironment=GraphicsEnvironment.getLocalGraphicsEnvironment();

        Rectangle rect=graphicsEnvironment.getMaximumWindowBounds();
        this.setSize((int)rect.getWidth(),(int)rect.getHeight());
        dRect=new Dimension((int)(rect.getWidth()/2.1350),(int)(rect.getHeight()/3.880));
        dCar=new Dimension((int)(rect.getWidth()/6.29),(int)(rect.getHeight()/3.6));
        System.out.println("Taille,Largeur:"+rect.getWidth()+" Longueur:"+rect.getHeight());
        
        ImageIcon Iconqostracker = new javax.swing.ImageIcon(getClass().getResource("/resources/logoqostracker.png"));
        Image im1=Iconqostracker.getImage();
        this.setIconImage(im1);
        init_interface_Principale();
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel59 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jComboOprateur = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jdatedebut = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        jdatefin = new com.toedter.calendar.JDateChooser();
        jButtonValider = new javax.swing.JButton();
        jCheckBoxPDF = new javax.swing.JCheckBox();
        jLabelLogo = new javax.swing.JLabel();
        jTabbedPanePrincipale = new javax.swing.JTabbedPane();
        jTabbedPane2G = new javax.swing.JTabbedPane();
        jPanelSynthese = new javax.swing.JPanel();
        jLabelSynCdr4 = new javax.swing.JLabel();
        jLabelSynCdr1 = new javax.swing.JLabel();
        jLabelSynCdr7 = new javax.swing.JLabel();
        jLabelSynCdr2 = new javax.swing.JLabel();
        jLabelSynCdr5 = new javax.swing.JLabel();
        jLabelSynCdr8 = new javax.swing.JLabel();
        jLabelSynCarte2 = new javax.swing.JLabel();
        jLabelSynCarte5 = new javax.swing.JLabel();
        jLabelSynCarte8 = new javax.swing.JLabel();
        jLabelSynCarte1 = new javax.swing.JLabel();
        jLabelSynCarte4 = new javax.swing.JLabel();
        jLabelSynCarte7 = new javax.swing.JLabel();
        jLabelSynCdr3 = new javax.swing.JLabel();
        jLabelSynCdr6 = new javax.swing.JLabel();
        jLabelSynCdr9 = new javax.swing.JLabel();
        jLabelSynCarte6 = new javax.swing.JLabel();
        jLabelSynCarte3 = new javax.swing.JLabel();
        jLabelSynCarte9 = new javax.swing.JLabel();
        jPanelTrafic = new javax.swing.JPanel();
        jButtonPrecTr = new javax.swing.JButton();
        jButtonSuivTr = new javax.swing.JButton();
        jPanelTrImage = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jLabelTr1 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jLabelTr3 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        jLabelTr2 = new javax.swing.JLabel();
        jScrollPaneCarteTrafic = new java.awt.ScrollPane();
        jPanelCanalTCH = new javax.swing.JPanel();
        jButtonPrecAcces = new javax.swing.JButton();
        jButtonSuivAcces = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        jLabelCanTCH1 = new javax.swing.JLabel();
        jScrollPane14 = new javax.swing.JScrollPane();
        jLabelCanTCH3 = new javax.swing.JLabel();
        jScrollPane21 = new javax.swing.JScrollPane();
        jLabelCanTCH2 = new javax.swing.JLabel();
        jScrollPaneCarteCanTCH = new java.awt.ScrollPane();
        jPanelMaintenabilite = new javax.swing.JPanel();
        jButtonPrecMaint = new javax.swing.JButton();
        jButtonSuivMaint = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane23 = new javax.swing.JScrollPane();
        jLabelMaint1 = new javax.swing.JLabel();
        jScrollPane24 = new javax.swing.JScrollPane();
        jLabelMaint3 = new javax.swing.JLabel();
        jScrollPane25 = new javax.swing.JScrollPane();
        jLabelMaint2 = new javax.swing.JLabel();
        jScrollPaneCarteMaint = new java.awt.ScrollPane();
        jPanelHandover = new javax.swing.JPanel();
        jButtonPrecHO = new javax.swing.JButton();
        jButtonSuivHO = new javax.swing.JButton();
        jPanel23 = new javax.swing.JPanel();
        jScrollPane28 = new javax.swing.JScrollPane();
        jLabelHO1 = new javax.swing.JLabel();
        jScrollPane29 = new javax.swing.JScrollPane();
        jLabelHO3 = new javax.swing.JLabel();
        jScrollPane30 = new javax.swing.JScrollPane();
        jLabelHO2 = new javax.swing.JLabel();
        jScrollPaneCarteHO = new java.awt.ScrollPane();
        jPanelSMS = new javax.swing.JPanel();
        jButtonPrecSMS = new javax.swing.JButton();
        jButtonSuivSMS = new javax.swing.JButton();
        jPanel25 = new javax.swing.JPanel();
        jScrollPane33 = new javax.swing.JScrollPane();
        jLabelSMS1 = new javax.swing.JLabel();
        jScrollPane34 = new javax.swing.JScrollPane();
        jLabelSMS3 = new javax.swing.JLabel();
        jScrollPane35 = new javax.swing.JScrollPane();
        jLabelSMS2 = new javax.swing.JLabel();
        jScrollPaneCarteSMS = new java.awt.ScrollPane();
        jPanelCORE = new javax.swing.JPanel();
        jScrollPaneAllBTS2G = new javax.swing.JScrollPane();
        jPanelDATA = new javax.swing.JPanel();
        jPanel49 = new javax.swing.JPanel();
        jScrollPane45 = new javax.swing.JScrollPane();
        jTreeBTS9 = new javax.swing.JTree();
        jScrollPane46 = new javax.swing.JScrollPane();
        jTreeElt9 = new javax.swing.JTree();
        jPanel50 = new javax.swing.JPanel();
        jPanel51 = new javax.swing.JPanel();
        jScrollPane47 = new javax.swing.JScrollPane();
        jScrollPane48 = new javax.swing.JScrollPane();
        jScrollPane49 = new javax.swing.JScrollPane();
        jScrollPane50 = new javax.swing.JScrollPane();
        jPanel52 = new javax.swing.JPanel();
        jPanelRoaming = new javax.swing.JPanel();
        jPanel53 = new javax.swing.JPanel();
        jScrollPane55 = new javax.swing.JScrollPane();
        jTreeBTS10 = new javax.swing.JTree();
        jScrollPane56 = new javax.swing.JScrollPane();
        jTreeElt10 = new javax.swing.JTree();
        jPanel54 = new javax.swing.JPanel();
        jPanel55 = new javax.swing.JPanel();
        jScrollPane51 = new javax.swing.JScrollPane();
        jScrollPane52 = new javax.swing.JScrollPane();
        jScrollPane54 = new javax.swing.JScrollPane();
        jScrollPane53 = new javax.swing.JScrollPane();
        jPanel56 = new javax.swing.JPanel();
        jTabbedPane3G = new javax.swing.JTabbedPane();
        jPanelSynthese2 = new javax.swing.JPanel();
        jLabelSynCdr41 = new javax.swing.JLabel();
        jLabelSynCdr11 = new javax.swing.JLabel();
        jLabelSynCdr71 = new javax.swing.JLabel();
        jLabelSynCdr21 = new javax.swing.JLabel();
        jLabelSynCdr51 = new javax.swing.JLabel();
        jLabelSynCdr81 = new javax.swing.JLabel();
        jLabelSynCarte21 = new javax.swing.JLabel();
        jLabelSynCarte51 = new javax.swing.JLabel();
        jLabelSynCarte81 = new javax.swing.JLabel();
        jLabelSynCarte11 = new javax.swing.JLabel();
        jLabelSynCarte41 = new javax.swing.JLabel();
        jLabelSynCarte71 = new javax.swing.JLabel();
        jLabelSynCdr31 = new javax.swing.JLabel();
        jLabelSynCdr61 = new javax.swing.JLabel();
        jLabelSynCdr91 = new javax.swing.JLabel();
        jLabelSynCarte61 = new javax.swing.JLabel();
        jLabelSynCarte31 = new javax.swing.JLabel();
        jLabelSynCarte91 = new javax.swing.JLabel();
        jPanelTrafic2 = new javax.swing.JPanel();
        jButtonPrecTr2 = new javax.swing.JButton();
        jButtonSuivTr2 = new javax.swing.JButton();
        jPanelTrImage2 = new javax.swing.JPanel();
        jScrollPane19 = new javax.swing.JScrollPane();
        jLabelTr11 = new javax.swing.JLabel();
        jScrollPane20 = new javax.swing.JScrollPane();
        jLabelTr31 = new javax.swing.JLabel();
        jScrollPane41 = new javax.swing.JScrollPane();
        jLabelTr21 = new javax.swing.JLabel();
        jScrollPaneCarteTrafic2 = new java.awt.ScrollPane();
        jPanelCanalTCH2 = new javax.swing.JPanel();
        jButtonPrecAcces2 = new javax.swing.JButton();
        jButtonSuivAcces2 = new javax.swing.JButton();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane42 = new javax.swing.JScrollPane();
        jLabelCanTCH11 = new javax.swing.JLabel();
        jScrollPane43 = new javax.swing.JScrollPane();
        jLabelCanTCH31 = new javax.swing.JLabel();
        jScrollPane44 = new javax.swing.JScrollPane();
        jLabelCanTCH21 = new javax.swing.JLabel();
        jScrollPaneCarteCanTCH2 = new java.awt.ScrollPane();
        jPanelMaintenabilite2 = new javax.swing.JPanel();
        jButtonPrecMaint2 = new javax.swing.JButton();
        jButtonSuivMaint2 = new javax.swing.JButton();
        jPanel27 = new javax.swing.JPanel();
        jScrollPane69 = new javax.swing.JScrollPane();
        jLabelMaint11 = new javax.swing.JLabel();
        jScrollPane70 = new javax.swing.JScrollPane();
        jLabelMaint31 = new javax.swing.JLabel();
        jScrollPane71 = new javax.swing.JScrollPane();
        jLabelMaint21 = new javax.swing.JLabel();
        jScrollPaneCarteMaint2 = new java.awt.ScrollPane();
        jPanelHandover2 = new javax.swing.JPanel();
        jButtonPrecHO2 = new javax.swing.JButton();
        jButtonSuivHO2 = new javax.swing.JButton();
        jPanel28 = new javax.swing.JPanel();
        jScrollPane72 = new javax.swing.JScrollPane();
        jLabelHO11 = new javax.swing.JLabel();
        jScrollPane73 = new javax.swing.JScrollPane();
        jLabelHO31 = new javax.swing.JLabel();
        jScrollPane74 = new javax.swing.JScrollPane();
        jLabelHO21 = new javax.swing.JLabel();
        jScrollPaneCarteHO2 = new java.awt.ScrollPane();
        jPanelSMS2 = new javax.swing.JPanel();
        jButtonPrecSMS2 = new javax.swing.JButton();
        jButtonSuivSMS2 = new javax.swing.JButton();
        jPanel29 = new javax.swing.JPanel();
        jScrollPane75 = new javax.swing.JScrollPane();
        jLabelSMS11 = new javax.swing.JLabel();
        jScrollPane76 = new javax.swing.JScrollPane();
        jLabelSMS31 = new javax.swing.JLabel();
        jScrollPane77 = new javax.swing.JScrollPane();
        jLabelSMS21 = new javax.swing.JLabel();
        jScrollPaneCarteSMS2 = new java.awt.ScrollPane();
        jPanelCORE2 = new javax.swing.JPanel();
        jScrollPaneAllBTS3G = new javax.swing.JScrollPane();
        jPanelDATA2 = new javax.swing.JPanel();
        jPanel66 = new javax.swing.JPanel();
        jScrollPane78 = new javax.swing.JScrollPane();
        jTreeBTS13 = new javax.swing.JTree();
        jScrollPane79 = new javax.swing.JScrollPane();
        jTreeElt13 = new javax.swing.JTree();
        jPanel67 = new javax.swing.JPanel();
        jPanel68 = new javax.swing.JPanel();
        jScrollPane80 = new javax.swing.JScrollPane();
        jScrollPane81 = new javax.swing.JScrollPane();
        jScrollPane82 = new javax.swing.JScrollPane();
        jScrollPane83 = new javax.swing.JScrollPane();
        jPanel69 = new javax.swing.JPanel();
        jPanelRoaming2 = new javax.swing.JPanel();
        jPanel70 = new javax.swing.JPanel();
        jScrollPane84 = new javax.swing.JScrollPane();
        jTreeBTS14 = new javax.swing.JTree();
        jScrollPane85 = new javax.swing.JScrollPane();
        jTreeElt14 = new javax.swing.JTree();
        jPanel71 = new javax.swing.JPanel();
        jPanel72 = new javax.swing.JPanel();
        jScrollPane86 = new javax.swing.JScrollPane();
        jScrollPane87 = new javax.swing.JScrollPane();
        jScrollPane88 = new javax.swing.JScrollPane();
        jScrollPane89 = new javax.swing.JScrollPane();
        jPanel73 = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuFichier = new javax.swing.JMenu();
        jMenuItemDeconnexion = new javax.swing.JMenuItem();
        jMenuItemQuitter = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemAPropos = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("QoS Tracker");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel59.setBackground(new java.awt.Color(228, 228, 238));
        jPanel59.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel59.setAutoscrolls(true);

        jPanel13.setBackground(new java.awt.Color(228, 228, 238));

        jLabel4.setFont(new java.awt.Font("Vijaya", 1, 24));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Audit QoS de :");
        jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jComboOprateur.setFont(new java.awt.Font("Vijaya", 1, 20));
        jComboOprateur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboOprateurActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Vijaya", 1, 20));
        jLabel5.setText("Période du ");

        jdatedebut.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jdatedebut.setToolTipText("date de debut au format yyyy-MM-dd"); // NOI18N
        jdatedebut.setDateFormatString("yyyy-MM-dd"); // NOI18N
        jdatedebut.setFont(new java.awt.Font("Vijaya", 1, 14));

        jLabel6.setFont(new java.awt.Font("Vijaya", 1, 20));
        jLabel6.setText("au");

        jdatefin.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jdatefin.setToolTipText("date de fin au format yyyy-MM-dd"); // NOI18N
        jdatefin.setDateFormatString("yyyy-MM-dd"); // NOI18N
        jdatefin.setFont(new java.awt.Font("Vijaya", 1, 14));

        jButtonValider.setFont(new java.awt.Font("Vijaya", 1, 18));
        jButtonValider.setText("VALIDER");
        jButtonValider.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonValiderActionPerformed(evt);
            }
        });

        jCheckBoxPDF.setBackground(new java.awt.Color(228, 228, 238));
        jCheckBoxPDF.setFont(new java.awt.Font("Tahoma", 1, 14));
        jCheckBoxPDF.setText("Générer le Rapport");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboOprateur, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jdatedebut, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jdatefin, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBoxPDF, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonValider)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButtonValider, javax.swing.GroupLayout.PREFERRED_SIZE, 23, Short.MAX_VALUE)
                                .addComponent(jCheckBoxPDF, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4)
                                .addComponent(jComboOprateur, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(27, 27, 27))
                    .addComponent(jLabel6)
                    .addComponent(jdatefin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jdatedebut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane2G.setBackground(new java.awt.Color(228, 228, 238));
        jTabbedPane2G.setAutoscrolls(true);

        jPanelSynthese.setBackground(new java.awt.Color(228, 228, 238));
        jPanelSynthese.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPanelSyntheseFocusGained(evt);
            }
        });

        jLabelSynCarte2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelSynCarte2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelSynCarte2MouseClicked(evt);
            }
        });

        jLabelSynCarte5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelSynCarte5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelSynCarte5MouseClicked(evt);
            }
        });

        jLabelSynCarte8.setToolTipText("afficher la carte");
        jLabelSynCarte8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelSynCarte8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelSynCarte8MouseClicked(evt);
            }
        });

        jLabelSynCarte4.setToolTipText("afficher la carte");
        jLabelSynCarte4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelSynCarte4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelSynCarte4MouseClicked(evt);
            }
        });

        jLabelSynCarte7.setToolTipText("afficher la carte");
        jLabelSynCarte7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelSynCarte7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelSynCarte7MouseClicked(evt);
            }
        });

        jLabelSynCarte6.setToolTipText("afficher la carte");
        jLabelSynCarte6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelSynCarte6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelSynCarte6MouseClicked(evt);
            }
        });

        jLabelSynCarte3.setToolTipText("Afficher la carte");
        jLabelSynCarte3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelSynCarte3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelSynCarte3MouseClicked(evt);
            }
        });

        jLabelSynCarte9.setToolTipText("afficher la carte");
        jLabelSynCarte9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelSynCarte9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelSynCarte9MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanelSyntheseLayout = new javax.swing.GroupLayout(jPanelSynthese);
        jPanelSynthese.setLayout(jPanelSyntheseLayout);
        jPanelSyntheseLayout.setHorizontalGroup(
            jPanelSyntheseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSyntheseLayout.createSequentialGroup()
                .addGroup(jPanelSyntheseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelSynCdr4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                    .addComponent(jLabelSynCdr1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                    .addComponent(jLabelSynCdr7, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSyntheseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelSynCarte4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addComponent(jLabelSynCarte7, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addComponent(jLabelSynCarte1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSyntheseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelSynCdr8, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addComponent(jLabelSynCdr5, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addComponent(jLabelSynCdr2, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSyntheseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelSynCarte8, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addComponent(jLabelSynCarte2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addComponent(jLabelSynCarte5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSyntheseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelSynCdr3, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                    .addComponent(jLabelSynCdr6, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                    .addComponent(jLabelSynCdr9, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSyntheseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelSynCarte3, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(jLabelSynCarte6, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(jLabelSynCarte9, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelSyntheseLayout.setVerticalGroup(
            jPanelSyntheseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelSyntheseLayout.createSequentialGroup()
                .addGroup(jPanelSyntheseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelSynCarte3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(jLabelSynCdr1, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(jLabelSynCdr2, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(jLabelSynCarte2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(jLabelSynCdr3, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(jLabelSynCarte1, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSyntheseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelSynCarte4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(jLabelSynCdr4, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(jLabelSynCdr6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(jLabelSynCarte5, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(jLabelSynCdr5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(jLabelSynCarte6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSyntheseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelSynCdr7, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                    .addComponent(jLabelSynCdr9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                    .addComponent(jLabelSynCdr8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                    .addComponent(jLabelSynCarte9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                    .addComponent(jLabelSynCarte7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                    .addComponent(jLabelSynCarte8, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane2G.addTab("Synthese", jPanelSynthese);

        jButtonPrecTr.setText("Précedent");
        jButtonPrecTr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrecTrActionPerformed(evt);
            }
        });

        jButtonSuivTr.setText("Suivant");
        jButtonSuivTr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSuivTrActionPerformed(evt);
            }
        });

        jPanelTrImage.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelTr1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane9.setViewportView(jLabelTr1);

        jScrollPane10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelTr3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane10.setViewportView(jLabelTr3);

        jScrollPane11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelTr2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane11.setViewportView(jLabelTr2);

        javax.swing.GroupLayout jPanelTrImageLayout = new javax.swing.GroupLayout(jPanelTrImage);
        jPanelTrImage.setLayout(jPanelTrImageLayout);
        jPanelTrImageLayout.setHorizontalGroup(
            jPanelTrImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
            .addComponent(jScrollPane11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
            .addComponent(jScrollPane9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
        );
        jPanelTrImageLayout.setVerticalGroup(
            jPanelTrImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTrImageLayout.createSequentialGroup()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelTraficLayout = new javax.swing.GroupLayout(jPanelTrafic);
        jPanelTrafic.setLayout(jPanelTraficLayout);
        jPanelTraficLayout.setHorizontalGroup(
            jPanelTraficLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTraficLayout.createSequentialGroup()
                .addGroup(jPanelTraficLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelTraficLayout.createSequentialGroup()
                        .addGap(323, 323, 323)
                        .addComponent(jButtonPrecTr, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonSuivTr, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelTraficLayout.createSequentialGroup()
                        .addComponent(jPanelTrImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPaneCarteTrafic, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelTraficLayout.setVerticalGroup(
            jPanelTraficLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTraficLayout.createSequentialGroup()
                .addGroup(jPanelTraficLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonPrecTr)
                    .addComponent(jButtonSuivTr))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelTraficLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneCarteTrafic, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
                    .addComponent(jPanelTrImage, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane2G.addTab("Trafic", jPanelTrafic);

        jButtonPrecAcces.setText("Précedent");
        jButtonPrecAcces.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrecAccesActionPerformed(evt);
            }
        });

        jButtonSuivAcces.setText("Suivant");
        jButtonSuivAcces.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSuivAccesActionPerformed(evt);
            }
        });

        jPanel17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelCanTCH1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane13.setViewportView(jLabelCanTCH1);

        jScrollPane14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelCanTCH3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane14.setViewportView(jLabelCanTCH3);

        jScrollPane21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelCanTCH2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane21.setViewportView(jLabelCanTCH2);

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 810, Short.MAX_VALUE)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 809, Short.MAX_VALUE)
                    .addComponent(jScrollPane21, javax.swing.GroupLayout.DEFAULT_SIZE, 809, Short.MAX_VALUE)))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane21, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelCanalTCHLayout = new javax.swing.GroupLayout(jPanelCanalTCH);
        jPanelCanalTCH.setLayout(jPanelCanalTCHLayout);
        jPanelCanalTCHLayout.setHorizontalGroup(
            jPanelCanalTCHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCanalTCHLayout.createSequentialGroup()
                .addGroup(jPanelCanalTCHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCanalTCHLayout.createSequentialGroup()
                        .addGap(317, 317, 317)
                        .addComponent(jButtonPrecAcces, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonSuivAcces, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelCanalTCHLayout.createSequentialGroup()
                        .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPaneCarteCanTCH, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelCanalTCHLayout.setVerticalGroup(
            jPanelCanalTCHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCanalTCHLayout.createSequentialGroup()
                .addGroup(jPanelCanalTCHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSuivAcces)
                    .addComponent(jButtonPrecAcces, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCanalTCHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneCarteCanTCH, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
                    .addComponent(jPanel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane2G.addTab("Accessibilité- Signalisation", jPanelCanalTCH);

        jButtonPrecMaint.setText("Précedent");
        jButtonPrecMaint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrecMaintActionPerformed(evt);
            }
        });

        jButtonSuivMaint.setText("Suivant");
        jButtonSuivMaint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSuivMaintActionPerformed(evt);
            }
        });

        jPanel21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane23.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelMaint1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane23.setViewportView(jLabelMaint1);

        jScrollPane24.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelMaint3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane24.setViewportView(jLabelMaint3);

        jScrollPane25.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelMaint2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane25.setViewportView(jLabelMaint2);

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane24, javax.swing.GroupLayout.DEFAULT_SIZE, 831, Short.MAX_VALUE)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane23, javax.swing.GroupLayout.DEFAULT_SIZE, 830, Short.MAX_VALUE)
                    .addComponent(jScrollPane25, javax.swing.GroupLayout.DEFAULT_SIZE, 830, Short.MAX_VALUE)))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addComponent(jScrollPane23, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane25, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane24, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelMaintenabiliteLayout = new javax.swing.GroupLayout(jPanelMaintenabilite);
        jPanelMaintenabilite.setLayout(jPanelMaintenabiliteLayout);
        jPanelMaintenabiliteLayout.setHorizontalGroup(
            jPanelMaintenabiliteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMaintenabiliteLayout.createSequentialGroup()
                .addGroup(jPanelMaintenabiliteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelMaintenabiliteLayout.createSequentialGroup()
                        .addGap(388, 388, 388)
                        .addComponent(jButtonPrecMaint, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonSuivMaint, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelMaintenabiliteLayout.createSequentialGroup()
                        .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPaneCarteMaint, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelMaintenabiliteLayout.setVerticalGroup(
            jPanelMaintenabiliteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMaintenabiliteLayout.createSequentialGroup()
                .addGroup(jPanelMaintenabiliteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonPrecMaint)
                    .addComponent(jButtonSuivMaint))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelMaintenabiliteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneCarteMaint, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
                    .addComponent(jPanel21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane2G.addTab("Maintenabilité", jPanelMaintenabilite);

        jButtonPrecHO.setText("Précédent");
        jButtonPrecHO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrecHOActionPerformed(evt);
            }
        });

        jButtonSuivHO.setText("Suivant");
        jButtonSuivHO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSuivHOActionPerformed(evt);
            }
        });

        jPanel23.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane28.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelHO1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane28.setViewportView(jLabelHO1);

        jScrollPane29.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelHO3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane29.setViewportView(jLabelHO3);

        jScrollPane30.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelHO2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane30.setViewportView(jLabelHO2);

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane29, javax.swing.GroupLayout.DEFAULT_SIZE, 801, Short.MAX_VALUE)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane28, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
                    .addComponent(jScrollPane30, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addComponent(jScrollPane28, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane30, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane29, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelHandoverLayout = new javax.swing.GroupLayout(jPanelHandover);
        jPanelHandover.setLayout(jPanelHandoverLayout);
        jPanelHandoverLayout.setHorizontalGroup(
            jPanelHandoverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHandoverLayout.createSequentialGroup()
                .addGroup(jPanelHandoverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelHandoverLayout.createSequentialGroup()
                        .addGap(375, 375, 375)
                        .addComponent(jButtonPrecHO, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSuivHO, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelHandoverLayout.createSequentialGroup()
                        .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPaneCarteHO, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelHandoverLayout.setVerticalGroup(
            jPanelHandoverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHandoverLayout.createSequentialGroup()
                .addGroup(jPanelHandoverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSuivHO)
                    .addComponent(jButtonPrecHO))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelHandoverLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneCarteHO, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
                    .addComponent(jPanel23, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane2G.addTab("Handover", jPanelHandover);

        jButtonPrecSMS.setText("Précédent");
        jButtonPrecSMS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrecSMSActionPerformed(evt);
            }
        });

        jButtonSuivSMS.setText("Suivant");
        jButtonSuivSMS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSuivSMSActionPerformed(evt);
            }
        });

        jPanel25.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane33.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelSMS1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane33.setViewportView(jLabelSMS1);

        jScrollPane34.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelSMS3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane34.setViewportView(jLabelSMS3);

        jScrollPane35.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelSMS2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane35.setViewportView(jLabelSMS2);

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane34, javax.swing.GroupLayout.DEFAULT_SIZE, 849, Short.MAX_VALUE)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane33, javax.swing.GroupLayout.DEFAULT_SIZE, 848, Short.MAX_VALUE)
                    .addComponent(jScrollPane35, javax.swing.GroupLayout.DEFAULT_SIZE, 848, Short.MAX_VALUE)))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addComponent(jScrollPane33, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane35, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane34, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelSMSLayout = new javax.swing.GroupLayout(jPanelSMS);
        jPanelSMS.setLayout(jPanelSMSLayout);
        jPanelSMSLayout.setHorizontalGroup(
            jPanelSMSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSMSLayout.createSequentialGroup()
                .addGroup(jPanelSMSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelSMSLayout.createSequentialGroup()
                        .addGap(381, 381, 381)
                        .addComponent(jButtonPrecSMS, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSuivSMS, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelSMSLayout.createSequentialGroup()
                        .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPaneCarteSMS, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelSMSLayout.setVerticalGroup(
            jPanelSMSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelSMSLayout.createSequentialGroup()
                .addGroup(jPanelSMSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSuivSMS)
                    .addComponent(jButtonPrecSMS))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSMSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPaneCarteSMS, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
                    .addComponent(jPanel25, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane2G.addTab("SMS", jPanelSMS);

        javax.swing.GroupLayout jPanelCORELayout = new javax.swing.GroupLayout(jPanelCORE);
        jPanelCORE.setLayout(jPanelCORELayout);
        jPanelCORELayout.setHorizontalGroup(
            jPanelCORELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCORELayout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(jScrollPaneAllBTS2G, javax.swing.GroupLayout.DEFAULT_SIZE, 894, Short.MAX_VALUE)
                .addGap(172, 172, 172))
        );
        jPanelCORELayout.setVerticalGroup(
            jPanelCORELayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCORELayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jScrollPaneAllBTS2G, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE)
                .addGap(66, 66, 66))
        );

        jTabbedPane2G.addTab("Carte globale du réseau", jPanelCORE);

        jPanel49.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTreeBTS9.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane45.setViewportView(jTreeBTS9);

        treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTreeElt9.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane46.setViewportView(jTreeElt9);

        javax.swing.GroupLayout jPanel49Layout = new javax.swing.GroupLayout(jPanel49);
        jPanel49.setLayout(jPanel49Layout);
        jPanel49Layout.setHorizontalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel49Layout.createSequentialGroup()
                .addComponent(jScrollPane45, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane46, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel49Layout.setVerticalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane46, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
            .addComponent(jScrollPane45, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
        );

        jPanel50.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel50Layout = new javax.swing.GroupLayout(jPanel50);
        jPanel50.setLayout(jPanel50Layout);
        jPanel50Layout.setHorizontalGroup(
            jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 291, Short.MAX_VALUE)
        );
        jPanel50Layout.setVerticalGroup(
            jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 47, Short.MAX_VALUE)
        );

        jPanel51.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane47.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane48.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane49.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane50.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel51Layout = new javax.swing.GroupLayout(jPanel51);
        jPanel51.setLayout(jPanel51Layout);
        jPanel51Layout.setHorizontalGroup(
            jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane47, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
            .addComponent(jScrollPane49, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
            .addComponent(jScrollPane48, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
            .addComponent(jScrollPane50, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
        );
        jPanel51Layout.setVerticalGroup(
            jPanel51Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel51Layout.createSequentialGroup()
                .addComponent(jScrollPane47, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane48, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane49, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane50, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
        );

        jPanel52.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel52Layout = new javax.swing.GroupLayout(jPanel52);
        jPanel52.setLayout(jPanel52Layout);
        jPanel52Layout.setHorizontalGroup(
            jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 403, Short.MAX_VALUE)
        );
        jPanel52Layout.setVerticalGroup(
            jPanel52Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 515, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanelDATALayout = new javax.swing.GroupLayout(jPanelDATA);
        jPanelDATA.setLayout(jPanelDATALayout);
        jPanelDATALayout.setHorizontalGroup(
            jPanelDATALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDATALayout.createSequentialGroup()
                .addGroup(jPanelDATALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel49, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel50, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel51, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelDATALayout.setVerticalGroup(
            jPanelDATALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDATALayout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addGroup(jPanelDATALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelDATALayout.createSequentialGroup()
                        .addComponent(jPanel49, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel50, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel51, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jTabbedPane2G.addTab("DATA", jPanelDATA);

        jPanel53.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTreeBTS10.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane55.setViewportView(jTreeBTS10);

        treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTreeElt10.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane56.setViewportView(jTreeElt10);

        javax.swing.GroupLayout jPanel53Layout = new javax.swing.GroupLayout(jPanel53);
        jPanel53.setLayout(jPanel53Layout);
        jPanel53Layout.setHorizontalGroup(
            jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel53Layout.createSequentialGroup()
                .addComponent(jScrollPane55, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane56, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel53Layout.setVerticalGroup(
            jPanel53Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane56, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
            .addComponent(jScrollPane55, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
        );

        jPanel54.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel54Layout = new javax.swing.GroupLayout(jPanel54);
        jPanel54.setLayout(jPanel54Layout);
        jPanel54Layout.setHorizontalGroup(
            jPanel54Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 298, Short.MAX_VALUE)
        );
        jPanel54Layout.setVerticalGroup(
            jPanel54Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 53, Short.MAX_VALUE)
        );

        jPanel55.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane51.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane52.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane54.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane53.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel55Layout = new javax.swing.GroupLayout(jPanel55);
        jPanel55.setLayout(jPanel55Layout);
        jPanel55Layout.setHorizontalGroup(
            jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane51, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
            .addComponent(jScrollPane54, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
            .addComponent(jScrollPane53, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
            .addComponent(jScrollPane52, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
        );
        jPanel55Layout.setVerticalGroup(
            jPanel55Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel55Layout.createSequentialGroup()
                .addComponent(jScrollPane51, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane52, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane53, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane54, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel56.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel56Layout = new javax.swing.GroupLayout(jPanel56);
        jPanel56.setLayout(jPanel56Layout);
        jPanel56Layout.setHorizontalGroup(
            jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 399, Short.MAX_VALUE)
        );
        jPanel56Layout.setVerticalGroup(
            jPanel56Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 504, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanelRoamingLayout = new javax.swing.GroupLayout(jPanelRoaming);
        jPanelRoaming.setLayout(jPanelRoamingLayout);
        jPanelRoamingLayout.setHorizontalGroup(
            jPanelRoamingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRoamingLayout.createSequentialGroup()
                .addGroup(jPanelRoamingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel53, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel54, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel55, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel56, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelRoamingLayout.setVerticalGroup(
            jPanelRoamingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRoamingLayout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addGroup(jPanelRoamingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelRoamingLayout.createSequentialGroup()
                        .addComponent(jPanel53, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel54, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel55, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel56, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane2G.addTab("Roaming", jPanelRoaming);

        jTabbedPanePrincipale.addTab("2G", jTabbedPane2G);

        jTabbedPane3G.setBackground(new java.awt.Color(228, 228, 238));
        jTabbedPane3G.setAutoscrolls(true);

        jPanelSynthese2.setBackground(new java.awt.Color(228, 228, 238));
        jPanelSynthese2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPanelSynthese2FocusGained(evt);
            }
        });

        jLabelSynCarte21.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelSynCarte21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelSynCarte21MouseClicked(evt);
            }
        });

        jLabelSynCarte51.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelSynCarte51.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelSynCarte51MouseClicked(evt);
            }
        });

        jLabelSynCarte81.setToolTipText("afficher la carte");
        jLabelSynCarte81.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelSynCarte81.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelSynCarte81MouseClicked(evt);
            }
        });

        jLabelSynCarte41.setToolTipText("afficher la carte");
        jLabelSynCarte41.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelSynCarte41.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelSynCarte41MouseClicked(evt);
            }
        });

        jLabelSynCarte71.setToolTipText("afficher la carte");
        jLabelSynCarte71.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelSynCarte71.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelSynCarte71MouseClicked(evt);
            }
        });

        jLabelSynCarte61.setToolTipText("afficher la carte");
        jLabelSynCarte61.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelSynCarte61.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelSynCarte61MouseClicked(evt);
            }
        });

        jLabelSynCarte31.setToolTipText("Afficher la carte");
        jLabelSynCarte31.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelSynCarte31.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelSynCarte31MouseClicked(evt);
            }
        });

        jLabelSynCarte91.setToolTipText("afficher la carte");
        jLabelSynCarte91.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabelSynCarte91.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelSynCarte91MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanelSynthese2Layout = new javax.swing.GroupLayout(jPanelSynthese2);
        jPanelSynthese2.setLayout(jPanelSynthese2Layout);
        jPanelSynthese2Layout.setHorizontalGroup(
            jPanelSynthese2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSynthese2Layout.createSequentialGroup()
                .addGroup(jPanelSynthese2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelSynCdr41, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                    .addComponent(jLabelSynCdr11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                    .addComponent(jLabelSynCdr71, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSynthese2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelSynCarte41, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addComponent(jLabelSynCarte71, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addComponent(jLabelSynCarte11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSynthese2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelSynCdr81, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addComponent(jLabelSynCdr51, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addComponent(jLabelSynCdr21, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSynthese2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelSynCarte81, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addComponent(jLabelSynCarte21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                    .addComponent(jLabelSynCarte51, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSynthese2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelSynCdr31, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                    .addComponent(jLabelSynCdr61, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                    .addComponent(jLabelSynCdr91, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSynthese2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelSynCarte31, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(jLabelSynCarte61, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(jLabelSynCarte91, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelSynthese2Layout.setVerticalGroup(
            jPanelSynthese2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelSynthese2Layout.createSequentialGroup()
                .addGroup(jPanelSynthese2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelSynCarte31, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(jLabelSynCdr11, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(jLabelSynCdr21, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(jLabelSynCarte21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(jLabelSynCdr31, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(jLabelSynCarte11, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSynthese2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelSynCarte41, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(jLabelSynCdr41, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(jLabelSynCdr61, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(jLabelSynCarte51, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(jLabelSynCdr51, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(jLabelSynCarte61, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSynthese2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelSynCdr71, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                    .addComponent(jLabelSynCdr91, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                    .addComponent(jLabelSynCdr81, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                    .addComponent(jLabelSynCarte91, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                    .addComponent(jLabelSynCarte71, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                    .addComponent(jLabelSynCarte81, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane3G.addTab("Synthese", jPanelSynthese2);

        jButtonPrecTr2.setText("Précedent");
        jButtonPrecTr2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrecTr2ActionPerformed(evt);
            }
        });

        jButtonSuivTr2.setText("Suivant");
        jButtonSuivTr2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSuivTr2ActionPerformed(evt);
            }
        });

        jPanelTrImage2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelTr11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane19.setViewportView(jLabelTr11);

        jScrollPane20.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelTr31.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane20.setViewportView(jLabelTr31);

        jScrollPane41.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelTr21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane41.setViewportView(jLabelTr21);

        javax.swing.GroupLayout jPanelTrImage2Layout = new javax.swing.GroupLayout(jPanelTrImage2);
        jPanelTrImage2.setLayout(jPanelTrImage2Layout);
        jPanelTrImage2Layout.setHorizontalGroup(
            jPanelTrImage2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane20, javax.swing.GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
            .addComponent(jScrollPane41, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
            .addComponent(jScrollPane19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
        );
        jPanelTrImage2Layout.setVerticalGroup(
            jPanelTrImage2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTrImage2Layout.createSequentialGroup()
                .addComponent(jScrollPane19, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane41, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane20, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelTrafic2Layout = new javax.swing.GroupLayout(jPanelTrafic2);
        jPanelTrafic2.setLayout(jPanelTrafic2Layout);
        jPanelTrafic2Layout.setHorizontalGroup(
            jPanelTrafic2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTrafic2Layout.createSequentialGroup()
                .addGroup(jPanelTrafic2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelTrafic2Layout.createSequentialGroup()
                        .addGap(323, 323, 323)
                        .addComponent(jButtonPrecTr2, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonSuivTr2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelTrafic2Layout.createSequentialGroup()
                        .addComponent(jPanelTrImage2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPaneCarteTrafic2, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelTrafic2Layout.setVerticalGroup(
            jPanelTrafic2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTrafic2Layout.createSequentialGroup()
                .addGroup(jPanelTrafic2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonPrecTr2)
                    .addComponent(jButtonSuivTr2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelTrafic2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneCarteTrafic2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
                    .addComponent(jPanelTrImage2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane3G.addTab("Trafic", jPanelTrafic2);

        jButtonPrecAcces2.setText("Précedent");
        jButtonPrecAcces2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrecAcces2ActionPerformed(evt);
            }
        });

        jButtonSuivAcces2.setText("Suivant");
        jButtonSuivAcces2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSuivAcces2ActionPerformed(evt);
            }
        });

        jPanel19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane42.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelCanTCH11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane42.setViewportView(jLabelCanTCH11);

        jScrollPane43.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelCanTCH31.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane43.setViewportView(jLabelCanTCH31);

        jScrollPane44.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelCanTCH21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane44.setViewportView(jLabelCanTCH21);

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane43, javax.swing.GroupLayout.DEFAULT_SIZE, 810, Short.MAX_VALUE)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane42, javax.swing.GroupLayout.DEFAULT_SIZE, 809, Short.MAX_VALUE)
                    .addComponent(jScrollPane44, javax.swing.GroupLayout.DEFAULT_SIZE, 809, Short.MAX_VALUE)))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addComponent(jScrollPane42, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane44, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane43, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelCanalTCH2Layout = new javax.swing.GroupLayout(jPanelCanalTCH2);
        jPanelCanalTCH2.setLayout(jPanelCanalTCH2Layout);
        jPanelCanalTCH2Layout.setHorizontalGroup(
            jPanelCanalTCH2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCanalTCH2Layout.createSequentialGroup()
                .addGroup(jPanelCanalTCH2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCanalTCH2Layout.createSequentialGroup()
                        .addGap(317, 317, 317)
                        .addComponent(jButtonPrecAcces2, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonSuivAcces2, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelCanalTCH2Layout.createSequentialGroup()
                        .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPaneCarteCanTCH2, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelCanalTCH2Layout.setVerticalGroup(
            jPanelCanalTCH2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCanalTCH2Layout.createSequentialGroup()
                .addGroup(jPanelCanalTCH2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSuivAcces2)
                    .addComponent(jButtonPrecAcces2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCanalTCH2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneCarteCanTCH2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
                    .addComponent(jPanel19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane3G.addTab("Accessibilité- Signalisation", jPanelCanalTCH2);

        jButtonPrecMaint2.setText("Précedent");
        jButtonPrecMaint2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrecMaint2ActionPerformed(evt);
            }
        });

        jButtonSuivMaint2.setText("Suivant");
        jButtonSuivMaint2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSuivMaint2ActionPerformed(evt);
            }
        });

        jPanel27.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane69.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelMaint11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane69.setViewportView(jLabelMaint11);

        jScrollPane70.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelMaint31.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane70.setViewportView(jLabelMaint31);

        jScrollPane71.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelMaint21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane71.setViewportView(jLabelMaint21);

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane70, javax.swing.GroupLayout.DEFAULT_SIZE, 831, Short.MAX_VALUE)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane69, javax.swing.GroupLayout.DEFAULT_SIZE, 830, Short.MAX_VALUE)
                    .addComponent(jScrollPane71, javax.swing.GroupLayout.DEFAULT_SIZE, 830, Short.MAX_VALUE)))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addComponent(jScrollPane69, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane71, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane70, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelMaintenabilite2Layout = new javax.swing.GroupLayout(jPanelMaintenabilite2);
        jPanelMaintenabilite2.setLayout(jPanelMaintenabilite2Layout);
        jPanelMaintenabilite2Layout.setHorizontalGroup(
            jPanelMaintenabilite2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMaintenabilite2Layout.createSequentialGroup()
                .addGroup(jPanelMaintenabilite2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelMaintenabilite2Layout.createSequentialGroup()
                        .addGap(388, 388, 388)
                        .addComponent(jButtonPrecMaint2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonSuivMaint2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelMaintenabilite2Layout.createSequentialGroup()
                        .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPaneCarteMaint2, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelMaintenabilite2Layout.setVerticalGroup(
            jPanelMaintenabilite2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMaintenabilite2Layout.createSequentialGroup()
                .addGroup(jPanelMaintenabilite2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonPrecMaint2)
                    .addComponent(jButtonSuivMaint2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelMaintenabilite2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneCarteMaint2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
                    .addComponent(jPanel27, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane3G.addTab("Maintenabilité", jPanelMaintenabilite2);

        jButtonPrecHO2.setText("Précédent");
        jButtonPrecHO2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrecHO2ActionPerformed(evt);
            }
        });

        jButtonSuivHO2.setText("Suivant");
        jButtonSuivHO2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSuivHO2ActionPerformed(evt);
            }
        });

        jPanel28.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane72.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelHO11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane72.setViewportView(jLabelHO11);

        jScrollPane73.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelHO31.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane73.setViewportView(jLabelHO31);

        jScrollPane74.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelHO21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane74.setViewportView(jLabelHO21);

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane73, javax.swing.GroupLayout.DEFAULT_SIZE, 801, Short.MAX_VALUE)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane72, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
                    .addComponent(jScrollPane74, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addComponent(jScrollPane72, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane74, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane73, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelHandover2Layout = new javax.swing.GroupLayout(jPanelHandover2);
        jPanelHandover2.setLayout(jPanelHandover2Layout);
        jPanelHandover2Layout.setHorizontalGroup(
            jPanelHandover2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHandover2Layout.createSequentialGroup()
                .addGroup(jPanelHandover2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelHandover2Layout.createSequentialGroup()
                        .addGap(375, 375, 375)
                        .addComponent(jButtonPrecHO2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSuivHO2, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelHandover2Layout.createSequentialGroup()
                        .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPaneCarteHO2, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelHandover2Layout.setVerticalGroup(
            jPanelHandover2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHandover2Layout.createSequentialGroup()
                .addGroup(jPanelHandover2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSuivHO2)
                    .addComponent(jButtonPrecHO2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelHandover2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneCarteHO2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
                    .addComponent(jPanel28, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane3G.addTab("Handover", jPanelHandover2);

        jButtonPrecSMS2.setText("Précédent");
        jButtonPrecSMS2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrecSMS2ActionPerformed(evt);
            }
        });

        jButtonSuivSMS2.setText("Suivant");
        jButtonSuivSMS2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSuivSMS2ActionPerformed(evt);
            }
        });

        jPanel29.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane75.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelSMS11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane75.setViewportView(jLabelSMS11);

        jScrollPane76.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelSMS31.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane76.setViewportView(jLabelSMS31);

        jScrollPane77.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelSMS21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jScrollPane77.setViewportView(jLabelSMS21);

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane76, javax.swing.GroupLayout.DEFAULT_SIZE, 849, Short.MAX_VALUE)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane75, javax.swing.GroupLayout.DEFAULT_SIZE, 848, Short.MAX_VALUE)
                    .addComponent(jScrollPane77, javax.swing.GroupLayout.DEFAULT_SIZE, 848, Short.MAX_VALUE)))
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addComponent(jScrollPane75, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane77, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane76, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelSMS2Layout = new javax.swing.GroupLayout(jPanelSMS2);
        jPanelSMS2.setLayout(jPanelSMS2Layout);
        jPanelSMS2Layout.setHorizontalGroup(
            jPanelSMS2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSMS2Layout.createSequentialGroup()
                .addGroup(jPanelSMS2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelSMS2Layout.createSequentialGroup()
                        .addGap(381, 381, 381)
                        .addComponent(jButtonPrecSMS2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSuivSMS2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelSMS2Layout.createSequentialGroup()
                        .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPaneCarteSMS2, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelSMS2Layout.setVerticalGroup(
            jPanelSMS2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelSMS2Layout.createSequentialGroup()
                .addGroup(jPanelSMS2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSuivSMS2)
                    .addComponent(jButtonPrecSMS2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSMS2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPaneCarteSMS2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
                    .addComponent(jPanel29, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane3G.addTab("SMS", jPanelSMS2);

        javax.swing.GroupLayout jPanelCORE2Layout = new javax.swing.GroupLayout(jPanelCORE2);
        jPanelCORE2.setLayout(jPanelCORE2Layout);
        jPanelCORE2Layout.setHorizontalGroup(
            jPanelCORE2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCORE2Layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(jScrollPaneAllBTS3G, javax.swing.GroupLayout.DEFAULT_SIZE, 894, Short.MAX_VALUE)
                .addGap(172, 172, 172))
        );
        jPanelCORE2Layout.setVerticalGroup(
            jPanelCORE2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCORE2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jScrollPaneAllBTS3G, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE)
                .addGap(66, 66, 66))
        );

        jTabbedPane3G.addTab("Carte globale du réseau", jPanelCORE2);

        jPanel66.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTreeBTS13.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane78.setViewportView(jTreeBTS13);

        treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTreeElt13.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane79.setViewportView(jTreeElt13);

        javax.swing.GroupLayout jPanel66Layout = new javax.swing.GroupLayout(jPanel66);
        jPanel66.setLayout(jPanel66Layout);
        jPanel66Layout.setHorizontalGroup(
            jPanel66Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel66Layout.createSequentialGroup()
                .addComponent(jScrollPane78, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane79, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel66Layout.setVerticalGroup(
            jPanel66Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane79, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
            .addComponent(jScrollPane78, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
        );

        jPanel67.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel67Layout = new javax.swing.GroupLayout(jPanel67);
        jPanel67.setLayout(jPanel67Layout);
        jPanel67Layout.setHorizontalGroup(
            jPanel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 291, Short.MAX_VALUE)
        );
        jPanel67Layout.setVerticalGroup(
            jPanel67Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 47, Short.MAX_VALUE)
        );

        jPanel68.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane80.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane81.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane82.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane83.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel68Layout = new javax.swing.GroupLayout(jPanel68);
        jPanel68.setLayout(jPanel68Layout);
        jPanel68Layout.setHorizontalGroup(
            jPanel68Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane80, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
            .addComponent(jScrollPane82, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
            .addComponent(jScrollPane81, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
            .addComponent(jScrollPane83, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
        );
        jPanel68Layout.setVerticalGroup(
            jPanel68Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel68Layout.createSequentialGroup()
                .addComponent(jScrollPane80, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane81, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane82, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane83, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
        );

        jPanel69.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel69Layout = new javax.swing.GroupLayout(jPanel69);
        jPanel69.setLayout(jPanel69Layout);
        jPanel69Layout.setHorizontalGroup(
            jPanel69Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 403, Short.MAX_VALUE)
        );
        jPanel69Layout.setVerticalGroup(
            jPanel69Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 515, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanelDATA2Layout = new javax.swing.GroupLayout(jPanelDATA2);
        jPanelDATA2.setLayout(jPanelDATA2Layout);
        jPanelDATA2Layout.setHorizontalGroup(
            jPanelDATA2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDATA2Layout.createSequentialGroup()
                .addGroup(jPanelDATA2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel66, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel67, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel68, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel69, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelDATA2Layout.setVerticalGroup(
            jPanelDATA2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDATA2Layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addGroup(jPanelDATA2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel69, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelDATA2Layout.createSequentialGroup()
                        .addComponent(jPanel66, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel67, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel68, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jTabbedPane3G.addTab("DATA", jPanelDATA2);

        jPanel70.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTreeBTS14.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane84.setViewportView(jTreeBTS14);

        treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        jTreeElt14.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane85.setViewportView(jTreeElt14);

        javax.swing.GroupLayout jPanel70Layout = new javax.swing.GroupLayout(jPanel70);
        jPanel70.setLayout(jPanel70Layout);
        jPanel70Layout.setHorizontalGroup(
            jPanel70Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel70Layout.createSequentialGroup()
                .addComponent(jScrollPane84, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane85, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel70Layout.setVerticalGroup(
            jPanel70Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane85, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
            .addComponent(jScrollPane84, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
        );

        jPanel71.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel71Layout = new javax.swing.GroupLayout(jPanel71);
        jPanel71.setLayout(jPanel71Layout);
        jPanel71Layout.setHorizontalGroup(
            jPanel71Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 298, Short.MAX_VALUE)
        );
        jPanel71Layout.setVerticalGroup(
            jPanel71Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 53, Short.MAX_VALUE)
        );

        jPanel72.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane86.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane87.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane88.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane89.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel72Layout = new javax.swing.GroupLayout(jPanel72);
        jPanel72.setLayout(jPanel72Layout);
        jPanel72Layout.setHorizontalGroup(
            jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane86, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
            .addComponent(jScrollPane88, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
            .addComponent(jScrollPane89, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
            .addComponent(jScrollPane87, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
        );
        jPanel72Layout.setVerticalGroup(
            jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel72Layout.createSequentialGroup()
                .addComponent(jScrollPane86, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane87, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane89, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane88, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel73.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel73Layout = new javax.swing.GroupLayout(jPanel73);
        jPanel73.setLayout(jPanel73Layout);
        jPanel73Layout.setHorizontalGroup(
            jPanel73Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 399, Short.MAX_VALUE)
        );
        jPanel73Layout.setVerticalGroup(
            jPanel73Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 504, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanelRoaming2Layout = new javax.swing.GroupLayout(jPanelRoaming2);
        jPanelRoaming2.setLayout(jPanelRoaming2Layout);
        jPanelRoaming2Layout.setHorizontalGroup(
            jPanelRoaming2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRoaming2Layout.createSequentialGroup()
                .addGroup(jPanelRoaming2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel70, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel71, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel72, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel73, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelRoaming2Layout.setVerticalGroup(
            jPanelRoaming2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRoaming2Layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addGroup(jPanelRoaming2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelRoaming2Layout.createSequentialGroup()
                        .addComponent(jPanel70, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel71, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel72, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel73, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane3G.addTab("Roaming", jPanelRoaming2);

        jTabbedPanePrincipale.addTab("3G", jTabbedPane3G);

        javax.swing.GroupLayout jPanel59Layout = new javax.swing.GroupLayout(jPanel59);
        jPanel59.setLayout(jPanel59Layout);
        jPanel59Layout.setHorizontalGroup(
            jPanel59Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel59Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel59Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPanePrincipale, javax.swing.GroupLayout.DEFAULT_SIZE, 1134, Short.MAX_VALUE)
                    .addGroup(jPanel59Layout.createSequentialGroup()
                        .addComponent(jLabelLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel59Layout.setVerticalGroup(
            jPanel59Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel59Layout.createSequentialGroup()
                .addGroup(jPanel59Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel59Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jTabbedPanePrincipale, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)
                .addGap(11, 11, 11))
        );

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(projet_consultation.Projet_ConsultationApp.class).getContext().getActionMap(Interface_Principale.class, this);
        jMenuFichier.setAction(actionMap.get("deconnexion")); // NOI18N
        jMenuFichier.setText("Fichier");

        jMenuItemDeconnexion.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemDeconnexion.setText("Deconnexion");
        jMenuItemDeconnexion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDeconnexionActionPerformed(evt);
            }
        });
        jMenuFichier.add(jMenuItemDeconnexion);

        jMenuItemQuitter.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemQuitter.setText("Quitter");
        jMenuItemQuitter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemQuitterActionPerformed(evt);
            }
        });
        jMenuFichier.add(jMenuItemQuitter);

        jMenuBar1.add(jMenuFichier);

        jMenu2.setText("Aide");

        jMenuItemAPropos.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, java.awt.event.InputEvent.SHIFT_MASK));
        jMenuItemAPropos.setText("A Propos");
        jMenuItemAPropos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAProposActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItemAPropos);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel59, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel59, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void jButtonValiderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonValiderActionPerformed
        // TODO add your handling code here:
        boolean test=false;
        jButtonValider.setEnabled(false);
        jComboOprateur.setEnabled(false);
        operateurCalcule=null;
        boolean ImprimeRapport=false;
        String dateD="";
        String dateF="";
        try
        {
            jPanelTrImage.setSize(dRect.width+50, dRect.height*3+10);
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal=Calendar.getInstance();
            //String d = dateformat.format(cal.getTime());

            cal.add(Calendar.MONTH,-12);
            Date annee_prec=cal.getTime();

            cal=Calendar.getInstance();
            cal.add(Calendar.DATE,-1);
            Date jour_prec=cal.getTime();

            Calendar cal_debut=Calendar.getInstance();
            Calendar cal_fin=Calendar.getInstance();
            cal_debut.setTime(jdatedebut.getDate());
            cal_fin.setTime(jdatefin.getDate());

            if(cal_debut.before(cal_fin))
            {
                Calendar cal_annee_prec=Calendar.getInstance();
                cal_annee_prec.setTime(annee_prec);
                Calendar cal_jour_prec=Calendar.getInstance();
                cal_jour_prec.setTime(jour_prec);
                if(cal_debut.after(cal_annee_prec) && cal_debut.before(cal_jour_prec) && cal_fin.after(cal_annee_prec) && cal_fin.before(cal_jour_prec))
                {
                    SetImageDefautOngletSynthese();
                    setImageOngletAccessibiliteDefault();
                    setImageOngletHandoverDefault();
                    setImageOngletMaintenabiliteDefault();
                    setImageOngletSMSDefault();
                    setImageOngletTraficDefaut();
                    ImprimeRapport=false;
                    CalculFini=false;
                    dateD=dateformat.format(cal_debut.getTime());
                    dateF=dateformat.format(cal_fin.getTime());
                    System.out.println("date debut:"+dateD);
                    System.out.println("date Fin:"+dateF);
                    test=true;
                } else
                {
                    test=false;
                    JOptionPane.showMessageDialog(this, "Echec ...Veuillez verifier les dates","Erreur",JOptionPane.INFORMATION_MESSAGE);
                }
            } else
            {
                test=false;
                JOptionPane.showMessageDialog(this, "Echec ...Veuillez verifier les dates ");
            }
        } catch (Exception ex)
        {
            test=false;
            //Logger.getLogger(Interface_Principale.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Veuillez saisir une date de début et une date de fin ","Erreur",JOptionPane.ERROR_MESSAGE);
        }
 
        if (test==true)
        {
            operateur = getSelectedOperateur();
            if (operateur.getGeneration().equalsIgnoreCase("2G"))
            {
                ///debut test
                jTabbedPane2G.setEnabledAt(0, false);
                jTabbedPane2G.setEnabledAt(1, false);
                jTabbedPane2G.setEnabledAt(2, false);
                jTabbedPane2G.setEnabledAt(3, false);
                jTabbedPane2G.setEnabledAt(4, false);
                jTabbedPane2G.setEnabledAt(5, false);
                jTabbedPane2G.setSelectedIndex(0);

                SupprimerImage(mes.get_CheminImage() + operateur.getCodeOperateur());
                File file = new File(mes.get_CheminImage() + operateur.getCodeOperateur());
                if (!file.isDirectory())
                {
                    file.mkdirs();
                }
                if (jCheckBoxPDF.isSelected())
                {
                    ImprimeRapport = true;
                }
                try
                {
                    ConnexionBDDOperateur cn = new ConnexionBDDOperateur(operateur.getBddOperateur());

                    if (cn.getNbreJrsPeriode(dateD, dateF) > 0)
                    {
                        showCalculBox(operateur, dateD, dateF, ImprimeRapport);
                        if (CalculFini == true)
                        {
                            try
                            {
                                Connexion_BDDGenerale cnbdd = Connexion_BDDGenerale.getInstance();
                                ParametreKPI paramKPI = cnbdd.getParametresKPIFromKPI("BHTR");

                                CarteKPI carte1 = new CarteKPI(operateur.getBddOperateur());
                                DefStyle def1 = new DefStyle();
                                def1.modifierStylebts("valkpi", paramKPI.getSeuil1() / 100, paramKPI.getSeuil2() / 100, paramKPI.getType(), mes.get_CheminParametre() + "stylebtslabel1.xml");
                                jScrollPaneCarteTrafic.removeAll();
                                jScrollPaneCarteTrafic.add(carte1.buildMapBTSDefectueuses(mes.get_CheminParametre() + "stylebtslabel1.xml", cheminFichierlog, "table_bts_bhtr", "BHTR"));

                                CarteKPI carte2 = new CarteKPI(operateur.getBddOperateur());
                                paramKPI = cnbdd.getParametresKPIFromKPI("TCHCRBH");
                                DefStyle def2 = new DefStyle();
                                def2.modifierStylebts("valkpi", paramKPI.getSeuil1() / 100, paramKPI.getSeuil2() / 100, paramKPI.getType(), mes.get_CheminParametre() + "stylebtslabel2.xml");
                                jScrollPaneCarteCanTCH.removeAll();
                                jScrollPaneCarteCanTCH.add(carte2.buildMapBTSDefectueuses(mes.get_CheminParametre() + "stylebtslabel2.xml", cheminFichierlog, "table_bts_tchcrbh", "TCHCRBH"));

                                CarteKPI carte3 = new CarteKPI(operateur.getBddOperateur());
                                paramKPI = cnbdd.getParametresKPIFromKPI("CDRBH");
                                DefStyle def3 = new DefStyle();
                                def3.modifierStylebts("valkpi", paramKPI.getSeuil1() / 100, paramKPI.getSeuil2() / 100, paramKPI.getType(), mes.get_CheminParametre() + "stylebtslabel3.xml");
                                jScrollPaneCarteMaint.removeAll();
                                jScrollPaneCarteMaint.add(carte3.buildMapBTSDefectueuses(mes.get_CheminParametre() + "stylebtslabel3.xml", cheminFichierlog, "table_bts_cdrbh", "CDRBH"));

                                CarteKPI carte4 = new CarteKPI(operateur.getBddOperateur());
                                paramKPI = cnbdd.getParametresKPIFromKPI("Hosucces");
                                DefStyle def4 = new DefStyle();
                                def4.modifierStylebts("valkpi", paramKPI.getSeuil1() / 100, paramKPI.getSeuil2() / 100, paramKPI.getType(), mes.get_CheminParametre() + "stylebtslabel4.xml");
                                jScrollPaneCarteHO.removeAll();
                                jScrollPaneCarteHO.add(carte4.buildMapBTSDefectueuses(mes.get_CheminParametre() + "stylebtslabel4.xml", cheminFichierlog, "table_bts_hosucces", "Hosucces"));

                                CarteKPI carte5 = new CarteKPI(operateur.getBddOperateur());
                                paramKPI = cnbdd.getParametresKPIFromKPI("SMSLR");
                                DefStyle def5 = new DefStyle();
                                def5.modifierStylebts("valkpi", paramKPI.getSeuil1() / 100, paramKPI.getSeuil2() / 100, paramKPI.getType(), mes.get_CheminParametre() + "stylebtslabel5.xml");
                                jScrollPaneCarteSMS.removeAll();
                                jScrollPaneCarteSMS.add(carte5.buildMapBTSDefectueuses(mes.get_CheminParametre() + "stylebtslabel5.xml", cheminFichierlog, "table_bts_smslr", "SMSLR"));

                            } catch (InterruptedException ex) {
                                Logger.getLogger(Interface_Principale.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (Exception ex) {
                                Logger.getLogger(Interface_Principale.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            operateurCalcule = operateur;
                            if (operateurCalcule.getGeneration().equalsIgnoreCase("2G")) {
                                jTabbedPane2G.setEnabledAt(0, true);
                                jTabbedPane2G.setEnabledAt(1, true);
                                jTabbedPane2G.setEnabledAt(2, true);
                                jTabbedPane2G.setEnabledAt(3, true);
                                jTabbedPane2G.setEnabledAt(4, true);
                                jTabbedPane2G.setEnabledAt(5, true);
                                jTabbedPane2G.setEnabledAt(6, true);
                            }

                            setImageOngletSynthese(mes.get_CheminImage() + operateurCalcule.getCodeOperateur(), operateurCalcule.getGeneration());
                            setImageOngletAccessibilite(mes.get_CheminImage() + operateurCalcule.getCodeOperateur(), operateurCalcule.getGeneration());
                            setImageOngletHandover(mes.get_CheminImage() + operateurCalcule.getCodeOperateur(), operateurCalcule.getGeneration());
                            setImageOngletSMS(mes.get_CheminImage() + operateurCalcule.getCodeOperateur(), operateurCalcule.getGeneration());
                            setImageOngletTrafic(mes.get_CheminImage() + operateurCalcule.getCodeOperateur(), operateurCalcule.getGeneration());
                            setImageOngletMaintenabilite(mes.get_CheminImage() + operateurCalcule.getCodeOperateur(), operateurCalcule.getGeneration());

                            jd1 = null;jd2 = null;jd3 = null;
                            jd4 = null;jd5 = null;jd6 = null;
                            jd7 = null;jd8 = null;
                            CalculFini = false;
                        } else {
                            operateurCalcule = null;
                            JOptionPane.showMessageDialog(this, "Erreur lors du Calcul", "Erreur", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Données TOTALEMENT Manquantes pour la période indiquée! ", "Info", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Interface_Principale.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if (operateur.getGeneration().equalsIgnoreCase("3G"))
            {
                ///debut test
                jTabbedPane3G.setEnabledAt(0, false);
                jTabbedPane3G.setEnabledAt(1, false);
                jTabbedPane3G.setEnabledAt(2, false);
                jTabbedPane3G.setEnabledAt(3, false);
                jTabbedPane3G.setEnabledAt(4, false);
                jTabbedPane3G.setEnabledAt(5, false);
                //jTabbedPaneMain.setEnabledAt(6, false);
                jTabbedPane3G.setSelectedIndex(0);

                SupprimerImage(mes.get_CheminImage() + operateur.getCodeOperateur());
                File file = new File(mes.get_CheminImage() + operateur.getCodeOperateur());
                if (!file.isDirectory())
                {
                    file.mkdirs();
                }
                if (jCheckBoxPDF.isSelected())
                {
                    ImprimeRapport = true;
                }
                try
                {
                    ConnexionBDDOperateur cn = new ConnexionBDDOperateur(operateur.getBddOperateur());

                    if (cn.getNbreJrsPeriode(dateD, dateF) > 0)
                    {
                        showCalculBox(operateur, dateD, dateF, ImprimeRapport);
                        if (CalculFini == true)
                        {
                            try
                            {
                                Connexion_BDDGenerale cnbdd = Connexion_BDDGenerale.getInstance();
                                ParametreKPI paramKPI = cnbdd.getParametresKPIFromKPI("BHTR");

                                CarteKPI carte1 = new CarteKPI(operateur.getBddOperateur());
                                DefStyle def1 = new DefStyle();
                                def1.modifierStylebts("valkpi", paramKPI.getSeuil1() / 100, paramKPI.getSeuil2() / 100, paramKPI.getType(), mes.get_CheminParametre() + "stylebtslabel1.xml");
                                jScrollPaneCarteTrafic.removeAll();
                                jScrollPaneCarteTrafic.add(carte1.buildMapBTSDefectueuses(mes.get_CheminParametre() + "stylebtslabel1.xml", cheminFichierlog, "table_bts_bhtr", "BHTR"));

                                CarteKPI carte2 = new CarteKPI(operateur.getBddOperateur());
                                paramKPI = cnbdd.getParametresKPIFromKPI("TCHCRBH");
                                DefStyle def2 = new DefStyle();
                                def2.modifierStylebts("valkpi", paramKPI.getSeuil1() / 100, paramKPI.getSeuil2() / 100, paramKPI.getType(), mes.get_CheminParametre() + "stylebtslabel2.xml");
                                jScrollPaneCarteCanTCH.removeAll();
                                jScrollPaneCarteCanTCH.add(carte2.buildMapBTSDefectueuses(mes.get_CheminParametre() + "stylebtslabel2.xml", cheminFichierlog, "table_bts_tchcrbh", "TCHCRBH"));

                                CarteKPI carte3 = new CarteKPI(operateur.getBddOperateur());
                                paramKPI = cnbdd.getParametresKPIFromKPI("CDRBH");
                                DefStyle def3 = new DefStyle();
                                def3.modifierStylebts("valkpi", paramKPI.getSeuil1() / 100, paramKPI.getSeuil2() / 100, paramKPI.getType(), mes.get_CheminParametre() + "stylebtslabel3.xml");
                                jScrollPaneCarteMaint.removeAll();
                                jScrollPaneCarteMaint.add(carte3.buildMapBTSDefectueuses(mes.get_CheminParametre() + "stylebtslabel3.xml", cheminFichierlog, "table_bts_cdrbh", "CDRBH"));

                                CarteKPI carte4 = new CarteKPI(operateur.getBddOperateur());
                                paramKPI = cnbdd.getParametresKPIFromKPI("Hosucces");
                                DefStyle def4 = new DefStyle();
                                def4.modifierStylebts("valkpi", paramKPI.getSeuil1() / 100, paramKPI.getSeuil2() / 100, paramKPI.getType(), mes.get_CheminParametre() + "stylebtslabel4.xml");
                                jScrollPaneCarteHO.removeAll();
                                jScrollPaneCarteHO.add(carte4.buildMapBTSDefectueuses(mes.get_CheminParametre() + "stylebtslabel4.xml", cheminFichierlog, "table_bts_hosucces", "Hosucces"));

                                CarteKPI carte5 = new CarteKPI(operateur.getBddOperateur());
                                paramKPI = cnbdd.getParametresKPIFromKPI("SMSLR");
                                DefStyle def5 = new DefStyle();
                                def5.modifierStylebts("valkpi", paramKPI.getSeuil1() / 100, paramKPI.getSeuil2() / 100, paramKPI.getType(), mes.get_CheminParametre() + "stylebtslabel5.xml");
                                jScrollPaneCarteSMS.removeAll();
                                jScrollPaneCarteSMS.add(carte5.buildMapBTSDefectueuses(mes.get_CheminParametre() + "stylebtslabel5.xml", cheminFichierlog, "table_bts_smslr", "SMSLR"));

                            } catch (InterruptedException ex) {
                                Logger.getLogger(Interface_Principale.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (Exception ex) {
                                Logger.getLogger(Interface_Principale.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            operateurCalcule = operateur;
                            jTabbedPane3G.setEnabledAt(0, true);
                            jTabbedPane3G.setEnabledAt(1, true);
                            jTabbedPane3G.setEnabledAt(2, true);
                            jTabbedPane3G.setEnabledAt(3, true);
                            jTabbedPane3G.setEnabledAt(4, true);
                            jTabbedPane3G.setEnabledAt(5, true);
                            jTabbedPane3G.setEnabledAt(6, true);

                            setImageOngletSynthese(mes.get_CheminImage() + operateurCalcule.getCodeOperateur(), operateurCalcule.getGeneration());
                            setImageOngletAccessibilite(mes.get_CheminImage() + operateurCalcule.getCodeOperateur(), operateurCalcule.getGeneration());
                            setImageOngletHandover(mes.get_CheminImage() + operateurCalcule.getCodeOperateur(), operateurCalcule.getGeneration());
                            setImageOngletSMS(mes.get_CheminImage() + operateurCalcule.getCodeOperateur(), operateurCalcule.getGeneration());
                            setImageOngletTrafic(mes.get_CheminImage() + operateurCalcule.getCodeOperateur(), operateurCalcule.getGeneration());
                            setImageOngletMaintenabilite(mes.get_CheminImage() + operateurCalcule.getCodeOperateur(), operateurCalcule.getGeneration());

                            jd1 = null;jd2 = null;jd3 = null;
                            jd4 = null;jd5 = null;jd6 = null;
                            jd7 = null;jd8 = null;
                            CalculFini = false;
                        } else {
                            operateurCalcule = null;
                            JOptionPane.showMessageDialog(this, "Erreur lors du Calcul", "Erreur", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Pas de données disponible pour la période sélectionnée! ", "Erreur", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Interface_Principale.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            ////fin test
        }
        jButtonValider.setEnabled(true);
        jComboOprateur.setEnabled(true);
        System.gc();
    }//GEN-LAST:event_jButtonValiderActionPerformed
    
    private void jComboOprateurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboOprateurActionPerformed
        // TODO add your handling code here:       
        jComboOprateur.setEnabled(false);
        jButtonValider.setEnabled(false);
        operateur = getSelectedOperateur();
        try
        {
            jScrollPaneAllBTS3G.removeAll();
            jScrollPaneAllBTS2G.removeAll();
            if (operateur.getGeneration().equalsIgnoreCase("2G"))
            {
                ConnexionBDDOperateur cn = new ConnexionBDDOperateur(operateur.getBddOperateur());
                CarteKPI carteKPI = new CarteKPI(operateur.getBddOperateur());
                jScrollPaneAllBTS2G.add(carteKPI.buildMap_AllBTS(mes.get_CheminParametre() + "stylebts.xml", cheminFichierlog));
                cn.closeConnection();
                jTabbedPane2G.setSelectedIndex(0);
                jTabbedPanePrincipale.setSelectedIndex(0);
            }
            else  if (operateur.getGeneration().equalsIgnoreCase("3G"))
            {
                ConnexionBDDOperateur cn = new ConnexionBDDOperateur(operateur.getBddOperateur());
                CarteKPI carteKPI = new CarteKPI(operateur.getBddOperateur());
                jScrollPaneAllBTS3G.add(carteKPI.buildMap_AllBTS(mes.get_CheminParametre() + "stylebts.xml", cheminFichierlog));
                cn.closeConnection();
                jTabbedPane3G.setSelectedIndex(0);
                jTabbedPanePrincipale.setSelectedIndex(1);
            }
        } 
        catch (SQLException ex)
        {
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des données dans la base de données de l'opérateur:" + operateur.getNomOperateur(), " Erreur", JOptionPane.INFORMATION_MESSAGE);
            Logger.getLogger(Interface_Principale.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(this, "Erreur lors de la connexion à la base de données de l'opérateur:" + operateur.getNomOperateur(), "Erreur", JOptionPane.INFORMATION_MESSAGE);
            Logger.getLogger(Interface_Principale.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        if (operateurCalcule != null)
        {
            if (operateurCalcule.getNomOperateur().trim().equalsIgnoreCase(operateur.getNomOperateur().trim()) && operateurCalcule.getGeneration().equalsIgnoreCase("2G"))
            {
                jTabbedPane3G.setEnabled(false);
                jTabbedPane2G.setEnabled(true);
                jTabbedPane2G.setEnabledAt(1, true);
                jTabbedPane2G.setEnabledAt(2, true);
                jTabbedPane2G.setEnabledAt(3, true);
                jTabbedPane2G.setEnabledAt(4, true);
                jTabbedPane2G.setEnabledAt(5, true);
                try
                {
                    setImageOngletSynthese(mes.get_CheminImage() + operateur.getCodeOperateur(),operateurCalcule.getGeneration());
                    setImageOngletAccessibilite(mes.get_CheminImage() + operateur.getCodeOperateur(),operateurCalcule.getGeneration());
                    setImageOngletHandover(mes.get_CheminImage() + operateur.getCodeOperateur(),operateurCalcule.getGeneration());
                    setImageOngletSMS(mes.get_CheminImage() + operateur.getCodeOperateur(),operateurCalcule.getGeneration());
                    setImageOngletTrafic(mes.get_CheminImage() + operateur.getCodeOperateur(),operateurCalcule.getGeneration());
                    setImageOngletMaintenabilite(mes.get_CheminImage() + operateur.getCodeOperateur(),operateurCalcule.getGeneration());
                }
                catch (Exception ex)
                {
                    Logger.getLogger(Interface_Principale.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if(operateurCalcule.getNomOperateur().trim().equalsIgnoreCase(operateur.getNomOperateur().trim()) && operateurCalcule.getGeneration().equalsIgnoreCase("3G"))
            {
                jTabbedPane2G.setEnabled(false);
                jTabbedPane3G.setEnabled(true);
                jTabbedPane3G.setEnabledAt(1, true);
                jTabbedPane3G.setEnabledAt(2, true);
                jTabbedPane3G.setEnabledAt(3, true);
                jTabbedPane3G.setEnabledAt(4, true);
                jTabbedPane3G.setEnabledAt(5, true);
                try
                {
                    setImageOngletSynthese(mes.get_CheminImage() + operateur.getCodeOperateur(),operateurCalcule.getGeneration());
                    setImageOngletAccessibilite(mes.get_CheminImage() + operateur.getCodeOperateur(),operateurCalcule.getGeneration());
                    setImageOngletHandover(mes.get_CheminImage() + operateur.getCodeOperateur(),operateurCalcule.getGeneration());
                    setImageOngletSMS(mes.get_CheminImage() + operateur.getCodeOperateur(),operateurCalcule.getGeneration());
                    setImageOngletTrafic(mes.get_CheminImage() + operateur.getCodeOperateur(),operateurCalcule.getGeneration());
                    setImageOngletMaintenabilite(mes.get_CheminImage() + operateur.getCodeOperateur(),operateurCalcule.getGeneration());
                }
                catch (Exception ex)
                {
                    Logger.getLogger(Interface_Principale.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                jTabbedPane2G.setEnabledAt(1, false);
                jTabbedPane2G.setEnabledAt(2, false);
                jTabbedPane2G.setEnabledAt(3, false);
                jTabbedPane2G.setEnabledAt(4, false);
                jTabbedPane2G.setEnabledAt(5, false);
                jTabbedPane2G.setSelectedIndex(0);

                jTabbedPane3G.setEnabledAt(1, false);
                jTabbedPane3G.setEnabledAt(2, false);
                jTabbedPane3G.setEnabledAt(3, false);
                jTabbedPane3G.setEnabledAt(4, false);
                jTabbedPane3G.setEnabledAt(5, false);
                jTabbedPane3G.setSelectedIndex(0);

                SetImageDefautOngletSynthese();
                setImageOngletAccessibiliteDefault();
                setImageOngletHandoverDefault();
                setImageOngletMaintenabiliteDefault();
                setImageOngletSMSDefault();
                setImageOngletTraficDefaut();
            }
        }
        jComboOprateur.setEnabled(true);
        jButtonValider.setEnabled(true);
        System.gc();
    }//GEN-LAST:event_jComboOprateurActionPerformed

    private void jButtonSuivSMSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSuivSMSActionPerformed
        // TODO add your handling code here:
        operateur=getSelectedOperateur();
        File file=new File(mes.get_CheminImage() + operateur.getCodeOperateur() );
        String chemingraphiques=mes.get_CheminImage() + operateur.getCodeOperateur() + File.separator ;

        if (file.exists() && file.isDirectory())
        {
            if (pageSMS < 4)
            {
                pageSMS = pageSMS + 1;
            }
            switch (pageSMS)
            {
                case 1:
                {
                    operateur = getSelectedOperateur();
                    ImageIcon image1 = new ImageIcon();
                    image1 = new ImageIcon(chemingraphiques+"SMS3.jpg");
                    Image imlabel1 = image1.getImage();
                    imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                    jLabelSMS1.setIcon(new ImageIcon(imlabel1));
                    break;
                }
            }
        }
}//GEN-LAST:event_jButtonSuivSMSActionPerformed

    private void jButtonPrecSMSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrecSMSActionPerformed
        // TODO add your handling code here:
        operateur=getSelectedOperateur();
        File file=new File(mes.get_CheminImage() + operateur.getCodeOperateur() );
        String chemingraphiques=mes.get_CheminImage() + operateur.getCodeOperateur() + File.separator ;

        if (file.isDirectory() && file.exists())
        {
            if (pageSMS >= 2)
            {
                pageSMS = pageSMS - 1;
            }
            switch (pageSMS)
            {
                case 1:
                {
                    operateur = getSelectedOperateur();
                    jButtonPrecSMS.setEnabled(false);

                    ImageIcon image1 = new ImageIcon();
                    image1 = new ImageIcon(chemingraphiques+ "SMS3.jpg");
                    Image imlabel1 = image1.getImage();
                    imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                    jLabelSMS1.setIcon(new ImageIcon(imlabel1));
                    break;
                }
            }
        }
}//GEN-LAST:event_jButtonPrecSMSActionPerformed

    private void jButtonSuivHOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSuivHOActionPerformed
        // TODO add your handling code here:
        operateur=getSelectedOperateur();
        File file=new File(mes.get_CheminImage() + operateur.getCodeOperateur() );
        String chemingraphiques=mes.get_CheminImage() + operateur.getCodeOperateur() + File.separator ;
        if (file.exists() && file.isDirectory())
        {
            if (pageHandOver < 4)
            {
                pageHandOver = pageHandOver + 1;
            }

            switch (pageHandOver)
            {
                case 1:
                {
                    ImageIcon image1 = new ImageIcon();
                    image1 = new ImageIcon(chemingraphiques + "HO4.jpg");
                    Image imlabel1 = image1.getImage();
                    imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                    jLabelHO1.setIcon(new ImageIcon(imlabel1));

                    image1 = new ImageIcon(chemingraphiques + "HO5.jpg");
                    imlabel1 = image1.getImage();
                    imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                    jLabelHO2.setIcon(new ImageIcon(imlabel1));

                    image1 = new ImageIcon(chemingraphiques + "HO6.jpg");
                    imlabel1 = image1.getImage();
                    imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                    jLabelHO3.setIcon(new ImageIcon(imlabel1));
                    break;
                }
                case 2:
                {
                    jButtonSuivHO.setEnabled(false);
                    jButtonPrecHO.setEnabled(true);
                    ImageIcon image1 = new ImageIcon();
                    image1 = new ImageIcon(chemingraphiques + "HO8.jpg");
                    Image imlabel1 = image1.getImage();
                    imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                    jLabelHO1.setIcon(new ImageIcon(imlabel1));
                    jButtonPrecHO.setEnabled(true);
                    jButtonSuivHO.setEnabled(false);

                    image1 = new ImageIcon();
                    jLabelHO2.setIcon(image1);

                    image1 = new ImageIcon();
                    jLabelHO3.setIcon(image1);
                    jButtonPrecHO.setEnabled(true);
                    break;
                }
            }
        }
}//GEN-LAST:event_jButtonSuivHOActionPerformed

    private void jButtonPrecHOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrecHOActionPerformed
        // TODO add your handling code here:
        operateur=getSelectedOperateur();
        File file=new File(mes.get_CheminImage() + operateur.getCodeOperateur() );
        String chemingraphiques=mes.get_CheminImage() + operateur.getCodeOperateur() + File.separator ;
        
        if (file.exists() && file.isDirectory())
        {
            if (pageHandOver >= 2)
            {
                pageHandOver = pageHandOver - 1;
            }
            switch (pageHandOver)
            {
                case 1:
                {
                    jButtonSuivHO.setEnabled(true);
                    jButtonPrecHO.setEnabled(false);
                    ImageIcon image1 = new ImageIcon();
                    image1 = new ImageIcon(chemingraphiques + "HO4.jpg");
                    Image imlabel1 = image1.getImage();
                    imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                    jLabelHO1.setIcon(new ImageIcon(imlabel1));

                    image1 = new ImageIcon(chemingraphiques + "HO5.jpg");
                    imlabel1 = image1.getImage();
                    imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                    jLabelHO2.setIcon(new ImageIcon(imlabel1));

                    image1 = new ImageIcon(chemingraphiques + "HO6.jpg");
                    imlabel1 = image1.getImage();
                    imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                    jLabelHO3.setIcon(new ImageIcon(imlabel1));
                    break;
                }
                case 2:
                {
                    ImageIcon image1 = new ImageIcon();
                    image1 = new ImageIcon(chemingraphiques + "HO8.jpg");
                    Image imlabel1 = image1.getImage();
                    imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                    jLabelHO1.setIcon(new ImageIcon(imlabel1));
                    break;
                }
            }
        }
}//GEN-LAST:event_jButtonPrecHOActionPerformed

    private void jButtonSuivMaintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSuivMaintActionPerformed
        // TODO add your handling code here:
        operateur=getSelectedOperateur();
        File file=new File(mes.get_CheminImage() + operateur.getCodeOperateur() );
        String chemingraphiques=mes.get_CheminImage() + operateur.getCodeOperateur() + File.separator ;
        
        if (file.exists() && file.isDirectory())
        {
            if (pageMaint < 4)
            {
                pageMaint = pageMaint + 1;
            }
            switch (pageMaint)
            {
                case 1:
                {
                    ImageIcon image1 = new ImageIcon();
                    image1 = new ImageIcon(chemingraphiques + "Maintien1.jpg");
                    Image imlabel1 = image1.getImage();
                    imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                    jLabelMaint1.setIcon(new ImageIcon(imlabel1));

                    image1 = new ImageIcon(chemingraphiques + "Maintien2.jpg");
                    imlabel1 = image1.getImage();
                    imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                    jLabelMaint2.setIcon(new ImageIcon(imlabel1));

                    image1 = new ImageIcon(chemingraphiques + "Maintien5.jpg");
                    imlabel1 = image1.getImage();
                    imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                    jLabelMaint3.setIcon(new ImageIcon(imlabel1));
                    break;
                }
            }
        }
}//GEN-LAST:event_jButtonSuivMaintActionPerformed

    private void jButtonPrecMaintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrecMaintActionPerformed
        // TODO add your handling code here:
        operateur=getSelectedOperateur();
        File file=new File(mes.get_CheminImage() + operateur.getCodeOperateur() );
        String chemingraphiques=mes.get_CheminImage() + operateur.getCodeOperateur() + File.separator ;

        if (file.exists() && file.isDirectory())
        {
            if (pageMaint >= 2)
            {
                pageMaint = pageMaint - 1;
            }
            switch (pageMaint) 
            {
                case 1:
                {
                    jButtonPrecMaint.setEnabled(false);
                    ImageIcon image1 = new ImageIcon();
                    image1 = new ImageIcon(chemingraphiques + "Maintien1.jpg");
                    Image imlabel1 = image1.getImage();
                    imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                    jLabelMaint1.setIcon(new ImageIcon(imlabel1));

                    image1 = new ImageIcon(chemingraphiques + "Maintien2.jpg");
                    imlabel1 = image1.getImage();
                    imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                    jLabelMaint2.setIcon(new ImageIcon(imlabel1));

                    image1 = new ImageIcon(chemingraphiques + "Maintien5.jpg");
                    imlabel1 = image1.getImage();
                    imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                    jLabelMaint3.setIcon(new ImageIcon(imlabel1));
                    break;
                }
            }
        }
}//GEN-LAST:event_jButtonPrecMaintActionPerformed

    private void jButtonSuivAccesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSuivAccesActionPerformed
        // TODO add your handling code here:
        operateur=getSelectedOperateur();
        File file=new File(mes.get_CheminImage() + operateur.getCodeOperateur() );
        String chemingraphiques=mes.get_CheminImage() + operateur.getCodeOperateur() + File.separator ;
        
        if (file.exists() && file.isDirectory())
        {
            if (pageTCH < 4)
            {
                pageTCH = pageTCH + 1;
            }
            switch (pageTCH)
            {
                case 1:
                {
                    ImageIcon image1 = new ImageIcon(chemingraphiques + "Acces14.jpg");
                    Image imlabel1 = image1.getImage();
                    imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                    jLabelCanTCH1.setIcon(new ImageIcon(imlabel1));

                    image1 = new ImageIcon(chemingraphiques + "Acces2.jpg");
                    imlabel1 = image1.getImage();
                    imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                    jLabelCanTCH2.setIcon(new ImageIcon(imlabel1));

                    image1 = new ImageIcon(chemingraphiques + "Acces9.jpg");
                    imlabel1 = image1.getImage();
                    imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                    jLabelCanTCH3.setIcon(new ImageIcon(imlabel1));
                    break;
                }
                case 2:
                {
                    jButtonPrecAcces.setEnabled(true);
                    jButtonSuivAcces.setEnabled(false);

                    ImageIcon image1 = new ImageIcon();
                    image1 = new ImageIcon(chemingraphiques + "Acces10.jpg");
                    Image imlabel1 = image1.getImage();
                    imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                    jLabelCanTCH1.setIcon(new ImageIcon(imlabel1));

                    image1 = new ImageIcon(chemingraphiques + "Acces13.jpg");
                    imlabel1 = image1.getImage();
                    imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                    jLabelCanTCH2.setIcon(new ImageIcon(imlabel1));

                    image1 = new ImageIcon(chemingraphiques + "Acces12.jpg");
                    imlabel1 = image1.getImage();
                    imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                    jLabelCanTCH3.setIcon(new ImageIcon(imlabel1));
                    break;
                }
            }
        }
}//GEN-LAST:event_jButtonSuivAccesActionPerformed

    private void jButtonPrecAccesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrecAccesActionPerformed
        // TODO add your handling code here:
        operateur=getSelectedOperateur();
        File file=new File(mes.get_CheminImage() + operateur.getCodeOperateur() );
        String chemingraphiques=mes.get_CheminImage() + operateur.getCodeOperateur() + File.separator ;

        if (file.exists() && file.isDirectory())
        {
            if (pageTCH >= 2)
            {
                pageTCH = pageTCH - 1;
            }
            switch (pageTCH)
            {
                case 1:
                {
                    jButtonPrecAcces.setEnabled(false);
                    jButtonSuivAcces.setEnabled(true);
                    ImageIcon image1 = new ImageIcon(chemingraphiques + "Acces14.jpg");
                    Image imlabel1 = image1.getImage();
                    imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                    jLabelCanTCH1.setIcon(new ImageIcon(imlabel1));

                    image1 = new ImageIcon(chemingraphiques + "Acces2.jpg");
                    imlabel1 = image1.getImage();
                    imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                    jLabelCanTCH2.setIcon(new ImageIcon(imlabel1));

                    image1 = new ImageIcon(chemingraphiques + "Acces9.jpg");
                    imlabel1 = image1.getImage();
                    imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                    jLabelCanTCH3.setIcon(new ImageIcon(imlabel1));
                    break;
                }
                case 2:
                {
                    ImageIcon image1 = new ImageIcon();
                    image1 = new ImageIcon(chemingraphiques + "Acces10.jpg");
                    Image imlabel1 = image1.getImage();
                    imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                    jLabelCanTCH1.setIcon(new ImageIcon(imlabel1));

                    image1 = new ImageIcon(chemingraphiques + "Acces13.jpg");
                    imlabel1 = image1.getImage();
                    imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                    jLabelCanTCH2.setIcon(new ImageIcon(imlabel1));

                    image1 = new ImageIcon(chemingraphiques + "Acces12.jpg");
                    imlabel1 = image1.getImage();
                    imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);

                    jLabelCanTCH3.setIcon(new ImageIcon(imlabel1));
                    break;
                }
            }
        }
}//GEN-LAST:event_jButtonPrecAccesActionPerformed

    private void jButtonSuivTrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSuivTrActionPerformed
        // TODO add your handling code here:
        operateur=getSelectedOperateur();
        File file=new File(mes.get_CheminImage() + operateur.getCodeOperateur() );
        String chemingraphiques=mes.get_CheminImage() + operateur.getCodeOperateur() + File.separator ;

        if (file.exists() && file.isDirectory())
        {
            if (pageTrafic < 4)
            {
                pageTrafic = pageTrafic + 1;
            }
            switch (pageTrafic)
            {
                case 1:
                {
                    ImageIcon image1 = new ImageIcon();
                    image1 = new ImageIcon(chemingraphiques + "trafic1.jpg");
                    Image imlabel1 = image1.getImage();
                    imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                    jLabelTr1.setIcon(new ImageIcon(imlabel1));

                    image1 = new ImageIcon(chemingraphiques + "trafic2.jpg");
                    imlabel1 = image1.getImage();
                    imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                    jLabelTr2.setIcon(new ImageIcon(imlabel1));
                    break;
                }
            }
        }
}//GEN-LAST:event_jButtonSuivTrActionPerformed

    private void jButtonPrecTrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrecTrActionPerformed
        // TODO add your handling code here:
        operateur=getSelectedOperateur();
        File file=new File(mes.get_CheminImage() + operateur.getCodeOperateur() );
        String chemingraphiques=mes.get_CheminImage() + operateur.getCodeOperateur() + File.separator ;

        if (file.exists() && file.isDirectory())
        {
            if (pageTrafic >= 2)
            {
                pageTrafic = pageTrafic - 1;
            }
            switch (pageTrafic)
            {
                case 1:
                {
                    jButtonPrecTr.setEnabled(false);
                    jButtonSuivTr.setEnabled(true);
                    ImageIcon image1 = new ImageIcon();
                    image1 = new ImageIcon(chemingraphiques + "trafic1.jpg");
                    Image imlabel1 = image1.getImage();
                    imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                    jLabelTr1.setIcon(new ImageIcon(imlabel1));

                    image1 = new ImageIcon(chemingraphiques + "trafic2.jpg");
                    imlabel1 = image1.getImage();
                    imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                    jLabelTr2.setIcon(new ImageIcon(imlabel1));
                    break;
                }
            }
        }
}//GEN-LAST:event_jButtonPrecTrActionPerformed

    private void jPanelSyntheseFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanelSyntheseFocusGained
        // TODO add your handling code here:
}//GEN-LAST:event_jPanelSyntheseFocusGained

    private void jLabelSynCarte9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelSynCarte9MouseClicked
        if (jd8 == null)
        {
            int bouton=evt.getButton();
            if (bouton==MouseEvent.BUTTON1)
            {
                jd8 = ZoomCarteKPI("SMSLR");
            }
        }
        else if(jd8 != null)
        {
            jd8.setLocationRelativeTo(this);
            jd8.setVisible(true);
        }
}//GEN-LAST:event_jLabelSynCarte9MouseClicked

    private void jLabelSynCarte3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelSynCarte3MouseClicked
        if(jd2==null )
        {
            int bouton=evt.getButton();
            if (bouton==MouseEvent.BUTTON1)
            {
                jd2 = ZoomCarteKPI("TCHCRBH");
            }
        }
        else if(jd2 != null)
        {
            jd2.setLocationRelativeTo(this);
            jd2.setVisible(true);
        }
}//GEN-LAST:event_jLabelSynCarte3MouseClicked

    private void jLabelSynCarte6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelSynCarte6MouseClicked
        // TODO add your handling code here:
        if(jd5==null )
        {
            int bouton=evt.getButton();
            if (bouton==MouseEvent.BUTTON1)
            {
                jd5 = ZoomCarteKPI("CDRBH");
            }
        }
        else if(jd5 != null)
        {
            jd5.setLocationRelativeTo(this);
            jd5.setVisible(true);
        }
    }//GEN-LAST:event_jLabelSynCarte6MouseClicked

    private void jLabelSynCarte7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelSynCarte7MouseClicked
        // TODO add your handling code here:
        if(jd6==null )
        {
            int bouton=evt.getButton();
            if (bouton==MouseEvent.BUTTON1)
            {
                jd6 = ZoomCarteKPI("HoSucces");
            }
        }
        else if(jd6 != null)
        {
            jd6.setLocationRelativeTo(this);
            jd6.setVisible(true);
        }
    }//GEN-LAST:event_jLabelSynCarte7MouseClicked

    private void jLabelSynCarte4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelSynCarte4MouseClicked
        // TODO add your handling code here:
        if(jd3==null )
        {
            int bouton=evt.getButton();
            if (bouton==MouseEvent.BUTTON1)
            {
                jd3=ZoomCarteKPI("TCHDRBH");
            }
        }
        else if(jd3 != null)
        {
            jd3.setLocationRelativeTo(this);
            jd3.setVisible(true);
        }
}//GEN-LAST:event_jLabelSynCarte4MouseClicked

    private void jLabelSynCarte8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelSynCarte8MouseClicked
        // TODO add your handling code here:
        if(jd7==null )
        {
            int bouton=evt.getButton();
            if (bouton==MouseEvent.BUTTON1)
            {
                jd7= ZoomCarteKPI("HOULQR");
            }
        }
        else if(jd7 != null)
        {
            jd7.setLocationRelativeTo(this);
            jd7.setVisible(true);
        }
    }//GEN-LAST:event_jLabelSynCarte8MouseClicked

    private void jLabelSynCarte5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelSynCarte5MouseClicked
        // TODO add your handling code here:
        if(jd4==null )
        {
            int bouton=evt.getButton();
            if (bouton==MouseEvent.BUTTON1)
            {
                jd4=ZoomCarteKPI("CSRBH");
            }
        }
        else if(jd4 != null)
        {
            jd4.setLocationRelativeTo(this);
            jd4.setVisible(true);
        }
    }//GEN-LAST:event_jLabelSynCarte5MouseClicked

    private void jLabelSynCarte2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelSynCarte2MouseClicked
        // TODO add your handling code here:
        if(jd1==null)
        {
            int bouton=evt.getButton();
            if (bouton==MouseEvent.BUTTON1)
            {
                jd1 = ZoomCarteKPI("CSSR");
            }
        }
        else if(jd1 != null)
        {
            jd1.setLocationRelativeTo(this);
            jd1.setVisible(true);
        }
}//GEN-LAST:event_jLabelSynCarte2MouseClicked

    private void jMenuItemAProposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAProposActionPerformed
        // TODO add your handling code here:
        if (aboutBox == null)
        {
            JFrame mainFrame = Projet_ConsultationApp.getApplication().getMainFrame();
            aboutBox = new Projet_ConsultationAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        Projet_ConsultationApp.getApplication().show(aboutBox);
    }//GEN-LAST:event_jMenuItemAProposActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        ///SupprimerImage(mes.get_CheminImage());
    }//GEN-LAST:event_formWindowClosing

    private void jMenuItemDeconnexionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemDeconnexionActionPerformed
        // TODO add your handling code here:
        int valQuitter=JOptionPane.showConfirmDialog(this,"Voulez-vous quitter ?", "",JOptionPane.YES_NO_OPTION);
        if(valQuitter==0)
        {
            SupprimerImage(mes.get_CheminImage());
            this.dispose();
            JFrame_connexion jfrm=new JFrame_connexion();
            jfrm.setVisible(true);
        }
    }//GEN-LAST:event_jMenuItemDeconnexionActionPerformed

    private void jMenuItemQuitterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemQuitterActionPerformed
        // TODO add your handling code here:
        int valQuitter=JOptionPane.showConfirmDialog(this,"Voulez-vous quitter ?", "",JOptionPane.YES_NO_OPTION);
        if(valQuitter==0)
        {
            SupprimerImage(mes.get_CheminImage());
            this.dispose();
            System.exit(0);
        }
    }//GEN-LAST:event_jMenuItemQuitterActionPerformed

    private void jLabelSynCarte21MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelSynCarte21MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabelSynCarte21MouseClicked

    private void jLabelSynCarte51MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelSynCarte51MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabelSynCarte51MouseClicked

    private void jLabelSynCarte81MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelSynCarte81MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabelSynCarte81MouseClicked

    private void jLabelSynCarte41MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelSynCarte41MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabelSynCarte41MouseClicked

    private void jLabelSynCarte71MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelSynCarte71MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabelSynCarte71MouseClicked

    private void jLabelSynCarte61MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelSynCarte61MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabelSynCarte61MouseClicked

    private void jLabelSynCarte31MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelSynCarte31MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabelSynCarte31MouseClicked

    private void jLabelSynCarte91MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelSynCarte91MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabelSynCarte91MouseClicked

    private void jPanelSynthese2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanelSynthese2FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanelSynthese2FocusGained

    private void jButtonPrecTr2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrecTr2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonPrecTr2ActionPerformed

    private void jButtonSuivTr2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSuivTr2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonSuivTr2ActionPerformed

    private void jButtonPrecAcces2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrecAcces2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonPrecAcces2ActionPerformed

    private void jButtonSuivAcces2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSuivAcces2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonSuivAcces2ActionPerformed

    private void jButtonPrecMaint2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrecMaint2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonPrecMaint2ActionPerformed

    private void jButtonSuivMaint2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSuivMaint2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonSuivMaint2ActionPerformed

    private void jButtonPrecHO2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrecHO2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonPrecHO2ActionPerformed

    private void jButtonSuivHO2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSuivHO2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonSuivHO2ActionPerformed

    private void jButtonPrecSMS2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrecSMS2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonPrecSMS2ActionPerformed

    private void jButtonSuivSMS2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSuivSMS2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonSuivSMS2ActionPerformed

    private JDialog ZoomCarteKPI(String kpi)
    {
        try
        { 
            operateur = getSelectedOperateur();
            if (operateurCalcule != null)
            { 
                operateurCalcule = operateur;
                CarteKPI carteKPI = new CarteKPI(operateurCalcule.getBddOperateur());
                Connexion_BDDGenerale cnbdd = Connexion_BDDGenerale.getInstance();
                ParametreKPI paramKPI = cnbdd.getParametresKPIFromKPI(kpi);
                StyleCarte styleCarte = new StyleCarte();
                styleCarte.modifierStyle(paramKPI.getKpi(), paramKPI.getSeuil1() / 100, paramKPI.getSeuil2() / 100, paramKPI.getType(), cheminParametres + "stylecarteRegions.xml");
                styleCarte.modifierStyle(paramKPI.getKpi(), paramKPI.getSeuil1() / 100, paramKPI.getSeuil2() / 100, paramKPI.getType(), cheminParametres + "styleAXE.xml");

                JDialog jDialog = new JDialog(this, true);
                JPanel pfond = new JPanel(new BorderLayout(1, 3));
                JLabel jlbp = new JLabel(paramKPI.getNomKpi());
                jlbp.setFont(new Font("Dialog", Font.BOLD, 32));
                jlbp.setHorizontalAlignment(JLabel.CENTER);
                jlbp.setVerticalAlignment(JLabel.CENTER);
                pfond.setSize(600, 800);//mauritanie
                //pfond.add(BorderLayout.NORTH, carteKPI.buildTool());
                jDialog.setContentPane(pfond);
                jDialog.setTitle(paramKPI.getNomKpi());
                jDialog.setFont(new Font("Dialog", Font.BOLD, 14));
                jDialog.getContentPane().add(BorderLayout.NORTH, carteKPI.buildTool());
                jDialog.getContentPane().add(BorderLayout.CENTER, carteKPI.buildMapRegion(cheminParametres + "stylecarteRegions.xml", cheminFichierlog));
                jDialog.setSize(900, 900);
                return jDialog;
            }
        } catch (Exception ex)
        {
            Logger.getLogger(Interface_Principale.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
    * @param args the command line arguments
    */
    /*public static void main(String args[])
    {
        Interface_Principale itf = new Interface_Principale();
        itf.setVisible(true);
    }
     * 
     */


 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonPrecAcces;
    private javax.swing.JButton jButtonPrecAcces2;
    private javax.swing.JButton jButtonPrecHO;
    private javax.swing.JButton jButtonPrecHO2;
    private javax.swing.JButton jButtonPrecMaint;
    private javax.swing.JButton jButtonPrecMaint2;
    private javax.swing.JButton jButtonPrecSMS;
    private javax.swing.JButton jButtonPrecSMS2;
    private javax.swing.JButton jButtonPrecTr;
    private javax.swing.JButton jButtonPrecTr2;
    private javax.swing.JButton jButtonSuivAcces;
    private javax.swing.JButton jButtonSuivAcces2;
    private javax.swing.JButton jButtonSuivHO;
    private javax.swing.JButton jButtonSuivHO2;
    private javax.swing.JButton jButtonSuivMaint;
    private javax.swing.JButton jButtonSuivMaint2;
    private javax.swing.JButton jButtonSuivSMS;
    private javax.swing.JButton jButtonSuivSMS2;
    private javax.swing.JButton jButtonSuivTr;
    private javax.swing.JButton jButtonSuivTr2;
    private javax.swing.JButton jButtonValider;
    private javax.swing.JCheckBox jCheckBoxPDF;
    private javax.swing.JComboBox jComboOprateur;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelCanTCH1;
    private javax.swing.JLabel jLabelCanTCH11;
    private javax.swing.JLabel jLabelCanTCH2;
    private javax.swing.JLabel jLabelCanTCH21;
    private javax.swing.JLabel jLabelCanTCH3;
    private javax.swing.JLabel jLabelCanTCH31;
    private javax.swing.JLabel jLabelHO1;
    private javax.swing.JLabel jLabelHO11;
    private javax.swing.JLabel jLabelHO2;
    private javax.swing.JLabel jLabelHO21;
    private javax.swing.JLabel jLabelHO3;
    private javax.swing.JLabel jLabelHO31;
    private javax.swing.JLabel jLabelLogo;
    private javax.swing.JLabel jLabelMaint1;
    private javax.swing.JLabel jLabelMaint11;
    private javax.swing.JLabel jLabelMaint2;
    private javax.swing.JLabel jLabelMaint21;
    private javax.swing.JLabel jLabelMaint3;
    private javax.swing.JLabel jLabelMaint31;
    private javax.swing.JLabel jLabelSMS1;
    private javax.swing.JLabel jLabelSMS11;
    private javax.swing.JLabel jLabelSMS2;
    private javax.swing.JLabel jLabelSMS21;
    private javax.swing.JLabel jLabelSMS3;
    private javax.swing.JLabel jLabelSMS31;
    private javax.swing.JLabel jLabelSynCarte1;
    private javax.swing.JLabel jLabelSynCarte11;
    private javax.swing.JLabel jLabelSynCarte2;
    private javax.swing.JLabel jLabelSynCarte21;
    private javax.swing.JLabel jLabelSynCarte3;
    private javax.swing.JLabel jLabelSynCarte31;
    private javax.swing.JLabel jLabelSynCarte4;
    private javax.swing.JLabel jLabelSynCarte41;
    private javax.swing.JLabel jLabelSynCarte5;
    private javax.swing.JLabel jLabelSynCarte51;
    private javax.swing.JLabel jLabelSynCarte6;
    private javax.swing.JLabel jLabelSynCarte61;
    private javax.swing.JLabel jLabelSynCarte7;
    private javax.swing.JLabel jLabelSynCarte71;
    private javax.swing.JLabel jLabelSynCarte8;
    private javax.swing.JLabel jLabelSynCarte81;
    private javax.swing.JLabel jLabelSynCarte9;
    private javax.swing.JLabel jLabelSynCarte91;
    private javax.swing.JLabel jLabelSynCdr1;
    private javax.swing.JLabel jLabelSynCdr11;
    private javax.swing.JLabel jLabelSynCdr2;
    private javax.swing.JLabel jLabelSynCdr21;
    private javax.swing.JLabel jLabelSynCdr3;
    private javax.swing.JLabel jLabelSynCdr31;
    private javax.swing.JLabel jLabelSynCdr4;
    private javax.swing.JLabel jLabelSynCdr41;
    private javax.swing.JLabel jLabelSynCdr5;
    private javax.swing.JLabel jLabelSynCdr51;
    private javax.swing.JLabel jLabelSynCdr6;
    private javax.swing.JLabel jLabelSynCdr61;
    private javax.swing.JLabel jLabelSynCdr7;
    private javax.swing.JLabel jLabelSynCdr71;
    private javax.swing.JLabel jLabelSynCdr8;
    private javax.swing.JLabel jLabelSynCdr81;
    private javax.swing.JLabel jLabelSynCdr9;
    private javax.swing.JLabel jLabelSynCdr91;
    private javax.swing.JLabel jLabelTr1;
    private javax.swing.JLabel jLabelTr11;
    private javax.swing.JLabel jLabelTr2;
    private javax.swing.JLabel jLabelTr21;
    private javax.swing.JLabel jLabelTr3;
    private javax.swing.JLabel jLabelTr31;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenuFichier;
    private javax.swing.JMenuItem jMenuItemAPropos;
    private javax.swing.JMenuItem jMenuItemDeconnexion;
    private javax.swing.JMenuItem jMenuItemQuitter;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JPanel jPanel54;
    private javax.swing.JPanel jPanel55;
    private javax.swing.JPanel jPanel56;
    private javax.swing.JPanel jPanel59;
    private javax.swing.JPanel jPanel66;
    private javax.swing.JPanel jPanel67;
    private javax.swing.JPanel jPanel68;
    private javax.swing.JPanel jPanel69;
    private javax.swing.JPanel jPanel70;
    private javax.swing.JPanel jPanel71;
    private javax.swing.JPanel jPanel72;
    private javax.swing.JPanel jPanel73;
    private javax.swing.JPanel jPanelCORE;
    private javax.swing.JPanel jPanelCORE2;
    private javax.swing.JPanel jPanelCanalTCH;
    private javax.swing.JPanel jPanelCanalTCH2;
    private javax.swing.JPanel jPanelDATA;
    private javax.swing.JPanel jPanelDATA2;
    private javax.swing.JPanel jPanelHandover;
    private javax.swing.JPanel jPanelHandover2;
    private javax.swing.JPanel jPanelMaintenabilite;
    private javax.swing.JPanel jPanelMaintenabilite2;
    private javax.swing.JPanel jPanelRoaming;
    private javax.swing.JPanel jPanelRoaming2;
    private javax.swing.JPanel jPanelSMS;
    private javax.swing.JPanel jPanelSMS2;
    private javax.swing.JPanel jPanelSynthese;
    private javax.swing.JPanel jPanelSynthese2;
    private javax.swing.JPanel jPanelTrImage;
    private javax.swing.JPanel jPanelTrImage2;
    private javax.swing.JPanel jPanelTrafic;
    private javax.swing.JPanel jPanelTrafic2;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane23;
    private javax.swing.JScrollPane jScrollPane24;
    private javax.swing.JScrollPane jScrollPane25;
    private javax.swing.JScrollPane jScrollPane28;
    private javax.swing.JScrollPane jScrollPane29;
    private javax.swing.JScrollPane jScrollPane30;
    private javax.swing.JScrollPane jScrollPane33;
    private javax.swing.JScrollPane jScrollPane34;
    private javax.swing.JScrollPane jScrollPane35;
    private javax.swing.JScrollPane jScrollPane41;
    private javax.swing.JScrollPane jScrollPane42;
    private javax.swing.JScrollPane jScrollPane43;
    private javax.swing.JScrollPane jScrollPane44;
    private javax.swing.JScrollPane jScrollPane45;
    private javax.swing.JScrollPane jScrollPane46;
    private javax.swing.JScrollPane jScrollPane47;
    private javax.swing.JScrollPane jScrollPane48;
    private javax.swing.JScrollPane jScrollPane49;
    private javax.swing.JScrollPane jScrollPane50;
    private javax.swing.JScrollPane jScrollPane51;
    private javax.swing.JScrollPane jScrollPane52;
    private javax.swing.JScrollPane jScrollPane53;
    private javax.swing.JScrollPane jScrollPane54;
    private javax.swing.JScrollPane jScrollPane55;
    private javax.swing.JScrollPane jScrollPane56;
    private javax.swing.JScrollPane jScrollPane69;
    private javax.swing.JScrollPane jScrollPane70;
    private javax.swing.JScrollPane jScrollPane71;
    private javax.swing.JScrollPane jScrollPane72;
    private javax.swing.JScrollPane jScrollPane73;
    private javax.swing.JScrollPane jScrollPane74;
    private javax.swing.JScrollPane jScrollPane75;
    private javax.swing.JScrollPane jScrollPane76;
    private javax.swing.JScrollPane jScrollPane77;
    private javax.swing.JScrollPane jScrollPane78;
    private javax.swing.JScrollPane jScrollPane79;
    private javax.swing.JScrollPane jScrollPane80;
    private javax.swing.JScrollPane jScrollPane81;
    private javax.swing.JScrollPane jScrollPane82;
    private javax.swing.JScrollPane jScrollPane83;
    private javax.swing.JScrollPane jScrollPane84;
    private javax.swing.JScrollPane jScrollPane85;
    private javax.swing.JScrollPane jScrollPane86;
    private javax.swing.JScrollPane jScrollPane87;
    private javax.swing.JScrollPane jScrollPane88;
    private javax.swing.JScrollPane jScrollPane89;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JScrollPane jScrollPaneAllBTS2G;
    private javax.swing.JScrollPane jScrollPaneAllBTS3G;
    private java.awt.ScrollPane jScrollPaneCarteCanTCH;
    private java.awt.ScrollPane jScrollPaneCarteCanTCH2;
    private java.awt.ScrollPane jScrollPaneCarteHO;
    private java.awt.ScrollPane jScrollPaneCarteHO2;
    private java.awt.ScrollPane jScrollPaneCarteMaint;
    private java.awt.ScrollPane jScrollPaneCarteMaint2;
    private java.awt.ScrollPane jScrollPaneCarteSMS;
    private java.awt.ScrollPane jScrollPaneCarteSMS2;
    private java.awt.ScrollPane jScrollPaneCarteTrafic;
    private java.awt.ScrollPane jScrollPaneCarteTrafic2;
    private javax.swing.JTabbedPane jTabbedPane2G;
    private javax.swing.JTabbedPane jTabbedPane3G;
    private javax.swing.JTabbedPane jTabbedPanePrincipale;
    private javax.swing.JTree jTreeBTS10;
    private javax.swing.JTree jTreeBTS13;
    private javax.swing.JTree jTreeBTS14;
    private javax.swing.JTree jTreeBTS9;
    private javax.swing.JTree jTreeElt10;
    private javax.swing.JTree jTreeElt13;
    private javax.swing.JTree jTreeElt14;
    private javax.swing.JTree jTreeElt9;
    private com.toedter.calendar.JDateChooser jdatedebut;
    private com.toedter.calendar.JDateChooser jdatefin;
    // End of variables declaration//GEN-END:variables

    private void SetImageDefautOngletSynthese()
    {
        try
        {
            //afficher image Onglet Synthèse
            org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(projet_consultation.Projet_ConsultationApp.class).getContext().getResourceMap(projet_consultation.Principale.Interface_Principale.class);
            //ImageIcon im1=resourceMap.getImageIcon("logo_regulateur");
            //ImageIcon image1 = new ImageIcon("./resources/fond11.jpg");
            ImageIcon image1 = resourceMap.getImageIcon("fond11.icon");
            Image im1 = image1.getImage();
            im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            ImageIcon image2 = resourceMap.getImageIcon("fond12.icon");
            Image im2 = image2.getImage();
            im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            jLabelSynCdr1.setIcon(new ImageIcon(im1));
            jLabelSynCarte1.setIcon(new ImageIcon(im2));
            jLabelSynCdr1.setSize(dCar);
            jLabelSynCarte1.setSize(dCar);

            image1 = resourceMap.getImageIcon("fond13.icon");
            im1 = image1.getImage();
            im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            image2 = resourceMap.getImageIcon("fond14.icon");
            im2 = image2.getImage();
            im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            jLabelSynCdr2.setIcon(new ImageIcon(im1));
            jLabelSynCarte2.setIcon(new ImageIcon(im2));
            jLabelSynCdr2.setSize(dCar);
            jLabelSynCarte2.setSize(dCar);

            image1 = resourceMap.getImageIcon("fond15.icon");
            im1 = image1.getImage();
            im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            image2 = resourceMap.getImageIcon("fond16.icon");
            im2 = image2.getImage();
            im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            jLabelSynCdr3.setIcon(new ImageIcon(im1));
            jLabelSynCarte3.setIcon(new ImageIcon(im2));
            jLabelSynCdr3.setSize(dCar);
            jLabelSynCarte3.setSize(dCar);

            image1 = resourceMap.getImageIcon("fond21.icon");
            im1 = image1.getImage();
            im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            image2 = resourceMap.getImageIcon("fond22.icon");
            im2 = image2.getImage();
            im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            jLabelSynCdr4.setIcon(new ImageIcon(im1));
            jLabelSynCarte4.setIcon(new ImageIcon(im2));
            jLabelSynCdr4.setSize(dCar);
            jLabelSynCarte4.setSize(dCar);

            image1 = resourceMap.getImageIcon("fond23.icon");
            im1 = image1.getImage();
            im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            image2 = resourceMap.getImageIcon("fond24.icon");
            im2 = image2.getImage();
            im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            jLabelSynCdr5.setIcon(new ImageIcon(im1));
            jLabelSynCarte5.setIcon(new ImageIcon(im2));
            jLabelSynCdr5.setSize(dCar);
            jLabelSynCarte5.setSize(dCar);

            image1 = resourceMap.getImageIcon("fond25.icon");
            im1 = image1.getImage();
            im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            image2 = resourceMap.getImageIcon("fond26.icon");
            im2 = image2.getImage();
            im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            jLabelSynCdr6.setIcon(new ImageIcon(im1));
            jLabelSynCarte6.setIcon(new ImageIcon(im2));
            jLabelSynCdr6.setSize(dCar);
            jLabelSynCarte6.setSize(dCar);

            image1 = resourceMap.getImageIcon("fond31.icon");
            im1 = image1.getImage();
            im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            image2 = resourceMap.getImageIcon("fond32.icon");
            im2 = image2.getImage();
            im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            jLabelSynCdr7.setIcon(new ImageIcon(im1));
            jLabelSynCarte7.setIcon(new ImageIcon(im2));
            jLabelSynCdr7.setSize(dCar);
            jLabelSynCarte7.setSize(dCar);

            image1 = resourceMap.getImageIcon("fond33.icon");
            im1 = image1.getImage();
            im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            image2 = resourceMap.getImageIcon("fond34.icon");
            im2 = image2.getImage();
            im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            jLabelSynCdr8.setIcon(new ImageIcon(im1));
            jLabelSynCarte8.setIcon(new ImageIcon(im2));
            jLabelSynCdr8.setSize(dCar);
            jLabelSynCarte8.setSize(dCar);

            image1 = resourceMap.getImageIcon("fond35.icon");
            im1 = image1.getImage();
            im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            image2 = resourceMap.getImageIcon("fond36.icon");
            im2 = image2.getImage();
            im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            jLabelSynCdr9.setIcon(new ImageIcon(im1));
            jLabelSynCarte9.setIcon(new ImageIcon(im2));
            jLabelSynCdr9.setSize(dCar);
            jLabelSynCarte9.setSize(dCar);

            /////////Onglet 3G
            //afficher image Onglet Synthèse
            //ImageIcon im1=resourceMap.getImageIcon("logo_regulateur");
            //ImageIcon image1 = new ImageIcon("./resources/fond11.jpg");
            image1 = resourceMap.getImageIcon("fond11.icon");
            im1 = image1.getImage();
            im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            image2 = resourceMap.getImageIcon("fond12.icon");
            im2 = image2.getImage();
            im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            jLabelSynCdr11.setIcon(new ImageIcon(im1));
            jLabelSynCarte11.setIcon(new ImageIcon(im2));
            jLabelSynCdr11.setSize(dCar);
            jLabelSynCarte11.setSize(dCar);

            image1 = resourceMap.getImageIcon("fond13.icon");
            im1 = image1.getImage();
            im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            image2 = resourceMap.getImageIcon("fond14.icon");
            im2 = image2.getImage();
            im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            jLabelSynCdr21.setIcon(new ImageIcon(im1));
            jLabelSynCarte21.setIcon(new ImageIcon(im2));
            jLabelSynCdr21.setSize(dCar);
            jLabelSynCarte21.setSize(dCar);

            image1 = resourceMap.getImageIcon("fond15.icon");
            im1 = image1.getImage();
            im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            image2 = resourceMap.getImageIcon("fond16.icon");
            im2 = image2.getImage();
            im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            jLabelSynCdr31.setIcon(new ImageIcon(im1));
            jLabelSynCarte31.setIcon(new ImageIcon(im2));
            jLabelSynCdr31.setSize(dCar);
            jLabelSynCarte31.setSize(dCar);

            image1 = resourceMap.getImageIcon("fond21.icon");
            im1 = image1.getImage();
            im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            image2 = resourceMap.getImageIcon("fond22.icon");
            im2 = image2.getImage();
            im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            jLabelSynCdr41.setIcon(new ImageIcon(im1));
            jLabelSynCarte41.setIcon(new ImageIcon(im2));
            jLabelSynCdr41.setSize(dCar);
            jLabelSynCarte41.setSize(dCar);

            image1 = resourceMap.getImageIcon("fond23.icon");
            im1 = image1.getImage();
            im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            image2 = resourceMap.getImageIcon("fond24.icon");
            im2 = image2.getImage();
            im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            jLabelSynCdr51.setIcon(new ImageIcon(im1));
            jLabelSynCarte51.setIcon(new ImageIcon(im2));
            jLabelSynCdr51.setSize(dCar);
            jLabelSynCarte51.setSize(dCar);

            image1 = resourceMap.getImageIcon("fond25.icon");
            im1 = image1.getImage();
            im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            image2 = resourceMap.getImageIcon("fond26.icon");
            im2 = image2.getImage();
            im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            jLabelSynCdr61.setIcon(new ImageIcon(im1));
            jLabelSynCarte61.setIcon(new ImageIcon(im2));
            jLabelSynCdr61.setSize(dCar);
            jLabelSynCarte61.setSize(dCar);

            image1 = resourceMap.getImageIcon("fond31.icon");
            im1 = image1.getImage();
            im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            image2 = resourceMap.getImageIcon("fond32.icon");
            im2 = image2.getImage();
            im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            jLabelSynCdr71.setIcon(new ImageIcon(im1));
            jLabelSynCarte71.setIcon(new ImageIcon(im2));
            jLabelSynCdr71.setSize(dCar);
            jLabelSynCarte71.setSize(dCar);

            image1 = resourceMap.getImageIcon("fond33.icon");
            im1 = image1.getImage();
            im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            image2 = resourceMap.getImageIcon("fond34.icon");
            im2 = image2.getImage();
            im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            jLabelSynCdr81.setIcon(new ImageIcon(im1));
            jLabelSynCarte81.setIcon(new ImageIcon(im2));
            jLabelSynCdr81.setSize(dCar);
            jLabelSynCarte81.setSize(dCar);

            image1 = resourceMap.getImageIcon("fond35.icon");
            im1 = image1.getImage();
            im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            image2 = resourceMap.getImageIcon("fond36.icon");
            im2 = image2.getImage();
            im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
            jLabelSynCdr91.setIcon(new ImageIcon(im1));
            jLabelSynCarte91.setIcon(new ImageIcon(im2));
            jLabelSynCdr91.setSize(dCar);
            jLabelSynCarte91.setSize(dCar);
            
        } catch (Exception e)
        {
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);
            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe Interface_principale:" + DateduJour + " Heure:"+heuredejour+" Problem occured while displaying image:" + e.getMessage(), mes.get_CheminLog());
        }
    }

    private void setImageOngletSynthese(String  chemin,String generation)
    {
        ////afficher image Onglet Synthese
        try
        {
            if (generation.equalsIgnoreCase("2G"))
            {
                ImageIcon image1 = new ImageIcon(chemin + File.separator + "traficBH.jpg");
                Image im1 = image1.getImage();
                im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                ImageIcon image2 = new ImageIcon(chemin + File.separator + "traficBHTR.jpg");
                Image im2 = image2.getImage();
                im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                jLabelSynCdr1.setIcon(new ImageIcon(im1));
                jLabelSynCarte1.setIcon(new ImageIcon(im2));
                jLabelSynCdr1.setSize(dCar);
                jLabelSynCarte1.setSize(dCar);

                image1 = new ImageIcon(chemin + File.separator + "Acces15.jpg");
                im1 = image1.getImage();
                im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                image2 = new ImageIcon(chemin + File.separator + "CarteCSSR.jpg");
                im2 = image2.getImage();
                im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                jLabelSynCdr2.setIcon(new ImageIcon(im1));
                jLabelSynCarte2.setIcon(new ImageIcon(im2));
                jLabelSynCdr2.setSize(dCar);
                jLabelSynCarte2.setSize(dCar);

                image1 = new ImageIcon(chemin + File.separator + "Acces16.jpg");
                im1 = image1.getImage();
                im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                image2 = new ImageIcon(chemin + File.separator + "CarteTCHCRBH.jpg");
                im2 = image2.getImage();
                im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                jLabelSynCdr3.setIcon(new ImageIcon(im1));
                jLabelSynCarte3.setIcon(new ImageIcon(im2));
                jLabelSynCdr3.setSize(dCar);
                jLabelSynCarte3.setSize(dCar);

                image1 = new ImageIcon(chemin + File.separator + "Acces7.jpg");
                im1 = image1.getImage();
                im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                image2 = new ImageIcon(chemin + File.separator + "CarteTCHDRBH.jpg");
                im2 = image2.getImage();
                im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                jLabelSynCdr4.setIcon(new ImageIcon(im1));
                jLabelSynCarte4.setIcon(new ImageIcon(im2));
                jLabelSynCdr4.setSize(dCar);
                jLabelSynCarte4.setSize(dCar);

                image1 = new ImageIcon(chemin + File.separator + "Maintien3.jpg");
                im1 = image1.getImage();
                im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                image2 = new ImageIcon(chemin + File.separator + "CarteCSRBH.jpg");
                im2 = image2.getImage();
                im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                jLabelSynCdr5.setIcon(new ImageIcon(im1));
                jLabelSynCarte5.setIcon(new ImageIcon(im2));
                jLabelSynCdr5.setSize(dCar);
                jLabelSynCarte5.setSize(dCar);

                image1 = new ImageIcon(chemin + File.separator + "Maintien4.jpg");
                im1 = image1.getImage();
                im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                image2 = new ImageIcon(chemin + File.separator + "CarteCDRBH.jpg");
                im2 = image2.getImage();
                im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                jLabelSynCdr6.setIcon(new ImageIcon(im1));
                jLabelSynCarte6.setIcon(new ImageIcon(im2));
                jLabelSynCdr6.setSize(dCar);
                jLabelSynCarte6.setSize(dCar);

                image1 = new ImageIcon(chemin + File.separator + "HO1.jpg");
                im1 = image1.getImage();
                im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                image2 = new ImageIcon(chemin + File.separator + "CarteHOSUCCES.jpg");
                im2 = image2.getImage();
                im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                jLabelSynCdr7.setIcon(new ImageIcon(im1));
                jLabelSynCarte7.setIcon(new ImageIcon(im2));
                jLabelSynCdr7.setSize(dCar);
                jLabelSynCarte7.setSize(dCar);

                image1 = new ImageIcon(chemin + File.separator + "HO3.jpg");
                im1 = image1.getImage();
                im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                image2 = new ImageIcon(chemin + File.separator + "CarteHOULQR.jpg");
                im2 = image2.getImage();
                im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                jLabelSynCdr8.setIcon(new ImageIcon(im1));
                jLabelSynCarte8.setIcon(new ImageIcon(im2));
                jLabelSynCdr8.setSize(dCar);
                jLabelSynCarte8.setSize(dCar);

                image1 = new ImageIcon(chemin + File.separator + "SMS2.jpg");
                im1 = image1.getImage();
                im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                image2 = new ImageIcon(chemin + File.separator + "CarteSMSLR.jpg");
                im2 = image2.getImage();
                im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                jLabelSynCdr9.setIcon(new ImageIcon(im1));
                jLabelSynCarte9.setIcon(new ImageIcon(im2));
                jLabelSynCdr9.setSize(dCar);
                jLabelSynCarte9.setSize(dCar);
            } else if (generation.equalsIgnoreCase("3G"))
            {
                ImageIcon image1 = new ImageIcon(chemin + File.separator + "traficBH.jpg");
                Image im1 = image1.getImage();
                im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                ImageIcon image2 = new ImageIcon(chemin + File.separator + "traficBHTR.jpg");
                Image im2 = image2.getImage();
                im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                jLabelSynCdr11.setIcon(new ImageIcon(im1));
                jLabelSynCarte11.setIcon(new ImageIcon(im2));
                jLabelSynCdr1.setSize(dCar);
                jLabelSynCarte11.setSize(dCar);

                image1 = new ImageIcon(chemin + File.separator + "Acces15.jpg");
                im1 = image1.getImage();
                im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                image2 = new ImageIcon(chemin + File.separator + "CarteCSSR.jpg");
                im2 = image2.getImage();
                im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                jLabelSynCdr21.setIcon(new ImageIcon(im1));
                jLabelSynCarte21.setIcon(new ImageIcon(im2));
                jLabelSynCdr21.setSize(dCar);
                jLabelSynCarte21.setSize(dCar);

                image1 = new ImageIcon(chemin + File.separator + "Acces16.jpg");
                im1 = image1.getImage();
                im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                image2 = new ImageIcon(chemin + File.separator + "CarteTCHCRBH.jpg");
                im2 = image2.getImage();
                im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                jLabelSynCdr31.setIcon(new ImageIcon(im1));
                jLabelSynCarte31.setIcon(new ImageIcon(im2));
                jLabelSynCdr31.setSize(dCar);
                jLabelSynCarte31.setSize(dCar);

                image1 = new ImageIcon(chemin + File.separator + "Acces7.jpg");
                im1 = image1.getImage();
                im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                image2 = new ImageIcon(chemin + File.separator + "CarteTCHDRBH.jpg");
                im2 = image2.getImage();
                im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                jLabelSynCdr41.setIcon(new ImageIcon(im1));
                jLabelSynCarte41.setIcon(new ImageIcon(im2));
                jLabelSynCdr41.setSize(dCar);
                jLabelSynCarte41.setSize(dCar);

                image1 = new ImageIcon(chemin + File.separator + "Maintien3.jpg");
                im1 = image1.getImage();
                im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                image2 = new ImageIcon(chemin + File.separator + "CarteCSRBH.jpg");
                im2 = image2.getImage();
                im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                jLabelSynCdr51.setIcon(new ImageIcon(im1));
                jLabelSynCarte51.setIcon(new ImageIcon(im2));
                jLabelSynCdr51.setSize(dCar);
                jLabelSynCarte51.setSize(dCar);

                image1 = new ImageIcon(chemin + File.separator + "Maintien4.jpg");
                im1 = image1.getImage();
                im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                image2 = new ImageIcon(chemin + File.separator + "CarteCDRBH.jpg");
                im2 = image2.getImage();
                im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                jLabelSynCdr61.setIcon(new ImageIcon(im1));
                jLabelSynCarte61.setIcon(new ImageIcon(im2));
                jLabelSynCdr61.setSize(dCar);
                jLabelSynCarte61.setSize(dCar);

                image1 = new ImageIcon(chemin + File.separator + "HO1.jpg");
                im1 = image1.getImage();
                im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                image2 = new ImageIcon(chemin + File.separator + "CarteHOSUCCES.jpg");
                im2 = image2.getImage();
                im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                jLabelSynCdr71.setIcon(new ImageIcon(im1));
                jLabelSynCarte71.setIcon(new ImageIcon(im2));
                jLabelSynCdr71.setSize(dCar);
                jLabelSynCarte71.setSize(dCar);

                image1 = new ImageIcon(chemin + File.separator + "HO3.jpg");
                im1 = image1.getImage();
                im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                image2 = new ImageIcon(chemin + File.separator + "CarteHOULQR.jpg");
                im2 = image2.getImage();
                im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                jLabelSynCdr81.setIcon(new ImageIcon(im1));
                jLabelSynCarte81.setIcon(new ImageIcon(im2));
                jLabelSynCdr81.setSize(dCar);
                jLabelSynCarte81.setSize(dCar);

                image1 = new ImageIcon(chemin + File.separator + "SMS2.jpg");
                im1 = image1.getImage();
                im1 = im1.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                image2 = new ImageIcon(chemin + File.separator + "CarteSMSLR.jpg");
                im2 = image2.getImage();
                im2 = im2.getScaledInstance(dCar.width, dCar.height, Image.SCALE_DEFAULT);
                jLabelSynCdr91.setIcon(new ImageIcon(im1));
                jLabelSynCarte91.setIcon(new ImageIcon(im2));
                jLabelSynCdr91.setSize(dCar);
                jLabelSynCarte91.setSize(dCar);
            }
        }
        catch (Exception ex)
        {
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);
            formatter =new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe InterfacePrincipale,méthode:setImageOngletAccessibilite:" + DateduJour + " Heure:"+heuredejour+" Erreur: " + ex.getMessage(), mes.get_CheminLog());
        }
    }

    private void setImageOngletTrafic(String chemin,String generation)
    {
        //afficher image Onglet Trafic
        try
        {
            if (generation.equalsIgnoreCase("2G"))
            {
                ImageIcon image1 = new ImageIcon(chemin + File.separator + "trafic1.jpg");
                Image imlabel1 = image1.getImage();
                imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                jLabelTr1.setIcon(new ImageIcon(imlabel1));

                image1 = new ImageIcon(chemin + File.separator + "trafic2.jpg");
                imlabel1 = image1.getImage();
                imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                jLabelTr2.setIcon(new ImageIcon(imlabel1));

                image1 = new ImageIcon(chemin + File.separator + "Acces1.jpg");
                imlabel1 = image1.getImage();
                imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                jLabelTr3.setIcon(new ImageIcon(imlabel1));
                //jPanelTrImage.setSize(dRect.width+50, dRect.height*3+10);
            } else if (generation.equalsIgnoreCase("3G"))
            {
                ImageIcon image1 = new ImageIcon(chemin + File.separator + "trafic1.jpg");
                Image imlabel1 = image1.getImage();
                imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                jLabelTr11.setIcon(new ImageIcon(imlabel1));

                image1 = new ImageIcon(chemin + File.separator + "trafic2.jpg");
                imlabel1 = image1.getImage();
                imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                jLabelTr21.setIcon(new ImageIcon(imlabel1));

                image1 = new ImageIcon(chemin + File.separator + "Acces1.jpg");
                imlabel1 = image1.getImage();
                imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                jLabelTr31.setIcon(new ImageIcon(imlabel1));
                //jPanelTrImage.setSize(dRect.width+50, dRect.height*3+10);
            }
        } catch (Exception e)
        {
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);
            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe Interface_principale:" + DateduJour + " Heure:"+heuredejour+" Problem occured while displaying image:" + e.getMessage(), mes.get_CheminLog());
        }
    }

    private void setImageOngletTraficDefaut()
    {
        ////afficher image Onglet Trafic
        try
        {
            jLabelTr1.setIcon(new ImageIcon());
            jLabelTr2.setIcon(new ImageIcon());
            jLabelTr3.setIcon(new ImageIcon());

            jLabelTr11.setIcon(new ImageIcon());
            jLabelTr21.setIcon(new ImageIcon());
            jLabelTr31.setIcon(new ImageIcon());
            //jPanelTrImage.setSize(dRect.width+50, dRect.height*3+10);
        } catch (Exception e)
        {
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);
            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe Interface_principale:" + DateduJour + " Heure:"+heuredejour+" Problem occured while displaying image:" + e.getMessage(), mes.get_CheminLog());
        }
    }

    private void setImageOngletAccessibilite(String chemin,String generation)
    {
        /////afficher image Onglet Canal TCH
        try
        {
            if (generation.equalsIgnoreCase("2G"))
            {
                ImageIcon image1 = new ImageIcon(chemin + File.separator + "Acces14.jpg");
                Image imlabel1 = image1.getImage();
                imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                jLabelCanTCH1.setIcon(new ImageIcon(imlabel1));

                image1 = new ImageIcon(chemin + File.separator + "Acces2.jpg");
                imlabel1 = image1.getImage();
                imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                jLabelCanTCH2.setIcon(new ImageIcon(imlabel1));

                image1 = new ImageIcon(chemin + File.separator + "Acces9.jpg");
                imlabel1 = image1.getImage();
                imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                jLabelCanTCH3.setIcon(new ImageIcon(imlabel1));
            } else  if (generation.equalsIgnoreCase("3G"))
            {
                ImageIcon image1 = new ImageIcon(chemin + File.separator + "Acces14.jpg");
                Image imlabel1 = image1.getImage();
                imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                jLabelCanTCH11.setIcon(new ImageIcon(imlabel1));

                image1 = new ImageIcon(chemin + File.separator + "Acces2.jpg");
                imlabel1 = image1.getImage();
                imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                jLabelCanTCH21.setIcon(new ImageIcon(imlabel1));

                image1 = new ImageIcon(chemin + File.separator + "Acces9.jpg");
                imlabel1 = image1.getImage();
                imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                jLabelCanTCH31.setIcon(new ImageIcon(imlabel1));
            }
        } catch (Exception e)
        {
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);
            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe Interface_principale:" + DateduJour + " Heure:"+heuredejour+" Problem occured while displaying image:" + e.getMessage(), mes.get_CheminLog());
        }
    }

    private void setImageOngletAccessibiliteDefault()
    {
        /////afficher image Onglet Canal TCH
        try
        {
            jLabelCanTCH1.setIcon(new ImageIcon());
            jLabelCanTCH2.setIcon(new ImageIcon());
            jLabelCanTCH3.setIcon(new ImageIcon());

            jLabelCanTCH11.setIcon(new ImageIcon());
            jLabelCanTCH21.setIcon(new ImageIcon());
            jLabelCanTCH31.setIcon(new ImageIcon());
        } catch (Exception e)
        {
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);
            formatter =new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe InterfacePrincipale,méthode:setImageOngletAccessibilite:" + DateduJour + " Heure:"+heuredejour+" Erreur: " + e.getMessage(), mes.get_CheminLog());
        }
    }

    private  void setImageOngletMaintenabilite(String chemin,String generation)
    {
        /////////////////////////////////////
        //afficher image Onglet Canal Maintenabilité
        try
        {
            if (generation.equalsIgnoreCase("2G"))
            {
                ImageIcon image1 = new ImageIcon();
                image1 = new ImageIcon(chemin + File.separator + "Maintien1.jpg");
                Image imlabel1 = image1.getImage();
                imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                jLabelMaint1.setIcon(new ImageIcon(imlabel1));

                image1 = new ImageIcon(chemin + File.separator + "Maintien2.jpg");
                imlabel1 = image1.getImage();
                imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                jLabelMaint2.setIcon(new ImageIcon(imlabel1));

                image1 = new ImageIcon(chemin + File.separator + "Maintien5.jpg");
                imlabel1 = image1.getImage();
                imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                jLabelMaint3.setIcon(new ImageIcon(imlabel1));
            }
            else if (generation.equalsIgnoreCase("3G"))
            {
                ImageIcon image1 = new ImageIcon();
                image1 = new ImageIcon(chemin + File.separator + "Maintien1.jpg");
                Image imlabel1 = image1.getImage();
                imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                jLabelMaint1.setIcon(new ImageIcon(imlabel1));

                image1 = new ImageIcon(chemin + File.separator + "Maintien2.jpg");
                imlabel1 = image1.getImage();
                imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                jLabelMaint2.setIcon(new ImageIcon(imlabel1));

                image1 = new ImageIcon(chemin + File.separator + "Maintien5.jpg");
                imlabel1 = image1.getImage();
                imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                jLabelMaint3.setIcon(new ImageIcon(imlabel1));
            }
        } catch (Exception e)
        {
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);
            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe Interface_principale:" + DateduJour + " Heure:"+heuredejour+" Problem occured while displaying image:" + e.getMessage(), mes.get_CheminLog());
        }
    }

    private  void setImageOngletMaintenabiliteDefault()
    {
        //afficher image Onglet Canal Maintenabilité
        try
        {
            jLabelMaint1.setIcon(new ImageIcon());
            jLabelMaint2.setIcon(new ImageIcon());
            jLabelMaint3.setIcon(new ImageIcon());

            jLabelMaint11.setIcon(new ImageIcon());
            jLabelMaint11.setIcon(new ImageIcon());
            jLabelMaint31.setIcon(new ImageIcon());
        } catch (Exception e)
        {
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);
            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe Interface_principale:" + DateduJour + " Heure:"+heuredejour+" Problem occured while displaying image:" + e.getMessage(), mes.get_CheminLog());
        }
    }

    private  void setImageOngletHandover(String chemin,String generation)
    {
        try
        {
            if (generation.equalsIgnoreCase("2G"))
            {
                //afficher image Onglet Canal TCH
                ImageIcon image1 = new ImageIcon();
                image1 = new ImageIcon(chemin + File.separator + "HO4.jpg");
                Image imlabel1 = image1.getImage();
                imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                jLabelHO1.setIcon(new ImageIcon(imlabel1));
                //jLabelSyn1.setIcon(image1);

                image1 = new ImageIcon(chemin + File.separator + "HO5.jpg");
                imlabel1 = image1.getImage();
                imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                jLabelHO2.setIcon(new ImageIcon(imlabel1));
                //jLabelSyn2.setIcon(image1);

                image1 = new ImageIcon(chemin + File.separator + "HO6.jpg");
                imlabel1 = image1.getImage();
                imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                jLabelHO3.setIcon(new ImageIcon(imlabel1));
            }
            else if (generation.equalsIgnoreCase("3G"))
            {
                //afficher image Onglet Canal TCH
                ImageIcon image1 = new ImageIcon();
                image1 = new ImageIcon(chemin + File.separator + "HO4.jpg");
                Image imlabel1 = image1.getImage();
                imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                jLabelHO1.setIcon(new ImageIcon(imlabel1));
                //jLabelSyn1.setIcon(image1);

                image1 = new ImageIcon(chemin + File.separator + "HO5.jpg");
                imlabel1 = image1.getImage();
                imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                jLabelHO2.setIcon(new ImageIcon(imlabel1));
                //jLabelSyn2.setIcon(image1);

                image1 = new ImageIcon(chemin + File.separator + "HO6.jpg");
                imlabel1 = image1.getImage();
                imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                jLabelHO3.setIcon(new ImageIcon(imlabel1));
            }
        } catch (Exception e)
        {
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);
            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe Interface_principale:" + DateduJour + " Heure:"+heuredejour+" Problem occured while displaying image:" + e.getMessage(), mes.get_CheminLog());
        }
    }

    private  void setImageOngletHandoverDefault()
    {
        try
        {
            //afficher image Onglet Canal TCH
            ImageIcon image1 = new ImageIcon();
            image1 = new ImageIcon();
            Image imlabel1 = image1.getImage();
            jLabelHO1.setIcon(new ImageIcon());
            //jLabelSyn1.setIcon(image1);

            image1 = new ImageIcon();
            imlabel1 = image1.getImage();
            jLabelHO2.setIcon(new ImageIcon());
            //jLabelSyn2.setIcon(image1);

            image1 = new ImageIcon();
            imlabel1 = image1.getImage();
            jLabelHO3.setIcon(new ImageIcon());

            //afficher image Onglet Canal TCH 3G
            image1 = new ImageIcon();
            image1 = new ImageIcon();
            imlabel1 = image1.getImage();
            jLabelHO11.setIcon(new ImageIcon());
            //jLabelSyn1.setIcon(image1);

            image1 = new ImageIcon();
            imlabel1 = image1.getImage();
            jLabelHO21.setIcon(new ImageIcon());
            //jLabelSyn2.setIcon(image1);

            image1 = new ImageIcon();
            imlabel1 = image1.getImage();
            jLabelHO31.setIcon(new ImageIcon());
        } catch (Exception e)
        {
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);
            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe Interface_principale:" + DateduJour + " Heure:"+heuredejour+" Problem occured while displaying image:" + e.getMessage(), mes.get_CheminLog());
        }
    }

    private  void setImageOngletSMS(String chemin,String generation)
    {
        ////afficher image Onglet Canal TCH
        try
        {
            if (generation.equalsIgnoreCase("2G"))
            {
                ImageIcon image1 = new ImageIcon();
                image1 = new ImageIcon(chemin + File.separator + "SMS3.jpg");
                Image imlabel1 = image1.getImage();
                imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                jLabelSMS1.setIcon(new ImageIcon(imlabel1));
            } else if (generation.equalsIgnoreCase("3G"))
            {
                ImageIcon image1 = new ImageIcon();
                image1 = new ImageIcon(chemin + File.separator + "SMS3.jpg");
                Image imlabel1 = image1.getImage();
                imlabel1 = imlabel1.getScaledInstance(dRect.width, dRect.height, Image.SCALE_DEFAULT);
                jLabelSMS1.setIcon(new ImageIcon(imlabel1));
            }
        } catch (Exception ex)
        {
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);
            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Class Interface:"+DateduJour+" Heure:"+heuredejour+" Error while getting images from directory: "+ex.getMessage(), mes.get_CheminLog());
        }
    }

    private  void setImageOngletSMSDefault()
    {
        ////afficher image Onglet Canal TCH
        try
        {
            jLabelSMS1.setIcon(new ImageIcon());
            jLabelSMS11.setIcon(new ImageIcon());
        } catch (Exception ex)
        {
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);
            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Class Interface:"+DateduJour+" Heure:"+heuredejour+" Error while getting images from directory: "+ex.getMessage(), mes.get_CheminLog());
        }
    }

    public static void main(String[] arg)
    {
        Interface_Principale itf=new Interface_Principale();
        itf.setVisible(true);
    }
}
