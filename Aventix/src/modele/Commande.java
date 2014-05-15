package modele;
// Generated 5 mars 2014 00:03:03 by Hibernate Tools 3.6.0


import java.util.Date;

public class Commande  implements java.io.Serializable 
{
	private static final long serialVersionUID = 1L;
	private Integer numcommande;
     private Employeur employeur;
     private int nbcarte;
     private Date datecommande=new Date();
     private Facture facture;

    public Commande() {
    }

    public Commande(Employeur employeur, int nbcarte, Date datecommande) {
       this.employeur = employeur;
       this.nbcarte = nbcarte;
       this.datecommande = datecommande;
    }
   
    public Integer getNumcommande() {
        return this.numcommande;
    }
    
    public void setNumcommande(Integer numcommande) {
        this.numcommande = numcommande;
    }
    public Employeur getEmployeur() {
        return this.employeur;
    }
    
    public void setEmployeur(Employeur employeur) {
        this.employeur = employeur;
    }
    public int getNbcarte() {
        return this.nbcarte;
    }
    
    public void setNbcarte(int nbcarte) {
        this.nbcarte = nbcarte;
    }
    public Date getDatecommande() {
        return this.datecommande;
    }
    
    public void setDatecommande(Date datecommande) {
        this.datecommande = datecommande;
    }

    public Facture getFacture() {
        return facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    @Override
    public String toString() {
        return "Commande{" + "numcommande=" + numcommande + ", employeur=" + employeur + ", nbcarte=" + nbcarte + ", datecommande=" + datecommande + ", facture=" + facture + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.numcommande != null ? this.numcommande.hashCode() : 0);
        hash = 37 * hash + (this.employeur != null ? this.employeur.hashCode() : 0);
        hash = 37 * hash + this.nbcarte;
        hash = 37 * hash + (this.datecommande != null ? this.datecommande.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Commande other = (Commande) obj;
        if (this.numcommande != other.numcommande && (this.numcommande == null || !this.numcommande.equals(other.numcommande))) {
            return false;
        }
        if (this.datecommande != other.datecommande && (this.datecommande == null || !this.datecommande.equals(other.datecommande))) {
            return false;
        }
        return true;
    }


}


