package services;

import java.util.List;
import modele.Ouvrage;

public interface ServicesOuvrage
{
    public void referencerOuvrage(Ouvrage o);
    public Ouvrage trouverOuvrageParNumero(Long numero);
    public List<Ouvrage> listerTousLesOuvrages();
    public void modifierOuvrage(Ouvrage o);
    public void dereferencerOuvrage(Ouvrage o);
}
