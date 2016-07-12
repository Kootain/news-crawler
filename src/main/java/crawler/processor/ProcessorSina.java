/**
 * 
 */
package crawler.processor;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;




import crawler.model.Tags;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
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
public class ProcessorSina implements Processor{
	
	private static String INIT_URL = "http://roll.news.sina.com.cn/interface/rollnews_ch_out_interface.php?col=89&offset_num=0&num=3000&page=1";
	
	public void processor(Page page){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String strDate  = sdf.format(new Date());
		String reg = "http://\\w+\\.sina\\.com\\.cn/(\\w+/)+"+strDate+"/doc-\\w+.shtml";
		if(page.getUrl().toString().equals(INIT_URL)){
			try {
				initProcessor(page);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(page.getUrl().regex(reg).match()){
			contentProcessor(page);
		}else{
			page.setSkip(true);
		}
	}
	
	public void init(Spider spider){
//		spider.addUrl(INIT_URL);
		spider.addRequest(new Request(INIT_URL).putExtra("_charset", "gb2312"));
	}
	
	public void initProcessor(Page page) throws ParseException{
		List<String> links = new JsonPathSelector("$.list[*].url").selectList(page.getRawText().substring(page.getRawText().indexOf("{")));
		List<String> tags = new JsonPathSelector("$.list[*].channel.title").selectList(page.getRawText().substring(page.getRawText().indexOf("{")));
		List<String> titles = new JsonPathSelector("$.list[*].title").selectList(page.getRawText().substring(page.getRawText().indexOf("{")));
		List<String> times = new JsonPathSelector("$.list[*].time").selectList(page.getRawText().substring(page.getRawText().indexOf("{")));
		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date today = new Date();
		String todayStr = format.format(today);
		today = format.parse(todayStr.substring(0, 10)+" 00:00:00");
		for(int i=0;i<links.size();i++){
			
			String d = format.format(Long.parseLong((String) times.get(i)+"000")); 
			Date newsTime = new Date();
			try {
				newsTime = format.parse(d);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			if(today.after(newsTime)) continue;
			page.addTargetRequest(new Request(links.get(i)).putExtra("title", titles.get(i)).putExtra("tag", tags.get(i)).putExtra("time", newsTime));
		}
	}
	
	
	private void contentProcessor(Page page){
		page.putField("title",page.getRequest().getExtra("title").toString());
		page.putField("subtitle","");
		page.putField("content",page.getHtml().smartContent());
		page.putField("link",page.getUrl().toString());
		
 
		page.putField("newsTime", (Date)page.getRequest().getExtra("time"));
		
		List<Tags> tags = new ArrayList<Tags>();	//tags存放所有标签
//		String[] tmptag = page.getHtml().css(".path").xpath("/text()").toString().trim().split(">");
		Tags tmpTags = new Tags(page.getRequest().getExtra("tag").toString(),0);		//父标签第二个参数0
		tags.add(tmpTags);	
//		tmpTags = new Tags("测试子tag",1);				//子标签第二个参数1
//		tags.add(tmpTags);
//		tmpTags = new Tags("测试关键字",-1);			//关键字类标签第二个参数 -1
//		tags.add(tmpTags);
		page.putField("tags", tags);
	}
}
