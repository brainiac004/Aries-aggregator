package brainiac.nht.ariesaggregator.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import brainiac.nht.ariesaggregator.Entity.Coin;
import brainiac.nht.ariesaggregator.Interface.ILoadMore;
import brainiac.nht.ariesaggregator.R;

public class CoinAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ILoadMore iLoadMore;
    boolean isLoading;
    Activity activity;
    List<Coin> items;

    int visibleThreshold = 5, lastVisibleItem, totalItemCount;

    public CoinAdapter(RecyclerView recyclerView, Activity activity, List<Coin> items) {
        this.activity = activity;
        this.items = items;
        final LinearLayoutManager linearLayoutManager =(LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)){
                    if (iLoadMore != null){
                        iLoadMore.onLoadMore();
                        isLoading = true;
                    }
                }

            }
        });
    }

    public void setiLoadMore(ILoadMore iLoadMore) {
        this.iLoadMore = iLoadMore;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(activity)
                    .inflate(R.layout.coin_table_layout, parent, false);
        return new CoinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Coin item = items.get(position);
        CoinViewHolder holderItem =(CoinViewHolder) holder;

        holderItem.coin_name.setText(item.getName());
        holderItem.coin_symbol.setText(item.getSymbol());
        holderItem.coin_price.setText(item.getPrice_usd());
        holderItem.on_hour_change.setText(item.percent_change_1h + "%");
        holderItem.twenty_four_hour_change.setText(item.percent_change_24h + "%");
        holderItem.seven_day_change.setText(item.percent_change_7d + "%");

        // load Imaage
        Picasso.with(activity)
                .load(new StringBuilder("https://res.cloudinary.com/dxi90ksom/image/upload/").append(item.getSymbol().toLowerCase()).append(".png").toString())
                .into(holderItem.coin_icon);

        holderItem.on_hour_change.setTextColor(item.getPercent_change_1h().contains("-")?
                Color.parseColor("#FF0000"):Color.parseColor("#32CD32"));
        holderItem.twenty_four_hour_change.setTextColor(item.getPercent_change_1h().contains("-")?
                Color.parseColor("#FF0000"):Color.parseColor("#32CD32"));
        holderItem.seven_day_change.setTextColor(item.getPercent_change_1h().contains("-")?
                Color.parseColor("#FF0000"):Color.parseColor("#32CD32"));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setLoaded(){
        isLoading = true;
    }
    public void updateData(List<Coin> coins){
        this.items = coins;
        notifyDataSetChanged();
    }
}
