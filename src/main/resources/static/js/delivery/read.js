
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
	
	
	function allOrder(data) {
		let storeNum = getParameterByName('num');
		
		$.ajax({
			url : "allOrder",
			type : "get",
			data : {"data" : data, "storeNum" : storeNum},
			success : function() {
				alert("주문에 성공했습니다.");
				resultMenu();
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
					
				/* var display = [[@{itemDisplay}]];
				console.log(display); */
				
				let htmlStr = "";
				
				$.each(data, function(index, item) {
					htmlStr += "<table border='1' class='menuTable'>";
					htmlStr += "<tr>"; 
					/* htmlStr += "<td><img src='" + display + "'></td>"; */
					htmlStr += "<td><a>" + item.itemName + "</a></td>";
					htmlStr += "<td>" + item.itemContent + "</td>";
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
