package pw.jawedyx.aimsupporter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Antonio on 07.10.2016.
 */
public class BGAdapter extends BaseAdapter {
    private Context context;
    private static final Integer[] mRes = {R.drawable.faces, R.drawable.lodyas, R.drawable.restaurant,
            R.drawable.school, R.drawable.weather, R.drawable.congruent_outline,
            R.drawable.congruent_pentagon, R.drawable.sativa, R.drawable.skulls,
            R.drawable.stardust, R.drawable.swirl_pattern, R.drawable.triangular};

    public BGAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return mRes.length;
    }

    @Override
    public Object getItem(int position) {
        return mRes[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(convertView == null){
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(5,5,5,5);
        }else{
            imageView = (ImageView)convertView;
        }
        imageView.setImageResource(mRes[position]);
        return imageView;
    }
}
