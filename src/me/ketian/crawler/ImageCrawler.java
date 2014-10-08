/**
 * This is a image crawler
 * that fetches images from http://jandan.net/pic
 * and filters small images (which might be an advertisement).
 */
package me.ketian.crawler;


import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import edu.uci.ics.crawler4j.util.IO;

/**
 * @author ketian
 *
 */

public class ImageCrawler extends BasicCrawler {
	private static final Pattern imgPatterns = Pattern.compile(".*(\\.(bmp|gif|jpe?g|png|tiff?))$");
	
	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
		//System.out.println(href);
		if (imgPatterns.matcher(href).matches()) {
			return true;
		}
		
		boolean inDomain = false;
		for (String domain: crawlDomains) {
			if (href.startsWith(domain)) {
				inDomain = true;
				break;
			}
		}
		
		if (!inDomain) {
			return false;
		}
		
		return super.shouldVisit(url);
	}
	
	@Override
	public void visit(Page page) {
		
		String url = page.getWebURL().getURL();

	    if (!imgPatterns.matcher(url).matches()) {
	    	super.visit(page);
	    	return;
	    }

	    if (!(page.getParseData() instanceof BinaryParseData)) {
	    	return;
	    }

	    if (page.getContentData().length < 10 * 1024) {
	    	return;
	    }

	    String filename = url.substring(url.lastIndexOf("/"));
	    IO.writeBytesToFile(page.getContentData(), storageFolder.getAbsolutePath() + "/" + filename);
	    System.out.println("Stored: " + url);

	}
	
}
