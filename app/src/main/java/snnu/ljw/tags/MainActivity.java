package snnu.ljw.tags;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import snnu.ljw.tags.dao.ApiUse;
import snnu.ljw.tags.dao.TagDAO;

public class MainActivity extends AppCompatActivity implements SearchView.SearchViewListener {
    private TagDAO tagDAO = new TagDAO(this);

    private TagContainerLayout mTagContainerLayout1;
    private SearchView searchView;
    private ListView listViewRes;

    private ArrayAdapter<String> hintAdapter;

    private ArrayAdapter<String> autoCompleteAdapter;

    private SearchAdapter resAdapter;

    private List<Tag> DataFromRemote = new ArrayList<Tag>();

    private List<String> hintData;

    private List<String> autoCompleteData;

    private List<Tag> resultData;

    private static int DEFAULT_HINT_SIZE = 4;

    private static int hintSize = DEFAULT_HINT_SIZE;

    public static void setHintSize(int hintSize){
        MainActivity.hintSize = hintSize;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDatas();
        initSearchView();


        mTagContainerLayout1 = (TagContainerLayout) findViewById(R.id.tagcontainerLayout1);


        // Set custom click listener
        mTagContainerLayout1.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                System.out.println("ljw"+DataFromRemote.get(position).getdescrib());
                TagDialog dialog = new TagDialog(MainActivity.this);
                dialog.setMessage(DataFromRemote.get(position).getdescrib())
                        .setTitle(DataFromRemote.get(position).getName())
                        .setSingle(true)
                        .setOnClickBottomListener(new TagDialog.OnClickBottomListener() {
                            @Override
                            public void onPositiveClick() {
                                dialog.dismiss();
                            }

                            @Override
                            public void onNegtiveClick() {
                                dialog.dismiss();
                            }
                        }).show();
            }

            @Override
            public void onTagLongClick(final int position, String text) {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("温馨提示")
                        .setMessage("您确定要删除tag标签吗？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (position < mTagContainerLayout1.getChildCount()) {
                                    mTagContainerLayout1.removeTag(position);
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();
            }

        });

//        mTagContainerLayout1.setTags(list1);

        List<int[]> colors = new ArrayList<int[]>();
        //int[]color = {backgroundColor, tagBorderColor, tagTextColor, tagSelectedBackgroundColor}
        int[] col1 = {Color.parseColor("#ff0000"), Color.parseColor("#000000"), Color.parseColor("#ffffff"), Color.parseColor("#999999")};
        int[] col2 = {Color.parseColor("#0000ff"), Color.parseColor("#000000"), Color.parseColor("#ffffff"), Color.parseColor("#999999")};

        colors.add(col1);
        colors.add(col2);
        Button btnAddTag = (Button) findViewById(R.id.btn_add_tag);
        btnAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,editTag.class);
                startActivity(intent);
                // Add tag in the specified position
//                mTagContainerLayout1.addTag(text.getText().toString(), 4);
            }
        });

    }



    private void initSearchView(){
        listViewRes = findViewById(R.id.main_lv_search);

        searchView = findViewById(R.id.search1);
        searchView.setSearchViewListener(this);
        searchView.setTipsHintAdapter(hintAdapter);
        searchView.setAutoCompleteAdapter(autoCompleteAdapter);

        //结果列表选项事件监听
        listViewRes.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TagDialog dialog = new TagDialog(MainActivity.this);
                        dialog.setMessage(resultData.get(position).getdescrib())
                                .setTitle(resultData.get(position).getName())
                                .setSingle(true)
                                .setOnClickBottomListener(new TagDialog.OnClickBottomListener() {
                                    @Override
                                    public void onPositiveClick() {
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onNegtiveClick() {
                                        dialog.dismiss();
                                    }
                                }).show();
                    }
                }
        );
    }

    private void initDatas(){
        getDatas();
        System.out.println("exet");
        getAutoCompleateData(null);
        //初始化搜索结果数据
        getResultData(null);
    }

    private void getDatas() {
        tagDAO.getAllTags(
                new ApiUse<List<Tag>>() {
                    @Override
                    public void onSuccess(List<Tag> result) {
                        for (Tag tag:result){
                            DataFromRemote.add(new Tag(tag.getName(),tag.getdescrib()));
                        }
                        List<String> list2 = new ArrayList<String>();
                        for(Tag tag : DataFromRemote){
                            list2.add(tag.getName());
                        }
                        mTagContainerLayout1.setTags(list2);
                    }

                    @Override
                    public void onFail(Exception e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    private void getAutoCompleateData(String text){
        System.out.println("zhixinlin");
        if (autoCompleteData ==null){
            autoCompleteData = new ArrayList<>(hintSize);
        }
        else {
            autoCompleteData.clear();
            for (int i=0,count =0 ;i<DataFromRemote.size()&& count<hintSize;i++){
                System.out.println("zhixin2");
                if (DataFromRemote.get(i).getName().contains(text.trim())){
                    autoCompleteData.add(DataFromRemote.get(i).getName());
                    count++;
                }
            }
        }
        if (autoCompleteAdapter == null){
            autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,autoCompleteData);
        }
        else {
            autoCompleteAdapter.notifyDataSetChanged();
        }
    }

    private void getResultData(String text){
        if (resultData == null){
            resultData = new ArrayList<>();
        }
        else {
            resultData.clear();
            for (int i=0;i<DataFromRemote.size();i++){
                if (DataFromRemote.get(i).getName().contains(text.trim())){
                    resultData.add(DataFromRemote.get(i));
                }
            }
        }
        if (resAdapter == null){
            resAdapter = new SearchAdapter(this,resultData,R.layout.item_bean_list);
        }
        else {
            resAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefreshAutoComplete(String text) {
        System.out.println("zhixinxin");
        getAutoCompleateData(text);

    }

    @Override
    public void onSearch(String text) {
        getResultData(text);
        listViewRes.setVisibility(View.VISIBLE);
        if (listViewRes.getAdapter() == null){
            listViewRes.setAdapter(resAdapter);
        }
        else {
            resAdapter.notifyDataSetChanged();
        }
        if (resultData.size() == 0){
            Toast.makeText(this,"不好意思，没有您需要的tag",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this,"完成搜索",Toast.LENGTH_LONG).show();

        }
    }
}