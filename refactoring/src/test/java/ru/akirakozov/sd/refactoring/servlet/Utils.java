package ru.akirakozov.sd.refactoring.servlet;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Utils {
    public static final String NAME = "name";
    public static final String PRICE = "price";
    public static final String COMMAND = "command";
    public static final String GET_METHOD = "GET";
    public static final String EOLN = "\n";
    public static final String TAB = "\t";
    public static final String BR_TAG = "</br>";
    public static final String HTML_BODY_OPEN_TAG = "<html><body>";
    public static final String BODY_HTML_CLOSE_TAG = "</body></html>";

    public enum Commands {
        MAX("max"),
        MIN("min"),
        SUM("sum"),
        COUNT("count");

        private final String name;

        Commands(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum Store {
        PHONE("iphone6", "300"),
        GUITAR("guitar", "1000"),
        GUITAR2("guitar2", "1000"),
        PIANO("piano", "700"),
        PIANO2("piano2", "700"),
        DRUMS("drums", "500");

        private final String name;
        private final String price;

        Store(String name, String price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public String getPrice() {
            return price;
        }
    }

    public static class StringPrintWriter extends PrintWriter {
        public StringPrintWriter() {
            super(new StringWriter());
        }

        @Override
        public String toString() {
            return out.toString();
        }
    }
}
