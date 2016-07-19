package crawler.processor;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import crawler.downloader.CharsetConfigDownloader;
import crawler.pipeline.MysqlPipeLine;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;


@Component
public class ProcessorCenter implements PageProcessor,ApplicationContextAware {

	static ApplicationContext ctx;
	
	private static String[] SOURCE_LIST = {	
											"Qq",
											"163",
											"Sina",
											"Sohu"
											};
	
	@Autowired
	private MysqlPipeLine mysqlPipeLine;
	
	private Spider spider;
	
	
    public Site site = Site.me()//.setHttpProxy(new HttpHost("127.0.0.1",8888))
            .setRetryTimes(3).setSleepTime(1000).setUseGzip(true);
    
    public void crawel(){
    	if(spider==null){
	    	spider = Spider.create(new ProcessorCenter())
	    						  .addPipeline(mysqlPipeLine)
	    						  .setDownloader(new CharsetConfigDownloader())
	    						  .thread(5);
    	}
    	

        for(String source:SOURCE_LIST){	//调用个网站processor初始化函数
			Processor processor = (Processor) ctx.getBean(source);
			processor.init(spider);
        }
        spider.run();
        spider.close();
    }

    public static void main(String[] args) {
    	ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:applicationContext*.xml");
    	final ProcessorCenter pc = ctx.getBean(ProcessorCenter.class);
    	pc.crawel();
    }
    
    @Override
    public void process(Page page) {
    	String source = getSourceFromPage(page);
    	page.putField("resource", source);
    	Processor processor = (Processor) ctx.getBean(source);
    	processor.processor(page);
    }

    @Override
    public Site getSite() {
        return site;
    }
    
    private String getSourceFromPage(Page page){
    	String type = page.getUrl().regex("http\\w?://\\S+\\.(\\S+)\\.com").toString();
    	if(type!=null && !"".equals(type)){
    		type = type.substring(0,1).toUpperCase() + type.toLowerCase().substring(1);
    	}
    	return type;
    }

	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		this.ctx = arg0;
	}
    
}
