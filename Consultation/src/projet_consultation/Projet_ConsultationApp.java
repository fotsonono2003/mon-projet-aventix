package projet_consultation;

import javax.swing.JFrame;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import projet_consultation.Principale.JFrame_connexion;

public class Projet_ConsultationApp extends SingleFrameApplication
{
 
    @Override protected void startup()
    {
        JFrame FrmConnexion=new JFrame_connexion();
        FrmConnexion.setVisible(true);
    }
 
    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of Projet_ConsultationApp
     */
    public static Projet_ConsultationApp getApplication() {
        return Application.getInstance(Projet_ConsultationApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(Projet_ConsultationApp.class, args);
    }
}
