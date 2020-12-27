package snnu.ljw.tags;

import android.content.Context;

import java.util.List;

public class SearchAdapter extends CommonAdapter<Tag>{


    public SearchAdapter(Context context, List<Tag> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, int pos) {
        holder.setTagText(R.id.itemName,myData.get(pos).getName());
    }
}
