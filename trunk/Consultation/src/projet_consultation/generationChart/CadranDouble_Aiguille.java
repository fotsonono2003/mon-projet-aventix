package projet_consultation.generationChart;

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
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.dial.DialBackground;
import org.jfree.chart.plot.dial.DialCap;
import org.jfree.chart.plot.dial.DialPlot;
import org.jfree.chart.plot.dial.DialPointer;
import org.jfree.chart.plot.dial.DialTextAnnotation;
import org.jfree.chart.plot.dial.DialValueIndicator;
import org.jfree.chart.plot.dial.StandardDialFrame;
import org.jfree.chart.plot.dial.StandardDialRange;
import org.jfree.chart.plot.dial.StandardDialScale;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;
import projet_consultation.ConnexionBDD.ConnexionBDDOperateur;
import projet_consultation.ConnexionBDD.Connexion_BDDGenerale;
import projet_consultation.ClassesGenerales.Fichier;
import projet_consultation.ClassesGenerales.Operateur;
import projet_consultation.ClassesGenerales.ParametreKPI;

public class CadranDouble_Aiguille extends ApplicationFrame
{
    private String kpi1;
    private String nomkpi1;
    private String kpi2;
    private String nomkpi2;
    private String typekpi1;
    private String typekpi2;
    private double seuil1Kpi1=0;
    private double seuil2Kpi1=0;
    private double seuil1Kpi2=0;
    private double seuil2Kpi2=0;
    private double maxcadrankpi1=0;
    private double maxcadrankpi2=0;
    private double mincadrankpi1=0;
    private double mincadrankpi2=0;
    private double pascadrankpi1=0;
    private double pascadrankpi2=0;
    private String type1;
    private String type2;
    private ConnexionBDDOperateur cn;
    private double valKpi1=0;
    private double valKpi2=0;
    private Operateur operateur;
 
