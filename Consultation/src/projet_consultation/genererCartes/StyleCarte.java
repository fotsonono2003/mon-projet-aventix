package projet_consultation.genererCartes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;


public class StyleCarte
{
	 
    private void enregistreFichier(String fichier,Document document) throws Exception
    {
        XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
	sortie.output(document, new FileOutputStream(fichier));
    }

    public void modifierStyle (String KPI,double kpiseuil1,double kpiseuil2,String sens, String fichierstyle) throws JDOMException, IOException, Exception
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
                  Element elt= ((Element) a1.get(0)).setText(KPI);
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
             if (sens.trim().equalsIgnoreCase("vor"))
             {
                 Element polygon1 =(Element) list41.get(3);
                 List listcolor1=polygon1.getChildren();
                 Iterator listcolor1it= listcolor1.iterator();
                 Element current = (Element) listcolor1it.next();
                 List a1 =current.getChildren();
                 Element elt= ((Element) a1.get(0)).setText("#BCFF57");//vert
                 enregistreFichier(fichierstyle,doc);
                 Element polygon3 =(Element) list43.get(3);
                 List listcolor3=polygon3.getChildren();
                 Iterator listcolor3it= listcolor3.iterator();
                 Element current3 = (Element) listcolor3it.next();
                 List a3color =current3.getChildren();
                 Element eltcolor3= ((Element) a3color .get(0)).setText("#FF5768");//rouge
                 enregistreFichier(fichierstyle,doc);
             }
            if(sens.trim().equalsIgnoreCase("rov"))
             {
                 Element polygon1 =(Element) list41.get(3);
                 List listcolor1=polygon1.getChildren();
                 Iterator listcolor1it= listcolor1.iterator();
                 Element current = (Element) listcolor1it.next();
                 List a1 =current.getChildren();
                 Element elt= ((Element) a1.get(0)).setText("#FF5768");//rouge
                 enregistreFichier(fichierstyle,doc);
                 Element polygon3 =(Element) list43.get(3);
                 List listcolor3=polygon3.getChildren();
                 Iterator listcolor3it= listcolor3.iterator();
                 Element current3 = (Element) listcolor3it.next();
                 List a3color =current3.getChildren();
                 Element eltcolor3= ((Element) a3color .get(0)).setText("#BCFF57");//vert
                 enregistreFichier(fichierstyle,doc);
             }
        }
    }
}
