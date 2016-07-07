/**
 * 
 */
package crawler.processor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;










import crawler.model.Tags;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.selector.JsonPathSelector;

public class Processor163 {
	private static String INIT_URL = "http://news.163.com/special/0001220O/news_json.js";
	public static void processor(Page page){
		if(page.getUrl().toString().equals(INIT_URL)){
			List<String> linkList = new JsonPathSelector("$.news[*]").selectList(page.getRawText().substring(page.getRawText().indexOf("{")));
			for(String links : linkList){
				page.addTargetRequests(new JsonPathSelector("$[*].l").selectList(links));
			}
		}
		else{

		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String strTime = sdf.format(new Date());
		strTime = strTime.substring(0,10);
		strTime = strTime + " 00:00:00";
		Date today = new Date();
		try {
			today = sdf.parse(strTime);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}            //今天零点的时间
		
		String newsTime = page.getUrl().toString();
		Date newsDateTime = new Date();
		if(newsTime!=null){
			Pattern timePattern = Pattern.compile("http://\\w+.163.com/(\\d+)/(\\d{2})(\\d{2})/(\\d+)/\\w+");
			Matcher m = timePattern.matcher(newsTime);
			if(m.find()){
				try {
					newsDateTime = sdf.parse("20"+m.group(1)+"-"+m.group(2)+"-"+m.group(3)+" "+m.group(4)+":00:00");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		if(page.getUrl().toString().contains(".html")&&newsDateTime.after(today)){
			String title = page.getHtml().xpath("//h1/text()").toString();
			List<String> subContent = page.getHtml().xpath("//div[@class='post_text']/p/text()").all();
			String content = "";
			content = StringUtils.join(subContent.toArray(),"<br/>");
			String pulishTime = page.getHtml().xpath("//div[@class='post_time_source']/text()").toString();
			pulishTime = pulishTime.trim().substring(0,19);
			SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date newsDatetime = new Date();
			try {
				newsDatetime = timeFormat.parse(pulishTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			List<Tags> tags = new ArrayList<Tags>();	//tags存放所有标签
			Tags tmpTags = new Tags("测试父tag",0);		//父标签第二个参数0
			tags.add(tmpTags);
			
			tmpTags = new Tags("测试子tag",1);				//子标签第二个参数1
			tags.add(tmpTags);
			
			tmpTags = new Tags("测试关键字",-1);			//关键字类标签第二个参数 -1
			tags.add(tmpTags);
			
			
			page.putField("title",title);
			page.putField("subtitle","");
			page.putField("content",content);
			page.putField("link",page.getUrl().toString());
			page.putField("newsTime", newsDatetime);
			page.putField("tags", tags);
		}
	}
}
	
	public static void init(Spider spider){
			spider.addUrl(INIT_URL);
	}
}
