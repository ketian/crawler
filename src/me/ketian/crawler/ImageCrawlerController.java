/**
 * This is the controller for ImageCrawler.
 * It generates proper configuration and calls the crawler.
 */
package me.ketian.crawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * @author ketian
 *
 */
public class ImageCrawlerController {

	/**
	 * @param args 
	 * args[0]: folder to store intermediate data; 
	 * args[1]: number of concurrent threads.
	 * args[2]: folder to store images.
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 3) {
			System.out.println("Needed parameters: ");
			System.out.println("\t rootFolder (it will contain intermediate crawl data)");
			System.out.println("\t numberOfCralwers (number of concurrent threads)");
			System.out.println("\t storageFolder (a folder for storing downloaded images)");
			return;
		}
		String rootFolder = args[0];
		int numberOfCrawlers = Integer.parseInt(args[1]);
		String storageFolder = args[2];
		
		CrawlConfig config = new CrawlConfig();
		
		config.setCrawlStorageFolder(rootFolder);
		config.setIncludeBinaryContentInCrawling(true);
		config.setPolitenessDelay(100);
		config.setMaxPagesToFetch(1000);
		config.setMaxDepthOfCrawling(2);
		config.setResumableCrawling(false);
		
		String[] crawlDomains = new String[] { 
				//"http://jandan.net/ooxx",
				"http://jandan.net/pic"
				 };
		
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
		
		for (String domain: crawlDomains) {
			controller.addSeed(domain);
		}
		
		ImageCrawler.configure(crawlDomains, storageFolder);
		controller.start(ImageCrawler.class, numberOfCrawlers);
	}
	
}
