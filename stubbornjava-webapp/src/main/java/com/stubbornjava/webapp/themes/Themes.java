package com.stubbornjava.webapp.themes;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.jooq.lambda.Seq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;
import com.stubbornjava.common.Json;

// {{start:themes}}
public class Themes {
    private static final Logger log = LoggerFactory.getLogger(Themes.class);
    private Themes() {}

    // A list of all the theme websites we currently support.
    private static List<Supplier<List<HtmlCssTheme>>> suppliers = Lists.newArrayList(
        BootstrapBayScraper::popularThemes,
        TemplateMonsterScraper::popularThemes,
        WrapBootstrapScraper::popularThemes
    );

    // Sort by downloads desc then by name.
    private static final Comparator<HtmlCssTheme> popularSort =
        Comparator.comparing(HtmlCssTheme::getDownloads).reversed()
                  .thenComparing(HtmlCssTheme::getTitle);

    // Fetch all themes and sort them together.
    private static final List<HtmlCssTheme> fetchPopularThemes() {
        return Seq.seq(suppliers)
                  .map(sup -> {
                    /*
                     *  If one fails we don't want them all to fail.
                     *  This can be handled better but good enough for now.
                     */
                    try {
                        return sup.get();
                    } catch (Exception ex) {
                        log.warn("Error fetching themes", ex);
                        return Lists.<HtmlCssTheme>newArrayList();
                    }
                  })
                  .flatMap(List::stream)
                  .sorted(popularSort)
                  .toList();
    }

    /*
     *  Fetch all themes and cache for 4 hours. It takes a little time
     *  to scrape all the sites. We also want to be nice and not spam the sites.
     */
    private static final Supplier<List<HtmlCssTheme>> themesSupplier =
        Suppliers.memoizeWithExpiration(Themes::fetchPopularThemes, 4L, TimeUnit.HOURS);

    public static List<HtmlCssTheme> getPopularThemes(int num) {
        return Seq.seq(themesSupplier.get()).limit(num).toList();
    }

    public static void main(String[] args) {
        List<HtmlCssTheme> themes = getPopularThemes(50);
        log.debug(Json.serializer().toPrettyString(themes));
    }
}
// {{end:themes}}
