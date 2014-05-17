package projet_consultation.generationChart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.GraphicsEnvironment;
import java.awt.Paint;
import java.awt.Point;
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
import org.jfree.chart.plot.DialShape;
import org.jfree.chart.plot.MeterInterval;
import org.jfree.chart.plot.MeterPlot;
import org.jfree.chart.plot.dial.DialBackground;
import org.jfree.chart.plot.dial.DialCap;
import org.jfree.chart.plot.dial.DialPlot;
import org.jfree.chart.plot.dial.DialPointer;
import org.jfree.chart.plot.dial.StandardDialFrame;
import org.jfree.chart.plot.dial.StandardDialRange;
import org.jfree.chart.plot.dial.StandardDialScale;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.Range;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;
import projet_consultation.ConnexionBDD.ConnexionBDDOperateur;
import projet_consultation.ClassesGenerales.Fichier;
import projet_consultation.ClassesGenerales.Operateur;
import projet_consultation.ConnexionBDD.Connexion_BDDGenerale;
import projet_consultation.ClassesGenerales.ParametreKPI;

public class CadranBH extends ApplicationFrame
{
    private static final long serialVersionUID = 1L;
    private DefaultValueDataset dataset;
    private int val;
    private String nomkpi;
    private String kpi;
    private String cheminfichierlog;
    private String nomImage;
    private String bddoperateur;
    private ConnexionBDDOperateur cn;


    public CadranBH(String cheminimage, String cheminfichierlog, String KPI,Operateur operateur)
    {
        super(cheminimage);
        try 
        {
            this.kpi = KPI.trim();

            Connexion_BDDGenerale cnbdd = Connexion_BDDGenerale.getInstance();
            ParametreKPI parametreKPI = new ParametreKPI();
            parametreKPI = cnbdd.getParametresKPIFromKPI(kpi);
            this.nomkpi = parametreKPI.getNomKpi();
            this.nomImage = cheminimage;
            bddoperateur = operateur.getBddOperateur();
            this.cheminfichierlog = cheminfichierlog.toLowerCase().trim();
            this.setValeurKPI();
            //JFreeChart jfreechart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, meterplot, true);
            //JFreeChart chart1= new JFreeChart("",JFreeChart.DEFAULT_TITLE_FONT,getDialPlotMeterChart(),false);
            JFreeChart chart1 = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, getDialPlotChart(), false);
            TextTitle txt = new TextTitle(nomkpi, new Font("Dialog", Font.BOLD, 16));
            chart1.setTitle(txt);
            chart1.setBackgroundPaint(Color.white);
            ChartPanel cp1 = new ChartPanel(chart1);
            cp1.setPreferredSize(new Dimension(600, 400));
            createImageChart(chart1);
        }
        catch (SQLException ex)
        {
            Logger.getLogger(CadranBH.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private DialPlot getDialPlotChart()
    {
        DialPlot plot = new DialPlot();
        plot.setView(0.0, 0.0, 1.0, 1.0);
        //plot.setDataset(0,new DefaultValueDataset(val-0.5));
        StandardDialFrame dialFrame = new StandardDialFrame();
        dialFrame.setBackgroundPaint(Color.BLACK);
        dialFrame.setForegroundPaint(Color.BLACK);
        plot.setDialFrame(dialFrame);

        GradientPaint gp = new GradientPaint(new Point(),new Color(255, 255, 255), new Point(),new Color(170, 170, 220));
        DialBackground db = new DialBackground(gp);
        db.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.VERTICAL));
        plot.setBackground(db);

        StandardDialScale scale = new StandardDialScale(0,24,-90,-360,1,4);
        scale.setTickRadius(0.88);///////////////////
        scale.setTickLabelOffset(0.15);
        scale.setTickLabelFont(new Font("Dialog",Font.BOLD,12));
        //plot.addScale(0, scale);
        //plot.mapDatasetToScale(0, 0);

        StandardDialRange range = new StandardDialRange(0, 24, new Color(0x07, 0x1e, 0xef));////bleu ciel 071eef
        range.setInnerRadius(0.42);
        range.setOuterRadius(0.43);
        range.setScaleIndex(0);
        //plot.addLayer(range);

        int v=val;
        if(v==0)
        {
            scale = new StandardDialScale((int)1,(int)24,-115,-305,1,4);
            scale.setTickRadius(0.88);///////////////////
            scale.setTickLabelOffset(0.15);
            scale.setTickLabelFont(new Font("Dialog",Font.BOLD,12));
            plot.addScale(0, scale);
            range = new StandardDialRange((int)1, (int)24, new Color(0x07, 0x1e, 0xef));////bleu ciel 071eef
            range.setInnerRadius(0.55);
            range.setOuterRadius(0.56);
            range.setScaleIndex(0);
            plot.addLayer(range);
            v=23;val=24;
            plot.setDataset(0,new DefaultValueDataset(23.5));
            StandardDialRange range1 = new StandardDialRange((val-1),val, new Color(0xef,0x07,0x1d));////rouge ef071d
            range1.setInnerRadius(0.55);
            range1.setOuterRadius(0.56);
            range1.setScaleIndex(0);
            plot.addLayer(range1);
        }
        else
        {
            scale = new StandardDialScale((int)0, (int)23,-115,-305,1,4);
            scale.setTickRadius(0.88);///////////////////
            scale.setTickLabelOffset(0.15);
            scale.setTickLabelFont(new Font("Dialog",Font.BOLD,12));
            plot.addScale(0, scale);
            range = new StandardDialRange((int)0, (int)23, new Color(0x07, 0x1e, 0xef));////bleu ciel 071eef
            range.setInnerRadius(0.55);
            range.setOuterRadius(0.56);
            range.setScaleIndex(0);
            plot.addLayer(range);
            v=val-1;
            plot.setDataset(0,new DefaultValueDataset(val-0.5));
            StandardDialRange range1 = new StandardDialRange((val-1),val, new Color(0xef,0x07,0x1d));////rouge ef071d
            range1.setInnerRadius(0.55);
            range1.setOuterRadius(0.56);
            range1.setScaleIndex(0);
            plot.addLayer(range1);
        }

        //permet de parametrer la taille de l'aiguille du cadran
        Paint paint = new Color(191, 48, 0);

        DialPointer needle = new DialPointer.Pointer(0);
        plot.addPointer(needle);
        
        DialCap cap = new DialCap();
        cap.setFillPaint(paint);
        cap.setRadius(0.10);
        plot.setCap(cap);

        return plot;
    }

