package com.skytech.moa.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import com.skytech.android.ArkActivity;
import com.skytech.android.util.CustomProgress;
import com.skytech.android.util.CustomToast;
import com.skytech.moa.R;
import com.skytech.moa.model.Contact;
import com.skytech.moa.services.ContactsService;
import com.skytech.moa.view.IContactsView;
import com.skytech.moa.widgets.EditTextWithDelete;
import com.skytech.moa.widgets.indelible.CharacterParser;
import com.skytech.moa.widgets.indelible.IndelibleAdapter;
import com.skytech.moa.widgets.indelible.IndelibleBar;
import com.skytech.moa.widgets.indelible.PinyinComparator;
import com.skytech.moa.widgets.swipemenulistview.SwipeMenu;
import com.skytech.moa.widgets.swipemenulistview.SwipeMenuCreator;
import com.skytech.moa.widgets.swipemenulistview.SwipeMenuItem;
import com.skytech.moa.widgets.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * activity of contacts
 */
public class Contacts extends ArkActivity implements IContactsView {
    private ContactsService service;

    private TextView titleView;
    private SwipeMenuListView listView;
    private IndelibleBar indelibleBar;
    private TextView dialog;
    private EditTextWithDelete clearEditText;
    private View noNetwork;
    private View noData;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    private IndelibleAdapter adapter;
    private List<Contact> sourceDateList;

    public static Intent createContactsIntent(Context context) {
        Intent intent = new Intent(context, Contacts.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);
        setTitle(getString(R.string.contacts));
        initView();
        initService();
        reload();
    }

    private void setTitle(String title) {
        titleView.setText(title);
    }

    private void initService() {
        service = new ContactsService(this);

        sourceDateList = new ArrayList<>();
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
    }

    private void reload() {
        service.loadContacts(null);
    }

    public Activity getContext() {
        return this;
    }

    private void initView() {
        titleView = (TextView) getContext().findViewById(R.id.title);
        listView = (SwipeMenuListView) getContext().findViewById(R.id.contact_list);
        indelibleBar = (IndelibleBar) getContext().findViewById(R.id.sidrbar);
        dialog = (TextView) getContext().findViewById(R.id.dialog);
        noNetwork = findViewById(R.id.no_network);
        noData = findViewById(R.id.no_data);

        noNetwork.setOnClickListener(errorViewClick());
        noData.setOnClickListener(errorViewClick());

        indelibleBar.setTextView(dialog);

        adapter = new IndelibleAdapter(getContext());
        listView.setAdapter(adapter);

        // set creator
        listView.setMenuCreator(prepareSwipeMenuCreator());

        // step 2. listener item click event
        listView.setOnMenuItemClickListener(getOnMenuItemClickListener());

        // set SwipeListener
        listView.setOnSwipeListener(getOnSwipeListener());

        //设置右侧触摸监听
        indelibleBar.setOnTouchingLetterChangedListener(getOnTouchingLetterChangedListener());

        listView.setOnItemClickListener(getItemClickListener());

        clearEditText = (EditTextWithDelete) findViewById(R.id.filter_edit);

        //根据输入框输入值的改变来过滤搜索
        clearEditText.addTextChangedListener(clearTextWatcher());
    }

    private View.OnClickListener errorViewClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reload();
            }
        };
    }

    private AdapterView.OnItemClickListener getItemClickListener() {
        return new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                edit(((Contact) adapter.getItem(position)));
            }
        };
    }

    private IndelibleBar.OnTouchingLetterChangedListener getOnTouchingLetterChangedListener() {
        return new IndelibleBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    listView.setSelection(position);
                }
            }
        };
    }

    private SwipeMenuListView.OnSwipeListener getOnSwipeListener() {
        return new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        };
    }

    private SwipeMenuCreator prepareSwipeMenuCreator() {
        // step 1. create a MenuCreator
        return new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "sms" item
                SwipeMenuItem smsItem = new SwipeMenuItem(
                        getContext());
                // set item background
                /*smsItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));*/
                // set item width
                smsItem.setWidth(dp2px(90));
                // set item title font color
                smsItem.setIcon(R.drawable.ic_contact_message_gray);
                // add to menu
                menu.addMenuItem(smsItem);

                // create "call" item
                SwipeMenuItem callItem = new SwipeMenuItem(
                        getContext());
                // set item width
                callItem.setWidth(dp2px(90));
                // set a icon
                callItem.setIcon(R.drawable.ic_contact_phone_gray);
                // add to menu
                menu.addMenuItem(callItem);

                /*// create "edit" item
                SwipeMenuItem editItem = new SwipeMenuItem(
                        getContext());
                // set item width
                editItem.setWidth(dp2px(90));
                // set a icon
                editItem.setIcon(R.drawable.ic_contact__editor_gray);
                // add to menu
                menu.addMenuItem(editItem);*/
            }
        };
    }

    private SwipeMenuListView.OnMenuItemClickListener getOnMenuItemClickListener() {
        return new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Contact contact = (Contact) adapter.getItem(position);
                switch (index) {
                    case 0:
                        // open
                        sms(contact);
                        break;
                    case 1:
                        // call
                        call(contact);
                        break;
                    case 2:
                        // edit
                        edit(contact);
                        break;
                }
                return false;
            }
        };
    }

    private void edit(Contact contact) {
        startActivity(ContactsDetail.createIntent(getContext(), contact));
    }

    private void sms(Contact contact) {
        String phoneNo = contact.getPhoneList().get(0).getPhone();
        if (phoneNo == null || TextUtils.isEmpty(phoneNo.trim())) {
            CustomToast.show(getContext(), "没有电话号码");
        } else {
            Uri uri = Uri.parse("smsto:" + phoneNo);
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
            startActivity(sendIntent);
        }
    }

    private void call(Contact contact) {
        String phoneNo = contact.getPhoneList().get(0).getPhone();
        if (phoneNo == null || TextUtils.isEmpty(phoneNo.trim())) {
            CustomToast.show(getContext(), "没有电话号码");
        } else {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNo)));
        }
    }


    private TextWatcher clearTextWatcher() {
        return new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<Contact> filterDateList = new ArrayList<Contact>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = sourceDateList;
        } else {
            filterDateList.clear();
            for (Contact contact : sourceDateList) {
                String name = contact.getName();
                if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(contact);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateList(filterDateList);
        adapter.notifyDataSetChanged();
    }

    private void refreshList(List<Contact> contactList) {
        // 根据a-z进行排序源数据
        sourceDateList = contactList;
        Collections.sort(contactList, pinyinComparator);
        listView.setAdapter(adapter);

        adapter.updateList(contactList);
        adapter.notifyDataSetChanged();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private void showNoDataView() {
        noData.setVisibility(View.VISIBLE);
    }

    private void hideNoDataView() {
        noData.setVisibility(View.GONE);
    }

    private void showNoNetWork() {
        noNetwork.setVisibility(View.VISIBLE);
    }

    private void hideNoNetWork() {
        noNetwork.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        CustomProgress.showProgress(getContext());
    }

    @Override
    public void loadSuccess(List<Contact> contacts) {
        CustomProgress.hideProgress();
        refreshList(contacts);
        if (contacts.isEmpty()) {
            showNoDataView();
        } else {
            hideNoNetWork();
            hideNoDataView();
        }
    }

    @Override
    public void loadFailure(String msg) {
        CustomToast.show(getContext(), msg);
        showNoNetWork();
        CustomProgress.hideProgress();
    }
}
