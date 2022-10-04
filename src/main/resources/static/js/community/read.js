$(document).ready(function() {
		loadAddress();
		partyPeople();
		$("#btn").on("click",function() { mmaapp(); });
		deleteClassBoard();
		storeInfo();
	});
	
	function getParameterByName(name) {
		  name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
		  var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
		  results = regex.exec(location.search);
		  return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
	}
	
	// 0925 추가
	function storeInfo() {
		let classNum = getParameterByName('num');
		$.ajax({
			url : "storeInfo",
			type : "get",
			data : {"classNum" : classNum},
			success : function(data) {
				console.log(data);
				let htmlStr = "<p></p>";
				if(data != "") {
					htmlStr = "<table>";
					htmlStr += "<tr><th><img src='storeDisplay?num=" + data.storeNum + "'></th>";
					htmlStr += "<th><a href='/delivery/read?num=" + data.storeNum + "'>" + data.storeName + "</a></th></tr>";
				} else {
					htmlStr = "<p>지정된 가게는 없습니다.</p>";
				}
				$("#storeInfo").html(htmlStr);
			}
			
		});
	}
	
	
	
	// 0923 추가
	function deleteBoard() {
		let classNum = getParameterByName('num');
		var roomId = $("#roomId").val();
		$.ajax({
			url : "deleteBoard",
			type : "post",
			data : {"classNum" : classNum, "roomId" : roomId},
			success : function() {
				location.href="index";
			}
		});
	}
	
	
	// 0923 추가
	function deleteClassBoard() {
		let classNum = getParameterByName('num');
		let memberId = $("#loginUser").val();
		
		$.ajax({
			url : "deleteClassBoard",
			type : "post",
			data : {"classNum" : classNum},
			success : function(data) {
				console.log(data.memberId);
				
				if(memberId == data.memberId) {
					htmlStr = "<input type='button' value='게시글 삭제하기' onclick='deleteBoard()'>";
					$("#deleteArea").html(htmlStr);
				}
			}
		});
	}	
	
	
	
	function withdrawalParty() {
		let classNum = getParameterByName('num');
		
		$.ajax({
			url : "withdrawalParty",
			type : "get",
			data : {"classNum" : classNum},
			success : function() {
				partyPeople();
				// 0924 추가
				location.href="/community/index";
			}
		});
	}
	
	function enterChat() {
		var sender = $("#loginUser").val();
		var roomId = $("#roomId").val();
                if(sender !== "") {
                    localStorage.setItem('wschat.sender',sender);
                    localStorage.setItem('wschat.roomId',roomId);
                    location.href="/chat/room/enter/"+roomId;
                }
	}
	
	
	function joinParty() {
		let classNum = getParameterByName('num');
		$.ajax({
			url : "joinParty",
			type : "get",
			data : {"classNum" : classNum},
			success : function(data) {
				partyPeople();
				alert(data);
				
			}
		});
	}
	
	// 0923추가
	function memberPage(memberNum) {
		
		location.href = "/member/mypage?num=" + memberNum;
	}
	
	
	function partyPeople() {
		let classNum = getParameterByName('num');
		$.ajax({
			url : "partyPeople",
			type : "get",
			data : {"classNum" : classNum},
			success : function(data) {
				console.log(data);
				
				let htmlStr = "";
				$.each(data, function(index, item) {
					htmlStr += "<table><tr>";
					htmlStr += "<td>" + item.memberNum + "</td>";
					htmlStr += "<th>" + item.nickname + "</th>";
					htmlStr += "<td>" + item.address + "</td>";
					htmlStr += "<td><input type='button' onclick='memberPage(" + item.memberNum + ")' value='마이페이지 방문'></td>";
					htmlStr += "</tr></table>";
				});
				
				$(".participient").html(htmlStr);
			}
		});
	}
	
	function loadAddress() {
		let destination = $("#destination").val();
		let myAddress = $("#myAddress").val();
		let addressList = [destination, myAddress];

		$.each(addressList, function(index, item) {
			$.ajax({           
				url:'https://dapi.kakao.com/v2/local/search/address.json?query='+encodeURIComponent(item),
				type:'GET',           
				headers: {'Authorization' : 'KakaoAK ff7e45d762a8cd9c3235651811e41a69'},   
				success:function(data){
					let a = index + 1;
					let b = data.documents[0];
											
					$("#x" + a + "").val(b.y);
					$("#y" + a + "").val(b.x);
				}
			});	
		});
					
	}				
		
		function mmaapp() {
			
		
			let sX = $("#x1").val();
			let sY = $("#y1").val();
			let dX = $("#x2").val();
			let dY = $("#y2").val();
			
			let sXY = {sX, sY};
			let dXY = {dX, dY};
			let sdXY = {sXY, dXY};
			console.log(sdXY);
			
			var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
		    mapOption = {
		        center: new kakao.maps.LatLng(sX, sY), // 지도의 중심좌표
		        level: 3 // 지도의 확대 레벨
		    };  
			
			// 지도를 생성합니다    
			var map = new kakao.maps.Map(mapContainer, mapOption); 
			
			// 마커를 표시할 위치와 내용을 가지고 있는 객체 배열입니다 
			var positions = [
			    {
			        content: '<div>우리집</div>', 
			        latlng: new kakao.maps.LatLng(dX, dY)
			    },
			    {
			        content: '<div>목적지</div>', 
			        latlng: new kakao.maps.LatLng(sX, sY)
			    },
			    {
			        content: '<div>텃밭</div>', 
			        latlng: new kakao.maps.LatLng(33.450879, 126.569940)
			    },
			    {
			        content: '<div>근린공원</div>',
			        latlng: new kakao.maps.LatLng(33.451393, 126.570738)
			    }
			];

			for (var i = 0; i < positions.length; i ++) {
			    // 마커를 생성합니다
			    var marker = new kakao.maps.Marker({
			        map: map, // 마커를 표시할 지도
			        position: positions[i].latlng // 마커의 위치
			    });

			    // 마커에 표시할 인포윈도우를 생성합니다 
			    var infowindow = new kakao.maps.InfoWindow({
			        content: positions[i].content // 인포윈도우에 표시할 내용
			    });

			    // 마커에 mouseover 이벤트와 mouseout 이벤트를 등록합니다
			    // 이벤트 리스너로는 클로저를 만들어 등록합니다 
			    // for문에서 클로저를 만들어 주지 않으면 마지막 마커에만 이벤트가 등록됩니다
			    kakao.maps.event.addListener(marker, 'mouseover', makeOverListener(map, marker, infowindow));
			    kakao.maps.event.addListener(marker, 'mouseout', makeOutListener(infowindow));
			}

			// 인포윈도우를 표시하는 클로저를 만드는 함수입니다 
			function makeOverListener(map, marker, infowindow) {
			    return function() {
			        infowindow.open(map, marker);
			    };
			}

			// 인포윈도우를 닫는 클로저를 만드는 함수입니다 
			function makeOutListener(infowindow) {
			    return function() {
			        infowindow.close();
			    };
			}
			
			var polyline = new daum.maps.Polyline({
				map:map,
				path : [
				new daum.maps.LatLng(sX, sY),
				new daum.maps.LatLng(dX, dY)
				],
			 strokeWeight: 2,
			 strokeColor: '#FF00FF',
			 strokeOpacity: 0.8,
			 strokeStyle: 'solid'
			});

			//return getTimeHTML(polyline.getLength());//미터단위로 길이 반환;
			console.log("길이"+polyline.getLength());
			let mt = polyline.getLength().toFixed(0);
			if (mt > 1000) {
				mt = mt / 1000; 
				$("#m").text("목적지까지 " + mt +"KM입니다.");				
			} else if (mt < 1000) {
				$("#m").text("목적지까지 " + mt +"M입니다.");
			}
			return polyline.getLength();			    
	}	
	