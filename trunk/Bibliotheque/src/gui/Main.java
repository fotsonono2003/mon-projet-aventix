package gui;

import dao.OuvrageDAO;
import modele.Auteur;
import modele.Emprunteur;
import modele.Ouvrage;
import services.ServicesImpl;

public class Main
{
    public static void main(String[] args)
    {
        /*Ouvrage o1=new Ouvrage("Mon roman 1",2001);
        Ouvrage o2=new Ouvrage("Mon roman 2",2002);
        Ouvrage o3=new Ouvrage("Mon roman 3",2003);
        Ouvrage o4=new Ouvrage("Mon roman 4",2004);
        Ouvrage o5=new Ouvrage("Mon roman 5",2005);

        o4.setAnneeDeParution(2005);
         *
         */

        /*OuvrageDAO oDAO=new OuvrageDAO();
        oDAO.CreateOuvrage(o1);
        oDAO.CreateOuvrage(o2);
        oDAO.CreateOuvrage(o3);
        oDAO.CreateOuvrage(o4);
        oDAO.CreateOuvrage(o5);
        
        System.out.println(oDAO.findAllOuvrage());
        Ouvrage o =oDAO.findOuvragebyId(new Long(3));
        System.out.println(o);

        o2.setAnneeDeParution(2009);
        oDAO.updateOuvrage(o2);

        System.out.println(oDAO.findAllOuvrage());
        */
        
        /*ServicesImpl services=new ServicesImpl();
        services.referencerOuvrage(o1);
        services.referencerOuvrage(o2);
        services.referencerOuvrage(o3);
        services.referencerOuvrage(o4);
        services.referencerOuvrage(o5);

        System.out.println(services.listerTousLesOuvrages());
        Ouvrage o=services.trouverOuvrageParNumero(new Long(3));
        System.out.println(o);

        o2.setAnneeDeParution(2009);
        services.modifierOuvrage(o2);
        System.out.println(services.listerTousLesOuvrages());
         *
         */

        Auteur a1=new Auteur("Victor Hugo");
        Auteur a2=new Auteur("Emile Zola");

        Ouvrage o1=new Ouvrage("Notre dame de paris",1981);
        Ouvrage o2=new Ouvrage("Germinal",1885,a2);
        Ouvrage o3=new Ouvrage("Les miserables",1862);
        Ouvrage o4=new Ouvrage("La petite fadette",2004,new Auteur("George Sand"));

        a1.addOuvrage(o1);
        a1.addOuvrage(o3);

        ServicesImpl servicesImpl=new ServicesImpl();
        servicesImpl.referencerAuteur(a1);
        servicesImpl.referencerOuvrage(o2);
        servicesImpl.referencerOuvrage(o4);

        System.out.println(servicesImpl.listerTousLesAuteurs());
        System.out.println(servicesImpl.listerTousOuvragesDunAuteur(a1));
        System.out.println(servicesImpl.listerTousOuvragesDunAuteur(a2));
        System.out.println(servicesImpl.listerTousOuvragesDunAuteur("George Sand"));

        Emprunteur e1=new Emprunteur("Jean","22 rue ici");
        Emprunteur e2=new Emprunteur("Stephanie","35 rue la");
        Emprunteur e3=new Emprunteur("Clementine","27 rue par ici");

        servicesImpl.referencerEmprunteur(e1);
        servicesImpl.referencerEmprunteur(e2);

        servicesImpl.preterOuvrage(e2, o4);
        servicesImpl.preterOuvrage(e3, o2);
        servicesImpl.preterOuvrage(new Emprunteur("claire", "12 rue en bas"), o1);
    }

}
