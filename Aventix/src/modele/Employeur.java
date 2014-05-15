package modele;

import java.util.HashSet;
import java.util.Set;

public class Employeur  implements java.io.Serializable
{
	
	private static final long serialVersionUID = 1L;
	private Integer numemployeur;
     private String raisonsocial;
     private String login;
     private String password;
     private String email;
     private String adresse;
     private Set<Employe> ListEmployes = new HashSet<Employe>(0);
     private Set<Commande> commandes = new HashSet<Commande>(0);
     


    public Employeur() {
    }

    public Employeur(String raisonsocial, String login, String password, String email, String adresse, Set<Employe> employes, Set<Commande> commandes)
    {
       this.raisonsocial = raisonsocial;
       this.login = login;
       this.password = password;
       this.email = email;
       this.adresse = adresse;
       this.ListEmployes = employes;
       this.commandes = commandes;
    }
   
    public Integer getNumemployeur() {
        return this.numemployeur;
    }
    
    public void setNumemployeur(Integer numemployeur) {
        this.numemployeur = numemployeur;
    }
    public String getRaisonsocial() {
        return this.raisonsocial;
    }
    
    public void setRaisonsocial(String raisonsocial) {
        this.raisonsocial = raisonsocial;
    }
    public String getLogin() {
        return this.login;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    public String getAdresse() {
        return this.adresse;
    }
    
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Set<Employe> getListEmployes() {
        return ListEmployes;
    }

    public void setListEmployes(Set<Employe> ListEmployes) {
        this.ListEmployes = ListEmployes;
    }

    public Set<Commande> getCommandes() {
        return this.commandes;
    }
    
    public void setCommandes(Set<Commande> commandes) {
        this.commandes = commandes;
    }

    @Override
    public String toString() {
        return "Employeur{" + "numemployeur=" + numemployeur + ", raisonsocial=" + raisonsocial + ", login=" + login + ", password=" + password + ", email=" + email + ", adresse=" + adresse + '}';
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result
				+ ((numemployeur == null) ? 0 : numemployeur.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employeur other = (Employeur) obj;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		if (numemployeur == null) {
			if (other.numemployeur != null)
				return false;
		} else if (!numemployeur.equals(other.numemployeur))
			return false;
		return true;
	}
	

}


