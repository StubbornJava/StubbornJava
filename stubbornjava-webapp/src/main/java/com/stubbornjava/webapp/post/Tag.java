package com.stubbornjava.webapp.post;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder=true)
public class Tag {
    private final long tagId;
    private final String name;

    public TagBuilder newBuilder() {
        return Tag.builder()
                  .tagId(tagId)
                  .name(name);
    }

    public static void main(String[] args) {
        Tag t = Tag.builder().name("test").build();
        Tag t2 = t.toBuilder().tagId(1L).build();
        System.out.println(t2);
    }
}
