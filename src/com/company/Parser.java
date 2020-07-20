package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;


public class Parser {


    private String mainURL;
    private volatile HashSet<String> uniqueLinks = new HashSet<>();
    private List<String> links = new ArrayList<>();

    public Parser(String mainURL) {
        this.mainURL = mainURL;
    }


    public List<String> parse() {
        new ForkJoinPool().invoke(new ForkParser(mainURL));
        return links;
    }


    private class ForkParser extends RecursiveAction {

        private String currentUrl;


        public ForkParser(String currentUrl) {
            this.currentUrl = currentUrl;

        }

        @Override
        protected void compute() {
            if (!uniqueLinks.contains(currentUrl)) {
                uniqueLinks.add(currentUrl);
                links.add(getStringWithTab(currentUrl, mainURL));
                Document document = null;
                try {
                    Thread.sleep(1000);
                    document = Jsoup.connect(currentUrl).timeout(0).get();
                } catch (IOException | InterruptedException e) {
                    System.out.println(e);
                }
                if (document != null) {
                    parse(document, currentUrl);
                }
            }
        }

        protected void parse(Document document, String currentUrl) {
            ArrayList<ForkParser> tasks = new ArrayList<>();
            if (document != null) {
                Elements aElements = document.select("a");
                aElements.stream()
                        .map(link -> link.attr("abs:href"))
                        .filter(thisLink -> thisLink.contains(currentUrl)
                                && !thisLink.equals(currentUrl)
                                && !thisLink.equals(mainURL)
                                && !uniqueLinks.contains(thisLink)
                                && !thisLink.contains("pdf")
                                && !thisLink.contains("xlsx")
                                && !thisLink.contains("xml"))
                        .forEach(thisLink3 -> {
                            ForkParser task = new ForkParser(thisLink3);
                            task.fork();
                            tasks.add(task);
                        });
                tasks.stream().forEach(ForkParser::join);
            }
        }

        private String getStringWithTab(String thisLink, String mainUrl) {
            int index = mainUrl.length();
            String fragment = thisLink.substring((index - 1), (thisLink.length() - 1));
            String quantity = fragment.replaceAll("[^/]", "");
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < quantity.length(); i++) {
                buffer.append("\t");
            }
            buffer.append(thisLink);
            return buffer.toString();
        }
    }
}




