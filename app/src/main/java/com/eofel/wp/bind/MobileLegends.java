package com.eofel.wp.bind;

import android.support.v7.widget.RecyclerView;
import android.widget.AdapterView.OnItemClickListener;
import com.eofel.wp.R;
import com.eofel.wp.utils.ItemContent;
import java.util.ArrayList;
import com.squareup.picasso.Picasso;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;


public class MobileLegends extends RecyclerView.Adapter<MobileLegends.ViewHolder> {

	private ArrayList<ItemContent> item;
	
	private OnItemClicked clicked;
	
	public interface OnItemClicked {
		void itemClicked(ItemContent content)
	}
	
	public MobileLegends(ArrayList<ItemContent> data, OnItemClicked clicked) {
		item = data;
		this.clicked = clicked;
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.mobile_legends, parent, false);
		ViewHolder holder = new ViewHolder(view);
		return holder;
		
	}

	@Override
	public int getItemCount() {
		return item.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		holder.bind(item.get(position), clicked);
	}
	
	static class ViewHolder extends RecyclerView.ViewHolder {

		public ImageView imageView;
		public TextView textView;

		public ViewHolder(View itemView) {
			super(itemView);
			imageView = itemView.findViewById(R.id.contentImage);
			textView = itemView.findViewById(R.id.contentName);
		}

		public void bind(final ItemContent item, final OnItemClicked clicked) {
			Log.d("Loop", item.getUrl());
			Picasso.with(itemView.getContext())
				.load(item.getUrl())
				.into(imageView);
			itemView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					clicked.itemClicked(item);
				}

			});
		}
	}

}
