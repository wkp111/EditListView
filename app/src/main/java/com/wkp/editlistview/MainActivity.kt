package com.wkp.editlistview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import com.wkp.editlistview_library.view.EditListView

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
