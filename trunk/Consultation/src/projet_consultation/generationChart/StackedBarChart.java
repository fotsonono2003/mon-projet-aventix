package projet_consultation.generationChart;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.File;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jfree.chart.*;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.*;
import projet_consultation.ConnexionBDD.ConnexionBDDOperateur;
import projet_consultation.ConnexionBDD.Connexion_BDDGenerale;
import projet_consultation.ClassesGenerales.Fichier;
import projet_consultation.ClassesGenerales.Operateur;
import projet_consultation.ClassesGenerales.ParametreKPI;

public class StackedBarChart extends ApplicationFrame
{
    private String kpi1=null;
    private String nomkpi1=null;
    private String kpi2=null;
    private String nomkpi2=null;
    private String nomImage;
    private String cheminfichierlog;
    private List<Double> valeurkpi1=new ArrayList<Double>();
    private List<Double> valeurkpi2=new ArrayList<Double>();
    private List<String> region=new ArrayList<String>();
    private Operateur operateur;
    private ConnexionBDDOperateur cn;

    public StackedBarChart(String nomImage,String cheminfichierlog,String kpi1,String kpi2,Operateur operateur)
    {
        super("");
        this.kpi1=kpi1;
        this.kpi2=kpi2;
        String typekpi1=null;
        String typekpi2=null;
        double seuil1kpi1=0;
        double seuil2kpi1=0;
        double seuil1kpi2=0;
        double seuil2kpi2=0;
        this.operateur=operateur;

        try
        {
            cn=new ConnexionBDDOperateur();
            String requete="Select * from tableparametrekpi where kpi='"+kpi1+"' or kpi='"+kpi2+"'";
            Connexion_BDDGenerale cnbdd=Connexion_BDDGenerale.getInstance();
            ParametreKPI paramkpi1=cnbdd.getParametresKPIFromKPI(kpi1);
            ParametreKPI paramkpi2=cnbdd.getParametresKPIFromKPI(kpi2);

            this.nomkpi1 = paramkpi1.getNomKpi();
            typekpi1=paramkpi1.getTypekpi();
            seuil1kpi1=paramkpi1.getSeuil1();
            seuil2kpi1=paramkpi1.getSeuil2();

            this.nomkpi2 = paramkpi2.getNomKpi();
            typekpi2=paramkpi2.getTypekpi();
            seuil1kpi2=paramkpi2.getSeuil1();
            seuil2kpi2=paramkpi2.getSeuil2();

            cn=new ConnexionBDDOperateur(operateur.getBddOperateur());
            requete="select region,"+kpi1.toLowerCase()+","+kpi2.toLowerCase()+" from tablevaleurskpi where region!='Global'";
            ResultSet resultat1 = cn.getResultset(requete);
            while(resultat1.next())
            {
                try
                {
                    region.add(resultat1.getString("region").trim());
                }
                catch (Exception e)
                {
                    region.add(" ");
                }
                try
                {
                    if(typekpi1!=null)
                    {
                        if (typekpi1.equalsIgnoreCase("pourcentage"))
                        {
                            double val = (double) ((int) (resultat1.getDouble(kpi1) * 10000)) / 10000;
                            valeurkpi1.add(val * 100);
                        } else
                        {
                            double val = (double) ((int) (resultat1.getDouble(kpi1) * 10000)) / 10000;
                            valeurkpi1.add(val);
                        }
                    }
                    else
                    {
                        double val = (double) ((int) (resultat1.getDouble(kpi1) * 10000)) / 10000;
                        valeurkpi1.add(val);
                    }
                }
                catch (Exception e)
                {
                    valeurkpi1.add(0.0d);
                }
                try
                {
                    if(typekpi2!=null)
                    {
                        if (typekpi2.equalsIgnoreCase("pourcentage"))
                        {
                            double val = (double) ((int) (resultat1.getDouble(kpi2) * 10000)) / 10000;
                            valeurkpi2.add( val* 100);
                        } else
                        {
                            double val = (double) ((int) (resultat1.getDouble(kpi2) * 10000)) / 10000;
                            valeurkpi2.add(val);
                        }
                    }
                    else
                    {
                        double val = (double) ((int) (resultat1.getDouble(kpi2) * 10000)) / 10000;
                        valeurkpi2.add(val);
                    }
                }
                catch (Exception e)
                {
                    valeurkpi2.add(0d);
                }
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(StackedBarChart.class.getName()).log(Level.SEVERE, null, ex);
        }

        final CategoryDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);
        this.createImageChart(nomImage,chart, cheminfichierlog);
        cn.closeConnection();
  }
    //methode permettznt de crréer une image à partir du chart
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
        } catch (Exception ex)
        {
            d=new Dimension(900, 270);
            Logger.getLogger(StackedBarChart.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
	{
            File f1 = new File(nomImage);
            ChartUtilities.saveChartAsJPEG(f1,chart,d.width,d.height);
	}
	catch (Exception e)
	{
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe DialChartDouble "+heuredejour+" Problem occurred creating chart.", cheminfichierlog);
	}
    }

    private CategoryDataset createDataset()
    {
        DefaultCategoryDataset result = new DefaultCategoryDataset();
        
        for (int i = 0; i<valeurkpi1.size() && i<valeurkpi2.size() ;i++)
        {
            result.addValue(valeurkpi1.get(i),this.nomkpi1, region.get(i));
            result.addValue(valeurkpi2.get(i),this.nomkpi2, region.get(i));
        }
        return result;
    }

    private JFreeChart createChart(final CategoryDataset dataset)
    {
        final JFreeChart chart =ChartFactory.createStackedBarChart(" ", "", "Pourcentage %",dataset, PlotOrientation.VERTICAL, true, true, false);
        final CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.getRenderer().setSeriesPaint(0, new Color(30, 100, 175));
        plot.getRenderer().setSeriesPaint(1, new Color(90, 190, 110));
        //plot.getRenderer().setSeriesPaint(2, new Color(225, 45, 45));
        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        plot.getDomainAxis().setTickLabelFont(new Font("Dialog",Font.BOLD,12));

        BarRenderer barrenderer = (BarRenderer)plot.getRenderer();

        chart.setBackgroundPaint(new Color(249, 231, 236));

        barrenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        barrenderer.setBaseItemLabelsVisible(true);
        barrenderer.setItemLabelAnchorOffset(-10.0D);
        barrenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.CENTER, TextAnchor.CENTER));
        barrenderer.setBaseItemLabelFont(new Font("Verdana", Font.CENTER_BASELINE, 8));
        plot.setRenderer(barrenderer);
        return chart;
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
    }
    
       
}