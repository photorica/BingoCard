package jp.photorica.bingo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import jp.photorica.bingo.R;
import jp.photorica.bingo.fragment.BingoFragment;

/**
 * Created by yt_kaga on 2013/11/25.
 */
public class BingoAdapter extends ArrayAdapter<String> {

    private LayoutInflater mLayoutInflator;
    private ViewHolder mHolder;

    private class ViewHolder {
        public TextView numberView;
    }

    public BingoAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        mLayoutInflator = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = mLayoutInflator.inflate(R.layout.adapter_bingo, null);
            mHolder = new ViewHolder();
            mHolder.numberView = (TextView) convertView.findViewById(R.id.label_number);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        String number = getItem(position);
        mHolder.numberView.setText(number);

        if (BingoFragment.CENTER_NUMBER == position) {
            convertView.setEnabled(false);
            convertView.setBackgroundColor(Color.argb(255, 85, 85, 85));
        } else {
            convertView.setEnabled(true);
        }

        return convertView;
    }
}
