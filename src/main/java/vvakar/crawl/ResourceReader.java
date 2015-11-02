package vvakar.crawl;

import java.io.IOException;

/**
 * Something that has the ability to fetch the contents of a resource
 */
public interface ResourceReader {

    /**
     * Read resource successfully or throw {@code IOException}
     */
    String read(String url) throws IOException;
}
