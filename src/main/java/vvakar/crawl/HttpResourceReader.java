package vvakar.crawl;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 *  Read an http resource and return its contents as a string. Fails silently.
 */
public class HttpResourceReader implements ResourceReader {
    private final static Logger log = Logger.getLogger(HttpResourceReader.class);

    public String read(String url) throws IOException {
        String html = "";
        try ( InputStream in = new URL(url).openStream()) {
            html = IOUtils.toString(in);
        } catch (Exception ex) {
            log.error("Cannot read URL " + url + ", skipping");
        }

        return html;
    }
}
