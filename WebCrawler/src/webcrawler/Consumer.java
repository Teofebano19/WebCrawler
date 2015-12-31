/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Teofebano
 */
public class Consumer {
    private static File path = new File("data");
    static Queue<String> queue = new LinkedList<String>();
    static LinkedHashSet<String> marked = new LinkedHashSet<String>();
    private static final int NUMBER_OF_LINKS = 1000;

    public static void downloadHTMLPage(String startURL, File path) {
        try {
            URL url = new URL(startURL);
            FileWriter fw = new FileWriter(path);
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection connection = null;
            if (urlConnection instanceof HttpURLConnection)
                    connection = (HttpURLConnection) urlConnection;
            connection.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String current;
            while ((current = in.readLine()) != null) {
                    fw.write(current);
            }
        } catch (Exception ex) {
                System.out.println(ex.getMessage());
        }
    }

    public static void deleteFiles(File file_to_delete) {
        try {
            if (file_to_delete.isDirectory()) {
                for (File f : file_to_delete.listFiles()) {
                    if (f.isDirectory()) {
                            deleteFiles(f);
                    }
                    f.delete();
                }
            }
        } catch (Exception e) {
                e.printStackTrace(System.err);
        }
    }

    public void start(){
            try {
                if (path.exists()) {
                        deleteFiles(path);
                } else {
                        path.mkdir();
                }
            } catch (Exception e) {
                    System.out.println(e.getMessage());
            }
            // initial web page
            String s = "http://web.mit.edu/";

            // list of web pages to be examined
            queue.add(s);

            // set of examined web pages
            marked.add(s);
            Document doc;
            System.out.println("List of url");
            // breadth first search crawl of web
            OUTER: while (!queue.isEmpty()) {
                String v = queue.remove();
                System.out.println(v);

                if (marked.size() < NUMBER_OF_LINKS) {
                    try {
                        doc = Jsoup.connect(v).get();
                        Elements questions = doc.select("a[href]");
                        for (Element link : questions) {
                            if ((link.attr("abs:href").contains("mit.edu") && (link.attr("abs:href").startsWith("http")))) {
                                if (marked.size() == NUMBER_OF_LINKS)
                                    continue OUTER;
                                else {
                                    queue.add(link.attr("abs:href"));
                                    marked.add(link.attr("abs:href"));
                                }
                            }
                        }
                    } catch (IOException e) {
                            e.printStackTrace();
                    }
                }
            }
            System.out.println("Total Links downloaded :: " + marked.size());

            int counter = 1;
            int listSize = marked.size();
            for (String fileName : marked) {
                System.out.println("Downloading .... file " + counter + "/"
                                + listSize + ".." + fileName);
                downloadHTMLPage(fileName, new File(path + "\\code" + counter
                                + ".html"));
                counter++;
            }
            System.out.println("Finished downloads");
    }
}
