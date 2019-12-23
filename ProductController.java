package cn.snnu.mm.web.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.snnu.mm.bean.Cart;
import cn.snnu.mm.bean.CartItem;
import cn.snnu.mm.bean.Consignee;
import cn.snnu.mm.bean.Order;
import cn.snnu.mm.bean.OrderItem;
import cn.snnu.mm.bean.PageBean;
import cn.snnu.mm.bean.Product;
import cn.snnu.mm.bean.User;
import cn.snnu.mm.service.ProductService;
import cn.snnu.mm.util.Common;
import cn.snnu.mm.util.MailUtil;

/**
 * 产品相关操作，包括商品详情页
 * 、商品加入购物车、删除商品等
 * 
 * @author zhangwz
 * 2018年4月17日 上午9:24:48
 */
@Controller
@RequestMapping("/product")
public class ProductController extends BaseController {

	private static final long serialVersionUID = -3137145294685526747L;
	
	@Resource
	ProductService productService;
	
	//进入商品的详情页面
	@RequestMapping(value = "/product_info", method = RequestMethod.GET)
	public String productInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
		System.out.println("------- product_info -------");
		//接收商品的pid参数
		String pid = request.getParameter("pid");
		System.out.println("pid = " + pid);
		
		Product product = productService.findById(pid);
		
		request.setAttribute("product", product);
		
		String pids = pid;
		Cookie[] cookies = request.getCookies();
		
		if (cookies != null) {
			//非第一次浏览商品
			for(Cookie cookie : cookies){
				if (("pids").equals(cookie.getName())) {
					pids = cookie.getValue();
					
					//将pids拆成一个数组
					String[] pidsArray = pids.split("-");//{3,1,2}
					//数组转化为集合
					List<String> pidsList = Arrays.asList(pidsArray);//[3,1,2]
					LinkedList<String> list = new LinkedList<String>(pidsList);//[3,1,2]
					//判断集合中是否存在当前pid-更改显示顺序
					if (list.contains(pid)) {
						//包含当前查看商品的pid-已经浏览过
						list.remove(pid);
						list.addFirst(pid);
					} else {
						//没有浏览过该商品
						//不包含当前查看商品的pid 直接将该pid放到头上
						list.addFirst(pid);
					}
					
					//将[3,1,2]转成3-1-2字符串
					StringBuffer sb = new StringBuffer();
					for(int i = 0; i < list.size() && i < 4; i++){
						sb.append(list.get(i));
						sb.append("-");//3-1-2-
					}
					
					//去掉3-1-2-后的-
					pids = sb.substring(0, sb.length()-1);
				} 
			}
		}
		
		//后续添加查看 浏览历史 功能时会使用-第一次浏览商品
		Cookie cookie_pids = new Cookie("pids",pids);
		response.addCookie(cookie_pids);
		
