package projet_consultation.demo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import projet_consultation.ConnexionBDD.ConnexionBDDOperateur;

public class InsertLineSymboliser
{

    public InsertLineSymboliser()
    {
    }

    public void Insertion()
    {
        try 
        {
            List<String> lstregion = new ArrayList<String>();
            List<Float> lstLong = new ArrayList<Float>();
            List<Float> lstLat = new ArrayList<Float>();
            ConnexionBDDOperateur cntun = new ConnexionBDDOperateur("airtelbdd");
            String requete = "";
            int tab[]={2504,1502,1504,2567};
            int n=tab.length;
            //int n=lstregion.size();
            String str="";
            String axe="Axe1";
            for (int i=0;i<n;i++)
            {
                requete="select * from table_bts where nom='"+tab[i]+"'";
                ResultSet result = cntun.getResultset(requete);
                lstLat.clear();lstLong.clear();
                
                while (result.next())
                {
                    //axe=result.getString("localite").trim();
                    lstLong.add(result.getFloat("longitude"));
                    lstLat.add(result.getFloat("latitude"));
                }
                System.out.println("liste:"+lstLong);
                System.out.println("liste:"+lstLat);
                System.out.println("Nom:"+tab[i]);
                int m=lstLong.size();
                if(m>1)
                {
                    for(int j=1;j<m;j++)
                    {
                        if(j<m-1)
                        {
                            str=str+lstLong.get(j-1)+" "+lstLat.get(j-1)+","+lstLong.get(j)+" "+lstLat.get(j)+",";
                        }
                        else
                        {
                            str=str+lstLong.get(j-1)+" "+lstLat.get(j-1)+","+lstLong.get(j)+" "+lstLat.get(j);
                        }
                    }
                }
                else if(m==1)
                {
                    //str=str+lstLat.get(0)+" "+lstLong.get(0)+","+lstLat.get(0)+" "+lstLong.get(0);
                }
                if(i!=n-1)
                    str=str+lstLong.get(0)+" "+lstLat.get(0)+",";
                else
                    str=str+lstLong.get(0)+" "+lstLat.get(0)+","+lstLong.get(0)+" "+lstLat.get(0);
            }
            requete="insert into table_line(axe,the_geom) values('"+axe+"',GeomFromText('LINESTRING("+str+")',-1))";
            System.out.println("requete:"+requete);
            cntun.ExecuterRequete(requete);
            System.out.println("***************valeur de str:"+str);
        } catch (SQLException ex)
        {
            Logger.getLogger(InsertLineSymboliser.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex)
        {
            Logger.getLogger(InsertLineSymboliser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String arg[])
    {
        InsertLineSymboliser inst=new InsertLineSymboliser();
        inst.Insertion();
    }
}
