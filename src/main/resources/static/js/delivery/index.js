	
	$(document).ready(function() {
		
		button();
		rank();
	});
	
	function rank() {
      
      $.ajax({
         url : "storeRank",
         type : "get",
         success : function(data) {
            
            // 9월 29일 랭킹뱃지 추가작업 
            let htmlStr = "<h1 style='text-align:center' class='rankTitle'>가게 랭킹</h1><ul>";
            
            $.each(data, function(index, item) {
   
               
               if(index <= 9) {
                  if ( index <=2 ){
                     
                     htmlStr += "<li class='top3Stores' onClick='selectStore("+ item.storeNum+ ")'>"+  "<img src='' id='storeRank" + index + "'>  "  + "<span style='display: inline-block; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; max-width: 13ch;' >" + item.storeName +"</span>" + "</li>";
                  }else {
                     
                     htmlStr += "<li class='underTop3' onClick='selectStore("+ item.storeNum+ ")'>" + (index+1)+"위 " +item.storeName + "</li>";
                  }
               }
               
            });
            htmlStr += "</ul>";
            $("#storeRank").html(htmlStr);
            
            for ( let i =0; i<=2; i++){
               let img_src = "/images/rank/rank"+ (i+1) + ".png";
               document.getElementById("storeRank"+i).src = img_src;
               document.getElementById("storeRank"+i).style.width="30px";
               document.getElementById("storeRank"+i).style.width="30px";
            }            
         }
         
      });
   }
	
	
	function button() {
		
		let a = $("#myAddress").val();
		console.log(a);
		$.ajax({           
			url:'https://dapi.kakao.com/v2/local/search/address.json?query='+encodeURIComponent(a),
			type:'GET',           
			headers: {'Authorization' : 'KakaoAK ff7e45d762a8cd9c3235651811e41a69'},   
			success:function(data){
				
				console.log(data);
				let b = data.documents[0];
				
				
				var rs = dfs_xy_conv("toXY", b.y, b.x);
				
			
		
		console.log(rs);
		
		// 소스출처 : http://www.kma.go.kr/weather/forecast/digital_forecast.jsp  내부에 있음
		// (사용 예)
		//var rs = dfs_xy_conv("toLL", "57", "74");
		//console.log(rs.lat, rs.lng);
		//console.log(rs.x, rs.y);
		//
	    //<!--
	    //
	    // LCC DFS 좌표변환을 위한 기초 자료
	    //
	    
	    //
	    // LCC DFS 좌표변환 ( code : "toXY"(위경도->좌표, v1:위도, v2:경도), "toLL"(좌표->위경도,v1:x, v2:y) )
	    //
	
	
	    function dfs_xy_conv(code, v1, v2) {
	    	var RE = 6371.00877; // 지구 반경(km)
	        var GRID = 5.0; // 격자 간격(km)
	        var SLAT1 = 30.0; // 투영 위도1(degree)
	        var SLAT2 = 60.0; // 투영 위도2(degree)
	        var OLON = 126.0; // 기준점 경도(degree)
	        var OLAT = 38.0; // 기준점 위도(degree)
	        var XO = 43; // 기준점 X좌표(GRID)
	        var YO = 136; // 기1준점 Y좌표(GRID)
			
	        var DEGRAD = Math.PI / 180.0;
	        var RADDEG = 180.0 / Math.PI;
	        var re = RE / GRID;
	        var slat1 = SLAT1 * DEGRAD;
	        var slat2 = SLAT2 * DEGRAD;
	        var olon = OLON * DEGRAD;
	        var olat = OLAT * DEGRAD;
	
			var sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
	        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
	        var sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
	        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
	        var ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
	        ro = re * sf / Math.pow(ro, sn);
	        var rs = {};
	        if (code == "toXY") {
	        	 rs['lat'] = v1;
	             rs['lng'] = v2;
	             var ra = Math.tan(Math.PI * 0.25 + (v1) * DEGRAD * 0.5);
	             console.log(ra);
	             ra = re * sf / Math.pow(ra, sn);
	             var theta = v2 * DEGRAD - olon;
	             if (theta > Math.PI) theta -= 2.0 * Math.PI;
	             if (theta < -Math.PI) theta += 2.0 * Math.PI;
	             theta *= sn;
	             rs['x'] = Math.floor(ra * Math.sin(theta) + XO + 0.5);
	             rs['y'] = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
	        }
	        else {
	            rs['x'] = v1;
	            rs['y'] = v2;
	            console.log(v1);
	            var xn = v1 - XO;
	            var yn = ro - v2 + YO;
	            ra = Math.sqrt(xn * xn + yn * yn);
	            if (sn < 0.0) - ra;
	            var alat = Math.pow((re * sf / ra), (1.0 / sn));
	            alat = 2.0 * Math.atan(alat) - Math.PI * 0.5;
	
	            if (Math.abs(xn) <= 0.0) {
	                theta = 0.0;
	            }
	            else {
	                if (Math.abs(yn) <= 0.0) {
	                    theta = Math.PI * 0.5;
	                    if (xn < 0.0) - theta;
	                }
	                else theta = Math.atan2(xn, yn);
	            }
	            var alon = theta / sn + olon;
	            rs['lat'] = alat * RADDEG;
	            rs['lng'] = alon * RADDEG;
	        }
			console.log(rs);
	        return rs;
	    }
		
		
		
		
		
			var arr = [];
			var today = new Date();
			var week = new Array('일', '월', '화', '수', '목', '금', '토');
			var year = today.getFullYear();
			var month = today.getMonth() + 1;
			var day = today.getDate();
			var hours = today.getHours();
			var minutes = today.getMinutes();
			var hours_al = new Array('02', '05', '08', '11', '14', '17', '20', '23');
			
			for (var i = 0; i < hours_al.length; i++) {
				var h = hours_al[i] - hours;
				if (h == -1 || h == 0 || h == -2) {
					var now = hours_al[i];
				} 
				
				if (hours == 00) {
					var now = hours_al[7];
				}
			}
			
			if (hours < 10) {
				hours = '0' + hours;
			}
			if (month < 10) {
				month = '0' + month;
			}
			if (day < 10) {
				day = '0' + day;
			}
			
			today = year + "" + month + "" + day;
			
			apikey = "FWAJO3sIoJFZOeouozC2dK8AZr4S5rwvNd3GjoRxFALaKT1yfF26eiweg3luifGB5BILZsve2bQQFzft6DndIg%3D%3D",
			ForecastGribUrl = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"
			ForecastGribUrl += "?servicekey=" + apikey;
			ForecastGribUrl += "&pageNo=1";
			ForecastGribUrl += "&numOfRows=10";
			ForecastGribUrl += "&dataType=XML";
			ForecastGribUrl += "&base_date=" + today;
			ForecastGribUrl += "&base_time=" + now + "00";
			ForecastGribUrl += "&nx=" + rs.x + "&ny=" + rs.y;
			arr.push({'url' : ForecastGribUrl}); 
			
	
			
			$.ajax({
				url : ForecastGribUrl,
				type : 'get',
				success : function(data) {
					var cate = '';
					var temp = '';
					var sky = '';
					var rain = '';
					$(data).find("response > body > items > item").each(function(index) {
						cate = $(this).find("category").text();
						if(cate == 'TMP') {
							temp = $(this).find("fcstValue").text();
						}
						if(cate == 'SKY') {
							sky = $(this).find("fcstValue").text();
						}
						if(cate == 'PTY') {
							rain = $(this).find("fcstValue").text();
						}
					});
					
					$('.weather').append('<h2 class="list-group-item in0"></h2>');
					$('.in0').html(temp + " °C");
					
					let arr1 = ["냉면", "백숙", "초계국수", "팥빙수", "메밀소바"];
					let arr2 = ["치킨", "피자", "돈까스", "국밥", "라멘"];
					let arr3 = ["붕어빵", "칼국수", "찐빵", "오뎅탕", "우동"];
			
					let rand = Math.floor(Math.random() * 5);
					
					let str1 = "<h1> 오늘의 추천음식 : " + 
					
					arr1[rand] + "</h1>"; 
					
					let str2 = "<h1> 오늘의 추천음식 : " + 
					
					arr2[rand] + "</h1>";
					
					let str3 = "<h1> 오늘의 추천음식 : " + 
					
					arr2[rand] + "</h1>"; 
					
					if(temp >= 30) {
						$('#weatherRecommendFood').html(str1);
					} else if(temp > 10){
						$('#weatherRecommendFood').html(str2);
					} else if(temp <= 10) {
						$('#weatherRecommendFood').html(str3);
					}
					
					if (rain != 0) {
						switch (rain) {
						case '1':
							$('.in0').append(" / 비");
							break;
						case '2':
							$('.in0').append(" / 비/눈");
							break;
						case '3':
							$('.in0').append(" / 눈");
							break;
						}
					} else {
						// 9월28일 추가 작업   
						switch (sky) {
						case '1':
							$('.in0').append(" / 맑음");	
							$(".weatherIcon").css({"background-image" : "url(/images/weather/sunny.gif)"});
							
							break;
						case '2':
							$('.in0').append(" / 구름조금");
							$(".weatherIcon").css({"background-image" : "url(/images/weather/cloud.gif)"});
							break;
						case '3':
							$('.in0').append(" / 구름많음");
							$(".weatherIcon").css({"background-image" : "url(/images/weather/foggy.gif)"});
							
							break;
						case '4':
							$('.in0').append(" / 흐림");
							$(".weatherIcon").css({"background-image" : "url(/images/weather/hurim.gif)"});
							break;
						}
					}
					
				}
			});
			
	
			},   
			error : function(e){
				console.log(e);
			}
	});
	}
	function getMyAddress(){
		let myAddress = $("#myAddress").val();
		if ( myAddress == null || myAddress.trim().length == 0){
			alert("회원님의 등록된 주소가 없습니다.")
		}
		$("#searchWord").val(myAddress);
	}
    //-->
