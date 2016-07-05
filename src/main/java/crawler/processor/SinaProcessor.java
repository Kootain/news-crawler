/**
 * 
 */
package crawler.processor;

import java.util.Date;
import java.util.List;


import org.apache.commons.lang3.StringUtils;

import us.codecraft.webmagic.Page;

/**
 *************************
 * 
 ************************* 
 * @author kootain
 * @creation 2016年7月5日
 *
 */
public class SinaProcessor {
	public static void processor(Page page){
			page.putField("title",page.getHtml().xpath("//h1/text()").toString());
			page.putField("subtitle","测试subtitle");
			List<String> tmp = page.getHtml().css(".page-content .article").xpath("//p/text()").all();
			String content = StringUtils.join(tmp,"<br/>");
			page.putField("content",content);
			page.putField("link",page.getUrl().toString());
			page.putField("newsTime", new Date());
	}
}
