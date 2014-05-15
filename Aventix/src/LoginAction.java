import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import Services.ServicesEmploie;



public class LoginAction extends ActionSupport
{
    private String username; 
    private String password;
    private String message;
    private int numemployeur;
    private final ServicesEmploie services=new ServicesEmploie();
    
    public String execute() throws Exception 
    {
        //if(services.VerifierLoginAndPasswordEmploye(username, password))
        if(username.equals("thierry") && password.equals("narcisse"))
        {
            message="Welcome:"+username;
            return Action.SUCCESS;
        }
        else
        {
            addActionError("Not A valid User");
            return Action.LOGIN;
        }
    }

    @Override
    public void validate()
    {
        if(username==null || username.trim().equals(""))
        {
            addFieldError("username", "Username can not be blank");
        }
        if(password==null || password.trim().equals(""))
        {
            addFieldError("password", "Password can not be blank");
        }
        //super.validate(); //To change body of generated methods, choose Tools | Templates.
    }
    
    

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getNumemployeur() {
        return numemployeur;
    }

    public void setNumemployeur(int numemployeur) {
        this.numemployeur = numemployeur;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
