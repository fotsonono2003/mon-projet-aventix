package modele;
// Generated 5 mars 2014 00:03:03 by Hibernate Tools 3.6.0


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Carte  implements java.io.Serializable 
{
	private static final long serialVersionUID = 1L;
	private int numerocarte;
     private Employe employe;
     private Date datecreation=new Date();
     private float solde;
     private int password=1234;
     private boolean actif=true;
     private Set<Transactions> transactionses = new HashSet<Transactions>(0);

    public Carte() {
    }

	
    public Carte(int numerocarte, Employe employe, Date datecreation, float solde, int password, boolean actif) {
        this.numerocarte = numerocarte;
        this.employe = employe;
        this.datecreation = datecreation;
        this.solde = solde;
        this.password = password;
        this.actif = actif;
    }
    public Carte(int numerocarte, Employe employe, Date datecreation, float solde, int password, boolean actif, Set<Transactions> transactionses) {
       this.numerocarte = numerocarte;
       this.employe = employe;
       this.datecreation = datecreation;
       this.solde = solde;
       this.password = password;
       this.actif = actif;
       this.transactionses = transactionses;
    }
   
    public int getNumerocarte() {
        return this.numerocarte;
    }
    
    public void setNumerocarte(int numerocarte) {
        this.numerocarte = numerocarte;
    }
    public Employe getEmploye() {
        return this.employe;
    }
    
    public void setEmploye(Employe employe) {
        this.employe = employe;
    }
    public Date getDatecreation() {
        return this.datecreation;
    }
    
    public void setDatecreation(Date datecreation) {
        this.datecreation = datecreation;
    }
    public float getSolde() {
        return this.solde;
    }
    
    public void setSolde(float solde) {
        this.solde = solde;
    }
    public int getPassword() {
        return this.password;
    }
    
    public void setPassword(int password) {
        this.password = password;
    }
    public boolean isActif() {
        return this.actif;
    }
    
    public void setActif(boolean actif) {
        this.actif = actif;
    }
    public Set<Transactions> getTransactionses() {
        return this.transactionses;
    }
    
    public void setTransactionses(Set<Transactions> transactionses) {
        this.transactionses = transactionses;
    }

    @Override
    public String toString() {
        return "Carte{" + "numerocarte=" + numerocarte + ", datecreation=" + datecreation + ", solde=" + solde + ", password=" + password + ", actif=" + actif + '}';
    }


    @Override
    public boolean equals(Object obj) 
    {
        if(!(obj instanceof Carte))
        {
            return false;
        }
        Carte carte=(Carte)obj;
        return carte.getNumerocarte()==this.numerocarte; 
    }

    @Override
    public int hashCode() {
        return super.hashCode(); //To change body of generated methods, choose Tools | Templates.
    }




}


