
package projet_consultation.demo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import projet_consultation.ConnexionBDD.ConnexionBDDOperateur;
import projet_consultation.ConnexionBDD.Connexion_BDDGenerale;

public class Remplir_Table_bts
{

    public Remplir_Table_bts() {}
    
    public void AjouterTableA_Mattel1() throws SQLException
    {
        ConnexionBDDOperateur cn=new ConnexionBDDOperateur("geoserver");
        String requete="select kpi from tableparametrekpi";
        ResultSet resultset = cn.getResultset(requete);
        try
        {
            while (resultset.next())
            {
                String kpi=resultset.getString("kpi").toLowerCase().trim();
                requete="ALTER TABLE mau_mattel1 ADD COLUMN "+kpi+" real;";
                System.out.println("requete:"+requete);
                //cn=new ConnexionBDDOperateur("tunisianabdd");
                //cn.ExecuterRequete(requete);
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(Remplir_Table_bts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void RemplirValeurKpi() throws SQLException
    {
        ConnexionBDDOperateur cn=new ConnexionBDDOperateur("geoserver");
        String requete="select kpi from tableparametrekpi order by kpi";
        ResultSet resultsetKpi = cn.getResultset(requete);
        cn=new ConnexionBDDOperateur("airtelbdd");
        requete="select * from table_bts";
        ResultSet resultset = cn.getResultset(requete);
        try
        {
            while (resultsetKpi.next())
            {
                requete="select id_pkm from table_bts order by id_pkm";
                ResultSet resultsetdId_pkm = cn.getResultset(requete);
                String kpi = null;

                kpi = resultsetKpi.getString("kpi").toLowerCase().trim();
                System.out.println("/////////////*****************//////////////KPI:" + kpi);
                while (resultsetdId_pkm.next())
                {
                    double val = Math.random();
                    val=(double)((int)(val*100))/100;

                    //requete="insert into table_bts(nom,localite,longitude,latitude,the_geom,id_pkm) values('"+resultset.getString("nom")+"','"+resultset.getString("localite")+"',"+resultset.getDouble("longitude")+","+resultset.getDouble("latitude")+",'"+resultset.getObject("the_geom").toString()+"',"+resultset.getInt("id_pkm")+") ";
                    requete = "update table_bts set "+kpi+"="+val+" where id_pkm="+resultsetdId_pkm.getInt("id_pkm");
                    //System.out.println("requete:" + requete);
                    cn.ExecuterRequete(requete);
                }
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(Remplir_Table_bts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void RemplirValeurKpiTable_line() throws SQLException, ClassNotFoundException
    {
        Connexion_BDDGenerale cnBDD=Connexion_BDDGenerale.getInstance();
        String requete="select kpi from tableparametrekpi order by kpi";
        ResultSet resultsetKpi = cnBDD.getResultset(requete);
        
        ConnexionBDDOperateur cn=new ConnexionBDDOperateur("airtelbdd");
        requete="select * from table_line";
        //ResultSet resultset = cn.getResultset(requete);
        try
        {
            while (resultsetKpi.next())
            {
                requete="select gid from table_line order by gid";
                ResultSet resultsetdId_pkm = cn.getResultset(requete);
                String kpi = null;

                kpi = resultsetKpi.getString("kpi").toLowerCase().trim();
                System.out.println("/////////////*****************//////////////KPI:" + kpi);
                while (resultsetdId_pkm.next())
                {
                    double val = Math.random();
                    val=(double)((int)(val*100))/100;

                    //requete="insert into table_bts(nom,localite,longitude,latitude,the_geom,id_pkm) values('"+resultset.getString("nom")+"','"+resultset.getString("localite")+"',"+resultset.getDouble("longitude")+","+resultset.getDouble("latitude")+",'"+resultset.getObject("the_geom").toString()+"',"+resultset.getInt("id_pkm")+") ";
                    requete = "update table_line set "+kpi+"="+val+" where gid="+resultsetdId_pkm.getInt("gid");
                    System.out.println("requete:" + requete);
                    cn.ExecuterRequete(requete);
                }
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(Remplir_Table_bts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void RemplirRegionTableValeurKPI() throws SQLException
    {
        ConnexionBDDOperateur cn=new ConnexionBDDOperateur("mtnbdd");
        String requete="delete from tablevaleurskpi";
        cn.ExecuterRequete(requete);

        requete="select distinct(localite) from table_bts order by localite";
        ResultSet resultSetLo=cn.getResultset(requete);
        try
        {
            int i=0;
            while (resultSetLo.next())
            {
                requete="insert into tablevaleurskpi(region) values('"+resultSetLo.getString("localite").trim()+"') ";
                cn.ExecuterRequete(requete);
                i++;
                System.out.println("valeur:"+i);
            }
                requete="insert into tablevaleurskpi(region) values('Global') ";
                cn.ExecuterRequete(requete);
        } catch (SQLException ex)
        {
            Logger.getLogger(Remplir_Table_bts.class.getName()).log(Level.SEVERE, null, ex);
        }
        requete="select kpi from tableparametrekpi order by kpi";
        ResultSet resultsetKpi = cn.getResultset(requete);
        
        try
        {
            while (resultsetKpi.next())
            {
                requete="select coderegion from tablevaleurskpi order by coderegion";
                ResultSet resultsetdId_pkm = cn.getResultset(requete);
                String kpi = null;

                kpi = resultsetKpi.getString("kpi").toLowerCase().trim();
                System.out.println("/////////////*****************//////////////KPI:" + kpi);
                while (resultsetdId_pkm.next())
                {
                    double val = Math.random();
                    val=(double)((int)(val*100))/100;

                    //requete="insert into table_bts(nom,localite,longitude,latitude,the_geom,id_pkm) values('"+resultset.getString("nom")+"','"+resultset.getString("localite")+"',"+resultset.getDouble("longitude")+","+resultset.getDouble("latitude")+",'"+resultset.getObject("the_geom").toString()+"',"+resultset.getInt("id_pkm")+") ";
                    requete = "update tablevaleurskpi set "+kpi+"="+val+" where coderegion="+resultsetdId_pkm.getInt("coderegion");
                    System.out.println("requete:" + requete);
                    cn.ExecuterRequete(requete);
                }
            }

        } catch (SQLException ex)
        {
            Logger.getLogger(Remplir_Table_bts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void SupprimerDoublonRegion() throws SQLException
    {
        ConnexionBDDOperateur cn=new ConnexionBDDOperateur("mtnbdd");
        String requete="";

        requete="select distinct(region) from tablevaleurskpi order by region";
        ResultSet resultSetLo=cn.getResultset(requete);
        try
        {
            while (resultSetLo.next())
            {
                requete="delete from tablevaleurskpi where region='"+resultSetLo.getString("localite")+"2' or region='"+resultSetLo.getString("localite")+"3'";
                cn.ExecuterRequete(requete);
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(Remplir_Table_bts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void RemplirValeurKpiVide() throws SQLException
    {
        ConnexionBDDOperateur cn=new ConnexionBDDOperateur("geoserver");
        String requete="select kpi from tableparametrekpi";
        ResultSet resultsetKpi = cn.getResultset(requete);
        cn=new ConnexionBDDOperateur("tunisianabdd");
        requete="select * from table_bts22";
        ResultSet resultset = cn.getResultset(requete);
        try
        {
            while (resultsetKpi.next())
            {
                String kpi=resultsetKpi.getString("kpi").toLowerCase().trim();
                System.out.println("/////////////*****************//////////////KPI:"+kpi);
                while (resultset.next())
                {
                    //requete="insert into table_bts(nom,localite,longitude,latitude,the_geom,id_pkm) values('"+resultset.getString("nom")+"','"+resultset.getString("localite")+"',"+resultset.getDouble("longitude")+","+resultset.getDouble("latitude")+",'"+resultset.getObject("the_geom").toString()+"',"+resultset.getInt("id_pkm")+") ";
                    double val=Math.random();
                    val=(double)((int)(val*100))/100;
                    requete = "update table_bts set "+kpi+"="+val+" where "+kpi+" is null";
                    System.out.println("requete:" + requete);
                    System.out.println("Val:" + val);
                    //cn.ExecuterRequete(requete);
                }
                resultset.beforeFirst();
                System.out.println("************************************************KPI:"+kpi);
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(Remplir_Table_bts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void MultiplierValeurKpi() throws SQLException
    {
        ConnexionBDDOperateur cn=new ConnexionBDDOperateur("airtelbdd");
        String requete="select id_pkm from table_bts";
        ResultSet resultset= cn.getResultset(requete);
        try
        {
            while (resultset.next())
            {
                requete="update table_bts set tchm=tchm*1000";
                System.out.println("Requete:"+requete);
                cn.ExecuterRequete(requete);
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(Remplir_Table_bts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void RemplirTableBTS()
    {
        try
        {
            ConnexionBDDOperateur cnOp = new ConnexionBDDOperateur("mtnbdd");
            Connexion_BDDGenerale cnGen=Connexion_BDDGenerale.getInstance();
            String requete="select * from table_geometrique";
            ResultSet resultSet=cnGen.getResultset(requete);
            while (resultSet.next())
            {
                requete="insert into table_regions(nom_region,gid,the_geom) values('"+resultSet.getString("adm2")+"',"+resultSet.getInt("gid")+",'"+resultSet.getObject("the_geom").toString()+"')";
                StringBuilder str=new StringBuilder();
                //str.append(requete);
                str.append("insert into table_regions(nom_region,gid,the_geom) values('").append(resultSet.getString("adm2")).append("',").append(resultSet.getInt("gid")).append(",'").append(resultSet.getObject("the_geom").toString()).append("')");
                System.out.println("requete:"+requete);
                cnOp.ExecuterRequete(str.toString());
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(Remplir_Table_bts.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static  void main(String arg[])
    {
        Remplir_Table_bts rmp=new Remplir_Table_bts();
        rmp.RemplirTableBTS();
        //rmp.RemplirRegionTableValeurKPI();
        //rmp.MultiplierValeurKpi();
        //rmp.SupprimerDoublonRegion();
        System.out.println("VAL:"+Math.random());
        String str="thierry";
        str=str.substring(str.length()-1);
        System.out.println("str:"+str);

        str="thierry";
        str=str.substring(str.length()-1);
        System.out.println("str:"+str);
    }
}
