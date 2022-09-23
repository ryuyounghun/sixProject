package com.abc.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.abc.domain.Item;
import com.abc.domain.Order;
import com.abc.domain.Receipt;
import com.abc.domain.Review;
import com.abc.domain.Store;
import com.abc.domain.Wishlist;

@Mapper
public interface DeliveryDAO {
	
	// 가게 등록 및 출력 부분
	public int insertStore(Store store);
	public List<Store> selectSearch(Map<String, String> map);
	public Store selectOneStore(int num);
	public int updateRating(Store store);
	
	public int plusWishlist(int storeNum);
	public int minusWishlist(int storeNum);
	
	public List<Store> selectMemberOne(int num);
	public List<Store> selectStoreRank();
	
	// 0923 추가
	public List<Store> selectStoreListByCategory(String category);
	
	// 상품 등록 및 출력부분
	public int insertItem(Item item);
	public List<Item> selectItemSearch(Map<String, String> map);
	public Item selectOneItem(int num);
	
	// 주문 등록 및 출력부분
	public int insertOrder(Order order);
	public List<Order> selectMyOrder(Map<Object, Object> map);
	public List<Order> selectMyOrders(int memberNum);
	public Order selectOneOrder(int num);
	
	public int plusOrder(Order order);
	public int minusOrder(Order order);
	public int deleteOrder(int num);
	
	public int completeOrder(int orderNum);
	public int paymentOrder(Map<Object, Object> map);
	
	// 영수증 관련
	public int insertReceipt(Receipt receipt);
	public List<Receipt> selectReceipt(int memberNum);
	public Receipt selectReceiptByReceiptNum(int receiptNum);
	public List<Receipt> selectReceiptByStoreNum(int storeNum);
	
	public int updateReceiptByWaiting(int receiptNum);
	public int updateReceiptByComplete(int receiptNum);

	// 리뷰 관련
	public int insertReview(Review review);
	public List<Review> selectReviews(int storeNum);
	public Review selectReviewsByReceiptNum(int receiptNum);
	public Review selectReviewsByMemberNum(int memberNum);

	// 찜 관련
	public int insertWishlist(Wishlist wish);
	public Wishlist selectWishlist(Map<Object, Object> map);
	public int deleteWish(Map<Object, Object> map);
}
