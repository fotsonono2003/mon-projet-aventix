package projet_consultation.GenererRapport;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import projet_consultation.ConnexionBDD.Connexion_BDDGenerale;
import projet_consultation.ClassesGenerales.Fichier;
 
public class Envoirapport
{  
    Transport transport=null;
    Session session =null; 

    public void envoyermail(String cheminfichierlog,String serveur,int port,String mailfrom,String password,String mailto,String cheminrapport) throws SQLException
    {
        Connexion_BDDGenerale cn = Connexion_BDDGenerale.getInstance();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("kk:mm");
        Fichier fichier = new Fichier();

        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        //prop.put( "mail.smtp.host",serveur);
        properties.put("mail.smtp.port",port);

        try
        {
            session = Session.getDefaultInstance(properties, null);
            transport = session.getTransport();
            transport.connect(serveur, mailfrom, password);

            Message message = new MimeMessage(session);
            message.setSubject("Rapport");
            message.setText("Test Text de Java ");

            InternetAddress internetAddresses = new InternetAddress();
            //internetAddresses = new InternetAddress(listemail.elementAt(dest));
            internetAddresses = new InternetAddress(mailto);
            message.addRecipient(Message.RecipientType.TO, internetAddresses);
            message.setFrom(new InternetAddress(mailfrom));

            MimeBodyPart mbp = new MimeBodyPart();
            mbp.attachFile(new File(cheminrapport));
            MimeMultipart mp = new MimeMultipart();
            mp.addBodyPart(mbp);
            message.setContent(mp);
            //Transport.send(message);
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();
            cn.StopConn();
        }
        catch (AddressException e1)
        {
            Logger.getLogger(Envoirapport.class.getName()).log(Level.SEVERE, null, e1);
            formatter = new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour = formatter.format(date);

            formatter = new SimpleDateFormat("kk:mm");
            String heuredejour = formatter.format(date);
            fichier.ecrire("Classe Envoirapport:" + DateduJour + " Heure:" + heuredejour + " adresse invalide:" + e1.getMessage(), cheminfichierlog);
        }
        catch (MessagingException e1)
        {
            Logger.getLogger(Envoirapport.class.getName()).log(Level.SEVERE, null, e1);
            formatter = new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour = formatter.format(date);

            formatter = new SimpleDateFormat("kk:mm");
            String heuredejour = formatter.format(date);
            fichier.ecrire("Classe Envoirapport:" + DateduJour + " Heure:" + heuredejour + " Erreur:" + e1.getMessage(), cheminfichierlog);
        }
        catch (IOException e)
        {
            Logger.getLogger(Envoirapport.class.getName()).log(Level.SEVERE, null, e);
            formatter = new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour = formatter.format(date);

            formatter = new SimpleDateFormat("kk:mm");
            String heuredejour = formatter.format(date);
            fichier.ecrire("Classe Envoirapport:" + DateduJour + " Heure:" + heuredejour + " Le fichier à envoyer n'existe pas:" + e.getMessage(), cheminfichierlog);
        }
    }  
}