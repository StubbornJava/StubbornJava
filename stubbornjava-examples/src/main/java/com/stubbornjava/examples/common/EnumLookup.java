package com.stubbornjava.examples.common;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.Enums;
import com.google.common.collect.Maps;
import com.stubbornjava.common.EnumUtils;
import com.stubbornjava.common.Json;

public class EnumLookup {
    private static final Logger log = LoggerFactory.getLogger(EnumLookup.class);

    // {{start:enums}}
    public enum CardColor {
        RED,
        BLACK,
        ;
    }

    // Jackson annotation to print the enum as an Object instead of the default name.
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum CardSuit {
        // Unicode suits - https://en.wikipedia.org/wiki/Playing_cards_in_Unicode
        SPADE("Spade", String.valueOf((char) 0x2660), CardColor.BLACK),
        HEART("HEART", String.valueOf((char) 0x2665), CardColor.RED),
        DIAMOND("Diamond", String.valueOf((char) 0x2666), CardColor.RED),
        CLUB("Club", String.valueOf((char) 0x2663), CardColor.BLACK),
        ;

        private String displayName;
        private String symbol;
        private CardColor color;
        private CardSuit(String displayName, String symbol, CardColor color) {
            this.displayName = displayName;
            this.symbol =  symbol;
            this.color = color;
        }
        public String getDisplayName() {
            return displayName;
        }
        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }
        public String getSymbol() {
            return symbol;
        }
        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }
        public CardColor getColor() {
            return color;
        }
        public void setColor(CardColor color) {
            this.color = color;
        }
    // {{end:enums}}


        // {{start:iteration}}
        /*
         * Please don't do this! It is inefficient and it's
         * not very hard to use Guava or a static Map as an index.
         */
        public static CardSuit crappyFindByName(String name) {
            for (CardSuit suit : CardSuit.values()) {
                if (name.equals(suit.name())) {
                    return suit;
                }
            }
            return null;
        }
        // {{end:iteration}}

        // {{start:map}}
        private static final Map<String, CardSuit> nameIndex =
                Maps.newHashMapWithExpectedSize(CardSuit.values().length);
        static {
            for (CardSuit suit : CardSuit.values()) {
                nameIndex.put(suit.name(), suit);
            }
        }
        public static CardSuit lookupByName(String name) {
            return nameIndex.get(name);
        }
        // {{end:map}}

        // {{start:guava}}
        public static CardSuit getIfPresent(String name) {
            return Enums.getIfPresent(CardSuit.class, name).orNull();
        }
        // {{end:guava}}

        // {{start:mapDisplayName}}
        private static final Map<String, CardSuit> displayNameIndex =
                Maps.newHashMapWithExpectedSize(CardSuit.values().length);
        static {
            for (CardSuit suit : CardSuit.values()) {
                nameIndex.put(suit.getDisplayName(), suit);
            }
        }
        public static CardSuit lookupByDisplayName(String name) {
            return nameIndex.get(name);
        }
        // {{end:mapDisplayName}}

        // {{start:mapDisplayNameUtil}}
        private static final Function<String, CardSuit> func =
                EnumUtils.lookupMap(CardSuit.class, e -> e.getDisplayName());
        public static CardSuit lookupByDisplayNameUtil(String name) {
            return func.apply(name);
        }
        // {{end:mapDisplayNameUtil}}
    }

    // {{start:main}}
    public static void main(String[] args) {
        List<String> names = Arrays.stream(CardSuit.values())
                                   .map(e -> e.name())
                                   .collect(Collectors.toList());
        names.add("Missing");

        List<String> displayNames = Arrays.stream(CardSuit.values())
                                          .map(e -> e.getDisplayName())
                                          .collect(Collectors.toList());
        displayNames.add("Missing");


        log.debug("Running valueOf");
        for (String name : names) {
            try {
                log.debug("looking up {} found {}", name, Json.serializer().toString(CardSuit.valueOf(name)));
            } catch (Exception ex) {
                log.warn("Exception Thrown", ex);
            }
        }

        log.debug("Running crappyFindByName");
        for (String name : names) {
            log.debug("looking up {} found {}", name, Json.serializer().toString(CardSuit.crappyFindByName(name)));
        }

        log.debug("Running lookupByName");
        for (String name : names) {
            log.debug("looking up {} found {}", name, Json.serializer().toString(CardSuit.lookupByName(name)));
        }

        log.debug("Running Guava getIfPresent");
        for (String name : names) {
            log.debug("looking up {} found {}", name, Json.serializer().toString(CardSuit.getIfPresent(name)));
        }

        log.debug("Running lookupByDisplayName");
        for (String displayName : displayNames) {
            log.debug("looking up {} found {}", displayName, Json.serializer().toString(CardSuit.lookupByDisplayName(displayName)));
        }

        log.debug("Running lookupByDisplayNameUtil");
        for (String displayName : displayNames) {
            log.debug("looking up {} found {}", displayName, Json.serializer().toString(CardSuit.lookupByDisplayNameUtil(displayName)));
        }
    }
    // {{end:main}}
}
