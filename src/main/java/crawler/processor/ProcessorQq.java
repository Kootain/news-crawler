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

import crawler.model.News;
import crawler.model.Tags;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.selector.Selectable;

/**
 *************************
 * 
 ************************* 
 * @author kootain
 * @creation 2016年7月8日
 *
 */
@Component("Qq")
public class ProcessorQq extends Processor {
	
	private static String INIT_URL_TPL = "http://roll.news.qq.com/interface/roll.php?date=%s&page=%d&mode=1";
	
	private static int INIT_NUM = 10;
	
	private int countLoad;
	
	private int countUnfinish;
	
	public void processor(Page page) {
		if(page.getUrl().regex("http://roll.news.qq.com/interface/roll.php\\S+").match()){
			listProcessor(page);
		}else{
			contentProcessor(page);
		}
	}

	@Override
	public void init(Spider spider) {
		countLoad=0;
		countUnfinish=INIT_NUM;
		for(int i=1;i<=INIT_NUM;i++){
			spider.addRequest(new Request(String.format(INIT_URL_TPL, getTodayTimeString(), i))
								.putExtra("_referer", "http://roll.news.qq.com/")
								.putExtra("_charset", "gb2312")
								.setPriority(1));
		}
	}
	
	public void listProcessor(Page page){
		int count = 0;
		int np = 0;
		try{
			count = Integer.parseInt(page.getJson().jsonPath("$data.count").toString());
			np = Integer.parseInt(page.getJson().jsonPath("$data.page").toString());
		}catch (Exception e){
			page.setSkip(true);
			countUnfinish--;
			return;
		}
		if(count>np&&np>INIT_NUM){	//载入下一页list页
			countUnfinish++;
			page.addTargetRequest(new Request(String.format(INIT_URL_TPL, getTodayTimeString(), (np+1)))
							.putExtra("_referer", "http://roll.news.qq.com/")
							.putExtra("_charset", "gb2312")
							.setPriority(1));
		}
		String jsonString = page.getJson().jsonPath("$data.article_info").toString();
		Page tmp = new Page();
		tmp.setRawText(jsonString).setRequest(new Request(page.getUrl().toString()));
		List<String> times = tmp.getHtml().$("li .t-time","text").all();
		List<String> tags = tmp.getHtml().$("li .t-tit","text").all();
		List<String> links = tmp.getHtml().$("li").links().all();
		List<String> titles = tmp.getHtml().$("li a","text").all();
		if(times.size()==titles.size()&&titles.size()==links.size()){
			countLoad += links.size();
			for(int i=0; i<titles.size();i++){
				page.addTargetRequest(new Request(links.get(i))
										.putExtra("title", titles.get(i))
										.putExtra("time", getTodayTimeString().substring(0, 5)+times.get(i))
										.putExtra("tag", tags.get(i)));
			}
		}else{
			//TODO : List页解析错误
		}
		synchronized(this){
	    	countUnfinish--;
	    	if(countUnfinish==0){
	    		logger.debug(String.format("【%s】%d条记录加入任务队列",getSourceFromPage(page),countLoad));
	    	}
		}
		page.setSkip(true);
	}
	
	private void contentProcessor(Page page){
		page.putField("title",page.getRequest().getExtra("title").toString());
		page.putField("subtitle","");
		page.putField("content",page.getHtml().smartContent());
		page.putField("link",page.getUrl().toString());
		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd H:mm");
		Date newsTime = new Date();
		try {
			newsTime = format.parse(page.getRequest().getExtra("time").toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		page.putField("newsTime", newsTime);
		List<Tags> tags = new ArrayList<Tags>();
		Tags tmpTags = new Tags(page.getRequest().getExtra("tag").toString()
												.replace("[", "").replace("]", "").trim(),0);
		tags.add(tmpTags);
		page.putField("tags", tags);
	}
	
	
	/**
	 * @return Today date in yyyy-MM-dd
	 */
	private String getTodayTimeString(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String timeStr = sdf.format(new Date());
		return timeStr;
	}
	

}
