package cn.snnu.mm.dao.Impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSON;

import cn.snnu.mm.bean.Brand;
import cn.snnu.mm.bean.Carrier;
import cn.snnu.mm.bean.Category;
import cn.snnu.mm.bean.Consignee;
import cn.snnu.mm.bean.Order;
import cn.snnu.mm.bean.OrderItem;
import cn.snnu.mm.bean.Product;
import cn.snnu.mm.dao.ProductDao;
import cn.snnu.mm.util.DbPoolConnection;

@Repository
public class ProductDaoImpl implements ProductDao{
	
	//获取数据连接池单例
	DbPoolConnection dbp = DbPoolConnection.getInstance();
	private DruidPooledConnection conn = null;
	private PreparedStatement pstate = null;
	private Statement state = null;
	private ResultSet rs = null;
	
	@Override
	public List<Product> findByHot() throws SQLException {
		
		try {
			conn = dbp.getConnection();
			String sql = "select * from product where is_hot = 1";
			System.out.println("sql = " + sql);
			state = conn.createStatement();
			rs = state.executeQuery(sql);
			
			List<Product> list = new ArrayList<Product>();
			
			System.out.println("hotresulrset = " + rs.next());
			while (rs.next()) {
				Product product = new Product();
				
				product.setPid(rs.getString(1));
				product.setPname(rs.getString(2));
				product.setMarket_price(rs.getDouble(3));
				product.setShop_price(rs.getDouble(4));
				product.setPimage(rs.getString(5));
				product.setPdate(rs.getDate(6));
				product.setIs_hot(rs.getInt(7));
				product.setPdesc(rs.getString(8));
				product.setPflag(rs.getInt(9));
				product.setPbrand(rs.getString(11));
				list.add(product);
			}
			
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {  
			     rs.close();  
			     rs = null;  
			}  
			
		    if(state != null) {  
		    	state.close();  
		    	state = null;  
		    } 
		    
		    if(conn != null) {
		        conn.close();  
		        conn = null;  
		    }
		}
		
		return null;
	}

	@Override
	public List<Product> findByNew() throws SQLException {
		
		List<Product> list = new ArrayList<Product>();
		
		try {
			conn = dbp.getConnection();
			String sql = "select * from (select * from product order by pdate desc) where rownum < 7";
			System.out.println("sql = " + sql);
			state = conn.createStatement();
			rs = state.executeQuery(sql);
			
			while (rs.next()) {
				Product product = new Product();
				
				product.setPid(rs.getString(1));
				product.setPname(rs.getString(2));
				product.setMarket_price(rs.getDouble(3));
				product.setShop_price(rs.getDouble(4));
				product.setPimage(rs.getString(5));
				product.setPdate(rs.getDate(6));
				product.setIs_hot(rs.getInt(7));
				product.setPdesc(rs.getString(8));
				product.setPflag(rs.getInt(9));
				product.setPbrand(rs.getString(11));
				list.add(product);
			}
			
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {  
			     rs.close();  
			     rs = null;  
			}  
			
		    if(state != null) {  
		    	state.close();  
		    	state = null;  
		    } 
		    
		    if(conn != null) {
		        conn.close();  
		        conn = null;  
		    }
		}
		
		return null;
	}

	@Override
	public Product findById(String pid) throws SQLException {
		try {
			conn = dbp.getConnection();
			String sql = "select * from product where pid = '" + pid + "'";
			System.out.println("sql = " + sql);
			state = conn.createStatement();
			rs = state.executeQuery(sql);
			
			Product product = new Product();
			
			while (rs.next()) {
				product.setPid(rs.getString(1));
				product.setPname(rs.getString(2));
				product.setMarket_price(rs.getDouble(3));
				product.setShop_price(rs.getDouble(4));
				product.setPimage(rs.getString(5));
				product.setPdate(rs.getDate(6));
				product.setIs_hot(rs.getInt(7));
				product.setPdesc(rs.getString(8));
				product.setPflag(rs.getInt(9));
				product.setPbrand(rs.getString(11));
			}
			
			return product;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {  
			     rs.close();  
			     rs = null;  
			}  
			
		    if(state != null) {  
		    	state.close();  
		    	state = null;  
		    } 
		    
		    if(conn != null) {
		        conn.close();  
		        conn = null;  
		    } 
		}
		
		return null;
	}

