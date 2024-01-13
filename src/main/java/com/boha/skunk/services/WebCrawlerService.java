package com.boha.skunk.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class WebCrawlerService {
    static final String mm = "\uD83C\uDF0D\uD83C\uDF0D\uD83C\uDF0D\uD83C\uDF0D " +
            "WebCrawlerService \uD83D\uDD35";
    static final Logger logger = Logger.getLogger(WebCrawlerService.class.getSimpleName());
    static final Gson G = new GsonBuilder().setPrettyPrinting().create();

    public List<TitleLinkPair> crawlWebPage(String url) {
        List<TitleLinkPair> titleLinkPairs = new ArrayList<>();
        logger.info(mm + " ... crawl web page: " + url);
        try {
            Document document = Jsoup.connect(url).get();
            Elements elements2 = document.select("a.elementor-button.elementor-button-link.elementor-size-lg[href]");

            for (Element element : elements2) {
                if (element.hasAttr("href")) {
                    String link = element.attr("href");
                    logger.info(mm + " link?  \uD83C\uDF4E " + link
                            + " \uD83C\uDF50\uD83C\uDF50  ");
                } else {
                    // The element does not have an href attribute
                    logger.info(mm + " The element does not have an href attribute ");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return titleLinkPairs;
    }


    public static class TitleLinkPair {
        private String title;
        private String link;

        public TitleLinkPair(String title, String link) {
            this.title = title;
            this.link = link;
        }

        public String getTitle() {
            return title;
        }

        public String getLink() {
            return link;
        }
    }
}
