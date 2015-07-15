package com.skytech.moa.doc.widgets;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.skytech.android.util.AndroidVersionCheckUtils;
import com.skytech.moa.ArkBaseDialog;
import com.skytech.moa.adapter.MultipleChoiceAdapter;
import com.skytech.moa.adapter.SingleChoiceAdapter;
import com.skytech.android.adapter.SkyAdapter;
import com.skytech.moa.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 字典项选择框
 */
public class FieldChoiceView extends BaseFieldView implements View.OnClickListener {
    private String valueIds = "";
    private String subValueIds = "";
    private static final String SEPARATOR = ",";

    private Context mContext;
    private SkyAdapter adapter;
    private ListView list;
    private Dialog dialog;
    private TextView subValueView;
    private String itemValue = "value";
    private String itemKey = "key";
    private String itemChildren = "children";
    private String defaultValue = "";
    private boolean isMultipleChoice;
    private JSONArray options;
    private JSONArray subOptions;
    private JSONArray selectedOptions;
    private JSONArray subSelectedOptions;

    public FieldChoiceView(Context context) {
        this(context, null, -1);
        mContext = context;
    }

    public FieldChoiceView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
        mContext = context;
    }

    public FieldChoiceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.doc_field_choice, this);
        keyNameView = (TextView) findViewById(R.id.field_title);
        valueView = findViewById(R.id.field_value);
        subValueView = (TextView) findViewById(R.id.field_sub_value);
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.doc);
        isMultipleChoice = ta.getBoolean(R.styleable.doc_multipleChoice, false);
        //defaultValue = ta.getString(R.styleable.doc_defaultValue);
        initStyles(attrs);
        valueView.setOnClickListener(this);
        subValueView.setOnClickListener(this);

    }

    public void setIsMultiple(boolean isMultiple) {
        this.isMultipleChoice = isMultiple;
        if (isMultipleChoice) {
            adapter = new MultipleChoiceAdapter(mContext, itemValue);
        } else {
            adapter = new SingleChoiceAdapter(mContext, itemValue);
        }
    }

    public void setOptions(Object object) {
        options = (JSONArray) object;
    }

    @Override
    public void setValue(Object object) {
        if (object instanceof String) {
            ((TextView) valueView).setText((String) object);
        }
    }

    private String getValueText() {
        return ((TextView) valueView).getText().toString();
    }

    private void setSubValue(Object object) {
        if (object instanceof String) {
            ((TextView) subValueView).setText((String) object);
        }
    }

    @Override
    public void setValueId(Object object) {
        if (object instanceof String) {
            String[] valueIdArray;
            valueIds = (String) object;
            if (valueIds.contains(SEPARATOR)) {
                valueIdArray = valueIds.split(SEPARATOR);
                for (int i = 0; i < valueIdArray.length; i++) {
                    setOptionByValueId(valueIdArray[i]);
                }
            } else {
                setOptionByValueId(valueIds);
            }
        } else if (object instanceof JSONArray) {
            selectedOptions = (JSONArray) object;
        }
        setKeyValue();
    }

    private void setOptionByValueId(String id) {
        if (null != options && options.length() > 0) {
            for (int i = 0; i < options.length(); i++) {
                JSONObject item = options.optJSONObject(i);
                if (item.optString(itemKey).equals(id)) {
                    setSelectedOptions(item);
                }
            }
        }
    }

    private void setSubValueId(Object object) {
        if (object instanceof String) {
            subValueIds = (String) object;
        }
    }

    private void setSubOptions(JSONArray subOptions) {
        if (null == subOptions) {
            subValueView.setVisibility(GONE);
            return;
        }
        this.subOptions = subOptions;
        subValueIds = "";
        subValueView.setText("");
        subValueView.setVisibility(VISIBLE);
    }

    @Override
    public Object getValue() {
        if (isMultipleChoice) {
            return getValueWhenIsMultipleChoice();
        } else {
            return getValueWhenIsSingleChoice();
        }
    }

    private Object getValueWhenIsSingleChoice() {
        if (TextUtils.isEmpty(valueIds)) {
            return defaultValue;
        } else {
            //without second grade option
            if (TextUtils.isEmpty(subValueIds)) {
                return valueIds;
            } else {
                JSONObject object = new JSONObject();
                try {
                    object.put(getKeyId() + "1", valueIds);
                    object.put(getKeyId() + "2", subValueIds);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return object;
            }
        }
    }

    private Object getValueWhenIsMultipleChoice() {
        if (null == selectedOptions || selectedOptions.length() == 0) {
            return defaultValue;
        }
        return selectedOptions;
    }

    @Override
    public void setHint(String hint) {
        ((TextView) valueView).setHint(hint);
    }

    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);
        valueView.setEnabled(editable);
    }

    private void setKeyValue() {
        String key = "";
        String value = "";

        if (null == selectedOptions) return;

        int length = selectedOptions.length();
        for (int i = 0; i < length; i++) {
            if (TextUtils.isEmpty(value)) {
                key = selectedOptions.optJSONObject(i).optString(itemKey, "");
                value = selectedOptions.optJSONObject(i).optString(itemValue, "");
            } else {
                key += SEPARATOR + selectedOptions.optJSONObject(i).optString(itemKey, "");
                value += SEPARATOR + selectedOptions.optJSONObject(i).optString(itemValue, "");
            }
        }
        valueIds = key;
        setValue(value);
    }

    private void setSelectedOptions(JSONObject object) {
        if (null == selectedOptions || !isMultipleChoice) {
            selectedOptions = new JSONArray();
        }
        selectedOptions.put(object);
    }

    private void setSubSelectedOptions(JSONObject object) {
        if (null == subSelectedOptions) {
            subSelectedOptions = new JSONArray();
        }
        subSelectedOptions.put(object);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.field_value:
                adapter.setData(options);
                adapter.setSelectedData(selectedOptions);
                if (options != null && options.length() > 0) {
                    showDialog();
                }
                list.setOnItemClickListener(getListItemClickListener());
                break;
            case R.id.field_sub_value:
                adapter.setData(subOptions);
                adapter.setSelectedData(subSelectedOptions);
                if (subOptions != null && subOptions.length() > 0) {
                    showDialog();
                }
                list.setOnItemClickListener(getSubListItemClickListener());
                break;
        }
    }

    private void showDialog() {
        if (dialog == null) {
            createDialog();
        }
        dialog.show();

        adapter.notifyDataSetChanged();
    }

    private void createDialog() {
        View searchView = LayoutInflater.from(mContext).inflate(R.layout.doc_field_server_choice, null);
        list = (ListView) searchView.findViewById(R.id.list);

        LinearLayout footerParent = new LinearLayout(mContext);
        list.addHeaderView(footerParent);
        list.setHeaderDividersEnabled(false);

        list.setAdapter(adapter);
        adapter.setListView(list);

        ArkBaseDialog.Builder builder = new ArkBaseDialog.Builder(mContext);
        builder.setTitle("请选择");
        builder.setContentView(searchView);
        builder.setAdapter(adapter);

        if (isMultipleChoice) {
            builder.setPositiveButton(R.drawable.button_blue, R.string.done, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    setKeyValue();
                    dialog.dismiss();
                }
            });
            list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        } else {
            list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }

        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);

    }

    private AdapterView.OnItemClickListener getListItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView ctv = (CheckedTextView) view;
                JSONObject jsonObject = options.optJSONObject(position - 1);

                setSubOptions(jsonObject.optJSONArray(itemChildren));

                if (isMultipleChoice == false) {
                    setSelectedOptions(jsonObject);
                    setKeyValue();
                    dialog.dismiss();
                } else {
                    if (!AndroidVersionCheckUtils.hasJellyBean()) {
                        ctv.toggle();
                    }
                    if (ctv.isChecked()) {
                        addSelectedItem(jsonObject);
                    } else {
                        removeSelectedByJSON(jsonObject);
                    }
                }
            }
        };
    }

    private AdapterView.OnItemClickListener getSubListItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView ctv = (CheckedTextView) view;
                JSONObject jsonObject = subOptions.optJSONObject(position - 1);

                if (isMultipleChoice == false) {
                    setSubSelectedOptions(jsonObject);
                    setSubValue(jsonObject.optString(itemValue, ""));
                    setSubValueId(jsonObject.optString(itemKey, ""));
                    dialog.dismiss();
                }
            }
        };
    }

    private void addSelectedItem(JSONObject jsonObject) {
        if (null == selectedOptions) {
            selectedOptions = new JSONArray();
        }
        selectedOptions.put(jsonObject);
        notifyDataSetChanged();
    }

    private void removeSelectedByJSON(JSONObject jsonObject) {
        if (null == selectedOptions) {
            selectedOptions = new JSONArray();
        }
        JSONArray tempArray = new JSONArray();
        for (int i = 0; i < selectedOptions.length(); i++) {
            if (selectedOptions.optJSONObject(i).optString(itemKey).equals(jsonObject.optString(itemKey))) continue;
            tempArray.put(selectedOptions.optJSONObject(i));
        }
        selectedOptions = tempArray;
        notifyDataSetChanged();
    }

    private void notifyDataSetChanged() {
        adapter.setSelectedData(selectedOptions);
        adapter.notifyDataSetChanged();
    }
}
