package com.skytech.moa.view;


import com.skytech.moa.model.NewsInfo;

import java.util.ArrayList;

public interface INewsView {

    void loadNewsSuccess(ArrayList<NewsInfo> list, boolean isMore);

    void loadNewsFailure(String error);
}
