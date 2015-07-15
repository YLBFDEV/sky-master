package com.skytech.moa.view;

import com.skytech.moa.model.Contact;

import java.util.List;

public interface IContactsView {
    void loadSuccess(List<Contact> contacts);

    void loadFailure(String msg);

    void showLoading();
}
