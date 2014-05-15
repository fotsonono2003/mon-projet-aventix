package modele;

import java.util.HashSet;
import java.util.Set;

public class Commercant  implements java.io.Serializable 
{        
	private static final long serialVersionUID = 1L;
	private Integer numcommercant;
     private String raisonSociale;
     private String login;
     private String password;
     private int rib;
     private String siret;
     private String email;
     private String adresse;
     private String telephone;
     private Set<Lecteurcarte> lecteurcartes = new HashSet<Lecteurcarte>(0);
     
    public Commercant() {
    }

	
    public Commercant(String raisonSociale, int rib, String email) {
        this.raisonSociale = raisonSociale;
        this.rib = rib;
        this.email = email;
    }
    
    public Commercant(String raisonSociale, String login, String password, int rib, String siret, String email, Set<Lecteurcarte> lecteurcartes) {
       this.raisonSociale = raisonSociale;
       this.login = login;
       this.password = password;
       this.rib = rib;
       this.siret = siret;
       this.email = email;
       this.lecteurcartes = lecteurcartes;
    }
   
    public Integer getNumcommercant() {
        return this.numcommercant;
    }
    
    public void setNumcommercant(Integer numcommercant) {
        this.numcommercant = numcommercant;
    }
    public String getRaisonSociale() {
        return this.raisonSociale;
    }
    
    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
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
    public int getRib() {
        return this.rib;
    }
    
    public void setRib(int rib) {
        this.rib = rib;
    }
    public String getSiret() {
        return this.siret;
    }
    
    public void setSiret(String siret) {
        this.siret = siret;
    }
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Lecteurcarte> getLecteurcartes() {
    return this.lecteurcartes;
    }
    public void setLecteurcartes(Set<Lecteurcarte> lecteurcartes) {
    this.lecteurcartes = lecteurcartes;
    }

    public String getAdresse() {
		return adresse;
	}


	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}


	public String getTelephone() {
		return telephone;
	}


	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}


	@Override
	public String toString() {
		return "Commercant [numcommercant=" + numcommercant
				+ ", raisonSociale=" + raisonSociale + ", login=" + login
				+ ", password=" + password + ", rib=" + rib + ", siret="
				+ siret + ", email=" + email + ", adresse=" + adresse
				+ ", telephone=" + telephone + ", lecteurcartes="
				+ lecteurcartes + "]";
	}



     
}


