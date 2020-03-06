package com.eofel.wp.bind;

import android.support.v7.widget.RecyclerView;
import com.eofel.wp.R;
import com.eofel.wp.utils.ItemContent;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultImage extends RecyclerView.Adapter<ResultImage.ViewHolder> {
	private ArrayList<ItemContent> item;

	private OnItemClicked clicked;

	public interface OnItemClicked {
		void itemClicked(ItemContent content)
	}

	public ResultImage(ArrayList<ItemContent> item, OnItemClicked clicked) {
		this.item = item;
		this.clicked = clicked;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
			.inflate(R.layout.result_list, parent, false);
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
			imageView = itemView.findViewById(R.id.flexResult);
			textView = itemView.findViewById(R.id.flexResultName);
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
