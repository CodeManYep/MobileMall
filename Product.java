package cn.snnu.mm.bean;

import java.util.Date;

/**
 * 商品表
 * 
 * @author zhangwz
 * 2018年4月10日 下午10:46:39
 */
public class Product {

	private String pid;//商品id
	private String pname;//商品名称
	private double market_price;//市场价
	private double shop_price;//商城价
	private String pimage;//商品图片路径
	private Date pdate;//上架日期
	private int is_hot;//是否热卖
	private String pdesc;//商品描述
	private int pflag;//商品标志-0：未下架，1：已下架
	private String pbrand;//商品品牌
	private String cid;//商品分类id
	
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	private String bid;//商品品牌id
	
	/**
	 * 分类:以面向对象的方式描述商品与分类之间的关系
	 * 多个商品属于一个类
	 */
	private Category category;
	private Brand brand;
	
	public String getPbrand() {
		return pbrand;
	}
	public void setPbrand(String pbrand) {
		this.pbrand = pbrand;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public double getMarket_price() {
		return market_price;
	}
	public void setMarket_price(double market_price) {
		this.market_price = market_price;
	}
	public double getShop_price() {
		return shop_price;
	}
	public void setShop_price(double shop_price) {
		this.shop_price = shop_price;
	}
	public String getPimage() {
		return pimage;
	}
	public void setPimage(String pimage) {
		this.pimage = pimage;
	}
	public Date getPdate() {
		return pdate;
	}
	public void setPdate(Date pdate) {
		this.pdate = pdate;
	}
	public int getIs_hot() {
		return is_hot;
	}
	public void setIs_hot(int is_hot) {
		this.is_hot = is_hot;
	}
	public String getPdesc() {
		return pdesc;
	}
	public void setPdesc(String pdesc) {
		this.pdesc = pdesc;
	}
	public int getPflag() {
		return pflag;
	}
	public void setPflag(int pflag) {
		this.pflag = pflag;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public Brand getBrand() {
		return brand;
	}
	public void setBrand(Brand brand) {
		this.brand = brand;
	}
	
}
