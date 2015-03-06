/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wenwen.chatuidemo.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.wenwen.chatui.debug.DebugLog;
import com.wenwen.chatuidemo.R;
import com.wenwen.chatuidemo.adapter.BothSexAdapter;
import com.wenwen.chatuidemo.adapter.ChildAdapter;
import com.wenwen.chatuidemo.adapter.FeelingsAdapter;
import com.wenwen.chatuidemo.adapter.HotspotAdapter;
import com.wenwen.chatuidemo.domain.Notice;
import com.wenwen.chatuidemo.utils.DialogUtil;
import com.wenwen.chatuidemo.utils.JsonUtil;
import com.wenwen.chatuidemo.utils.StringUtil;

/**
 * 新闻
 * 
 * @author Administrator
 * 
 */
public class NewFragment extends Fragment implements OnClickListener,
        OnCheckedChangeListener {
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButton1;
    private RadioButton mRadioButton2;
    private RadioButton mRadioButton3;
    private RadioButton mRadioButton4;
    private ImageView mImageView;
    private float mCurrentCheckedRadioLeft;//
    private HorizontalScrollView mHorizontalScrollView;//
    private ViewPager mViewPager; //

    private View viewDefault; // 初始视图
    private View hotview;
    private View childview;
    private View bothsexview;
    private View feelview;

    private ListView hotListView;
    private ListView bothsexListView;
    private ListView childListView;
    private ListView feelListView;
    private ArrayList<View> mViews;//

    private Dialog progressDialog;
    private int noticeLimit = 0;
    private List<Notice> data = new ArrayList<Notice>();
    private int page = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater
                .inflate(R.layout.fragment_news_layout, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        init();
        iniListener();
        iniVariable();
        mRadioButton1.setChecked(true);
        mViewPager.setCurrentItem(1);
        mCurrentCheckedRadioLeft = getCurrentCheckedRadioLeft();
    }

    private Handler hostpotrunhandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case 1:
                progressDialog.dismiss();
                break;
            case 2:
                progressDialog.dismiss();
                hotListView.setAdapter(new HotspotAdapter(getActivity(), data));
                noticeLimit = data.size();
                break;
            case 3:
                progressDialog.dismiss();
                break;
            case 4:
                progressDialog.dismiss();
                break;

            }
        }
    };

    private void iniVariable() {
        // TODO Auto-generated method stub
        viewDefault = getActivity().getLayoutInflater().inflate(
                R.layout.layout_0, null);
        // 初始化页面加载数据
        hotview = getActivity().getLayoutInflater().inflate(R.layout.layout_1,
                null);
        childview = getActivity().getLayoutInflater().inflate(
                R.layout.layout_2, null);
        bothsexview = getActivity().getLayoutInflater().inflate(
                R.layout.layout_3, null);
        feelview = getActivity().getLayoutInflater().inflate(R.layout.layout_4,
                null);
        hotListView = (ListView) hotview.findViewById(R.id.news_list);

        // TODO Auto-generated method stub
        mViews = new ArrayList<View>();
        mViews.add(viewDefault);
        mViews.add(hotview);
        mViews.add(childview);
        mViews.add(bothsexview);
        mViews.add(feelview);
        mViewPager.setAdapter(new MyPagerAdapter());
        progressDialog = DialogUtil.createProgressDialog(getActivity(),
                "正在通信，请稍后...");

        ImageView img = (ImageView) progressDialog
                .findViewById(R.id.progress_img);
        Animation animation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.progress_anim);
        img.startAnimation(animation);
        progressDialog.show();
        Thread thread = new Thread(hostpotrun);
        thread.start();

    }

    private Handler bothsexhandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case 1:
                progressDialog.dismiss();
                break;
            case 2:
                progressDialog.dismiss();
                noticeLimit = data.size();
                bothsexListView.setAdapter(new BothSexAdapter(getActivity(),data));
                break;
            case 3:
                progressDialog.dismiss();
                break;
            case 4:
                progressDialog.dismiss();
                break;

            }
        }
    };

    private Handler feelhandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case 1:
                progressDialog.dismiss();
                break;
            case 2:
                progressDialog.dismiss();
                noticeLimit = data.size();
                feelListView
                        .setAdapter(new FeelingsAdapter(getActivity(), data));
            case 3:
                progressDialog.dismiss();
                break;
            case 4:
                progressDialog.dismiss();
                break;

            }
        }
    };

    private Handler childhandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case 1:
                progressDialog.dismiss();
                break;
            case 2:
                progressDialog.dismiss();
                noticeLimit = data.size();
                childListView.setAdapter(new ChildAdapter(getActivity(), data));
                break;
            case 3:
                progressDialog.dismiss();
                break;
            case 4:
                progressDialog.dismiss();
                break;

            }
        }
    };

    private Runnable hostpotrun = new Runnable() {
        @Override
        public void run() {
            try {
                // String json =
                // HttpRequest.sendGetRequest(Setting.GET_NOTICE_INFO_LIST_URL,
                // params);
                String json = "{\"result\":\"true\",\"information\":[{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"英国足球巨星大卫·贝克汉姆来 到北京\",\"subview\":\"“中国青少年足球发展及中超联赛推广大使”新闻发布会，有了新身\"},{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"习近平今日启程访问俄罗斯 夫人彭丽媛将陪同\",\"subview\":\"国家主席习近平22日上午乘专机离开北京，对俄罗斯、坦桑尼亚、南非、刚果共和国进行国事访问，并出席在南非德班举行的金砖国家领导人第五次会晤。彭丽媛、王沪宁、栗战书、杨洁篪等陪同出访。\"},{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"盛光祖任董事长\",\"subview\":\"原铁道部人士透露，铁道部分拆后成立的中国铁路\"},{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"xxxxx\",\"subview\":\"盛光祖任董事长\"},{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"原铁道部人士透露，铁道部分拆后成立的中国铁路\",\"subview\":\"这是假数据\"},{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"原铁道部人士透露，铁道部分拆后成立的中国铁路\",\"subview\":\"这是假数据\"},{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"原铁道部人士透露，铁道部分拆后成立的中国铁路\",\"subview\":\"这是假数据\"},{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"原铁道部人士透露，铁道部分拆后成立的中国铁路\",\"subview\":\"这是假数据\"},{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"原铁道部人士透露，铁道部分拆后成立的中国铁路\",\"subview\":\"这是假数据\"}]}";
                if (StringUtil.isNotEmpty(json)) {
                    System.out.println("json ==== " + json);
                    // 重新加载时json不为空认为是取到第一页的数据
                    page = 1;
                    List<Notice> list = JsonUtil.noticeListFromJson(json);
                    if (list != null) {
                        data = list;
                        if (list.size() < 6) {
                            hostpotrunhandler.sendEmptyMessage(2);
                        } else {
                            hostpotrunhandler.sendEmptyMessage(2);
                        }

                    } else {
                        // 加载结束了，没有加载更多的选项出现
                        hostpotrunhandler.sendEmptyMessage(1);
                    }
                } else {
                    hostpotrunhandler.sendEmptyMessage(3);
                }

            } catch (Exception e) {
                e.printStackTrace();
                hostpotrunhandler.sendEmptyMessage(3);
            }
        }
    };
    private Runnable childunnable = new Runnable() {
        @Override
        public void run() {
            try {
                String json = "{\"result\":\"true\",\"information\":[{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"英国足球巨星大卫·贝克汉姆来 到北京\",\"subview\":\"“中国青少年足球发展及中超联赛推广大使”新闻发布会，有了新身\"},{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"习近平今日启程访问俄罗斯 夫人彭丽媛将陪同\",\"subview\":\"国家主席习近平22日上午乘专机离开北京，对俄罗斯、坦桑尼亚、南非、刚果共和国进行国事访问，并出席在南非德班举行的金砖国家领导人第五次会晤。彭丽媛、王沪宁、栗战书、杨洁篪等陪同出访。\"},{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"盛光祖任董事长\",\"subview\":\"原铁道部人士透露，铁道部分拆后成立的中国铁路\"},{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"xxxxx\",\"subview\":\"盛光祖任董事长\"},{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"原铁道部人士透露，铁道部分拆后成立的中国铁路\",\"subview\":\"这是假数据\"},{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"原铁道部人士透露，铁道部分拆后成立的中国铁路\",\"subview\":\"这是假数据\"},{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"原铁道部人士透露，铁道部分拆后成立的中国铁路\",\"subview\":\"这是假数据\"},{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"原铁道部人士透露，铁道部分拆后成立的中国铁路\",\"subview\":\"这是假数据\"},{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"原铁道部人士透露，铁道部分拆后成立的中国铁路\",\"subview\":\"这是假数据\"}]}";
                if (StringUtil.isNotEmpty(json)) {
                    System.out.println("json ==== " + json);
                    // 重新加载时json不为空认为是取到第一页的数据
                    page = 1;
                    List<Notice> list = JsonUtil.noticeListFromJson(json);
                    if (list != null) {
                        data = list;
                        if (list.size() < 6) {
                            childhandler.sendEmptyMessage(2);
                        } else {
                            childhandler.sendEmptyMessage(2);
                        }

                    } else {
                        // 加载结束了，没有加载更多的选项出现
                        childhandler.sendEmptyMessage(1);
                    }
                } else {
                    childhandler.sendEmptyMessage(3);
                }

            } catch (Exception e) {
                e.printStackTrace();
                childhandler.sendEmptyMessage(3);
            }
        }
    };

    private Runnable hotspot = new Runnable() {
        @Override
        public void run() {
            // Map<String,String> params = new HashMap<String,String>();
            // params.put("type", ""+noticeType);
            // params.put("limit", ""+noticeLimit);
            try {
                // String json =
                // HttpRequest.sendGetRequest(Setting.GET_NOTICE_INFO_LIST_URL,
                // params);
                String json = "{\"result\":\"true\",\"information\":[{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"英国足球巨星大卫·贝克汉姆来 到北京\",\"subview\":\"“中国青少年足球发展及中超联赛推广大使”新闻发布会，有了新身\"},{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"传铁路总公司为正部级 \",\"subview\":\"原铁道部人士透露，铁道部分拆后成立的中国铁路\"},{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"盛光祖任董事长\",\"subview\":\"原铁道部人士透露，铁道部分拆后成立的中国铁路\"},{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"xxxxx\",\"subview\":\"盛光祖任董事长\"},{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"原铁道部人士透露，铁道部分拆后成立的中国铁路\",\"subview\":\"这是假数据\"},{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"原铁道部人士透露，铁道部分拆后成立的中国铁路\",\"subview\":\"这是假数据\"},{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"原铁道部人士透露，铁道部分拆后成立的中国铁路\",\"subview\":\"这是假数据\"},{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"原铁道部人士透露，铁道部分拆后成立的中国铁路\",\"subview\":\"这是假数据\"},{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"原铁道部人士透露，铁道部分拆后成立的中国铁路\",\"subview\":\"这是假数据\"}]}";
                if (StringUtil.isNotEmpty(json)) {
                    System.out.println("json ==== " + json);
                    // 重新加载时json不为空认为是取到第一页的数据
                    page = 1;
                    List<Notice> list = JsonUtil.noticeListFromJson(json);
                    if (list != null) {
                        data = list;
                        if (list.size() < 6) {
                            bothsexhandler.sendEmptyMessage(2);
                        } else {
                            bothsexhandler.sendEmptyMessage(2);
                        }

                    } else {
                        // 加载结束了，没有加载更多的选项出现
                        bothsexhandler.sendEmptyMessage(1);
                    }
                } else {
                    bothsexhandler.sendEmptyMessage(3);
                }

            } catch (Exception e) {
                e.printStackTrace();
                bothsexhandler.sendEmptyMessage(3);
            }
        }
    };
    private Runnable feelingsrung = new Runnable() {
        @Override
        public void run() {
            try {
                String json = "{\"result\":\"true\",\"information\":[{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"英国足球巨星大卫·贝克汉姆来 到北京\",\"subview\":\"“中国青少年足球发展及中超联赛推广大使”新闻发布会，有了新身\"},{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"传铁路总公司为正部级 \",\"subview\":\"原铁道部人士透露，铁道部分拆后成立的中国铁路\"},{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"盛光祖任董事长\",\"subview\":\"原铁道部人士透露，铁道部分拆后成立的中国铁路\"},{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"xxxxx\",\"subview\":\"盛光祖任董事长\"},{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"原铁道部人士透露，铁道部分拆后成立的中国铁路\",\"subview\":\"这是假数据\"},{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"原铁道部人士透露，铁道部分拆后成立的中国铁路\",\"subview\":\"这是假数据\"},{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"原铁道部人士透露，铁道部分拆后成立的中国铁路\",\"subview\":\"这是假数据\"},{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"原铁道部人士透露，铁道部分拆后成立的中国铁路\",\"subview\":\"这是假数据\"},{\"date\":\"2012-01-14\",\"information_id\":111,\"title\":\"原铁道部人士透露，铁道部分拆后成立的中国铁路\",\"subview\":\"这是假数据\"}]}";
                if (StringUtil.isNotEmpty(json)) {
                    System.out.println("json ==== " + json);
                    // 重新加载时json不为空认为是取到第一页的数据
                    page = 1;
                    List<Notice> list = JsonUtil.noticeListFromJson(json);
                    if (list != null) {
                        data = list;
                        if (list.size() < 6) {
                            System.err.println("====================");
                            feelhandler.sendEmptyMessage(2);
                        } else {
                            feelhandler.sendEmptyMessage(2);
                        }

                    } else {
                        // 加载结束了，没有加载更多的选项出现
                        feelhandler.sendEmptyMessage(1);
                    }
                } else {
                    feelhandler.sendEmptyMessage(3);
                }

            } catch (Exception e) {
                e.printStackTrace();
                feelhandler.sendEmptyMessage(3);
            }
        }
    };

    /**
     * ViewPager
     * 
     * @author
     * 
     */
    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(View v, int position, Object obj) {
            // TODO Auto-generated method stub
            ((ViewPager) v).removeView(mViews.get(position));
        }

        @Override
        public void finishUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mViews.size();
        }

        @Override
        public Object instantiateItem(View v, int position) {

            ((ViewPager) v).addView(mViews.get(position));
            return mViews.get(position);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

    }

    private float getCurrentCheckedRadioLeft() {
        // TODO Auto-generated method stub
        if (mRadioButton1.isChecked()) {
            return getResources().getDimension(R.dimen.rdo1);
        } else if (mRadioButton2.isChecked()) {
            return getResources().getDimension(R.dimen.rdo2);
        } else if (mRadioButton3.isChecked()) {
            return getResources().getDimension(R.dimen.rdo3);
        } else if (mRadioButton4.isChecked()) {
            return getResources().getDimension(R.dimen.rdo4);
        }
        return 0f;
    }

    private void iniListener() {
        // TODO Auto-generated method stub
        mRadioGroup.setOnCheckedChangeListener(this);

        mViewPager.setOnPageChangeListener(new MyPagerOnPageChangeListener());
    }

    private void init() {
        // TODO Auto-generated method stub
        mRadioGroup = (RadioGroup) getView().findViewById(R.id.radioGroup);
        mRadioButton1 = (RadioButton) getView().findViewById(R.id.btn1);
        mRadioButton2 = (RadioButton) getView().findViewById(R.id.btn2);
        mRadioButton3 = (RadioButton) getView().findViewById(R.id.btn3);
        mRadioButton4 = (RadioButton) getView().findViewById(R.id.btn4);
        mImageView = (ImageView) getView().findViewById(R.id.img1);
        mHorizontalScrollView = (HorizontalScrollView) getView().findViewById(
                R.id.horizontalScrollView);
        mViewPager = (ViewPager) getView().findViewById(R.id.pager);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        AnimationSet _AnimationSet = new AnimationSet(true);
        TranslateAnimation _TranslateAnimation;
        if (checkedId == R.id.btn1) {
            _TranslateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, getResources().getDimension(R.dimen.rdo1), 0f, 0f);
            _AnimationSet.addAnimation(_TranslateAnimation);
            _AnimationSet.setFillBefore(false);
            _AnimationSet.setFillAfter(true);
            _AnimationSet.setDuration(100);
            mImageView.startAnimation(_AnimationSet);
            mViewPager.setCurrentItem(1);
            mRadioButton1.setTextColor(Color.RED);

        } else if (checkedId == R.id.btn2) {
            _TranslateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, getResources().getDimension(R.dimen.rdo2), 0f, 0f);
            _AnimationSet.addAnimation(_TranslateAnimation);
            _AnimationSet.setFillBefore(false);
            _AnimationSet.setFillAfter(true);
            _AnimationSet.setDuration(100);
            mImageView.startAnimation(_AnimationSet);
            mViewPager.setCurrentItem(2);
        } else if (checkedId == R.id.btn3) {
            _TranslateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, getResources().getDimension(R.dimen.rdo3), 0f, 0f);
            _AnimationSet.addAnimation(_TranslateAnimation);
            _AnimationSet.setFillBefore(false);
            _AnimationSet.setFillAfter(true);
            _AnimationSet.setDuration(100);
            mImageView.startAnimation(_AnimationSet);
            mViewPager.setCurrentItem(3);
        } else if (checkedId == R.id.btn4) {
            _TranslateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, getResources().getDimension(R.dimen.rdo4), 0f, 0f);
            _AnimationSet.addAnimation(_TranslateAnimation);
            _AnimationSet.setFillBefore(false);
            _AnimationSet.setFillAfter(true);
            _AnimationSet.setDuration(100);
            mImageView.startAnimation(_AnimationSet);
            mViewPager.setCurrentItem(4);
        }
        mCurrentCheckedRadioLeft = getCurrentCheckedRadioLeft();
        DebugLog.i("zj", "getCurrentCheckedRadioLeft="
                + getCurrentCheckedRadioLeft());
        DebugLog.i("zj",
                "getDimension=" + getResources().getDimension(R.dimen.rdo2));
        mHorizontalScrollView.smoothScrollTo((int) mCurrentCheckedRadioLeft
                - (int) getResources().getDimension(R.dimen.rdo2), 0);
    }

    /**
     * 
     */
    private class MyPagerOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        /**
         * 
         */
        @Override
        public void onPageSelected(int position) {
            System.out.println("position=>" + position);
            if (position == 0) {
                mViewPager.setCurrentItem(1);
            } else if (position == 1) {
                mRadioButton1.performClick();
            } else if (position == 2) {
                mRadioButton2.performClick();
            } else if (position == 3) {
                mRadioButton3.performClick();
            } else if (position == 4) {
                mRadioButton4.performClick();
                mViewPager.setCurrentItem(4);
            }
            // 移动加载子项页
            loadSubPager(position);
        }

    }

    /**
     * 新闻加载子选项卡
     */
    public void loadSubPager(int position) {

        if (position == 2) { // 育儿
            childListView = (ListView) childview.findViewById(R.id.news_list);
            // guoNeiListView.setDivider(null); //去除分割线
              childListView.setOnScrollListener(new OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view,
                        int scrollState) {
              
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                        int visibleItemCount, int totalItemCount) {

                }
            });
            progressDialog = DialogUtil.createProgressDialog(getActivity(),
                    "正在加载，请稍后...");
            ImageView img = (ImageView) progressDialog
                    .findViewById(R.id.progress_img);
            Animation animation = AnimationUtils.loadAnimation(getActivity(),
                    R.anim.progress_anim);
            img.startAnimation(animation);
            progressDialog.show();
            Thread thread = new Thread(childunnable);
            thread.start();
        } else if (position == 3) { // 两性
            bothsexListView = (ListView) bothsexview.findViewById(R.id.news_list);
            // guoJiListView.setDivider(null); //去除分割线
            bothsexListView.setOnScrollListener(new OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view,
                        int scrollState) {
                
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                        int visibleItemCount, int totalItemCount) {

                }
            });
            progressDialog = DialogUtil.createProgressDialog(getActivity(),
                    "正在加载，请稍后...");
            ImageView img = (ImageView) progressDialog
                    .findViewById(R.id.progress_img);
            Animation animation = AnimationUtils.loadAnimation(getActivity(),
                    R.anim.progress_anim);
            img.startAnimation(animation);
            progressDialog.show();
            Thread thread = new Thread(hotspot);
            thread.start();
        } else if (position == 4) { // 情感
            // junShiListView.setDivider(null); //去除分割线
            feelListView = (ListView) feelview.findViewById(R.id.news_list);
            feelListView.setOnScrollListener(new OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view,
                        int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                        int visibleItemCount, int totalItemCount) {

                }
            });
            progressDialog = DialogUtil.createProgressDialog(getActivity(),
                    "正在加载，请稍后...");
            ImageView img = (ImageView) progressDialog.findViewById(R.id.progress_img);
            Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.progress_anim);
            img.startAnimation(animation);
            progressDialog.show();
            Thread thread = new Thread(feelingsrung);
            thread.start();
        }
    }
}
