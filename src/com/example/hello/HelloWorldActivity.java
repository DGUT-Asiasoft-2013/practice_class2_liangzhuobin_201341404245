package com.example.hello;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import fragments.MainTabbarFragment;
import fragments.MainTabbarFragment.OnNewClickedListener;
import fragments.MainTabbarFragment.OnTabSelectedListener;
import pages.FeedListFragment;
import pages.MyProfileFragment;
import pages.NoteListFragment;
import pages.SearchFragment;

public class HelloWorldActivity extends Activity {
	
	FeedListFragment contentFeedList = new FeedListFragment();
	NoteListFragment contentNoteList = new NoteListFragment();
	SearchFragment contentSearchPage = new SearchFragment();
	MyProfileFragment contentMyProfile = new MyProfileFragment();

	MainTabbarFragment tabbar;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_helloworld);
		tabbar = (MainTabbarFragment) getFragmentManager().findFragmentById(R.id.frag_tabbar);
		tabbar.setOnTabSelectedListener(new OnTabSelectedListener() {

			@Override
			public void onTabSelected(int index) {
				changeContentFragment(index);
			}
		});
		
		tabbar.setOnNewClickedListener(new OnNewClickedListener() {
			
			@Override
			public void onNewClicked() {
				bringUpEditor();
				
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		if(tabbar.getSelectedIndex()<0){
			tabbar.setSelectedItem(0);
		}
	}
	
	void changeContentFragment(int index){
		Fragment newFrag = null;

		switch (index) {
		case 0: newFrag = contentFeedList; break;
		case 1: newFrag = contentNoteList; break;
		case 2: newFrag = contentSearchPage; break;
		case 3: newFrag = contentMyProfile; break;

		default:break;
		}

		if(newFrag==null) return;

		getFragmentManager()
		.beginTransaction()
		.replace(R.id.content, newFrag)
		.commit();
	}
	void bringUpEditor(){
		Intent itnt = new Intent(this, NewContentActivity.class);
		startActivity(itnt);
		overridePendingTransition(R.anim.slide_in_bottom, R.anim.none);
	}
}
