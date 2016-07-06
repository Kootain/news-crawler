/**
 * 
 */
package crawler.model;

/**
 *************************
 * 
 ************************* 
 * @author kootain
 * @creation 2016年7月6日
 *
 */
public class Tags {
	private int tid;	//TagID
	
	private String tagName; //Tag名称
	
	private int parentTag;	//父TagID	0一级目录, 大于零二级目录, -1关键字
	
	private int catelogId;  //对应catelogID
	
	private String catelogName;	//对应catelog名称
	
	private int tagType;	//插入标记,0为一级目录,1为其它级目录,-1位关键字



	/**
	 * @return the tid
	 */
	public int getTid() {
		return tid;
	}

	/**
	 * @param tid the tid to set
	 */
	public void setTid(int tid) {
		this.tid = tid;
	}

	/**
	 * @return the tagName
	 */
	public String getTagName() {
		return tagName;
	}

	/**
	 * @param tagName the tagName to set
	 */
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	/**
	 * @return the parentTag
	 */
	public int getParentTag() {
		return parentTag;
	}

	/**
	 * @param parentTag the parentTag to set
	 */
	public void setParentTag(int parentTag) {
		this.parentTag = parentTag;
	}

	/**
	 * @return the catelogId
	 */
	public int getCatelogId() {
		return catelogId;
	}

	/**
	 * @param catelogId the catelogId to set
	 */
	public void setCatelogId(int catelogId) {
		this.catelogId = catelogId;
	}

	/**
	 * @return the catelogName
	 */
	public String getCatelogName() {
		return catelogName;
	}

	/**
	 * @param catelogName the catelogName to set
	 */
	public void setCatelogName(String catelogName) {
		this.catelogName = catelogName;
	}

	/**
	 * @return the tagType
	 */
	public int getTagType() {
		return tagType;
	}

	/**
	 * @param tagType the tagType to set
	 */
	public void setTagType(int tagType) {
		this.tagType = tagType;
	}
}
