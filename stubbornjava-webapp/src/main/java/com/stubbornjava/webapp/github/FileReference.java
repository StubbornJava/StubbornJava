package com.stubbornjava.webapp.github;

public class FileReference {
    private final String owner;
    private final String repo;
    private final String name;
    private final String path;
    private FileReference(String owner, String repo, String name, String path) {
        super();
        this.owner = owner;
        this.repo = repo;
        this.name = name;
        this.path = path;
    }
    public String getOwner() {
        return owner;
    }
    public String getRepo() {
        return repo;
    }
    public String getName() {
        return name;
    }
    public String getPath() {
        return path;
    }

    private static final String stubbornJavaOwner = "StubbornJava";
    private static final String stubbornJavaRepo = "StubbornJava";
    private static final String stubbornJavaExampleRepo = "StubbornJava-example";

    public static FileReference stubbornJava(String name, String path) {
        return new FileReference(stubbornJavaOwner, stubbornJavaRepo, name, path);
    }

    public static FileReference stubbornJavaExample(String name, String path) {
        return new FileReference(stubbornJavaOwner, stubbornJavaExampleRepo, name, path);
    }
}
