package vvakar.util;

import com.google.common.base.Preconditions;
import org.apache.log4j.Logger;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.util.HashSet;
import java.util.Set;

/**
 * Html parsing utils
 */
public class HtmlParseUtil {
    private static final Logger log = Logger.getLogger(HtmlParseUtil.class);

    /**
     * Expressions for extracting supported elements.
     * TODO: add script, source, etc tag support for more accurate site map
     */
    private static final XPathExpression ANCHORS, IMGS;
    static {
        try {
            ANCHORS = XPathFactory.newInstance().newXPath().compile("//a/@href");
            IMGS = XPathFactory.newInstance().newXPath().compile("//img/@src");
        } catch (Exception e) {
            throw new IllegalStateException("Should never happen", e);
        }
    }

    /**
     * Extract anchors from html source. Fail silently if there's trouble.
     *
     * @param html String-ified html source
     * @return Set of links found on the page or empty set
     */
    public static Set<String> extractLinks(String html) {
        return extractFromPage(html, ANCHORS);
    }

    /**
     * Extract image links from html source. Fail silently if there's trouble.
     *
     * @param html String-ified html source.
     * @return Set of images found on the page or empty set
     */
    public static Set<String> extractImageLinks(String html) {
        return extractFromPage(html, IMGS);
    }

    private static Set<String> extractFromPage(String html, XPathExpression expression) {
        Preconditions.checkNotNull(html);
        Preconditions.checkNotNull(expression);

        Set<String> urls = new HashSet<>();
        try {
            // TODO: improve the efficiency of instantiating these objects - singleton?
            TagNode tagNode = new HtmlCleaner().clean(html);
            org.w3c.dom.Document doc = new DomSerializer(
                    new CleanerProperties()).createDOM(tagNode);
            NodeList nodeList = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodeList.getLength(); ++i) {
                Node node = nodeList.item(i);
                urls.add(node.getNodeValue());
            }
        } catch (Exception ex) {
            log.error("Error parsing source: " + html, ex);
        }

        return urls;
    }
}