	//提交订单 将订单的数据和订单项的数据存储到数据库中
	@Override
	public void submitOrder(Order order) throws SQLException {
		try {
			//开启事务
			conn.setAutoCommit(false);
			submitOrder(order);
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				conn.commit();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	//向orders表插入数据
	@Override
	public void addOrders(Order order) throws SQLException {
		try {
			conn = dbp.getConnection();
			String sql = "insert into orders values(?,?,?,?,?,?,?,?)";
			pstate = conn.prepareStatement(sql);
			
			pstate.setString(1, order.getOid());
			//java.util.date -> java.sql.date
			java.sql.Date sqlDate = new java.sql.Date(order.getOrdertime().getTime());
			pstate.setDate(2, sqlDate);
			pstate.setDouble(3, order.getTotal());
			pstate.setInt(4, order.getState());
			pstate.setString(5, order.getAddress());
			pstate.setString(6, order.getName());
			pstate.setString(7, order.getTelephone());
			System.out.println("uid = " + order.getUser().getId());
			pstate.setString(8, order.getUser().getId());
			
			pstate.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstate != null) {  
				pstate.close();  
				pstate = null;  
		    } 
		    
		    if(conn != null) {
		        conn.close();  
		        conn = null;  
		    }
		}
	}

	//向orderitem表插入数据
	@Override
	public void addOrderItem(Order order) throws SQLException {
		try {
			conn = dbp.getConnection();
			String sql = "insert into orderitem values(?,?,?,?,?)";
			pstate = conn.prepareStatement(sql);
			List<OrderItem> orderItems = order.getOrderItems();
			
			for(OrderItem item : orderItems){
				pstate.setString(1, item.getItemid());
				pstate.setInt(2, item.getCount());
				pstate.setDouble(3, item.getSubtotal());
				pstate.setString(4, item.getProduct().getPid());
				pstate.setString(5, item.getOrder().getOid());
				pstate.executeUpdate();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstate != null) {  
				pstate.close();  
				pstate = null;  
		    } 
		    
		    if(conn != null) {
		        conn.close();  
		        conn = null;  
		    }
		}
	}

	@Override
	public List<Consignee> findInfoById(String id) throws SQLException {
		
		List<Consignee> list = new ArrayList<Consignee>();
		
		try {
			conn = dbp.getConnection();
			String sql = "select * from consigneeinfo where id = '" + id + "'";
			System.out.println("sql = " + sql);
			state = conn.createStatement();
			rs = state.executeQuery(sql);
			
			while (rs.next()) {
				
				Consignee consignee = new Consignee();
				
				consignee.setCid(rs.getString(1));
				consignee.setLocation(rs.getString(2));
				consignee.setName(rs.getString(3));
				consignee.setAddress(rs.getString(4));
				consignee.setTelephone(rs.getString(5));
				consignee.setEmail(rs.getString(6));
				list.add(consignee);
			}
			
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {  
			     rs.close();  
			     rs = null;  
			}  
			
		    if(state != null) {  
		    	state.close();  
		    	state = null;  
		    } 
		    
		    if(conn != null) {
		        conn.close();  
		        conn = null;  
		    } 
		}
		
		return list;
	}
	
	//向consigneeinfo表插入数据
	@Override
	public void saveConsignee(String id, String location, String name, String address, String telephone, String email,String uid) throws SQLException {
		try {
			conn = dbp.getConnection();
			String sql = "insert into consigneeinfo values(?,?,?,?,?,?,?)";
			pstate = conn.prepareStatement(sql);
			
			pstate.setString(1, id);
			pstate.setString(2, location);
			pstate.setString(3, name);
			pstate.setString(4, address);
			pstate.setString(5, telephone);
			pstate.setString(6, email);
			pstate.setString(7, uid);
			
			pstate.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstate != null) {  
				pstate.close();  
				pstate = null;  
		    } 
		    
		    if(conn != null) {
		        conn.close();  
		        conn = null;  
		    }
		}
	}
	
