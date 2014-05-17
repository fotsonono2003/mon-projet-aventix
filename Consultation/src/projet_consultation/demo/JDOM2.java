package  projet_consultation.demo;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import org.jdom.*;
import org.jdom.input.*;

public class JDOM2
{
   static org.jdom.Document document;
   static Element racine;

   public static void main(String[] args)
   {
      //On crée une instance de SAXBuilder
      SAXBuilder sxb = new SAXBuilder();
      try
      {
         //On crée un nouveau document JDOM avec en argument le fichier XML
         //Le parsing est terminé ;)
         document = sxb.build(new File("./src/projet_consultation/Fichiers_parametres/parametreconnexion.xml"));
      }
      catch(Exception e)
      {
          e.printStackTrace();
      }

      //On initialise un nouvel élément racine avec l'élément racine du document.
      racine = document.getRootElement();

      //Méthode définie dans la partie 3.2. de cet article
      afficheALL();
   }

    //Ajouter cette méthodes à la classe JDOM2
    static void afficheALL()
    {
       //On crée une List contenant tous les noeuds "etudiant" de l'Element racine
       List listEtudiants = racine.getChildren("param");

       //On crée un Iterator sur notre liste
       Iterator i = listEtudiants.iterator();
       while(i.hasNext())
       {
          //On recrée l'Element courant à chaque tour de boucle afin de
          //pouvoir utiliser les méthodes propres aux Element comme :
          //sélectionner un nœud fils, modifier du texte, etc...
          Element courant = (Element)i.next();
          //On affiche le nom de l’élément courant
          System.out.println(courant.getChild("login").getText());
          System.out.println(courant.getChild("password").getText());
          System.out.println(courant.getChild("adresseIP").getText());
          System.out.println(courant.getChild("port").getText());
          System.out.println(courant.getChild("pilote").getText());
          System.out.println(courant.getChild("bdd").getText());
       }
    }
    
}