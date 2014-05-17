package projet_consultation.generationChart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.ThermometerPlot;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.ui.ApplicationFrame;
import projet_consultation.ConnexionBDD.ConnexionBDDOperateur;
import projet_consultation.ClassesGenerales.Fichier;
import projet_consultation.ClassesGenerales.Operateur;
import projet_consultation.ClassesGenerales.ParametreKPI;
import projet_consultation.ConnexionBDD.Connexion_BDDGenerale;

public class Thermometer extends ApplicationFrame
{
    private String nomImage;
    private String cheminfichierlog;
    private String nomkpi;
    private String kpi;
    private float val;
    private Operateur operateur;
    private ConnexionBDDOperateur cn;

    public Thermometer(String nomImage,String cheminfichierlog,String kpi,Operateur operateur)
    {
        super("");

        try 
        {
            this.nomImage = nomImage;
            this.cheminfichierlog = cheminfichierlog;
            Connexion_BDDGenerale cnbdd = Connexion_BDDGenerale.getInstance();
            ParametreKPI paramkpi1 = cnbdd.getParametresKPIFromKPI(kpi);
            this.kpi = kpi.trim();
            this.nomkpi = paramkpi1.getNomKpi();
            this.operateur = operateur;
            this.setValeurKPI();
            final DefaultValueDataset dataset = new DefaultValueDataset(val);
            // create the chart...
            final ThermometerPlot plot = new ThermometerPlot(dataset);
            final JFreeChart chart = new JFreeChart("% Trafic @ BH", JFreeChart.DEFAULT_TITLE_FONT, plot, false); // false pour include legend
            // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
            //plot.setInsets(new Insets(5, 5, 5, 5));
            plot.setUnits(0);
            plot.setRange(0.0, 100.0);
            plot.setSubrangeInfo(ThermometerPlot.NORMAL, -50.0, -40, -10.0, 22.0);
            plot.setSubrangeInfo(ThermometerPlot.WARNING, -40, -30, 18.0, 26.0); //marqueur jaune
            plot.setSubrangeInfo(ThermometerPlot.CRITICAL, -30, 100, -10.0, 10.0); //marqueur rouge
            plot.setSubrangePaint(2, new Color(0x87, 0xba, 0xfa));
            plot.setThermometerStroke(new BasicStroke(2.0f));
            plot.setThermometerPaint(Color.lightGray);
            this.createImageChart(chart);
            // OPTIONAL CUSTOMISATION COMPLETED.
            // add the chart to a panel...
            final ChartPanel chartPanel = new ChartPanel(chart);
            setContentPane(chartPanel);
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Thermometer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setValeurKPI()
    {
        try
        {
            cn=new ConnexionBDDOperateur(operateur.getBddOperateur());
            String requete="select "+kpi.toLowerCase()+" from tablevaleurskpi where region='Global'";
            ResultSet resultat1 = cn.getResultset(requete);
            while (resultat1.next())
            {
                try
                {
                    val = resultat1.getFloat(kpi.toLowerCase())*100;
                    val = (float)((int)(val*10000))/10000;
                } catch (Exception ex)
                {
                    val=0.0f;
                }
            }
            cn.closeConnection();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(CadranBH.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void createImageChart(JFreeChart chart)
    {
        Dimension d=new Dimension(300,290);
        try
        {
            GraphicsEnvironment graphicsEnvironment=GraphicsEnvironment.getLocalGraphicsEnvironment();
            Rectangle rect=graphicsEnvironment.getMaximumWindowBounds();
            double largeur=rect.getWidth()/6.23;
            double longeur=rect.getHeight()/3.586;
            if(longeur!=Double.NaN && longeur!=Double.NEGATIVE_INFINITY && longeur!=Double.POSITIVE_INFINITY && largeur!=Double.NaN  && largeur!=Double.NEGATIVE_INFINITY && largeur!=Double.POSITIVE_INFINITY)
            {
                d=new Dimension((int)largeur,(int)longeur);
            }
        } catch (Exception ex)
        {
            d=new Dimension(300,290);
            Logger.getLogger(Thermometer.class.getName()).log(Level.SEVERE, null, ex);
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
            fichier.ecrire("Classe CadranBH: "+heuredejour+" ,Problem occurred creating chart CadranBH for: "+nomkpi, cheminfichierlog);
	}
    }
    
}
