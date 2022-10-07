
$(document).ready(function() {
		
		$("#searchWord").on("keyup", function() { load(); });
		
		$("#menuButton").on("click", function() { 
			$("#info").hide();
			$("#review").hide();
			$("#menu").show();
		});
		
		$("#reviewButton").on("click", function() { 
			$("#menu").hide();
			$("#info").hide();
			$("#review").show();
		});
		
		$("#infoButton").on("click", function() { 
			$("#menu").hide();
			$("#review").hide();
			$("#info").show();
		});
		
		$("#wish").on("click", function() { clickWish(); });
		
		$("#review").hide();
		$("#info").hide();
		load();
		resultMenu();
		printReview();
		loadWish();
		$("#infoButton").click(function() {loadMap();});
		
		
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
		$("#leftoverPoint").hide();
		let htmlStr = "";
		$.ajax({
			url : "checkAddress",
			type : "get",
			data : {"loginUser" : loginUser},
			success : function(data) {
				
				htmlStr = "<h3>이 주소가 맞습니까?</h3>";
				htmlStr += "<h4>" + data.address + "</h4>";
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
							htmlStr = "<table id='orderList'>";
							htmlStr += "<tr><th>주문서</th></tr>";
							htmlStr += "<tr><th>잠시만 기다려주세요.</th></tr>";
							htmlStr += "<tr><th>" + data.orderHistory + "</th></tr>";
							htmlStr += "<tr><th>" + data.totalAmount + "원</th></tr>";
							htmlStr += "</table>";
							
							
							$("#checkBtn").hide();
							$("#receiptModal").html(htmlStr);
							$("#leftoverPoint").show();
							leftoverPoint();
							$("#staticBackdrop").modal('show');
						} else {
							htmlStr = "<h3>잔액이 모자랍니다.</h3>"
							
							$("#checkBtn").hide();
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
				
				let htmlStr = "<h1>잔여 포인트 : " + data.memberPoint + " 포인트</h1>";
				
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
				
				let htmlStr = "<h1 style='border-top: 2px solid #C98474' >총금액</h1>";
				htmlStr += "<input type='button' onclick='allOrder(" + data + ")' value='주문' class='order-btn'>";
				htmlStr += "<h2>" + data.toLocaleString('ko-kr') + "원</h2>";
				
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
					htmlStr += "<tr class='receiptTitle-rightSide'>"
					htmlStr += "<td>메뉴명</td>";
					htmlStr += "<td style='text-align:center'>가격</td>";
					htmlStr += "<td colspan='3' style='text-align:center'>수량</td>";
					htmlStr += "</tr>";
					
				$.each(data, function(index, item) {
					
					htmlStr += "<tr class='receiptInfo-rightSide'>"; 
					htmlStr += "<td  style='text-align:center'>" + item.itemName + "</td>";
					htmlStr += "<td  style='text-align:center'>" + item.itemPrice + "원</td>";
					htmlStr += "<td><input type='button' value='-' onclick='minus(" + item.orderNum + ")'></td>";
					htmlStr += "<td style='text-align:center'>" + item.quantity + "개</td>";
					htmlStr += "<td><input type='button' value='+' onclick='plus(" + item.orderNum + ")'></td>";
					htmlStr += "<td><input type='button' value='X' onclick='deleteOrder(" + item.orderNum + ")'></td>";
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
					htmlStr += "<table class='menuTable'>";
					htmlStr += "<tr>"; 
					htmlStr += "<td class='menu-info'> "+  item.itemName + "</td>";
					htmlStr += "</tr>"; 
					
					htmlStr += "<tr>"; 
					htmlStr += "<td class='menu-info' style='color: #999999;'>" + item.itemContent + "</td>";
					htmlStr += "</tr>"; 
					
					htmlStr += "<tr>"; 
					htmlStr += "<td class='menu-info'>" + item.itemPrice.toLocaleString('ko-KR') + "원</td>";
					htmlStr += "</tr>"; 
					
					htmlStr += "<tr>"; 
					htmlStr += "<td><img alt='구매하기' src='/images/deliveryImages/cart.png'class='cart-icon'  id='item" + item.itemNum + "' onclick='checkItem(" + item.itemNum + ");'></td>";
					htmlStr += "<td><img src='itemDisplay?num=" + item.itemNum + "' width='80px;' class='menu-img'></td>"; 
					htmlStr += "</tr>";
					htmlStr += "</table>"
					htmlStr += "<hr>"
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
	
	
	
