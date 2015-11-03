package vvakar.beans;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a web page with its outbound links
 */
public class Page {
    public final String url;
    public final ClassifiedLinksBean classifiedLinksBean;
    public final Set<String> imageLinks;

    public Page(String url, ClassifiedLinksBean classifiedLinksBean, Collection<String> imgs) {
        this.url = url;
        this.classifiedLinksBean = classifiedLinksBean;
        this.imageLinks = Collections.unmodifiableSet(new HashSet<>(imgs));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(url);
        imageLinks.stream().forEach(l -> sb.append("\n\tImg: " + l));
        classifiedLinksBean.internal.stream().forEach(i -> sb.append("\n\tLink: " + i));
        classifiedLinksBean.external.stream().forEach(i -> sb.append("\n\tExternal Link: " + i));
        return sb.toString();
    }
}
