package com.ryan.interviewtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ryan.interviewtest.adapter.MyPagerAdapter;
import com.ryan.interviewtest.base.FieldConfig;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private TextView mNetworkUnavailableTv;

    private List<MainFragment> fragments;
    private NetWorkChangedReceiver mNetWorkChangedReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNetworkUnavailableTv = (TextView) findViewById(R.id.network_unavailable_tv);
        mTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
        fragments = new ArrayList<>();
        MainFragment androidFragment = new MainFragment();
        androidFragment.setType(FieldConfig.TYPE_ANDROID);
        MainFragment iSOFragment = new MainFragment();
        iSOFragment.setType(FieldConfig.TYPE_IOS);
        MainFragment frontEndFragment = new MainFragment();
        frontEndFragment.setType(FieldConfig.TYPE_FRONT_END);
        fragments.add(androidFragment);
        fragments.add(iSOFragment);
        fragments.add(frontEndFragment);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),fragments));
        mTabLayout.setupWithViewPager(mViewPager);

        if (!isNetworkAvailable()){
            mNetworkUnavailableTv.setVisibility(View.VISIBLE);
        }

        mNetWorkChangedReceiver = new NetWorkChangedReceiver();

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mNetWorkChangedReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        } else {
            return false;
        }
    }

    public class NetWorkChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.equals(action, ConnectivityManager.CONNECTIVITY_ACTION)){
                if (!isNetworkAvailable()){
                    mNetworkUnavailableTv.setVisibility(View.VISIBLE);
                }else {
                    mNetworkUnavailableTv.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mNetWorkChangedReceiver);
    }
}
