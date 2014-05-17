
package services;

import java.util.List;
import modele.Auteur;
import modele.Ouvrage;

public interface ServicesAuteur
{
    public void referencerAuteur(Auteur a);
    public List<Auteur> listerTousLesAuteurs();
    public List<Ouvrage> listerTousOuvragesDunAuteur(Auteur a);
    public List<Ouvrage> listerTousOuvragesDunAuteur(String a);
}
