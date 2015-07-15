package com.skytech.moa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.skytech.android.ArkActivity;
import com.skytech.android.util.CustomToast;
import com.skytech.moa.R;
import com.skytech.moa.model.Module;
import com.skytech.moa.model.ModuleUtility;
import com.skytech.moa.presenter.ModulePresenter;
import com.skytech.moa.utils.Constant;
import com.skytech.moa.view.home.ModuleViewPage;
import com.skytech.moa.view.home.ModulesViewFragment;

public class Home extends ArkActivity implements View.OnClickListener,ModulesViewFragment.Callback {
    public final static String ACTION_REMINDS_UPDATED = "com.skytech.moa.reminds";

    private ModuleViewPage moduleViewPage;
    private ModulePresenter modulePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        moduleViewPage = new ModuleViewPage(this);
        modulePresenter = new ModulePresenter(moduleViewPage);
        moduleViewPage.init(getSupportFragmentManager(), this);
        initView();
    }

    private SlidingMenu slidingMenu;
    private void initView() {
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        slidingMenu.setSecondaryMenu(R.layout.setting_board);
        slidingMenu.setBehindOffsetRes(R.dimen.sliding_menu_padding);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        initSlidingMenuListeners();
    }

    public void toSetting(View view) {
        slidingMenu.showSecondaryMenu();
    }

    protected void onPersonalSpaceClicked() {
        Toast.makeText(this, "onPersonalSpaceClicked", Toast.LENGTH_LONG).show();
    }

    protected void onFavoritesClicked() {
        Toast.makeText(this, "onFavoritesClicked", Toast.LENGTH_LONG).show();
    }

    protected void onScanClicked() {
        Toast.makeText(this, "onScanClicked", Toast.LENGTH_LONG).show();
    }

    protected void onClearCacheClicked() {
        Toast.makeText(this, "onClearCacheClicked", Toast.LENGTH_LONG).show();
    }

    protected void onNotificationsClicked() {
        Toast.makeText(this, "onNotificationsClicked", Toast.LENGTH_LONG).show();
    }

    protected void onAppThemeCenterClicked() {
        Toast.makeText(this, "onAppThemeCenterClicked", Toast.LENGTH_LONG).show();
    }

    protected void onAboutClicked() {
        Toast.makeText(this, "onAboutClicked", Toast.LENGTH_LONG).show();
    }

    protected void onExitClicked() {
        Toast.makeText(this, "onExitClicked", Toast.LENGTH_LONG).show();
    }

    public void toIM(View view) {

    }

    public void showAffairMenu(View view) {
        findViewById(R.id.affair_menu_board_root).setVisibility(View.VISIBLE);
        findViewById(R.id.module_page_container).setEnabled(false);
        findViewById(R.id.module_page_container).setClickable(false);
    }

    public void hideAffairMenu(View view) {
        findViewById(R.id.affair_menu_board_root).setVisibility(View.GONE);
    }

    public void toSign(View view) {
        startActivity(Attendance.createIntent(this));
    }

    public void onApplyAffair(View view) {
        Toast.makeText(this, "onApplyAffair", Toast.LENGTH_LONG).show();
    }

    public void onAddTask(View view) {
        Toast.makeText(this, "onAddTask", Toast.LENGTH_LONG).show();
    }

    public void onNewExpenseAccount(View view) {
        Toast.makeText(this, "onNewExpenseAccount", Toast.LENGTH_LONG).show();
    }

    public void onAddWorkingLog(View view) {
        Toast.makeText(this, "onAddWorkingLog", Toast.LENGTH_LONG).show();
    }

    public void onApplyMeetingRoom(View view) {
        Toast.makeText(this, "onApplyMeetingRoom", Toast.LENGTH_LONG).show();
    }

    public void onNewMemo(View view) {
        Toast.makeText(this, "onNewMemo", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onFragmentPrepared() {
        modulePresenter.load(ModulePresenter.ALL_MODULES);
    }

    @Override
    public void onModuleClicked(Module module) {
        Class<?> moduleActivity = ModuleUtility.getInstance().getModuleActivity(module.getKey());
        Intent intent;
        if (null != moduleActivity) {
            intent = new Intent(this, moduleActivity);
            intent.putExtra(Constant.EXTRA_MODULE_CODE, module.getKey());
            intent.putExtra(Constant.EXTRA_MODULE_NAME, module.getName());
        } else {
            CustomToast.show(this, getString(R.string.under_construction_hint));
            return;
        }
        startActivity(intent);
    }

    private void initSlidingMenuListeners() {
        slidingMenu.findViewById(R.id.personal_space_entry).setOnClickListener(this);
        slidingMenu.findViewById(R.id.favorites_entry).setOnClickListener(this);
        slidingMenu.findViewById(R.id.scan_entry).setOnClickListener(this);
        slidingMenu.findViewById(R.id.clear_cache_entry).setOnClickListener(this);
        slidingMenu.findViewById(R.id.notice_entry).setOnClickListener(this);
        slidingMenu.findViewById(R.id.notice_entry).setOnClickListener(this);
        slidingMenu.findViewById(R.id.theme_center_entry).setOnClickListener(this);
        slidingMenu.findViewById(R.id.about_entry).setOnClickListener(this);
        slidingMenu.findViewById(R.id.exit_entry).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.personal_space_entry:
                onPersonalSpaceClicked();
                break;
            case R.id.favorites_entry:
                onFavoritesClicked();
                break;
            case R.id.scan_entry:
                onScanClicked();
                break;
            case R.id.clear_cache_entry:
                onClearCacheClicked();
                break;
            case R.id.notice_entry:
                onNotificationsClicked();
                break;
            case R.id.theme_center_entry:
                onAppThemeCenterClicked();
                break;
            case R.id.about_entry:
                onAboutClicked();
                break;
            case R.id.exit_entry:
                onExitClicked();
                break;
            default:
                Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
        }
    }
}
