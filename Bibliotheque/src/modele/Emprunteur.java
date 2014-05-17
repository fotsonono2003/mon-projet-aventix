package modele;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Emprunteur implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nom;
    private String adresse;

    public Emprunteur() {
    }

    public Emprunteur(String nom, String adresse) {
        this.nom = nom;
        this.adresse = adresse;
    }

    @OneToOne(mappedBy="emprunteur")
    private Ouvrage emprunt;
    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Ouvrage getEmprunt() {
        return emprunt;
    }

    public void setEmprunt(Ouvrage emprunt) {
        this.emprunt = emprunt;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Emprunteur)) {
            return false;
        }
        Emprunteur other = (Emprunteur) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modele.Emprunteur[id=" + id + ",Nom:"+nom+",Adresse:"+adresse+"]";
    }

}
