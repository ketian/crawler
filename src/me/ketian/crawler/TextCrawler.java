/**
 * This is a text crawler
 * that fetches the fiction from http://ds.eywedu.com/jinyong/tlbb.
 */
package me.ketian.crawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.util.IO;

import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;

/**
 * @author ketian
 *
 */
public class TextCrawler extends BasicCrawler {
	
	Boolean firstTimeVisit = true;
	static Boolean oneFile;
	
	public static void setOneFile(Boolean isOneFile) {
		oneFile = isOneFile;
	}

	@Override
	public void visit(Page page) {
		
		String url = page.getWebURL().getURL();
	    //super.visit(page);

	    HtmlParseData parseData = (HtmlParseData) page.getParseData();
	    if (oneFile) {
	    	//String filename = page.getWebURL().getDomain();
	    	String filename = "《天龙八部》";
	    	File fout = new File(storageFolder.getAbsolutePath() + "/" + filename + ".txt");
	    	
	    	if (firstTimeVisit) {
	    		firstTimeVisit = false;
	    		
	    		if (fout.exists()) {
	    			fout.delete();
	    		}
	    		
	    		try {
	    			fout.createNewFile();
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    		}
	    	}
	    	
	    	try {
	    		OutputStream out = new FileOutputStream(fout, true);
		    	out.write(parseData.getText().replace(" ", "").getBytes());
		    	out.close();
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
	    } else {
		    String filename = page.getWebURL().getAnchor();
		    IO.writeBytesToFile(parseData.getText().replace(" ", "").getBytes(), 
		    		storageFolder.getAbsolutePath() + "/" + filename + ".txt");
	    }
	    
	    System.out.println("Stored: " + url);

	}
	
}
