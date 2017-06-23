package com.stubbornjava.webapp.guide;

public class GuideTitle {
    private final String title;
    private final String slug;
    public GuideTitle(String title, String slug) {
        super();
        this.title = title;
        this.slug = slug;
    }
    public String getTitle() {
        return title;
    }
    public String getSlug() {
        return slug;
    }
}
