package com.skytech.moa.view;


import com.skytech.moa.model.NewsCommentInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/6/2.
 */
public interface INewsCommentView {

    void loadNewsCommentSuccess(ArrayList<NewsCommentInfo> list, boolean isMore);

    void loadNewsCommentFailure(String error);

}