	@Override
	public void delConsignee(String cid) throws SQLException {
		try {
			conn = dbp.getConnection();
			String sql = "delete from consigneeinfo where cid = '" + cid + "'";
			state = conn.createStatement();
			state.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(state != null) {  
		    	state.close();  
		    	state = null;  
		    } 
		    
		    if(conn != null) {
		        conn.close();  
		        conn = null;  
		    }
		}
		
	}

	@Override
	public void editConsignee(String flag, String location, String name, String address, String telephone,
			String email, String uid) throws SQLException {
		try {
			conn = dbp.getConnection();
			String sql = "update consigneeinfo set location='"+location+"',name='"+name+"',address='"+address+"',"
					   + "telephone='"+telephone+"',email='"+email+"',id='"+uid+"' where cid='"+flag+"'";
			state = conn.createStatement();
			state.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(state != null) {  
		    	state.close();  
		    	state = null;  
		    } 
		    
		    if(conn != null) {
		        conn.close();  
		        conn = null;  
		    }
		}		
	}

	@Override
	public void updateOrder(Order order) throws SQLException {
		try {
			conn = dbp.getConnection();
			String sql = "update orders set address=?,name=?,telephone=? where oid=?";
			System.out.println("sql = " + sql);
			pstate = conn.prepareStatement(sql);
			
			pstate.setString(1, order.getAddress());
			pstate.setString(2, order.getName());
			pstate.setString(3, order.getTelephone());
			pstate.setString(4, order.getOid());
			
			pstate.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstate != null) {  
				pstate.close();  
				pstate = null;  
		    } 
		    
		    if(conn != null) {
		        conn.close();  
		        conn = null;  
		    }
		}		
	}

	@Override
	public List<Order> findAllOrders(String id, int index, int currentCount) throws SQLException {
		
		List<Order> list = new ArrayList<Order>();
		
		try {
			conn = dbp.getConnection();
			String sql = "SELECT * FROM (SELECT A.*, ROWNUM RN FROM (SELECT * FROM orders where id = '" + id + "' order by ordertime desc) A WHERE ROWNUM <= '" + (index+currentCount) + "') WHERE RN > '" + index + "'";
			System.out.println("sql = " + sql);
			state = conn.createStatement();
			rs = state.executeQuery(sql);
			
			while (rs.next()) {
				Order order = new Order();
				
				order.setOid(rs.getString(1));
				order.setOrdertime(rs.getDate(2));
				order.setTotal(rs.getDouble(3));
				order.setState(rs.getInt(4));
				order.setAddress(rs.getString(5));
				order.setName(rs.getString(6));
				order.setTelephone(rs.getString(7));
				list.add(order);
			}
			
			System.out.println("orderList = " + JSON.toJSONString(list));
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {  
			     rs.close();  
			     rs = null;  
			}  
			
		    if(state != null) {  
		    	state.close();  
		    	state = null;  
		    } 
		    
		    if(conn != null) {
		        conn.close();  
		        conn = null;  
		    } 
		}
		
		return null;
	}

	//Map表示列名与列值得映射关系
	@Override
	public List<Map<String, Object>> findAllOrderItemByOid(String oid, String time, String word) throws SQLException {
		
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		
		try {
			conn = dbp.getConnection();
			String sql = null;
			System.out.println("time = " + time + ",word = " + word);
			if (word != null) {
				if (time.equals("1")) {
					sql = "select i.count,i.subtotal,o.ordertime,p.pname,p.shop_price,p.pdesc,p.pid from "
						+ "orderitem i,orders o,product p where i.pid=p.pid and i.oid=o.oid and o.ordertime between add_months(sysdate,-3) and sysdate"
						+ " and i.oid='" + oid + "' and p.pdesc like '%" + word + "%'";
				} else if (time.equals("2")) {
					System.out.println("time = " + time);
					sql = "select i.count,i.subtotal,o.ordertime,p.pname,p.shop_price,p.pdesc,p.pid from "
							+ "orderitem i,orders o,product p where i.pid=p.pid and i.oid=o.oid and to_char(o.ordertime,'yyyy')=to_char(sysdate,'yyyy')"
							+ " and i.oid='" + oid + "' and p.pdesc like '%" + word + "%'";
				} else if (time.equals("3")) {
					sql = "select i.count,i.subtotal,o.ordertime,p.pname,p.shop_price,p.pdesc,p.pid from "
							+ "orderitem i,orders o,product p where i.pid=p.pid and i.oid=o.oid"
							+ " and i.oid='" + oid + "' and p.pdesc like '%" + word + "%'";
				}
			} else {
				if (time.equals("1")) {
					sql = "select i.count,i.subtotal,o.ordertime,p.pname,p.shop_price,p.pdesc,p.pid from "
						+ "orderitem i,orders o,product p where i.pid=p.pid and i.oid=o.oid and o.ordertime between add_months(sysdate,-3) and sysdate"
						+ " and i.oid='" + oid + "'";
				} else if (time.equals("2")) {
					sql = "select i.count,i.subtotal,o.ordertime,p.pname,p.shop_price,p.pdesc,p.pid from "
							+ "orderitem i,orders o,product p where i.pid=p.pid and i.oid=o.oid and to_char(o.ordertime,'yyyy')=to_char(sysdate,'yyyy')"
							+ " and i.oid='" + oid + "'";
				} else if (time.equals("3")) {
					sql = "select i.count,i.subtotal,o.ordertime,p.pname,p.shop_price,p.pdesc,p.pid from "
							+ "orderitem i,orders o,product p where i.pid=p.pid and i.oid=o.oid"
							+ " and i.oid='" + oid + "'";
				} 
			}
			
			System.out.println("sql = " + sql);
			state = conn.createStatement();
			rs = state.executeQuery(sql);
			
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				
				map.put("count", rs.getString(1));
				map.put("subtotal", rs.getDouble(2));
				map.put("ordertime", rs.getDate(3));
				map.put("pname", rs.getString(4));
				map.put("shop_price", rs.getDouble(5));
				map.put("pdesc", rs.getString(6));
				map.put("pid", rs.getString(7));
				mapList.add(map);
			}
			
			return mapList;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {  
			     rs.close();  
			     rs = null;  
			}  
			
		    if(state != null) {  
		    	state.close();  
		    	state = null;  
		    } 
		    
		    if(conn != null) {
		        conn.close();  
		        conn = null;  
		    } 
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> findAllOrderItemByPid(String[] pidArray, Order order) throws SQLException {
		String result = "";
		
		for (int i = 0; i < pidArray.length; i++) {
			result += "'" + pidArray[i] + "'" + ",";
		}
		
		result = result.substring(0,result.length() - 1);
		
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		
		try {
			conn = dbp.getConnection();
			String sql = "select DISTINCT  i.count,i.subtotal,p.pname,p.shop_price,p.pdesc from orderitem i,product p where "
					   + "i.pid=p.pid and p.pid in (" + result + ") and i.oid = '" + order.getOid() + "'";
			System.out.println("sql = " + sql);
			state = conn.createStatement();
			rs = state.executeQuery(sql);
			
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				
				map.put("count", rs.getString(1));
				map.put("subtotal", rs.getDouble(2));
				map.put("pname", rs.getString(3));
				map.put("shop_price", rs.getDouble(4));
				map.put("pdesc", rs.getString(5));
				mapList.add(map);
			}
			
			return mapList;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {  
			     rs.close();  
			     rs = null;  
			}  
			
		    if(state != null) {  
		    	state.close();  
		    	state = null;  
		    } 
		    
		    if(conn != null) {
		        conn.close();  
		        conn = null;  
		    } 
		}
		return null;
	}

	@Override
	public int getCount(String id) throws SQLException {
		System.out.println("------- Dao -------");
		int count = 0;
		
		try {
			conn = dbp.getConnection();
			state = conn.createStatement();
			String sql = "select count(*) from orders where id = '"+ id +"'";
			System.out.println("sql = " + sql);
			rs = state.executeQuery(sql);
			while (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    try {  
		    	if(rs != null) {  
				     rs.close();  
				     rs = null;  
				}  
				
			    if(state != null) {  
			    	state.close();  
			    	state = null;  
			    } 
			    
			    if(conn != null) {
			        conn.close();  
			        conn = null;  
			    }
		    } catch (SQLException e) {  
		    	e.printStackTrace();  
		    }  
		}
		
		return count;
	}

	@Override
	public List<Brand> finBrand() throws SQLException {
		
		List<Brand> list = new ArrayList<Brand>();
		
		try {
			conn = dbp.getConnection();
			String sql = "select * from brand where rownum < 13";
			System.out.println("sql = " + sql);
			state = conn.createStatement();
			rs = state.executeQuery(sql);
			
			while (rs.next()) {
				Brand brand = new Brand();
				
				brand.setBid(rs.getString(1));
				brand.setBname(rs.getString(2));
				brand.setBimg(rs.getString(3));
				brand.setBdesc(rs.getString(4));
				list.add(brand);
			}
			
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {  
			     rs.close();  
			     rs = null;  
			}  
			
		    if(state != null) {  
		    	state.close();  
		    	state = null;  
		    } 
		    
		    if(conn != null) {
		        conn.close();  
		        conn = null;  
		    }
		}
		
		return null;
	}

	@Override
	public int getBrandCount(String flag,String bid) throws SQLException {
		System.out.println("------- DaoImpl -------");
		int count = 0;
		
		try {
			conn = dbp.getConnection();
			state = conn.createStatement();
			String sql = null;
			
			System.out.println("flag = " + flag);
			
			//判断执行哪个SQL语句
			if (flag.equals("1")) {
				sql = "select count(*) from product where crid = '"+ bid +"'";
			} else if (flag.equals("2")) {
				sql = "select count(*) from product where cid = '"+ bid +"'";
			} else if (flag.equals("3")) {
				sql = "select count(*) from product";
			} else {
				sql = "select count(*) from product where bid = '"+ bid +"'";
			}
			
			System.out.println("sql = " + sql);
			rs = state.executeQuery(sql);
			while (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    try {  
		    	if(rs != null) {  
				     rs.close();  
				     rs = null;  
				}  
				
			    if(state != null) {  
			    	state.close();  
			    	state = null;  
			    } 
			    
			    if(conn != null) {
			        conn.close();  
			        conn = null;  
			    }
		    } catch (SQLException e) {  
		    	e.printStackTrace();  
		    }  
		}
		
		return count;
	}

	@Override
	public List<Product> findBrandProductByPage(String flag, String bid, int index, int currentCount) throws SQLException {
		
		List<Product> list = new ArrayList<Product>();
		
		try {
			conn = dbp.getConnection();
			
			String sql = null;
			
			//判断执行哪个SQL语句
			if (flag.equals("1")) {
				sql = "SELECT * FROM (SELECT A.*, ROWNUM RN FROM (SELECT * FROM product where crid = '" + bid + "') A WHERE ROWNUM <= '" + (index+currentCount) + "') WHERE RN > '" + index + "'";
			} else if (flag.equals("2")) {
				sql = "SELECT * FROM (SELECT A.*, ROWNUM RN FROM (SELECT * FROM product where cid = '" + bid + "') A WHERE ROWNUM <= '" + (index+currentCount) + "') WHERE RN > '" + index + "'";
			} else if (flag.equals("3")) {
				sql = "SELECT * FROM (SELECT A.*, ROWNUM RN FROM (SELECT * FROM product) A WHERE ROWNUM <= '" + (index+currentCount) + "') WHERE RN > '" + index + "'";
			} else {
				sql = "SELECT * FROM (SELECT A.*, ROWNUM RN FROM (SELECT * FROM product where bid = '" + bid + "') A WHERE ROWNUM <= '" + (index+currentCount) + "') WHERE RN > '" + index + "'";
			}

			
			System.out.println("sql = " + sql);
			state = conn.createStatement();
			rs = state.executeQuery(sql);
			
			while (rs.next()) {
				Product product = new Product();
				
				product.setPid(rs.getString("pid"));
				product.setPname(rs.getString("pname"));
				product.setShop_price(rs.getDouble("shop_price"));
				product.setPimage(rs.getString("pimage"));
				product.setPdesc(rs.getString("pdesc"));
				product.setPbrand(rs.getString("pbrand"));
				list.add(product);
			}
			
			System.out.println("list = " + JSON.toJSONString(list));
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {  
			     rs.close();  
			     rs = null;  
			}  
			
		    if(state != null) {  
		    	state.close();  
		    	state = null;  
		    } 
		    
		    if(conn != null) {
		        conn.close();  
		        conn = null;  
		    } 
		}
		
		return null;
	}

	@Override
	public int getProductCount(String name) throws SQLException {
		System.out.println("------- Dao -------");
		int count = 0;
		
		try {
			conn = dbp.getConnection();
			state = conn.createStatement();
			String sql = "select count(*) from product where pdesc like '%"+ name +"%'";
			System.out.println("sql = " + sql);
			rs = state.executeQuery(sql);
			while (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    try {  
		    	if(rs != null) {  
				     rs.close();  
				     rs = null;  
				}  
				
			    if(state != null) {  
			    	state.close();  
			    	state = null;  
			    } 
			    
			    if(conn != null) {
			        conn.close();  
			        conn = null;  
			    }
		    } catch (SQLException e) {  
		    	e.printStackTrace();  
		    }  
		}
		
		return count;
	}

	@Override
	public List<Product> findProductByPage(String name, int index, int currentCount) throws SQLException {
		
		List<Product> list = new ArrayList<Product>();
		
		try {
			conn = dbp.getConnection();
			String sql = "SELECT * FROM (SELECT A.*, ROWNUM RN FROM (SELECT * FROM product where pdesc like '%" + name + "%') A WHERE ROWNUM <= '" + (index+currentCount) + "') WHERE RN > '" + index + "'";
			System.out.println("sql = " + sql);
			state = conn.createStatement();
			rs = state.executeQuery(sql);
			
			while (rs.next()) {
				Product product = new Product();
				
				product.setPid(rs.getString("pid"));
				product.setPname(rs.getString("pname"));
				product.setShop_price(rs.getDouble("shop_price"));
				product.setPimage(rs.getString("pimage"));
				product.setPdesc(rs.getString("pdesc"));
				product.setPbrand(rs.getString("pbrand"));
				list.add(product);
			}
			
			System.out.println("list = " + JSON.toJSONString(list));
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {  
			     rs.close();  
			     rs = null;  
			}  
			
		    if(state != null) {  
		    	state.close();  
		    	state = null;  
		    } 
		    
		    if(conn != null) {
		        conn.close();  
		        conn = null;  
		    } 
		}
		
		return null;
	}

	@Override
	public List<Product> findProductResultByPage(String flag, String bid, String word, int index, int currentCount)
			throws SQLException {
		
		List<Product> list = new ArrayList<Product>();
		
		try {
			conn = dbp.getConnection();
			
			String sql = null;
			
			//判断执行哪个SQL语句
			if (flag.equals("1")) {
				sql = "SELECT * FROM (SELECT A.*, ROWNUM RN FROM (SELECT * FROM product where crid = '" + bid + "' and pdesc like '%"+ word +"%') A WHERE ROWNUM <= '" + (index+currentCount) + "') WHERE RN > '" + index + "'";
			} else if (flag.equals("2")) {
				sql = "SELECT * FROM (SELECT A.*, ROWNUM RN FROM (SELECT * FROM product where cid = '" + bid + "' and pdesc like '%"+ word +"%') A WHERE ROWNUM <= '" + (index+currentCount) + "') WHERE RN > '" + index + "'";
			} else if (flag.equals("3")) {
				sql = "SELECT * FROM (SELECT A.*, ROWNUM RN FROM (SELECT * FROM product where pdesc like '%"+ word +"%') A WHERE ROWNUM <= '" + (index+currentCount) + "') WHERE RN > '" + index + "'";
			} else {
				sql = "SELECT * FROM (SELECT A.*, ROWNUM RN FROM (SELECT * FROM product where bid = '" + bid + "' and pdesc like '%"+ word +"%') A WHERE ROWNUM <= '" + (index+currentCount) + "') WHERE RN > '" + index + "'";
			}
			
			System.out.println("sql = " + sql);
			state = conn.createStatement();
			rs = state.executeQuery(sql);
			
			while (rs.next()) {
				Product product = new Product();
				
				product.setPid(rs.getString("pid"));
				product.setPname(rs.getString("pname"));
				product.setShop_price(rs.getDouble("shop_price"));
				product.setPimage(rs.getString("pimage"));
				product.setPdesc(rs.getString("pdesc"));
				product.setPbrand(rs.getString("pbrand"));
				list.add(product);
			}
			
			System.out.println("list = " + JSON.toJSONString(list));
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {  
			     rs.close();  
			     rs = null;  
			}  
			
		    if(state != null) {  
		    	state.close();  
		    	state = null;  
		    } 
		    
		    if(conn != null) {
		        conn.close();  
		        conn = null;  
		    } 
		}
		
		return null;
	}

	@Override
	public List<Product> findProductResult(String name, String word, int index, int currentCount) throws SQLException {
		
		List<Product> list = new ArrayList<Product>();
		
		try {
			conn = dbp.getConnection();
			String sql = "SELECT * FROM (SELECT A.*, ROWNUM RN FROM (SELECT * FROM product where pdesc like '%" + name + "%' and pdesc like '%" + word + "%') A WHERE ROWNUM <= '" + (index+currentCount) + "') WHERE RN > '" + index + "'";
			System.out.println("sql = " + sql);
			state = conn.createStatement();
			rs = state.executeQuery(sql);
			
			while (rs.next()) {
				Product product = new Product();
				
				product.setPid(rs.getString("pid"));
				product.setPname(rs.getString("pname"));
				product.setShop_price(rs.getDouble("shop_price"));
				product.setPimage(rs.getString("pimage"));
				product.setPdesc(rs.getString("pdesc"));
				product.setPbrand(rs.getString("pbrand"));
				list.add(product);
			}
			
			System.out.println("list = " + JSON.toJSONString(list));
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {  
			     rs.close();  
			     rs = null;  
			}  
			
		    if(state != null) {  
		    	state.close();  
		    	state = null;  
		    } 
		    
		    if(conn != null) {
		        conn.close();  
		        conn = null;  
		    } 
		}
		
		return null;
	}

	@Override
	public List<Product> findHotBrand() throws SQLException {
		
		try {
			conn = dbp.getConnection();
			String sql = "select * from product e1 where exists(select pbrand from product e2 where e1.pbrand=e2.pbrand group by e2.pbrand having max(e2.rowid)=e1.rowid) and is_hot = 2 and rownum < 7";
			System.out.println("sql = " + sql);
			state = conn.createStatement();
			rs = state.executeQuery(sql);
			
			List<Product> list = new ArrayList<Product>();
			
			while (rs.next()) {
				Product product = new Product();
				
				product.setPid(rs.getString(1));
				product.setPname(rs.getString(2));
				product.setMarket_price(rs.getDouble(3));
				product.setShop_price(rs.getDouble(4));
				product.setPimage(rs.getString(5));
				product.setPdate(rs.getDate(6));
				product.setIs_hot(rs.getInt(7));
				product.setPdesc(rs.getString(8));
				product.setPflag(rs.getInt(9));
				product.setPbrand(rs.getString(11));
				list.add(product);
			}
			System.out.println("hotBrandList = " + JSON.toJSONString(list));
			
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {  
			     rs.close();  
			     rs = null;  
			}  
			
		    if(state != null) {  
		    	state.close();  
		    	state = null;  
		    } 
		    
		    if(conn != null) {
		        conn.close();  
		        conn = null;  
		    }
		}
		
		return null;
	}

	@Override
	public List<Category> findCategory() throws SQLException {
		
		try {
			conn = dbp.getConnection();
			String sql = "select * from CATEGORY";
			System.out.println("sql = " + sql);
			state = conn.createStatement();
			rs = state.executeQuery(sql);
			
			List<Category> list = new ArrayList<Category>();
			
			while (rs.next()) {
				Category category = new Category();
				
				category.setCid(rs.getString(1));
				category.setCname(rs.getString(2));
				list.add(category);
			}
			System.out.println("categoryList = " + JSON.toJSONString(list));
			
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {  
			     rs.close();  
			     rs = null;  
			}  
			
		    if(state != null) {  
		    	state.close();  
		    	state = null;  
		    } 
		    
		    if(conn != null) {
		        conn.close();  
		        conn = null;  
		    }
		}
		
		return null;
	}

	@Override
	public List<Carrier> finCarrier() throws SQLException {
		
		try {
			conn = dbp.getConnection();
			String sql = "select * from carrier";
			System.out.println("sql = " + sql);
			state = conn.createStatement();
			rs = state.executeQuery(sql);
			
			List<Carrier> list = new ArrayList<Carrier>();
			
			while (rs.next()) {
				Carrier carrier = new Carrier();
				
				carrier.setCrid(rs.getString(1));
				carrier.setCrname(rs.getString(2));
				list.add(carrier);
			}
			System.out.println("carrierList = " + JSON.toJSONString(list));
			
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {  
			     rs.close();  
			     rs = null;  
			}  
			
		    if(state != null) {  
		    	state.close();  
		    	state = null;  
		    } 
		    
		    if(conn != null) {
		        conn.close();  
		        conn = null;  
		    }
		}
		
		return null;
	}

	@Override
	public List<Product> findAccessories() throws SQLException {
		
		try {
			conn = dbp.getConnection();
			String sql = "select * from product where is_hot = 3";
			//String sql = "select * from product where cid = '439127f46d6a4aebbce743038084dac2'";
			System.out.println("sql = " + sql);
			state = conn.createStatement();
			rs = state.executeQuery(sql);
			
			List<Product> list = new ArrayList<Product>();
			
			while (rs.next()) {
				Product product = new Product();
				
				product.setPid(rs.getString(1));
				product.setPname(rs.getString(2));
				product.setMarket_price(rs.getDouble(3));
				product.setShop_price(rs.getDouble(4));
				product.setPimage(rs.getString(5));
				product.setPdate(rs.getDate(6));
				product.setIs_hot(rs.getInt(7));
				product.setPdesc(rs.getString(8));
				product.setPflag(rs.getInt(9));
				product.setPbrand(rs.getString(11));
				list.add(product);
			}
			
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {  
			     rs.close();  
			     rs = null;  
			}  
			
		    if(state != null) {  
		    	state.close();  
		    	state = null;  
		    } 
		    
		    if(conn != null) {
		        conn.close();  
		        conn = null;  
		    }
		}
		
		return null;
	}

}

