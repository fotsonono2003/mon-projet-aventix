package Services;

import modele.Employe;
import modele.Employeur;

public interface ServicesEmployeur 
{
    public void AddEmployeur(Employeur employeur);
    
    public void AddEmployeToEmployeur(Employeur employeur,Employe employe);
    
    public void AddEmployeToEmployeur(int numEmployeur,Employe employe);

    public boolean SupprimerEmployeur(int numeroEmployeur);
    
    public boolean SupprimerEmployeur(Employeur employeur);

    public boolean MiseAjourEmployeur(Employeur employeur);
    
    public Employeur getEmployeurByNumero(int numero);
    
    public void SupprimerEmployeToEmployeur(int numeroEmployeur,int numeroEmploye);
}
