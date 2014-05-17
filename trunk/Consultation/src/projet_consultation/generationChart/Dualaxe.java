package projet_consultation.generationChart;

import java.awt.BasicStroke;
import java.awt.Color;   
import java.awt.Dimension;   
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;  
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.*;   
import org.jfree.chart.axis.*;   
import org.jfree.chart.block.*;   
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;   
import org.jfree.chart.plot.*;   
import org.jfree.chart.renderer.category.LayeredBarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;   
import org.jfree.chart.title.CompositeTitle;   
import org.jfree.chart.title.LegendTitle;   
import org.jfree.data.category.CategoryDataset;   
import org.jfree.data.category.DefaultCategoryDataset;   
import org.jfree.ui.*;   
import org.jfree.util.SortOrder;
import projet_consultation.ConnexionBDD.ConnexionBDDOperateur;
import projet_consultation.ConnexionBDD.Connexion_BDDGenerale;
import projet_consultation.ClassesGenerales.Fichier;
import projet_consultation.ClassesGenerales.Operateur;
import projet_consultation.ClassesGenerales.ParametreKPI;
   
public class Dualaxe extends ApplicationFrame   
{   
    private static final long serialVersionUID = 1L;
    private String kpi1;
    private String kpi2;
    private String kpi3;
    private String nomkpi1;
    private String nomkpi2;
    private String nomkpi3;
    private String cheminfichierlog;
    private String axeY1;
    private String axeY2;

    private Vector<String> vecteurregion = new Vector<String>();
    private Vector<Float> vecteurkpi1 = new Vector<Float>();
    private Vector<Float> vecteurkpi2 = new Vector<Float>();
    private Vector<Float> vecteurkpi3 = new Vector<Float>();
    private CategoryDataset datasetDroite;
    private ConnexionBDDOperateur cn;
    private Operateur operateur; 
    
