/**
 *  @author zhangwz
 * 2018年4月26日 下午17:23:04
 */
$(function() {
	//上面全选-全选、取消全选
	$("#toggle-checkboxes_up").click(function(){
		if(this.checked){  
			$("input[name='checkItem']").prop("checked", true);
			$("input[name='toggle-checkboxes']").prop("checked", true);
		}else{   
			$("input[name='checkItem']").prop("checked", false);
			$("input[name='toggle-checkboxes']").prop("checked", false);
		}   
	});
	
	//下面全选-全选、取消全选
	$("#toggle-checkboxes_down").click(function(){
		if(this.checked){  
			$("input[name='checkItem']").prop("checked", true);
			$("input[name='toggle-checkboxes']").prop("checked", true);
		}else{   
			$("input[name='checkItem']").prop("checked", false);
			$("input[name='toggle-checkboxes']").prop("checked", false);
		}   
	});
	
	$("input[name='checkItem']").click(function() {
		$(this).attr('checked', 'false');
		if (all()) {
			$("input[name='toggle-checkboxes']").prop("checked", true);
		} else {
			$("input[name='toggle-checkboxes']").prop("checked", false);
		}
	});
	
	//判断子选框全部选中则全选复选框处于选中状态
	function all() {
		var n = 0;
		$("input[name='checkItem']").each(function(){
			if (this.checked) {
				n ++;
			} 
		});
		
		if (n == $("input[name='checkItem']").length) {
			return true;
		} else {
			return false;
		}
	}
	
	//初始默认选中状态
	$("input[name='checkItem']").each(function(){
		  $(this).attr('checked', 'true');
	});

	$("input[name='toggle-checkboxes']").each(function(){
		  $(this).attr('checked', 'true');
	});
});

//弹出登录对话框
function loginDialog(id) {
	$("#unique").val(id);
	var test = $("#unique").val();
	layui.use(['layer'], function(){
    	  var layer = layui.layer;
    	  layer.open({
      	  type: 1,
      	  area: ['410px', '499px'],
      	  title: "您尚未登录",
      	  fixed: true, //固定
      	  maxmin: true,
      	  content: $("#loginDialogBody"),
      	  //btn: ['确定','关闭'],
      	  yes: function(index){
              layer.close(index);
          },
          
          cancel: function(){
            //右上角关闭回调
          }
    	  });
	});
}

/**
 * 弹框-用户登录
 * 
 */

$(function() {
	//大写CapsLock键按下提示
	document.getElementById("nloginpwd").onkeypress = detectCapsLock;
	function detectCapsLock(event) {
		var e = event||window.event;
		var tip = document.getElementById("capsLockTip");
		var keyCode = e.keyCode||e.which; //按键的keyCode 
		var isShift = e.shiftKey||(keyCode == 16 )||false ; //shift键是否按住
		//CapsLock打开，且没有按住shift键 				//CapsLock打开，且按住shift键
		if (((keyCode >= 65&&keyCode <= 90)&&!isShift)||((keyCode >= 97&&keyCode <= 122 )&&isShift)){
			tip.style.display = "inline";
		} else {
			tip.style.display = "none";
		}
	}
	
	//账号和密码合法性检验
	function checkLogin() {
		var loginname = $("#loginname").val();//登录名
		var password = $("#nloginpwd").val();//密码
		
		var loginTip = document.getElementById("loginTip");
		
		if (loginname == "" && password == "") {
			loginTip.className = "msg-error";
			loginTip.innerHTML = "<b></b>请输入账户名和密码";
			return false;
		} else if (password == "") {
			loginTip.className = "msg-error";
			loginTip.innerHTML = "<b></b>请输入密码";
			return false;
		} else if (loginname == "") {
			loginTip.className = "msg-error";
			loginTip.innerHTML = "<b></b>请输入账户名";
			return false;
		} else {
			loginTip.className = "msg-error hide";
			loginTip.innerHTML = "<b></b>";
			return true;
		}
		
	}
	
	//验证用户是否存在
	$("#loginsubmit").click(function() {
		if (checkLogin()) {
			var loginname = $("#loginname").val();//登录名
			var password = $("#nloginpwd").val();//密码
			var projectName = $("#projectName").val();//${ctx}
			
			var flag = $("#unique").val();//判断是登录还是点击的去结算
			
			$.ajax({
		        type: "post",
		        url: projectName + "/user/login.do",
		        data: {
		        	"loginname":loginname,
		        	"password":password
		        },
		        cache: false,
		        async : false,
		        dataType: "text",
		        success: function (data){
		        	if (data == "ok") {
		        		if (flag == "2") {
		        			location.href = "cart";
						} else {
							location.href = "cart";
						}
					} else {
						var loginTip = document.getElementById("loginTip");
						loginTip.className = "msg-error";
						loginTip.innerHTML = "<b></b>账户名或密码错误";
						return false;
					}
		        },
		        error:function () {      
		            alert("抱歉，登录失败，可能尚未注册");
		        }
		     });
		} else {
			alert("fail");
			return false;
		}
	});
	
});
/**
 * 
 * 购物车中全部处于未选中的状态下不跳转至结算页面
 * 
 * @author zhangwz
 * @2018年5月19日下午4:17:53
 */
function submitOrder() {
	var projectName = $("#projectName").val();//${ctx}
	
	//商品处于全部未选中状态则不允许跳转至结算界面
	if (flag()) {
		alert("请您至少选中一项商品");
		return false;
	} else {
		location.href = projectName + "/product/order_info?method=submitOrder";
	}

}

//全部未选中状态的时候返回true
function flag() {
	$("input[name='checkItem']").each(function(){
		//全部未选中状态的时候返回true
		if (this.checked != true) {
			return true;
		}
	});
	
}