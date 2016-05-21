package vexybot.aggregator.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;

public class GoogleStrategy implements Strategy {
    private static final String URL_FORMAT = "https://www.google.ru/search?q=%s";

    @Override
    public String getInfo(String searchString) {
        String info = "";
        Document document;
        try {
            document = getDocument(searchString);
            Elements elementsByAttributeValue;
            if (searchString.toLowerCase().contains("что такое ")) {
                elementsByAttributeValue = document.getElementsByAttributeValue("data-dobid", "dfn");
                if (!elementsByAttributeValue.isEmpty()) {
                    int i = 1;
                    for (Element element : elementsByAttributeValue) {
                        info += i + ". " + element.text() + "\n";
                        i++;
                    }
                } else
                    info = "Не знаю.";
            } else if (searchString.toLowerCase().contains("кто такой ")
                    || searchString.toLowerCase().contains("кто такая ")
                    || searchString.toLowerCase().contains("кто такие ")) {
                elementsByAttributeValue = document.getElementsByAttributeValue("data-dobid", "wd-dfn");
                if (!elementsByAttributeValue.isEmpty() || elementsByAttributeValue.size() != 0) {
                    info = elementsByAttributeValue.get(0).text();
                    Elements elementsByClass = document.getElementsByAttributeValue("data-dobid", "wd-src");
                    info += "\n" + elementsByClass.get(0).attr("href");
                } else
                    info = "Не знаю.";
            }
        } catch (IOException e) {
        }
        return info;
    }

    protected Document getDocument(String searchString) throws IOException {
        String url = String.format(URL_FORMAT, URLEncoder.encode(searchString, "UTF-8"));
        String referer = "google.ru";
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.85 Safari/537.36";
        Document document = Jsoup
                .connect(url)
                .userAgent(userAgent)
                .referrer(referer)
                .get();
        return document;
    }
}
