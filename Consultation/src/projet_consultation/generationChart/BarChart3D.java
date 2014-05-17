package projet_consultation.generationChart;

import java.awt.BasicStroke;   
import java.awt.Color;   
import java.awt.Dimension;    
import java.awt.Paint;   
import java.io.File;   
import java.sql.SQLException;
import java.util.Vector; 
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.ChartFactory;   
import org.jfree.chart.ChartPanel;   
import org.jfree.chart.ChartUtilities;   
import org.jfree.chart.JFreeChart;     
import org.jfree.chart.labels.ItemLabelAnchor;   
import org.jfree.chart.labels.ItemLabelPosition;   
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;   
import org.jfree.chart.plot.CategoryPlot;   
import org.jfree.chart.plot.PlotOrientation;   
import org.jfree.chart.plot.ValueMarker;   
import org.jfree.chart.renderer.category.BarRenderer3D;   
import org.jfree.data.category.CategoryDataset;   
import org.jfree.data.category.DefaultCategoryDataset;   
import org.jfree.ui.ApplicationFrame;   
import org.jfree.ui.Layer;    
import org.jfree.ui.TextAnchor;   
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import projet_consultation.ConnexionBDD.ConnexionBDDOperateur;
import projet_consultation.ConnexionBDD.Connexion_BDDGenerale;
import projet_consultation.ClassesGenerales.Fichier;
import projet_consultation.ClassesGenerales.Operateur;
import projet_consultation.ClassesGenerales.ParametreKPI;
   
public class BarChart3D extends ApplicationFrame   
{   
    private static final long serialVersionUID = 1L;
    private double seuil1=0;
    private double seuil2=0;
    private double  minAxe=0;
    private double maxAxe=0;
    private String type="";
    private  Operateur operateur;
    private ParametreKPI parametreKPI=new ParametreKPI();

    public BarChart3D(String nomImage, String cheminfichierlog, String KPI,Operateur operateur)
    {
        super(nomImage);
        this.operateur = operateur;
        CategoryDataset localCategoryDataset = createDataset(KPI);
        JFreeChart localJFreeChart = createChart(nomImage, cheminfichierlog, localCategoryDataset);
        ChartPanel localChartPanel = new ChartPanel(localJFreeChart);
        localChartPanel.setPreferredSize(new Dimension(500, 400));
        setContentPane(localChartPanel);
    }
  
