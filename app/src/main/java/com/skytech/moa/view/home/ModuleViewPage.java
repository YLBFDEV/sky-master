package com.skytech.moa.view.home;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.skytech.moa.R;
import com.skytech.moa.model.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModuleViewPage implements IModuleLoaderView {
    private static final int PAGE_SIZE = 9;

    private Activity activity;
    private ViewPager viewPager;
    private LinearLayout dotsContainer;
    private List<ModulesViewFragment> modulesViews;
    private View[] dots;

    private ModulesViewPagerAdapter modulesViewPagerAdapter;


    public ModuleViewPage(Activity activity) {
        this.activity = activity;
        modulesViews = new ArrayList<ModulesViewFragment>();
    }

    public void init(FragmentManager fm, ModulesViewFragment.Callback callback) {
        initView();
        initPageContainer(fm, callback);
        initDots();
    }

    private void initView() {
        viewPager = (ViewPager) activity.findViewById(R.id.module_page_container);
        dotsContainer = (LinearLayout) activity.findViewById(R.id.page_indicator_container);
    }

    private void initPageContainer(FragmentManager fm, ModulesViewFragment.Callback callback) {
        modulesViewPagerAdapter = new ModulesViewPagerAdapter(fm);
        modulesViews.add(ModulesViewFragment.newInstance(1, callback));
        modulesViews.add(ModulesViewFragment.newInstance(2, callback));
        viewPager.setAdapter(modulesViewPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setCurrentDot(position);
            }
        });


    }


    private final class ModulesViewPagerAdapter extends FragmentPagerAdapter {

        public ModulesViewPagerAdapter(FragmentManager mgr) {
            super(mgr);
        }

        @Override
        public int getCount() {
            return modulesViews.size();
        }

        @Override
        public Fragment getItem(int position) {
            return modulesViews.get(position);
        }
    }

    private void initDots() {
        dots = new ImageView[modulesViews.size()];

        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < modulesViews.size(); i++) {
            dots[i] = new ImageView(activity);
            dots[i].setBackgroundResource(R.drawable.dot_selector);
            dotsContainer.addView(dots[i], layoutParams);
        }
    }

    private void setCurrentDot(int position) {
        for (int i = 0; i < modulesViews.size(); i++) {
            dots[i].setClickable(i == position);
        }
    }

    public void load(List<Module> modules) {
        int pageCount = modules.size() / PAGE_SIZE + 1;
        for (int i = 0; i < pageCount; i++) {
            List<Module> moduleList = new ArrayList<Module>();
            int firstIndex = i * PAGE_SIZE;
            int lastIndex = modules.size() >= (i + 1) * PAGE_SIZE ? (i + 1) * PAGE_SIZE : modules.size();
            for (int j = firstIndex; j < lastIndex; j++) {
                moduleList.add(modules.get(j));
            }

            ModulesViewFragment mvf = modulesViews.get(i);
            mvf.load(moduleList);
        }
    }


    public void setRemindModules(Map<String, Integer> remindsMap) {

    }


    public void loadModuleFailed() {
    }

}
