/**
 * This is the controller for BasicCrawler.
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
public class BasicCrawlController {

	/**
	 * @param args 
	 * args[0]: folder to store intermediate data; 
	 * args[1]: number of concurrent threads.
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.out.println("Needed parameters: ");
			System.out.println("\t rootFolder (it will contain intermediate crawl data)");
			System.out.println("\t numberOfCralwers (number of concurrent threads)");

			return;
		}
		
		String rootFolder = args[0];
		int numberOfCrawlers = Integer.parseInt(args[1]);
		
		
		CrawlConfig config = new CrawlConfig();
		
		config.setCrawlStorageFolder(rootFolder);
		config.setIncludeBinaryContentInCrawling(true);
		config.setPolitenessDelay(100);
		config.setMaxPagesToFetch(1000);
		config.setMaxDepthOfCrawling(2);
		config.setResumableCrawling(false);
		
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
		
		controller.addSeed("http://jandan.net/");
		
		controller.start(BasicCrawler.class, numberOfCrawlers);

	}

}
