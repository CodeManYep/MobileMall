<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html class="">
<head>
	<meta charset="UTF-8">
	<title>我的购物车-商城</title>
	
	<c:set var="ctx" value="${pageContext.request.contextPath}" />
  	
  	<link rel="icon" href="${ctx}/static/resource/img/logo.icon" type="image/x-icon">
  	
  	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/cart/css/ui-base.css" />
  	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/cart/css/shortcut.css" />
  	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/cart/css/global-header.css" />
  	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/cart/css/myjd.css" />
  	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/cart/css/nav.css" />
  	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/cart/css/shoppingcart.css" />
  	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/cart/css/global-footer.css" />
  	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/cart/css/common.css" />
  	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/cart/css/header-2017.css" />
  	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/cart/css/cart-sidebar.css" />
  	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/cart/css/cart-sidebar-follow.css" />
  	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/cart/css/cart-filter-bar.css" />
  	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/cart/css/cart-similar.css" />
  	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/cart/css/cart-gift.css" />
  	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/cart/css/order-combined.css" />
  	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/cart/css/unmarket.css" />
  	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/cart/css/cart-inner-new.css" />
  	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/cart/css/cart-toolbar-new.css" />
  	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/cart/css/cart-removed.css" />
  	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/cart/css/cart-full.css" />
  	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/cart/css/cart-smart.css" />
  	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/cart/css/cart-tabs-new.css" />
  	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/cart/css//p-detect.css" />
  	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/cart/css/backpanel.css" />
  	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/cart/css/order-cross.css" />
  	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/cart/css/no-login.css" />
  	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/cart/css/area.css" />
  	<style type="text/css">
 			.fixed-bottom{
  			position:fixed;
  			bottom:0px;
  			z-index:999
 			}
	</style>
  	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/cart/css/tips.css" />
  	<link type="text/css" rel="stylesheet" href="${ctx}/static/resource/css/common.css" />
  	<link type="text/css" rel="stylesheet" href="${ctx}/static/resource/layui/css/layui.css" />
	
	<style type="text/css">
		.jdm-toolbar .jdm-toolbar-footer .jdm-tbar-tab-survey .tab-ico {
			display: none;
			_display:none;
		}
		
		.jdm-toolbar .jdm-toolbar-footer .jdm-tbar-tab-survey .tab-text {
			left: 0px;
			width: 35px;
			height:30px;
			padding:2px 0 3px;
			line-height: 15px;
			background: #c81623;
			_display:block;
		 }
	</style>
	
	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/login/css/base.css" />
	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/login/css/common.css" />
	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/login/css/foreign-number-layer-170524.css" />
	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/login/css/login-banner.css" />
	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/login/css/login-form-2016-1124.css" />
	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/login/css/safe-step.css" />
	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/login/css/tinyscrollbar-170524.css" />
	<link type="text/css" rel="stylesheet" href="${ctx}/static/dist/login/css/login-form-box-2016-1124.css" />
	
	<script type="text/javascript" src="${ctx}/static/resource/js/jquery-3.3.1.min.js"></script>
	<script type="text/javascript" src="${ctx}/static/dist/cart/js/imageMap.js"></script>
	<script type="text/javascript" src="${ctx}/static/resource/layui/layui.js"></script>
	<script type="text/javascript" src="${ctx}/static/dist/cart/js/cart.js"></script>
