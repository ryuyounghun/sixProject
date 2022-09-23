package com.abc.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.dao.DeliveryDAO;
import com.abc.domain.Item;
import com.abc.domain.Order;
import com.abc.domain.Receipt;
import com.abc.domain.Review;
import com.abc.domain.Store;
import com.abc.domain.Wishlist;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DeliveryServiceImpl implements DeliveryService{

	@Autowired
	private DeliveryDAO dDao;
	
	@Override
	public int insertStore(Store store) {
		// TODO Auto-generated method stub
		return dDao.insertStore(store);
	}

	@Override
	public List<Store> selectSearch(String keyword, String category) {
		// TODO Auto-generated method stub
		
		Map<String, String> map = new HashMap<String, String>();
		
		log.debug("category : {}", category);
		log.debug("keyword : {}", keyword);
		
		map.put("category", category);
		map.put("searchKeyword", keyword);
		
		return dDao.selectSearch(map);
	}

	@Override
	public Store selectOneStore(int num) {
		// TODO Auto-generated method stub
		return dDao.selectOneStore(num);
	}

	@Override
	public List<Store> selectMemberOne(int num) {
		// TODO Auto-generated method stub
		return dDao.selectMemberOne(num);
	}
	
	@Override
	public int insertItem(Item item) {
		// TODO Auto-generated method stub
		return dDao.insertItem(item);
	}

	@Override
	public List<Item> selectItemSearch(String title, String storeNum) {

		Map<String, String> map = new HashMap<String, String>();
		
		log.debug("title : {}", title);
		log.debug("storeNum : {}", storeNum);
		
		map.put("title", title);
		map.put("storeNum", storeNum);
		
		return dDao.selectItemSearch(map);
	}

	@Override
	public Item selectOneItem(int num) {
		// TODO Auto-generated method stub
		return dDao.selectOneItem(num);
	}

	@Override
	public int insertOrder(Order order) {
		// TODO Auto-generated method stub
		return dDao.insertOrder(order);
	}

	@Override
	public List<Order> selectMyOrder(int memberNum, int storeNum) {
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		
		log.debug("memberNum : {}", memberNum);
		log.debug("storeNum : {}", storeNum);
		
		map.put("memberNum", memberNum);
		map.put("storeNum", storeNum);
		
		return dDao.selectMyOrder(map);
	}

	@Override
	public int plusOrder(Order order) {
		// TODO Auto-generated method stub
		return dDao.plusOrder(order);
	}

	@Override
	public Order selectOneOrder(int num) {
		// TODO Auto-generated method stub
		return dDao.selectOneOrder(num);
	}

	@Override
	public int minusOrder(Order order) {
		// TODO Auto-generated method stub
		return dDao.minusOrder(order);
	}

	@Override
	public int paymentOrder(int memberNum, int storeNum) {
		// TODO Auto-generated method stub
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		
		log.debug("memberNum : {}", memberNum);
		log.debug("storeNum : {}", storeNum);
		
		map.put("memberNum", memberNum);
		map.put("storeNum", storeNum);
		
		return dDao.paymentOrder(map);
	}

	@Override
	public int deleteOrder(int num) {
		// TODO Auto-generated method stub
		return dDao.deleteOrder(num);
	}

	@Override
	public List<Order> selectMyOrders(int memberNum) {
		// TODO Auto-generated method stub
		return dDao.selectMyOrders(memberNum);
	}

	@Override
	public int insertReceipt(Receipt receipt) {
		// TODO Auto-generated method stub
		return dDao.insertReceipt(receipt);
	}

	@Override
	public List<Receipt> selectReceipt(int memberNum) {
		// TODO Auto-generated method stub
		return dDao.selectReceipt(memberNum);
	}

	@Override
	public int insertReview(Review review) {
		// TODO Auto-generated method stub
		return dDao.insertReview(review);
	}

	@Override
	public int completeOrder(int orderNum) {
		// TODO Auto-generated method stub
		return dDao.completeOrder(orderNum);
	}

	@Override
	public List<Review> selectReviews(int storeNum) {
		// TODO Auto-generated method stub
		return dDao.selectReviews(storeNum);
	}

	@Override
	public int updateRating(Store store) {
		// TODO Auto-generated method stub
		return dDao.updateRating(store);
	}

	@Override
	public Receipt selectReceiptByReceiptNum(int receiptNum) {
		// TODO Auto-generated method stub
		return dDao.selectReceiptByReceiptNum(receiptNum);
	}

	@Override
	public int insertWishlist(Wishlist wish) {
		// TODO Auto-generated method stub
		return dDao.insertWishlist(wish);
	}

	@Override
	public int plusWishlist(int storeNum) {
		// TODO Auto-generated method stub
		return dDao.plusWishlist(storeNum);
	}

	@Override
	public Wishlist selectWishlist(int storeNum, int memberNum) {
		// TODO Auto-generated method stub
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		
		log.debug("storeNum : {}", storeNum);
		log.debug("memberNum : {}", memberNum);
		
		map.put("memberNum", memberNum);
		map.put("storeNum", storeNum);

		return dDao.selectWishlist(map);
	}

	@Override
	public List<Receipt> selectReceiptByStoreNum(int storeNum) {
		// TODO Auto-generated method stub
		return dDao.selectReceiptByStoreNum(storeNum);
	}

	@Override
	public int deleteWish(int storeNum, int memberNum) {
		// TODO Auto-generated method stub
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		
		log.debug("storeNum : {}", storeNum);
		log.debug("memberNum : {}", memberNum);
		
		map.put("memberNum", memberNum);
		map.put("storeNum", storeNum);
		
		return dDao.deleteWish(map);
	}

	@Override
	public int minusWishlist(int storeNum) {
		// TODO Auto-generated method stub
		return dDao.minusWishlist(storeNum);
	}

	@Override
	public List<Store> selectStoreRank() {
		// TODO Auto-generated method stub
		return dDao.selectStoreRank();
	}

	@Override
	public Review selectReviewsByReceiptNum(int receiptNum) {
		// TODO Auto-generated method stub
		return dDao.selectReviewsByReceiptNum(receiptNum);
	}

	@Override
	public Review selectReviewsByMemberNum(int memberNum) {
		// TODO Auto-generated method stub
		return dDao.selectReviewsByMemberNum(memberNum);
	}

	@Override
	public int updateReceiptByWaiting(int receiptNum) {
		// TODO Auto-generated method stub
		return dDao.updateReceiptByWaiting(receiptNum);
	}

	@Override
	public int updateReceiptByComplete(int receiptNum) {
		// TODO Auto-generated method stub
		return dDao.updateReceiptByComplete(receiptNum);
	}

	@Override
	public List<Store> selectStoreListByCategory(String category) {
		// TODO Auto-generated method stub
		return dDao.selectStoreListByCategory(category);
	}


}
