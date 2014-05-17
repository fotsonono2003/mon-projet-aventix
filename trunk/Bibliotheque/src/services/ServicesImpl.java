
package services;

import dao.AuteurDAO;
import dao.EmprunteurDao;
import dao.OuvrageDAO;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import modele.Auteur;
import modele.Emprunteur;
import modele.Ouvrage;

public class ServicesImpl implements ServicesOuvrage,ServicesAuteur,ServicesEmprunteur
{

    public void referencerOuvrage(Ouvrage o)
    {
        OuvrageDAO odao=new OuvrageDAO();
        EntityTransaction tx=null;
        try
        {
            tx = JpaUtil.getEntityTransaction();
            tx.begin();
            odao.CreateOuvrage(o);
            tx.commit();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();

            if(tx!=null && tx.isActive())
            {
                tx.rollback();
            }
        }
        finally
        {
            JpaUtil.closeEntityManager();
        }
    }

    public Ouvrage trouverOuvrageParNumero(Long numero)
    {
        OuvrageDAO odao=new OuvrageDAO();
        return odao.findOuvragebyId(numero);
    }

    public List<Ouvrage> listerTousLesOuvrages()
    {
        OuvrageDAO odao=new OuvrageDAO();
        return odao.findAllOuvrage();
    }

    public void modifierOuvrage(Ouvrage o)
    {
        OuvrageDAO odao=new OuvrageDAO();
        EntityTransaction tx=null;
        try
        {
            tx=JpaUtil.getEntityTransaction();
            tx.begin();
            odao.updateOuvrage(o);
            tx.commit();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            if(tx!=null && tx.isActive())
            {
                tx.rollback();
            }
        }
        finally
        {
            JpaUtil.closeEntityManager();
        }
    }

    public void dereferencerOuvrage(Ouvrage o)
    {
        OuvrageDAO odao=new OuvrageDAO();
        EntityTransaction tx=null;
        try
        {
            tx = JpaUtil.getEntityTransaction();
            tx.begin();
            odao.deleteOuvrage(o);
            tx.commit();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            if(tx!=null && tx.isActive())
            {
                tx.rollback();
            }
        }
        finally
        {
            JpaUtil.closeEntityManager();
        }
    }

    public void referencerAuteur(Auteur a)
    {
        AuteurDAO dao=new AuteurDAO();
        EntityTransaction tx=null;
        try
        {
            tx=JpaUtil.getEntityTransaction();
            tx.begin();
            dao.createAuteur(a);
            tx.commit();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            if(tx!=null && tx.isActive())
            {
                tx.rollback();
            }
        }
        finally
        {
            JpaUtil.closeEntityManager();
        }
    }

    public List<Ouvrage> listerTousOuvragesDunAuteur(Auteur a)
    {
        OuvrageDAO dao=new OuvrageDAO();
        return dao.findOuvrageById(a);
    }

    public List<Ouvrage> listerTousOuvragesDunAuteur(String a) 
    {
        OuvrageDAO dao=new OuvrageDAO();
        return dao.findOuvrageByAuteur(a);
    }

    public List<Auteur> listerTousLesAuteurs()
    {
        AuteurDAO dao=new AuteurDAO();
        return dao.findALlAuteur();
    }

    public void referencerEmprunteur(Emprunteur e)
    {
        EmprunteurDao dao=new EmprunteurDao();
        EntityTransaction tx=null;
        try
        {
            tx=JpaUtil.getEntityTransaction();
            tx.begin();
            dao.createEmprunteur(e);
            tx.commit();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            if(tx!=null && tx.isActive())
            {
                tx.rollback();
            }
        }
        finally
        {
            JpaUtil.closeEntityManager();
        }
    }

    public void preterOuvrage(Emprunteur e, Ouvrage o)
    {
        OuvrageDAO dao=new OuvrageDAO();
        EmprunteurDao daoe=new EmprunteurDao();
        EntityTransaction tx=null;
        try
        {
            tx=JpaUtil.getEntityTransaction();
            tx.begin();
            e.setEmprunt(o);
            dao.updateOuvrage(o);
            daoe.updateEmprunteur(e);
            tx.commit();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            if(tx!=null && tx.isActive())
            {
                tx.rollback();
            }
        }
        finally
        {
            JpaUtil.closeEntityManager();
        }
    }

}
