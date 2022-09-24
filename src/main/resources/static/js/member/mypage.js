$(document).ready(function(){
		
		$("#btnReply_write").on("click", function() { writeReply(); });
		$("#btnReply_update").hide();
		loadAllReply();	
		// 0924 추가
		$("#wishlist").click(function() { wishList(); });
		$("#buyCoupon").hide();
		$("#wishlistStore").hide();
		$("#buyCoupons").click(function() {
			$("#couponList").hide();
			$("#wishlistStore").hide();
			$("#buyCoupon").show();
			$("#exampleModalLabel").text("쿠폰 구매하기");
			$("#exampleModal").modal("show");
		});
		couponList();
		myPoint();
		$("#myCouponList").click(function() {
			$("#wishlistStore").hide();
			$("#buyCoupon").hide();
			$("#couponList").show();
			$("#exampleModalLabel").text("MyCoupon List");
			$("#exampleModal").modal("show");
		});
	});
	
	// 0924 추가
	
	
	// 0924 추가
	function buyCoupon(couponNum) {
		
		$.ajax({
			url : "/master/buyOneCoupon",
			type : "get",
			data : {"couponNum" : couponNum},
			success : function(data) {
				alert("상품을 구입하셨습니다.");
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
				
				$.each(data, function(index, item) {
					htmlStr += "<table>";
					htmlStr += "<tr><th>" + item.couponName + "</th></tr>";
					htmlStr += "<tr><th>" + item.couponPoint + "</th></tr>";
					htmlStr += "<tr><th><input type='button' value='사용' onclick='useCoupon(" + item.myCouponNum + ")'></th></tr>";
					htmlStr += "</table>";
				});
				
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
				console.log(data);
				alert("쿠폰을 사용하였습니다.");
				
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
				
				let htmlStr = "<h1>잔여 포인트</h1>";
				htmlStr += "<h1>" + data.memberPoint + "원</h1>";
				
				$("#point").html(htmlStr);
			}
		});
	
	
	/////////////////////////////////////////////////////////////
	function getParameterByName(name) {
		  name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
		  var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
		  results = regex.exec(location.search);
		  return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
	}
	
	function loadAllReply(){
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
					
				/* htmlStr += "<a href = 'javascript:loadAllReply();'>댓글 새로고침</a>" */
					
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
	}