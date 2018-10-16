package petersoboyejo.com.maptour;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.MyViewHolder> {

    private List<Location> locations;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView locationName, cord;

        public MyViewHolder(View view) {
            super(view);
            locationName = (TextView) view.findViewById(R.id.locationName);
            cord = (TextView) view.findViewById(R.id.cord);
        }
    }


    public LocationAdapter(List<Location> locations) {
        this.locations = locations;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.destinations_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Location movie = locations.get(position);
        holder.locationName.setText(movie.getName());
        holder.cord.setText(Double.toString(movie.getLatitude()) + ", " + Double.toString(movie.getLongitude()));
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }
}