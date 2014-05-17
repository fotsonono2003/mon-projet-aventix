package projet_consultation.generationChart;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DialShape;
import org.jfree.chart.plot.MeterInterval;
import org.jfree.chart.plot.MeterPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.Range;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.ui.ApplicationFrame;
import projet_consultation.ConnexionBDD.ConnexionBDDOperateur;
import projet_consultation.ConnexionBDD.Connexion_BDDGenerale;
import projet_consultation.ClassesGenerales.Fichier;
import projet_consultation.ClassesGenerales.Operateur;
import projet_consultation.ClassesGenerales.ParametreKPI;

public class Cadran extends ApplicationFrame
{
    private static final long serialVersionUID = 1L;
    private DefaultValueDataset dataset;
    private double seuil1=0;
    private double seuil2=0;
    private String type="";
    private int pascadran=0;
    private double maxcadran=0;
    private double mincadran=0;
    private String nomkpi="";
    private String kpi="";
    private String cheminfichierlog="";
    private String nomImage="";
    private String typekpi = "";
    private double valKpi=0;
    private ParametreKPI parametreKPI=new ParametreKPI();
    private Operateur operateur;
    
    public Cadran(String cheminimage, String cheminfichierlog, String KPI, Operateur operateur) throws ClassNotFoundException
    {
        super(cheminimage);
        try
        {
            parametreKPI = new ParametreKPI();
            Connexion_BDDGenerale cnbdd = Connexion_BDDGenerale.getInstance();
            parametreKPI = cnbdd.getParametresKPIFromKPI(KPI);
            this.nomkpi = parametreKPI.getNomKpi();
            this.kpi = KPI.trim();
            this.operateur = operateur;
            this.nomImage = cheminimage;
            this.cheminfichierlog = cheminfichierlog.toLowerCase().trim();
            this.setValeurKPI();
            //JFreeChart jfreechart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, meterplot, true);
            JFreeChart chart1 = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, getDialPlotMeterChart(), true);
            TextTitle txt = new TextTitle(nomkpi, new Font("Dialog", Font.BOLD, 16));
            chart1.setTitle(txt);
            chart1.setBackgroundPaint(Color.white);
            //chart1.setTitle(nomkpi);
            ChartPanel cp1 = new ChartPanel(chart1);
            cp1.setPreferredSize(new Dimension(600, 400));
            JPanel content = new JPanel(new BorderLayout());
            content.add(cp1);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setContentPane(content);
            ChartPanel chartPanel = new ChartPanel(chart1);
            createImageChart(chart1);
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Cadran.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private MeterPlot getDialPlotMeterChart()
    {
        MeterPlot meterplot = new MeterPlot(dataset);
        //meterplot.setRange(new Range(0.0D, 60D));
        if(valKpi<0)
        {
            valKpi=0;
        }else if(valKpi>100)
            valKpi=100;
        
        if (type.equals("rov"))
	{
            if (valKpi>1)
            {
                if(valKpi<=seuil1)
                {
                    mincadran=valKpi*0.90;
                    double val=seuil2*1.2;
                    if(val<=100)
                        maxcadran=seuil2*1.2;
                    else maxcadran=100;
                }
                else if(valKpi>seuil1 && valKpi<seuil2)
                {
                    mincadran=seuil1*0.90;
                    double val=seuil2*1.2;
                    if(val<=100)
                        maxcadran=seuil2*1.2;
                    else maxcadran=100;
                }
                else if(valKpi>=seuil2)
                {
                    mincadran=seuil1*0.90;
                    double val=valKpi*1.2;
                    if(val<=100)
                        maxcadran=valKpi*1.2;
                    else maxcadran=100;
                }

                if((maxcadran-mincadran)>=30)
                    pascadran=5;
                else pascadran=2;
            }
            else
            {
                mincadran=0;
                maxcadran=seuil2+1.5;
                pascadran=1;
            }
                
            meterplot.setRange(new Range(mincadran, maxcadran));
            
            MeterInterval range=new MeterInterval("Conforme", new Range(seuil2, maxcadran), Color.lightGray, new BasicStroke(2.0F), new Color(0xbc, 0xff, 0x57));
            meterplot.addInterval(range);

            MeterInterval range2=new MeterInterval("Critique", new Range(seuil1, seuil2), Color.lightGray, new BasicStroke(2.0F), new Color(0xFF, 0xEB, 0x30));
            meterplot.addInterval(range2);

            MeterInterval range3=new MeterInterval("Non Conforme", new Range(mincadran, seuil1), Color.lightGray, new BasicStroke(2.0F), new Color(0xFF, 0x57, 0x68));
            meterplot.addInterval(range3);
	}
	else if (type.equals("vor"))
	{
            if (valKpi>1)
            {
                if(valKpi<=seuil1)
                {
                    mincadran=valKpi*0.90;
                    double val=(seuil2*1.2);
                    if(val<=100)
                        maxcadran=seuil2*1.2;
                    else maxcadran=100;
                }
                else if(valKpi>seuil1 && valKpi<seuil2)
                {
                    mincadran=(seuil1*0.90);
                    if((int)(seuil2*1.2)<=100)
                        maxcadran=seuil2*1.2;
                    else maxcadran=100;
                }
                else if(valKpi>=seuil2)
                {
                    mincadran=seuil1*0.90;
                    double val=valKpi*1.2;
                    if(val<=100)
                        maxcadran=valKpi*1.2;
                    else maxcadran=100;
                }
                int val=(int)(maxcadran-mincadran);
                if(val>=30)
                    pascadran=5;
                else if(val>=15 && val<30)
                    pascadran=2;
                else pascadran=1;
            } else
            {
                mincadran=0;
                maxcadran=seuil2+1.5;
                pascadran=1;
            }
            /*System.out.println("KPI:" + kpi);
            System.out.println("Valeur KPI KPI:" + valKpi);
            System.out.println("Seuil1:" + seuil1);
            System.out.println("Seuil2:" + seuil2);
            System.out.println("MinCadran:" + mincadran);
            System.out.println("MaxCadran:" + maxcadran);
             * 
             */
            meterplot.setRange(new Range(mincadran, maxcadran));
            
            meterplot.addInterval(new MeterInterval("Non Conforme", new Range(seuil2, maxcadran), Color.lightGray, new BasicStroke(2.0F), new Color(0xFF, 0x57, 0x68)));
            meterplot.addInterval(new MeterInterval("Critique", new Range(seuil1, seuil2), Color.lightGray, new BasicStroke(2.0F), new Color(0xFF, 0xEB, 0x30)));
            meterplot.addInterval(new MeterInterval("Conforme", new Range(mincadran, seuil1), Color.lightGray, new BasicStroke(2.0F), new Color(0xbc, 0xFF, 0x57)));
        }
        meterplot.setUnits("%");
        meterplot.setDrawBorder(true);
        meterplot.setOutlineStroke(new BasicStroke(3.0F));

        meterplot.setNeedlePaint(Color.BLACK);
        meterplot.setDialBackgroundPaint(Color.lightGray);
        meterplot.setDialOutlinePaint(Color.BLACK);
        meterplot.setDialShape(DialShape.CHORD);
        meterplot.setMeterAngle(260);
        meterplot.setTickLabelsVisible(true);

        meterplot.setTickLabelFont(new Font("Dialog", 1, 12));
        meterplot.setTickLabelPaint(Color.BLACK);
        meterplot.setTickSize(5D);
        meterplot.setTickPaint(Color.lightGray);
        meterplot.setValuePaint(Color.BLUE);//5d6394
        //meterplot.setValuePaint(new Color(0x5d,0x63,0x94));//5d6394
        meterplot.setValueFont(new Font("Dialog", Font.BOLD, 14));

        return meterplot;
    }

    private void setValeurKPI() 
    {
        try
        {
            ConnexionBDDOperateur cn=new ConnexionBDDOperateur();
            parametreKPI=new ParametreKPI();
            Connexion_BDDGenerale cnbdd=Connexion_BDDGenerale.getInstance();
            parametreKPI=cnbdd.getParametresKPIFromKPI(kpi);
            seuil1=parametreKPI.getSeuil1();seuil1=(double)((int)(seuil1*100))/100;
            seuil2=parametreKPI.getSeuil2();seuil2=(double)((int)(seuil2*100))/100;
            pascadran=parametreKPI.getPas();
            maxcadran=parametreKPI.getValSup();
            mincadran=parametreKPI.getValInf();
            type=parametreKPI.getType();
            typekpi=parametreKPI.getTypekpi();
            cn=new ConnexionBDDOperateur(operateur.getBddOperateur());
            String requete="select * from tablevaleurskpi where trim(region)='Global'";
            ResultSet resultat1 =cn.getResultset(requete);
            while (resultat1.next())
            {
                double val=resultat1.getDouble(this.kpi) ;
                if(typekpi!=null)
                {
                    if(typekpi.equalsIgnoreCase("pourcentage"))
                    {
                        try
                        {
                            valKpi=val*100;
                        } catch (Exception ex)
                        {
                            valKpi=0;
                            ex.printStackTrace();
                        }
                    }
                    else
                    {
                        try
                        {
                            valKpi = val;
                        } catch (Exception e)
                        {
                            valKpi=0;
                        }
                    }
                }
                else
                {
                    this.dataset=new DefaultValueDataset(val);
                }
            }
            if(valKpi<0)
            {
                valKpi=0;
            }else if(valKpi>100)
            {
                valKpi=100;
            }
            this.dataset = new DefaultValueDataset(valKpi);
            cn.closeConnection();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(BarChart3D.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (Exception e)
        {
            d=new Dimension(300,290);
            e.printStackTrace();
        }
	try
	{
            File f1 = new File(nomImage);
            ChartUtilities.saveChartAsJPEG(f1, chart, (int)d.getWidth(),(int)d.getHeight());
	}
	catch (Exception e)
	{
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe Cadran "+heuredejour+" Problem occurred creating chart Cadran for: "+nomkpi, cheminfichierlog);
	}
    }
}
