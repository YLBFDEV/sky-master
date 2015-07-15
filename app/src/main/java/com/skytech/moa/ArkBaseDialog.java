package com.skytech.moa;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.LinearLayout.LayoutParams;
import com.skytech.android.util.TouchHelper;

/**
 * Create custom Dialog windows for your application
 * Custom dialogs rely on custom layouts wich allow you to
 * create and use your own look & feel.
 * <p/>
 * Under GPL v3 : http://www.gnu.org/licenses/gpl-3.0.html
 *
 * @author zhangyk
 */

public class ArkBaseDialog extends Dialog {
    private boolean isLANDSCAPE = true;

    public ArkBaseDialog(Context context, int theme) {
        super(context, theme);
    }

    public ArkBaseDialog(Context context) {
        super(context);
    }

    /**
     * Helper class for creating a custom dialog
     */
    public static class Builder {

        private Context context;
        private String title;
        private int title_icon;
        private int title_background;
        private ListAdapter adapter = null;
        private boolean listAutoHeight = true;
        private String message;
        private String positiveButtonText;
        private int closeButtonRes;
        private int positiveButtonRes;
        private int negativeButtonRes;
        private int showItemCount = 10;
        private String negativeButtonText;
        private View contentView;
        private ListView listView;
        private OnClickListener
                closeButtonClickListener,
                positiveButtonClickListener,
                negativeButtonClickListener;
        private View.OnClickListener icoClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * Set the Dialog message from String
         *
         * @param message
         * @return
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @param message
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title icon from resource
         *
         * @param resId
         * @return
         */
        public Builder setTitleIcon(int resId) {
            this.title_icon = resId;
            return this;
        }

        public Builder setTitleIcon(int resId, View.OnClickListener listener) {
            this.title_icon = resId;
            this.icoClickListener = listener;
            return this;
        }

        /**
         * Set the Dialog title background from resource
         *
         * @param title_bg
         * @return
         */
        public Builder setTitleBackground(int title_bg) {
            this.title_background = title_bg;
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }


        /**
         * Set the Dialog List Adapter
         *
         * @param adapter
         * @return
         */
        public Builder setAdapter(ListAdapter adapter) {
            this.adapter = adapter;
            return this;
        }

        public Builder setListView(ListView listView) {
            this.listView = listView;
            return this;
        }

        public Builder setShowItemCount(int count) {
            this.showItemCount = count;
            return this;
        }

        /**
         * Set the Dialog List Width
         *
         * @param listAutoHeight
         * @return
         */
        public Builder setListAutoHeight(boolean listAutoHeight) {
            this.listAutoHeight = listAutoHeight;
            return this;
        }

