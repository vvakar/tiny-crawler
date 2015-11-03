package vvakar.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.apache.log4j.Logger;
import vvakar.beans.ClassifiedLinksBean;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.Set;

/**
 * Utilities for domain-related operations
 */
public class DomainUtil {
    private static final Logger log = Logger.getLogger(DomainUtil.class);

    /**
     * Sort out whether links belong to specified domain or are external.
     */
    public static ClassifiedLinksBean classifyLinks(String domain, Set<String> links) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(domain));
        Preconditions.checkNotNull(links);

        Set<String> external = new HashSet<>();
        Set<String> internal = new HashSet<>();

        for (String link : links) {
            if (isPartOfDomain(domain, link)) {
                internal.add(link);
            } else {
                external.add(link);
            }
        }
        return new ClassifiedLinksBean(internal, external);
    }

    /**
     * Figure out domain name from given url
     *
     * @param link valid url
     * @return domain name
     */
    public static String getDomain(String link) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(link));
        try {
            String host = new URI(link).getHost();
            return host;
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException("Couldn't parse url " + link, ex);
        }
    }

    /**
     * Figure out if link is from this domain. Fail silently.
     *
     * @return true if link belongs to domain, false if not or something went wrong
     */
    private static boolean isPartOfDomain(String domain, String link) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(domain));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(link));

        boolean isIt = false;
        try {
            String host = new URI(link).getHost();
            isIt = host == null /*relative path*/ || host.endsWith(domain);
        } catch (URISyntaxException ex) {
            log.error("Couldn't parse url " + link, ex);
        }

        return isIt;
    }


    /**
     * Best effort to make specified urls canonical. For example, relative urls are rewritten on top of the base url.
     * Lots of improvements possible.
     *
     * @return new set with updated urls
     */
    public static Set<String> normalizeUrls(String baseUrl, Set<String> toNormalize) {
        Set<String> notSeenFixed = new HashSet<>();
        for (String notSeen : toNormalize) {
            if (!isAbsoluteUrl(notSeen)) {
                if (notSeen.startsWith("/") && baseUrl.endsWith("/")) {
                    notSeen = baseUrl.substring(0, baseUrl.length() - 1) + notSeen;
                } else {
                    notSeen = baseUrl + notSeen;
                }
            }
            notSeenFixed.add(notSeen);
        }
        return notSeenFixed;
    }

    /**
     * Cheap attempt to guess whether url is absolute
     */
    private static boolean isAbsoluteUrl(String url) {
        return url.startsWith("http");
    }
}
