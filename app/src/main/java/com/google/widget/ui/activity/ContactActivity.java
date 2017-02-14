package com.google.widget.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.widget.R;
import com.google.widget.base.BaseActivity;
import com.google.widget.base.adapter.ViewHolder;
import com.google.widget.base.adapter.abslistview.CommonAdapter;
import com.google.widget.domain.ContactInfo;
import com.google.widget.engine.ContactInfoParser;
import com.google.widget.utils.ColorGenerator;
import com.google.widget.utils.PinyinUtils;
import com.google.widget.view.QuickIndexBar;
import com.google.widget.view.QuickIndexBar.OnLetterUpdateListener;
import com.google.widget.view.SearchEditText;
import com.google.widget.view.TextDrawable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContactActivity extends BaseActivity implements OnItemClickListener {

	@Bind(R.id.lv_contact)
	ListView lv_contact;
	@Bind(R.id.tv_center)
	TextView tv_center;
	@Bind(R.id.bar)
	QuickIndexBar bar;
	@Bind(R.id.et_search)
	SearchEditText et_search;

	private Handler mHandler = new Handler();
	private List<ContactInfo> list;
	private ContactAdapter adapter;
	private ColorGenerator mGenerator = ColorGenerator.DEFAULT;
	private SparseArray<TextDrawable> array;
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_contact);
		ButterKnife.bind(this);
	}

	@Override
	public void initData() {
		list = ContactInfoParser.findAll(this);
		Collections.sort(list);
		adapter = new ContactAdapter(ContactActivity.this, R.layout.item_contact, list);
		lv_contact.setAdapter(adapter);

		array = new SparseArray<>(list.size());
		for (int i=0; i<list.size(); i++){
			String text = list.get(i).name.substring(0,1);
			array.put(i,TextDrawable.builder().buildRound(text,mGenerator.getColor(text)));
		}
	}

	@Override
	public void initListener() {
		bar.setListener(new OnLetterUpdateListener() {
			@Override
			public void onLetterUpdate(String letter) {
				showLetter(letter);
				for (int i = 0; i < list.size(); i++) {
					ContactInfo person = list.get(i);
					String l = person.pinyin.charAt(0) + "";
					if (TextUtils.equals(letter, l)) {
						lv_contact.setSelection(i);
						break;
					}
				}
			}
		});
		
		lv_contact.setOnItemClickListener(this);

		// 根据输入框输入值的改变来过滤搜索
		et_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent();
		intent.putExtra("phone", list.get(position).phone);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	/**
	 * 显示字母
	 * 
	 * @param letter
	 */
	protected void showLetter(String letter) {
		tv_center.setVisibility(View.VISIBLE);
		tv_center.setText(letter);
		bar.setBackgroundColor(Color.rgb(191, 191, 191));

		mHandler.removeCallbacksAndMessages(null);
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				tv_center.setVisibility(View.GONE);
				bar.setBackgroundColor(Color.TRANSPARENT);
			}
		}, 250);

	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<ContactInfo> filterDateList = new ArrayList<ContactInfo>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = list;
		} else {
			filterDateList.clear();
			for (ContactInfo contactinfo : list) {
				String name = contactinfo.name;
				if (name.indexOf(filterStr.toString()) != -1
						|| PinyinUtils.getPinyin(name).startsWith(filterStr.toString())) {
					filterDateList.add(contactinfo);
				}
			}
		}

		Collections.sort(filterDateList);
		adapter.updateListView(filterDateList);
	}

	private class ContactAdapter extends CommonAdapter<ContactInfo> {

		public ContactAdapter(Context context, int layoutId, List<ContactInfo> datas) {
			super(context, layoutId, datas);
		}

		public void updateListView(List<ContactInfo> list) {
			mDatas = list;
			notifyDataSetChanged();
		}

		@Override
		public void convert(ViewHolder holder, ContactInfo contactInfo) {
			int position = holder.getPos();
			String str = null;
			String currentLetter = contactInfo.pinyin.charAt(0) + "";
			// 根据上一个首字母,决定当前是否显示字母
			if (position == 0) {
				str = currentLetter;
			} else {
				// 上一个人的拼音的首字母
				String preLetter = mDatas.get(position - 1).pinyin.charAt(0) + "";
				if (!TextUtils.equals(preLetter, currentLetter)) {
					str = currentLetter;
				}
			}

			holder.setVisible(R.id.tv_index, str == null ? false : true);
			holder.setText(R.id.tv_index, currentLetter);
			holder.setImageDrawable(R.id.icon,array.get(position));
			holder.setText(R.id.tv_name,contactInfo.name);
			holder.setText(R.id.tv_number, contactInfo.phone);
		}
	}

}
