package com.stubbornjava.webapp.github;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class FileContentUtils {
    private static final Pattern startPattern = Pattern.compile("\\{\\{start?:(.*)\\}\\}");
    private static final Pattern spacePattern = Pattern.compile("^(\\s*)");

    public static Map<String, FileContent.Section> parseContent(String raw) {
        Map<String, FileContent.Section> sections = Maps.newHashMap();
        int currentLineNum = 1;
        List<String> lines = Lists.newArrayList();
        boolean inSection = false;
        String sectionName = null;
        int startLineNum = 0;
        int endLineNum = 0;

        try (Scanner sc = new Scanner(raw)) {
            while (sc.hasNextLine()) {
                String currentLine = sc.nextLine();

                // We are not in a section try to find one.
                if (!inSection) {
                    Matcher matcher = startPattern.matcher(currentLine);
                    if (matcher.find()) {
                        // reset all variables
                        startLineNum = currentLineNum + 1;
                        sectionName = matcher.group(1);
                        inSection = true;
                        lines.clear();
                    }
                } else {
                    if (currentLine.contains("{{end:" + sectionName + "}}")) {
                        inSection = false;
                        endLineNum = currentLineNum - 1;
                        if (lines.size() > 0) {
                            Matcher matcher = spacePattern.matcher(lines.get(0));
                            if (matcher.find()) {
                                // Here we are stripping out indentations so it reads better.
                                int indentSpaces = matcher.group(1).length();
                                StringBuilder sb = new StringBuilder();
                                lines.stream().forEach(line -> {
                                    String replaced = line.replaceAll("\t", "    "); // replace tabs with 4 spaces
                                    sb.append(line.substring(Math.min(replaced.length(), indentSpaces)) + "\n");
                                });
                                sections.put(sectionName, new FileContent.Section(startLineNum, endLineNum, sb.toString()));
                            }
                        }
                    } else {
                        lines.add(currentLine);
                    }
                }

                currentLineNum++;
            }
        }

        if (inSection == true) {
            throw new IllegalArgumentException("unmatching section for " + sectionName);
        }
        return sections;
    }
}