    private MeterPlot getDialPlotMeterChart()
    {
        int v=-1;
        if(val==0)
        {
            v=23;val=24;
            dataset=new DefaultValueDataset(23.5);
        }
        else
        {
            dataset=new DefaultValueDataset((val-0.5));
            v=val-1;
        }
        //dataset=new DefaultValueDataset((int)val);
        MeterPlot meterplot = new MeterPlot(dataset);
        meterplot.setRange(new Range(0, 24));
        meterplot.setMeterAngle(360);
        meterplot.setDialShape(DialShape.CIRCLE);
        meterplot.setDialBackgroundPaint(new Color(0x87,0xba,0xfa));
        meterplot.setDialOutlinePaint(new Color(0x04,0x31,0x6a));
        
        MeterInterval range=new MeterInterval("", new Range(v, val), Color.WHITE, new BasicStroke(2.0F), new Color(0xfa,0x8f,0x87));
        meterplot.addInterval(range);
        meterplot.setTickLabelPaint(new Color(0xf5,0x24,0x16));
        meterplot.setUnits("");
        meterplot.setValuePaint(meterplot.getDialBackgroundPaint());
        meterplot.setTickLabelFont(new Font("Dialog", Font.BOLD, 14));
        //meterplot.setTickLabelPaint(meterplot.getDialBackgroundPaint());
        //meterplot.setTickLabelPaint(Color.BLUE);
        meterplot.setNeedlePaint(new Color(0xdb,0x16,0x09));
        return meterplot;
    }
    
    private void setValeurKPI()
    {
        try
        {
            cn=new ConnexionBDDOperateur(bddoperateur);
            String requete="select "+kpi.toLowerCase()+" from tablevaleurskpi where trim(region)='Global'";
            ResultSet resultat1 = cn.getResultset(requete);
            while (resultat1.next())
            {
                try
                {
                    val = resultat1.getInt(kpi.toLowerCase());
                } catch (SQLException ex)
                {
                    val=-1;
                    Logger.getLogger(CadranBH.class.getName()).log(Level.SEVERE, null, ex);
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
        String str=null;
        Dimension d=new Dimension(300,290);
        try
        {
            GraphicsEnvironment graphicsEnvironment=GraphicsEnvironment.getLocalGraphicsEnvironment();
            Rectangle rect=graphicsEnvironment.getMaximumWindowBounds();
            double largeur=rect.getWidth()/6.23;
            double longeur=rect.getHeight()/3.851;
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
