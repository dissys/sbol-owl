package dissys.keele.ac.uk;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
        System.out.print( "..." );
        Document doc = Jsoup.parse(new File("../sbol-owl-org.htm"), "UTF-8");
        Elements links = doc.select("a");
        String sbolbase="http://sbols.org/v2";
        for (Element link : links) {
        	String title=link.attr("title");
        	
        	if (title!=null && title.contains(sbolbase))
        	{
        		String newLocalUrl=title.replace(sbolbase, "");
        		String newLocalId=newLocalUrl.replace("#", "");
        		
        		String oldLocalId=link.attr("href").replaceAll("#", "");
        		link.attr("href", newLocalUrl);
        		Element divContainer=doc.getElementById(oldLocalId);
        		if (divContainer!=null) {
        			divContainer.attr("id",newLocalId);
        		}
        	}
        }
        final File f = new File("../sbol-owl.htm");
        FileUtils.writeStringToFile(f, doc.outerHtml(), "UTF-8");
        System.out.println( "done!" );
    }
}
