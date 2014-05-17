package projet_consultation.Calcul.demoCalcul;

import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.postgis.PostgisNGDataStoreFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.swing.JMapPane;
import org.geotools.swing.action.PanAction;
import org.geotools.swing.action.ZoomInAction;
import org.geotools.swing.action.ZoomOutAction;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.FeatureId;
import org.geotools.feature.FeatureCollection;
import org.geotools.styling.PolygonSymbolizer;
import projet_consultation.ConnexionBDD.Connexion_BDDGenerale;

public class CarteKPIFiltre
{
    private JMapPane mappane= new JMapPane();
    private MapContext mapcontext;// = new DefaultMapContext();
    ResultSet result = null;
    String requete;
    Vector <Float> tbvalkpi = new Vector <Float> ();
    public static StyleBuilder  sb = new StyleBuilder();
    private String bdd;
    private Map config = new HashMap();
    private  DataStore datastore;
    StreamingRenderer render;
    private static  Vector <Integer> idregion = new Vector <Integer> ();

    public CarteKPIFiltre()
    {
    }

    public CarteKPIFiltre(String bdd)
    {
        this.bdd = bdd;
    }


    @SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })

    public JPanel  carteKPI_FilterParregion (String fichierStyle,String cheminfichierlog,String nomRegion) throws InterruptedException
    {
        JPanel pfond = new JPanel(new BorderLayout());
        pfond.setSize(900,1100);
        pfond.add(BorderLayout.CENTER,buildMapfilterparregion(fichierStyle, cheminfichierlog,nomRegion));
        pfond.add(BorderLayout.NORTH, buildTool());
        return  pfond;
    }


    public JMenuBar buildTool()
    {
        JMenuBar menu = new JMenuBar();
	JButton zinButton = new JButton(new ZoomInAction(mappane,true));
	JButton zoutButton = new JButton( new ZoomOutAction(mappane,true));
	JButton panButton = new JButton(new PanAction(mappane,true));
	JButton resetAction = new JButton(new org.geotools.swing.action.ResetAction(mappane, true));
	JButton infoAction = new JButton(new org.geotools.swing.action.InfoAction(mappane, true));
	menu.add(zinButton);
	menu.add(zoutButton);
	menu.add(panButton);
        menu.add(resetAction);
        menu.add(infoAction);
        return menu;
    }



    public JPanel buildMapfilterparregion1(String fichierStyle, String cheminfichierlog, String nomRegion)
    {
        mapcontext = new DefaultMapContext();
        //mappane.setBackground(new Color(93,151,180));
        mappane.setBackground(new Color(87, 168, 208));
        //mappane.setBackground(new Color(255,255,255));
        Connexion_BDDGenerale cnGen = null;
        try
        {
            cnGen = Connexion_BDDGenerale.getInstance();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(CarteKPIFiltre.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
        {
            mapcontext.setTitle("Projet");
            mappane.setMapContext(mapcontext);
            MapLayer maplayer;
            config = new HashMap();
            config.put(PostgisNGDataStoreFactory.DBTYPE.key, "postgis");
            config.put(PostgisNGDataStoreFactory.HOST.key, cnGen.getAdresseIP());
            config.put(PostgisNGDataStoreFactory.PORT.key, cnGen.getPort());
            config.put(PostgisNGDataStoreFactory.SCHEMA.key, "public");
            config.put(PostgisNGDataStoreFactory.DATABASE.key, cnGen.getBDDGenerale());
            config.put(PostgisNGDataStoreFactory.USER.key, cnGen.getUtilisateur());
            config.put(PostgisNGDataStoreFactory.PASSWD.key, cnGen.getPassword());
            config.put(PostgisNGDataStoreFactory.LOOSEBBOX, true);
            config.put(PostgisNGDataStoreFactory.PREPARED_STATEMENTS, true);
            datastore = DataStoreFinder.getDataStore(config);
            //FeatureSource source1 =  datastore.getFeatureSource("tunisie_coordonnees");
            FeatureSource source1 = datastore.getFeatureSource("table_geometrique");
            idregion.removeAllElements();
            //requete ="select gid from tunisie_coordonnees where adm2="+"'"+nomRegion+"'";
            requete = "select gid,adm2 from table_geometrique where adm2='" + nomRegion + "'";
            result = cnGen.getResultset(requete);

            while (result.next())
            {
                idregion.addElement(result.getInt("gid"));
            }
            //***************************Debut ajout code filtrage ********************************************************************************
            Filter filter;
            FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
            Set<FeatureId> fids = new HashSet<FeatureId>();

            for (int i = 0; i < idregion.size(); i++)
            {
                FeatureId fid = ff.featureId("table_geometrique." + idregion.get(i));
                fids.add(fid);
            }
            System.out.println("*************************** filter1 : " + fids);
            filter = ff.id(fids);
            FeatureCollection<SimpleFeatureType, SimpleFeature> source = source1.getFeatures(filter);
            //FeatureIterator iterator = source.features();

            //***************************Fin ajout code filtrage****************************************************************************

            SLDParser parser = new SLDParser(new StyleFactoryImpl());
            parser.setInput(fichierStyle);
            Style ptStyle = parser.readXML()[0];
            maplayer = new DefaultMapLayer(source, ptStyle);
            // maplayer = new  DefaultMapLayer(source1, sb.createStyle(sb.createPolygonSymbolizer(Color.LIGHT_GRAY, Color.BLACK, 1)));
            maplayer.setQuery(Query.ALL);
            mapcontext.addLayer(maplayer);
            maplayer.setVisible(true);
            render = new StreamingRenderer();
            HashMap hints = new HashMap();
            hints.put("memoryPreloadingEnabled", Boolean.TRUE);
            render.setRendererHints(hints);
            mappane.setRenderer(render);
            mappane.setDisplayArea(mapcontext.getLayerBounds());
        }
        catch (Exception ex)
        {
            Logger.getLogger(CarteKPIFiltre.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mappane;
    }
    
    public JPanel buildMapfilterparregion(String fichierStyle, String cheminfichierlog, String nomRegion)
    {
        mapcontext = new DefaultMapContext();
        //mappane.setBackground(new Color(93,151,180));
        mappane.setBackground(new Color(87, 168, 208));
        //mappane.setBackground(new Color(255,255,255));
        Connexion_BDDGenerale cnGen = null;
        try
        {
            cnGen = Connexion_BDDGenerale.getInstance();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(CarteKPIFiltre.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
        {
            mapcontext.setTitle("Projet");
            mappane.setMapContext(mapcontext);
            MapLayer maplayer;
            config = new HashMap();
            config.put(PostgisNGDataStoreFactory.DBTYPE.key, "postgis");
            config.put(PostgisNGDataStoreFactory.HOST.key, cnGen.getAdresseIP());
            config.put(PostgisNGDataStoreFactory.PORT.key, cnGen.getPort());
            config.put(PostgisNGDataStoreFactory.SCHEMA.key, "public");
            config.put(PostgisNGDataStoreFactory.DATABASE.key, cnGen.getBDDGenerale());
            config.put(PostgisNGDataStoreFactory.USER.key, cnGen.getUtilisateur());
            config.put(PostgisNGDataStoreFactory.PASSWD.key, cnGen.getPassword());
            config.put(PostgisNGDataStoreFactory.LOOSEBBOX, true);
            config.put(PostgisNGDataStoreFactory.PREPARED_STATEMENTS, true);
            datastore = DataStoreFinder.getDataStore(config);
            //FeatureSource source1 =  datastore.getFeatureSource("tunisie_coordonnees");
            FeatureSource source1 = datastore.getFeatureSource("table_geometrique");
            idregion.removeAllElements();
            //requete ="select gid from tunisie_coordonnees where adm2="+"'"+nomRegion+"'";
            requete = "select gid,adm2 from table_geometrique where adm2='" + nomRegion + "'";
            result = cnGen.getResultset(requete);

            while (result.next())
            {
                idregion.addElement(result.getInt("gid"));
            }
            //***************************Debut ajout code filtrage ********************************************************************************
            Filter filter;
            FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
            Set<FeatureId> fids = new HashSet<FeatureId>();

            for (int i = 0; i < idregion.size(); i++)
            {
                FeatureId fid = ff.featureId("table_geometrique." + idregion.get(i));
                fids.add(fid);
            }
            System.out.println("*************************** filter1 : " + fids);
            filter = ff.id(fids);
            FeatureCollection<SimpleFeatureType, SimpleFeature> source = source1.getFeatures(filter);
            //FeatureIterator iterator = source.features();

            //***************************Fin ajout code filtrage****************************************************************************
            PolygonSymbolizer ps = sb.createPolygonSymbolizer(new Color(255, 204, 102), new Color(102,102,102), 1.2);
            Style solstyle = sb.createStyle();
            solstyle.addFeatureTypeStyle(sb.createFeatureTypeStyle(ps));

            /*SLDParser parser = new SLDParser(new StyleFactoryImpl());
            parser.setInput(fichierStyle);
             *
             */

             //Style ptStyle = parser.readXML()[0];
            //maplayer = new DefaultMapLayer(source, ptStyle);
            //maplayer = new DefaultMapLayer(source, ptStyle);
            
            maplayer = new DefaultMapLayer(source,solstyle);
            // maplayer = new  DefaultMapLayer(source1, sb.createStyle(sb.createPolygonSymbolizer(Color.LIGHT_GRAY, Color.BLACK, 1)));
            maplayer.setQuery(Query.ALL);
            mapcontext.addLayer(maplayer);
            maplayer.setVisible(true);
            render = new StreamingRenderer();
            HashMap hints = new HashMap();
            hints.put("memoryPreloadingEnabled", Boolean.TRUE);
            render.setRendererHints(hints);
            mappane.setRenderer(render);
            mappane.setDisplayArea(mapcontext.getLayerBounds());
        }
        catch (Exception ex)
        {
            Logger.getLogger(CarteKPIFiltre.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mappane;
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
    }

    public static void main(String args[])
    {
//        java.awt.EventQueue.invokeLater(new Runnable() {
//        public void run()
//        {
//             // new CarteKPI();
//            }
//        });
        System.out.println("333333333333333333");
    }
}