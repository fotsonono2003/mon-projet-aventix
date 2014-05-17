package projet_consultation.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Lancer un exécutable ou une commande externe
 */
public class RunExternal
{

    public static void launch(String command)
    {
        try
        {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(command);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            while ((line = input.readLine()) != null)
            {
                String str=new String(line.getBytes(),"UTF-8");
                //System.out.println(line);
                System.out.println(str);
            }
            //Attendre la fin de l'execution
            if (process.waitFor() != 0)
            {
                System.out.println("Une erreur est survenue ");
            }
        } catch (InterruptedException ex)
        {
            Logger.getLogger(RunExternal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(RunExternal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String args[])
    {
        String setProperty = System.setProperty("file.encoding", "UTF-8");
        //Lancer la calculatrice de Windows
        //RunExternal.launch("C://WINDOWS//system32//calc.exe");
        //Lister le contenu d'un répertoire avec DOS
        //RunExternal.launch("cmd /c dir");
        RunExternal.launch("ping 127.0.0.1");
        String myString = "éèàùÔ";
        System.out.println("1 - chaine en clair: " + myString);
        try
        {
            myString = java.net.URLEncoder.encode(myString, "UTF-8");
        } catch (UnsupportedEncodingException ex)
        {
            Logger.getLogger(RunExternal.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("2 - chaine encodée: " + myString);

        System.out.println("3 - chaine encodée: " + myString);
        try
        {
            java.net.URLDecoder.decode(myString, "UTF-8");
        } catch (UnsupportedEncodingException ex)
        {
            Logger.getLogger(RunExternal.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("4 - chaine en clair: " + myString);
        //RunExternal.launch("ipconfig /all");
        
    }
}