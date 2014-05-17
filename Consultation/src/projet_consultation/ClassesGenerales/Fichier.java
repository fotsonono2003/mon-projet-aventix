package projet_consultation.ClassesGenerales;

import java.io.*;
import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
 

public class Fichier
{
    private String algo = "Blowfish";
    private final String password= "bi4tbi4t";
    private final String UNICODE_FORMAT = "ISO-8859-2";
    //private static final String UNICODE_FORMAT = "UTF-8";

    public  void ecrire (String commentaire , String cheminFichier)
    {
        try
        {
            PrintWriter ecrivain;
            ecrivain = new PrintWriter(new BufferedWriter(new FileWriter(cheminFichier + "logConsultation.txt", true)));

            PrintStream ps = new PrintStream(System.out, true, UNICODE_FORMAT);
            System.setOut(ps);
            ecrivain.println(commentaire);
            ecrivain.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(Fichier.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean crypterFichier(String entree, String sortie)
    {
        try
        {
            byte[] passwordInBytes = password.getBytes(UNICODE_FORMAT);
            Key clef = new SecretKeySpec(passwordInBytes, algo);
            Cipher cipher = Cipher.getInstance(algo);
            cipher.init(Cipher.ENCRYPT_MODE, clef);

            byte[] texteClaire = ouvrirFichier(entree);
            byte[] texteCrypte = cipher.doFinal(texteClaire);
            sauverFichier(sortie, texteCrypte);
            return true;
        }
        catch (Exception e)
        {
            System.err.println("Erreur lors de l'encryptage des donnees");
            return false;
        }
    }
    
    public boolean crypterFichier(String password,String entree, String sortie)
    {
        try
        {
            byte[] passwordInBytes = password.getBytes(UNICODE_FORMAT);
            Key clef = new SecretKeySpec(passwordInBytes, algo);
            Cipher cipher = Cipher.getInstance(algo);
            cipher.init(Cipher.ENCRYPT_MODE, clef);

            byte[] texteClaire = ouvrirFichier(entree);
            byte[] texteCrypte = cipher.doFinal(texteClaire);
            sauverFichier(sortie, texteCrypte);
            return true;
        }
        catch (Exception e)
        {
            System.err.println("Erreur lors de l'encryptage des donnees");
            return false;
        }
    }
    public boolean decrypterFichier(String FichierEntree, String FichierSortie)
    {
        boolean b=false;
        try
        {
            byte[] passwordInBytes = password.getBytes(UNICODE_FORMAT);
            Key clef = new SecretKeySpec(passwordInBytes, algo);
            Cipher cipher = Cipher.getInstance(algo);
            cipher.init(Cipher.DECRYPT_MODE, clef);

            byte[] texteCrypte = ouvrirFichier(FichierEntree);
            byte[] texteClaire = cipher.doFinal(texteCrypte);
            sauverFichier(FichierSortie, texteClaire);
            b=true;
        }
        catch (Exception e)
        {
            System.err.println("Erreur lors du décryptage des donnees");
            b=false;
        }
        return b;
    }
    public boolean decrypterFichier(String password,String FichierEntree, String FichierSortie)
    {
        boolean b=false;
        try
        {
            byte[] passwordInBytes = password.getBytes(UNICODE_FORMAT);
            Key clef = new SecretKeySpec(passwordInBytes, algo);
            Cipher cipher = Cipher.getInstance(algo);
            cipher.init(Cipher.DECRYPT_MODE, clef);

            byte[] texteCrypte = ouvrirFichier(FichierEntree);
            byte[] texteClaire = cipher.doFinal(texteCrypte);
            sauverFichier(FichierSortie, texteClaire);
            b=true;
        }
        catch (Exception e)
        {
            System.err.println("Erreur lors du décryptage des donnees");
            b=false;
        }
        return b;
    }

    private byte[] ouvrirFichier(String filename)
    {
        try
        {
            File fichier1 = new File(filename);
            byte[] result = new byte[(int) fichier1.length()];
            FileInputStream in = new FileInputStream(filename);
            in.read(result);
            in.close();
            return result;
        }
        catch (Exception e)
        {
            System.err.println("Probleme lors de la lecture du fichier:"+filename +" Erreur:"+ e.getMessage());
            return null;
        }
    }

    private void sauverFichier(String filename, byte[] data)
    {
        try
        {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(data);
            out.close();
        }
        catch (Exception e)
        {
            System.err.println("Probleme lors de la sauvegarde du fichier: " + e.getMessage());
        }
    }
} 