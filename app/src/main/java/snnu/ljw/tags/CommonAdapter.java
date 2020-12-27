package snnu.ljw.tags;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {

    protected Context context;
    protected List<T> myData;
    protected int LayoutId;

    public CommonAdapter(Context context, List<T> data, int layoutId) {
        this.context = context;
        myData = data;
        this.LayoutId = layoutId;
    }

    @Override
    public int getCount() {
        return myData.size();
    }

    @Override
    public Object getItem(int position) {
        return myData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.getHolder(context,convertView,LayoutId,parent,position);
        convert(holder,position);
        return holder.getConvertView();
    }


    public abstract void convert(ViewHolder holder,int pos);
}
