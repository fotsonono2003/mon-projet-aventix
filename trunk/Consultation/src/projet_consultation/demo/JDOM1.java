package projet_consultation.demo;

import java.io.*;
import org.jdom.*;
import org.jdom.output.*;

public class JDOM1
{
   //Nous allons commencer notre arborescence en créant la racine XML
   //qui sera ici "personnes".
   static Element racine = new Element("options");

   //On crée un nouveau Document JDOM basé sur la racine que l'on vient de créer
   static org.jdom.Document document = new Document(racine);

    public static void main(String[] args)
    {
        
        Element param = new Element("option");

        Element passwd = new Element("logo_operateur");
        passwd.setText("bi4t");

        Element login = new Element("login");
        login.setText("admin");

        Element adresseIP = new Element("adresseIP");
        adresseIP.setText("192.168.1.11");

        Element pilote = new Element("pilote");
        pilote.setText("org.postgresql.Driver");

        Element bdd = new Element("bdd");
        bdd.setText("geoserver");

        Element port = new Element("port");
        port.setText("5432");

        param.addContent(login);
        param.addContent(passwd);
        param.addContent(adresseIP);
        param.addContent(pilote);
        param.addContent(port);
        param.addContent(bdd);

        racine.addContent(param);
        //On crée un nouvel Attribut classe et on l'ajoute à etudiant
        //grâce à la méthode setAttribute
        //Attribute classe = new Attribute("classe","P2");
        //passwd.setAttribute(classe);

        //On crée un nouvel Element nom, on lui assigne du texte
        //et on l'ajoute en tant qu'Element de etudiant
//        Element login = new Element("login");
//        login.setText("postgres");
//        passwd.addContent(login);

        //Les deux méthodes qui suivent seront définies plus loin dans l'article
        affiche();
        //enregistre("./src/projet_consultation/Fichiers_parametres/parametreconnexion.xml");
        enregistre("./src/projet_consultation/Fichiers_parametres/option.xml");
    }

    //Ajouter ces deux méthodes à notre classe JDOM1
    static void affiche()
    {
       try
       {
          //On utilise ici un affichage classique avec getPrettyFormat()
          XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
          sortie.output(document, System.out);
       }
       catch (java.io.IOException e)
       {
           e.printStackTrace();
       }
    }

    static void enregistre(String fichier)
    {
       try
       {
          //On utilise ici un affichage classique avec getPrettyFormat()
          XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
          //Remarquez qu'il suffit simplement de créer une instance de FileOutputStream
          //avec en argument le nom du fichier pour effectuer la sérialisation.
          sortie.output(document, new FileOutputStream(fichier));
       }
       catch (java.io.IOException e)
       {
           e.printStackTrace();
       }
    }

}