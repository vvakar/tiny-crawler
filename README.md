#Tiny Crawler

Crawl a website starting at the specified url. The output is a listing of discovered pages along with their outbound internal and external links, as well as any images they may reference. It will not list script and other referenced resources, but the intent is to be extensible.

For demonstration purposes, the crawler is limited to 100 pages.


##Running

mvn exec:java -Dexec.mainClass=vvakar.crawl.App -Dexec.args='http://wiprodigital.com/'


##Design Limitations

This code represents a work-in-progress state of a barely functioning crawler. As design emerges through TDD, we can consider following refactorings:
* `Crawler.process()` should not have to populate `Page` with the links. The `Page` object should come pre-populated by some underlying abstraction.
* `HtmlParseUtil` has to parse the DOM separately for each type of element (two currently available: anchor tags and images). Unify that logic and make it easier to extend.
* `ClassifiedLinksBean` is a relic from an earlier design stage. Integrate it better with the concept of a `Page`, and consider evolution toward script and other resources.
* Should not assume linked resources are HTML pages. Check Content-type, Content-encoding headers (e.g. don't try to parse pdf's)
* Handle redirects and otherwise check http response codes and consider desired outcomes (e.g. - if we get a 500, should we retry later?).
* Handle https
* Parsing HTML must account for various structural issues and have graceful ways around them. The crawler does that only at a very basic level.
* Can only see static links. It won't understand javascript, flash, or other dynamically generated links.
* Single-threaded with blocking I/O, hence limited capacity.
* Tests are somewhat superficial and do not make the effort to cover some of the edge cases



