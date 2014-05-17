package projet_consultation.demo;

import java.io.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import projet_consultation.creation_dossiers.mes_documents;

public class Securite
{
    private String algo = "Blowfish";

    public void crypter(String password, String entree, String sortie)
    {
        try
        {
            byte[] passwordInBytes = password.getBytes("ISO-8859-2");
            Key clef = new SecretKeySpec(passwordInBytes, algo);
            Cipher cipher = Cipher.getInstance(algo);
            cipher.init(Cipher.ENCRYPT_MODE, clef);

            byte[] texteClaire = ouvrirFichier(entree);
            byte[] texteCrypte = cipher.doFinal(texteClaire);
            sauverFichier(sortie, texteCrypte);
        }
        catch (Exception e)
        {
            System.out.println("Erreur lors de l'encryptage des donnees");
        }
    }

    public void decrypter(String password, String entree, String sortie)
    {
        try
        {
            byte[] passwordInBytes = password.getBytes("ISO-8859-2");
            Key clef = new SecretKeySpec(passwordInBytes, algo);
            Cipher cipher = Cipher.getInstance(algo);
            cipher.init(Cipher.DECRYPT_MODE, clef);

            byte[] texteCrypte = ouvrirFichier(entree);
            byte[] texteClaire = cipher.doFinal(texteCrypte);
            sauverFichier(sortie, texteClaire);
        }
        catch (Exception e)
        {
            System.out.println("Erreur lors du décryptage des donnees");
        }
    }

    private byte[] ouvrirFichier(String filename)
    {
        try
        {
            File fichier = new File(filename);
            byte[] result = new byte[(int) fichier.length()];
            FileInputStream in = new FileInputStream(filename);
            in.read(result);
            in.close();
            return result;
        }
        catch (Exception e)
        {
            System.out.println("Probleme lors de la lecture du fichier: " + e.getMessage());
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
            System.out.println("Probleme lors de la sauvegarde du fichier: " + e.getMessage());
        }
    }

    public static void main(String arg[])
    {
        mes_documents mes=new mes_documents();
        Securite s=new Securite();
        //s.crypter("bi4tbi4t", mes.get_CheminParametre()+"parametreconnexion.xml", mes.get_CheminParametre()+"parametreconnexion_crypte.xml");
        s.decrypter("bi4tbi4t", mes.get_CheminParametre()+"parametreconnexion_crypte.xml", mes.get_CheminParametre()+"parametreconnexion.xml");
    }
}