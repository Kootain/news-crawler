/**
 * 
 */
package crawler.processor;

import java.util.Date;
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
//   	 page.putField("name", page.getHtml().css("h1","text").toString());
//        page.putField("description", page.getHtml().xpath("//div[@class='lemma-summary']/div[@class='para']/allText()"));
	//mock data
		page.putField("title","测试title");
		page.putField("subtitle","测试subtitle");
		page.putField("content","测试content");
		page.putField("resource","测试resource");
		page.putField("link",page.getUrl().toString());
		page.putField("newsTime", new Date());
	}
}