    public CadranDouble_Aiguille(String nomImage,String cheminfichierlog,String kpi1,String kpi2,Operateur operateur)
    {
        super("");
        try
        {
            ParametreKPI parametreKPI = new ParametreKPI();
            Connexion_BDDGenerale cnbdd = Connexion_BDDGenerale.getInstance();
            parametreKPI = cnbdd.getParametresKPIFromKPI(kpi1);
            this.kpi1 = kpi1.trim();
            this.nomkpi1 = parametreKPI.getNomKpi();
            
            parametreKPI = cnbdd.getParametresKPIFromKPI(kpi2);
            this.kpi2 = kpi2.trim();
            this.nomkpi2 = parametreKPI.getNomKpi();
            this.operateur = operateur;
            this.getValuesForChart(); //get data for diagrams
            final JFreeChart chart = new JFreeChart(getDialPlotChart());
            chart.setBackgroundPaint(Color.white);
            TextTitle txt = new TextTitle(nomkpi1 + "\n" + nomkpi2, new Font("Dialog", Font.BOLD, 14));
            chart.setTitle(txt);
            this.createImageChart(nomImage, chart, cheminfichierlog);
        } catch (SQLException ex)
        {
            Logger.getLogger(CadranDouble_Aiguille.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private DialPlot getDialPlotChart()
    {
        DialPlot plot = new DialPlot();
        plot.setView(0.0, 0.0, 1.0, 1.0);
        if (valKpi1<0)
        {
            valKpi1=0;
        } else if(valKpi1>100)
        {
            valKpi1=100;
        }
        if (valKpi2<0)
        {
            valKpi2=0;
        } else if(valKpi2>100)
        {
            valKpi2=100;
        }
        plot.setDataset(0, new DefaultValueDataset(valKpi1));
        plot.setDataset(1,new DefaultValueDataset(valKpi2));//plot.setDataset(0,this.dataset1);
        StandardDialFrame dialFrame = new StandardDialFrame();
        dialFrame.setBackgroundPaint(Color.BLACK);
        //dialFrame.setForegroundPaint(Color.darkGray);
        dialFrame.setForegroundPaint(Color.BLACK);
        plot.setDialFrame(dialFrame);

        GradientPaint gp = new GradientPaint(new Point(),new Color(255, 255, 255), new Point(),new Color(170, 170, 220));
        DialBackground db = new DialBackground(gp);
        db.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.VERTICAL));
        plot.setBackground(db);

        DialTextAnnotation annotation1 =null;
        DialTextAnnotation annotation2 =null;
        StandardDialScale scale = new StandardDialScale(1,24,-120,-300,1,4);
        StandardDialScale scale2 = new StandardDialScale(0, 100,-120,-300,10.0,4);

        if(typekpi1.equalsIgnoreCase("pourcentage"))
        {
            annotation1 = new DialTextAnnotation("%");
            if (type1.equalsIgnoreCase("vor"))
            {
                if (valKpi1>1)
                {
                    if (valKpi1 <= seuil1Kpi1)
                    {
                        mincadrankpi1 = (int) (valKpi1 * 0.90);
                        int val = (int) (seuil2Kpi1 * 1.2);
                        if (val <= 100) {
                            maxcadrankpi1 = (int) (seuil2Kpi1 * 1.2);
                        } else {
                            maxcadrankpi1 = 100;
                        }
                    } else if (valKpi1 > seuil1Kpi1 && valKpi1 < seuil2Kpi1) {
                        mincadrankpi1 = (int) (seuil1Kpi1 * 0.90);
                        if ((int) (seuil2Kpi1 * 1.2) <= 100) {
                            maxcadrankpi1 = (int) (seuil2Kpi1 * 1.2);
                        } else {
                            maxcadrankpi1 = 100;
                        }
                    } else if (valKpi1 >= seuil2Kpi1) {
                        mincadrankpi1 = (int) (seuil1Kpi1 * 0.90);
                        int val = (int) (valKpi1 * 1.2);
                        if (val <= 100) {
                            maxcadrankpi1 = (int) (valKpi1 * 1.2);
                        } else {
                            maxcadrankpi1 = 100;
                        }
                    }
                    int val = (int) (maxcadrankpi1 - mincadrankpi1);
                    if (val >= 30) {
                        pascadrankpi1 = 5;
                    } else {
                        pascadrankpi1 = 2;
                    }
                } else 
                {
                    mincadrankpi1=0;
                    maxcadrankpi1=seuil2Kpi1+1.5;
                    pascadrankpi1=1;
                }
                scale = new StandardDialScale(mincadrankpi1,maxcadrankpi1,-120,-300,pascadrankpi1,5);
            }else if(type1.equalsIgnoreCase("rov"))
            {
                if (valKpi1>1)
                {
                    if (valKpi1 <= seuil1Kpi1)
                    {
                        mincadrankpi1 = (int) (valKpi1 * 0.90);
                        int val = (int) (seuil2Kpi1 * 1.2);
                        if (val <= 100)
                        {
                            maxcadrankpi1 = (int) (seuil2Kpi1 * 1.2);
                        } else {
                            maxcadrankpi1 = 100;
                        }
                    } else if (valKpi1 > seuil1Kpi1 && valKpi1 < seuil2Kpi1) {
                        mincadrankpi1 = (int) (seuil1Kpi1 * 0.90);
                        int val = (int) (seuil2Kpi1 * 1.2);
                        if (val <= 100) {
                            maxcadrankpi1 = (int) (seuil2Kpi1 * 1.2);
                        } else {
                            maxcadrankpi1 = 100;
                        }
                    } else if (valKpi1 >= seuil2Kpi1) {
                        mincadrankpi1 = (int) (seuil1Kpi1 * 0.90);
                        int val = (int) (valKpi1 * 1.2);
                        if (val <= 100) {
                            maxcadrankpi1 = (int) (valKpi1 * 1.2);
                        } else {
                            maxcadrankpi1 = 100;
                        }
                    }
                    if ((maxcadrankpi1 - mincadrankpi1) >= 30) {
                        pascadrankpi1 = 5;
                    } else {
                        pascadrankpi1 = 2;
                    }
                } else
                {
                    mincadrankpi1=0;
                    maxcadrankpi1=seuil2Kpi1+1.5;
                    pascadrankpi1=1;
                }
                scale = new StandardDialScale(mincadrankpi1,maxcadrankpi1,-120,-300,pascadrankpi1,5);
            }
        }
        else
        {
            annotation1 = new DialTextAnnotation("Hrs");
            scale = new StandardDialScale(1,24,-120,-300,1,4);
        }
        if(typekpi2.equalsIgnoreCase("pourcentage"))
        {
            annotation2 = new DialTextAnnotation("%");
            //scale2 = new StandardDialScale(mincadrankpi2, maxcadrankpi2,-120,-300,pascadrankpi2,5);
            if (type2.equalsIgnoreCase("vor"))
            {
                if (valKpi2>1)
                {
                    if (valKpi2 <= seuil1Kpi2)
                    {
                        mincadrankpi2 = (int) (valKpi2 * 0.90);
                        if ((int) (seuil2Kpi2 * 1.2) <= 100)
                        {
                            maxcadrankpi2 = (int) (seuil2Kpi2 * 1.2);
                        } else {
                            maxcadrankpi2 = 100;
                        }
                    } else if (valKpi2 > seuil1Kpi2 && valKpi2 < seuil2Kpi2) {
                        mincadrankpi2 = (int) (seuil1Kpi2 * 0.90);
                        if ((int) (seuil2Kpi2 * 1.2) <= 100) {
                            maxcadrankpi2 = (int) (seuil2Kpi2 * 1.2);
                        } else {
                            maxcadrankpi2 = 100;
                        }
                    } else if (valKpi2 >= seuil2Kpi2) {
                        mincadrankpi2 = (int) (seuil1Kpi2 * 0.90);
                        if ((int) (valKpi2 * 1.2) <= 100) {
                            maxcadrankpi2 = (int) (valKpi2 * 1.2);
                        } else {
                            maxcadrankpi2 = 100;
                        }
                    }
                    if ((maxcadrankpi2 - mincadrankpi2) >= 30)
                    {
                        pascadrankpi2 = 5;
                    } else
                    {
                        pascadrankpi2 = 2;
                    }
                } else
                {
                    mincadrankpi2=0;
                    maxcadrankpi2=seuil2Kpi2+1.5;
                    pascadrankpi2=1;
                }
                scale2 = new StandardDialScale(mincadrankpi2,maxcadrankpi2,-120,-300,pascadrankpi2,5);
            }else if(type2.equalsIgnoreCase("rov"))
            {
                if (valKpi2>1)
                {
                    if (valKpi2 <= seuil1Kpi2)
                    {
                        mincadrankpi2 = (int) (valKpi2 * 0.90);
                        int val = (int) (seuil2Kpi2 * 1.2);
                        if (val <= 100)
                        {
                            maxcadrankpi2 = (int) (seuil2Kpi2 * 1.2);
                        } else
                        {
                            maxcadrankpi2 = 100;
                        }
                    } else if (valKpi2 > seuil1Kpi2 && valKpi2 < seuil2Kpi2)
                    {
                        mincadrankpi2 = (int) (seuil1Kpi2 * 0.90);
                        int val = (int) (seuil2Kpi2 * 1.2);
                        if (val <= 100) {
                            maxcadrankpi2 = (int) (seuil2Kpi2 * 1.2);
                        } else
                        {
                            maxcadrankpi2 = 100;
                        }
                    } else if (valKpi2 >= seuil2Kpi2)
                    {
                        mincadrankpi2 = (int) (seuil1Kpi2 * 0.90);
                        int val = (int) (valKpi2 * 1.2);
                        if (val <= 100)
                        {
                            maxcadrankpi2 = val;
                        } else {
                            maxcadrankpi2 = 100;
                        }
                    }
                    if ((maxcadrankpi2 - mincadrankpi2) >= 30)
                    {
                        pascadrankpi2 = 5;
                    } else
                    {
                        pascadrankpi2 = 2;
                    }
                } else
                {
                    mincadrankpi2=0;
                    maxcadrankpi2=seuil2Kpi2+1.5;
                    pascadrankpi2=1;
                }
                scale2 = new StandardDialScale(mincadrankpi2,maxcadrankpi2,-120,-300,pascadrankpi2,5);
            }
        }
        else
        {
            annotation2 = new DialTextAnnotation("Hrs");
            scale2 = new StandardDialScale(1, 24,-120,-300,1,4);
        }
        
        annotation1.setFont(new Font("Dialog", Font.BOLD, 11));
        annotation1.setRadius(0.70);
        annotation1.setAngle(-99);

        annotation2.setFont(new Font("Dialog", Font.BOLD, 11));
        annotation2.setPaint(Color.red);
        annotation2.setRadius(0.70);
        annotation2.setAngle(-78);

        plot.addLayer(annotation1);
        plot.addLayer(annotation2);

        //permet de fixer les parametres du cadre contenant la valeur sur laquelle l'aiguille pointe
        DialValueIndicator dvi1 = new DialValueIndicator(0);
        dvi1.setFont(new Font("Dialog", Font.BOLD, 12));
        dvi1.setOutlinePaint(Color.darkGray);
        dvi1.setRadius(0.60);
        dvi1.setAngle(-103.0);

        DialValueIndicator dvi2 = new DialValueIndicator(1);
        dvi2.setFont(new Font("Dialog", Font.BOLD, 12));
        dvi2.setOutlinePaint(Color.red);
        dvi2.setRadius(0.60);
        dvi2.setAngle(-77.0);

        plot.addLayer(dvi2);
        plot.addLayer(dvi1);

        //StandardDialScale scale = new StandardDialScale(1,24,-120,-300,1,4);
        scale.setTickRadius(0.88);///////////////////
        //scale.setTickRadius(0.70);
        scale.setTickLabelOffset(0.15);
        scale.setTickLabelFont(new Font("Dialog",Font.PLAIN,10));
        plot.addScale(0, scale);
        //plot.mapDatasetToScale(0, 0);

        //StandardDialScale scale2 = new StandardDialScale(0, 100,-120,-300,10.0,4);
        scale2.setTickRadius(0.50);
        scale2.setTickLabelOffset(0.15);
        scale2.setTickLabelFont(new Font("Dialog", Font.PLAIN,10));
        scale2.setMajorTickPaint(Color.BLUE);
        scale2.setMinorTickPaint(Color.red);
        plot.addScale(1, scale2);
        plot.mapDatasetToScale(1, 1);//////
        //plot.mapDatasetToScale(0, 1);

        if (type1.equalsIgnoreCase("rov"))
	{
            StandardDialRange range3 = new StandardDialRange(mincadrankpi1, seuil1Kpi1, new Color(0xFF, 0x09, 0x21));//rouge
            range3.setInnerRadius(0.62);
            range3.setOuterRadius(0.63);
            range3.setScaleIndex(0);
            //range3.setBounds(0.62,0.70);
            plot.addLayer(range3);
            
            StandardDialRange range2 = new StandardDialRange(seuil1Kpi1, seuil2Kpi1, new Color(0xEF, 0xD8, 0x07));//jaune
            range2.setInnerRadius(0.62);
            range2.setOuterRadius(0.63);
            range2.setScaleIndex(0);
            plot.addLayer(range2);

            StandardDialRange range = new StandardDialRange(seuil2Kpi1, maxcadrankpi1,new Color(0x16, 0xB8, 0x4E));////vert
            range.setInnerRadius(0.62);
            range.setOuterRadius(0.63);
            range.setScaleIndex(0);
            //range.setBounds(0.63,0.68);
            plot.addLayer(range);
	}
	else if (type1.equalsIgnoreCase("vor"))
	{
            StandardDialRange range3 = new StandardDialRange(mincadrankpi1, seuil1Kpi1, new Color(0x16, 0xB8, 0x4E));////vert
            range3.setInnerRadius(0.62);
            range3.setOuterRadius(0.63);
            range3.setScaleIndex(0);
            //range3.setBounds(0.62,0.70);
            plot.addLayer(range3);
            
            StandardDialRange range2 = new StandardDialRange(seuil1Kpi1, seuil2Kpi1, new Color(0xEF, 0xD8, 0x07));////jaune
            range2.setInnerRadius(0.62);
            range2.setOuterRadius(0.63);
            range2.setScaleIndex(0);
            plot.addLayer(range2);

            StandardDialRange range = new StandardDialRange(seuil2Kpi1, maxcadrankpi1, new Color(0xFF, 0x09, 0x21));//rouge
            range.setInnerRadius(0.62);
            range.setOuterRadius(0.63);
            range.setScaleIndex(0);
            plot.addLayer(range);
	}

        if (type2.equalsIgnoreCase("rov"))
	{
            StandardDialRange range3 = new StandardDialRange(mincadrankpi2, seuil1Kpi2, new Color(0xFF, 0x09, 0x21));////rouge
            range3.setInnerRadius(0.42);
            range3.setOuterRadius(0.43);
            range3.setScaleIndex(1);
            plot.addLayer(range3);

            StandardDialRange range2 = new StandardDialRange(seuil1Kpi2, seuil2Kpi2, new Color(0xEF, 0xD8, 0x07));////jaune
            range2.setInnerRadius(0.42);
            range2.setOuterRadius(0.43);
            range2.setScaleIndex(1);
            plot.addLayer(range2);

            StandardDialRange range = new StandardDialRange(seuil2Kpi2, maxcadrankpi2,new Color(0x16, 0xB8, 0x4E));////vert
            range.setInnerRadius(0.42);
            range.setOuterRadius(0.43);
            range.setScaleIndex(1);
            //range.setBounds(0.42,0.43);
            plot.addLayer(range);
	}
	else if (type2.equalsIgnoreCase("vor"))
	{
            //StandardDialRange range3 = new StandardDialRange(mincadrankpi2, seuil1Kpi2, new Color(0x16, 0xB8, 0x4E));////vert
            StandardDialRange range3 = new StandardDialRange(mincadrankpi2, seuil1Kpi2, new Color(0x16, 0xB8, 0x4E));////vert
            range3.setInnerRadius(0.42);
            range3.setOuterRadius(0.43);
            range3.setScaleIndex(1);
            plot.addLayer(range3);

            //StandardDialRange range2 = new StandardDialRange(seuil1Kpi2, seuil2Kpi2, new Color(0xEF, 0xD8, 0x07));////jaune
            StandardDialRange range2 = new StandardDialRange(seuil1Kpi2, seuil2Kpi2, new Color(0xEF, 0xD8, 0x07));////jaune
            range2.setInnerRadius(0.42);
            range2.setOuterRadius(0.43);
            range2.setScaleIndex(1);
            plot.addLayer(range2);

            //StandardDialRange range = new StandardDialRange(seuil2Kpi2, maxcadrankpi2, new Color(0xFF, 0x09, 0x21));////rouge
            StandardDialRange range = new StandardDialRange(seuil2Kpi2, maxcadrankpi2, new Color(0xFF, 0x09, 0x21));////rouge
            range.setInnerRadius(0.42);
            range.setOuterRadius(0.43);
            range.setScaleIndex(1);
            plot.addLayer(range);
	}

        //permet de parametrer la taille de l'aiguille du cadran
        Paint paint = new Color(191, 48, 0);
        //DialPointer needle3 = new ScaledDialPointer(-1, paint, paint);
        //DialPointer dp=new DialPointer.Pointer();

        DialPointer needle = new DialPointer.Pointer(0);
        plot.addPointer(needle);

        DialPointer needle2 = new DialPointer.Pin(1);
        needle2.setRadius(0.55);
        plot.addPointer(needle2);

        DialCap cap = new DialCap();
        cap.setFillPaint(paint);
        cap.setRadius(0.10);
        plot.setCap(cap);
        
        return plot;
    }

    private void getValuesForChart()
    {
        try
        {
            Connexion_BDDGenerale cnbdd=Connexion_BDDGenerale.getInstance();
            ParametreKPI paramkpi1=cnbdd.getParametresKPIFromKPI(kpi1);
            ParametreKPI paramkpi2=cnbdd.getParametresKPIFromKPI(kpi2);

            typekpi2=paramkpi2.getTypekpi();
            seuil1Kpi2=paramkpi2.getSeuil1();
            seuil2Kpi2=paramkpi2.getSeuil2();
            pascadrankpi2=paramkpi2.getPas();
            maxcadrankpi2=paramkpi2.getValSup();
            mincadrankpi2=paramkpi2.getValInf();
            type2=paramkpi2.getType();
                        
            typekpi1=paramkpi1.getTypekpi();
            seuil1Kpi1=paramkpi1.getSeuil1();
            seuil2Kpi1=paramkpi1.getSeuil2();
            pascadrankpi1=paramkpi1.getPas();
            maxcadrankpi1=paramkpi1.getValSup();
            mincadrankpi1=paramkpi1.getValInf();
            type1=paramkpi1.getType();
            cn=new ConnexionBDDOperateur(operateur.getBddOperateur());
            String requete="Select * from tablevaleurskpi where  trim(region)='Global' ";
            ResultSet resultat = cn.getResultset(requete);
            while (resultat.next())
            {
                double val1=0,val2=0;
                try
                {
                    val1=resultat.getDouble(this.kpi1);
                } catch (Exception e)
                {
                    val1=0;
                }
                try
                {
                    val2=resultat.getDouble(this.kpi2);
                } catch (Exception e)
                {
                    val2=0;
                }
                if(typekpi1!=null)
                {
                    if(typekpi1.equalsIgnoreCase("pourcentage"))
                    {
                        valKpi1=val1*100;
                    }
                    else
                    {
                        valKpi1=val1;
                    }
                }
                else
                {
                    valKpi1=val1;
                }

                if(typekpi2!=null)
                {
                    if(typekpi2.equalsIgnoreCase("pourcentage"))
                    {
                        valKpi2=val2*100;
                    }
                    else
                    {
                        valKpi2=val2;
                    }
                }
                else
                {
                    valKpi2=val2;
                }
            }
            cn.closeConnection();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void createImageChart(String nomImage,JFreeChart chart,String cheminfichierlog)
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
            //d=new Dimension(300,290);
        }
        catch (Exception e)
        {
            d=new Dimension(300,290);
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
            fichier.ecrire("Classe DialChartDouble "+heuredejour+" Problem occurred creating chart CadranDouble "+e.getMessage(), cheminfichierlog);
	}
    }

    public  final void setValue(double  val1,double val2)
    {
        valKpi1=val1;
        valKpi2=val2;
    }

}
