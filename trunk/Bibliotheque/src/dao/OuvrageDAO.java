
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import modele.Auteur;
import modele.Ouvrage;
import services.JpaUtil;

public class OuvrageDAO
{
    
    public void CreateOuvrage(Ouvrage o)
    {
        EntityManager em=JpaUtil.getEntityManager();
        em.persist(o);
    }

    public void  updateOuvrage(Ouvrage o)
    {
        EntityManager em=JpaUtil.getEntityManager();
    }

    public void deleteOuvrage(Ouvrage o)
    {
        EntityManager em=JpaUtil.getEntityManager();
        em.merge(o);
    }

    public List<Ouvrage> findAllOuvrage()
    {
        EntityManager em=JpaUtil.getEntityManager();
        Query query=em.createQuery("select o from Ouvrage as o");
        List<Ouvrage>liste=query.getResultList();
        return liste;
    }

    public Ouvrage findOuvragebyId(Long id)
    {
        EntityManager em=JpaUtil.getEntityManager();
        return em.find(Ouvrage.class,id);
    }

    public List<Ouvrage> findOuvrageById(Auteur a)
    {
        EntityManager em=JpaUtil.getEntityManager();
        Query query=em.createQuery("select o from Ouvrage as o where o.auteur=:auteur").setParameter("auteur", a);
        List<Ouvrage>liste=query.getResultList();
        return liste;
    }

    public List<Ouvrage>findOuvrageByAuteur(String na)
    {
        EntityManager em=JpaUtil.getEntityManager();
        Query query=em.createQuery("select o from Ouvrage as o where o.auteur.nom=:nomAuteur").setParameter("nomAuteur", na);
        List<Ouvrage>liste=query.getResultList();
        return liste;
    }
}
