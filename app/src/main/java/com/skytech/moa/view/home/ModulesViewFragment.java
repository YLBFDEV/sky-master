package com.skytech.moa.view.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.skytech.android.adapter.ArkAdapter;
import com.skytech.moa.R;
import com.skytech.moa.model.Module;
import com.skytech.moa.model.ModuleUtility;
import com.skytech.moa.widgets.ModuleItemView;

import java.util.ArrayList;
import java.util.List;

public class ModulesViewFragment extends Fragment {
    private final static String KEY_POSITION = "position";
    private final static int INVALID_POSITION = -1;
    private GridView moduleGridView;
    private ArkAdapter moduleViewAdapter = new ArkAdapter();
    private List<Module> modules = new ArrayList<Module>();
    private Callback callback;

    public interface Callback {
        public void onFragmentPrepared();
        public void onModuleClicked(Module module);

    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public int getPageNumber() {
        return getArguments().getInt(KEY_POSITION, INVALID_POSITION);
    }

    public static ModulesViewFragment newInstance(int position, Callback callback) {
        ModulesViewFragment  fragment = new ModulesViewFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        fragment.setArguments(args);
        fragment.setCallback(callback);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.module_base, container, false);
        moduleGridView = (GridView) result.findViewById(R.id.modules);
        initView();
        return result;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (null != callback) {
            callback.onFragmentPrepared();
        }
    }

    private void initView() {
        moduleViewAdapter.setAdapterHandler(
                new ArkAdapter.AdapterHandler() {
                    @Override
                    public View getView(int position, View view, ViewGroup parent) {
                        view = new ModuleItemView(ModulesViewFragment.this.getActivity());
                        Module module = modules.get(position);
                        ((ModuleItemView) view).setName(module.getName());
                        ((ModuleItemView) view).setIcon(ModuleUtility.getInstance().getModuleImage(module.getKey()));
                        ((ModuleItemView) view).setTodoNumber(module.getNum());
                        return view;
                    }

                    @Override
                    public int getCount() {
                        return modules.size();
                    }

                });
        moduleGridView.setAdapter(moduleViewAdapter);
        moduleGridView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (null != callback) {
                            callback.onModuleClicked(modules.get(position));
                        }
                    }
                });
    }

    public void load(List<Module> moduleList) {
        modules.clear();
        modules = moduleList;
        moduleViewAdapter.notifyDataSetChanged();
    }

}