        /**
         * Set a custom content view for the Dialog.
         * If a message is set, the contentView is not
         * added to the Dialog...
         *
         * @param v
         * @return
         */
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * Set the close button resource and it's listener
         *
         * @param closeButtonRes
         * @param listener
         * @return
         */
        public Builder setCloseButton(int closeButtonRes,
                                      OnClickListener listener) {
            this.closeButtonRes = closeButtonRes;
            this.closeButtonClickListener = listener;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(int positiveButtonRes, int positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonRes = positiveButtonRes;
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        /**
         * Set the positive button text and it's listener
         *
         * @param positiveButtonText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(int positiveButtonRes, String positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonRes = positiveButtonRes;
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        /**
         * Set the negative button resource and it's listener
         *
         * @param negativeButtonText
         * @param listener
         * @return
         */
        public Builder setNegativeButton(int negativeButtonRes, int negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonRes = negativeButtonRes;
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        /**
         * Set the negative button text and it's listener
         *
         * @param negativeButtonText
         * @param listener
         * @return
         */
        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        /**
         * 设置ListView的高度（动态的）  TODO delete
         */
        private void setListViewHeightBasedOnChildren(ListView listView) {

            ListAdapter listAdapter = listView.getAdapter();

            if (listAdapter == null) {
                return;
            }

            int totalHeight = 0;
            if (listAdapter.getCount() > showItemCount) {
                for (int i = 0; i < showItemCount; i++) {
                    View listItem = listAdapter.getView(i, null, listView);
                    listItem.measure(0, 0);
                    totalHeight += listItem.getMeasuredHeight();
                }
            } else {
                for (int i = 0; i < listAdapter.getCount(); i++) {
                    View listItem = listAdapter.getView(i, null, listView);
                    listItem.measure(0, 0);
                    totalHeight += listItem.getMeasuredHeight();
                }
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            int count = 7;
            if (listAdapter.getCount() < 8) {
                count = listAdapter.getCount();
            }
            if (totalHeight > 600) {
                totalHeight = 600;
            }
            params.height = totalHeight
                    + (listView.getDividerHeight() * (count - 1));
            //((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10); // 可删除

            listView.setLayoutParams(params);
        }

        /**
         * Create the custom dialog
         */
        @SuppressLint("Override")
        public ArkBaseDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final ArkBaseDialog dialog = new ArkBaseDialog(context, R.style.alertdialog);

            View layout = inflater.inflate(R.layout.alert_dialog, null);
            dialog.addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            // set the dialog title
            TextView titleView = ((TextView) layout.findViewById(R.id.dialog_title_show));
            titleView.setText(title);

            // set the dialog title icon
            if (0 != title_background) {
                layout.findViewById(R.id.dialog_title_layout).setBackgroundResource(title_background);
            }

            if (0 != title_icon) {
                titleView.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(title_icon), null, null, null);
                /*ImageView ico = (ImageView) layout.findViewById(R.id.dialog_title_icon);
                ico.setImageResource(title_icon);*/
                if (null != icoClickListener) {
                    titleView.setClickable(true);
                    titleView.setOnClickListener(icoClickListener);
                }
            }

            ListView dialogListView = (ListView) layout.findViewById(R.id.dialog_list);
            // set the dialog list adpter
            if (adapter != null) {
                dialogListView.setAdapter(adapter);
                // set the dialog list width
                if (listAutoHeight) {
                    //setListViewHeightBasedOnChildren(dialogListView);
                }
            } else {
                dialogListView.setVisibility(View.GONE);
            }

            //set the close button
            if (closeButtonClickListener != null) {
                ImageButton imageClose = ((ImageButton) layout.findViewById(R.id.dialog_close));
                imageClose.setBackgroundResource(closeButtonRes);
                TouchHelper.setOnTouchListener(imageClose, 0.85f, 0.85f);
                imageClose.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        closeButtonClickListener.onClick(
                                dialog,
                                DialogInterface.BUTTON_NEGATIVE);
                    }
                });
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.dialog_close).setVisibility(View.GONE);
            }

            // set the confirm button
            if (positiveButtonText != null) {
                // int positiveTextLenght = positiveButtonText.length();
                ((Button) layout.findViewById(R.id.dialog_confirm_btn))
                        .setText(positiveButtonText);
              /*  ((Button) layout.findViewById(R.id.dialog_confirm_btn))
                        .setWidth(104 + (positiveTextLenght - 2) * 15);*/
                layout.findViewById(R.id.dialog_confirm_btn).setBackgroundResource(positiveButtonRes);
                if (positiveButtonClickListener != null) {
                    (layout.findViewById(R.id.dialog_confirm_btn))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(
                                            dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.dialog_confirm_btn).setVisibility(
                        View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
                //   int negativeTextLenght = negativeButtonText.length();
                Button btnCancel = (Button) layout.findViewById(R.id.dialog_cancel_btn);
                //   btnCancel.setWidth(104 + (negativeTextLenght - 2) * 15);
                btnCancel.setText(negativeButtonText);
                btnCancel.setBackgroundResource(negativeButtonRes);
                if (negativeButtonClickListener != null) {
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            negativeButtonClickListener.onClick(
                                    dialog,
                                    DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.dialog_cancel_btn).setVisibility(View.GONE);
            }
            // set the content message
            if (message != null) {
                ((TextView) layout.findViewById(
                        R.id.dialog_content_show)).setText(message);
            } else if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                ((LinearLayout) layout.findViewById(R.id.dialog_content_layout))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.dialog_content_layout))
                        .addView(contentView,
                                new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            } else if (null != listView) {
                ((LinearLayout) layout.findViewById(R.id.dialog_content_layout))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.dialog_content_layout))
                        .addView(listView,
                                new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                //setListViewHeightBasedOnChildren(listView);
            } else {
                (layout.findViewById(R.id.dialog_content_show)).setVisibility(View.GONE);
            }
            dialog.setContentView(layout);
            dialog.setCanceledOnTouchOutside(true);
            return dialog;
        }

    }

}
