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


public class PubgMobile extends RecyclerView.Adapter<PubgMobile.ViewHolder> {

	private ArrayList<ItemContent> item;

	private OnItemClicked clicked;

	public interface OnItemClicked {
		void itemClicked(ItemContent content)
	}

	public PubgMobile(ArrayList<ItemContent> item, OnItemClicked clicked) {
		this.item = item;
		this.clicked = clicked;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
			.inflate(R.layout.pubg_mobile,parent, false);
		return new ViewHolder(view);

	}

	@Override
	public int getItemCount() {
		return item.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		holder.bind(item.get(position), clicked);
	}

	static class ViewHolder extends RecyclerView.ViewHolder {

		public ImageView imageView;
		public TextView textView;

		public ViewHolder(View itemView) {
			super(itemView);
			imageView = itemView.findViewById(R.id.contentImagepubg);
			textView = itemView.findViewById(R.id.contentNamepubg);
		}

		public void bind(final ItemContent item, final OnItemClicked clicked) {
			Picasso.with(itemView.getContext())
				.load(item.getUrl())
				.fit().centerCrop()
				.into(imageView);
			itemView.setTag(textView);
			itemView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					clicked.itemClicked(item);
				}

			});
		}
	}

}
