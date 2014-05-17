package projet_consultation.creation_dossiers;

import java.io.File;

public class mes_documents
{
    private String dossierPrincipale;
    private String dossierRapport;
    private String dossierLog;
    private String dossierImage;
    private static mes_documents mes=null;

    public mes_documents()
    {
        boolean creer_dossierPrincipal = creer_dossierPrincipal();
    }

    public static mes_documents getInstance()
    {
        if(mes==null)
        {
            mes=new mes_documents();
        }
        return mes;
    }
    
    public String get_CheminParametre()
    {
        return "./resources"+File.separator+"Fichiers_parametres"+File.separator;
    }

    public String get_CheminLog()
    {
        return dossierLog+File.separator;
    }

    public String get_CheminRapport()
    {
        return dossierRapport+File.separator;
    }

    public String get_CheminImage()
    {
        return dossierImage+File.separator;
    }

    private boolean creer_dossierRapport()
    {
        boolean b=false;
        try
        {
            File dir = new File(dossierPrincipale+File.separator+"rapport");
            if(dir.exists())
            {
                dossierRapport=dir.getPath();
                b=true;
            }
            else
            {
                dir.mkdirs();
                dossierRapport=dir.getPath();
                b=true;
            }
        }
        catch (SecurityException e)
        {
            b=false;
            e.printStackTrace();
        }
        catch (Exception e)
        {
            b=false;
            e.printStackTrace();
        }
        return b;
    }

    private boolean creer_dossierImage()
    {
        boolean b=false;
        try
        {
            File dir = new File(dossierPrincipale+File.separator+"graphes");
            if(dir.exists())
            {
                dossierImage=dir.getPath();
                b=true;
            }
            else
            {
                dir.mkdirs();
                dossierImage=dir.getPath();
                b=true;
            }
        }
        catch (SecurityException e)
        {
            b=false;
            e.printStackTrace();
        }
        catch (Exception e)
        {
            b=false;
            e.printStackTrace();
        }
        return b;
    }

    private boolean creer_dossierLog()
    {
        boolean b=false;
        try
        {
            File dir = new File(dossierPrincipale+File.separator+"Log");
            if(dir.exists())
            {
                dossierLog=dir.getPath();
                b=true;
            }
            else
            {
                dir.mkdirs();
                dossierLog=dir.getPath();
                b=true;
            }
        }
        catch (SecurityException e)
        {
            b=false;
            e.printStackTrace();
        }
        catch (Exception e)
        {
            b=false;
            e.printStackTrace();
        }
        return b;
    }

    private boolean creer_dossierPrincipal()
    {
        boolean b=false;
        try
        {
            //String osname=System.getProperty("user.home");
            //File dir = new File(osname+File.separator+"bi4t");
            File dir = new File("./bi4t");
            if(dir.exists())
            {
                dossierPrincipale=dir.getPath();
                creer_dossierImage();
                creer_dossierLog();
                creer_dossierRapport();
                //creer_dossierParametres();
                b=true;
            }
            else
            {
                dir.mkdirs();
                dossierPrincipale=dir.getPath();
                creer_dossierImage();
                creer_dossierLog();
                creer_dossierRapport();
                b=true;
            }
        }
        catch (SecurityException e)
        {
            b=false;
            e.printStackTrace();
            
        }
        catch (Exception e)
        {
            b=false;
            e.printStackTrace();
        }
        return b;
    }

}
