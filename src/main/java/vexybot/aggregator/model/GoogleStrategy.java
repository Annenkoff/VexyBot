package vexybot.aggregator.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;

public class GoogleStrategy implements Strategy {
    private static final String URL_FORMAT = "https://www.google.com/search?q=%s";

    @Override
    public String getInfo(String searchString) throws IOException {
        String info = "";
        Document document = getDocument(searchString);
        Elements elementsByAttributeValue;
        try {
            elementsByAttributeValue = document.getElementsByClass("_cgc").first().getElementsByTag("span");
            if (!elementsByAttributeValue.isEmpty() || elementsByAttributeValue.size() != 0) {
                Elements site = document.getElementsByClass("_cgc").first().getElementsByTag("a");
                info += elementsByAttributeValue.get(0).text();
                info += "\n" + site.get(0).attr("href");
                return info;
            }
        } catch (Exception e) {
        }
        try {
            elementsByAttributeValue = document.getElementsByAttributeValue("data-dobid", "wd-dfn");
            if (!elementsByAttributeValue.isEmpty() || elementsByAttributeValue.size() != 0) {
                info += elementsByAttributeValue.get(0).text();
                Elements elementsByClass = document.getElementsByAttributeValue("data-dobid", "wd-src");
                info += "\n" + elementsByClass.get(0).attr("href");
                return info;
            }
        } catch (Exception e) {
        }
        try {
            elementsByAttributeValue = document.getElementsByClass("_oDd").first().getElementsByTag("span");
            if (!elementsByAttributeValue.isEmpty() || elementsByAttributeValue.size() != 0) {
                Elements site = document
                        .getElementsByClass("rc")
                        .first().getElementsByClass("r").first().getElementsByTag("a");
                info += elementsByAttributeValue.get(0).text();
                info += "\n" + site.get(0).attr("href");
                return info;
            }
        } catch (Exception e) {
        }
        try {
            elementsByAttributeValue = document.getElementsByAttributeValue("data-dobid", "dfn");
            if (!elementsByAttributeValue.isEmpty() || elementsByAttributeValue.size() != 0) {
                int i = 1;
                for (Element element : elementsByAttributeValue) {
                    info += i + ". " + element.text() + "\n";
                    i++;
                }
                return info;
            }
        } catch (Exception e) {
        }
        return "Не знаю.";
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