    private CategoryDataset createDataset(String KPI)
    {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Vector<String> vecteurregion = new Vector<String>();
        Vector<Double> vecteurvaleurkpi =new Vector<Double>();
        
        try
        {
            ConnexionBDDOperateur cn=new ConnexionBDDOperateur();
            parametreKPI=new ParametreKPI();
            Connexion_BDDGenerale cnbdd=Connexion_BDDGenerale.getInstance();
            parametreKPI=cnbdd.getParametresKPIFromKPI(KPI);
            String nomkpi=parametreKPI.getNomKpi();
            seuil1=parametreKPI.getSeuil1();//seuil1=((int)(seuil1*100))/100;
            seuil2=parametreKPI.getSeuil2();//seuil2=((int)(seuil2*100))/100;
            type=parametreKPI.getType().trim();
            if(type==null)
                type="";
            cn=new ConnexionBDDOperateur(operateur.getBddOperateur());
            String requete="select region,"+KPI+" from tablevaleurskpi where region!='Global'";
            
            ResultSet resultat1 =cn.getResultset(requete) ;
            while(resultat1.next())
            {
                try
                {
                    vecteurregion.add(resultat1.getString("region").trim());
                } catch (Exception ex)
                {
                    vecteurregion.add(" ");
                }
                try
                {
                    double val=(float)((int)(resultat1.getDouble(KPI)*10000))/10000;
                    vecteurvaleurkpi.add(val*100);
                } catch (Exception ex)
                {
                    vecteurvaleurkpi.add(0d);
                }
            }

            if(vecteurvaleurkpi.size()>0)
            {
                minAxe=vecteurvaleurkpi.get(0);
                maxAxe=vecteurvaleurkpi.get(0);
            }
            for (int i = 0; i < vecteurregion.size(); i++)
            {
                dataset.addValue(vecteurvaleurkpi.get(i),nomkpi, vecteurregion.get(i));
                if(vecteurvaleurkpi.get(i)<minAxe)
                {
                    minAxe=vecteurvaleurkpi.get(i);
                }
                if(vecteurvaleurkpi.get(i)>maxAxe)
                {
                    maxAxe=vecteurvaleurkpi.get(i);
                }
            }
                maxAxe=(float)((int)(maxAxe*10000))/10000;
                minAxe=(float)((int)(minAxe*10000))/10000;
            cn.closeConnection();
        }catch (SQLException ex)
        {
            Logger.getLogger(BarChart3D.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dataset;
    }

    private JFreeChart createChart(String title,String cheminfichierlog, CategoryDataset paramCategoryDataset)
    {
        JFreeChart localJFreeChart = ChartFactory.createBarChart3D(" ", "Region", " Valeur en % ", paramCategoryDataset, PlotOrientation.VERTICAL, true, true, false);
        localJFreeChart.setBackgroundPaint(Color.white);
        CategoryPlot localCategoryPlot = (CategoryPlot)localJFreeChart.getPlot();
        final NumberAxis rangeAxis = (NumberAxis) localCategoryPlot.getRangeAxis();
        //rangeAxis.setAutoRangeIncludesZero(true);
        minAxe=minAxe * 0.8; 
        maxAxe=maxAxe * 1.2;        
        if(minAxe<maxAxe)
        {
            rangeAxis.setRange(minAxe,maxAxe);
        }//else rangeAxis.setAutoRangeIncludesZero(true);
        System.out.println("MaxeAxe:"+rangeAxis.getRange().getUpperBound());
        System.out.println("MinAxe:"+rangeAxis.getRange().getLowerBound());

        localCategoryPlot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);//permet de modifier l'angle des chaines de Krteres de la l'axe des abscisses
        localCategoryPlot.getDomainAxis().setTickLabelFont(new Font("Dialog",Font.BOLD,12));//permet de definir un FONT pour les chaine de krteres de l'axes des abscisses
        rangeAxis.setLabelFont(new Font("Dialog",Font.BOLD,12));// permet de définir un FONT pour le label representant l'axe des ordonnées
        rangeAxis.setTickLabelFont(new Font("Dialog", Font.BOLD,10));//permet de definir un FONT pour les valeurs de l'axe des ordonnées
        
        CustomBarRenderer3D localCustomBarRenderer3D = new CustomBarRenderer3D();
        localCustomBarRenderer3D.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        localCustomBarRenderer3D.f1 = seuil1;
        localCustomBarRenderer3D.f2 = seuil2;
        localCustomBarRenderer3D.type = type;
        localCustomBarRenderer3D.setBaseItemLabelFont(new Font("Dialog",Font.BOLD, 9));
        //localCustomBarRenderer3D.setLegendTextFont(0, new Font("Dialog",Font.BOLD, 15 ));
        localCustomBarRenderer3D.setBaseItemLabelsVisible(true);
        localCustomBarRenderer3D.setItemLabelAnchorOffset(5.0D);
        //localCustomBarRenderer3D.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
        localCustomBarRenderer3D.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.CENTER));
        localCategoryPlot.setRenderer(localCustomBarRenderer3D);
        if (type.equals("rov"))
        {
            ValueMarker localValueMarker1 = new ValueMarker(seuil1, new Color(0xEE, 0x10, 0x10), new BasicStroke(1F), new Color(0xEE, 0x10, 0x10), new BasicStroke(1F), 1F);
            localCategoryPlot.addRangeMarker(localValueMarker1, Layer.BACKGROUND);
            ValueMarker localValueMarker2 = new ValueMarker(seuil2, new Color(0x54, 0xF9, 0x8D), new BasicStroke(1F), new Color(0x54, 0xF9, 0x8D), new BasicStroke(1F), 1F);
            localCategoryPlot.addRangeMarker(localValueMarker2, Layer.BACKGROUND);
        }
        else if(type.equals("vor"))
        {
            ValueMarker localValueMarker1 = new ValueMarker(seuil1, new Color(0x54, 0xF9, 0x8D), new BasicStroke(1F), new Color(0x54, 0xF9, 0x8D), new BasicStroke(1F), 1F);
            localCategoryPlot.addRangeMarker(localValueMarker1, Layer.BACKGROUND);
            ValueMarker localValueMarker2 = new ValueMarker(seuil2, new Color(0xEE, 0x10, 0x10), new BasicStroke(1F), new Color(0xEE, 0x10, 0x10), new BasicStroke(1F), 1F);
            localCategoryPlot.addRangeMarker(localValueMarker2, Layer.BACKGROUND);
        }
        localCustomBarRenderer3D.setBaseItemLabelsVisible(true);
        createImageChart(localJFreeChart, title, cheminfichierlog);
        return localJFreeChart;
    }
  
    static class CustomBarRenderer3D extends  BarRenderer3D
    {
        private static final long serialVersionUID = 1L;
        double f1;
        double f2;
        String type;
       
        @Override
        public Paint getItemPaint(int paramInt1, int paramInt2)
        {
            CategoryDataset localCategoryDataset = getPlot().getDataset();
            double d = localCategoryDataset.getValue(paramInt1, paramInt2).doubleValue();
            if (type.equals("rov"))
            {
                if (d > f2)
                {
                    return ( new Color(0x16, 0xB8, 0x4E));
                }
                if (f1 > d)
                {
                    return ( new Color(0xFF, 0x5E, 0x4D));
                }
                else return ( new Color(0xEF,0xD8, 0x07));
            }
            else if(type.equals("vor"))
            {
                if (d > f2)
                {
                    return ( new Color(0xFF, 0x5E, 0x4D));
                }
                if (f1 > d)
                {
                    return ( new Color(0x16, 0xB8, 0x4E));
                }
                else return ( new Color(0xEF,0xD8, 0x07));
            }
            else
            {
                return ( new Color(0xEF,0xD8, 0x07));
            }
        }
    }

    private void createImageChart(JFreeChart chart,String title,String cheminfichierlog)
    {

        String str=null;
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
            Logger.getLogger(BarChart3D.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
        {
            File f1 = new File(title);
            ChartUtilities.saveChartAsJPEG(f1, chart,(int) d.getWidth(),(int)d.getHeight());
        }
        catch (Exception ex)
        {
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("kk:mm");
            String heuredejour = formatter.format(date);
            Fichier fichier = new Fichier();
            fichier.ecrire("Classe Barchart3D " + heuredejour + " Problem occurred creating chart:"+ex.getMessage(), cheminfichierlog);
        }
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
    }

}  