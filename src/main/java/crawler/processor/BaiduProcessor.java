/**
 * 
 */
package crawler.processor;

import us.codecraft.webmagic.Page;

/**
 *************************
 * 
 ************************* 
 * @author kootain
 * @creation 2016��6��13��
 *
 */
public class BaiduProcessor {
	public static void processor(Page page){
   	 	page.putField("name", page.getHtml().css("h1","text").toString());
        page.putField("description", page.getHtml().xpath("//div[@class='lemma-summary']/div[@class='para']/allText()"));
	}
}
