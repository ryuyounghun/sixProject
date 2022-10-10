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
			$("#reviewModal").hide();
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
			$("#reviewModal").hide();
			$("#buyCoupon").hide();
			$("#orderList").hide();
			$("#profileModal").hide();
			$("#couponList").show();
			//0930 세련 수정함
			//쿠폰 목록(모달창)켰을 때 보일 이름 바꿨음.
			$("#exampleModalLabel").text("나의 쿠폰 목록");
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
		
		// 1001 추가
		$("#btnReply_update").hide();
	});
	
	// 1004 추가
	function goStoreRead(storeNum) {
		location.href = "/delivery/read?num=" + storeNum;
	}
	
	
	
	// 0928 레벨별 뱃지추가하기  1003 추가
	function badge(){
		let memberNum = getParameterByName('num');
		
		$.ajax({
			url : "mypageLevel",
			type : "post",
			data : {"memberNum" : memberNum},
			success : function(data) {
				let memberLevel = data.memberLevel;
				
				for ( let i =0; i <= 10; i++){
				if(memberLevel == i){
					img_src = "/images/levelBadges/lv"+ i + ".png";
				}
			}
			document.getElementById("levelImg").src=img_src;
			}
		});		
	}
	
	// 0928 추가
	function profileImage() {
		$("#couponList").hide();
		$("#reviewModal").hide();
		$("#buyCoupon").hide();
		$("#orderList").hide();
		$("#wishlistStore").hide();
		$("#profileModal").show();
		
		$("#exampleModalLabel").text("프로필 사진 바꾸기");
		
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
		$("#reviewModal").hide();
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
					htmlStr += htmlStr += "<span class = 'couponTitle'>주문 목록이 없습니다.</span>";
				} else {
					$.each(data, function(index, item) {
						// 0930 세련 수정함
						htmlStr += "<div class = 'couponCenter2'>";
						htmlStr += "<span class = 'first1'>" + item.receiptNum + "</span>";
						htmlStr += "<span class = 'first1'>" + item.totalAmount + "</span>";
						htmlStr += "<span class= 'first1'>" + item.orderHistory + "</span>";
						htmlStr += "<span class= 'first1'><input type='button' class = 'btn1' value='리뷰쓰기' onclick='writeReview(" + item.storeNum + "," + item.receiptNum + ")'></span>";
						htmlStr += "</div>";
					});
				}
				
				$("#orderList").html(htmlStr);
				$("#exampleModalLabel").text("주문 리스트");
				$("#orderList").show();
				$("#exampleModal").modal("show");
				
			}
		});
	}
	
	// 0925 추가
	function writeReview(storeNum, receiptNum) {
		$("#hiddenStoreNum").val(storeNum);
		$("#hiddenReceiptNum").val(receiptNum);
		
		$.ajax({
			url : "checkComplete",
			type : "get",
			data : {"receiptNum" : receiptNum},
			success : function(data) {
				console.log(data.complete);
				if(data.complete == 'Y') {
					endReview(receiptNum);
				}else {
					alert("아직 주문이 완료되지 않았습니다.");
				}
				
			}
			
		});
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
					// 0930 세련 수정함
					// 1002 세련 수정함 orderHistory,reviewContent 부분에 기호추가
					htmlStr += "<div class = 'title2'>";
					htmlStr += "<span class = 'second1'>" + item.nickname + "  </span>";
					htmlStr += "<span class = 'second1'> ★" + item.rating + "  </span>";
					htmlStr += "<span class = 'second1'>《 " + item.orderHistory + " 》 </span>";
					htmlStr += "<span class = 'second1'>' " + item.reviewContent + " '<span>";
					htmlStr += "</div>";
				});				
				$("#myReviewList").html(htmlStr);
			}
		});
	}
	
	
	//0924 추가  1004 수정
	function wishList() {
		$("#couponList").hide();
		$("#buyCoupon").hide();
		$("#reviewModal").hide();
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
				
				if(data == "") {
					htmlStr += "<span class = 'couponTitle'>찜한 매장이 없습니다.</span>";
				} else {
				
					$.each(data, function(index, item) {
						// 0930 세련 수정함 타이틀1(수정예정)
						// 1001 세련 img 높이 설정 추가
						htmlStr += "<div class='title1' onclick='goStoreRead(" + item.storeNum + ")'>";
						htmlStr += "<span class = 'thrid1'><img src='storeDisplay?num=" + item.storeNum + "' width='80px;' height='45px;'></span>"; 
						htmlStr += "<span class = 'thrid1'>" + item.storeName + "</a></span>";
						htmlStr += "<span class = 'thrid1'> ★ " + item.rating + "</span>";
						htmlStr += "<span class = 'thrid1'> ♥ " + item.wishlist + "</span>";
						htmlStr += "</div>"
					});
				}
				$("#exampleModalLabel").text("찜 리스트");
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
					htmlStr += "<span class = 'couponTitle'>남은 쿠폰이 없습니다.</span>";
				} else {
					
					$.each(data, function(index, item) {
						//0930 세련 수정함
						// 1002 세련 span class = 'cpBtn'로 수정함 
						htmlStr += "<div class = 'couponCenter'>";
						htmlStr += "<span class = 'fourth1'> 『 " + item.couponName + " 』</span>";
						htmlStr += "<span class = 'fourth1'>" + item.couponPoint + "원</span>";
						htmlStr += "<span class = 'cpBtn'><input type='button' class = 'btn1' value='사용하기' onclick='useCoupon(" + item.myCouponNum + ")'></span>";
						htmlStr += "</div>";
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
					$("#couponList").text("남은 쿠폰이 없습니다.");
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
				htmlStr += "<h5>" + data.memberPoint + " point</h5>";
				
				$("#point").html(htmlStr);
			}
		});
	}
	
	
	function loadAllReply() {
		 let memberNum = getParameterByName('num');
	 	 let loginMember = $("#loginMember").val();
		$.ajax({
			url:"loadAllReply",	// 콘트롤러에서 메핑이랑 일치해야 함 (굳이 자바스크립트 함수이름과 같을 필요 없음)
			type:"get",
			data: {"memberNum": memberNum},	// ""=> 콘트롤러 파라미터로 넘겨줌(이름 콘트롤러랑 똑같이)
			success: function(data){	// data는 콘트롤러에서 return 값으로 가져온거(list 결과들, 이름을 어떻게 가져와도 자유!)
				let htmlStr = "<table>";
				$.each(data, function(index, value){	//data는 리스트니까 반복문 each로 돌려줘야함 그래서 index 얼마나 있는지 볼라고 필요함
					htmlStr += "<tr>";
					// 1008 추가
					htmlStr += "<td class = 'contentBox2'>" + value.content + "</td>";
					htmlStr += "<td>" + value.nickname + "</td>";
					htmlStr += "<td class = 'inputdateBox2'>" + value.inputdate + "&nbsp;&nbsp;&nbsp;</td>";
					// 1001 추가
					// 1008 추가 
					if(loginMember == value.writerNum){
						htmlStr += "<td class = 'updateDeleteBox2'><a href='javascript:updateCheckGuestbook("+ value.guestBookNum+");' class = 'update2'>수정&nbsp;&nbsp;</a>";
						htmlStr += "  |  ";
						htmlStr += "<a href='javascript:deleteGuestbook("+ value.guestBookNum+");' class = 'delete2'>&nbsp;&nbsp;삭제</a></td>";
					}
					htmlStr += "</tr>";	
				});
				htmlStr += "</table>";
					
				$("#reList").html(htmlStr);
				}
		});
	}
	
	// 1001 추가
	function updateCheckGuestbook(guestBookNum) {
		
		$.ajax({
			url : "updateCheckGuestbook",
			type : "get",
			data : {"guestBookNum" : guestBookNum},
			success : function(data) {
				$("#content").val(data.content);
				$("#btnReply_write").hide();
				$("#btnReply_update").show();
				$("#btnReply_update").click( function() { updateGuestbook(data.guestBookNum) } );
			}
		});
	}
	// 1001 추가
	function updateGuestbook(guestBookNum) {
		let content = $("#content").val();
		$.ajax({
			url : "updateGuestbook",
			type : "get",
			data : {"guestBookNum" : guestBookNum, "content" : content},
			success : function() {
				loadAllReply();
				$("#btnReply_write").show();
				$("#btnReply_update").off().hide();
				$("#content").val("");
			}
		});
	}
	
	// 1001 추가
	function deleteGuestbook(guestBookNum) {
		$.ajax({
			url : "deleteGuestbook",
			type : "get",
			data : {"guestBookNum" : guestBookNum},
			success : function() {
				loadAllReply();
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


	