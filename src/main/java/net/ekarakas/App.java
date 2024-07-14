package net.ekarakas;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class App {
    final static String URL = "https://windows10spotlight.com";
    static String PAGE = "1";
    static Boolean PRINT = true;
    static Boolean DOWNLOAD = true;
    static Boolean DB = true;
    static String OUTPUT = "";

    public static void main(String[] args) {
        if (args.length == 5) {
            int intValue = Integer.parseInt(args[0]);
            boolean boolValue1 = Boolean.parseBoolean(args[1]);
            boolean boolValue2 = Boolean.parseBoolean(args[2]);
            boolean boolValue3 = Boolean.parseBoolean(args[3]);
            String stringValue = args[4];

            PAGE = String.valueOf(intValue);
            PRINT = boolValue1;
            DOWNLOAD = boolValue2;
            DB = boolValue3;
            OUTPUT = stringValue;

            start();
        } else {
            System.out.println("args1 : pagenumber (int)\nargs2 : print (bool)\nargs3 : download (bool)\nargs4 : db (bool)\nargs5 : output (str)");
            return;
        }
    }

    public static void start() {
        int maxPageNumber = maxPageNumber();

        if (PRINT)
            System.out.println("Page Number : " + PAGE);

        List<String> imageUrlList = header(PAGE);

        for (String url : imageUrlList) {
            Spotlight image = detail(url);

            if (DB) {
                Db db = new Db(image);
            }

            if (PRINT)
                System.out.println("================================================================================");
        }
    }

    public static List<String> header(String pageNumber) {
        String url = URL + "/page/" + pageNumber;
        List<String> imageUrlList = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).get();

            // detail image urls
            Elements h2Elements = doc.select("h2 a");
            for (Element h2Element : h2Elements) {
                String href = h2Element.attr("href");
                imageUrlList.add(href);

                if (PRINT) {
                    System.out.println("Image Url: " + href);
                }
            }

            if (PRINT)
                System.out.println("================================================================================");

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return imageUrlList;
    }

    public static Spotlight detail(String url) {
        Spotlight image = null;
        try {
            Document doc = Jsoup.connect(url).get();

            image = new Spotlight();

            // Title
            String title = doc.select("h1").text();
            if (PRINT)
                System.out.println("Title: " + title);

            image.setTitle(title);

            // Date
            Elements dateElements = doc.select("aside .date");
            Element dateElement = dateElements.get(0);
            String dateText = dateElement.text();

            if (PRINT)
                System.out.println("Date: " + dateText);

            image.setDate(dateText);

            // img src and id
            Elements fancyboxElements = doc.select("div.entry a");

            Element landscapeImage = fancyboxElements.get(0);
            Element portraitImage = fancyboxElements.get(1);

            String landscapeSource = landscapeImage.attr("href");
            String portraitSource = portraitImage.attr("href");

            String landscapeId = Util.getId(landscapeSource);
            String portraitId = Util.getId(portraitSource);

            image.setLandscapeId(landscapeId);
            image.setPortraitId(portraitId);

            image.setLandscapeSource(landscapeSource);
            image.setPortraitSource(portraitSource);

            if (PRINT) {
                System.out.println("landscape id: " + landscapeId);
                System.out.println("landscape img src: " + landscapeSource);

                System.out.println("portrait id: " + portraitId);
                System.out.println("portrait img src: " + portraitSource);
            }

            if (DOWNLOAD) {
                Util.downloadImage(landscapeSource, OUTPUT + "/Landscape/" + landscapeId + ".jpg");
                Util.downloadImage(portraitSource, OUTPUT + "/Portrait/" + portraitId + ".jpg");
            }

            image.setTags(tagList(url));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return image;
    }

    public static List<String> tagList(String detailUrl) {
        List<String> tagList = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(detailUrl).get();

            // rel='tag'
            Elements tagElements = doc.select("a[rel=tag]");
            for (Element tagElement : tagElements) {
                String tagText = tagElement.text();
                tagList.add(tagText);
            }

            if (PRINT) {
                System.out.println(tagList);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return tagList;
    }

    public static int maxPageNumber() {
        int maxPageNumber = Integer.MIN_VALUE;
        try {
            Document doc = Jsoup.connect(URL).get();

            Elements pageNumberElements = doc.select(".page-numbers");
            for (Element pageNumberElement : pageNumberElements) {
                try {
                    int pageNumber = Integer.parseInt(pageNumberElement.html().replace(",", ""));
                    if (pageNumber > maxPageNumber) {
                        maxPageNumber = pageNumber;
                    }
                } catch (NumberFormatException e) {
                    // System.err.println("Ignore non-numeric page numbers");
                    // System.err.println(e.getMessage());
                }
            }

            if (PRINT) {
                System.out.println("Max page number: " + maxPageNumber);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return maxPageNumber;
    }
}
