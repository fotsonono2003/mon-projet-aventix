
package projet_consultation.demo;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;

public class testdesktop extends JDesktopPane
{

    @Override
    public void paintComponents(Graphics g)
    {
        Image img = new ImageIcon(this.getClass().getResource("/resources/fond.jpg")).getImage();
        try
        {
            Graphics2D g2d = (Graphics2D) g;
            double x = img.getWidth(null);
            double y = img.getHeight(null);
            g2d.scale(getWidth() / x, getHeight() / y);
            g2d.drawImage(img, 0, 0, this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
