package vexybot.aggregator.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URLEncoder;

public class WikipediaStrategy implements Strategy {
    private static final String URL_FORMAT = "https://ru.m.wikipedia.org/w/index.php?search=%s";

    @Override
    public String getInfo(String searchString) {
        String info = null;
        Document document;
        try {
            document = getDocument(searchString);
        } catch (IOException e) {
        }
        return info;
    }

    protected Document getDocument(String searchString) throws IOException {
        String url = String.format(URL_FORMAT, URLEncoder.encode(searchString, "UTF-8"));
        String referer = "wikipedia.ru";
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.85 Safari/537.36";
        Document document = Jsoup
                .connect(url)
                .userAgent(userAgent)
                .referrer(referer)
                .get();
        return document;
    }
}
