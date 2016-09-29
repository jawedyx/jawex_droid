package pw.jawedyx.aimsupporter;


import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder>{
    private Cursor cursor;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        public ViewHolder(CardView v){
            super(v);
            cardView = v;
        }
    }

    public CardAdapter(Cursor cursor){
        cursor.moveToFirst();
        this.cursor = cursor;

    }

    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(CardAdapter.ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        TextView textAimName = (TextView)cardView.findViewById(R.id.card_text_name);
        TextView textAimDate = (TextView)cardView.findViewById(R.id.card_text_date);

            textAimName.setText(cursor.getString(1));
            textAimDate.setText(cursor.getString(2));
            cursor.moveToNext();


    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }
}
