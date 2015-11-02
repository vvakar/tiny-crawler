package vvakar.beans;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Sets of links classified by their scope, e.g. internal vs external
 */
public class ClassifiedLinksBean {
    public final Set<String> internal;
    public final Set<String> external;

    public ClassifiedLinksBean(Collection<String> internal, Collection<String> external) {
        this.internal = Collections.unmodifiableSet(new HashSet<>(internal));
        this.external = Collections.unmodifiableSet(new HashSet<>(external));
    }
}
