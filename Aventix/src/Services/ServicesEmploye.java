package Services;

import modele.Employe;

public interface ServicesEmploye
{
    void AddEmploye(Employe employe);
    boolean SupprimerEmploye(int numeroEmploye);
    boolean MiseAjourEmploye(Employe employe);
    public Employe getEmployeByNumero(int numero) ;
}
