package com.skytech.moa.doc.widgets;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.skytech.moa.ArkBaseDialog;
import com.skytech.android.adapter.ArkAdapter;
import com.skytech.moa.R;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class FieldServerChoiceText extends BaseFieldView implements DialogInterface.OnClickListener, View.OnClickListener {
    private Context mContext;
    private String valueIds = "";

    private ListView list;
    private ServerChoicePresenter presenter;
    private ArkAdapter adapterList;
    private Dialog dialog;

    private String constName;
    private String itemText = "chinaname";
    private String itemKey = "id";
    private boolean isMultipleChoice;
    private JSONArray items = new JSONArray();
    private JSONArray serverItems = new JSONArray();
    private JSONArray selectedArray = new JSONArray();

    private List<String> relevanceControlNames;

    public FieldServerChoiceText(Context context) {
        this(context, null, -1);
        mContext = context;
    }

    public FieldServerChoiceText(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
        mContext = context;

    }

    public FieldServerChoiceText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.doc);
        if (getOrientation() == VERTICAL) {
            LayoutInflater.from(context).inflate(R.layout.doc_field_edit_ver, this);
        } else {
            LayoutInflater.from(context).inflate(R.layout.doc_field_edit, this);
        }

        keyNameView = (TextView) findViewById(R.id.field_title);
        valueView = findViewById(R.id.field_value);
        isMultipleChoice = ta.getBoolean(R.styleable.doc_multipleChoice, false);
        initStyles(attrs);
        valueView.setOnClickListener(this);
        valueView.setEnabled(false);
        setAdapterList();
        presenter = new ServerChoicePresenter();
    }

    public void setDictionaries(Object object) {
        constName = (String) object;
    }

    public void setTag(Object object) {
        valueView.setTag(object);
    }

    public Object getTag() {
        return valueView.getTag();
    }

    private void showDialog() {
        if (dialog == null) {
            createDialog();
        }
        dialog.show();
        loadList();
    }

    private void createDialog() {
        View searchView = LayoutInflater.from(mContext).inflate(R.layout.doc_field_server_choice, null);

        list = (ListView) searchView.findViewById(R.id.list);

        LinearLayout footerParent = new LinearLayout(mContext);
        list.addHeaderView(footerParent);
        list.setHeaderDividersEnabled(false);

        list.setAdapter(adapterList);

        if (isMultipleChoice) {
            list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        } else {
            list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView ctv = (CheckedTextView) view;
                JSONObject jsonObject = items.optJSONObject(position - 1);

                if (isMultipleChoice == false) {
                    selectedArray = new JSONArray();
                    selectedArray.put(jsonObject);
                    setValue(jsonObject.optString(itemText, ""));
                    setTag(jsonObject.optString(itemKey, ""));
                    dialog.dismiss();
                } else {
                    if (ctv.isChecked()) {
                        addSelectedItem(jsonObject);
                    } else {
                        removeSelectedByJSON(jsonObject);
                    }
                }
            }
        });
        ArkBaseDialog.Builder builder = new ArkBaseDialog.Builder(mContext);
        builder.setTitle("请选择");
        builder.setContentView(searchView);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
    }

    private void addSelectedItem(JSONObject jsonObject) {
        selectedArray.put(jsonObject);

        adapterList.notifyDataSetChanged();
    }

    private void removeSelectedByJSON(JSONObject jsonObject) {
        JSONArray tempArray = new JSONArray();
        for (int i = 0; i < selectedArray.length(); i++) {
            if (selectedArray.optJSONObject(i).optString(itemKey).equals(jsonObject.optString(itemKey))) continue;
            tempArray.put(selectedArray.optJSONObject(i));
        }
        selectedArray = tempArray;

        adapterList.notifyDataSetChanged();
    }

    private void setAdapterList() {
        adapterList = new ArkAdapter();
        adapterList.setAdapterHandler(new ArkAdapter.AdapterHandler() {
            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                final ListViewHolder holder;
                if (null == view) {
                    holder = new ListViewHolder();
                    if (isMultipleChoice) {
                        view = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_multiple_choice, viewGroup, false);
                    } else {
                        view = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_single_choice, viewGroup, false);
                    }
                    holder.text = (CheckedTextView) view;
                    view.setTag(holder);
                } else {
                    holder = (ListViewHolder) view.getTag();
                }

                holder.text.setTextColor(mContext.getResources().getColor(R.color.text_gray));
                if (isMultipleChoice) {
                    holder.text.setCheckMarkDrawable(getResources().getDrawable(R.drawable.checkbox_selector));
                } else {
                    holder.text.setCheckMarkDrawable(getResources().getDrawable(R.drawable.radiobutton_selector));
                }
                JSONObject data = items.optJSONObject(i);
                String text = null;
                text = data.optString(itemText, "");
                holder.text.setText(text);
                list.setItemChecked(i + 1, false);
                for (int j = 0; j < selectedArray.length(); j++) {
                    if (selectedArray.optJSONObject(j).optString(itemKey).equals(data.optString(itemKey))) {
                        holder.text.setTextColor(mContext.getResources().getColor(R.color.red));
                        list.setItemChecked(i + 1, true);
                        break;
                    }
                }
                return view;
            }

            @Override
            public int getCount() {
                if (items == null) {
                    return 0;
                }
                return items.length();
            }
        });
    }

    private void setSelectedText() {
        String textStr = "";
        for (int i = 0; i < selectedArray.length(); i++) {
            textStr = TextUtils.isEmpty(textStr) ? selectedArray.optJSONObject(i).optString(itemText) : textStr + "," +
                    selectedArray.optJSONObject(i).optString(itemText);
        }
        setValue(textStr);
    }

    public void loadList() {
      /*  presenter.query(constName, new ArkHttpClient.HttpHandler() {
            @Override
            public void onSuccess(JSONObject response, boolean isInCache) {
                super.onSuccess(response, isInCache);
                serverItems = response.optJSONArray("data");
                if (null == serverItems) {
                    Log.e(Logging.LOG_TAG, response.toString());
                } else {
                    items = serverItems;
                    adapterList.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String statusCode, String error) {
                super.onFailure(statusCode, error);
            }
        });*/

    }

    @Override
    public void onClick(View view) {
        showDialog();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        setSelectedText();
        dialog.dismiss();
    }

    @Override
    public void setValue(Object object) {
        if (object instanceof String) {
            ((TextView) valueView).setText((String) object);
        }
    }

    @Override
    public void setValueId(Object object) {
        if (object instanceof String) {
            valueIds = (String) object;
        }
    }

    @Override
    public Object getValue() {
        return valueIds;
    }

    @Override
    public void setHint(String hint) {
        ((TextView) valueView).setHint(hint);
    }

    public class ListViewHolder {
        CheckedTextView text;
    }
}
