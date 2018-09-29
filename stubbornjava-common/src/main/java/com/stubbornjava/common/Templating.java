package com.stubbornjava.common;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.HumanizeHelper;
import com.github.jknack.handlebars.MarkdownHelper;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.cache.ConcurrentMapTemplateCache;
import com.github.jknack.handlebars.helper.AssignHelper;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.googlecode.htmlcompressor.compressor.HtmlCompressor;
// {{start:templating}}
public class Templating {
    private static final Logger log = LoggerFactory.getLogger(Templating.class);
    private static final Map<Object, Object> NO_DATA = Maps.newHashMapWithExpectedSize(0);

    // Once again using static for convenience use your own DI method.
    private static final Templating DEFAULT;
    static {
        Templating.Builder builder =
            new Templating.Builder()
                          .withHelpers(new TemplateHelpers())
                          .withHelper("md", new MarkdownHelper())
                          .withHelper(AssignHelper.NAME, AssignHelper.INSTANCE)
                          .register(HumanizeHelper::register);
        // Don't cache locally, makes development annoying
        if (Env.LOCAL != Env.get()) {
            builder.withCaching()
                   .withResourceLoaders();
        } else {
            String root = AssetsConfig.assetsRoot();
            builder.withLocalResourceLoaders(root);
        }
        DEFAULT = builder.build();
    }

    public static Templating instance() {
        return DEFAULT;
    }

    private final Handlebars handlebars;
    private final HtmlCompressor compressor = new HtmlCompressor();
    Templating(Handlebars handlebars) {
        this.handlebars = handlebars;
    }

    public String renderHtmlTemplate(String templateName) {
        return renderHtmlTemplate(templateName, NO_DATA);
    }

    public String renderHtmlTemplate(String templateName, Object data) {
        String response = renderTemplate(templateName, data);
        return compressor.compress(response);
    }

    public String renderTemplate(String templateName) {
        return renderTemplate(templateName, NO_DATA);
    }

    public String renderTemplate(String templateName, Object data) {
        Template template;
        try {
            template = handlebars.compile(templateName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return render(template, data);
    }

    public String renderRawHtmlTemplate(String rawTemplate) {
        return renderRawTemplate(rawTemplate, NO_DATA);
    }

    public String renderRawHtmlTemplate(String rawTemplate, Object data) {
        String response = renderRawTemplate(rawTemplate, data);
        return compressor.compress(response);
    }

    public String renderRawTemplate(String rawTemplate) {
        return renderRawTemplate(rawTemplate, NO_DATA);
    }

    public String renderRawTemplate(String rawTemplate, Object data) {
        Template template;
        try {
            template = handlebars.compileInline(rawTemplate);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return render(template, data);
    }

    private String render(Template template, Object data) {
        try {
            // Can't currently get the jackson module working not sure why.
            Map<String, Object> jsonMap = Json.serializer().mapFromJson(Json.serializer().toString(data));
            if (log.isDebugEnabled()) {
                log.debug("rendering template " + template.filename() + "\n" + Json.serializer().toPrettyString(jsonMap));
            }
            return template.apply(jsonMap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static class Builder {
        private final Handlebars handlebars = new Handlebars();
        private final List<TemplateLoader> loaders = Lists.newArrayList();
        public Builder() {

        }

        public Builder withResourceLoaders() {
            log.debug("using resource loaders");
            loaders.add(new ClassPathTemplateLoader());
            loaders.add(new ClassPathTemplateLoader(TemplateLoader.DEFAULT_PREFIX, ".sql"));
            return this;
        }

        public Builder withLocalResourceLoaders(String root) {
            log.debug("using local loaders");
            loaders.add(new FileTemplateLoader(root));
            loaders.add(new FileTemplateLoader(root, ".sql"));
            return this;
        }

        public Builder withCaching() {
            log.debug("Using caching handlebars");
            handlebars.with(new ConcurrentMapTemplateCache());
            return this;
        }

        public <T> Builder withHelper(String helperName, Helper<T> helper) {
            log.debug("using template helper {}" , helperName);
            handlebars.registerHelper(helperName, helper);
            return this;
        }

        public <T> Builder withHelpers(Object helpers) {
            log.debug("using template helpers {}" , helpers.getClass());
            handlebars.registerHelpers(helpers);
            return this;
        }

        public <T> Builder register(Consumer<Handlebars> consumer) {
            log.debug("registering helpers");
            consumer.accept(handlebars);
            return this;
        }

        public Templating build() {
            handlebars.with(loaders.toArray(new TemplateLoader[0]));
            return new Templating(handlebars);
        }
    }
}
// {{end:templating}}
