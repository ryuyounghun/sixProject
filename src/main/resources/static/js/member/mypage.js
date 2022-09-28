$(document).ready(function(){
		// 0928 추가
		let loginUser = $("#loginUser").val();
		$("#hiddenMypage").val(loginUser);
		console.log(typeof memberNumber);
		loadAllReply();	
		$("#btnReply_write").on("click", function() { writeReply(); });
		$("#btnReply_update").hide();
		// 0924 추가
		$("#wishlistBtn").click(function() { wishList(); });
		$("#buyCoupon").hide();
		$("#wishlistStore").hide();
		$("#buyCouponsBtn").click(function() {
			$("#couponList").hide();
			$("#wishlistStore").hide();
			$("#orderList").hide();
			$("#profileModal").hide();
			$("#buyCoupon").show();
			$("#exampleModalLabel").text("쿠폰 구매하기");
			$("#exampleModal").modal("show");
		});
		couponList();
		myPoint();
		$("#myCouponListBtn").click(function() {
			$("#wishlistStore").hide();
			$("#buyCoupon").hide();
			$("#orderList").hide();
			$("#profileModal").hide();
			$("#couponList").show();
			$("#exampleModalLabel").text("MyCoupon List");
			$("#exampleModal").modal("show");
		});
		
		// 0925 추가
		$("#buyCouponsBtn").hide();
		$("#myCouponListBtn").hide();
		$("#reviewModal").hide();
		$("#sellerPageBtn").hide();
		$("#updateMemberInfoBtn").hide();
		$("#profileBtn").hide();
		$("#blockUserBtn").hide();
		showMenuList();
		$("#orderListBtn").click(function() { myOrderList(); });
		myReviews();
		
		// 0927 추가 
		$("#sellerPageBtn").click(function() { location.href="/delivery/sellerPage"; });
	
		// 0928 추가
		mypageImage();
		badge();
		$("#profileBtn").click(function() { profileImage(); })
	});
	
	
	// 0928 레벨별 뱃지추가하기
	function badge(){
			let memberLevel = $("#memberLevel").val();
			
			for ( let i =0; i <= 10; i++){
				if(memberLevel == i){
					img_src = "/images/levelBadges/lv"+ i + ".png";
				}
			}
			
			document.getElementById("levelImg").src=img_src;
	}
	
	// 0928 추가
	function profileImage() {
		$("#couponList").hide();
		$("#buyCoupon").hide();
		$("#orderList").hide();
		$("#wishlistStore").hide();
		$("#profileModal").show();
		
		$("#exampleModalLabel").text("프로필사진");
		
		$("#exampleModal").modal("show");
	}
	
	
	// 0928 추가
	function mypageImage() {
		let memberNum = getParameterByName('num');
		
		$.ajax({
			url : "mypageImage",
			type : "get",
			data : {"memberNum" : memberNum},
			success : function(data) {
				console.log(data.savedFile);
				let htmlStr = "";
				
				if(data.savedFile != null) {
					htmlStr += "<img src='memberDisplay?num=" + memberNum + "' width='100px'>";
				} else if (data.savedFile == null){
					htmlStr += "<img src='/images/rabbit.jpg' width='100px'>";
				}
				$("#mainImage").html(htmlStr);
			}
		})
		
		
	}
	
	function getParameterByName(name) {
		  name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
		  var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
		  results = regex.exec(location.search);
		  return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
	}
	
	// 0925 추가
	function showMenuList() {
		let memberNum = getParameterByName('num');
		let loginMember = $("#loginMember").val();
		console.log(memberNum);
		console.log(loginMember);
		if(memberNum == loginMember) {
			$("#buyCouponsBtn").show();
			$("#myCouponListBtn").show();
			$("#sellerPageBtn").show();
			$("#updateMemberInfoBtn").show();
			$("#profileBtn").show();
			$("#blockUserBtn").show();
		} 
		
	}
	
	function myOrderList() {
		$("#couponList").hide();
		$("#buyCoupon").hide();
		$("#wishlistStore").hide();
		$("#profileModal").hide();
		let memberNum = getParameterByName('num');
		
		$.ajax({
			url : "myOrderList",
			type : "get",
			data : {"memberNum" : memberNum},
			success : function(data) {
				let htmlStr = "";
				
				if(data == "") {
					htmlStr += "<h1>주문 목록이 없습니다...</h1>";
				} else {
					$.each(data, function(index, item) {
						htmlStr += "<table>";
						htmlStr += "<tr><th>" + item.receiptNum + "</th>";
						htmlStr += "<th>" + item.totalAmount + "</th>";
						htmlStr += "<th><input onclick='writeReview(" + item.storeNum + "," + item.receiptNum + ")' type='button' value='리뷰쓰기'></th></tr>";
						htmlStr += "<tr><th>" + item.orderHistory + "</th></tr>";
						htmlStr += "</table>";
					});
				}
				
				$("#orderList").html(htmlStr);
				$("#exampleModalLabel").text("주문목록");
				$("#orderList").show();
				$("#exampleModal").modal("show");
				
			}
		});
	}
	
	// 0925 추가
	function writeReview(storeNum, receiptNum) {
		$("#hiddenStoreNum").val(storeNum);
		$("#hiddenReceiptNum").val(receiptNum);
		
		endReview(receiptNum);
	}
	
	// 0925 추가
	function endReview(receiptNum) {
		
		$.ajax({
			url : "/delivery/checkReview",
			type : "get",
			data : {"receiptNum" : receiptNum},
			success : function(data) {
				if(data == "") {
					$("#orderList").hide();
					$("#reviewModal").show();
				} else {
					alert("이미 리뷰 한 주문입니다.");
				}
			}
		});
	}
	
	// 0925 추가
	function myReviews() {
		let memberNum = getParameterByName('num');
		
		
		$.ajax({
			url : "myReviews",
			type : "get",
			data : {"memberNum" : memberNum},
			success : function(data) {
				console.log(data);
				let htmlStr = "";
				$.each(data, function(index, item) {
					htmlStr += "<table>";
					htmlStr += "<tr><th>" + item.nickname + "</th>";
					htmlStr += "<th> ★" + item.rating + "</th></tr>";
					htmlStr += "<tr><th colspan='2'>" + item.orderHistory + "</th></tr>";
					htmlStr += "<tr><th colspan='2'>" + item.reviewContent + "</th></tr>";
					htmlStr += "</table>";
				});				
				$("#myReviewList").html(htmlStr);
			}
		});
	}
	
	
	//0924 추가
	function wishList() {
		$("#couponList").hide();
		$("#buyCoupon").hide();
		$("#profileModal").hide();
		$("#orderList").hide();
		$("#wishlistStore").show();
		let memberNum = getParameterByName('num');
		
		$.ajax({
			url : "lookWishList",
			type : "get",
			data : {"memberNum" : memberNum},
			success : function(data) {
				console.log(data);
				
				/* <![CDATA[ */
				let htmlStr = "";
				
				
				$.each(data, function(index, item) {
					htmlStr += "<table border='1' class='menuTable'>";
					htmlStr += "<tr>"; 
					htmlStr += "<td><img src='storeDisplay?num=" + item.storeNum + "' width='80px;'></td>"; 
					htmlStr += "<td class='nameTd'><a href='/delivery/read" + "?num=" + item.storeNum + "'>" + item.storeName + "</a></td>";
					htmlStr += "<td> ★ " + item.rating + "</td>";
					htmlStr += "<td> ♥ " + item.wishlist + "</td>";
					htmlStr += "</tr>";
					htmlStr += "</table>"
				});
				$("#exampleModalLabel").text("찜리스트");
				$("#wishlistStore").html(htmlStr);
				$("#exampleModal").modal("show");
				/* ]]> */
			}
		});
	}
	
	// 0924 추가
	function buyCoupon(couponNum) {
		
		$.ajax({
			url : "/master/buyOneCoupon",
			type : "get",
			data : {"couponNum" : couponNum},
			success : function(data) {
				alert("상품을 구입하셨습니다.");
				couponList();
			}
		});
	}
	
	// 0924 추가
	function couponList() {
		
		$.ajax({
			url : "myCouponList",
			type : "get",
			success : function(data) {
				console.log(data);
				
				let htmlStr = "";
				if(data == "") {
					htmlStr += "<h1>남은 쿠폰이 없습니다....</h1>";
				} else {
					
					$.each(data, function(index, item) {
						htmlStr += "<table>";
						htmlStr += "<tr><th>" + item.couponName + "</th></tr>";
						htmlStr += "<tr><th>" + item.couponPoint + "</th></tr>";
						htmlStr += "<tr><th><input type='button' value='사용' onclick='useCoupon(" + item.myCouponNum + ")'></th></tr>";
						htmlStr += "</table>";
					});
				}
				
				$("#couponList").html(htmlStr);
			}
		});
	}
	
	
	// 0924 추가
	function useCoupon(myCouponNum) {
		
		$.ajax({
			url : "useCoupon",
			type : "get",
			data : {"myCouponNum" : myCouponNum},
			success : function(data) {
				alert("쿠폰을 사용하였습니다.");
				console.log(data);
				if(data == "") {
					$("#couponList").text("남은 쿠폰이 없습니다....");
				}
				
				couponList();
				myPoint();
			}
		});
	}
	
	// 0924 추가
	function myPoint() {
		let memberNum = getParameterByName('num');
		$.ajax({
			url : "myPoint",
			type : "get",
			data : {"memberNum" : memberNum},
			success : function(data) {
				console.log(data.memberPoint);
				
				let htmlStr = "<h5>잔여 포인트</h5>";
				htmlStr += "<h5>" + data.memberPoint + "point</h5>";
				
				$("#point").html(htmlStr);
			}
		});
	}
	
	
	function loadAllReply() {
		 let memberNum = getParameterByName('num');
	  
		$.ajax({
			url:"loadAllReply",	// 콘트롤러에서 메핑이랑 일치해야 함 (굳이 자바스크립트 함수이름과 같을 필요 없음)
			type:"get",
			data: {"memberNum": memberNum},	// ""=> 콘트롤러 파라미터로 넘겨줌(이름 콘트롤러랑 똑같이)
			success: function(data){	// data는 콘트롤러에서 return 값으로 가져온거(list 결과들, 이름을 어떻게 가져와도 자유!)
				let htmlStr = "<table>";
				$.each(data, function(index, value){	//data는 리스트니까 반복문 each로 돌려줘야함 그래서 index 얼마나 있는지 볼라고 필요함
					htmlStr += "<tr>";
					htmlStr += "<td>" + value.content + "</td>";
					htmlStr += "<td>" + value.nickname + "</td>";
					htmlStr += "<td>" + value.inputdate + "</td>";
					htmlStr += "</tr>";	
				});
				htmlStr += "</table>";
					
				$("#reList").html(htmlStr);
				}
		});
	}
	
	function writeReply(){
	      let content = $("#content").val();
	      
	      let memberNum = getParameterByName('num');
	      console.log("dlkj");
	      $.ajax({
	         url: "writeReply",
	         type: "post",
	         data: {"content" : content, "memberNum" : memberNum},
	         success: function(){
	            // 페이지 새로고침
	            $("#content").val(""); 		// 댓글창에 내용을 없애버림
	            loadAllReply();				// 페이지 새로고침 -> 댓글 목록만 가져와서 새로뿌리기
	         }
	      
	      });
	   }



	