		return "product_info";
	}
	
	//进入 购物车 页面
	@RequestMapping(value = "/cart", method = RequestMethod.GET)
	public String enterCart(HttpServletRequest request, HttpServletResponse response) throws Exception{
		System.out.println("------- cart -------");
		
		//定义一个记录历史商品信息的集合-最近浏览
		List<Product> historyProductList = new ArrayList<Product>();
		
		//获得客户端携带名字叫pids的cookie
		Cookie[] cookies = request.getCookies();
		
		for(Cookie cookie : cookies){
			System.out.println("name = " + cookie.getName() + ",value = " + cookie.getValue());
		}
		if(cookies!=null){
			for(Cookie cookie : cookies){
				if("pids".equals(cookie.getName())){
					String pids = cookie.getValue();//3-2-1
					System.out.println("pids = " + pids);
					String[] pidsArray = pids.split("-");
					for(String pid : pidsArray){
						Product product = productService.findById(pid);
						historyProductList.add(product);
					}
				}
			}
		}

		//将历史记录的集合放到域中
		request.setAttribute("historyProductList", historyProductList);
		
		return "cart";
	}
	
	//将商品添加到购物车
	@RequestMapping(value = "/addCart", method = RequestMethod.GET)
	public void addProductToCart(HttpServletRequest request, HttpServletResponse response) 
								 throws SQLException, IOException {
		HttpSession session = request.getSession();
		
		//获取商品pid
		String pid = request.getParameter("pid");
		
		System.out.println("pid = " + pid);
		//获取商品购买数量
		int buyNum = Integer.parseInt(request.getParameter("buyNum"));
		System.out.println("buyNum = " + buyNum);
		
		//获得product对象
		Product product = productService.findById(pid);
		
		//商品商城价
		double shop_price = product.getShop_price();
		//计算小计
		double subtotal = shop_price * buyNum;
		
		//封装CartItem
		CartItem item = new CartItem();
		item.setProduct(product);
		item.setBuyNum(buyNum);
		item.setSubtotal(subtotal);
		
		//获得购物车-判断是否在session中已经存在购物车
		Cart cart = (Cart) session.getAttribute("cart");
		if (cart == null) {
			cart = new Cart();
		}
		
		/**
		 * 将购物项放到车中-key是pid
		 * 先判断购物车中是否已将包含此购物项了-判断key是否已经存在
		 * 如果购物车中已经存在该商品-将现在买的数量与原有的数量进行相加操作
		 */
		Map<String, CartItem> cartItems = cart.getCartItems();
		
		double newSubTotal = 0.0;
		if (cartItems.containsKey(pid)) {
			//取出原有商品的数量
			CartItem cartItem = cartItems.get(pid);
			
			int oldBuyNum = cartItem.getBuyNum();
			oldBuyNum += buyNum;
			cartItem.setBuyNum(oldBuyNum);
			cart.setCartItems(cartItems);
			
			//修改小计
			//原理该商品的小计
			double oldSubTotal = cartItem.getSubtotal();
			//新买的商品小计
			newSubTotal = buyNum * product.getShop_price();
			//商品小计汇总
			double lastSubTotal = oldSubTotal + newSubTotal;
			cartItem.setSubtotal(lastSubTotal);
		} else {
			cart.getCartItems().put(pid, item);
			newSubTotal = buyNum * product.getShop_price();
		}
		
		//计算总计
		double total = cart.getTotal() + newSubTotal;
		cart.setTotal(total);
		
		session.setAttribute("cart", cart);
		
		//跳转到购物车页面
		response.sendRedirect("cart");
	}
	
	/**
	 * 删除单一商品
	 * 
	 * @author zhangwz
	 * 2018年4月28日 下午13:21:48
	 * @throws IOException 
	 */
	@RequestMapping(value = "/delProFromCart", method = RequestMethod.GET)
	public void delProFromCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//获取要删除的商品pid
		String pid = request.getParameter("pid");
		
		//删除session中的购物车中的购物项集合中的item
		HttpSession session = request.getSession();
		Cart cart = (Cart) session.getAttribute("cart");
		
		if (cart != null) {
			Map<String, CartItem> cartItems = cart.getCartItems();
			//修改总价
			cart.setTotal(cart.getTotal() - cartItems.get(pid).getSubtotal());
			//从map中移除该商品
			cartItems.remove(pid);
			cart.setCartItems(cartItems);
		}
		
		session.setAttribute("cart", cart);
		
		//跳转至购物车页面
		response.sendRedirect("cart");
	}
	
	/**
	 * 购物车中商品处于未选中状态的时候从 购物车 中移除
	 * 即移除该pid
	 * 
	 * @author zhangwz
	 * @throws IOException 
	 * @2018年5月19日下午3:54:27
	 */
	@RequestMapping(value = "/unCheckedFromCart", method = RequestMethod.GET)
	@ResponseBody
	public void unCheckedFromCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//获取要删除的商品pid
		String pid = request.getParameter("pid");
		
		//删除session中的购物车中的购物项集合中的item
		HttpSession session = request.getSession();
		Cart cart = (Cart) session.getAttribute("cart");
		
		if (cart != null) {
			Map<String, CartItem> cartItems = cart.getCartItems();
			//修改总价
			cart.setTotal(cart.getTotal() - cartItems.get(pid).getSubtotal());
			//从map中移除该商品
			cartItems.remove(pid);
			cart.setCartItems(cartItems);
		} 
		
		session.setAttribute("cart", cart);
		
		//跳转至购物车页面
		response.sendRedirect("cart");
	}
	
	//删除选中的商品
	@RequestMapping(value = "/delChoosePro", method = RequestMethod.GET)
	public void delChoosePro(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//获取选中商品pid数组
		String[] pidArray = request.getParameter("result").split(",");
		
		
		//删除session中的购物车中的购物项集合中的item
		HttpSession session = request.getSession();
		Cart cart = (Cart) session.getAttribute("cart");
		
		if (cart != null) {
			Map<String, CartItem> cartItems = cart.getCartItems();
			
			System.out.println("------- pidArray -------");
			for (int i = 0; i < pidArray.length; i++) {
				System.out.println(pidArray[i]);
				//修改总价
				cart.setTotal(cart.getTotal() - cartItems.get(pidArray[i]).getSubtotal());
				//从map中移除该商品
				cartItems.remove(pidArray[i]);
			}
			
			cart.setCartItems(cartItems);
		}
		
		session.setAttribute("cart", cart);
		
		//跳转至购物车页面
		response.sendRedirect("cart");
	}
	
	//清空购物车
	@RequestMapping(value = "/clearCart", method = RequestMethod.GET)
	public void clearCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//获取购物车并清空
		HttpSession session = request.getSession();
		session.removeAttribute("cart");
		
		//重定向至 购物车 页面
		response.sendRedirect("cart");
	}
	
	//提交订单，进入订单详情页面
	@RequestMapping(value = "/order_info", method = RequestMethod.GET)
	public String submitOrder(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
		//获得登录用户
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		/**
		 * 根据userid在收货人信息表中获取收货人信息对象，包括地址、联系方式、
		 * 姓名等
		 */
		List<Consignee> consigneeList = productService.findInfoById(user.getId());
		System.out.println("------- controller -------");
		for(Consignee consignee : consigneeList){
			System.out.println("consignee = " + consignee.getName());
		}
		request.setAttribute("consigneeList", consigneeList);
		
		//获得购物车
		Cart cart = (Cart) session.getAttribute("cart");
		
		//封装好一个Order对象 传递给service层
		Order order = new Order();
		
		String oid = Common.getUUID();//订单号
		Date date = new Date();//下单时间
		//日期格式转换
		/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.format(date);*/
		double total = cart.getTotal();//订单总金额
		
		order.setOid(oid);
		order.setOrdertime(date);
		order.setTotal(total);
		order.setState(0);
		order.setAddress(null);
		order.setTelephone(null);
		order.setUser(user);
		
		/**
		 * 该订单中有多少订单项
		 * 
		 * List<OrderItem> orderItems = new ArrayList<OrderItem>();
		 */
		//获得购物车中的购物项的集合map
		Map<String, CartItem> cartItems = cart.getCartItems();
		for(Map.Entry<String, CartItem> entry : cartItems.entrySet()){
			//取出每一个购物项
			CartItem cartItem = entry.getValue();
			//创建新的订单项
			OrderItem orderItem = new OrderItem();
			//itemid-订单项的id
			orderItem.setItemid(Common.getUUID());
			//count-订单项内商品的购买数量
			orderItem.setCount(cartItem.getBuyNum());
			//subtotal-订单项小计
			orderItem.setSubtotal(cartItem.getSubtotal());
			//product-订单项内部的商品
			orderItem.setProduct(cartItem.getProduct());
			//order-该订单项属于哪个订单
			orderItem.setOrder(order);

			//将该订单项添加到订单的订单项集合中
			order.getOrderItems().add(orderItem);
		}
		
		//order对象封装完毕
		//传递数据到service层
		productService.submitOrder(order);

		session.setAttribute("order", order);
		
		//返回  购物车 页面
		return "order_info";
	}
	
	//删除某一个收货人信息delConsignee
	@RequestMapping(value = "/delConsignee", method = RequestMethod.GET)
	public void delConsignee(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		//获取要删除的收货人的cid
		String cid = request.getParameter("cid");
		//在收货人信息表中删除该收货人地址
		productService.delConsignee(cid);
		
		response.sendRedirect("order_info");
	}
	
	//保存、编辑收货人信息saveConsignee.do
	@RequestMapping(value = "/saveConsignee.do", method = RequestMethod.POST)
	public void saveConsignee(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		System.out.println("------- saveConsignee.do -------");
		
		String location = request.getParameter("location");
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String telephone = request.getParameter("telephone");
		String email = request.getParameter("email");
		
		String flag = request.getParameter("flag");
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String uid = user.getId();//当前用户id
		
		if (flag.equals("1")) {
			productService.saveConsignee(Common.getUUID(),location,name,address,telephone,email,uid);
		} else {
			System.out.println("------- here update -------");
			System.out.println("flag = " + flag);
			productService.editConsignee(flag,location,name,address,telephone,email,uid);
		}
		
		//return "order_info";
		response.sendRedirect("order_info");
	}
	
	//点击 提交订单 后 进入 我的订单-当前的订单 页面order
	@RequestMapping(value = "/order", method = RequestMethod.GET)
	public String getOrder(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
		
		String address = request.getParameter("address");
		String name = request.getParameter("name");
		String telephone = request.getParameter("telephone");
		String oid = request.getParameter("oid");
		String total = request.getParameter("total");
		
		System.out.println("oid = " + oid);
		
		//封装order对象
		Order order = new Order();
		order.setOid(oid);
		order.setName(name);
		order.setAddress(address);
		order.setTelephone(telephone);
		order.setTotal(Double.parseDouble(total));
		
		request.setAttribute("order", order);
		
		productService.updateOrder(order);
		
		//获取商品pid数组
		String[] pidArray = request.getParameter("pid").split(",");
		
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		
		mapList = productService.findAllOrderItemByPid(pidArray,order);
		
		/*for (Map<String, Object> m : mapList)  
	    {  
	      for (String k : m.keySet())  
	      {  
	        System.out.println(k + " : " + m.get(k));  
	      }  
	  
	    }*/
		
		//将mapList转换成List<OrderItem> orderItems 
		for(Map<String,Object> map : mapList){
			
			try {
				//从map中取出count subtotal 封装到OrderItem中
				OrderItem item = new OrderItem();
				//item.setCount(Integer.parseInt(map.get("count").toString()));
				/**
				 * BeanUtils.populate( Object bean, Map properties )，
			 	 * 这个方法会遍历map<key, value>中的key，
			 	 * 如果bean中有这个属性，就把这个key对应的value值赋给bean的属性。
				 */
				BeanUtils.populate(item, map);
				//从map中取出pimage pname shop_price 封装到Product中
				Product product = new Product();
				BeanUtils.populate(product, map);
				//将product封装到OrderItem
				item.setProduct(product);
				//将orderitem封装到order中的orderItemList中
				//order.getOrderItems().add(item);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		
		request.setAttribute("mapList", mapList);
		
		//获取购物车并清空
		HttpSession session = request.getSession();
		session.removeAttribute("cart");
		
		//request.getRequestDispatcher("order").forward(request, response);
		//response.sendRedirect("order");
		
		//提交订单后向客户和商家都发送邮件
		String emailMsg = "您有新的提单信息，请前往订单列表查看";
		
		try {
		    //MailUtils.sendMail("email address", emailMsg);//客户邮箱
			MailUtil.sendMail("email address", emailMsg);//商家邮箱
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		System.out.println("邮件发送成功");
		
		return "order";
	}
	
	
	/**
	 * 提交订单，进入订单列表页面
	 * 包括
	 * 1、把收货人信息存入orders表
	 * 2、跳转到订单列表页面
	 * 最后一公里
	 * 
	 * @author zhangwz
	 * 2018年5月9日 上午00:14:48
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@RequestMapping(value = "/order_list", method = RequestMethod.GET)
	public String confirmOrder(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		System.out.println("------- order_list -------");
		
		/**
		 * 事件段内的订单详情
		 * 1-近三个月订单
		 * 2-近一年订单
		 * 3-全部订单
		 * 默认近3个月订单-1
		 * 
		 * @author zhangwz
		 * 2018年5月12日 上午01:14:37
		 */
		String time = request.getParameter("num");
		if (time == null) {
			time = "1";
		}
		System.out.println("time = " + time);
		//搜索内容-商品名称/商品编号/订单号
		String word = request.getParameter("word");
		if (word == "") {
			word = null;
		}
		/**
		 * 增加分页显示订单功能
		 * 
		 * @author zhangwz
		 * @2018年5月12日下午5:40:33
		 */
		String currentPageStr = request.getParameter("currentPage");
		if(currentPageStr==null) currentPageStr="1";
		
		int currentPage = Integer.parseInt(currentPageStr);
		int currentCount = 7;
		
		//以下是获得我的订单，并跳转至我的订单页面显示
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		PageBean pageBean = productService.findAllOrders(user.getId(),currentPage,currentCount,time,word);
		System.out.println("pageBeanList = " + JSON.toJSONString(pageBean.getList()));
		
		System.out.println("------- num -- word -------");
		System.out.println("num = " + time + ",word = " + word);
		
		request.setAttribute("pageBean", pageBean);
		request.setAttribute("num", time);
		request.setAttribute("word", word);
		
		return "order_list";
	}
	/**
	 * 进入品牌列表页面
	 * 
	 * @author zhangwz
	 * @throws SQLException 
	 * @2018年5月13日下午3:45:37
	 */
	@RequestMapping(value = "/brand_list", method = RequestMethod.GET)
	public String enterBrand(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		System.out.println("------- brand_list -------");
		
		//品牌id
		String bid = request.getParameter("bid");
		String word = request.getParameter("word");//获取输入值
		//获取传入标志：1-carrier.crid 2-category.cid
		String flag = request.getParameter("flag");
		
		System.out.println("flag = " + flag);
		
		String currentPageStr = request.getParameter("currentPage");
		if(currentPageStr==null) currentPageStr="1";
		int currentPage = Integer.parseInt(currentPageStr);
		int currentCount = 20;
		//pageBean中封装product和brand表中的数据
		PageBean pageBean;
		if (word == null) {
			pageBean = productService.findBrandListByBid(flag,bid,currentPage,currentCount);
		} else {
			pageBean = productService.findBrandFromResult(flag,bid,word,currentPage,currentCount);
		}
		
		System.out.println("pageBean = " + JSON.toJSONString(pageBean));
		
		request.setAttribute("pageBean", pageBean);
		request.setAttribute("bid", bid);
		request.setAttribute("flag", flag);
		
		return "brand_list";
	}
	/**
	 * 首页产品搜索功能
	 * 
	 * @author zhangwz
	 * @throws SQLException 
	 * @2018年5月13日下午10:33:07
	 */
	@RequestMapping(value = "/product_list", method = RequestMethod.GET)
	public String productList(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		System.out.println("------- product_list -------");
		
		//商品搜索输入项
		String name = request.getParameter("name");
		String word = request.getParameter("word");//结果中输入值
		
		String currentPageStr = request.getParameter("currentPage");
		if(currentPageStr==null) currentPageStr="1";
		int currentPage = Integer.parseInt(currentPageStr);
		int currentCount = 12;
		
		//pageBean中封装product和brand表中的数据
		PageBean pageBean;
		
		if (word != null) {
			pageBean = productService.findProductFromResult(name,word,currentPage,currentCount);
		} else {
			pageBean = productService.findProductListByName(name,currentPage,currentCount);
		}
		
		System.out.println("pageBean = " + JSON.toJSONString(pageBean));
		
		request.setAttribute("pageBean", pageBean);
		request.setAttribute("name", name);
		
		return "product_list";
	}
	
}
