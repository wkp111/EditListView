# EditListView [ ![Download](https://api.bintray.com/packages/wkp/maven/EditListView/images/download.svg) ](https://bintray.com/wkp/maven/EditListView/_latestVersion)
可编辑选择、删除条目的ListView
## 演示图
![DragGridView](https://github.com/wkp111/EditListView/blob/master/EditListView.gif "演示图")
## Gradle集成
```groovy
dependencies{
      compile 'com.wkp:EditListView:1.0.1'
      //Android Studio3.0+可用以下方式
      //implementation 'com.wkp:EditListView:1.0.1'
}
```
Note：可能存在Jcenter还在审核阶段，这时会集成失败！
## 使用详解
> 属性讲解
```xml
        <!--是否开启测量高度-->
        <attr name="wkp_measureHeight" format="boolean"/>
        <!--未选中状态时图标-->
        <attr name="wkp_uncheckedImg" format="reference"/>
        <!--选中状态时图标-->
        <attr name="wkp_checkedImg" format="reference"/>
```
Note：每个属性都有对应的java设置代码！
> 布局
```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:app="http://schemas.android.com/apk/res-auto"
            xmlns:tools="http://schemas.android.com/tools"
            android:id="@+id/sv"
            android:layout_width="match_parent"
            android:layout_height="wrap_content">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal">

            <TextView
                android:id="@+id/delete"
                android:onClick="delete"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:gravity="center"
                android:padding="@dimen/len_5dp"
                android:text="删除"/>

            <TextView
                android:id="@+id/edit"
                android:onClick="edit"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:gravity="center"
                android:padding="@dimen/len_5dp"
                android:text="编辑"/>

            <TextView
                android:id="@+id/exit_edit"
                android:onClick="exitEdit"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:gravity="center"
                android:padding="@dimen/len_5dp"
                android:text="退出编辑"/>

            <TextView
                android:id="@+id/select_all"
                android:onClick="selectAll"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:gravity="center"
                android:padding="@dimen/len_5dp"
                android:text="全选"/>

            <TextView
                android:id="@+id/select_none"
                android:onClick="selectNone"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:gravity="center"
                android:padding="@dimen/len_5dp"
                android:text="全不选"/>

        </LinearLayout>

        <com.wkp.editlistview_library.view.EditListView
            android:id="@+id/lv"
            app:wkp_checkedImg="@drawable/ic_checked"
            app:wkp_uncheckedImg="@drawable/ic_uncheck"
            app:wkp_measureHeight="true"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"></com.wkp.editlistview_library.view.EditListView>

    </LinearLayout>
</ScrollView>
```
Note：ScrollView嵌套
> 代码示例
```java
/**
 * kotlin用法
 */
class MainActivity : AppCompatActivity() {

    private val data = arrayListOf<String>("托儿索", "儿童劫", "小学僧", "橡皮妮", "喜之螂", "提款姬", "鱼尾雯", "鸡毛信", "娃娃鱼", "过家嘉", "尿不狮", "沙琪马", "阿童木", "大嘴猴", "香港皎")
    private var mListView: EditListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mListView = findViewById<EditListView>(R.id.lv)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, data)
        //设置适配器
        mListView!!.adapter = adapter
        //设置是否测量高度（解决ScrollView冲突）
//        mListView!!.setMeasureHeight(true)
        //长按监听
        mListView!!.setOnItemLongClickListener({ parent, view, position, id ->
            //开启编辑状态
            mListView!!.isEditState = true
            //设置长按条目选中状态
            mListView!!.setItemChecked(position, true)
            //返回false会导致OnItemClickListener调用，使以上的选中状态消失
            true
        })
        //设置所有条目选中/未选中监听（每次条目状态改变都会回调）
        mListView!!.setOnAllItemCheckedListener { checked -> Log.d("MainActivity", "checked:" + checked) }
    }

    //删除按钮
    fun delete(view: View) {
        //删除所有已选中条目（adapter的源数据为数组时不支持转换）
        mListView!!.deleteAllCheckedItem(data)
    }

//  编辑按钮
    fun edit(view: View) {
//      开启编辑状态
        mListView!!.isEditState = true
    }

//  退出编辑按钮
    fun exitEdit(view: View) {
//       关闭编辑状态
        mListView!!.isEditState = false
    }

//  全选按钮
    fun selectAll(view: View) {
//       全选
        mListView!!.setAllItemChecked()
        //是否全选
        Log.d("MainActivity","isAllItemChecked:" + mListView!!.isAllItemChecked)
    }

//  全不选按钮
    fun selectNone(view: View) {
//      全不选
        mListView!!.setAllItemUnchecked()
        //是否全不选
        Log.d("MainActivity","isAllItemUnchecked:" + mListView!!.isAllItemUnchecked)
    }
}



/**
 * Created by user on 2017/11/6.
 * java用法
 */

public class TestActivity extends AppCompatActivity {

    private String[] mStrings = {"托儿索", "儿童劫", "小学僧", "橡皮妮", "喜之螂", "提款姬", "鱼尾雯", "鸡毛信", "娃娃鱼", "过家嘉", "尿不狮",
            "沙琪马", "阿童木", "大嘴猴", "香港皎"};
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
        //设置适配器
        mListView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,data));
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
```
Note：还有其他API请根据需要自行参考！ 
## 寄语
控件支持直接代码创建，还有更多API请观看<a href="https://github.com/wkp111/EditListView/blob/master/editlistview-library/src/main/java/com/wkp/editlistview_library/view/EditListView.java">EditListView.java</a>内的注释说明。<br/>
欢迎大家使用，感觉好用请给个Star鼓励一下，谢谢！<br/>
大家如果有更好的意见或建议以及好的灵感，请邮箱作者，谢谢！<br/>
QQ邮箱：1535514884@qq.com<br/>
163邮箱：15889686524@163.com<br/>
Gmail邮箱：wkp15889686524@gmail.com<br/>

## 版本更新
* v1.0.1
新创建可编辑列表控件库
## License

   Copyright 2017 wkp

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
