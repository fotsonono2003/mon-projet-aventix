
package projet_consultation.demo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import projet_consultation.ConnexionBDD.ConnexionBDDOperateur;

public class LireCSV_MTN_Congo
{
    public void lireFichierCSV() throws SQLException
    {
        try
        {
            //connexion_BDD cn=connexion_BDD.getInstance();
            ConnexionBDDOperateur cn=new ConnexionBDDOperateur("mtnbdd");
            /*String requete="select * from base_sites_airtel;";
            ResultSet result=st.executeQuery(requete);
            while (result.next())
            {
                System.out.println("resultat:"+result);
                double longitude=result.getDouble("longitude");
                double latitude=result.getDouble("latitude");
                int id=result.getInt("id");
                String requete2="update base_sites_airtel set the_geom=GeometryFromText('POINT("+longitude+" "+latitude+")',2154) where id="+id;
                int resultat2=st.executeUpdate(requete2);
            }
             * 
             */
            String chemin = "C:\\Users\\SFM\\Desktop\\code_java\\CONGOBRAZA_MTN.csv";
            String chemin1 = "C:\\Users\\SFM\\Desktop\\code_java\\lon_lat_MTN.csv";
            BufferedReader fichier_source = new BufferedReader(new FileReader(chemin));
            BufferedReader fichier_source1 = new BufferedReader(new FileReader(chemin1));
            String chaine;
            String chaine1;
            int i = 1;
            while((chaine = fichier_source.readLine())!= null && (chaine1 = fichier_source1.readLine())!= null )
            {
                if(i > 1)
                {
                    System.out.println("Ligne1: "+chaine1);
                    System.out.println("Ligne: "+chaine);
                    if(chaine!=null)
                    {
                        chaine=chaine.trim();
                        //chaine=chaine.replaceAll("'","\n");
                        //chaine=chaine.replaceAll("\"","");
                    }
                    double longitude=0;double latitude=0;
                    if(chaine1!=null)
                    {
                        chaine1=chaine1.trim();

                        String[] tabChaine1 = chaine1.split(";");
                        String val1=verifierValeur(tabChaine1[0]);
                        String val=val1.replace(",",".");
                        longitude=Double.parseDouble(val);
                        //val=tabChaine[tabChaine.length-2].replace(",",".");
                        val1=verifierValeur(tabChaine1[1]);
                        val=val1.replace(",",".");
                        latitude=Double.parseDouble(val);
                    }

                    String nom_site = null;
                    String region = null;
                    String[] tabChaine = null;
                    if (chaine!=null)
                    {
                        tabChaine = chaine.split(";");
                        region = tabChaine[1].trim().replaceAll("'","\n");
                        nom_site = tabChaine[2].trim().replaceAll("'","\n");
                        String LongDec=tabChaine[6].trim();
                        LongDec=LongDec.split("\"")[LongDec.split("\"").length-1];
                        String LatDec=tabChaine[7].trim();
                        LatDec=LatDec.split("\"")[LatDec.split("\"").length-1];
                        if(LatDec.equals("N"))
                        {
                            System.out.println("LongDec:"+LongDec);
                            System.out.println("LatDec:"+LatDec);
                        }
                        else if(LatDec.equals("S"))
                        {
                            System.out.println("LongDec:"+LongDec);
                            System.out.println("LatDec:"+LatDec);
                            latitude=latitude*(-1);
                        }
                        if(LongDec.equals("E"))
                        {
                        }
                        else if(LongDec.equals("W"))
                        {
                            longitude=longitude*(-1);
                        }

                    }
                    //String nom_secteur=tabChaine[2];
                    //String val=tabChaine[tabChaine.length-3].replace(",",".");
                    int id=i+1480;
                    System.out.println("valeur de i:"+(i+1480));
                    System.out.println("************************Valeur de i:"+i);
                    System.out.println("Latitude:"+latitude);
                    System.out.println("Longitude:"+longitude);
                    System.out.println("Nom:"+nom_site);
                    System.out.println("Localite:"+region);
                    System.out.println("Taille tableau:"+tabChaine.length);
                    String requete="insert into table_bts(nom,localite,longitude,latitude,the_geom) values('"+nom_site+"','"+region+"',"+longitude+","+latitude+",GeometryFromText('POINT("+longitude+" "+latitude+")',-1))";
                    System.out.println("Taille tableau:"+requete);
                    //String requete="insert into mau_mattel(nom,localite,longitude,latitude,kpi,the_geom) values('"+nom_site+"','"+localite+"',"+latitude+","+longitude+","+i+",GeometryFromText('POINT("+longitude+" "+latitude+")',2154))";
                    int j=cn.ExecuterRequete(requete);
                    /*if(i<=20)
                    {
                        requete="update  base_sites_airtel2 set kpi1="+(i-2)+" where id="+i;
                        j=st.executeUpdate(requete);
                    }
                    else if(i>20 && i<600)
                    {
                        requete="update  base_sites_airtel2 set kpi1="+(i-20)+" where id="+i;
                        j=st.executeUpdate(requete);
                    }
                    else
                    {
                        requete="update  base_sites_airtel2 set kpi1="+(i-600)+" where id="+i;
                        j=st.executeUpdate(requete);
                    }*/
                }
              i++;
            }
           fichier_source.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(LireCSV_MTN_Congo.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }

    public String verifierValeur(String val1)
    {
        String chaine="";
        int n=val1.length();
        for(int k=0;k<n;k++)
        {
            if(val1.charAt(k)=="0".charAt(0) || val1.charAt(k)=="1".charAt(0) || val1.charAt(k)=="2".charAt(0) || val1.charAt(k)=="3".charAt(0) || val1.charAt(k)=="4".charAt(0) ||
               val1.charAt(k)=="5".charAt(0) || val1.charAt(k)=="6".charAt(0) || val1.charAt(k)=="7".charAt(0) || val1.charAt(k)=="8".charAt(0) || val1.charAt(k)=="9".charAt(0) ||
                val1.charAt(k)==",".charAt(0) || val1.charAt(k)=="+".charAt(0) || val1.charAt(k)=="-".charAt(0) )
            {

                chaine=chaine+val1.charAt(k);
            }
            else
            {
                System.out.println("mauvais caractere:"+val1.charAt(k));
            }
        }
        if(chaine.contains(","))
        {
            String c=chaine.substring(chaine.indexOf(",")+1);
            String SDebut=chaine.substring(0,chaine.indexOf(",")+1);
            if(c.contains(","))
            {
                c=c.replace(",", "");
                chaine=SDebut+c;
            }
        }
        return chaine;
    }


    public void EssaiMultiLineString() throws SQLException
    {
        int tab[]={334,333,51,205,267,61,260,50,63,105,106,263};
        List<Integer>lstId=new ArrayList<Integer>();
        List<String>lstRegion=new ArrayList<String>();
        List<Double>lstLong=new ArrayList<Double>();
        List<Double>lstLat=new ArrayList<Double>();
        ConnexionBDDOperateur cn=new ConnexionBDDOperateur("tunisianabdd");
        ResultSet result;
        
        for (int i = 0; i < tab.length; i++)
        {
            String requete="select * from mau_mattel where identifiant="+tab[i];
            result=cn.getResultset(requete);
            try
            {
                while (result.next())
                {
                    lstLong.add(result.getDouble("longitude"));
                    lstLat.add(result.getDouble("latitude"));
                    lstRegion.add(result.getString("nom"));
                }
                requete="insert into mau_line(long,lat,regions) values("+lstLong.get(i)+","+lstLat.get(i)+",'"+lstRegion.get(i)+"') ";
                cn.ExecuterRequete(requete);
            } catch (SQLException sQLException)
            {
                Logger.getLogger(LireCSV_MTN_Congo.class.getName()).log(Level.SEVERE, null, sQLException);
            }
        }
    }


    public void SetRegion() throws SQLException
    {
        ConnexionBDDOperateur cn=new ConnexionBDDOperateur("tunisianabdd");
        int Axe1[]={259,302,51,205,323,61,260,50,63,54,105,106,9,56,333};
        int Axe2[]={262,62,74,75,287,325,221,119,222,223,301,224,227,};
        int Region3[]={44,308,310,335,45,309};
        int Axe3[]={211,131,304,208,312,184,99,203,55,64,8,274,284,175,328,117,170,129,151,138,140,279,216,157,154,158,283,155,217,143,220,218,219,295,59,58,124,291,76,280,174,294,97,238,258,52,67,85,57,135,239,159,127,270};
        int Region2[]={101,197,187,257,237,80,277,245,121,176,160,178,81,210,108,109};
        int Axe4[]={194,189,321,190,192,193,319,14,320,195,196,94};
        int Axe6[]={77,64,8,274,284,175,328,166,317,318,145,144,133,226,292,95};
        int Axe5[]={183,233,269,234,293,231,282,232,229,303,327,230,200,251,316,149,172,198,82,2,92,311,325,118,326,100,60,265,204,197,150,1,96,196,171,242,266,5,181,201,174,53,91,72,141,10,136,126,65,240,241,167,305,228,152,168};
        List<Integer> lstId=new ArrayList<Integer>();
        
        String requete="select identifiant from mau_mattel where nom='NouakchottNODEB'";
        ResultSet result=cn.getResultset(requete);
        try
        {
            while (result.next())
            {
                lstId.add(result.getInt("identifiant"));
                System.out.println("valeur:"+result.getInt("identifiant"));
            }

            for (int i = 0; i < Axe1.length; i++)
            {
                requete="update mau_mattel set region='Axe1' where identifiant="+Axe1[i];
                cn.ExecuterRequete(requete);
            }
   
            for (int i = 0; i < Axe2.length; i++)
            {
                requete="update mau_mattel set region='Axe2' where identifiant="+Axe2[i];
                cn.ExecuterRequete(requete);
            }

            for (int i = 0; i < Axe3.length; i++)
            {
                requete="update mau_mattel set region='Axe3' where identifiant="+Axe3[i];
                cn.ExecuterRequete(requete);
            }

            for (int i = 0; i < Axe4.length; i++)
            {
                requete="update mau_mattel set region='Axe4' where identifiant="+Axe4[i];
                cn.ExecuterRequete(requete);
            }

            for (int i = 0; i < Axe5.length; i++)
            {
                requete="update mau_mattel set region='Axe5' where identifiant="+Axe5[i];
                cn.ExecuterRequete(requete);
            }

            for (int i = 0; i < Axe6.length; i++)
            {
                requete="update mau_mattel set region='Axe6' where identifiant="+Axe6[i];
                cn.ExecuterRequete(requete);
            }

            for (int i = 0; i < Region2.length; i++)
            {
                requete="update mau_mattel set region='Region2' where identifiant="+Region2[i];
                cn.ExecuterRequete(requete);
            }
            
            for (int i = 0; i < Region3.length; i++)
            {
                requete="update mau_mattel set region='Region3' where identifiant="+Region3[i];
                cn.ExecuterRequete(requete);
            }
            for (int i = 0; i < lstId.size(); i++)
            {
                requete="update mau_mattel set region='Region1' where identifiant="+lstId.get(i);
                cn.ExecuterRequete(requete);
            }
        } catch (SQLException sQLException)
        {
            sQLException.printStackTrace();
        }
    }

    public void SetMultiLineString()
    {
        try {
            ConnexionBDDOperateur cn = new ConnexionBDDOperateur("tunisianabdd");
            String requete = "update mau_line set the_geom=GeomFromText('MULTILINESTRING((-14.9802 19.1689,-14.5362 19.5817,-14.1606 19.8671),(-14.1606 19.8671,-13.8183 20.0516,-13.546 20.2511,-13.0379 20.5186))',-1) where gid=13";
            cn.ExecuterRequete(requete);
        } catch (SQLException ex) {
            Logger.getLogger(LireCSV_MTN_Congo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static  void main(String arg[])
    {
        try {
            LireCSV_MTN_Congo lr = new LireCSV_MTN_Congo();
            lr.lireFichierCSV();
            //lr.SetRegion();
            //lr.SetRegion();
        } catch (SQLException ex) {
            Logger.getLogger(LireCSV_MTN_Congo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
