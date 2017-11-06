package com.wkp.editlistview_library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import com.wkp.editlistview_library.R;
import com.wkp.editlistview_library.util.ViewHolder;

import java.util.List;

/**
 * Created by user on 2017/11/6.
 */

public class EditListView extends ListView {
    private static final int DEFAULT_UNCHECKED_RES = R.drawable.ic_uncheck;     //默认未选中图标
    private static final int DEFAULT_CHECKED_RES = R.drawable.ic_checked;       //默认选中图标
    /**
     * 编辑状态是否开启，默认关闭
     */
    private boolean mEditState;
    /**
     * 是否开启测量控件高度（解决ScrollView嵌套），默认不开启
     */
    private boolean mIsMeasureHeight;
    /**
     * 未选中状态图标
     */
    @DrawableRes
    private int mUncheckedRes = DEFAULT_UNCHECKED_RES;
    /**
     * 选中状态图标
     */
    @DrawableRes
    private int mCheckedRes = DEFAULT_CHECKED_RES;

    /**
     * 内置适配器
     */
    private EditBaseAdapter mBaseAdapter;

    /**
     * 所有条目选中监听
     */
    private OnAllItemCheckedListener mListener;

    public EditListView(Context context) {
        this(context,null);
    }

    public EditListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public EditListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        initOther();
    }

    /**
     * 初始化属性参数
     * @param context
     * @param attrs
     */
    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EditListView);
        if (typedArray != null) {
            mIsMeasureHeight = typedArray.getBoolean(R.styleable.EditListView_wkp_measureHeight, false);
            mUncheckedRes = typedArray.getResourceId(R.styleable.EditListView_wkp_uncheckedImg, DEFAULT_UNCHECKED_RES);
            mCheckedRes = typedArray.getResourceId(R.styleable.EditListView_wkp_checkedImg, DEFAULT_CHECKED_RES);
            typedArray.recycle();
        }
    }

    /**
     * 初始化其他设置
     */
    private void initOther() {
        setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setItemChecked(position,isItemChecked(position));
            }
        });
    }

    /**
     * 设置控件高度
     */
    private void setListViewHeight() {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        if (mIsMeasureHeight) {
            ListAdapter adapter = getAdapter();
            if (adapter != null) {
                int totalHeight = 0;
                for (int i = 0; i < adapter.getCount(); i++) {
                    View view = adapter.getView(i, null, this);
                    view.measure(0, 0);
                    totalHeight += view.getMeasuredHeight();
                }
                params.height = totalHeight + getDividerHeight() * (adapter.getCount() - 1);
            }
        }
        setLayoutParams(params);
    }

    /**
     * 平滑到顶部（ScrollView）
     */
    private void smoothScrollToHeader(ViewParent view) {
        ViewParent parent = view.getParent();
        if (parent == null) {
            return;
        }
        if (parent instanceof ScrollView) {
            ((ScrollView) parent).smoothScrollTo(0,0);
        }else {
            smoothScrollToHeader(parent);
        }
    }

    /**
     * 设置条目选中状态
     * @param position
     * @param value
     */
    @Override
    public void setItemChecked(int position, boolean value) {
        boolean isChange = isItemChecked(position) != value;
        super.setItemChecked(position, value);
        if (mBaseAdapter != null) {
            mBaseAdapter.notifyDataSetChanged();
            if (mListener != null && isChange) {
                mListener.onAllItemChecked(mBaseAdapter.getCount() == getCheckedItemCount());
            }
        }
    }

    /**
     * 是否所有条目已选中
     * @return
     */
    public boolean isAllItemChecked() {
        if (mBaseAdapter == null) {
            return false;
        }
        return mBaseAdapter.getCount() == getCheckedItemCount();
    }

    /**
     * 是否所有条目未选中
     * @return
     */
    public boolean isAllItemUnchecked() {
        return getCheckedItemCount() == 0;
    }

    /**
     * 是否正在编辑
     * @return
     */
    public boolean isEditState() {
        return mEditState;
    }

    /**
     * 是否开启测量高度（解决ScrollView嵌套）
     * @param measureHeight 是否开启
     * @return 链式编程
     */
    public EditListView setMeasureHeight(boolean measureHeight) {
        mIsMeasureHeight = measureHeight;
        setListViewHeight();
        return this;
    }

    /**
     * 设置编辑状态
     * @param editState 是否开启编辑
     * @return 链式编程
     */
    public EditListView setEditState(boolean editState) {
        if (mEditState == editState) {
            return this;
        }
        mEditState = editState;
        setAllItemUnchecked();
        if (mBaseAdapter != null) {
            mBaseAdapter.notifyDataSetChanged();
        }
        return this;
    }

    /**
     * 设置所有条目为已选中
     * @return
     */
    public EditListView setAllItemChecked() {
        if (mBaseAdapter != null) {
            for (int i = 0; i < mBaseAdapter.getCount(); i++) {
                if (!isItemChecked(i)) {
                    setItemChecked(i,true);
                }
            }
        }
        return this;
    }

    /**
     * 设置所有条目为未选中
     * @return
     */
    public EditListView setAllItemUnchecked() {
        if (mBaseAdapter != null) {
            for (int i = 0; i < mBaseAdapter.getCount(); i++) {
                if (isItemChecked(i)) {
                    setItemChecked(i,false);
                }
            }
        }
        return this;
    }

    /**
     * 设置未选中状态图标
     * @param uncheckedRes 资源图片
     * @return 链式编程
     */
    public EditListView setUncheckedRes(int uncheckedRes) {
        mUncheckedRes = uncheckedRes;
        if (mBaseAdapter != null) {
            mBaseAdapter.notifyDataSetChanged();
        }
        return this;
    }

    /**
     * 设置已选中状态图标
     * @param checkedRes 资源图片
     * @return 链式编程
     */
    public EditListView setCheckedRes(int checkedRes) {
        mCheckedRes = checkedRes;
        if (mBaseAdapter != null) {
            mBaseAdapter.notifyDataSetChanged();
        }
        return this;
    }

    /**
     * 设置所有条目选中监听
     * @param listener
     * @return
     */
    public EditListView setOnAllItemCheckedListener(OnAllItemCheckedListener listener) {
        mListener = listener;
        return this;
    }

    /**
     * 删除所有已选中条目
     * @param adapterData 适配器原始数据(注意：不能由数组转换而来，转换的不具备删除功能)
     * @param <T>
     * @return
     */
    public <T> EditListView deleteAllCheckedItem(@NonNull List<T> adapterData) {
        for (int i = adapterData.size() - 1; i >= 0; i--) {
            if (isItemChecked(i)) {
                adapterData.remove(i);
                setItemChecked(i,false);
            }
        }
        if (mBaseAdapter != null) {
            mBaseAdapter.notifyDataSetChanged();
        }
        return this;
    }

    /**
     * 设置适配器
     * @param adapter
     */
    @Override
    public void setAdapter(ListAdapter adapter) {
        if (adapter == null) {
            throw new IllegalArgumentException("setAdapter()参数为null");
        }
        if (!(adapter instanceof BaseAdapter)) {
            throw new IllegalArgumentException("setAdapter()参数需要? extends BaseAdapter");
        }
        mBaseAdapter = mBaseAdapter == null ? new EditBaseAdapter(((BaseAdapter) adapter)) : mBaseAdapter;
        mBaseAdapter.setAdapter(((BaseAdapter) adapter));
        super.setAdapter(mBaseAdapter);
        setListViewHeight();
        smoothScrollToHeader(this);
    }

    /**
     * 内置适配器
     */
    class EditBaseAdapter extends BaseAdapter{
        private BaseAdapter mAdapter;

        public EditBaseAdapter(@NonNull BaseAdapter adapter) {
            mAdapter = adapter;
        }

        public void setAdapter(BaseAdapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public int getCount() {
            return mAdapter.getCount();
        }

        @Override
        public Object getItem(int position) {
            return mAdapter.getItem(position);
        }

        @Override
        public long getItemId(int position) {
            return mAdapter.getItemId(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = mAdapter.getView(position, convertView == null ? null : ((LinearLayout) convertView).getChildAt(1), parent);
            ViewHolder holder = ViewHolder.newInstance(parent.getContext(), convertView, R.layout.item_lv_lib);
            LinearLayout linearLayout = holder.getView(R.id.ll_item_lib, LinearLayout.class);
            if (linearLayout.indexOfChild(view) != -1) {
                linearLayout.removeView(view);
            }
            linearLayout.addView(view);
            ImageView imageView = holder.getView(R.id.iv_item_lib, ImageView.class);
            imageView.setVisibility(mEditState ? VISIBLE : GONE);
            imageView.setBackgroundResource(mUncheckedRes);
            imageView.setImageResource(isItemChecked(position) ? mCheckedRes : 0);
            return holder.mConvertView;
        }

        @Override
        public boolean hasStableIds() {
            return mAdapter.hasStableIds();
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
            mAdapter.registerDataSetObserver(observer);
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            mAdapter.unregisterDataSetObserver(observer);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            mAdapter.notifyDataSetChanged();
            setListViewHeight();
        }

        @Override
        public void notifyDataSetInvalidated() {
            super.notifyDataSetInvalidated();
            mAdapter.notifyDataSetInvalidated();
        }

        @Override
        public boolean areAllItemsEnabled() {
            return mAdapter.areAllItemsEnabled();
        }

        @Override
        public boolean isEnabled(int position) {
            return mAdapter.isEnabled(position);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return mAdapter.getDropDownView(position,convertView,parent);
        }

        @Override
        public int getItemViewType(int position) {
            return mAdapter.getItemViewType(position);
        }

        @Override
        public int getViewTypeCount() {
            return mAdapter.getViewTypeCount();
        }

        @Override
        public boolean isEmpty() {
            return mAdapter.isEmpty();
        }
    }

    /**
     * 所有条目选中/未选中监听
     */
    public interface OnAllItemCheckedListener{
        void onAllItemChecked(boolean checked);
    }
}
