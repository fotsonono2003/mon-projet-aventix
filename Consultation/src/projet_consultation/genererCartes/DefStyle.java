package projet_consultation.genererCartes;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import projet_consultation.ClassesGenerales.Fichier;
import projet_consultation.creation_dossiers.mes_documents;


public class DefStyle
{     
    private void enregistreFichier(String fichier,Document document) throws Exception
    {
        XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
	sortie.output(document, new FileOutputStream(fichier));
    } 
 
    @SuppressWarnings("unchecked")
    public void modifierStyleregion (String KPI,double kpiseuil1,double kpiseuil2,String sens, String fichierstyle) throws Exception
    {
        SAXBuilder builder = new SAXBuilder();
        org.jdom.Document doc = builder.build(fichierstyle);
        Element root = doc.getRootElement();
        List listOfModels = root.getChildren();
        Iterator listOfModelsIt = listOfModels.iterator();
        while( listOfModelsIt.hasNext())
        {
            Element currentModel = (Element) listOfModelsIt.next();
            List dataModel = currentModel.getChildren();
            Element listOfCompartments = (Element) dataModel.get(1);
            List listOflistOfCompartments = listOfCompartments.getChildren();
            Element list = (Element) listOflistOfCompartments.get(1);
            List list1 = list.getChildren();
            Element ogc1 =  (Element) list1.get(0);
            List list41 = ogc1.getChildren();
            Element rule1 =(Element) list41.get(2);
            List list51 = rule1.getChildren();
            Iterator list51it= list51.iterator();
            while (list51it.hasNext())
            {
                Element current1 = (Element) list51it.next();
                List a1 =current1.getChildren();
                @SuppressWarnings("unused")
                Element elt= ((Element) a1.get(0)).setText(KPI);
                @SuppressWarnings("unused")
                Element elt1= ((Element) a1.get(1)).setText(String.valueOf(kpiseuil1));
                enregistreFichier(fichierstyle,doc);
            }
            Element ogc2 =  (Element) list1.get(1);
	    List list42 = ogc2.getChildren();
	    Element rule2 =(Element) list42.get(2);
	    List list52 = rule2.getChildren();
	    Element list6=(Element) list52.get(0);
	    List list7 =list6.getChildren();
	    Element elt88 =(Element) list7.get(0);
	    List a2= elt88.getChildren();
	    Element elt66 =((Element) a2.get(0)).setText(KPI);   
            Element elt77 =((Element) a2.get(1)).setText(String.valueOf(kpiseuil1));
	    Element elt888 =(Element) list7.get(1);
	    List b2= elt888.getChildren();
	    Element elt666 =((Element) b2.get(0)).setText(KPI);   
            Element elt777 =((Element) b2.get(1)).setText(String.valueOf(kpiseuil2)); 
            enregistreFichier(fichierstyle,doc);
            Element ogc3 =  (Element) list1.get(2);
            List list43 = ogc3.getChildren();
       
            Element rule3 =(Element) list43.get(2);
            List list53 = rule3.getChildren();
            Iterator list53it= list53.iterator(); 
            while (list53it.hasNext())
            {
                Element current2 = (Element) list53it.next();
            	List a3 =current2.getChildren();
            	Element elt22= ((Element) a3.get(0)).setText(KPI);
            	Element elt1= ((Element) a3.get(1)).setText(String.valueOf(kpiseuil2));
            	enregistreFichier(fichierstyle,doc);
            }
            if (sens.equals("vor"))
            {
                Element polygon1 =(Element) list41.get(3);
                List listcolor1=polygon1.getChildren();
                Iterator listcolor1it= listcolor1.iterator();
                Element current = (Element) listcolor1it.next();
                List a1 =current.getChildren();
                Element elt= ((Element) a1.get(0)).setText("#008000");
                Element polygon3 =(Element) list43.get(3);
                List listcolor3=polygon3.getChildren();
                Iterator listcolor3it= listcolor3.iterator();
                Element current3 = (Element) listcolor3it.next();
                List a3color =current3.getChildren();
                Element eltcolor3= ((Element) a3color .get(0)).setText("#FF0000");
                enregistreFichier(fichierstyle,doc);
            }
            if (sens.equals("rov"))
            {
                Element polygon1 =(Element) list41.get(3);
                List listcolor1=polygon1.getChildren();
                Iterator listcolor1it= listcolor1.iterator();
                Element current = (Element) listcolor1it.next();
                List a1 =current.getChildren();
                Element elt= ((Element) a1.get(0)).setText("#FF0000");
                Element polygon3 =(Element) list43.get(3);
                List listcolor3=polygon3.getChildren();
                Iterator listcolor3it= listcolor3.iterator();
                Element current3 = (Element) listcolor3it.next();
                List a3color =current3.getChildren();
                Element eltcolor3= ((Element) a3color .get(0)).setText("#008000");
                enregistreFichier(fichierstyle,doc);
            }
    	} 
    }
    public void modifierStylebts (String nomkpi,double seuil1,double seuil2,String sens, String fichierStyle) throws Exception
    {
        SAXBuilder builder = new SAXBuilder();
        org.jdom.Document doc = builder.build(fichierStyle);
        Element root = doc.getRootElement();
        List listOfModels = root.getChildren();
        Iterator listOfModelsIt = listOfModels.iterator();
        while( listOfModelsIt.hasNext())
        {
            Element currentModel = (Element) listOfModelsIt.next();
            List dataModel = currentModel.getChildren();
            Element listOfCompartments = (Element) dataModel.get(1);
            List listOflistOfCompartments = listOfCompartments.getChildren();
            Element list = (Element) listOflistOfCompartments.get(1);
            List list1 = list.getChildren();
       
            Element ogc1 =  (Element) list1.get(0);
            List list41 = ogc1.getChildren();
            Element rule1 =(Element) list41.get(2);
            List list51 = rule1.getChildren();
            Iterator list51it= list51.iterator();
            while (list51it.hasNext())
            {
               Element current1 = (Element) list51it.next();
               List a1 =current1.getChildren();
               @SuppressWarnings("unused")
               Element elt= ((Element) a1.get(0)).setText(nomkpi);
               @SuppressWarnings("unused")
               Element elt1= ((Element) a1.get(1)).setText(String.valueOf(seuil1));
               Element point1 =(Element) list41.get(3);
               List listcolor1=point1.getChildren();
               Element eltpoint1 = (Element) listcolor1.get(0);
               List listpoint1 = eltpoint1.getChildren();
               Element eltcolor1 =(Element) listpoint1.get(0);
               List listmark1 =eltcolor1.getChildren();
               Element fill1 =((Element) listmark1.get(1));
               List listfill1=fill1.getChildren();
    	   
               if (sens.equals("rov"))
               {
                    Element colorfill1 =((Element) listfill1.get(0)).setText("#FF0000");
               }
               else
               {
                    Element colorfill1 =((Element) listfill1.get(0)).setText("#008000");
               }
            }
    	  
            Element ogc2 =  (Element) list1.get(1);
	    List list42 = ogc2.getChildren();
	    Element rule2 =(Element) list42.get(2);
	    List list52 = rule2.getChildren();
	    Element list6=(Element) list52.get(0);
	    List list7 =list6.getChildren();
	    Element elt88 =(Element) list7.get(0);
	    List a2= elt88.getChildren();
	    @SuppressWarnings("unused")
		Element elt66 =((Element) a2.get(0)).setText(nomkpi);   
		@SuppressWarnings("unused")
		Element elt77 =((Element) a2.get(1)).setText(String.valueOf(seuil1));
	    Element elt888 =(Element) list7.get(1);
	    List b2= elt888.getChildren();
	    @SuppressWarnings("unused")
		Element elt666 =((Element) b2.get(0)).setText(nomkpi);   
        @SuppressWarnings("unused")
		Element elt777 =((Element) b2.get(1)).setText(String.valueOf(seuil2)); 
    	Element ogc3 =  (Element) list1.get(2);
	    List list43 = ogc3.getChildren();
	    Element rule3 =(Element) list43.get(2);
	    List list53 = rule3.getChildren();
	    Iterator list53it= list53.iterator(); 
	    while (list53it.hasNext())
             {
	    	   Element current3 = (Element) list53it.next();
	    	   List a3 =current3.getChildren();
	    	   @SuppressWarnings("unused")
	    	   Element elt22= ((Element) a3.get(0)).setText(nomkpi);
	    	   @SuppressWarnings("unused")
	    	   Element elt13= ((Element) a3.get(1)).setText(String.valueOf(seuil2));  
    	   
	    	   Element point3 =(Element) list43.get(3);
	    	   List listcolor3=point3.getChildren();
	    	   Element eltpoint3 = (Element) listcolor3.get(0);
	    	   List listpoint3 = eltpoint3.getChildren();
	    	   Element eltcolor3 =(Element) listpoint3.get(0);
	    	   List listmark3 =eltcolor3.getChildren();
	    	   Element fill3 =((Element) listmark3.get(1));
	    	   List listfill3=fill3.getChildren();
	    	   
	    	   if(sens.equals("rov"))
                   {
                    Element colorfill3 =((Element) listfill3.get(0)).setText("#008000");
	    	   }
	    	   else
                   {
                        Element colorfill3 =((Element) listfill3.get(0)).setText("#FF0000");
	    	   }
                    try
                    {
                        enregistreFichier(fichierStyle,doc);
                    }
                    catch (Exception ex)
                    {
                        Date date=new Date();
			SimpleDateFormat formatter= new SimpleDateFormat("kk:mm");
			String heuredejour=formatter.format(date);
			SimpleDateFormat formatterdate= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
			String datedejour=formatterdate.format(date);
			Fichier fichier =new Fichier();
                        mes_documents mes=new mes_documents();
                        fichier.ecrire("Classe DessinCarte" + datedejour + " " + heuredejour + ex, mes.get_CheminLog());
                    }
                }
            }
    }
}