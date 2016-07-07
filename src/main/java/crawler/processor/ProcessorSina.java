/**
 * 
 */
package crawler.processor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.crypto.Data;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

import crawler.model.News;
import crawler.model.Tags;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.selector.JsonPathSelector;

/**
 *************************
 * 
 ************************* 
 * @author kootain
 * @creation 2016年7月5日
 *
 */
public class ProcessorSina {
	
	private static String INIT_URL = "http://roll.news.sina.com.cn/interface/rollnews_ch_out_interface.php?col=89&offset_num=0&num=3000&page=1";
	
	public static void processor(Page page){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String strDate  = sdf.format(new Date());
		String reg = "http://\\w+\\.sina\\.com\\.cn/(\\w+/)+"+strDate+"/doc-\\w+.shtml";
		
		if(page.getUrl().toString().equals(INIT_URL)){
			initProcessor(page);
		}else if(page.getUrl().regex(reg).match()){
			contentProcessor(page);
		}else{
			page.setSkip(true);
		}
	}
	
	public static void init(Spider spider){
		spider.addUrl(INIT_URL);
	}
	
	public static void initProcessor(Page page){
		List<String> links = new JsonPathSelector("$.list[*].url").selectList(page.getRawText().substring(page.getRawText().indexOf("{")));
		page.addTargetRequests(links);
	}
	
	
	private static void contentProcessor(Page page){
		page.putField("title",page.getHtml().xpath("//h1/text()").toString());
		page.putField("subtitle","");
		List<String> tmp = page.getHtml().css(".page-content .article").xpath("//p/text()").all();
		String content = StringUtils.join(tmp,"<br/>");
		page.putField("content",content);
		if(content.trim().equals("")){
			page.setSkip(true);
		}
		page.putField("link",page.getUrl().toString());
		String timeString = page.getHtml().css(".time-source").xpath("//span/text()").toString();
		Date newsTime = new Date();
		if(timeString!=null){
			Pattern timePattern = Pattern.compile("(\\d+)年(\\d+)月(\\d+)日(\\d+):(\\d+)");
			Matcher m = timePattern.matcher(timeString);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(m.find()){
				try {
					newsTime = sdf.parse(m.group(1)+"-"+m.group(2)+"-"+m.group(3)+" "+m.group(4)+":"+m.group(5)+":00");
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		page.putField("newsTime", newsTime);
		
		List<Tags> tags = new ArrayList<Tags>();	//tags存放所有标签
//		String[] tmptag = page.getHtml().css(".path").xpath("/text()").toString().trim().split(">");
		Tags tmpTags = new Tags("测试父tag",0);		//父标签第二个参数0
		tags.add(tmpTags);
		
		tmpTags = new Tags("测试子tag",1);				//子标签第二个参数1
		tags.add(tmpTags);
		
		tmpTags = new Tags("测试关键字",-1);			//关键字类标签第二个参数 -1
		tags.add(tmpTags);
		page.putField("tags", tags);
	}
}
