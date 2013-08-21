package com.can2do.goodsdemo;
import java.util.ArrayList;

/**
 * demo展示商品列表的商品信息
 *
 */
public final class Goods {
	
	public class GoodsDetail {
		String name;
		String body;
		String price;
		int resId;
	}

	ArrayList<Goods.GoodsDetail> mproductlist = new ArrayList<Goods.GoodsDetail>();

	public ArrayList<Goods.GoodsDetail> retrieveGoodsInfo() {
		GoodsDetail goodsDetail = null;

		//
		goodsDetail = new GoodsDetail();
		goodsDetail.name = "渡之珍";
		goodsDetail.body = "本草之当归";
		goodsDetail.price = "200";
		goodsDetail.resId = 30;
		mproductlist.add(goodsDetail);
		
		//
		goodsDetail = new GoodsDetail();
		goodsDetail.name = "渡之珍";
		goodsDetail.body = "本草之兔子草";
		goodsDetail.price = "0.01";
		goodsDetail.resId = 30;
		mproductlist.add(goodsDetail);
		
		//
		goodsDetail=new GoodsDetail();
		goodsDetail.name="渡之珍";
		goodsDetail.body="本草参";
		goodsDetail.price="0.01";
		goodsDetail.resId=30;
		mproductlist.add(goodsDetail);
		
		return mproductlist;
		
	}
}
