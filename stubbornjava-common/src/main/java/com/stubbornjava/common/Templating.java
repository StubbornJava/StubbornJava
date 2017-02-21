package com.stubbornjava.common;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.JsonNodeValueResolver;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.cache.ConcurrentMapTemplateCache;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.googlecode.htmlcompressor.compressor.HtmlCompressor;
// {{start:templating}}
public class Templating {
    private static final Logger log = LoggerFactory.getLogger(Templating.class);

    // Once again using static for convenience use your own DI method.
    private static final Templating DEFAULT;
    static {
        Templating.Builder builder =
            new Templating.Builder()
                          .withHelper("dateFormat", TemplateHelpers::dateFormat);
        // Don't cache locally, makes development annoying
        if (Env.LOCAL != Env.get()) {
            builder.withCaching()
                   .withResourceLoaders();
        } else {
            if (!Configs.properties().hasPath("assets.roots")) {
                log.error("Configuration is missing required \"assets.roots\" config for dev mode. Did you forget to add it to application.local.conf?");
                Preconditions.checkNotNull(null, "Missing required \"assets.roots\" config.");
            }
            List<String> roots = Configs.properties().getStringList("assets.roots");
            for (String root : roots) {
                builder.withLocalResourceLoaders(root);
            }
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

    public String renderHtmlTemplate(String templateName, Object data) {
        String response = renderTemplate(templateName, data);
        return compressor.compress(response);
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

    public String renderRawHtmlTemplate(String rawTemplate, Object data) {
        String response = rednerRawTemplate(rawTemplate, data);
        return compressor.compress(response);
    }

    public String rednerRawTemplate(String rawTemplate, Object data) {
        Template template;
        try {
            template = handlebars.compileInline(rawTemplate);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return render(template, data);
    }

    private String render(Template template, Object data) {
        Context context = null;
        try {
            JsonNode node = Json.serializer().nodeFromObject(data);
            // Very useful for debugging templates
            if (log.isDebugEnabled()) {
                log.debug("rendering template " + template.filename() + "\n" + Json.serializer().toPrettyString(node));
            }
            context = Context.newBuilder(node)
                             .resolver(JsonNodeValueResolver.INSTANCE)
                             .build();
            return template.apply(context);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (null != context) {
                context.destroy();
            }
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

        public Templating build() {
            handlebars.with(loaders.toArray(new TemplateLoader[0]));
            return new Templating(handlebars);
        }
    }
}
// {{end:templating}}
