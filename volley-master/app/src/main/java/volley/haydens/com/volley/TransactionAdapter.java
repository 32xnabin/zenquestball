package volley.haydens.com.volley;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.agri.Models.Transaction;
import com.agri.R;


/**
 * Created by asha on 4/23/2018.
 */

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    /**
     * Create ViewHolder class to bind list item view
     */
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public TextView rate;
        public TextView qty;
        public TextView total;

        public ImageView thumbnail;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.tTitle);
            rate = (TextView) itemView.findViewById(R.id.tRate);
            qty = (TextView) itemView.findViewById(R.id.tQuantity);
            total = (TextView) itemView.findViewById(R.id.tTotal);
            thumbnail = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }

    private List<Transaction> mListData;
    private Context mContext;

    public TransactionAdapter(Context context, List<Transaction> listData){
        mListData = listData;
        mContext = context;
    }

    private Context getmContext(){return mContext;}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View view = inflater.inflate(R.layout.trans_list_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Transaction t = mListData.get(position);
        holder.title.setText(t.getTitle());
        holder.rate.setText(t.getRate().toString());
        holder.qty.setText(t.getQty().toString());
        //Picasso.get().load(ApiEndpoint.BASE + m.getImage()).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }


}
