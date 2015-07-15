package com.skytech.moa.widgets;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.skytech.moa.R;

public class ModuleItemView extends RelativeLayout {
    private TextView moduleName;
    private ImageView moduleIcon;
    private TextView todoNumber;
	private View todoContainer;

    public ModuleItemView(Activity activity) {
        super(activity);
        LayoutInflater.from(activity).inflate(R.layout.item_module, this);
        moduleName = (TextView) findViewById(R.id.module_name);
        moduleIcon = (ImageView) findViewById(R.id.module_image);
        todoNumber = (TextView) findViewById(R.id.todo_number);
		todoContainer = findViewById(R.id.todo_container);
    }

    public void setName(String name) {
        moduleName.setText(name);
    }

    public void setIcon(int resId) {
        moduleIcon.setImageResource(resId);
    }

    public void setTodoNumber(String count) {
		if ((null != count) && (!TextUtils.isEmpty(count)) && (!count.equals("0"))) {
			todoContainer.setVisibility(View.INVISIBLE);
            if (count.length() >= 3) {
                count = "99+";
            } 
			todoNumber.setText(count);
   		} else {
			todoContainer.setVisibility(View.INVISIBLE);
		}
    }	
}
