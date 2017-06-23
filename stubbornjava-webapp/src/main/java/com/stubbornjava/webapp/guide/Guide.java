package com.stubbornjava.webapp.guide;

public class Guide {
    private final String title;
    private final String slug;
    private final String content;

    public Guide(String title, String slug, String content) {
        super();
        this.title = title;
        this.slug = slug;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }
    public String getSlug() {
        return slug;
    }
    public String getContent() {
        return content;
    }

}
