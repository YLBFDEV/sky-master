package com.skytech.moa.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.skytech.android.ArkActivity;
import com.skytech.moa.doc.FormView;
import com.skytech.moa.doc.widgets.FieldTextView;
import com.skytech.android.http.SkyHttpClient;
import com.skytech.android.widgets.NoScrollListView;
import com.skytech.moa.API;
import com.skytech.moa.R;
import com.skytech.moa.model.Contact;
import com.skytech.moa.utils.Constant;
import com.skytech.moa.widgets.PhoneAdapter;

/**
 * contact detail
 */
public class ContactsDetail extends ArkActivity {
    private Contact contact;
    private int padding = 0;
    private TextView titleView;
    private FormView formView;
    ImageView photo;

    private DisplayImageOptions options;

    public static Intent createIntent(Context context, Contact contact) {
        Intent intent = new Intent(context, ContactsDetail.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.EXTRA_CONTACT, contact);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_detail);
        if (null == savedInstanceState) {
            contact = (Contact) getIntent().getSerializableExtra(Constant.EXTRA_CONTACT);
        } else {
            contact = (Contact) savedInstanceState.getSerializable(Constant.EXTRA_CONTACT);
        }
        setTitle(contact.getName());
        //padding = (int) getResources().getDimension(R.dimen.view_margin);
        initView();
        init();

        initImageLoader();
    }

    private void initView() {
        titleView = (TextView) findViewById(R.id.title);
        formView = (FormView) findViewById(R.id.form);
        photo = (ImageView) findViewById(R.id.photo);
    }

    private void setTitle(String title) {
        titleView.setText(title);
    }

    private void initImageLoader() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_head)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_load_error)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(20)).build();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Constant.EXTRA_CONTACT, contact);
    }

    private void init() {
        String imageUrl = SkyHttpClient.getInstance().getAppBaseUrl() + "/" + String.format(API.GET_FILE, contact.getPhotoId());
        ImageLoader.getInstance().displayImage(imageUrl, photo, options);
        addField("", contact.getName());
        addField("", contact.getDuty());
        addField("", contact.getDepartment());
        addEmptyField();
        addListView();
    }

    private void addEmptyField() {
        FieldTextView fieldView = new FieldTextView(this);
        fieldView.setBackgroundResource(R.color.transparent);
        fieldView.setPadding(padding, padding, padding, padding);
        formView.addView(fieldView);
    }

    private void addField(String keyName, String value) {
        FieldTextView fieldView = new FieldTextView(this);
        fieldView.setBackgroundResource(R.drawable.doc_field_bg);
        fieldView.setPadding(padding, padding, padding, padding);
        fieldView.setKeyName(keyName);
        fieldView.setValue(value);

        formView.addView(fieldView);
    }

    private void addListView() {
        PhoneAdapter adapter = new PhoneAdapter(this);
        NoScrollListView listView = new NoScrollListView(this);
        listView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        adapter.updateData(contact.getPhoneList());
        listView.setAdapter(adapter);
        formView.addView(listView);
    }
}
