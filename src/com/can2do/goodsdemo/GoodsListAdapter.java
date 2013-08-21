package com.can2do.goodsdemo;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * demo展示商品列表填充器
 * 
 */
public class GoodsListAdapter extends BaseAdapter {

	private ArrayList<Goods.GoodsDetail> m_productList = null;
	private Context context;

	private class ProductItemView {
		TextView name;
		TextView body;
		TextView price;
	}

	public GoodsListAdapter(Context c, ArrayList<Goods.GoodsDetail> list) {
		m_productList = list;
		context = c;
	}

	public int getCount() {
		return m_productList.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final ProductItemView itemView;
		if (convertView == null) {
			itemView = new ProductItemView();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.goods_item, null);
			itemView.name = (TextView) convertView
					.findViewById(R.id.name);
			itemView.body = (TextView) convertView.findViewById(R.id.body);
			itemView.price = (TextView) convertView.findViewById(R.id.price);

			convertView.setTag(itemView);
		} else {
			itemView = (ProductItemView) convertView.getTag();
		}

		itemView.name.setText(this.m_productList.get(position).name);
		itemView.body.setText(this.m_productList.get(position).body);
		itemView.price.setText(this.m_productList.get(position).price);

		return convertView;
	}
}
