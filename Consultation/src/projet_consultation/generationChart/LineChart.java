package projet_consultation.generationChart;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.TextAnchor;
import projet_consultation.ConnexionBDD.ConnexionBDDOperateur;
import projet_consultation.ClassesGenerales.Fichier;
import projet_consultation.ClassesGenerales.Operateur;
import projet_consultation.ClassesGenerales.ParametreKPI;
import projet_consultation.ConnexionBDD.Connexion_BDDGenerale;


public class LineChart extends ApplicationFrame
{
    private String kpi1;
    private String nomkpi1;

    private String kpi2;
    private String nomkpi2;

    private String kpi3;
    private String nomkpi3;
    private String libelleaxeY;
    private List<Double> valeurskpi1=new ArrayList<Double>();
    private List<Double> valeurskpi2=new ArrayList<Double>();
    private List<Double> valeurskpi3=new ArrayList<Double>();
    private List<String> region=new ArrayList<String>();
    private ConnexionBDDOperateur cn;
    private Operateur operateur;

    public LineChart(String nomImage, String cheminfichierlog, String kpi1,  String kpi2, String libelleaxeY,Operateur operateur)
    {
        super("");
        try
        {
            ParametreKPI parametreKPI = new ParametreKPI();
            Connexion_BDDGenerale cnbdd = Connexion_BDDGenerale.getInstance();
            parametreKPI = cnbdd.getParametresKPIFromKPI(kpi1);
            this.kpi1 = kpi1.toLowerCase().trim();
            this.nomkpi1 = parametreKPI.getNomKpi();
            parametreKPI = cnbdd.getParametresKPIFromKPI(kpi2);
            this.kpi2 = kpi2.toLowerCase().trim();
            this.nomkpi2 = parametreKPI.getNomKpi();
            this.libelleaxeY = libelleaxeY;
            this.operateur = operateur;
            this.getvalueFromBDD2();
            final CategoryDataset dataset = createDataset_deuxcourbes();
            final JFreeChart chart = createChart(dataset);
            final ChartPanel chartPanel = new ChartPanel(chart);
            ////methode qui permet de créer l'image à partir du chart
            this.CreateImageChart(nomImage, chart, cheminfichierlog);
        }
        catch (SQLException ex)
        {
            Logger.getLogger(LineChart.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public LineChart(String nomImage, String cheminfichierlog, String kpi1, String kpi2,  String kpi3, String libelleaxeY,Operateur operateur)
    {
        super("");
        try
        {
            ParametreKPI parametreKPI = new ParametreKPI();
            Connexion_BDDGenerale cnbdd = Connexion_BDDGenerale.getInstance();

            this.kpi1 = kpi1.toLowerCase().trim();
            parametreKPI = cnbdd.getParametresKPIFromKPI(kpi1);
            this.nomkpi1 = parametreKPI.getNomKpi();

            this.kpi2 = kpi2.toLowerCase().trim();
            parametreKPI = cnbdd.getParametresKPIFromKPI(kpi2);
            this.nomkpi2 = parametreKPI.getNomKpi();

            this.kpi3 = kpi3.toLowerCase().trim();
            parametreKPI = cnbdd.getParametresKPIFromKPI(kpi3);
            this.nomkpi3 = parametreKPI.getNomKpi();
            
            this.libelleaxeY = libelleaxeY;
            this.operateur = operateur;
            this.getvalueFromBDD3();
            final CategoryDataset dataset = createDataset_troiscourbes();
            final JFreeChart chart = createChart(dataset);
            final ChartPanel chartPanel = new ChartPanel(chart);
            ////methode qui permet de créer l'image à partir du chart
            this.CreateImageChart(nomImage, chart, cheminfichierlog);
        } 
        catch (SQLException ex)
        {
            Logger.getLogger(LineChart.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getvalueFromBDD2()
    {
        try
        {
            cn=new ConnexionBDDOperateur(operateur.getBddOperateur());
            String requete="Select "+kpi1+","+kpi2+",region from tablevaleurskpi where region!='Global' ";
            ResultSet resultat = cn.getResultset(requete);
            while (resultat.next())
            {
                double val1=(double)((int)(resultat.getDouble(kpi1)*10000))/10000;
                double val2=(double)((int)(resultat.getDouble(kpi2)*10000))/10000;
                region.add(resultat.getString("region").trim());
                valeurskpi1.add(val1*100);
                valeurskpi2.add(val2*100);
            }
            cn.closeConnection();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(LineChart.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void getvalueFromBDD3()
    {
        try
        {
            cn=new ConnexionBDDOperateur(operateur.getBddOperateur());
            String requete="Select "+kpi1+","+kpi2+", "+kpi3+",region from tablevaleurskpi where trim(region)!='Global' ";
            ResultSet resultat = cn.getResultset(requete);
            while (resultat.next())
            {
                double val1=(double)((int)(resultat.getDouble(kpi1)*10000))/10000;
                double val2=(double)((int)(resultat.getDouble(kpi2)*10000))/10000;
                double val3=(double)((int)(resultat.getDouble(kpi3)*10000))/10000;
                region.add(resultat.getString("region").trim());
                valeurskpi1.add(val1*100);
                valeurskpi2.add(val2*100);
                valeurskpi3.add(val3*100);
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(LineChart.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private CategoryDataset createDataset_deuxcourbes()
    {
        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for(int i=0;i<valeurskpi1.size();i++)
        {

            double val1=(double)valeurskpi1.get(i);
            val1=(double)((int)(val1*10000))/10000;

            double val2=(double)valeurskpi2.get(i);
            val2=(double)((int)(val2*10000))/10000;

            dataset.addValue(val1,nomkpi1,region.get(i));
            dataset.addValue(val2,nomkpi2,region.get(i));
        }
        
        
      return dataset;
    }

    private CategoryDataset createDataset_troiscourbes()
    {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for(int i=0;i<valeurskpi1.size();i++)
        {
            double val1=(double)((int)(valeurskpi1.get(i)*10000))/10000;
            double val2=(double)((int)(valeurskpi2.get(i)*10000))/10000;
            double val3=(double)((int)(valeurskpi3.get(i)*10000))/10000;
            dataset.addValue(val1,nomkpi1,region.get(i));
            dataset.addValue(val2,nomkpi2,region.get(i));
            dataset.addValue(val3,nomkpi3,region.get(i));
        }
        return dataset;
    }

    private JFreeChart createChart(final CategoryDataset dataset)
    {

        // create the chart...
        final JFreeChart chart = ChartFactory.createLineChart(
            "",       // chart title
            "Region",                    // domain axis label
            libelleaxeY,                   // range axis label
            dataset,                   // data
            PlotOrientation.VERTICAL,  // orientation
            true,                      // include legend
            true,                      // tooltips
            false                      // urls
        );

        chart.setBackgroundPaint(Color.white);
        final CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.white);
        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        plot.getDomainAxis().setTickLabelFont(new Font("Dialog",Font.BOLD,12));
        plot.getDomainAxis().setTickLabelsVisible(true);
        // customise the range axis...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setLabelFont(new Font("Dialog",Font.BOLD,12));
        rangeAxis.setTickLabelFont(new Font("Dialog",Font.BOLD,12));
        //rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        //rangeAxis.setAutoRangeIncludesZero(true);
         //Stroke stroke = new BasicStroke(3F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
         Stroke stroke = new BasicStroke(3.0F);
        // customise the renderer...
         plot.getRenderer().setSeriesStroke(0, stroke);
         plot.getRenderer().setSeriesStroke(1, stroke);
         plot.getRenderer().setSeriesStroke(2, stroke);
         plot.getRenderer().setBaseItemLabelsVisible(true);
         plot.getRenderer().setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.CENTER));

         final LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setBaseItemLabelsVisible(true);
        renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.CENTER));
        renderer.setSeriesShapesVisible(0,true);//permet d'afficher les points de la courbes
        renderer.setSeriesShapesVisible(1,true);//permet d'afficher les points de la courbes
        renderer.setSeriesShapesVisible(2,true);//permet d'afficher les points de la courbes
        renderer.setSeriesItemLabelsVisible(0, true);
        renderer.setSeriesItemLabelsVisible(1, true);
        renderer.setSeriesItemLabelsVisible(2, true);
        
        plot.setRangeAxis(rangeAxis);
        plot.setRenderer(renderer);
        chart.setBorderStroke(stroke);
        return chart;
    }

    private void CreateImageChart(String nomImage,JFreeChart chart,String cheminfichierlog)
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
            ChartUtilities.saveChartAsJPEG(f1,chart,d.width,d.height);
        }
        catch (Exception ex)
        {
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe LineChart "+heuredejour+" Problem occurred creating chart.", cheminfichierlog);
         }
    }
    
    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
    }

}