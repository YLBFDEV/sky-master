package com.skytech.moa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.skytech.android.ArkActivity;
import com.skytech.moa.R;
import com.skytech.moa.model.Document;
import com.skytech.moa.services.LoadDocsService;
import com.skytech.moa.view.IDocsView;
import com.skytech.moa.widgets.doclibrary.DocGridViewAdapter;
import com.skytech.moa.widgets.doclibrary.DocListViewAdapter;
import org.json.JSONObject;

import java.util.ArrayList;

public class DocLibrary extends ArkActivity implements IDocsView {
    private ListView docList;
    private GridView docGrid;
    private ArrayList<Document> docListLibraries = new ArrayList<>();
    private DocListViewAdapter docListViewAdapter;
    private DocGridViewAdapter docGridViewAdapter;
    private RelativeLayout ListViewRelative;
    private RelativeLayout GridViewRelative;
    private Boolean isListView;
    private ImageView back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.docs_library);
        initData();
        initView();
        action();
    }

    public void initView() {
        docList = (ListView) findViewById(R.id.docLibraryList);
        docGrid = (GridView) findViewById(R.id.docLibraryGrid);
        ListViewRelative = (RelativeLayout) findViewById(R.id.ListViewRelative);
        GridViewRelative = (RelativeLayout) findViewById(R.id.GridViewRelative);
        back = (ImageView) findViewById(R.id.doc_back);
        docList.setOnItemClickListener(docListItemClickListener);
        docListViewAdapter = new DocListViewAdapter(this);
        docGridViewAdapter = new DocGridViewAdapter(this);
    }


    public void gridListChange(View view) {
        if (isListView) {
            ListViewRelative.setVisibility(View.GONE);
            GridViewRelative.setVisibility(View.VISIBLE);
            findViewById(R.id.module_list_change).setBackgroundResource(R.drawable.ic_list);
            isListView = false;
        } else {
            ListViewRelative.setVisibility(View.VISIBLE);
            GridViewRelative.setVisibility(View.GONE);
            findViewById(R.id.module_list_change).setBackgroundResource(R.drawable.ic_module);
            isListView = true;
        }
    }

    public void initData() {
        isListView = true;
        LoadDocsService loadDocsService = new LoadDocsService(this);
        try {
            JSONObject param = new JSONObject();
            param.put("uid", 123);
            param.put("pageNumber", 1);
            param.put("pageSize", 2);
            loadDocsService.loadAllDocs(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AdapterView.OnItemClickListener docListItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            if (position != (docListLibraries.size() - 1)) {
                Intent intent = new Intent(DocLibrary.this, DocDetail.class);
                startActivity(intent);
            }
        }
    };

    public void action() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void LoadAllDocuments(ArrayList<Document> documents) {
        this.docListLibraries = documents;
        documents.add(new Document(-1,"",0,"",-1));
        docListViewAdapter.setDocLibraries(documents);
        docListViewAdapter.notifyDataSetChanged();
        docList.setAdapter(docListViewAdapter);
        docGridViewAdapter.setDocLibraries(documents);
        docGridViewAdapter.notifyDataSetChanged();
        docGrid.setAdapter(docGridViewAdapter);
    }

    @Override
    public void failure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
