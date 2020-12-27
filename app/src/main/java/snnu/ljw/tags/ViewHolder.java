package snnu.ljw.tags;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ViewHolder {

    private SparseArray<View> mViews;
    private Context context;
    private View ConvertView;
    private int pos;

    public ViewHolder(Context context, int layoutId, ViewGroup parent, int pos) {
        this.ConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        this.mViews = new SparseArray<>();
        this.pos = pos;
        ConvertView.setTag(this);
    }

    public static ViewHolder getHolder(Context context, View convertView, int layoutId, ViewGroup parent, int pos) {
        if (convertView == null) {
            return new ViewHolder(context, layoutId, parent, pos);

        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.pos = pos;
            return holder;
        }
    }

    public View getConvertView() {
        return ConvertView;
    }

    public <T extends View> T getView(int ViewId){
        View view = mViews.get(ViewId);
        if (view == null){
            view = ConvertView.findViewById(ViewId);
            mViews.put(ViewId,view);
        }
        return (T)view;
    }

    public ViewHolder setTagText(int viewId,String text){
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }
}