</head> 
<body> 
	<!-- 引入header.jsp -->
	<jsp:include page="header-cart.jsp"></jsp:include> 
	
  	<!-- main --> 
  	<div id="container" class="cart"> 
   		<div class="w"> 
	    	<div id="chunjie" class="mb10"></div>
	    	
	    	<!--  购物车中没有商品   begin -->
		    <c:if test="${empty cart.cartItems}">
				<div class="cart-empty">
					<div class="message">
						<ul>
							<c:if test="${empty user}">
								<li class="txt">购物车内暂时没有商品，登录后将显示您之前加入的商品</li>
								<li>
									<a href="#none" onclick="loginDialog(1)" class="btn-1 login-btn mr10">登录</a>
									<a href="${ctx}/user/index" class="ftx-05">去购物&gt;</a>
								</li>
							</c:if>	
							
							<c:if test="${!empty user}">
								<li class="txt">购物车空空的哦~，去看看心仪的商品吧~</li>
								<li>
									<a href="${ctx}/user/index" class="ftx-05">去购物&gt;</a>
								</li>
							</c:if>
						</ul>
					</div>	
				</div>
			</c:if>
			<!--  判断购物车中是否有商品   end -->
			
			<!--  购物车中有商品   begin -->
			<c:if test="${!empty cart.cartItems}">
				<c:if test="${empty user}">
			    	<div class="nologin-tip"> 
				    	<span class="wicon"></span> 您还没有登录！登录后购物车的商品将保存到您账号中 
				    	<a class="btn-1 ml10" href="#none" onclick="loginDialog(1)">立即登录</a> 
				    </div> 
			    </c:if>
			    
			    <div class="cart-filter-bar"> 
				    <ul class="switch-cart"> 
					    <li class="switch-cart-item curr"> 
						    <a href="//cart.jd.com/cart.action"> 	
							    <em>全部商品</em> 
							    <span class="number"></span> 
						   	</a> 
						</li> 
				    </ul> 
				    <div class="cart-store"> 
				    	<span class="label">商品详情</span>
				    </div> 
				    <div class="clr"></div> 
				    <div class="w-line"> 
				    	<div class="floater" style="width: 79px; left: 0px;"></div> 
				    </div> 
				    <div class="tab-con ui-switchable-panel-selected" style="display: block;"></div> 
				    <div class="tab-con hide" style="display: none;"></div> 
			    </div> 
	   		</div> 
			<div class="cart-warp"> 
			<div class="w"> 
			<div id="jd-cart"> 
			<div class="cart-main cart-main-new"> 
			<div class="cart-thead"> 
				<div class="column t-checkbox"> 
					<div class="cart-checkbox"> 
						<input type="checkbox" id="toggle-checkboxes_up" name="toggle-checkboxes" class="jdcheckbox" /> 
						<label class="checked" for="">勾选全部商品</label> 
					</div>全选 
				</div> 
				<div class="column t-goods">商品 </div> 
				<div class="column t-props"></div> 
				<div class="column t-price">单价</div> 
				<div class="column t-quantity">数量 </div> 
				<div class="column t-sum">小计</div> 
				<div class="column t-action">操作</div> 
			</div> 
			
			<div id="cart-list"> 
				<div class="cart-item-list" id="cart-item-list-01"> 
					<div class="cart-tbody" id="vender_220891">
						<c:set var="count" value="1"></c:set>
						<c:forEach items="${cart.cartItems}" var="entry"> 
							<div class="item-list"> 
								<div class="item-single  item-item item-selected" id="product_26669050046" num="1"> 
									<div class="item-form"> 
										<div class="cell p-checkbox"> 
											<div class="cart-checkbox"> 
												<input type="checkbox" name="checkItem" value="26669050046_1" class="jdcheckbox" /> 
												<label for="" class="checked">勾选商品</label> 
												<span class="line-circle"></span> 
											</div> 
										</div> 
										
										<div class="cell p-goods"> 
											<div class="goods-item"> 
												<div class="p-img"> 
													<a href="${ctx}/product/product_info?method=productInfo&pid=${entry.value.product.pid}" target="_blank" class="J_zyyhq_26669050046">
														<img id="cart-img${count}" alt="${ctx}/${entry.value.product.pname}/${count}" src="" />
													</a> 
												</div> 
								                
												<div class="item-msg"> 
													<div class="p-name"> 
														<a href="${ctx}/product/product_info?method=productInfo&pid=${entry.value.product.pid}" target="_blank">${entry.value.product.pdesc}</a> 
													</div> 
													<div class="p-extend p-extend-new"> 
														<span class="promise return-y" title="支持7天无理由退货">
															<i class="return-y-icon"></i>
															<a href="#none" class="ftx-08">支持7天无理由退货</a>
														</span> 
													</div> 
												</div> 
											</div> 
										</div> 
								        <!-- 后续数据库中有这些信息的话可以加上 -->
								        <div class="cell p-props p-props-new"> 
								        	<!-- <div class="props-txt" title="宝石蓝">颜色：宝石蓝 </div> 
								        	<div class="props-txt" title="全网通 6G+64G">尺码：全网通 6G+64G</div> --> 
								        </div> 
								        
								        <div class="cell p-price p-price-new "> 
									        <strong>&yen;${entry.value.product.shop_price}</strong> 
									        <div> 
									        	<div class="clr"></div> 
									        </div> 
									        <p class="mt5" jj=""></p> 
									        <p class="mt5" bt=""></p> 
								        </div> 
								        
								        <div class="cell p-quantity"> 
									        <div class="quantity-form"> 
									        	<input autocomplete="off" type="text" class="itxt" value="${entry.value.buyNum}" minnum="1" /> 
									        </div> 
								        </div>
								         
								        <div class="cell p-sum" id="subtotal${count}" pid="${entry.value.product.pid}"> 
								        	<strong>&yen;${entry.value.subtotal}</strong> 
								        </div> 
								        
								        <div class="cell p-ops"> 
								        	<a onclick="delProFromCart('${entry.value.product.pid}')" class="cart-remove" href="javascript:void(0);">删除</a> 
								        </div> 
							        </div> 
						        	<div class="item-line"></div> 
						        </div> 
					        </div>
					        <!-- 拼图片路径URL begin -->
			                <script type="text/javascript">
				                $(function() {
				                	  var count = ${count};
				                	  var imageAltArr = $("#cart-img"+ count +"").attr("alt").split("/");
				                	  var ctx = imageAltArr[1];
				                	  var pname = imageAltArr[2];
				                	  console.log("pname = " + pname);
				                	  var url = imageMap[pname];
				                	  var imageUrl = "/" + ctx + "/" + url;
				                	  console.log("imageUrl = " + imageUrl);
				                	  $("#cart-img"+ count +"").attr("src",imageUrl);
				                });
			                </script>
			                <!-- 拼图片路径URL end -->
				        	<c:set var="count" value="${count+1}" />
				        </c:forEach> 
			        </div> 
		        </div> 
		        
		        <script type="text/javascript">
			        $(function() {
			        	$("input[name='checkItem']").click(function() {
			        		  var count = 1;
			        		  var array= new Array();
			              	  $("input[name='checkItem']").each(function(){
			              		  	//获取选中状态的小计
			            			if (this.checked) {
			            				var subTotal = $("#subtotal"+ count +"").text().trim();
			            				console.log("subTotal = " + subTotal);
			            				console.log("length = " + subTotal.length);
			            				var money = subTotal.substring(1,subTotal.length);
			            				console.log("money = " + money);
			            				array.push(money);
			            				console.log("array = " + array);
			            			}
			              		  	
			            			if (this.checked != true) {
			            				var pid = $("#subtotal"+ count +"").attr("pid");
			            				var projectName = $("#projectName").val();//${ctx}
			            				console.log("pid = " + pid);
			            		     	location.href="${ctx}/product/unCheckedFromCart?method=unCheckedFromCart&pid="+pid;
			            		     	/* $.ajax({
			            			        type: "post",
			            			        url: projectName + "/product/unCheckedFromCart?method=unCheckedFromCart",
			            			        data: {
			            			        	"pid":pid
			            			        },
			            			        cache: false,
			            			        async : false,
			            			        dataType: "text",
			            			        success: function (data){
			            			        	if (data == "ok") {
			            			        		
			            						} else {
			            							
			            						}
			            			        },
			            			        error:function () {      
			            			            alert("抱歉，处于未选中状态失败");
			            			        }
			            			     });*/
			            			} 
			            			
			            		    count ++;
			            		    
			            		    //重新计算总计
			            		    var total = 0.0;
			            		    total = parseFloat(total);
			            		    for (var i = 0; i < array.length; i++) {
										total = total + parseFloat(array[i]);
									}
			            		    total = parseFloat(total);
			            		    //total = total.toFixed(1);
			            		    console.log("total = " + total.toFixed(1));
			            		    $("#resultTotal").html("<em>&yen;"+ total.toFixed(1) +"</em>"); 
			            		    
			            	  });
			          	});
			        	
			        	//全选按钮点击后商品总计重新计算
			        	var lastTotal = ${cart.total};
			        	$("input[name='toggle-checkboxes']").click(function() {
			        		lastTotal = parseFloat(lastTotal);
			        		
			        		var total = 0.0;
	            		    total = parseFloat(total);
	            		    
			        		if (this.checked) {
			        			$("#resultTotal").html("<em>&yen;"+ lastTotal.toFixed(1) +"</em>");
			        		} else {
			        			$("#resultTotal").html("<em>&yen;"+ total.toFixed(1) +"</em>");
							}
			        	});
					});
		        </script>
		        
	        	<input type="hidden" id="isSsgdg" value="0" /> 
	        </div> 
			</div> 
			</div>
			 
			<div id="cart-floatbar"> 
				<div class="ui-ceilinglamp-1" style="width: 990px; height: 52px;">
					<div class="cart-toolbar" style="width: 988px; height: 50px;"> 
						<div class="toolbar-wrap"> 
							<div class="options-box"> 
								<div class="select-all"> 
									<div class="cart-checkbox"> 
										<input type="checkbox" id="toggle-checkboxes_down" name="toggle-checkboxes" class="jdcheckbox" /> 
										<label class="checked" for="">勾选全部商品</label> 
									</div>全选 
								</div>
								 
								<div class="operation"> 
									<a href="#none" class="remove-batch" onclick="delChooseProFromCart()">删除选中的商品</a> 
									<a href="#none" class="cleaner-opt J_clr_all" onclick="clearCart()">清空购物车</a> 
								</div> 
								
								<div class="clr"></div>
								 
								<div class="toolbar-right"> 
									<div class="normal"> 
										<div class="comm-right"> 
											<div class="btn-area"> 
												<c:if test="${!empty user}">
													<a onclick="submitOrder()" target="_blank" class="submit-btn" data-bind="1"> 
														去结算<b></b>
													</a> 
												</c:if>
												
												<c:if test="${empty user}">
													<a onclick="loginDialog(2)" target="_blank" class="submit-btn" data-bind="1"> 
														去结算<b></b>
													</a> 
												</c:if>
											</div>
											 
											<div class="price-sum"> 
												<div> 
													<span class="txt txt-new">总价：</span> 
													<span class="price sumPrice" id="resultTotal"><em>&yen;${cart.total}</em></span> 
													<b class="ml5 price-tips"></b> 
												</div> 
											</div> 
											
											<div class="clr"></div> 
										</div> 
									</div> 
								</div> 
							</div> 
						</div> 
					</div>
				</div> 
			</div> 
			</div> 
			</div>
			</c:if> 
		<!--  购物车中有商品  end -->
	    <div class="w"> 
	   		<div id="cart-smart"></div> 
	    </div>
	    
   		<!-- 商品浏览历史-最近浏览，后续添加商品推荐功能 begin -->
		<div class="w">
			<div class="m m1" id="c-tabs-new">
			    <div class="mt">
			        <div class="extra-l">
			            <a href="#none" class="c-item curr">最近浏览</a>
			        	<a href="#none" class="c-item">猜你喜欢</a>
			        </div>
			        
			        <div class="extra-r"></div>
			    </div>
			    
			    <div class="mc c-panel-main" style="position: relative;">
			    	<div class="c-panel" id="guess-products" style="position: absolute; z-index: 1; opacity: 1;">
		    			<div class="mc c-panel-main" style="position: relative;">    
		    				<div class="goods-list c-panel ui-switchable-panel-selected" style="position: absolute; z-index: 1; opacity: 1;">      
		    					<ul> 
		    						<c:forEach items="${historyProductList }" var="historyPro"> 
		    							<li>
		    								<div class="item">
		    									<div class="p-img">
		    										<a target="_blank" href="${ctx}/product/product_info?method=productInfo&pid=${historyPro.pid}">
		    											<img width="160" height="160" alt="${historyPro.pdesc}" src="${ctx}/${historyPro.pimage}">
		    										</a>
		    									</div>
		    									
		    									<div class="p-name">              
			    									<a target="_blank" href="href="${ctx}/product/product_info?method=productInfo&pid=${historyPro.pid}"">${historyPro.pdesc}</a>
		    							        </div>            
		    							        
		    							        <div class="p-price">              
		    							        	<strong><em>￥</em><i>${historyPro.shop_price}</i></strong>            
		    							        </div>            
		    							        
		    							        <div class="p-btn">              
		    							        	<a href="#none" onclick="addCart('${historyPro.pid}')" pid="${historyPro.pid}" class="btn-append"><b></b>加入购物车</a>            
		    							        </div>
		    								</div>
		    							</li> 
		    						</c:forEach>      
								</ul>
								<script type="text/javascript">
				            		//加入购物车
				            		function addCart(pid){
				            			//商品的数量=1
				            			location.href="${ctx}/product/addCart?method=addProductToCart&pid=" + pid + "&buyNum="+1;
				            		}
				            		//删除单一商品 
				            	    function delProFromCart(pid){
				            	    	if(confirm("您是否要删除该商品？")){
				            	    		location.href="${ctx}/product/delProFromCart?method=delProFromCart&pid="+pid;
				            	    	}
				            	    }
				            		//删除选中的商品
				            		function delChooseProFromCart() {
				            			
				            			var count = 1;
						        		var pid = "";
						        		
					              	  	$("input[name='checkItem']").each(function(){
					              		  	//获取选中状态商品的pid
					            			if (this.checked) {
					            				pid = pid + $("#subtotal"+ count +"").attr("pid") + ",";
					            			}
					            		    count ++;
						            	});
					              	  	
					              	  	var result = pid.substring(0,(pid.length-1));
				            			if(confirm("您是否要删除选中的商品？")){
				        					location.href="${ctx}/product/delChoosePro?method=delChoosePro&result=" + result;
				        				}
									}
				            		//清空购物车
				            	    function clearCart(){
				        				if(confirm("您是否要清空购物车？")){
				        					location.href="${ctx}/product/clearCart?method=clearCart";
				        				}
				        			}
			            		</script>
							</div>
					   </div>
					</div>	
				</div>
			</div>	
		</div>
		<!-- 商品浏览历史-最近浏览，后续添加商品推荐功能 end --> 
   		<div class="w"></div> 
   </div> 
   <!-- main --> 
   
   <!-- 登录对话框  begin -->
   <div class="ui-dialog" id="loginDialogBody" style="width: 410px; position: fixed; display: none; overflow: hidden; top: 0px; left: 0px;">
		<div class="ui-dialog-title" style="width: 390px;">      
			<span></span>     
		</div>    
		<input type="hidden" id="projectName" value="${ctx}" />
		<input type="hidden" type="text" id="unique" value="" style="display: none;"/>
		<div class="ui-dialog-content" style="height: 448px; width: 390px; overflow: hidden;">
			<div id="dialogIframe" name="dialogIframe" style="border: 0px; width: 100%; height: 100%;margin-left: 17px;margin-top: 53px;">
				<!-- 引入loginDialog.jsp -->
				<jsp:include page="loginDialog.jsp"></jsp:include>
			</div>
		</div>
		
		<a class="ui-dialog-close" title="关闭">
			<span class="ui-icon ui-icon-delete"></span>
		</a>
	</div>
   <!-- 登录对话框  end -->
  
	<!--footer start--> 
	<!-- 引入footer.jsp -->
	<jsp:include page="footer-proInfo.jsp"></jsp:include> 
	<!--footer end-->
 </body>
</html>