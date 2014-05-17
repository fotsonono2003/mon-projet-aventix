package projet_consultation.genererCartes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.postgis.PostgisNGDataStoreFactory;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.swing.JMapPane;
import org.geotools.swing.action.PanAction;
import org.geotools.swing.action.ZoomInAction;
import org.geotools.swing.action.ZoomOutAction;
import projet_consultation.ConnexionBDD.ConnexionBDDOperateur;
import projet_consultation.ConnexionBDD.Connexion_BDDGenerale;
import projet_consultation.ClassesGenerales.Fichier;
import projet_consultation.ClassesGenerales.Operateur;
import projet_consultation.ClassesGenerales.ParametreKPI;

public class CarteKPI
{
    private JMapPane mappane= new JMapPane();
    //private MapContext mapcontext;
    private StyleBuilder  sb = new StyleBuilder();
    private String bddoperateur;
    private Map config = new HashMap();
    private  DataStore datastore;
    //private StreamingRenderer render=new StreamingRenderer();
    private ConnexionBDDOperateur cn;

    public CarteKPI()
    {
    } 
 
    public CarteKPI(String bdd)
    {
        this.bddoperateur = bdd;
    }

    public void  carteKPI (String title,String kpi,String cheminfichierlog, String fichierStyleCarte,String fichierStyleAxe,Operateur operateur) throws SQLException
    {
        JFrame frm = new JFrame();
        bddoperateur=operateur.getBddOperateur().trim();
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setSize(700, 900);
        JPanel pfond = new JPanel(new BorderLayout(1, 2));
        Connexion_BDDGenerale cnbdd=Connexion_BDDGenerale.getInstance();
        ParametreKPI parametreKPI=cnbdd.getParametresKPIFromKPI(kpi);

        JLabel jlbp=new JLabel(parametreKPI.getNomKpi());
        jlbp.setFont(new Font("Dialog", Font.BOLD, 32));
        jlbp.setHorizontalAlignment(JLabel.CENTER);
        jlbp.setVerticalAlignment(JLabel.CENTER);
        pfond.setSize(700, 900);
        ImageIcon Iconqostracker = new javax.swing.ImageIcon(getClass().getResource("/resources/logoqostracker.png"));
        Image im1=Iconqostracker.getImage();
        frm.setIconImage(im1);
        frm.setContentPane(pfond);
        
        frm.getContentPane().add(BorderLayout.NORTH,jlbp);
        frm.getContentPane().add(BorderLayout.CENTER, buildMapRegionAxe(fichierStyleCarte,fichierStyleAxe,cheminfichierlog));
        frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
        try
        {
            Thread.sleep(3000);
        } catch (InterruptedException inex)
        {
            try
            {
                Thread.currentThread().stop();
            } catch (Exception ex) {
            }
        }
        try
        {
            BufferedImage image = new BufferedImage(683, 860, BufferedImage.SCALE_DEFAULT);
            Graphics2D g2 = image.createGraphics();
            pfond.paint(g2);
            g2.dispose();
            ImageIO.write(image, "JPEG",new File(title));
        }
        catch (Exception ex)
        {
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);
            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe Cartekpi,méthode:carteKPI "+DateduJour+" Heure:"+heuredejour+" error occured while creating map,Erreur: " + ex, cheminfichierlog);
        }
        try
        {
            //mapcontext.dispose();
            frm.dispose();
        }
        catch (Exception e)
        {
        }
    }

    public void  carteKPI (String title,String kpi,String cheminfichierlog, String fichierStyleRegions,Operateur operateur) throws SQLException
    {
        JFrame frm = new JFrame();
        bddoperateur=operateur.getBddOperateur().trim();
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setSize(700, 900);
        JPanel pfond = new JPanel(new BorderLayout(1, 2));
        Connexion_BDDGenerale cnbdd=Connexion_BDDGenerale.getInstance();
        ParametreKPI parametreKPI=cnbdd.getParametresKPIFromKPI(kpi);

        JLabel jlbp=new JLabel(parametreKPI.getNomKpi());
        jlbp.setFont(new Font("Dialog", Font.BOLD, 32));
        jlbp.setHorizontalAlignment(JLabel.CENTER);
        jlbp.setVerticalAlignment(JLabel.CENTER);
        pfond.setSize(700, 900);//mauritanie
        frm.setContentPane(pfond);
        frm.getContentPane().add(BorderLayout.NORTH,jlbp);
        frm.getContentPane().add(BorderLayout.CENTER, buildMapRegion(fichierStyleRegions,cheminfichierlog));
        frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
        try
        {
            Thread.sleep(3000);
        } catch (InterruptedException inex)
        {
            try
            {
                Thread.currentThread().stop();
            } catch (Exception ex) {
            }
        }
        try
        {
            BufferedImage image = new BufferedImage(683, 860, BufferedImage.SCALE_DEFAULT);//mauritanie
            Graphics2D g2 = image.createGraphics();
            pfond.paint(g2);
            g2.dispose();
            ImageIO.write(image, "JPEG",new File(title));
        }
        catch (Exception ex)
        {
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);
            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe Cartekpi,méthode:carteKPI "+DateduJour+" Heure:"+heuredejour+" error occured while creating map,Erreur: " + ex, cheminfichierlog);
        }
        try
        {
            //mapcontext.dispose();
            frm.dispose();
            cnbdd.StopConn();
        } catch (Exception e)
        {
        }
    }

    public JMenuBar buildTool()
    {
        JMenuBar menu = new JMenuBar();
	JButton zinButton = new JButton(new ZoomInAction(mappane,true));
	JButton zoutButton = new JButton( new ZoomOutAction(mappane,true));
	JButton panButton = new JButton(new PanAction(mappane,true));
	JButton resetAction = new JButton(new org.geotools.swing.action.ResetAction(mappane, true));
	//JButton infoAction = new JButton(new org.geotools.swing.action.InfoAction(mappane, true));
	menu.add(zinButton);
	menu.add(zoutButton);
	menu.add(panButton);
        menu.add(resetAction);
        //menu.add(infoAction);
        return menu;
    }

    public JPanel buildMap_default(String fichierStyle,String cheminfichierlog) throws SQLException
    {
        MapContext mapcontext= new DefaultMapContext();
        mappane.setBackground(new Color(204,204,204));
        cn=new ConnexionBDDOperateur(bddoperateur);
        MapLayer maplayer,maplayer2;

        try
        {
            mapcontext.setTitle("Projet");
            mappane.setMapContext(mapcontext);
            config = new HashMap();
            config.put( PostgisNGDataStoreFactory.DBTYPE.key, "postgis" );
            config.put( PostgisNGDataStoreFactory.HOST.key, cn.getAdresseIP());
            config.put( PostgisNGDataStoreFactory.PORT.key, cn.getPort());
            config.put( PostgisNGDataStoreFactory.SCHEMA.key, "public" );
            config.put( PostgisNGDataStoreFactory.DATABASE.key,cn.getBDDGenerale() );
            config.put( PostgisNGDataStoreFactory.USER.key, cn.getUtilisateur() );
            config.put( PostgisNGDataStoreFactory.PASSWD.key, cn.getPassword());
            config.put(PostgisNGDataStoreFactory.LOOSEBBOX, true );
            config.put(PostgisNGDataStoreFactory.PREPARED_STATEMENTS, true );
            datastore = DataStoreFinder.getDataStore(config);
            FeatureSource source1 =  datastore.getFeatureSource("table_geometrique");


            config = new HashMap();
            config.put( PostgisNGDataStoreFactory.DBTYPE.key, "postgis" );
            config.put( PostgisNGDataStoreFactory.HOST.key, cn.getAdresseIP());
            config.put( PostgisNGDataStoreFactory.PORT.key, cn.getPort());
            config.put( PostgisNGDataStoreFactory.SCHEMA.key, "public" );
            config.put( PostgisNGDataStoreFactory.DATABASE.key, bddoperateur );
            config.put( PostgisNGDataStoreFactory.USER.key, cn.getUtilisateur() );
            config.put( PostgisNGDataStoreFactory.PASSWD.key, cn.getPassword());
            config.put(PostgisNGDataStoreFactory.LOOSEBBOX, true );
            config.put(PostgisNGDataStoreFactory.PREPARED_STATEMENTS, true );
            datastore = DataStoreFinder.getDataStore(config);
            FeatureSource source2 =  datastore.getFeatureSource("table_bts");

            SLDParser parser = new SLDParser(new StyleFactoryImpl());
            parser.setInput(fichierStyle);
            Style ptStyle = parser.readXML()[0];
            maplayer = new DefaultMapLayer(source2,ptStyle);
            //  maplayer2 = new  DefaultMapLayer(source1, sb.createStyle(sb.createPolygonSymbolizer(Color.LIGHT_GRAY, Color.BLACK, 1)));
            PolygonSymbolizer ps = sb.createPolygonSymbolizer(new Color(255, 204, 102), new Color(102,102,102), 1.2);
	    Style solstyle = sb.createStyle();
	    solstyle.addFeatureTypeStyle(sb.createFeatureTypeStyle(ps));
            maplayer2=new DefaultMapLayer(source1,solstyle);
            MapLayer []  listemap = new MapLayer [2];
            maplayer2.setQuery(Query.ALL);
            maplayer.setQuery(Query.ALL);
            maplayer2.setVisible(true);
            maplayer.setVisible(true);
            try
            {
                listemap[1] = maplayer;
                listemap[0] = maplayer2;
                //mapcontext.addLayer(maplayer);
                mapcontext.addLayers(listemap);
                //maplayer.setVisible(true);
                StreamingRenderer render = new StreamingRenderer();
                HashMap hints = new HashMap();
                hints.put("memoryPreloadingEnabled", Boolean.TRUE);
                render.setRendererHints(hints);
                mappane.setRenderer(render);
                mappane.setDisplayArea(mapcontext.getLayerBounds());
                mappane.setDoubleBuffered(true);
                //render.stopRendering();
            } catch (Exception ex)
            {
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(CarteKPI.class.getName()).log(Level.SEVERE, null, ex);
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);
            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe Cartekpi,méthode:buildMap_default "+DateduJour+" Heure:"+heuredejour+" Erreur: " + ex, cheminfichierlog);
        }
        finally
        {
            try
            {
                cn.getConnection().commit();
                cn.getConnection().close();
            }
            catch (Exception ex)
            {}
        }
        JPanel pfond = new JPanel(new BorderLayout());
        pfond.setSize(900,1100);
        pfond.add(BorderLayout.CENTER,mappane);
        pfond.add(BorderLayout.NORTH, buildTool());
        return  pfond;
    }

    public JPanel buildMapBTSParRegion(String fichierStyle,String cheminfichierlog,String nomRegion,String kpi)
    {
        MapContext mapcontext = new DefaultMapContext();
        mappane.setBackground(new Color(204,204,204));
        MapLayer maplayer,maplayer2;
        try
        {
            mappane.setSize(800, 800);
            cn= new ConnexionBDDOperateur(bddoperateur);
            mapcontext.setTitle("Projet");
            mappane.setMapContext(mapcontext);
            config = new HashMap();
            config.put( PostgisNGDataStoreFactory.DBTYPE.key, "postgis" );
            config.put( PostgisNGDataStoreFactory.HOST.key, cn.getAdresseIP());
            config.put( PostgisNGDataStoreFactory.PORT.key, cn.getPort());
            config.put( PostgisNGDataStoreFactory.SCHEMA.key, "public" );
            config.put( PostgisNGDataStoreFactory.DATABASE.key, cn.getBDDGenerale());
            config.put( PostgisNGDataStoreFactory.USER.key, cn.getUtilisateur());
            config.put( PostgisNGDataStoreFactory.PASSWD.key, cn.getPassword());
            datastore = DataStoreFinder.getDataStore(config);
            FeatureSource source1 =  datastore.getFeatureSource("table_geometrique");

            List<Integer>idregions=new ArrayList<Integer>();

            String requete="delete from table_bts_courante";
            cn.ExecuterRequete(requete);
            requete ="select * from table_bts where region='"+nomRegion.trim()+"'";
            ResultSet result =cn.getResultset(requete);
            while (result.next())
            {
                idregions.add(result.getInt("id_pkm"));
                requete="insert into table_bts_courante(cell_name,region,valkpi,the_geom) values('"+result.getString("cell_name")+"','"+result.getString("region")+"',"+result.getDouble(kpi.toLowerCase())+",'"+result.getObject("the_geom").toString()+"') ";
                cn.ExecuterRequete(requete);
            }

            config = new HashMap();
            config.put( PostgisNGDataStoreFactory.DBTYPE.key, "postgis" );
            config.put( PostgisNGDataStoreFactory.HOST.key, cn.getAdresseIP());
            config.put( PostgisNGDataStoreFactory.PORT.key, cn.getPort());
            config.put( PostgisNGDataStoreFactory.SCHEMA.key, "public" );
            config.put( PostgisNGDataStoreFactory.DATABASE.key, cn.getBDDOperateur());
            config.put( PostgisNGDataStoreFactory.USER.key, cn.getUtilisateur());
            config.put( PostgisNGDataStoreFactory.PASSWD.key, cn.getPassword());
            datastore = DataStoreFinder.getDataStore(config);
            FeatureSource source2 =  datastore.getFeatureSource("table_bts_courante");

           SLDParser parser = new SLDParser(new StyleFactoryImpl());
           parser.setInput(fichierStyle);
           Style ptStyle = parser.readXML()[0];
           maplayer = new DefaultMapLayer(source2,ptStyle);
           //  maplayer2 = new  DefaultMapLayer(source1, sb.createStyle(sb.createPolygonSymbolizer(Color.LIGHT_GRAY, Color.BLACK, 1)));
           PolygonSymbolizer ps = sb.createPolygonSymbolizer(new Color(255, 204, 102), new Color(102,102,102), 1.2);
	   Style solstyle = sb.createStyle();
	   solstyle.addFeatureTypeStyle(sb.createFeatureTypeStyle(ps));
           maplayer2=new DefaultMapLayer(source1,solstyle);
           MapLayer []  listemap = new MapLayer [2];
           maplayer2.setQuery(Query.ALL);
           maplayer.setQuery(Query.ALL);
           maplayer2.setVisible(true);
           maplayer.setVisible(true);
           listemap[1]=maplayer;
           listemap[0]=maplayer2;
           //mapcontext.addLayer(maplayer);
           mapcontext.addLayers(listemap);
           //maplayer.setVisible(true);
           StreamingRenderer render = new StreamingRenderer();
           HashMap hints = new HashMap();
           hints.put("memoryPreloadingEnabled", Boolean.TRUE );
           render.setRendererHints( hints );
           mappane.setRenderer(render);
           mappane.setDoubleBuffered(true);
           mappane.setDisplayArea(mapcontext.getLayerBounds());
        }
        catch (UnsupportedOperationException ex)
        {
            Logger.getLogger(CarteKPI.class.getName()).log(Level.SEVERE, null, ex);
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);
            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe Cartekpi,méthode:buildMapBTSParRegion "+DateduJour+" Heure:"+heuredejour+" Erreur: " + ex, cheminfichierlog);
        }
        catch (IllegalStateException ex)
        {
            Logger.getLogger(CarteKPI.class.getName()).log(Level.SEVERE, null, ex);
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);
            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe Cartekpi,méthode:buildMapBTSParRegion "+DateduJour+" Heure:"+heuredejour+" Erreur: " + ex, cheminfichierlog);
        }
        catch (Exception ex)
        {
            Logger.getLogger(CarteKPI.class.getName()).log(Level.SEVERE, null, ex);
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);
            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe Cartekpi,méthode:buildMapBTSParRegion "+DateduJour+" Heure:"+heuredejour+" Erreur: " + ex, cheminfichierlog);
        }
        finally
        {
            cn.closeConnection();
        }
        JPanel pfond = new JPanel(new BorderLayout());
        //pfond.setSize(850,850);
        pfond.add(BorderLayout.CENTER,mappane);
        pfond.add(BorderLayout.NORTH, buildTool());
        return  pfond;
    }

    public JPanel buildMapBTSDefectueuses(String fichierStyle,String cheminfichierlog,String table_bts_kpi,String kpi)
    {
        MapContext mapcontext= new DefaultMapContext();
        mappane.setBackground(new Color(204,204,204));
        try
        {
            mappane.setSize(800, 800);
            cn= new ConnexionBDDOperateur(bddoperateur);
            mapcontext.setTitle("Projet");
            mappane.setMapContext(mapcontext);

            config = new HashMap();
            config.put(PostgisNGDataStoreFactory.DBTYPE.key,"postgis" );
            config.put(PostgisNGDataStoreFactory.HOST.key,cn.getAdresseIP());
            config.put(PostgisNGDataStoreFactory.PORT.key,cn.getPort());
            config.put(PostgisNGDataStoreFactory.SCHEMA.key,"public" );
            config.put(PostgisNGDataStoreFactory.DATABASE.key,cn.getBDDGenerale());
            config.put(PostgisNGDataStoreFactory.USER.key,cn.getUtilisateur());
            config.put(PostgisNGDataStoreFactory.PASSWD.key,cn.getPassword());
            datastore = DataStoreFinder.getDataStore(config);
            FeatureSource sourcePays =  datastore.getFeatureSource("table_geometrique");

            Connexion_BDDGenerale cnbdd=Connexion_BDDGenerale.getInstance();
            ParametreKPI parametreKPI=cnbdd.getParametresKPIFromKPI(kpi);

            //Filter filter = null;
            //filter=CQL.toFilter(kpi.toLowerCase()+" < "+seuil1);
            config = new HashMap();
            config.put(PostgisNGDataStoreFactory.DBTYPE.key,"postgis" );
            config.put(PostgisNGDataStoreFactory.HOST.key,cn.getAdresseIP());
            config.put(PostgisNGDataStoreFactory.PORT.key,cn.getPort());
            config.put(PostgisNGDataStoreFactory.SCHEMA.key,"public" );
            config.put(PostgisNGDataStoreFactory.DATABASE.key,cn.getBDDOperateur());
            config.put(PostgisNGDataStoreFactory.USER.key,cn.getUtilisateur());
            config.put(PostgisNGDataStoreFactory.PASSWD.key,cn.getPassword());
            datastore = DataStoreFinder.getDataStore(config);
            FeatureSource sourceBTS = datastore.getFeatureSource(table_bts_kpi);

           SLDParser parser = new SLDParser(new StyleFactoryImpl());
           parser.setInput(fichierStyle);
           Style ptStyle = parser.readXML()[0];
           //maplayer = new DefaultMapLayer(sourceBTS.getFeatures(filter),ptStyle);
           //MapLayer maplayerBTS = new DefaultMapLayer(sourceBTS.getFeatures(filter),ptStyle);
           MapLayer maplayerBTS = new MapLayer(sourceBTS,ptStyle);
           //maplayer2 = new  DefaultMapLayer(source1, sb.createStyle(sb.createPolygonSymbolizer(Color.LIGHT_GRAY, Color.BLACK, 1)));
           PolygonSymbolizer ps = sb.createPolygonSymbolizer(new Color(255, 204, 102), new Color(102,102,102), 1.2);
	   Style solstyle = sb.createStyle();
	   solstyle.addFeatureTypeStyle(sb.createFeatureTypeStyle(ps));
           MapLayer maplayerPays=new MapLayer(sourcePays,solstyle);
           MapLayer [] listemap = new MapLayer [2];
           maplayerBTS.setQuery(Query.ALL);
           maplayerPays.setQuery(Query.ALL);
           listemap[0]=maplayerPays;
           listemap[1]=maplayerBTS;
           mapcontext.addLayers(listemap);
           //maplayer.setVisible(true);
           StreamingRenderer render = new StreamingRenderer();
           HashMap hints = new HashMap();
           hints.put("memoryPreloadingEnabled", Boolean.TRUE );
           render.setRendererHints( hints );
           mappane.setRenderer(render);
           mappane.setDoubleBuffered(true);
           mappane.setDisplayArea(mapcontext.getLayerBounds());
           //render.stopRendering();
           cnbdd.StopConn();
        }
        catch (UnsupportedOperationException ex)
        {
            Logger.getLogger(CarteKPI.class.getName()).log(Level.SEVERE, null, ex);
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);
            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe Cartekpi,méthode:buildMapBTSDefectueuses "+DateduJour+" Heure:"+heuredejour+" Erreur: " + ex, cheminfichierlog);
        }
        catch (IllegalStateException ex)
        {
            Logger.getLogger(CarteKPI.class.getName()).log(Level.SEVERE, null, ex);
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);
            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe Cartekpi,méthode:buildMapBTSDefectueuses "+DateduJour+" Heure:"+heuredejour+" Erreur: " + ex, cheminfichierlog);
        }
        catch (Exception ex)
        {
            Logger.getLogger(CarteKPI.class.getName()).log(Level.SEVERE, null, ex);
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);
            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe Cartekpi,méthode:buildMapBTSDefectueuses "+DateduJour+" Heure:"+heuredejour+" Erreur: " + ex, cheminfichierlog);
        }
        finally
        {
            cn.closeConnection();
        }
        JPanel pfond = new JPanel(new BorderLayout());
        //pfond.setSize(850,850);
        pfond.add(BorderLayout.CENTER,mappane);
        pfond.add(BorderLayout.NORTH, buildTool());
        return  pfond;
    }

    public JPanel buildMapRegionAxe(String fichierStyle,String fichierStyleAxe,String cheminfichierlog)
    {
        MapContext mapcontext = new DefaultMapContext();
        mappane.setBackground(new Color(170,210,251));
        try
        {
            cn=new ConnexionBDDOperateur(bddoperateur);
            mapcontext.setTitle("Projet");
            mappane.setMapContext(mapcontext);
            MapLayer maplayerpays,maplayerregions,maplayeraxes;
            config = new HashMap();
            config.put( PostgisNGDataStoreFactory.DBTYPE.key, "postgis" );
            config.put( PostgisNGDataStoreFactory.HOST.key, cn.getAdresseIP() );
            config.put( PostgisNGDataStoreFactory.PORT.key, cn.getPort());
            config.put( PostgisNGDataStoreFactory.SCHEMA.key, "public" );
            config.put( PostgisNGDataStoreFactory.DATABASE.key, cn.getBDDGenerale());
            config.put( PostgisNGDataStoreFactory.USER.key, cn.getUtilisateur());
            config.put( PostgisNGDataStoreFactory.PASSWD.key, cn.getPassword());
            config.put( PostgisNGDataStoreFactory.LOOSEBBOX, true );
            config.put( PostgisNGDataStoreFactory.PREPARED_STATEMENTS, true );
            datastore = DataStoreFinder.getDataStore(config);

            FeatureSource sourcepays =  datastore.getFeatureSource("table_geometrique");//mauritanie_coordonnees

            config = new HashMap();
            config.put( PostgisNGDataStoreFactory.DBTYPE.key, "postgis" );
            config.put( PostgisNGDataStoreFactory.HOST.key, cn.getAdresseIP() );
            config.put( PostgisNGDataStoreFactory.PORT.key, cn.getPort());
            config.put( PostgisNGDataStoreFactory.SCHEMA.key, "public" );
            config.put( PostgisNGDataStoreFactory.DATABASE.key, cn.getBDDOperateur());
            config.put( PostgisNGDataStoreFactory.USER.key, cn.getUtilisateur());
            config.put( PostgisNGDataStoreFactory.PASSWD.key, cn.getPassword());
            config.put( PostgisNGDataStoreFactory.LOOSEBBOX, true );
            config.put( PostgisNGDataStoreFactory.PREPARED_STATEMENTS, true );
            datastore = DataStoreFinder.getDataStore(config);
            FeatureSource sourceaxes =  datastore.getFeatureSource("table_line");//base_sites_airtel2
            //FeatureSource sourceregions =  datastore.getFeatureSource("table_regions");//base_sites_airtel2

            SLDParser parseraxe = new SLDParser(new StyleFactoryImpl());
            parseraxe.setInput(fichierStyleAxe);
            Style ptStyle = parseraxe.readXML()[0];
            maplayeraxes = new DefaultMapLayer(sourceaxes,ptStyle);

            /*SLDParser parserRegion = new SLDParser(new StyleFactoryImpl());
            parserRegion.setInput(fichierStyle);
            Style ptStyleRegion = parserRegion.readXML()[0];
            //maplayerregions = new DefaultMapLayer(sourceregions,ptStyleRegion);
             * 
             */

            PolygonSymbolizer ps = sb.createPolygonSymbolizer(new Color(255, 204, 102), new Color(102,102,102), 1.2);
            Style solstyle = sb.createStyle();
            solstyle.addFeatureTypeStyle(sb.createFeatureTypeStyle(ps));
            maplayerpays = new DefaultMapLayer(sourcepays,solstyle);

            MapLayer []  listemap = new MapLayer [2];
	    listemap[0]=maplayerpays;
            //listemap[1]=maplayerregions;
            listemap[1]=maplayeraxes;
            mapcontext.addLayers(listemap);
            StreamingRenderer render = new StreamingRenderer();
            HashMap hints = new HashMap();
            hints.put("memoryPreloadingEnabled", Boolean.TRUE );
            render.setRendererHints( hints );
            mappane.setRenderer(render);
            mappane.setDisplayArea(mapcontext.getLayerBounds());
            mappane.setDoubleBuffered(true);
        }
        catch (Exception ex)
        {
            Logger.getLogger(CarteKPI.class.getName()).log(Level.SEVERE, null, ex);
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);
            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe Cartekpi,méthode:buildMapRegionAxe "+DateduJour+" Heure:"+heuredejour+" Erreur: " + ex, cheminfichierlog);
        }
        finally
        {
            cn.closeConnection();
        }
        return mappane;
    }

    public JPanel buildMapRegion(String fichierStyleRegion,String cheminfichierlog)
    {
        MapContext mapcontext= new DefaultMapContext();
        mappane.setBackground(new Color(170,210,251));
        try
        {
            cn=new ConnexionBDDOperateur(bddoperateur);
            mapcontext.setTitle("Projet");
            mappane.setMapContext(mapcontext);
            MapLayer maplayerpays,maplayerRegions;
            config = new HashMap();
            config.put( PostgisNGDataStoreFactory.DBTYPE.key, "postgis" );
            config.put( PostgisNGDataStoreFactory.HOST.key, cn.getAdresseIP() );
            config.put( PostgisNGDataStoreFactory.PORT.key, cn.getPort());
            config.put( PostgisNGDataStoreFactory.SCHEMA.key, "public" );
            config.put( PostgisNGDataStoreFactory.DATABASE.key, cn.getBDDGenerale());
            config.put( PostgisNGDataStoreFactory.USER.key, cn.getUtilisateur());
            config.put( PostgisNGDataStoreFactory.PASSWD.key, cn.getPassword());
            config.put( PostgisNGDataStoreFactory.LOOSEBBOX, true );
            config.put( PostgisNGDataStoreFactory.PREPARED_STATEMENTS, true );
            datastore = DataStoreFinder.getDataStore(config);

            FeatureSource sourcepays =  datastore.getFeatureSource("table_geometrique");//mauritanie_coordonnees

            config = new HashMap();
            config.put( PostgisNGDataStoreFactory.DBTYPE.key, "postgis" );
            config.put( PostgisNGDataStoreFactory.HOST.key, cn.getAdresseIP() );
            config.put( PostgisNGDataStoreFactory.PORT.key, cn.getPort());
            config.put( PostgisNGDataStoreFactory.SCHEMA.key, "public" );
            config.put( PostgisNGDataStoreFactory.DATABASE.key, cn.getBDDOperateur());
            config.put( PostgisNGDataStoreFactory.USER.key, cn.getUtilisateur());
            config.put( PostgisNGDataStoreFactory.PASSWD.key, cn.getPassword());
            config.put( PostgisNGDataStoreFactory.LOOSEBBOX, true );
            config.put( PostgisNGDataStoreFactory.PREPARED_STATEMENTS, true );
            datastore = DataStoreFinder.getDataStore(config);
            FeatureSource sourceregions =  datastore.getFeatureSource("table_valeurs_regions");//base_sites_airtel2

            SLDParser parserRegions = new SLDParser(new StyleFactoryImpl());
            parserRegions.setInput(fichierStyleRegion);
            Style ptStyle = parserRegions.readXML()[0];
            maplayerRegions = new DefaultMapLayer(sourceregions,ptStyle);

            /*SLDParser parserRegion = new SLDParser(new StyleFactoryImpl());
            parserRegion.setInput(fichierStyle);
            Style ptStyleRegion = parserRegion.readXML()[0];
            //maplayerregions = new DefaultMapLayer(sourceregions,ptStyleRegion);
             * 
             */

            PolygonSymbolizer ps = sb.createPolygonSymbolizer(new Color(255, 204, 102), new Color(102,102,102), 1.2);
            Style solstyle = sb.createStyle();
            solstyle.addFeatureTypeStyle(sb.createFeatureTypeStyle(ps));
            maplayerpays = new DefaultMapLayer(sourcepays,solstyle);

            MapLayer []  listemap = new MapLayer [2];
	    listemap[0]=maplayerpays;
            listemap[1]=maplayerRegions;
            mapcontext.addLayers(listemap);
            StreamingRenderer render = new StreamingRenderer();
            HashMap hints = new HashMap();
            hints.put("memoryPreloadingEnabled", Boolean.TRUE );
            render.setRendererHints( hints );
            mappane.setRenderer(render);
            mappane.setDisplayArea(mapcontext.getLayerBounds());
            mappane.setDoubleBuffered(true);
        }
        catch (Exception ex)
        {
            Logger.getLogger(CarteKPI.class.getName()).log(Level.SEVERE, null, ex);
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);
            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe Cartekpi,méthode:buildMapRegionAxe "+DateduJour+" Heure:"+heuredejour+" Erreur: " + ex, cheminfichierlog);
        }
        finally
        {
            cn.closeConnection();
        }
        return mappane;
    }

    public JPanel buildRegion(String fichierStyleRegion,String cheminfichierlog)
    {
        mappane= new JMapPane();
        MapContext mapcontext = new DefaultMapContext();
        mappane.setBackground(new Color(170,210,251));
        try
        {
            cn=new ConnexionBDDOperateur(bddoperateur);
            mapcontext.setTitle("Projet");
            mappane.setMapContext(mapcontext);
            MapLayer maplayerpays,maplayerRegions;
            config = new HashMap();
            config.put( PostgisNGDataStoreFactory.DBTYPE.key, "postgis" );
            config.put( PostgisNGDataStoreFactory.HOST.key, cn.getAdresseIP() );
            config.put( PostgisNGDataStoreFactory.PORT.key, cn.getPort());
            config.put( PostgisNGDataStoreFactory.SCHEMA.key, "public" );
            config.put( PostgisNGDataStoreFactory.DATABASE.key, cn.getBDDGenerale());
            config.put( PostgisNGDataStoreFactory.USER.key, cn.getUtilisateur());
            config.put( PostgisNGDataStoreFactory.PASSWD.key, cn.getPassword());
            config.put( PostgisNGDataStoreFactory.LOOSEBBOX, true );
            config.put( PostgisNGDataStoreFactory.PREPARED_STATEMENTS, true );
            datastore = DataStoreFinder.getDataStore(config);

            FeatureSource sourcepays =  datastore.getFeatureSource("table_geometrique");//mauritanie_coordonnees

            config = new HashMap();
            config.put( PostgisNGDataStoreFactory.DBTYPE.key, "postgis" );
            config.put( PostgisNGDataStoreFactory.HOST.key, cn.getAdresseIP() );
            config.put( PostgisNGDataStoreFactory.PORT.key, cn.getPort());
            config.put( PostgisNGDataStoreFactory.SCHEMA.key, "public" );
            config.put( PostgisNGDataStoreFactory.DATABASE.key, cn.getBDDOperateur());
            config.put( PostgisNGDataStoreFactory.USER.key, cn.getUtilisateur());
            config.put( PostgisNGDataStoreFactory.PASSWD.key, cn.getPassword());
            config.put( PostgisNGDataStoreFactory.LOOSEBBOX, true );
            config.put( PostgisNGDataStoreFactory.PREPARED_STATEMENTS, true );
            datastore = DataStoreFinder.getDataStore(config);
            FeatureSource sourceregions =  datastore.getFeatureSource("table_valeurs_regions");//base_sites_airtel2

            SLDParser parserRegions = new SLDParser(new StyleFactoryImpl());
            parserRegions.setInput(fichierStyleRegion);
            Style ptStyle = parserRegions.readXML()[0];
            maplayerRegions = new DefaultMapLayer(sourceregions,ptStyle);


            PolygonSymbolizer ps = sb.createPolygonSymbolizer(new Color(255, 204, 102), new Color(102,102,102), 1.2);
            Style solstyle = sb.createStyle();
            solstyle.addFeatureTypeStyle(sb.createFeatureTypeStyle(ps));
            maplayerpays = new DefaultMapLayer(sourcepays,solstyle);

            MapLayer []  listemap = new MapLayer [2];
	    listemap[0]=maplayerpays;
            listemap[1]=maplayerRegions;
            mapcontext.addLayers(listemap);
            StreamingRenderer render = new StreamingRenderer();
            HashMap hints = new HashMap();
            hints.put("memoryPreloadingEnabled", Boolean.TRUE );
            render.setRendererHints( hints );
            mappane.setRenderer(render);
            mappane.setDisplayArea(mapcontext.getLayerBounds());
            mappane.setDoubleBuffered(true);
        }
        catch (Exception ex)
        {
            Logger.getLogger(CarteKPI.class.getName()).log(Level.SEVERE, null, ex);
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);
            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe Cartekpi,méthode:buildMapRegionAxe "+DateduJour+" Heure:"+heuredejour+" Erreur: " + ex, cheminfichierlog);
        }
        finally
        {
            cn.closeConnection();
        }
        return mappane;
    }
    
    public JPanel buildMap_AllBTS(String fichierStyle,String cheminfichierlog) throws SQLException
    {
        mappane= new JMapPane();
        MapContext mapcontext= new DefaultMapContext();
        mappane.setBackground(new Color(204,204,204));
        cn=new ConnexionBDDOperateur(bddoperateur);

        MapLayer maplayerBTS,maplayerPays;

        try
        {
            mapcontext.setTitle("Projet");
            mappane.setMapContext(mapcontext);
            config=new HashMap();
            config.put(PostgisNGDataStoreFactory.DBTYPE.key, "postgis" );
            config.put(PostgisNGDataStoreFactory.HOST.key, cn.getAdresseIP());
            config.put(PostgisNGDataStoreFactory.PORT.key, cn.getPort());
            config.put(PostgisNGDataStoreFactory.SCHEMA.key, "public" );
            config.put(PostgisNGDataStoreFactory.DATABASE.key, cn.getBDDGenerale() );
            config.put(PostgisNGDataStoreFactory.USER.key, cn.getUtilisateur());
            config.put(PostgisNGDataStoreFactory.PASSWD.key, cn.getPassword());
            config.put(PostgisNGDataStoreFactory.LOOSEBBOX, true );
            config.put(PostgisNGDataStoreFactory.PREPARED_STATEMENTS, true );
            datastore = DataStoreFinder.getDataStore(config);
            FeatureSource sourcePays =  datastore.getFeatureSource("table_geometrique");

            config=new HashMap();
            config.put( PostgisNGDataStoreFactory.DBTYPE.key, "postgis" );
            config.put( PostgisNGDataStoreFactory.HOST.key, cn.getAdresseIP());
            config.put( PostgisNGDataStoreFactory.PORT.key, cn.getPort());
            config.put( PostgisNGDataStoreFactory.SCHEMA.key, "public" );
            config.put( PostgisNGDataStoreFactory.DATABASE.key, bddoperateur );
            config.put( PostgisNGDataStoreFactory.USER.key, cn.getUtilisateur());
            config.put( PostgisNGDataStoreFactory.PASSWD.key, cn.getPassword());
            config.put(PostgisNGDataStoreFactory.LOOSEBBOX, true );
            config.put(PostgisNGDataStoreFactory.PREPARED_STATEMENTS, true );
            config.put(PostgisNGDataStoreFactory.VALIDATECONN.key, true );
            datastore = DataStoreFinder.getDataStore(config);
            FeatureSource sourceBTS =   datastore.getFeatureSource("table_bts");

            SLDParser parser = new SLDParser(new StyleFactoryImpl());
            parser.setInput(fichierStyle);
            Style ptStyle = parser.readXML()[0];
            //maplayerBTS = new DefaultMapLayer(sourceBTS,ptStyle);
            maplayerBTS = new MapLayer(sourceBTS,ptStyle);
            //  maplayer2 = new  DefaultMapLayer(source1, sb.createStyle(sb.createPolygonSymbolizer(Color.LIGHT_GRAY, Color.BLACK, 1)));
            PolygonSymbolizer ps = sb.createPolygonSymbolizer(new Color(255, 204, 102), new Color(102,102,102), 1.2);
	    Style solstyle = sb.createStyle();
	    //solstyle.addFeatureTypeStyle(sb.createFeatureTypeStyle(ps));
	    solstyle.addFeatureTypeStyle(sb.createFeatureTypeStyle(ps));
            //maplayerPays=new DefaultMapLayer(sourcePays,solstyle);
            maplayerPays=new MapLayer(sourcePays,solstyle);
            MapLayer []  listemap = new MapLayer [2];
            maplayerPays.setQuery(Query.ALL);
            maplayerBTS.setQuery(Query.ALL);
            //maplayerPays.setVisible(true);
            //maplayerBTS.setVisible(true);
            listemap[0]=maplayerPays;
            listemap[1]=maplayerBTS;

            try
            {
                mapcontext.addLayer(maplayerPays);
                mapcontext.addLayer(maplayerBTS);
                //mapcontext.addLayers(listemap);
                //maplayer.setVisible(true);
                StreamingRenderer render = new StreamingRenderer();
                HashMap hints = new HashMap();
                hints.put("memoryPreloadingEnabled", Boolean.TRUE);
                render.setRendererHints(hints);
                mappane.setRenderer(render);
                //mappane.setDisplayArea(mapcontext.getLayerBounds());
                //mappane.setDoubleBuffered(true);
                render.stopRendering();
            }
            catch (Exception ex)
            {
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(CarteKPI.class.getName()).log(Level.SEVERE, null, ex);
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);
            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe Cartekpi,méthode: buildMap_AllBTS "+DateduJour+" Heure:"+heuredejour+" Erreur: " + ex, cheminfichierlog);
        }
        if(cn!=null)
        {
            cn.closeConnection();
        }
        JPanel pfond = new JPanel(new BorderLayout());
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();//PERMET DE RECUPERER LA TAILLE DE L'ECRAN
        java.awt.Rectangle rect = graphicsEnvironment.getMaximumWindowBounds();
        pfond.setSize((int)rect.getWidth(),(int)rect.getHeight());//Fixe la taille de la fenêtre en fonction de la taille de l'écran
        pfond.add(BorderLayout.CENTER,mappane);
        pfond.add(BorderLayout.NORTH, buildTool());
        return  pfond;
    } 

    @Override
    protected void finalize()
    {
        try
        {
            if (DataStoreFinder.getDataStore(config) != null)
            {
                DataStoreFinder.getDataStore(config).dispose();
            }
            if (cn != null)
            {
                cn.closeConnection();
            }
            config.clear();
            super.finalize();
        }
        catch (Throwable throwable)
        {
        }
    }

}