package dao;

import javax.persistence.EntityManager;
import modele.Emprunteur;
import services.JpaUtil;

public class EmprunteurDao
{
    public void createEmprunteur(Emprunteur e)
    {
        EntityManager em=JpaUtil.getEntityManager();
        em.merge(e);
    }

    public void updateEmprunteur(Emprunteur e)
    {
        EntityManager em=JpaUtil.getEntityManager();
        em.merge(e);
    }
}
