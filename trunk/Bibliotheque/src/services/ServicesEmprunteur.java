
package services;

import modele.Emprunteur;
import modele.Ouvrage;

public interface ServicesEmprunteur
{
    public void referencerEmprunteur(Emprunteur e);
    public void preterOuvrage(Emprunteur e,Ouvrage o);
}
