package modele;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Ouvrage implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String titre;
    private int anneeDeParution;

    @ManyToOne(cascade=CascadeType.PERSIST)
    private Auteur auteur;

    @OneToOne
    @JoinColumn(name="Emprunteur_ID",nullable=true)
    private Emprunteur emprunteur;
    public Ouvrage() {
    }

    public Emprunteur getEmprunteur() {
        return emprunteur;
    }

    public void setEmprunteur(Emprunteur emprunteur) {
        this.emprunteur = emprunteur;
    }

    public Ouvrage(String titre, int anneeDeParution, Auteur auteur)
    {
        this.titre = titre;
        this.anneeDeParution = anneeDeParution;
        this.auteur = auteur;
    }


    public Auteur getAuteur() {
        return auteur;
    }

    public void setAuteur(Auteur auteur) {
        this.auteur = auteur;
    }

    public int getAnneeDeParution() {
        return anneeDeParution;
    }

    public Ouvrage(String titre, int anneeDeParution) {
        this.titre = titre;
        this.anneeDeParution = anneeDeParution;
    }

    public void setAnneeDeParution(int anneeDeParution) {
        this.anneeDeParution = anneeDeParution;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
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
        if (!(object instanceof Ouvrage)) {
            return false;
        }
        Ouvrage other = (Ouvrage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modele.Ouvrage[id=" + id + ", Titre:"+titre+",Parution:"+anneeDeParution+"]";
    }

}
