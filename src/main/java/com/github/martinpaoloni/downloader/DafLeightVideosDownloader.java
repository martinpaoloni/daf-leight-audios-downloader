package com.github.martinpaoloni.downloader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Gets video URLs for DaF Leight books.
 * <p>
 * https://www.klett-sprachen.de/dafleicht-online/
 */
public class DafLeightVideosDownloader {

    private static String CHARSET_NAME = "utf8";
    private static final String CSS_QUERY = "iframe";

    public static void main(String[] args) throws IOException {

        Map<String, String> books = new HashMap<>();
        books.put("_1", "A1.1");
        books.put("_2", "A1.2");
        books.put("_21", "A2.1");
        books.put("_22", "A2.2");
        books.put("_B11", "B1.1");
        books.put("_B12", "B1.2");

        String pathFormat = "https://www.klett-sprachen.de/dafleicht-online/%s/%s";

        Map<String, String> pages = new HashMap<>();
        pages.put("landeskunde.html", "Landeskunde");
        pages.put("grammatik.html", "Grammatik");


        //For each book
        for (Map.Entry<String, String> book : books.entrySet()) {
            System.out.println("Videos for book " + book.getValue());
            //For each media category
            for (Map.Entry<String, String> page : pages.entrySet()) {
                System.out.println("= " + page.getValue());
                String url = String.format(pathFormat, book.getKey(), page.getKey());

                Elements elements = findElementsByQuery(new URL(url), CSS_QUERY);
                List<String> vimeoUrls = elements.stream().map(e -> e.attr("src")).collect(Collectors.toList());
                for(String vimeoUrl : vimeoUrls) {
                    System.out.println(vimeoUrl);
                }
            }
        }
    }

    private static Elements findElementsByQuery(URL url, String cssQuery) throws IOException {
        Document doc = Jsoup.parse(url, 30000);
        return doc.select(cssQuery);
    }
}
