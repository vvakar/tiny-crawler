package vvakar.util;

import com.google.common.collect.ImmutableSet;
import org.junit.Test;
import vvakar.beans.ClassifiedLinksBean;
import vvakar.util.DomainUtil;

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
}
