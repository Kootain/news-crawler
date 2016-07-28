/**
 * 
 */
package crawler.processor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Component;

import crawler.model.Tags;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.selector.JsonPathSelector;

@Component("163")
public class Processor163 extends Processor{
	private static String INIT_URL = "http://news.163.com/special/0001220O/news_json.js";
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");	
	
	public void processor(Page page){
		if(page.getUrl().toString().equals(INIT_URL))
			initProcessor(page);
		else
			contentProcessor(page);
	}
	
	public void init(Spider spider){
		spider.addRequest(new Request(INIT_URL).putExtra("_charset", "gb2312").setPriority(1));
	}
	
	private void initProcessor(Page page) {
		List<String> linkList = new JsonPathSelector("$.news[*]").selectList(page.getRawText().substring(page.getRawText().indexOf("{")));
		List<String> tags = new JsonPathSelector("$.category[*].n").selectList(page.getRawText().substring(page.getRawText().indexOf("{")));
		int count=0;
		for(int i = 0;i< linkList.size();i++){
			List<String> link = new JsonPathSelector("$[*].l").selectList(linkList.get(i));
			List<String> title = new JsonPathSelector("$[*].t").selectList(linkList.get(i));
			List<String> time = new JsonPathSelector("$[*].p").selectList(linkList.get(i));
			String tag = tags.get(i);
			count+=link.size();
			for(int j = 0;j< link.size();j++){
				if(strToDate(time.get(j)).after(today()))
					page.addTargetRequest(new Request(link.get(j)).putExtra("title", title.get(j)).putExtra("time", strToDate(time.get(j))).putExtra("tag", tag));
			}
		}
		logger.debug(String.format("【%s】%d条记录加入任务队列",getSourceFromPage(page),count));
		page.setSkip(true);
	}
	
	private static Date today(){
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
		return today;
	}
	
	private static Date strToDate(String str){
		Date newsTime = new Date();
		try {
			newsTime = sdf.parse(str);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return newsTime;
	}
	
	private void contentProcessor(Page page) {				
		page.putField("title",page.getRequest().getExtra("title").toString());
		page.putField("subtitle","");
		page.putField("content",page.getHtml().smartContent());
		page.putField("link",page.getUrl().toString());
		page.putField("newsTime", page.getRequest().getExtra("time"));
		List<Tags> tags = new ArrayList<Tags>();	//tags存放所有标签
		Tags tmpTags = new Tags(page.getRequest().getExtra("tag").toString(),0);		//父标签第二个参数0
		tags.add(tmpTags);
		page.putField("tags", tags);
		
	}
}
