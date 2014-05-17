package projet_consultation.demo;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JTextField;

public class TestList
{

    public List<String> testList()
    {
        String[] tabStr={"str","toto"};
        List<String> listTag = new ArrayList<String>();
        //new Float(value).floatValue();
       //.valueOf(null).floatValue();
       Float.parseFloat(null);
        if(tabStr==null || tabStr.length==0 )
        {
            System.err.println("No Tag Found");
        }
        else
        {
            listTag.addAll(Arrays.asList(tabStr));
        }
        return listTag;
    }

    public static void main(String[] arg)
    {
        JTextField jt=new JTextField("");

        String str="thierry";
        System.out.println(""+str.substring(0, 2));
    }
}
