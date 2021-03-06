package bvu.edu.camapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

/**
 * Created by loc on 10/2/16.
 */

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private List<Person> dataset;

    ResultAdapter(List<Person> dataset){
        this.dataset = dataset;
    }

    public void setDataset(List<Person> newDataset){
        dataset.clear();
        dataset.addAll(newDataset);
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_results, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.person_name.setText(dataset.get(position).getFirstName() + " " + dataset.get(position).getLastName());
        holder.gps_lat.setText("GPS LAT: " + dataset.get(position).getLat());
        holder.gps_lng.setText("GPS LNG: " + dataset.get(position).getLng());
        if(dataset.get(position).getLat().length() > 3){
            holder.indicator.setBackgroundColor(ContextCompat.getColor(holder.context, R.color.colorAccent));
        }
        else{
            holder.indicator.setBackgroundColor(ContextCompat.getColor(holder.context, android.R.color.holo_red_light));
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView person_name, gps_lat, gps_lng;
        View indicator;
        Context context;
        public ViewHolder(View itemView) {
            super(itemView);
            person_name = (TextView) itemView.findViewById(R.id.person_name);
            gps_lat = (TextView) itemView.findViewById(R.id.gps_lat);
            indicator = (View) itemView.findViewById(R.id.indicator);
            gps_lng = (TextView) itemView.findViewById(R.id.gps_lng);

            context = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Integer position = getAdapterPosition();
            Intent intent = new Intent(itemView.getContext(), CameraActivity.class);
            intent.putExtra("PERSON",dataset.get(position).getPersonInfo());
            itemView.getContext().startActivity(intent);
        }
    }
}