    public Dualaxe(String nomImage,String cheminfichierlog, String kpi1, String kpi2, String axeY1,String axeY2,Operateur operateur) 
    {
        super("");
        this.kpi1=kpi1;
        this.kpi2=kpi2;

        this.axeY1=axeY1;
        this.axeY2=axeY2;
        this.operateur=operateur;
        this.cheminfichierlog=cheminfichierlog;
        try
        {
            String typekpi1 =null;
            String typekpi2=null;
            //String typekpi3=null;
            Connexion_BDDGenerale cnbdd = Connexion_BDDGenerale.getInstance();
            cnbdd=Connexion_BDDGenerale.getInstance();
            ParametreKPI paramkpi1=cnbdd.getParametresKPIFromKPI(kpi1);
            this.nomkpi1=paramkpi1.getNomKpi();

            ParametreKPI paramkpi2=cnbdd.getParametresKPIFromKPI(kpi2);
            this.nomkpi2=paramkpi2.getNomKpi();
            typekpi1=paramkpi1.getTypekpi();
            typekpi2=paramkpi2.getTypekpi();
            cn=new ConnexionBDDOperateur(operateur.getBddOperateur());
            String requete="select region,"+kpi1+","+kpi2+" from tablevaleurskpi where region!='Global'";
            ResultSet resultat1 = cn.getResultset(requete);
            while(resultat1.next())
            {
                try
                {
                    vecteurregion.add(resultat1.getString("region").trim());
                }
                catch (Exception e)
                {
                    vecteurregion.add(" ");
                }
                try
                {
                    if(typekpi1!=null)
                    {
                        if(typekpi1.equalsIgnoreCase("pourcentage"))
                            vecteurkpi1.add(resultat1.getFloat(kpi1)*100);
                        else
                            vecteurkpi1.add(resultat1.getFloat(kpi1));
                    }
                    else
                        vecteurkpi1.add(resultat1.getFloat(kpi1));
                }
                catch (Exception e)
                {
                    vecteurkpi1.add((float)0);
                }
                try
                {
                    if(typekpi2!=null)
                    {
                        if(typekpi2.equalsIgnoreCase("pourcentage"))
                            vecteurkpi2.add(resultat1.getFloat(kpi2)*100);
                        else
                            vecteurkpi2.add(resultat1.getFloat(kpi2));
                    }
                    else
                        vecteurkpi2.add(resultat1.getFloat(kpi2));
                }
                catch (Exception e)
                {
                    vecteurkpi2.add(0F);
                }
            }

            if(typekpi1!=null && typekpi2!=null)
            {
                if(typekpi1.equalsIgnoreCase("pourcentage") && typekpi2.equalsIgnoreCase("pourcentage"))
                {
                     datasetDroite=null;
                    //JFreeChart jfreechart=createChartBar();
                    JFreeChart jfreechart=createChart(axegauche(), datasetDroite);
                    createImage(nomImage, jfreechart);
                    ChartPanel chartpanel = new ChartPanel(jfreechart);
                    chartpanel.setPreferredSize(new Dimension(600,400));
                    setContentPane(chartpanel);
                }
                else if(typekpi2.equalsIgnoreCase("pourcentage"))
                {
                    CategoryDataset datasetG=axegauche_single();
                    datasetDroite=axedroite_single();
                    JFreeChart jfreechart = createChart(datasetG,datasetDroite);
                    createImage(nomImage, jfreechart);
                    ChartPanel chartpanel = new ChartPanel(jfreechart);
                    chartpanel.setPreferredSize(new Dimension(600,400));
                    setContentPane(chartpanel);
                }
                else
                {
                    CategoryDataset datasetG=axegauche();
                    datasetDroite=axedroite_single();
                    datasetDroite=null;
                    JFreeChart jfreechart = createChart(datasetG,datasetDroite);
                    createImage(nomImage, jfreechart);
                    ChartPanel chartpanel = new ChartPanel(jfreechart);
                    chartpanel.setPreferredSize(new Dimension(580,380));
                    setContentPane(chartpanel);
                }
            }
            else if(typekpi1==null)
            {
            }
            cn.closeConnection();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(BarChart3D.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    public Dualaxe(String nomImage,String cheminfichierlog, String kpi1, String kpi2, String kpi3,String axeY1,String axeY2,Operateur operateur)
    {
        super("");
        this.kpi1=kpi1;
        this.kpi2=kpi2;
        this.kpi3=kpi3;
        
        this.axeY1=axeY1;
        this.axeY2=axeY2;
        this.operateur=operateur;
        this.cheminfichierlog=cheminfichierlog;
        try
        {
            cn=new ConnexionBDDOperateur();
            String requete="Select * from tableparametrekpi where kpi='"+kpi1+"' or kpi='"+kpi2+"' or kpi='"+kpi3+"'";
            ResultSet resultat = cn.getResultset(requete);
            String typekpi1 =null;
            String typekpi2=null;
            String typekpi3=null;
            Connexion_BDDGenerale cnbdd=Connexion_BDDGenerale.getInstance();
            ParametreKPI paramkpi1=cnbdd.getParametresKPIFromKPI(kpi1);
            ParametreKPI paramkpi2=cnbdd.getParametresKPIFromKPI(kpi2);
            ParametreKPI paramkpi3=cnbdd.getParametresKPIFromKPI(kpi3);
            
            this.nomkpi1=paramkpi1.getNomKpi();
            this.nomkpi2 = paramkpi2.getNomKpi();
            this.nomkpi3=paramkpi3.getNomKpi();
            typekpi1=paramkpi1.getTypekpi();
            typekpi2=paramkpi2.getTypekpi();
            typekpi3=paramkpi3.getTypekpi();

            cn=new ConnexionBDDOperateur(operateur.getBddOperateur());
            requete="select region,"+kpi1+","+kpi2+","+kpi3+" from tablevaleurskpi where region!='Global'";
            ResultSet resultat1 = cn.getResultset(requete);
            while(resultat1.next())
            {
                float val1=0,val2=0,val3=0;
                try
                {
                    val1 = resultat1.getFloat(kpi1);
                } catch (Exception ex)
                {
                    val1=0;
                }
                try
                {
                    val2 = resultat1.getFloat(kpi2);
                } catch (Exception ex)
                {
                    val2=0;
                }
                try
                {
                    val3 = resultat1.getFloat(kpi3);
                } catch (Exception ex)
                {
                    val3=0;
                }
                try
                {
                    vecteurregion.add(resultat1.getString("region").trim());
                }
                catch (Exception e)
                {
                    vecteurregion.add(" ");
                }
                if (typekpi1!=null)
                {
                    if(typekpi1.equalsIgnoreCase("pourcentage"))
                        vecteurkpi1.add(val1*100);
                            //vecteurkpi1.add(resultat1.getDouble(kpi1)*100);
                    else
                        vecteurkpi1.add(val1);
                }
                else
                {
                    vecteurkpi1.add(val1);
                }
                if(typekpi2.equalsIgnoreCase("pourcentage"))
                    vecteurkpi2.add(val2*100);
                else
                    vecteurkpi2.add(val2);
                if(typekpi3.equalsIgnoreCase("pourcentage"))
                    vecteurkpi3.add(val3*100);
                else
                    vecteurkpi3.add(val3);
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(BarChart3D.class.getName()).log(Level.SEVERE, null, ex);
        }
        CategoryDataset datasetGauche=axegauche();
        datasetDroite=axedroite();
        JFreeChart jfreechart = createChart(datasetGauche,datasetDroite);
        createImage(nomImage, jfreechart);
        ChartPanel chartpanel = new ChartPanel(jfreechart);
        chartpanel.setPreferredSize(new Dimension(600,400));
        setContentPane(chartpanel);
        cn.closeConnection();
    }
   
    private CategoryDataset axegauche()
    {
        final DefaultCategoryDataset datasetGauche=new DefaultCategoryDataset();
        for (int i = 0; i < vecteurkpi1.size(); i++)
        {
            datasetGauche.addValue(vecteurkpi1.get(i),nomkpi1, vecteurregion.get(i));
            datasetGauche.addValue(vecteurkpi2.get(i),nomkpi2, vecteurregion.get(i));
        }
        return datasetGauche;
    }

    private CategoryDataset axegauche_single()
    {
        final DefaultCategoryDataset datasetGauche=new DefaultCategoryDataset();
        for (int i = 0; i < vecteurkpi1.size(); i++)
        {
            datasetGauche.addValue(vecteurkpi1.get(i),nomkpi1, vecteurregion.get(i));
        }
        return datasetGauche;
    }
    
    private CategoryDataset axedroite()
    {   
    	final DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
        for (int i = 0; i < vecteurkpi3.size(); i++)
        {
            dataset1.addValue(vecteurkpi3.get(i), nomkpi3, vecteurregion.get(i));
        }
        return dataset1;
     }   
   
    private CategoryDataset axedroite_single()
    {
    	final DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
        for (int i = 0; i < vecteurkpi2.size(); i++)
        {
            dataset1.addValue(vecteurkpi2.get(i), nomkpi2, vecteurregion.get(i));
        }
        return dataset1;
     }

    private JFreeChart createChart(CategoryDataset datasetG,CategoryDataset datasetD)
    {
        JFreeChart chart = ChartFactory.createBarChart(" ", "",this.axeY1,datasetG,PlotOrientation.VERTICAL,false,true,false);

        chart.setBackgroundPaint(Color.white);
        CategoryPlot plot = chart.getCategoryPlot();
    	plot.setBackgroundPaint(new Color(238, 238, 255));
    	plot.setDomainGridlinePaint(Color.white);
    	plot.setDomainGridlinesVisible(true);
    	plot.setRangeGridlinePaint(Color.white);
    	plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD); 
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setLabelFont(new Font("Dialog",Font.BOLD,12));
        rangeAxis.setTickLabelFont(new Font("Dialog",Font.BOLD,12));
    	//rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.blue, 0.0f,0.0f, Color.blue);
    	GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, new Color(0x16, 0xB8, 0x4E), 0.0f,0.0f, new Color(0x16, 0xB8, 0x4E));

        LayeredBarRenderer renderer = new LayeredBarRenderer();
        renderer.setDrawBarOutline(false);
        plot.setRenderer(renderer);

        renderer.setSeriesPaint(0, gp1);
        renderer.setSeriesPaint(1, gp2);
        LineAndShapeRenderer lineandshaperenderer = new LineAndShapeRenderer();
        lineandshaperenderer.setToolTipGenerator(new StandardCategoryToolTipGenerator());
        GradientPaint gp3 = new GradientPaint(0.0f, 0.0f,  Color.red ,0f, 0.0f, Color.red);
        lineandshaperenderer.setSeriesPaint(0, gp3);
        lineandshaperenderer.setSeriesStroke(0, new BasicStroke(3F));//permet d'épaissir la courbe numero 0
        CategoryDataset categorydataset=null;
        if(datasetD!=null)
        {
            categorydataset=datasetD;
            plot.setDataset(1, categorydataset);
            plot.mapDatasetToRangeAxis(1, 1);

            NumberAxis numberaxis = new NumberAxis(this.axeY2);
            plot.setRangeAxis(1, numberaxis);
            plot.setRenderer(1, lineandshaperenderer);
            lineandshaperenderer.getPlot().getRangeAxis().setLabelFont(new Font("Dialog",Font.BOLD,12));
            lineandshaperenderer.getPlot().getRangeAxis().setTickLabelFont(new Font("Dialog",Font.BOLD,12));
            
            Stroke stroke = new BasicStroke(3.0F);
            plot.getRenderer(1).setSeriesStroke(0, stroke);
        }

    	plot.setRowRenderingOrder(SortOrder.DESCENDING);

        CategoryAxis categoryaxis = plot.getDomainAxis();
    	categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        categoryaxis.setTickLabelFont(new Font("Dialog",Font.BOLD,12));

        BlockContainer blockcontainer = new BlockContainer(new BorderArrangement());

        LegendTitle legendtitle = new LegendTitle(plot.getRenderer(0));
    	legendtitle.setMargin(new RectangleInsets(2D, 2D, 2D, 2D));   
    	legendtitle.setBorder(new BlockBorder());   

        LegendTitle legendtitle1 = new LegendTitle(plot.getRenderer(1));
    	legendtitle1.setMargin(new RectangleInsets(2D, 2D, 2D, 2D));
    	legendtitle1.setBorder(new BlockBorder());

    	blockcontainer.add(legendtitle, RectangleEdge.LEFT);
    	if(datasetD!=null)
        {
            blockcontainer.add(legendtitle1, RectangleEdge.RIGHT);
        }

        blockcontainer.add(new EmptyBlock(2000D, 0.0D));
    	CompositeTitle compositetitle = new CompositeTitle(blockcontainer);   
    	compositetitle.setPosition(RectangleEdge.BOTTOM);
    	chart.addSubtitle(compositetitle);
        return chart;
    }
    
    private void createImage(String nomImage,JFreeChart chart)
    {
        Dimension d=new Dimension(900, 270);
        try
        {
            GraphicsEnvironment graphicsEnvironment=GraphicsEnvironment.getLocalGraphicsEnvironment();
            Rectangle rect=graphicsEnvironment.getMaximumWindowBounds();
            double largeur=rect.getWidth()/2.1334;
            double longeur=rect.getHeight()/3.851;
            if(longeur!=Double.NaN && longeur!=Double.NEGATIVE_INFINITY && longeur!=Double.POSITIVE_INFINITY && largeur!=Double.NaN  && largeur!=Double.NEGATIVE_INFINITY && largeur!=Double.POSITIVE_INFINITY)
            {
                d=new Dimension((int)largeur, (int)longeur);
            }
        } catch (Exception e)
        {
            d=new Dimension(900, 270);
            e.printStackTrace();
        }
    	try
    	{
            File f1 = new File(nomImage);
            ChartUtilities.saveChartAsJPEG(f1, chart,d.width, d.height);
	}
    	catch (Exception e)
    	{
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe Dualaxe "+heuredejour+" Problem occurred creating chart.", cheminfichierlog);
	}
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
    }
    
} 