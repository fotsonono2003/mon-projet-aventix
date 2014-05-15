package modele;

import java.util.Date;

public class Employe  implements java.io.Serializable 
{
	private static final long serialVersionUID = 1L;
	private Integer numemploye;
     private Employeur employeur;
     private String nom;
     private String prenom;
     private String login;
     private String password="aventix";
     private String email;
     private Date dateEnregistrement=new Date();
     //private Set<Carte> cartes = new HashSet<Carte>(0);
     private Carte carte=new Carte();
     
    public Employe() 
    {
    }

   /* public Employe(Employeur employeur, String nom, String prenom, String login, String password, String email, Date dateEnregistrement, Set<Carte> cartes) {
       this.employeur = employeur;
       this.nom = nom;
       this.prenom = prenom;
       this.login = login;
       this.password = password;
       this.email = email;
       this.dateEnregistrement = dateEnregistrement;
       this.cartes = cartes;
    }
    */
   
    public Employe(Employeur employeur, String nom, String prenom, String login, String password, String email, Date dateEnregistrement, Carte carte) {
       this.employeur = employeur;
       this.nom = nom;
       this.prenom = prenom;
       this.login = login;
       this.password = password;
       this.email = email;
       this.dateEnregistrement = dateEnregistrement;
       this.carte = carte;
    }

    public Integer getNumemploye() {
        return this.numemploye;
    }
    
    public void setNumemploye(Integer numemploye) {
        this.numemploye = numemploye;
    }
    public Employeur getEmployeur() {
        return this.employeur;
    }
    
    public void setEmployeur(Employeur employeur) {
        this.employeur = employeur;
    }
    public String getNom() {
        return this.nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getPrenom() {
        return this.prenom;
    }
    
    public void setPrenom(String prenom) {
        this.prenom = prenom;
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
    public Date getDateEnregistrement() {
        return this.dateEnregistrement;
    }
    
    public void setDateEnregistrement(Date dateEnregistrement) {
        this.dateEnregistrement = dateEnregistrement;
    }

    public Carte getCarte() {
        return carte;
    }

    public void setCarte(Carte carte) {
        this.carte = carte;
    }

    @Override
    public String toString() 
    {
        return "Employe{" + "numemploye=" + numemploye + ", employeur=" + employeur+ ", nom=" + nom + ", prenom=" + prenom + ", login=" + login + ", password=" + password + ", email=" + email + ", dateEnregistrement=" + dateEnregistrement + ", carte=" + carte + '}';
    }
    
    

}


