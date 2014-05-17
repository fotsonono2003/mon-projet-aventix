package projet_consultation.genererCartes;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jfree.ui.ApplicationFrame;
import projet_consultation.ClassesGenerales.Fichier;
 
public class Thumbnail extends ApplicationFrame
{
    private static final long serialVersionUID = 1L;

    public Thumbnail(String title, String cheminfichierlog, String lien1, String lien2, String lien3, String lien4, String lien5, String lien6)
    {
        super(title);
        JPanel chartPanel = createDemoPanel(title, cheminfichierlog, lien1, lien2, lien3, lien4, lien5, lien6);
        chartPanel.setPreferredSize(new Dimension(1250, 850));
        setContentPane(chartPanel);
        this.setDefaultCloseOperation(ApplicationFrame.DISPOSE_ON_CLOSE);
        this.dispose();
    }

    private JPanel createDemoPanel(String title, String cheminfichierlog, String lien1, String lien2, String lien3, String lien4, String lien5, String lien6)
    {
        JPanel mainPanel = new JPanel(new GridLayout(2, 3));
        mainPanel.setSize(960, 660);
        ImageIcon image1 = new ImageIcon(lien1);
        mainPanel.add(new JLabel(image1));
        ImageIcon image2 = new ImageIcon(lien2);
        mainPanel.add(new JLabel(image2));
        ImageIcon image3 = new ImageIcon(lien3);
        mainPanel.add(new JLabel(image3));
        ImageIcon image4 = new ImageIcon(lien4);
        mainPanel.add(new JLabel(image4));
        ImageIcon image5 = new ImageIcon(lien5);
        mainPanel.add(new JLabel(image5));
        ImageIcon image6 = new ImageIcon(lien6);
        mainPanel.add(new JLabel(image6));
        JFrame frm = new JFrame();
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setSize(960, 660);
        //frm.setLocation(-1000, 0);
        frm.setContentPane(mainPanel);
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException e1)
        {
            Logger.getLogger(Thumbnail.class.getName()).log(Level.SEVERE, null, e1);
        }
        BufferedImage image = new BufferedImage(940, 620, BufferedImage.SCALE_DEFAULT);
        Graphics2D g2 = image.createGraphics();
        mainPanel.paint(g2);
        g2.dispose();
        frm.dispose();
        try
        { 
            ImageIO.write(image, "JPG", new File(title));
        } catch (Exception e)
        {
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour = formatter.format(date);

            formatter = new SimpleDateFormat("kk:mm");
            String heuredejour = formatter.format(date);
            Fichier fichier = new Fichier();
            fichier.ecrire("Classe Thumbnail:" + DateduJour + " Heure:" + heuredejour + " Problem occurred creating Image:" + e.getMessage(), cheminfichierlog);
            Logger.getLogger(Thumbnail.class.getName()).log(Level.SEVERE, null, e);
        }
        return mainPanel;
    }
}
