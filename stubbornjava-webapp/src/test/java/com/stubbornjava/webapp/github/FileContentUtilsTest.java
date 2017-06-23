package com.stubbornjava.webapp.github;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.stubbornjava.common.Resources;
import com.stubbornjava.webapp.github.FileContent.Section;

public class FileContentUtilsTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void parseSucceeds() {
        String raw = Resources.asString("FileContentUtils/SectionsTest.java.txt");
        Map<String, Section> sections = FileContentUtils.parseContent(raw);
        assertTrue(sections.size() == 2);
        assertTrue(sections.get("method1").getContent().equals("public static String method1() {\n    return \"method1\";\n}\n"));
        assertTrue(sections.get("method2").getContent().equals("public static String method2() {\n    return \"method2\";\n}\n"));
    }

    @Test
    public void parseFails() {
        String raw = Resources.asString("FileContentUtils/SectionsTestFail.java.txt");
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("unmatching section");
        FileContentUtils.parseContent(raw);
    }

}
