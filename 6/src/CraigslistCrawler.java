import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CraigslistCrawler {

    public static void main(String[] args){
        String Craigslist_URL = "https://sfbay.craigslist.org/d/apts-housing-for-rent/search/apa";

        crawl(Craigslist_URL);
    }

    public static void crawl(String url){
        String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.95 Safari/537.36";
        ObjectMapper mapper = new ObjectMapper();

        try {
            File file = new File("result.txt");
            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            HashMap<String, String> headers = new HashMap<>();
            headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            //headers.put("Accept-Encoding", "gzip, deflate");
            headers.put("Accept-Language", "en-US,en;q=0.8");
            Document doc = Jsoup.connect(url).headers(headers).userAgent(USER_AGENT).timeout(100000).get();

            //Elements results = doc.select("li[result-row]");
            Elements results = doc.getElementsByClass("result-row");
            if (results.size() == 0) {
                System.out.println("result:" + results.size());
                return;
            }
            System.out.println("result:" + results.size());

            for(int i = 1; i <= results.size() ;i++) {
                Ad ad = new Ad();
                //#sortable-results > ul > li:nth-child(1) > p > a
                String title_ele_path = "#sortable-results > ul > li:nth-child(" + i + ") > p > a";
                Element title_ele = doc.select(title_ele_path).first();
                if (title_ele != null) {
                    //System.out.println("title = " + title_ele.text());
                    ad.title = title_ele.text();
                }

                //#sortable-results > ul > li:nth-child(2) > p > span.result-meta > span.result-price
                String price_path = "#sortable-results > ul > li:nth-child(" + i + ") > p > span.result-meta > span.result-price";
                Element price_ele = doc.select(price_path).first();
                if (price_ele != null) {
                    String price_whole = price_ele.text();
                    //System.out.println("price = " + price_whole_ele.text());
                    try {
                        ad.price = price_whole;
                    } catch (NumberFormatException ne) {
                        ne.printStackTrace();
                    }
                }

                String detail_path = "#sortable-results > ul > li:nth-child(" + i + ") > p > a";
                Element detail_url_ele = doc.select(detail_path).first();
                if (detail_url_ele != null) {
                    String detail_url = detail_url_ele.attr("href");
                    //System.out.println("url = " + detail_url);
                    ad.url = detail_url;
                }

                //#sortable-results > ul > li:nth-child(1) > p > span.result-meta > span.result-hood
                String hood_path = "#sortable-results > ul > li:nth-child(" + i + ") > p > span.result-meta > span.result-hood";
                Element hood_ele = doc.select(hood_path).first();
                if (hood_ele != null) {
                    String hood = hood_ele.text().replaceAll("[()]","");
                    //System.out.println("hood = " + hood);
                    ad.hood = hood;
                }
                String jsonInString = mapper.writeValueAsString(ad);
                System.out.println(jsonInString);
                bw.write(jsonInString);
                bw.newLine();
            }
            bw.close();
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
