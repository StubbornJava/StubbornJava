package com.stubbornjava.webapp.github;

import java.util.Map;


public class FileContent {
    private final String name;
    private final String fileName;
    private final String url;
    private final Section content;
    private final Map<String, Section> sections;

    public FileContent(String name, String fileName, String url, String content) {
        super();
        this.name = name;
        this.fileName = name;
        this.url = url;
        this.content = new Section(1, 1, content);
        this.sections = FileContentUtils.parseContent(content);
    }
    public String getName() {
        return name;
    }
    public String getFileName() {
        return fileName;
    }
    public String getUrl() {
        return url;
    }
    public Section getContent() {
        return content;
    }
    public Map<String, Section> getSections() {
        return sections;
    }

    public static class Section {
        private final int lineStart;
        private final int lineEnd;
        private final String content;
        public Section(int lineStart, int lineEnd, String content) {
            super();
            this.lineStart = lineStart;
            this.lineEnd = lineEnd;
            this.content = content;
        }
        public int getLineStart() {
            return lineStart;
        }
        public int getLineEnd() {
            return lineEnd;
        }
        public String getContent() {
            return content;
        }
    }
}
