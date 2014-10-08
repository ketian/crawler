/**
 * This is a basic crawler
 * that implements basic functions
 * of Crawler4j.
 */
package me.ketian.crawler;


import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.util.List;
import java.util.regex.Pattern;
import java.io.File;

import org.apache.http.Header;

/**
 * @author ketian
 *
 */
public class BasicCrawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" 
            + "|png|tiff?|mid|mp2|mp3|mp4"
            + "|wav|avi|mov|mpeg|ram|m4v|pdf" 
            + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
	
	protected static File storageFolder;
	protected static String[] crawlDomains;
	
	public static void configure(String[] domain, String storageFolderName) {
		crawlDomains = domain;
		storageFolder = new File(storageFolderName);
		if (!storageFolder.exists()) {
			storageFolder.mkdirs();
		}
	}
	
	/**
	 * To decide whether the crawler should visit a page.
	 * Basic requirements include right domain and proper suffix.
	 */
	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
		Boolean rightDomain = false;
		for (String domain : crawlDomains) {
			if (href.startsWith(domain)) {
				rightDomain = true;
				break;
			}
		}
		return !FILTERS.matcher(href).matches() && rightDomain;
	}
	
	/**
	 * To gain basic information of the page visiting.
	 */
	@Override
	public void visit(Page page) {
		WebURL webURL = page.getWebURL();
		int docid = webURL.getDocid();
		String url = webURL.getURL();
		String domain = webURL.getDomain();
		String path = webURL.getPath();
		String subDomain = webURL.getSubDomain();
		String parentUrl = webURL.getParentUrl();
		String anchor = webURL.getAnchor();
		
		System.out.println("Docid: " + docid);
		System.out.println("URL: " + url);
		System.out.println("Domain: " + domain);
		System.out.println("Path: " + path);
		System.out.println("Sub-domain: " + subDomain);
		System.out.println("Parent page: " + parentUrl);
		System.out.println("Anchor text: " + anchor);
		
		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
			List<WebURL> links = htmlParseData.getOutgoingUrls();
			
			System.out.println("Text length: " + text.length());
			System.out.println("Html length: " + html.length());
			System.out.println("Number of outgoing links: " +  links.size());
		}
		
		Header[] responseHeaders = page.getFetchResponseHeaders();
		if (responseHeaders != null) {
			System.out.println("Response headers: ");
			for (Header header : responseHeaders) {
				System.out.println("\t" + header.getName() + ": " + header.getValue());
			}
		}
		
		System.out.println("============");
	}
}
