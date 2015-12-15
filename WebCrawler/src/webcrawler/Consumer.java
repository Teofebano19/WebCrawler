/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;

import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author user
 */
public class Consumer {
    private String url = "http://www.jsoup.org/";
    private int depth = 1;
    public void Start(){
        Crawl();
    }
    
    private void Crawl(){
        
        String url = this.getURL();
        HashSet<String> anchors = new HashSet<String>();
        try {
            Document doc;
            doc = Jsoup.connect(url).get();
            Elements links = doc.select("a");
            for (Element link : links){
                String anchor = link.attr("href").trim();
                anchors.add(anchor);
                System.out.println(anchor);
            }
        } catch (IOException ex) {
            Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("-------");
        for (String s:anchors){
            System.out.println(s);
        }
    }
    
    private String getURL(){
        return url;
    }
}
