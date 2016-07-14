package crawler.processor;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;

import crawler.model.Tags;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.selector.JsonPathSelector;

public class ProcessorSohu implements Processor{
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private static String INIT_URL = "http://news.sohu.com/_scroll_newslist/" + sdf.format(new Date()).substring(0,10).replace("-","") + "/news.inc";
	
	
	public void processor(Page page){
		if(page.getUrl().toString().equals(INIT_URL))
			initProcessor(page);
		else
			contentProcessor(page);
	}
	
	public void init(Spider spider){
		spider.addRequest(new Request(INIT_URL).putExtra("_charset", "ISO-8859-1"));
	}
	
	private void initProcessor(Page page) {
		String htmlCode = new String();
		try {
			htmlCode = new String((page.getRawText().substring(page.getRawText().indexOf("{"))).getBytes("ISO-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		List<String> tags = new JsonPathSelector("$.category[*]").selectList(htmlCode);
		Map<Integer,String> map = new HashMap<Integer, String>();
		for(int i = 0;i < tags.size();i++){
			map.put(i,new JsonPathSelector("$.[*]").selectList(tags.get(i)).get(0));
		}
		List<String> linkList = new JsonPathSelector("$.item[*]").selectList(htmlCode);
		for(String link : linkList){
			String url = new JsonPathSelector("$.[*]").selectList(link).get(2);
			url = StringEscapeUtils.unescapeJava(url);
			String title = new JsonPathSelector("$.[*]").selectList(link).get(1);
			String time = new JsonPathSelector("$.[*]").selectList(link).get(3);
			String tagStr = new JsonPathSelector("$.[*]").selectList(link).get(0);
			int tagNum = Integer.parseInt(tagStr);
			String tag = map.get(tagNum);
			page.addTargetRequest(new Request(url).putExtra("title", title).putExtra("time", strToDate(time)).putExtra("tag", tag));
		}
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
	
	private Date strToDate(String str){
		String strTime = sdf.format(new Date());
		strTime = strTime.substring(0,4) + "-" + str.substring(0,2) + "-" + str.substring(3,5) + str.substring(5,11) + ":00" ;
		Date newsTime = new Date();
		try {
			newsTime = sdf.parse(strTime);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return newsTime;
	}
}
