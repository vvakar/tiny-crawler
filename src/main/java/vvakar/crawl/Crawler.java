package vvakar.crawl;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.apache.log4j.Logger;
import vvakar.beans.ClassifiedLinksBean;
import vvakar.beans.Page;
import vvakar.util.DomainUtil;
import vvakar.util.HtmlParseUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Recursively crawl a website, noting the site map structure. Do not follow external links.
 */
public class Crawler {
    private static final Logger log = Logger.getLogger(Crawler.class);
    private static final int LIMIT_PAGES = 100;

    private final Queue<String> toCrawl = new LinkedList<>();
    private final ResourceReader resourceReader;
    private final Map<String, Page> visitedPages = new HashMap<>();

    public Crawler(ResourceReader resourceReader) {
        this.resourceReader = resourceReader;
    }

    /**
     * Perform the recursive crawl.
     *
     * @param startingURL where to start
     * @throws IOException if something goes wrong
     */
    public void crawl(String startingURL) throws IOException {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(startingURL));
        String domain = DomainUtil.getDomain(startingURL);
        toCrawl.add(startingURL);
        process(domain);
    }

    private void process(String domain) throws IOException {
        while (!toCrawl.isEmpty() && visitedPages.size() < LIMIT_PAGES) {
            String url = toCrawl.poll();
            if (!visitedPages.containsKey(url)) {
                String html = resourceReader.read(url);

                Set<String> links = HtmlParseUtil.extractLinks(html);
                Set<String> imgs = HtmlParseUtil.extractImageLinks(html);
                ClassifiedLinksBean classifiedLinksBean = DomainUtil.classifyLinks(domain, links);
                Set<String> notSeenInternalLinks = classifiedLinksBean.internal
                        .stream()
                        .filter(u -> !visitedPages.containsKey(u) && !u.startsWith("#") && !u.startsWith("/#") && !u.startsWith("javascript:"))
                        .collect(Collectors.toSet());

                visitedPages.put(url, new Page(url, classifiedLinksBean, imgs));

                notSeenInternalLinks = DomainUtil.normalizeUrls(url, notSeenInternalLinks);
                toCrawl.addAll(notSeenInternalLinks); // queue up newly discovered internal links

                log.info("Collected " + url);
            }
        }
    }


    public Map<String,Page> getPages() {
        return visitedPages;
    }
}
