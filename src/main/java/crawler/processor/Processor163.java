/**
 * 
 */
package crawler.processor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import us.codecraft.webmagic.Page;

public class Processor163 {
	public static void processor(Page page){
		String title = page.getHtml().xpath("//h1[@class='ep-h1']/text()").toString();
		List<String> subContent = page.getHtml().xpath("//div[@class='end-text']/p/text()").all();
		String content = "";
		content = StringUtils.join(subContent.toArray(),"<br/>");
		String newsTime = page.getHtml().xpath("//div[@class='ep-time-soure cDGray']/text()").toString();
		newsTime = newsTime.substring(1,20);
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date newsDatetime = new Date();
		try {
			newsDatetime = sdf.parse(newsTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		page.putField("title",title);
		page.putField("subtitle","");
		page.putField("content",content);
		page.putField("link",page.getUrl().toString());
		page.putField("newsTime", newsDatetime);
	}
}
