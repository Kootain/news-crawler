/**
 * 
 */
package crawler.model;

import java.util.Date;

/**
 *************************
 * 
 ************************* 
 * @author kootain
 * @creation 2016年6月30日
 *
 */
public class News {
	
	private int id;			//新闻ID
	
	private String title;	//新闻标题
	
	private String subtitle;	//新闻子标题
	
	private String content;		//新闻正文
	
	private String resource;	//新闻源
	
	private String link;		//新闻链接
	
	private Date newsTime;		//新闻发布时间
	
	private Date createTime;	//记录创建时间
	
	private boolean ishidden;	//是否展示标志位

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the subtitle
	 */
	public String getSubtitle() {
		return subtitle;
	}

	/**
	 * @param subtitle the subtitle to set
	 */
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the resource
	 */
	public String getResource() {
		return resource;
	}

	/**
	 * @param resource the resource to set
	 */
	public void setResource(String resource) {
		this.resource = resource;
	}

	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @param link the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * @return the newsTime
	 */
	public Date getNewsTime() {
		return newsTime;
	}

	/**
	 * @param newsTime the newsTime to set
	 */
	public void setNewsTime(Date newsTime) {
		this.newsTime = newsTime;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the ishidden
	 */
	public boolean isIshidden() {
		return ishidden;
	}

	/**
	 * @param ishidden the ishidden to set
	 */
	public void setIshidden(boolean ishidden) {
		this.ishidden = ishidden;
	}
	
}
