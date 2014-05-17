package projet_consultation.generationChart;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.File;
import java.util.Date;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import java.text.SimpleDateFormat;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import projet_consultation.ConnexionBDD.ConnexionBDDOperateur;
import projet_consultation.ConnexionBDD.Connexion_BDDGenerale;
import projet_consultation.ClassesGenerales.Fichier;
import projet_consultation.ClassesGenerales.Operateur;
import projet_consultation.ClassesGenerales.ParametreKPI;


public class OverlaidBarchart extends ApplicationFrame
{
    private String nomkpi1;
    private String nomkpi2;
    private String nomkpi3;
    private String kpi1;
    private String kpi2;
    private String kpi3;
    private String nomImage;
    private String cheminfichierlog;
    private List<Double> valeurkpi1=new ArrayList<Double>();
    private List<Double> valeurkpi2=new ArrayList<Double>();
    private List<Double> valeurkpi3=new ArrayList<Double>();
    private List<String> region=new ArrayList<String>();
    private String typekpi1=null;
    private String typekpi2=null;
    private String typekpi3=null;
    private Operateur operateur;
    private ConnexionBDDOperateur cn;

    public OverlaidBarchart(String nomImage,String cheminfichierlog,String kpi1,String kpi2,Operateur operateur)
    {
        super("");
        this.kpi1=kpi1.toLowerCase().trim();
        this.kpi2=kpi2.toLowerCase().trim();
        this.operateur=operateur;
        this.cheminfichierlog=cheminfichierlog.toLowerCase().trim();
        this.nomImage=nomImage.trim();

        try
        {
            cn=new ConnexionBDDOperateur();
            String requete="Select * from tableparametrekpi where kpi='"+kpi1+"' or kpi='"+kpi2+"'";

            Connexion_BDDGenerale cnbdd=Connexion_BDDGenerale.getInstance();

            ParametreKPI paramkpi1=cnbdd.getParametresKPIFromKPI(kpi1);
            typekpi1=paramkpi1.getTypekpi();
            this.nomkpi1 = paramkpi1.getNomKpi();

            ParametreKPI paramkpi2=cnbdd.getParametresKPIFromKPI(kpi2);
            this.nomkpi2 = paramkpi2.getNomKpi();
            typekpi2=paramkpi2.getTypekpi();

            cn=new ConnexionBDDOperateur(operateur.getBddOperateur());
            requete="Select * from tablevaleurskpi where region!='Global'";
            ResultSet resultat = cn.getResultset(requete);
            while (resultat.next())
            {
                double val1=0,val2=0;
                try
                {
                    val1 = resultat.getDouble(kpi1);
                    val1 = (float) ((int) (val1 * 10000)) / 10000;
                }
                catch (Exception ex)
                {
                    val1=0;
                }
                try
                {
                    val2 = resultat.getDouble(kpi2);
                    val2 = (float) ((int) (val2 * 10000)) / 10000;
                } catch (Exception ex)
                {
                    val2=0;
                }
                try
                {
                    region.add(resultat.getString("region").trim());
                } catch (Exception e)
                {
                    region.add(resultat.getString(" "));
                }
                try
                {
                    if(typekpi1!=null)
                    {
                        if(typekpi1.equalsIgnoreCase("pourcentage"))
                        {
                            valeurkpi1.add(val1*100);
                        }
                        else
                        {
                            valeurkpi1.add(val1);
                        }
                    }
                    else
                    {
                        valeurkpi1.add(val1);
                    }
                } catch (Exception e)
                {
                    valeurkpi1.add(0D);
                }
                try
                {
                    if(typekpi2!=null)
                    {
                        if(typekpi2.equalsIgnoreCase("pourcentage"))
                        {
                            valeurkpi2.add(val2*100);
                        }
                        else
                        {
                            valeurkpi2.add(val2);
                        }
                    }
                    else
                    {
                        valeurkpi2.add(val2);
                    }
                } catch (Exception e)
                {
                    valeurkpi2.add(0D);
                }
            }
            this.createDatasetandChart2();
            cn.closeConnection();
        } catch (Exception ex)
        {
            Logger.getLogger(OverlaidBarchart.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //methode permettant de tracer l'overlaidBarchart lorsqu'on passe deux KPI en parametre
    public OverlaidBarchart(String nomImage,String cheminfichierlog,String kpi1,String kpi2, String kpi3,Operateur operateur) throws SQLException
    {
        super("");
        this.operateur = operateur;
        this.kpi1 = kpi1.toLowerCase().trim();
        this.kpi2 = kpi2.toLowerCase().trim();
        this.kpi3 = kpi3.toLowerCase().trim();
        this.cheminfichierlog = cheminfichierlog.trim();
        this.nomImage = nomImage.trim();
        cn = new ConnexionBDDOperateur();
        //recuperation des type de KPI
        Connexion_BDDGenerale cnbdd = Connexion_BDDGenerale.getInstance();
        ParametreKPI paramkpi1 = cnbdd.getParametresKPIFromKPI(kpi1);
        ParametreKPI paramkpi2 = cnbdd.getParametresKPIFromKPI(kpi2);
        ParametreKPI paramkpi3 = cnbdd.getParametresKPIFromKPI(kpi3);
        this.nomkpi1 = paramkpi1.getNomKpi();
        this.nomkpi2 = paramkpi2.getNomKpi();
        this.nomkpi3 = paramkpi3.getNomKpi();
        typekpi1 = paramkpi1.getTypekpi();
        typekpi2 = paramkpi2.getTypekpi();
        typekpi3 = paramkpi3.getTypekpi();

        cn = new ConnexionBDDOperateur(operateur.getBddOperateur());
        String requete = "Select * from tablevaleurskpi where region!='Global'";
        ResultSet resultat = cn.getResultset(requete);
        while (resultat.next())
        {
            double val1 = 0, val2 = 0, val3 = 0;
            try
            {
                val1 = resultat.getDouble(kpi1);
                val1 = (float) ((int) (val1 * 10000)) / 10000;
            } catch (Exception ex) {
                val1 = 0;
            }
            try {
                val2 = resultat.getDouble(kpi2);
                val2 = (float) ((int) (val2 * 10000)) / 10000;
            } catch (Exception ex) {
                val2 = 0;
            }
            try {
                val3 = resultat.getDouble(kpi3);
                val3 = (float) ((int) (val3 * 10000)) / 10000;
            } catch (Exception ex) {
                val3 = 0;
            }
            try {
                region.add(resultat.getString("region").trim());
            } catch (Exception e) {
                region.add(resultat.getString(" "));
            }
            try
            {
                if (typekpi1 != null)
                {
                    if (typekpi1.equalsIgnoreCase("pourcentage"))
                    {
                        valeurkpi1.add(val1 * 100);
                    } else {
                        valeurkpi1.add(val1);
                    }
                } else {
                    valeurkpi1.add(val1);
                }
            } catch (Exception e) {
                valeurkpi1.add(0D);
            }
            try
            {
                if (typekpi2 != null)
                {
                    if (typekpi2.equalsIgnoreCase("pourcentage"))
                    {
                        valeurkpi2.add(val2 * 100);
                    } else {
                        valeurkpi2.add(val2);
                    }
                } else {
                    valeurkpi2.add(val2);
                }
            } catch (Exception e)
            {
                valeurkpi2.add(0D);
            }
            try
            {
                if (typekpi3 != null)
                {
                    if (typekpi3.equalsIgnoreCase("pourcentage"))
                    {
                        valeurkpi3.add(val3 * 100);
                    } else {
                        valeurkpi3.add(val3);
                    }
                } else
                {
                    valeurkpi3.add(val3);
                }
            } catch (Exception e)
            {
                valeurkpi3.add(0D);
            }
        }
        this.createDatasetandChart();
        cn.closeConnection();
    }

    private void createDatasetandChart2()
    {
        DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
        DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();

        for (int i=0; i<valeurkpi1.size() && i<valeurkpi2.size() ;i++)
        {
            float val = (float) ((int) (valeurkpi1.get(i) * 10000)) / 10000;
            dataset1.addValue(val,nomkpi1, region.get(i));

            val = (float) ((int) (valeurkpi2.get(i) * 10000)) / 10000;
            dataset2.addValue(val,nomkpi2, region.get(i));
        }


        final CategoryItemRenderer renderer = new BarRenderer();
        renderer.setItemLabelsVisible(true);
        final CategoryPlot plot = new CategoryPlot();
        plot.setDataset(dataset1);
        plot.setRenderer(renderer);

    	//CategoryAxis categoryaxis = plot.getDomainAxis();
    	//categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        plot.setDomainAxis(new CategoryAxis("Region"));
        plot.setRangeAxis(new NumberAxis("Valeurs "));

        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);

        final CategoryItemRenderer renderer2 = new LineAndShapeRenderer();
        renderer2.setSeriesStroke(0, new BasicStroke(3F));
        plot.setDataset(1, dataset2);
        plot.setRenderer(1, renderer2);
        //plot.getRenderer(1).setSeriesOutlineStroke(1,new BasicStroke(3F));

        // create the third dataset and renderer...
        final ValueAxis rangeAxis2 = new NumberAxis("Valeurs en %");
        rangeAxis2.setLabelFont(new Font("Dialog",Font.BOLD,12));
        rangeAxis2.setTickLabelFont(new Font("Dialog",Font.BOLD,10));
        plot.setRangeAxis(1, rangeAxis2);

        //plot.setDataset(2, dataset3);
        //final CategoryItemRenderer renderer3 = new LineAndShapeRenderer();
        //plot.setRenderer(2, renderer3);
        plot.mapDatasetToRangeAxis(1, 1);

        // change the rendering order so the primary dataset appears "behind" the
        // other datasets...
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        plot.getDomainAxis().setTickLabelFont(new Font("Dialog",Font.BOLD,12));
        final JFreeChart chart = new JFreeChart(plot);

        // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        this.createImageChart(nomImage, chart, cheminfichierlog);
    }

    private void createDatasetandChart()
    {
        DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
        DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();
        DefaultCategoryDataset dataset3 = new DefaultCategoryDataset();

        for (int i=0; i<valeurkpi1.size() && i<valeurkpi2.size() && i< valeurkpi3.size() ;i++)
        {
            float val = (float) ((int) (valeurkpi1.get(i) * 10000)) / 10000;
            dataset1.addValue(valeurkpi1.get(i),nomkpi1, region.get(i));

            val = (float) ((int) (valeurkpi2.get(i) * 10000)) / 10000;
            dataset2.addValue(val,nomkpi2, region.get(i));

            val = (float) ((int) (valeurkpi3.get(i) * 10000)) / 10000;
            dataset3.addValue(val,nomkpi3, region.get(i));
        }

        final CategoryItemRenderer renderer = new BarRenderer();
        renderer.setItemLabelsVisible(true);
        final CategoryPlot plot = new CategoryPlot();
        plot.setDataset(dataset1);
        plot.setRenderer(renderer);

        plot.setDomainAxis(new CategoryAxis("Region"));
        if(typekpi1!=null)
        {
            if(typekpi1.equalsIgnoreCase("pourcentage"))
            {
                plot.setRangeAxis(new NumberAxis("Valeurs en %"));
            }
            else
                plot.setRangeAxis(new NumberAxis("Valeurs"));
        }
        else
            plot.setRangeAxis(new NumberAxis("Valeurs "));

        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);

        final CategoryItemRenderer renderer2 = new LineAndShapeRenderer();
        renderer2.setSeriesStroke(0, new BasicStroke(3F));
        plot.setDataset(1, dataset2);
        plot.setRenderer(1, renderer2);

        // create the third dataset and renderer...
        ValueAxis rangeAxis2 =null;// new NumberAxis("Valeurs en %");
        if(typekpi3!=null)
        {
            if(typekpi3.equalsIgnoreCase("pourcentage"))
            {
                rangeAxis2 = new NumberAxis("Valeurs en %");
            }
            else
                rangeAxis2 = new NumberAxis("Valeurs en %");
        }
        else
            rangeAxis2 = new NumberAxis("Valeurs en %");

        CategoryPlot localcaCategoryPlot=renderer.getPlot();
        final NumberAxis rangeAxis = (NumberAxis) renderer.getPlot().getRangeAxis();
        rangeAxis.setTickLabelFont(new Font("Dialog",Font.BOLD,12));

        rangeAxis2.setLabelFont(new Font("Dialog",Font.BOLD,12));
        rangeAxis2.setTickLabelFont(new Font("Dialog",Font.BOLD,12));
        plot.setRangeAxis(1, rangeAxis2);

        plot.setDataset(2, dataset3);
        final CategoryItemRenderer renderer3 = new LineAndShapeRenderer();
        renderer3.setSeriesStroke(0, new BasicStroke(3F));
        plot.setRenderer(2, renderer3);
        final NumberAxis rangeAxis3 = (NumberAxis) renderer3.getPlot().getRangeAxis();
        rangeAxis3.setTickLabelFont(new Font("Dialog",Font.BOLD,12));

        plot.mapDatasetToRangeAxis(2, 1);
        //plot.mapDatasetToRangeAxis(1, 1);

        // change the rendering order so the primary dataset appears "behind" the
        // other datasets...
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        
        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        plot.getDomainAxis().setTickLabelFont(new Font("Dialog",Font.BOLD,12));
        final JFreeChart chart = new JFreeChart(plot);

        // add the chart to a panel...
        this.createImageChart(nomImage, chart, cheminfichierlog);
    }


    private void createImageChart(String nomImage,JFreeChart chart,String cheminfichierlog)
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
            ChartUtilities.saveChartAsJPEG(f1, chart, d.width,d.height);
	}
	catch (Exception e)
	{
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe OverlaidBarchart: "+heuredejour+" Problem occurred creating chart.", cheminfichierlog);
	}
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
    }
    
}