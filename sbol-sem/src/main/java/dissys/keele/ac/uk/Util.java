package dissys.keele.ac.uk;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.sbolstandard.core2.ComponentDefinition;
import org.sbolstandard.core2.SBOLConversionException;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLReader;
import org.sbolstandard.core2.SBOLValidationException;

public class Util {

	
	public static boolean createFolder(String folderPath)
	{
		File folder=new File(folderPath);
		if (!folder.exists())
		{
			folder.mkdir();
			return true;
		}	
		else
		{
			return false;
		}
	}
	
	public static String downloadFile(String url, String subDir) throws MalformedURLException, IOException {
		String fileName = subDir + "/" +  url.substring(url.lastIndexOf("/") + 1);
		if (!new File(fileName).exists()) {
			InputStream in = new URL(url).openStream();
			String data = null;
			try {
				data = IOUtils.toString(in);
				data=data.replaceAll("/1\"", "\"");
			} finally {
				IOUtils.closeQuietly(in);
			}
			FileUtils.write(new File(fileName), data);
		}
		return fileName;
	}

	
	public static void removeComponentDefinitions(String file1, String file2) throws SBOLValidationException, IOException, SBOLConversionException
	{
		if (!new File(file2).exists())
		{
		SBOLDocument doc = SBOLReader.read(file1);
		ArrayList<ComponentDefinition> compDefs=new ArrayList<ComponentDefinition>();
		for (ComponentDefinition compDef:doc.getComponentDefinitions())
		{
			compDefs.add(compDef);
		}
		for (ComponentDefinition compDef:compDefs)
		{
			doc.removeComponentDefinition(compDef);
		}
		doc.write(new File(file2));
		}
		
	}
	
	
}
