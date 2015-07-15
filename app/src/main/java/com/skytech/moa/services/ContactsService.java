package com.skytech.moa.services;

import com.skytech.android.http.ArkHttpClient;
import com.skytech.android.http.HttpCache;
import com.skytech.moa.API;
import com.skytech.moa.model.Contact;
import com.skytech.moa.view.IContactsView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ContactsService {
    private ArkHttpClient httpClient;
    private List<Contact> contactList;
    private IContactsView view;

    public ContactsService(IContactsView view) {
        this.view = view;
        httpClient = new HttpCache();
        contactList = new ArrayList<>();
    }

    public void loadContacts(JSONObject params) {
        view.showLoading();
        httpClient.get(API.GET_CONTACTS, new ArkHttpClient.HttpHandler() {
            @Override
            public void onSuccess(JSONObject response, boolean isInCache) {
                super.onSuccess(response, isInCache);
                JSONArray array = response.optJSONArray("list");
                if (null == array) {
                    view.loadFailure("加载失败");
                    return;
                }

                int length = array.length();
                for (int i = 0; i < length; i++) {
                    contactList.add(new Contact(array.optJSONObject(i)));
                }
                view.loadSuccess(contactList);
            }

            @Override
            public void onFailure(String statusCode, String error) {
                super.onFailure(statusCode, error);
                view.loadFailure(error);
            }
        }, "1");
    }
}
