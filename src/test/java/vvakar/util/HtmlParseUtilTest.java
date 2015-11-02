package vvakar.util;

import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import static org.junit.Assert.*;

public class HtmlParseUtilTest {
    private static final String DOMAIN = "wiprodigital.com";
    private static final String HTML_FIXTURE = "<html>\n" +
            "<body>\n" +
            "<p>Test body\n" +
            "</p>\n" +
            "<a href = \"http://somewhere.com/first.html\">First</a>\n" +        // external link
            "<a href = \"second.html\">Second</a>\n" +                           // relative internal link
            "<a href = \"http://" + DOMAIN + "/absolute.html\">Absolute</a>\n" + // absolute internal link
            "<img src = \"image1.jpg\"/>" +                                     // img
            "<a href=\"myfile.htm\"><img src=\"rainbow.gif\"></img></a>" +      // img anchor
            "</body>\n" +
            "</html>";


    @Test
    public void testGetLinks() throws Exception {
        assertEquals(ImmutableSet.of("http://wiprodigital.com/absolute.html",
                "http://somewhere.com/first.html",
                "second.html",
                "myfile.htm"), HtmlParseUtil.extractLinks(HTML_FIXTURE));
    }

    @Test
    public void testGetImages() throws Exception {
        assertEquals(ImmutableSet.of("image1.jpg", "rainbow.gif"), HtmlParseUtil.extractImageLinks(HTML_FIXTURE));
    }

    @Test
    public void failSilentlyIfHtmlIsUnreadable() {
        assertEquals(0, HtmlParseUtil.extractLinks("<html invalid>").size());
    }
}
