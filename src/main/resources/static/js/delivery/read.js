
$(document).ready(function() {
		$("#searchWord").on("keyup", function() { load(); });
		
		$("#menuButton").on("click", function() { 
			$("#menu").show();
			$("#info").hide();
			$("#review").hide();
		});
		
		$("#reviewButton").on("click", function() { 
			$("#menu").hide();
			$("#info").hide();
			$("#review").show();
		});
		
		$("#infoButton").on("click", function() { 
			$("#menu").hide();
			$("#info").show();
			$("#review").hide();
		});
		
		$("#wish").on("click", function() { clickWish(); });
		
		$("#review").hide();
		$("#info").hide();
		load();
		resultMenu();
		printReview();
		loadWish();
		loadMap();
		
	});
	
	function loadWish(){
		let storeNum = getParameterByName('num');
		let loginUser = $("#loginUser").val();
		
		console.log(loginUser);
		$.ajax({
			url : "loadWish",
			type : "get",
			data : {"storeNum" : storeNum},
			success : function(data) {
				console.log(data);
				
				if(data.memberId == loginUser) {
					$("#wish").val("♥");
				} else {
					$("#wish").val("♡");
				}
				
			}
		});
	}
	
	function clickWish(){
		let storeNum = getParameterByName('num');
		
		$.ajax({
			url : "clickWish",
			type : "get",
			data : {"storeNum" : storeNum},
			success : function(data) {
				
				loadWish();
				$("#countWishlist").html(data.wishlist);
			}
		});
	}
	
	function printReview() {
		let storeNum = getParameterByName('num');
		
		$.ajax({
			url : "printReviewAjax",
			type : "get",
			data : {"storeNum" : storeNum},
			success : function(data) {
				
				let htmlStr = "";
				
				$.each(data, function(index, item) {
					htmlStr += "<table border='1' class='menuTable'>";
					htmlStr += "<tr><td colspan='2'>" + item.nickname + "님</td></tr>";
					htmlStr += "<tr><td colspan='2'>" + item.orderHistory + "</td></tr>";
					htmlStr += "<tr>"; 
					htmlStr += "<td>" + item.reviewContent + "</td>";
					htmlStr += "<td>" + item.rating + "점</td>";
					htmlStr += "</tr>";
					htmlStr += "</table>"
				});
				$("#review").html(htmlStr);
			}
		});
	}
	
	
	function allOrder(orderData) {
		let storeNum = getParameterByName('num');
		let loginUser = $("#loginUser").val();
		let htmlStr = "";
		$.ajax({
			url : "checkAddress",
			type : "get",
			data : {"loginUser" : loginUser},
			success : function(data) {
				
				htmlStr = "<h2 class = 'addressBox'>이 주소가 맞습니까?</h2>";
				htmlStr += "<h4 class = 'addressBox'>" + '&#127969;' + data.address + "</h4>";
				$("#receiptModal").html(htmlStr);
				$("#staticBackdrop").modal('show');
				
				$("#checkBtn").click(function() {
					
					$.ajax({
					url : "allOrder",
					type : "get",
					data : {"data" : orderData, "storeNum" : storeNum, "address" : data.address},
					success : function(data) {
						console.log(data);
						resultMenu();
						if(data != "") {
							// 1005 세련 수정함 
							htmlStr = "<table id='orderList'>";
							htmlStr += "<td id = 'orderBox'>";
							htmlStr += "<tr><th class = 'orderTitle'></th></tr>";
							htmlStr += "<tr><th>주문목록 : " + data.orderHistory + "</th></tr>";
							htmlStr += "<tr><th>합계 : " + data.totalAmount + "원</th></tr>";
							htmlStr += "</td>";
							htmlStr += "</table>";
							
							
							$("#receiptModal").html(htmlStr);
							leftoverPoint();
							$("#staticBackdrop").modal('show');
						} else {
							// 1005 세련 수정함
							htmlStr = "<h3 class = 'errormsg'>포인트가 모자랍니다.</h3>"
							
							$("#receiptModal").html(htmlStr);
							$("#staticBackdrop").modal('show');
						}
					}
				});
					
				})
			}
		});
		
		
		
	}
	
	
	function leftoverPoint() {
		
		$.ajax({
			url : "leftoverPoint",
			type : "get",
			success : function(data) {
				console.log(data);
				// 1005 세련 수정함
				let htmlStr = "<h3>잔여 포인트 : " + data.memberPoint + " point</h3>";
				
				$("#leftoverPoint").html(htmlStr);
			}
		});
	}
	
	
	
	
	//onclick="ChooseCategory()"
	function orderPrice() {
		let storeNum = getParameterByName('num');
		
		$.ajax({
			url : "orderPrice",
			type : "get",
			data : {"storeNum" : storeNum},
			success : function(data) {
				
				let htmlStr = "<h1>총금액</h1>";
				htmlStr += "<h2>" + data + "원</h2>";
				htmlStr += "<input type='button' onclick='allOrder(" + data + ")' value='주문'>";
				
				$(".aside-right-allPay").html(htmlStr);
			}
		});
	}
	
	function ChooseCategory() {
		let storeNum = getParameterByName('num');
		
		$.ajax({
			url : "orderPrice",
			type : "get",
			data : {"storeNum" : storeNum},
			success : function(data) {
				
				let htmlStr = "<h1>총금액</h1>";
				htmlStr += "<h2>" + data + "원</h2>";
				htmlStr += "<input type='button' onclick='allOrder(" + data + ")' value='주문'>";
				
				$(".aside-right-allPay").html(htmlStr);
			}
		});
	}
	
	function deleteOrder(num) {
		
		$.ajax({
			url : "deleteOrder",
			type : "get",
			data : {"orderNum" : num},
			success : function(data) {
				resultMenu();
			}
		});
	}
	
	
	function plus(orderNum) {

		$.ajax({
			url : "plusOrder",
			type : "get",
			data : {"orderNum" : orderNum},
			success : function(data) {
				resultMenu();
				orderPrice();
			}
		});
	}
	
	function minus(orderNum) {
		$.ajax({
			url : "minusOrder",
			type : "get",
			data : {"orderNum" : orderNum},
			success : function(data) {
				resultMenu();
				orderPrice();
			}
		});
	}
	
	function getParameterByName(name) {
		  name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
		  var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
		  results = regex.exec(location.search);
		  return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
	}
	
	function orderMenu(num) {
		let storeNum = getParameterByName('num');
		let itemNum;
		
		if(typeof num == "undefined") {
			itemNum = 0;
		} else {
			itemNum = num;
		}
		
		$.ajax({
			url : "orderListAjax",
			type : "get",
			data : {"storeNum" : storeNum, "itemNum" : itemNum},
			success : function(data) {
				console.log(data);
				resultMenu();
			}
		});
	}
	
	function resultMenu() {
		let storeNum = getParameterByName('num');
		
		$.ajax({
			url : "reOrderListAjax",
			type : "get",
			data : {"storeNum" : storeNum},
			success : function(data) {
				
				console.log(data);
				
				let htmlStr = "<table>";
				$.each(data, function(index, item) {
					
					htmlStr += "<tr>"
					htmlStr += "<td colspan='5'>" + item.itemName + "</td>";
					htmlStr += "</tr><tr>"; 
					htmlStr += "<td><input type='button' value='X' onclick='deleteOrder(" + item.orderNum + ")'></td>";
					htmlStr += "<td>" + item.itemPrice + "</td>";
					htmlStr += "<td><input type='button' value='-' onclick='minus(" + item.orderNum + ")'></td>";
					htmlStr += "<td>" + item.quantity + "</td>";
					htmlStr += "<td><input type='button' value='+' onclick='plus(" + item.orderNum + ")'></td>";
					htmlStr += "</tr>";
				});
					htmlStr += "</table>"
				
				
				$(".aside-right-order").html(htmlStr);
				orderPrice(); 
			}
		});
	}
	
	
	
	function load() {
		let title = $("#searchWord").val();
		let storeNum = getParameterByName('num');
		
		$.ajax({
			url : "itemListAjax",
			type : "get",
			data : {"title" : title, "storeNum" : storeNum},
			success : function(data) {
				console.log(data);
				
				/* <![CDATA[ */
					
				let htmlStr = "";
				
				$.each(data, function(index, item) {
					htmlStr += "<table border='1' class='menuTable'>";
					htmlStr += "<tr>"; 
					htmlStr += "<td><img src='itemDisplay?num=" + item.itemNum + "' width='80px;'></td>"; 
					htmlStr += "<td><a>" + item.itemName + "</a></td>";
					htmlStr += "<td>" + item.itemPrice + "</td>";
					htmlStr += "<td><input type='button' value='주문' id='item" + item.itemNum + "' onclick='checkItem(" + item.itemNum + ");'></td>";
					htmlStr += "</tr>";
					htmlStr += "</table>"
				});
				
				
				$("#menu").html(htmlStr);
				/* ]]> */
			}
		});
	}
	
	function checkItem(itemNum) {
		let storeNum = getParameterByName('num');
		
		$.ajax({
			url : "checkItem",
			type : "get",
			data : {"storeNum" : storeNum, "itemNum" : itemNum},
			success : function(data) {
				console.log("checkItem 시작");
				console.log(data);
				
				if(data == "") {
					orderMenu(itemNum);
				} else {
					plus(data.orderNum);
				}
			}
		});
	}
	
	function ChooseCategory() {
		$('#modal').modal('show');
	}
	
	
	
