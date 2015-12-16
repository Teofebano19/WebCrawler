/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
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
 * @author user
 */
public class Consumer {
    private String url = "http://jsoup.org";
    private int depth = 2;
    PriorityQueue<String> anchors = new PriorityQueue<String>();
    
    public void Start(){
        anchors.add(url);
        Crawl();
    }
    
    private void Crawl(){
        String url = this.getURL();
        try {
            Document doc;
            Vector<Pair<Elements,String>> links = new Vector<Pair<Elements,String>>();
            for (int i=0;i<depth;i++){
                String domain = "";
                for (int j=0;j<anchors.size();i++){
                    System.out.println(anchors.element());
                    doc = Jsoup.connect(anchors.element()).get();
                    Elements E = doc.select("a");
//                    System.out.println(E);
                    domain = anchors.element();
                    Pair pair = new Pair(E,domain);
                    links.add(pair);
                    
                    anchors.remove();
                }
                System.out.println("--------------------");
//                System.out.println(links.size());
                for (int j=0;j<links.size();j++){
                    Elements ListE = links.elementAt(j).getKey();
                    String domainE = links.elementAt(j).getValue();
                    for (Element link:ListE){
                        String anchor = link.attr("href").trim();
                        if (anchor.startsWith("/")){
                            anchor = domainE + anchor;
                        }
                        anchors.add(anchor);
                        System.out.println(anchor);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private String getURL(){
        return url;
    }
}
