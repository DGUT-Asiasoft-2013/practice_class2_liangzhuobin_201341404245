package pages;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Random;

import com.example.hello.Article;
import com.example.hello.AvatarView;
import com.example.hello.FeedContentActivity;
import com.example.hello.PasswordRecoverActivity;
import com.example.hello.R;
import com.example.hello.api.Page;
import com.example.hello.api.Server;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class FeedListFragment extends Fragment {
	View view;
	ListView listView;
	View btnLoadMore;
	TextView textLoadMore;
	
	List<Article> data;
	int page=0;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		if(view==null){
			
			view= inflater.inflate(R.layout.fragment_page_feed_list, null);
			btnLoadMore=inflater.inflate(R.layout.widget_load_more_button, null);
			textLoadMore=(TextView)btnLoadMore.findViewById(R.id.text);
			
			
			listView=(ListView)view.findViewById(R.id.list);
			listView.addFooterView(btnLoadMore);
			listView.setAdapter(listAdapter);
			
			listView.setOnItemClickListener(new OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					onItemClicked(position);					
				}

			});
			
			btnLoadMore.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					loadmore();
					
				}
			});
		}
		
		return view;
	}
	
	
	BaseAdapter listAdapter=new BaseAdapter() {
		
		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view=null;
			
			if(convertView==null){
				LayoutInflater inflater=LayoutInflater.from(parent.getContext());
				view=inflater.inflate(R.layout.widget_feed_item, null);
			}else{
				view=convertView;
			}
			
			TextView textContext=(TextView)view.findViewById(R.id.text);
			TextView textTitle=(TextView)view.findViewById(R.id.title);
			TextView textAuthorName=(TextView)view.findViewById(R.id.username);
			TextView textDate=(TextView)view.findViewById(R.id.date);
			AvatarView avatar=(AvatarView)view.findViewById(R.id.avatar);
			
			Article article=data.get(position);
			
			textContext.setText(article.getText());
			textTitle.setText(article.getTitle());
			textAuthorName.setText(article.getAuthor().getName());
			avatar.load(article.getAuthor());
			
			String dateStr=DateFormat.format("yyyy-MM-dd hh:mm", article.getCreateDate()).toString();
			textDate.setText(dateStr);
			
			return view;
		}
		
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data==null ? 0:data.size();
		}
				
	};
	
	void onItemClicked(int position){
		Article text=data.get(position);
		Intent itnt=new Intent(this.getActivity(),FeedContentActivity.class);
		itnt.putExtra("data", (Serializable)text);
		
		startActivity(itnt);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		reload();
	}
	
	void reload(){
		Request request=Server.requestBuilderWithApi("feeds")
				.get()
				.build();
		
		Server.getSharedClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				try {
					final Page<Article> data=new ObjectMapper()
							.readValue(arg1.body().string(), new TypeReference<Page<Article>>() {});
					
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							FeedListFragment.this.page=data.getNumber();
							FeedListFragment.this.data=data.getContent();
							listAdapter.notifyDataSetInvalidated();
							
						}
					});
				} catch (final Exception e) {
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							new AlertDialog.Builder(getActivity())
							.setMessage(e.getMessage())
							.show();
							
						}
					});
				}
				
			}
			
			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						new AlertDialog.Builder(getActivity())
						.setMessage(arg1.getMessage())
						.show();
					}
				});
				
			}
		});
	}
	
	void loadmore(){
		btnLoadMore.setEnabled(false);
		textLoadMore.setText("载入中");
		Request request=Server.requestBuilderWithApi("feeds/"+(page+1)).get().build();
		Server.getSharedClient().newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Call arg0, Response arg1) throws IOException {
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						btnLoadMore.setEnabled(true);
						textLoadMore.setText("加载更多");
						
					}
				});
				
				try {
					final Page<Article> feeds=new ObjectMapper().readValue(arg1.body().string() ,new TypeReference<Page<Article>>() {});
					if(feeds.getNumber()>page){
						
						
						getActivity().runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								if(data==null){
									data=feeds.getContent();
								}else{
									data.addAll(feeds.getContent());
									
								}
								
								page=feeds.getNumber();
								listAdapter.notifyDataSetChanged();
								
							}
						});
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						btnLoadMore.setEnabled(true);
						textLoadMore.setText("加载更多");
						
					}
				});
				
			}
		});
	}
}
