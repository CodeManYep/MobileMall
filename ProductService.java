package cn.snnu.mm.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import cn.snnu.mm.bean.Brand;
import cn.snnu.mm.bean.Carrier;
import cn.snnu.mm.bean.Category;
import cn.snnu.mm.bean.Consignee;
import cn.snnu.mm.bean.Order;
import cn.snnu.mm.bean.PageBean;
import cn.snnu.mm.bean.Product;

public interface ProductService {

	//热门推荐商品
	public List<Product> findByHot() throws SQLException;

	//新品首发商品
	public List<Product> findByNew() throws SQLException;

	//根据商品pid获取商品信息
	public Product findById(String pid) throws SQLException;

	//提交订单 将订单的数据和订单项的数据存储到数据库中
	public void submitOrder(Order order) throws SQLException;

	//根据userid在收货人信息表中获取收货人信息对象
	public List<Consignee> findInfoById(String id) throws SQLException;

	//在收货人信息表中删除该收货人地址
	public void delConsignee(String cid) throws SQLException;
	
	//保存收货人信息
	public void saveConsignee(String uuid, String location, String name, String address, String telephone,
			String email, String uid) throws SQLException;
	
	//编辑收货人信息
	public void editConsignee(String flag, String location, String name, String address, String telephone,
			String email, String uid) throws SQLException;
	
	/**
	 * 将收货人信息插入orders表
	 * 
	 * @author zhangwz
	 * 2018年5月9日 上午01:05:48
	 */
	public void updateOrder(Order order) throws SQLException;

	/**
	 * 查询该用户的所有的订单信息(单表查询orders表)
	 * 集合中的每一个Order对象的数据是不完整的 缺少List<OrderItem> orderItems数据
	 * 
	 * @author zhangwz
	 * 2018年5月9日 上午17:09:48
	 */
	public PageBean findAllOrders(String id,int currentPage,int currentCount, String time, String word) throws SQLException;

	//查询该订单的所有的订单项-封装的是多个订单项和该订单项中的商品的信息
	public List<Map<String, Object>> findAllOrderItemByOid(String oid, String time, String word) throws SQLException;

	//获取 我的订单 中的商品详情
	public List<Map<String, Object>> findAllOrderItemByPid(String[] pidArray, Order order) throws SQLException;
	
	//获得精选品牌
	public List<Brand> finBrand() throws SQLException;

	/**
	 * 把品牌展示页面需要的数据封装到PageBean对象中
	 * 
	 * @author zhangwz
	 * @2018年5月13日下午4:02:40
	 */
	public PageBean findBrandListByBid(String flag, String bid, int currentPage, int currentCount) throws SQLException;

	//首页产品搜索功能-获得产品集合，封装到PageBean对象中
	public PageBean findProductListByName(String name, int currentPage, int currentCount) throws SQLException;

	/**
	 * 品牌展示页面结果中搜索到数据封装到PageBean对象中
	 * @author zhangwz
	 * @2018年5月15日上午9:48:37
	 */
	public PageBean findBrandFromResult(String flag, String bid, String word, int currentPage, int currentCount) throws SQLException;

	/**
	 * 产品搜索展示页面结果中搜索到数据封装到PageBean对象中
	 * 
	 * @author zhangwz
	 * @2018年5月15日上午10:18:09
	 */
	public PageBean findProductFromResult(String name, String word, int currentPage, int currentCount) throws SQLException;

	//获取人们推荐的品牌-特意 2 - Hot Selling Models
	public List<Product> findHotBrand() throws SQLException;

	//Hot Categories-热门分类
	public List<Category> findCategory() throws SQLException;

	//carrier
	public List<Carrier> finCarrier() throws SQLException;

	//Accessories
	public List<Product> findAccessories() throws SQLException;

}
