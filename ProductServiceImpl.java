package cn.snnu.mm.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.snnu.mm.bean.Brand;
import cn.snnu.mm.bean.Carrier;
import cn.snnu.mm.bean.Category;
import cn.snnu.mm.bean.Consignee;
import cn.snnu.mm.bean.Order;
import cn.snnu.mm.bean.OrderItem;
import cn.snnu.mm.bean.PageBean;
import cn.snnu.mm.bean.Product;
import cn.snnu.mm.dao.ProductDao;
import cn.snnu.mm.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Resource
	private ProductDao productDao;
	
	@Override
	public List<Product> findByHot() throws SQLException {
		return productDao.findByHot();
	}

	@Override
	public List<Product> findByNew() throws SQLException {
		return productDao.findByNew();
	}

	@Override
	public Product findById(String pid) throws SQLException {
		return productDao.findById(pid);
	}

	@Override
	public void submitOrder(Order order) throws SQLException {
		//调用dao存储order表数据的方法
		productDao.addOrders(order);
		//调用dao存储orderitem表数据的方法
		productDao.addOrderItem(order);
	}

	@Override
	public List<Consignee> findInfoById(String id) throws SQLException {
		return productDao.findInfoById(id);
	}

	@Override
	public void delConsignee(String cid) throws SQLException {
		productDao.delConsignee(cid);		
	}

	@Override
	public void saveConsignee(String uuid, String location, String name, String address, String telephone, 
			String email, String uid) throws SQLException {
		productDao.saveConsignee(uuid,location,name,address,telephone,email,uid);	
	}

	@Override
	public void editConsignee(String flag, String location, String name, String address, String telephone,
			String email, String uid) throws SQLException {
		productDao.editConsignee(flag,location,name,address,telephone,email,uid);		
	}

	@Override
	public void updateOrder(Order order) throws SQLException {
		productDao.updateOrder(order);
	}

	@Override
	public PageBean findAllOrders(String id, int currentPage, int currentCount, String time, String word) throws SQLException {
		//封装一个PageBean 返回web层
		PageBean<Order> pageBean = new PageBean<Order>();
		
		//1、封装当前页
		pageBean.setCurrentPage(currentPage);
		//2、封装每页显示的条数
		pageBean.setCurrentCount(currentCount);
		//3、封装总条数
		int totalCount = 0;
		
		try {
			totalCount = productDao.getCount(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pageBean.setTotalCount(totalCount);
		//4、封装总页数
		int totalPage = (int) Math.ceil(1.0*totalCount/currentCount);
		pageBean.setTotalPage(totalPage);
		
		//5、当前页显示的数据
		// select * from product where cid=? limit ?,?
		// 当前页与起始索引index的关系
		int index = (currentPage-1)*currentCount;

		List<Order> orderList = null;
		/**
		 * 独创
		 * 
		 * @author zhangwz
		 * 2018年5月12日 上午01:14:37
		 */
		try {
			/**
			 * 查询该用户的所有的订单信息(单表查询orders表)
			 * 集合中的每一个Order对象的数据是不完整的 缺少List<OrderItem> orderItems数据
			 */
			orderList = productDao.findAllOrders(id,index,currentCount);
			
			//循环所有的订单 为每个订单填充订单项集合信息
			if(orderList!=null){
				for(Order order : orderList){
					//获得每一个订单的oid
					String oid = order.getOid();
					//查询该订单的所有的订单项---mapList封装的是多个订单项和该订单项中的商品的信息
					List<Map<String, Object>> mapList = productDao.findAllOrderItemByOid(oid,time,word);
					//将mapList转换成List<OrderItem> orderItems 
					for(Map<String,Object> map : mapList){
						
						try {
							//从map中取出count subtotal 封装到OrderItem中
							OrderItem item = new OrderItem();
							//item.setCount(Integer.parseInt(map.get("count").toString()));
							BeanUtils.populate(item, map);
							//从map中取出pimage pname shop_price 封装到Product中
							Product product = new Product();
							BeanUtils.populate(product, map);
							//将product封装到OrderItem
							item.setProduct(product);
							//将orderitem封装到order中的orderItemList中
							order.getOrderItems().add(item);
						} catch (IllegalAccessException | InvocationTargetException e) {
							e.printStackTrace();
						}
						
						
					}

				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		pageBean.setList(orderList);
		
		System.out.println("pageBean = " + JSON.toJSONString(pageBean));
		
		return pageBean;
	}

	@Override
	public List<Map<String, Object>> findAllOrderItemByOid(String oid, String time, String word) throws SQLException{
		List<Map<String, Object>> mapList = null;
		try {
			mapList = productDao.findAllOrderItemByOid(oid,time,word);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapList;
	}

	@Override
	public List<Map<String, Object>> findAllOrderItemByPid(String[] pidArray, Order order) throws SQLException {
		List<Map<String, Object>> mapList = null;
		try {
			mapList = productDao.findAllOrderItemByPid(pidArray, order);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapList;
	}
	
	@Override
	public List<Brand> finBrand() throws SQLException {
		return productDao.finBrand();
	}

	@Override
	public PageBean findBrandListByBid(String flag, String bid, int currentPage, int currentCount) throws SQLException {
		
		//封装一个PageBean 返回web层
		PageBean<Product> pageBean = new PageBean<Product>();
		
		//1、封装当前页
		pageBean.setCurrentPage(currentPage);
		//2、封装每页显示的条数
		pageBean.setCurrentCount(currentCount);
		//3、封装总条数
		int totalCount = 0;
		try {
			totalCount = productDao.getBrandCount(flag,bid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pageBean.setTotalCount(totalCount);
		//4、封装总页数
		int totalPage = (int) Math.ceil(1.0*totalCount/currentCount);
		pageBean.setTotalPage(totalPage);
		
		//5、当前页显示的数据
		// select * from product where cid=? limit ?,?
		// 当前页与起始索引index的关系
		int index = (currentPage-1)*currentCount;
		List<Product> list = null;
		try {
			list = productDao.findBrandProductByPage(flag,bid,index,currentCount);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pageBean.setList(list);
		
		
		return pageBean;
	}

	@Override
	public PageBean findProductListByName(String name, int currentPage, int currentCount) throws SQLException {
		
		//封装一个PageBean 返回web层
		PageBean<Product> pageBean = new PageBean<Product>();
		
		//1、封装当前页
		pageBean.setCurrentPage(currentPage);
		//2、封装每页显示的条数
		pageBean.setCurrentCount(currentCount);
		//3、封装总条数
		int totalCount = 0;
		try {
			totalCount = productDao.getProductCount(name);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pageBean.setTotalCount(totalCount);
		//4、封装总页数
		int totalPage = (int) Math.ceil(1.0*totalCount/currentCount);
		pageBean.setTotalPage(totalPage);
		
		//5、当前页显示的数据
		// select * from product where cid=? limit ?,?
		// 当前页与起始索引index的关系
		int index = (currentPage-1)*currentCount;
		List<Product> list = null;
		try {
			list = productDao.findProductByPage(name,index,currentCount);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pageBean.setList(list);
		
		
		return pageBean;
	}

	@Override
	public PageBean findBrandFromResult(String flag, String bid, String word, int currentPage, int currentCount) {
		
		//封装一个PageBean 返回web层
		PageBean<Product> pageBean = new PageBean<Product>();
		
		//1、封装当前页
		pageBean.setCurrentPage(currentPage);
		//2、封装每页显示的条数
		pageBean.setCurrentCount(currentCount);
		//3、封装总条数
		int totalCount = 0;
		try {
			totalCount = productDao.getBrandCount(flag,bid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pageBean.setTotalCount(totalCount);
		//4、封装总页数
		int totalPage = (int) Math.ceil(1.0*totalCount/currentCount);
		pageBean.setTotalPage(totalPage);
		
		//5、当前页显示的数据
		// select * from product where cid=? limit ?,?
		// 当前页与起始索引index的关系
		int index = (currentPage-1)*currentCount;
		List<Product> list = null;
		try {
			list = productDao.findProductResultByPage(flag,bid,word,index,currentCount);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pageBean.setList(list);
		
		
		return pageBean;
	}

	@Override
	public PageBean findProductFromResult(String name, String word, int currentPage, int currentCount)
			throws SQLException {
		
		//封装一个PageBean 返回web层
		PageBean<Product> pageBean = new PageBean<Product>();
		
		//1、封装当前页
		pageBean.setCurrentPage(currentPage);
		//2、封装每页显示的条数
		pageBean.setCurrentCount(currentCount);
		//3、封装总条数
		int totalCount = 0;
		try {
			totalCount = productDao.getProductCount(name);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pageBean.setTotalCount(totalCount);
		//4、封装总页数
		int totalPage = (int) Math.ceil(1.0*totalCount/currentCount);
		pageBean.setTotalPage(totalPage);
		
		//5、当前页显示的数据
		// select * from product where cid=? limit ?,?
		// 当前页与起始索引index的关系
		int index = (currentPage-1)*currentCount;
		List<Product> list = null;
		try {
			list = productDao.findProductResult(name,word,index,currentCount);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pageBean.setList(list);
		
		
		return pageBean;
	}

	@Override
	public List<Product> findHotBrand() throws SQLException {
		return productDao.findHotBrand();
	}

	@Override
	public List<Category> findCategory() throws SQLException {
		return productDao.findCategory();
	}

	@Override
	public List<Carrier> finCarrier() throws SQLException {
		return productDao.finCarrier();
	}

	@Override
	public List<Product> findAccessories() throws SQLException {
		return productDao.findAccessories();
	}

}
