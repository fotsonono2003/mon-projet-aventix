package modele;
// Generated 5 mars 2014 00:03:03 by Hibernate Tools 3.6.0


import java.util.Date;

public class Facture  implements java.io.Serializable 
{
	
	private static final long serialVersionUID = 1L;
	private Integer numFacture;
     private Date dateEdition=new Date();
     private Commande commande;
     private float montant=0;

    public Facture() {
    }

    public Facture(Date dateEdition) {
       this.dateEdition = dateEdition;
    }
   
    public Integer getNumFacture() {
        return this.numFacture;
    }
    
    public void setNumFacture(Integer numFacture) {
        this.numFacture = numFacture;
    }
    public Date getDateEdition() {
        return this.dateEdition;
    }
    
    public void setDateEdition(Date dateEdition) {
        this.dateEdition = dateEdition;
    }


    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

	public float getMontant() {
		return montant;
	}

	public void setMontant(float montant) {
		this.montant = montant;
	}

	@Override
	public String toString() {
		return "Facture [numFacture=" + numFacture + ", dateEdition="
				+ dateEdition + ", commande=" + commande.getNumcommande() + ", montant="
				+ montant + "]";
	}


}


