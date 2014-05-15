package Services;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.internet.*;
import javax.mail.*;

import java.io.File;
import java.util.*;

/**
 * Classe permettant d'envoyer un mail.
 */
public class EnvoiMail
{
   private final static String MAILER_VERSION = "Java";
   public static boolean envoyerMailSMTP(boolean debug,String objet,String contenu,String to,File file) 
   {
         boolean result = false;
         try
         {
			Properties prop = System.getProperties();
			prop.put("mail.smtp.host", "smtp.insa-lyon.fr");
			Session session = Session.getDefaultInstance(prop, null);

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("thierry.fotjo-nono@insa-lyon.fr"));
			InternetAddress[] internetAddresses = new InternetAddress[1];
			internetAddresses[0] = new InternetAddress(to);
			message.setRecipients(Message.RecipientType.TO, internetAddresses);
			message.setSubject(objet);
			message.setText(contenu);
			message.setHeader("X-Mailer", MAILER_VERSION);
			message.setSentDate(new Date());
			session.setDebug(debug);
			if (file != null) 
			{
				Multipart multipart = new MimeMultipart();

				// creation partie principale du message
				BodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setText("Test");
				multipart.addBodyPart(messageBodyPart);

				// creation et ajout de la piece jointe
				messageBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(file);
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName("image.gif");
				multipart.addBodyPart(messageBodyPart);

				// ajout des éléments au mail
				message.setContent(multipart);
			}
			Transport.send(message);
			result = true;
         }
         catch (AddressException e) 
         {
        	 e.printStackTrace();
         } 
         catch (MessagingException e)
         {
                  e.printStackTrace();
         }
         return result;
   }
   
   public static void main(String[] args) {
         //EnvoiMail.envoyerMailSMTP(true,"fotsonono2003@yahoo.fr");
   }
}