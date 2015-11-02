package vvakar.crawl;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 *  Read an http resource and return its contents as a string
 */
public class HttpResourceReader implements ResourceReader {

    public String read(String url) throws IOException {
        try ( InputStream in = new URL(url).openStream()) {
            return IOUtils.toString(in);
        }
    }
}
