package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import modele.Auteur;
import services.JpaUtil;

public class AuteurDAO 
{
    public void createAuteur(Auteur a)
    {
        EntityManager em=JpaUtil.getEntityManager();
        em.persist(a);
    }

    public List<Auteur> findALlAuteur()
    {
        EntityManager em=JpaUtil.getEntityManager();
        Query query=em.createQuery("select a from Auteur as a");
        List<Auteur>liste=query.getResultList();
        return liste;
    }
}
