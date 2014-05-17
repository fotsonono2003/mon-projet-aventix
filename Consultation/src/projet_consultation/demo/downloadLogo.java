package projet_consultation.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import projet_consultation.ConnexionBDD.Connexion_BDDGenerale;
import projet_consultation.creation_dossiers.mes_documents;

public class downloadLogo
{
    private boolean getLogoOperateur(String location,String codeOperateur) throws Exception
    {
        File monImage = new File(location);
        FileOutputStream ostreamImage = new FileOutputStream(monImage);
        boolean test=false;
        try
        {
            Connexion_BDDGenerale cnbdd=Connexion_BDDGenerale.getInstance();
            Connection conn=cnbdd.getConnection();
            PreparedStatement ps = conn.prepareStatement("select logo from operateur where code_operateur=?");
            try
            {
                ps.setString(1, codeOperateur);
                ResultSet rs = ps.executeQuery();
                try
                {
                    if (rs.next())
                    {
                        InputStream istreamImage = rs.getBinaryStream("logo");
                        byte[] buffer = new byte[1024];
                        int length = 0;
                        while ((length = istreamImage.read(buffer)) != -1)
                        {
                            ostreamImage.write(buffer, 0, length);
                        }
                    }
                    test=true;
                }
                catch(Exception ex)
                {
                    test=false;
                }
                finally
                {
                    rs.close();
                }
            }
            catch(Exception ex)
            {
                test=false;
            }
            finally
            {
                try
                {
                    conn.close();
                    ps.close();
                } catch (Exception exception) {
                }
            }
        }
        catch(Exception ex)
        {
            test=false;
        }
        finally
        {
            ostreamImage.close();
        }
        return test;
    }

    public static void main(String[] arg)
    {
        try
        {
            mes_documents mes=new mes_documents();
            String code="AZ";
            String chemin=mes.get_CheminImage()+"logo.jpg";
            downloadLogo down = new downloadLogo();
            down.getLogoOperateur(chemin, code);
        }
        catch (Exception ex)
        {
            Logger.getLogger(downloadLogo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
