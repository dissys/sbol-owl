package dissys.keele.ac.uk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDFS;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;


/**
 * Hello world!
 *
 */
public class App 
{
	private static String sbolbase="http://sbols.org/v2";
    /*Steps (Make sure the remote and online Github repo includes the latest sbol.rdf)
     * 1- Remove the sbol-owl-org.htm
     * 2- Take a copy of the LODE from Github: git clone https://github.com/essepuntato/LODE.git
     * 3 - Run LODE:
     * 	cd LODE
     *  mvn clean jetty:run
     *  4 - Save the output from the following URL as sbol-owl.htm
     *      http://localhost:8080/lode/extract?url=https://dissys.github.io/sbol-owl/sbol.rdf
     *  5- Rename the sbol-owl.htm as sbol-owl-org.htm
     *  6 - Run this App.java to create the updated sbol-owl.htm with comments    
     * */
    public static void main( String[] args ) throws IOException
    {
        System.out.print( "..." );
        Document doc = Jsoup.parse(new File("../sbol-owl-org.htm"), "UTF-8");
        cleanHeaders(doc);
        Elements links = doc.select("a");
        
        Model ontModel=getRdfModel();
        
        for (Element link : links) {
        	String href=link.attr("href");
        	if (href.startsWith("http://localhost"))
        	{
        		href="#" + getAfter(href, "#");
        		link.attr("href",href);
        	}
        	String title=link.attr("title");
        	
        	if (title!=null && title.contains(sbolbase))
        	{
        		String newLocalUrl=title.replace(sbolbase, "");
        		String newLocalId=newLocalUrl.replace("#", "");
        		
        		String oldLocalId=getAfter(href, "#");
        		link.attr("href", newLocalUrl);
        		Element divContainer=doc.getElementById(oldLocalId);
        		if (divContainer!=null) {
        			divContainer.attr("id",newLocalId);
        			 addMissingComments(doc, divContainer, ontModel,newLocalId); 			
        		}
        	}
        }
        
        final File f = new File("../sbol-owl.htm");
        FileUtils.writeStringToFile(f, doc.outerHtml(), "UTF-8");
        System.out.println( "done!" );
    }
    
    private static String getAfter(String data, String separator)
    {
    	int index=data.indexOf(separator);
    	if (index>=0)
    	{
    		return data.substring(index+separator.length());
    	}
    	else
    	{
    		return data;
    	}
    }
    private static void cleanHeaders(Document doc)
    {
    	Elements heads=doc.select("div.head");
    	if (heads!=null)
    	{ 
    		Element head= heads.first();
    		Elements dls=head.getElementsByTag("dl");
    		int i=0;
    		ArrayList<Element> toRemove=new ArrayList<Element>();
    		
    	    for (Element el:dls)
    		{
    			if (i>=2)
    			{
    				toRemove.add(el);
    			}
    			i++;
    		}
    		for (Element el:toRemove)
    		{
    			el.remove();
    		}
    	}
    	
    }
    private  static void addMissingComments(Document doc, Element divContainer, Model ontModel, String sbolName)
    {
    	Elements comments=divContainer.getElementsByAttributeValueContaining("class", "comment");
		if (comments==null || comments.size()==0)
		{
			Elements descriptions=divContainer.getElementsByAttributeValueContaining("class", "description");
			if (descriptions!=null && descriptions.size()>0)
			{
				String commentString=getProperty(ontModel, sbolbase + "#" + sbolName, "http://www.w3.org/2000/01/rdf-schema#comment");
				if (commentString!=null)
				{
					Element description=descriptions.first();
					Element comment=createCommentElement(doc, divContainer, commentString);	
					comment.before(description);
				}
			}
		}  
    }
    private static String getProperty(Model ontModel, String resourceUri, String propertyUri)
    {
    	String value=null;
    	Resource resource= ontModel.getResource(resourceUri);
    	if (resource!=null)
    	{
    		Statement stmt=resource.getProperty(RDFS.comment);
    		if (stmt!=null)
    		{
    			value=stmt.getLiteral().getString();
    		}
    		else
    		{
    			System.out.println("No comment for " + resourceUri);
    		}
    	}
    	
    	return value;
    }
    private static Element createCommentElement (Document doc,Element parent, String commentString)
    {
    	//Element commentDiv=new Element("div");
    	Element commentDiv=doc.createElement("div");
    	commentDiv.appendTo(parent);
    	commentDiv.addClass("comment");
    	Element p=new Element("p");
    	p.appendText(commentString);
    	commentDiv.appendChild(p);
    	return commentDiv;
    }
    private static Model getRdfModel() throws IOException
    {
		Model model = ModelFactory.createDefaultModel();

		InputStream is = new FileInputStream("../sbol.rdf");
		model.read(is, RDFS.getURI());

		return model;
    }
}
