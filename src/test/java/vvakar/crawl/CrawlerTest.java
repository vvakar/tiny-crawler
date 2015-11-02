package vvakar.crawl;

import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class CrawlerTest {
    private Crawler crawler;
    private ResourceReader mockReader;

    @Before
    public void before() {
        mockReader = mock(ResourceReader.class);
        crawler = new Crawler(mockReader);
    }

    @Test
    public void testCrawlSinglePage() throws Exception {
        // given
        String leafURL = "http://leaf.com/abc";
        String LEAF_HTML = "<html/>";
        when(mockReader.read(leafURL)).thenReturn(LEAF_HTML);

        // when
        crawler.crawl(leafURL);

        // then
        assertEquals(ImmutableSet.of(leafURL), crawler.getPages().keySet());
        verify(mockReader).read(leafURL);
    }

    @Test
    public void testCrawlTwoPages() throws Exception {
        // given
        String childURL = "http://child.com/abc";
        String CHILD_HTML = "<html/>";
        String parentURL = "http://child.com/parent";
        String PARENT_HTML = "<html><body><a href='" + childURL + "'>Child</a></body></html>";
        when(mockReader.read(childURL)).thenReturn(CHILD_HTML);
        when(mockReader.read(parentURL)).thenReturn(PARENT_HTML);

        // when
        crawler.crawl(parentURL);

        // then
        assertEquals(ImmutableSet.of(childURL, parentURL), crawler.getPages().keySet());
        verify(mockReader).read(childURL);
        verify(mockReader).read(parentURL);
    }


    // TODO: test cycles in the site graph
    // TODO: perform more detailed verification of the Page contents in above tests
}
