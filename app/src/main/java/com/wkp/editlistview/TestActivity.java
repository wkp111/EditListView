package com.wkp.editlistview;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.wkp.editlistview_library.view.EditListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by user on 2017/11/6.
 * java用法
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class TestActivity extends AppCompatActivity {

    private String[] mStrings = {"托儿索", "儿童劫", "小学僧", "橡皮妮", "喜之螂", "提款姬", "鱼尾雯", "鸡毛信", "娃娃鱼", "过家嘉", "尿不狮",
            "沙琪马", "阿童木", "大嘴猴", "香港皎","脑残片","卖卖卖","333","干干干"};
    private List<String> data = new ArrayList<>();
    private EditListView mListView;
    {
        data.addAll(Arrays.asList(mStrings));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = findViewById(R.id.lv);
        //设置编辑/退出编辑动画时长
        mListView.setAnimDuration(400);
        //设置适配器
        mListView.setAdapter(new ArrayAdapter<String>(this,R.layout.item_lv,R.id.item_tv,data));
        //条目长按监听
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //开启编辑状态
                mListView.setEditState(true);
                //设置长按条目选中状态
                mListView.setItemChecked(position,true);
                //返回false会导致OnItemClickListener调用，使以上的选中状态消失
                return true;
            }
        });
        //设置所有条目选中/未选中监听（每次条目状态改变都会回调）
        mListView.setOnAllItemCheckedListener(new EditListView.OnAllItemCheckedListener() {
            @Override
            public void onAllItemChecked(boolean checked) {
                Log.d("TestActivity", "checked:" + checked);
            }
        });
    }
    //删除按钮
    public void delete(View view) {
        //删除所有已选中条目（adapter的源数据为数组时不支持转换）
        mListView.deleteAllCheckedItem(data);
    }
    //编辑按钮
    public void edit(View view) {
        //开启编辑状态
        mListView.setEditState(true);
    }
    //退出编辑按钮
    public void exitEdit(View view) {
        //关闭编辑状态
        mListView.setEditState(false);
    }
    //全选按钮
    public void selectAll(View view) {
        //全选
        mListView.setAllItemChecked();
        //是否全选
        Log.d("MainActivity", "isAllItemChecked:" + mListView.isAllItemChecked());
    }
    //全不选按钮
    public void selectNone(View view) {
        //全不选
        mListView.setAllItemUnchecked();
        //是否全不选
        Log.d("MainActivity", "isAllItemUnchecked:" + mListView.isAllItemUnchecked());
    }
}
