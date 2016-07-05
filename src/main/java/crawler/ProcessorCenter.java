package crawler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


import crawler.model.News;
import crawler.pipeline.MysqlPipeLine;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;


@Component
public class ProcessorCenter implements PageProcessor {
	@Qualifier("JobInfoDaoPipeline")
	private static String CLASS_BASE = "crawler.processor.";
	
	private static String METHOD_NAME = "processor";
	
    private Site site = Site.me()//.setHttpProxy(new HttpHost("127.0.0.1",8888))
            .setRetryTimes(3).setSleepTime(1000).setUseGzip(true);
    
    private static void crawel(){
    	Spider spider = Spider.create(new ProcessorCenter())
    						  .addPipeline(new MysqlPipeLine())
    						  .thread(2);
        String urlTemplate = "http://baike.baidu.com/search/word?word=%s&pic=1&sug=1&enc=utf8";
        ResultItems resultItems = spider.<ResultItems>get(String.format(urlTemplate, "水力发电"));
        resultItems=spider.<ResultItems>get("http://news.sina.com.cn/c/2016-07-04/doc-ifxtscen3329067.shtml");
        System.out.println(resultItems);
        spider.close();
    }

    public static void main(String[] args) {
    	ProcessorCenter.crawel();
    }
    
    @Override
    public void process(Page page) {
    	String webtype = processorName(page);
    	try {
    		Class<?> clazz = Class.forName(webtype);
    		Class<?>[] argsType = new Class[1];
    		Object[] args = new Object[1];
    		page.putField("source", webtype);
    		argsType[0] = page.getClass();
    		args[0] = page;
    		Method method = clazz.getMethod(METHOD_NAME, argsType);
    		method.invoke(null, args);
    	} catch (ClassNotFoundException e) {
    		e.printStackTrace();
    	} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
    	News news = pageTONews(page);
    	page.putField("itemObject", news);
    	
       System.out.println();
    }

    @Override
    public Site getSite() {
        return site;
    }
    
    /**
     * Return the processor class name with package name,
     * for reflect the processor class
     * @param page
     * @return
     */
    private static String processorName(Page page){
    	String type = page.getUrl().regex("http\\w?://\\S+\\.(\\S+)\\.com").toString();
    	if(type!=null && !"".equals(type)){
    		type = type.substring(0,1).toUpperCase() + type.toLowerCase().substring(1);
    	}
    	return CLASS_BASE+type+"Processor";
    }
    
    private static News pageTONews(Page page){
    	News news = new News();
    	news.setTitle(page.getResultItems().get("title"));
    	news.setSubtitle(page.getResultItems().get("subtitle"));
    	news.setContent(page.getResultItems().get("content"));
    	news.setResource(page.getResultItems().get("resource"));
    	news.setLink(page.getResultItems().get("link"));
    	news.setNewsTime(page.getResultItems().get("newsTime"));
    	news.setCreateTime(new Date());
    	news.setIshidden(false);
    	return news;
    }
}
