package com.company;

import java.io.IOException;
import java.util.List;


public class Main {

    public static void main(String[] args) throws IOException{


        long start = System.currentTimeMillis();

        String link = "https://skillbox.ru/";
        String filePath = "";

        Parser parser = new Parser(link);
        List<String> links = parser.parse();
        int size = links.size();

        Writer writer = new Writer(filePath,parser.parse());
        writer.write();

        long finish = System.currentTimeMillis();
        long time = (finish - start) / 1000;
        System.out.println("Time: " + time + ". The file contains " + size + " links.");

    }
}
