package projet_consultation.GenererRapport;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PRAcroForm;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.SimpleBookmark;
import projet_consultation.ClassesGenerales.Fichier;
 
public class Concat
{
    public void concatener (String cheminfichierlog, String args[])
    {
       	try
   	{
            int pageOffset = 0;
            ArrayList master = new ArrayList();
            int f = 0;
            String outFile = args[args.length - 1];
            Document document = null;
            PdfCopy writer = null;
            while (f < args.length - 1)
            {
                PdfReader reader = new PdfReader(args[f]);
                reader.consolidateNamedDestinations();
                int n = reader.getNumberOfPages();
                List bookmarks = SimpleBookmark.getBookmark(reader);
                if (bookmarks != null)
                {
                    if (pageOffset != 0)
                    {
                        SimpleBookmark.shiftPageNumbers(bookmarks, pageOffset, null);
                    }
                    master.addAll(bookmarks);
                }
                pageOffset += n;
                if (f == 0)
                {
                    document = new Document(reader.getPageSizeWithRotation(1));
                    writer = new PdfCopy(document, new FileOutputStream(outFile));
                    document.open();
                }
                PdfImportedPage page;
                for (int i = 0; i < n;)
                {
                    ++i;
                    page = writer.getImportedPage(reader, i);
                    writer.addPage(page);
                }
                PRAcroForm form = reader.getAcroForm();
                if (form != null)
                {
                    writer.copyAcroForm(reader);
                }
                f++;

                // Effacer ici args[f]
                //File MyFile = new File(args[f])
                //MyFile.delete();
            }
            if (!master.isEmpty())
            {
                writer.setOutlines(master);
            }
            document.close();
        }
   	catch (Exception ex)
   	{
            Date date=new Date();
            SimpleDateFormat formatter= new SimpleDateFormat("EEEE-dd-MMMM-yyyy");
            String DateduJour=formatter.format(date);

            formatter= new SimpleDateFormat("kk:mm");
            String heuredejour=formatter.format(date);
            Fichier fichier =new Fichier();
            fichier.ecrire("Classe Concat:"+DateduJour+" Heure:"+heuredejour+ " Erreur:" + ex.getMessage(), cheminfichierlog);
        }
    }  
}