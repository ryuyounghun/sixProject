package com.abc.service;

import java.util.List;
import java.util.Map;

import com.abc.domain.Item;
import com.abc.domain.Order;
import com.abc.domain.Receipt;
import com.abc.domain.Review;
import com.abc.domain.Store;
import com.abc.domain.Wishlist;

public interface DeliveryService {

	public int insertStore(Store store);
	public List<Store> selectSearch(String category, String keyword);
	public Store selectOneStore(int num);
	public int updateRating(Store store);
	
	public int plusWishlist(int storeNum);
	public int minusWishlist(int storeNum);
	
	public List<Store> selectMemberOne(int num);
	public List<Store> selectStoreRank();
	// 0923 추가
	public List<Store> selectStoreListByCategory(String category);
	
	public int insertItem(Item item);
	public List<Item> selectItemSearch(String title, String storeNum);
	public Item selectOneItem(int num);
	
	public int insertOrder(Order order);
	public List<Order> selectMyOrder(int memberNum, int storeNum);
	public List<Order> selectMyOrders(int memberNum);
	public Order selectOneOrder(int num);
	
	public int plusOrder(Order order);
	public int minusOrder(Order order);
	public int deleteOrder(int num);
	
	public int completeOrder(int orderNum);
	public int paymentOrder(int memberNum, int storeNum);
	
	public int insertReceipt(Receipt receipt);
	public List<Receipt> selectReceipt(int memberNum);
	public Receipt selectReceiptByReceiptNum(int receiptNum);
	public List<Receipt> selectReceiptByStoreNum(int storeNum);
	
	public int updateReceiptByWaiting(int receiptNum);
	public int updateReceiptByComplete(int receiptNum);
	
	public int insertReview(Review review);
	public List<Review> selectReviews(int storeNum);
	public Review selectReviewsByReceiptNum(int receiptNum);
	public Review selectReviewsByMemberNum(int memberNum);
	
	public int insertWishlist(Wishlist wish);
	public Wishlist selectWishlist(int storeNum, int memberNum);
	public int deleteWish(int storeNum, int memberNum);
	
	// 0924 추가
	public List<Wishlist> selectWishlistByMemberNum(int memberNum);
}
