package vvakar.util;

import com.google.common.collect.ImmutableSet;
import org.junit.Test;
import vvakar.beans.ClassifiedLinksBean;
import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by valentin.vakar on 11/2/15.
 */
public class DomainUtilTest {
    private static final String DOMAIN = "wiprodigital.com";

    @Test
    public void testIdentifyInternalVsExternalLinks() {
        // given
        String externalLink = "http://external.com/linkA";
        String internalLink = "http://" + DOMAIN + "/linkB";
        String internalLink2 = "http://apps." + DOMAIN + "/linkD";
        String externalPhishingLink = "http://" + DOMAIN + ".phish/linkD";
        String relativeLink = "relative/linkC";

        // when
        Set<String> links = ImmutableSet.of(externalLink, internalLink, relativeLink, internalLink2, externalPhishingLink);
        ClassifiedLinksBean classifiedLinksBean = DomainUtil.classifyLinks(DOMAIN, links);

        // then
        assertEquals(ImmutableSet.of(externalLink, externalPhishingLink), classifiedLinksBean.external);
        assertEquals(ImmutableSet.of(internalLink, internalLink2, relativeLink), classifiedLinksBean.internal);
    }

    @Test
    public void classifyUnparseableLinksAsExternal() {
        //given
        Set<String> unparseable = ImmutableSet.of("\\//\\//malformed");

        // when
        ClassifiedLinksBean classifiedLinksBean = DomainUtil.classifyLinks(DOMAIN, unparseable);

        //then
        assertEquals(unparseable, classifiedLinksBean.external);
        assertEquals(Collections.EMPTY_SET, classifiedLinksBean.internal);
    }

    @Test
    public void testGetDomain() {
        String url = "http://" + DOMAIN + "/index.html";
        assertEquals(DOMAIN, DomainUtil.getDomain(url));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testGetDomainWithMalformedUrl() {
        DomainUtil.getDomain("malformed url!");
    }

    @Test
    public void testNormalizeUrl() {
        Set<String> toNormalize = ImmutableSet.of("/index.html");
        String baseUrl = "http://" + DOMAIN + "/about/";
        assertEquals(ImmutableSet.of(baseUrl + "index.html"), DomainUtil.normalizeUrls(baseUrl, toNormalize));
    }

    @Test
    public void testNormalizeUrlWithLengthOneSlash() {
        Set<String> toNormalize = ImmutableSet.of("/");
        String baseUrl = "http://" + DOMAIN + "/about/";
        assertEquals(ImmutableSet.of(baseUrl), DomainUtil.normalizeUrls(baseUrl, toNormalize));
    }

    @Test
    public void testNormalizeUrlWithLengthOneNonSlash() {
        Set<String> toNormalize = ImmutableSet.of("a");
        String baseUrl = "http://" + DOMAIN + "/about/";
        assertEquals(ImmutableSet.of(baseUrl + "a"), DomainUtil.normalizeUrls(baseUrl, toNormalize));
    }
}
