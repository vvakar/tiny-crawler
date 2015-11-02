package vvakar.crawl;

import vvakar.beans.Page;

import java.util.Map;

/**
 * Drive the crawler
 */
public class App {
    public static void main(String[] args)  throws Exception {
        Crawler crawler = new Crawler(new HttpResourceReader());
        crawler.crawl(args[0]);
        Map<String, Page> pages = crawler.getPages();

        pages.values().stream().forEach(v -> System.out.println(v));
    }
}
