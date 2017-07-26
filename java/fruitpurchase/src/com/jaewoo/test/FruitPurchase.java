package com.jaewoo.test;

import java.util.ArrayList;

class FruitPurchase {
	public static void main(String[] args) {
		FruitSeller seller = new FruitSeller(0, 30, 1500);
		FruitBuyer buyer = new FruitBuyer(10000);
		ArrayList<String> list = new ArrayList<>();

		list.add("supbro");

		buyer.buyApple(seller, 4500);

		System.out.println("Seller's status: ");
		seller.showSaleResult();

		System.out.println("Buyer's status: ");
		buyer.showBuyResult();

		System.out.println(list.get(0));
	}
}

class FruitSeller {
	private int numOfApple;
	private int myMoney;
	private final int APPLE_PRICE;

	public FruitSeller(int money, int appleNum, int price) {
		this.myMoney = money;
		this.numOfApple = appleNum;
		this.APPLE_PRICE = price;
	}

	public int sellApple(int money) {
		int num = money/APPLE_PRICE;
		numOfApple -= num;
		myMoney += money;
		return num;
	}

	public void showSaleResult() {
		System.out.println("Remaining apple: " + numOfApple);
		System.out.println("profit : " + myMoney);
	}
}

class FruitBuyer {
	private int myMoney;
	private int numOfApple;

	public FruitBuyer(int money) {
		myMoney = money;
		numOfApple = 0;
	}

	public void buyApple(FruitSeller seller, int money) {
		numOfApple += seller.sellApple(money);
		myMoney -= money;
	}

	public void showBuyResult() {
		System.out.println("Remaining money: " + myMoney);
		System.out.println("Number of Apples bought : " + numOfApple);
	}
}
