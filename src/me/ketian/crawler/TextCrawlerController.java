/**
 * This is the controller for TextCrawler.
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
public class TextCrawlerController {

	/**
	 * @param args
	 * args[0]: folder to store intermediate data; 
	 * args[1]: number of concurrent threads.
	 * args[2]: folder to store texts.
	 * args[3]: whether data should be stored in one file.
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 4) {
			System.out.println("Needed parameters: ");
			System.out.println("\t rootFolder (it will contain intermediate crawl data)");
			System.out.println("\t numberOfCralwers (number of concurrent threads)");
			System.out.println("\t storageFolder (a folder for storing downloaded texts)");
			System.out.println("\t oneFile (shall the crawler put all data into one file?)");
			return;
		}
		String rootFolder = args[0];
		int numberOfCrawlers = Integer.parseInt(args[1]);
		String storageFolder = args[2];
		Boolean oneFile = Boolean.parseBoolean(args[3]);
		
		if (oneFile && numberOfCrawlers>1) {
			numberOfCrawlers = 1;
			System.out.println("In order to avoid problem of synchronization, numberOfCrawlers can only be 1!");
		}
		
		CrawlConfig config = new CrawlConfig();
		
		config.setCrawlStorageFolder(rootFolder);
		config.setIncludeBinaryContentInCrawling(true);
		config.setPolitenessDelay(100);
		config.setMaxPagesToFetch(1000);
		config.setMaxDepthOfCrawling(1);
		config.setResumableCrawling(false);
		
		String[] crawlDomains = new String[] { 
				"http://ds.eywedu.com/jinyong/tlbb"
				 };
		
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
		
		for (String domain: crawlDomains) {
			controller.addSeed(domain);
		}
		
		TextCrawler.configure(crawlDomains, storageFolder);
		TextCrawler.setOneFile(oneFile);
		controller.start(TextCrawler.class, numberOfCrawlers);
	}

}
