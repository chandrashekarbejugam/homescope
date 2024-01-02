package com.codeinfinity.homescope;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private List<Property> propertyList;
    private int selectedPriceFilter = -1;  // Default value indicating no filter selected

    public SearchAdapter(List<Property> propertyList) {
        this.propertyList = propertyList;
    }

    public void setPropertyList(List<Property> propertyList) {
        this.propertyList = propertyList;
        notifyDataSetChanged();
    }

    public void setSelectedPriceFilter(int priceFilter) {
        selectedPriceFilter = priceFilter;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Property property = null;
        if (selectedPriceFilter == -1) {
            property = propertyList.get(position);
        } else {
            int count = 0;
            for (Property prop : propertyList) {
                if (prop.getPrice().equals(String.valueOf(selectedPriceFilter))) {
                    if (count == position) {
                        property = prop;
                        break;
                    }
                    count++;
                }
            }
        }

        // Set the property data to the appropriate views in the item layout
        holder.nameTextView.setText(property.getName());
        holder.priceTextView.setText("Rs. " + String.valueOf(property.getPrice()));



        holder.beds.setText(String.valueOf(property.getBedrooms()));
        holder.baths.setText(String.valueOf(property.getBathrooms()));

        Glide.with(holder.itemView.getContext())
                .load(property.getImage())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Property property = null;
                if (selectedPriceFilter == -1) {
                    property = propertyList.get(holder.getAdapterPosition());
                } else {
                    int count = 0;
                    for (Property prop : propertyList) {
                        if (prop.getPrice().equals(String.valueOf(selectedPriceFilter)))  {
                            if (count == holder.getAdapterPosition()) {
                                property = prop;
                                break;
                            }
                            count++;
                        }
                    }
                }

                Intent intent = new Intent(v.getContext(), HomeDetailsActivity.class);
                intent.putExtra("property_name", property.getName());
                intent.putExtra("property_price", property.getPrice());
                intent.putExtra("property_description", property.getDescription());
                intent.putExtra("property_address", property.getAddress());
                intent.putExtra("image_path", property.getImage());
                intent.putExtra("property_area", property.getAreaSqft());
                intent.putExtra("property_beds", property.getBedrooms());
                intent.putExtra("property_baths", property.getBathrooms());
                intent.putExtra("property_living", property.getLivingRooms());
                intent.putExtra("property_facilities", property.getFacilities());
                intent.putExtra("property_uid", property.getUid());

                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (selectedPriceFilter == -1) {
            return propertyList.size();
        } else {
            int count = 0;
            for (Property property : propertyList) {
                if (property.getPrice().equals(String.valueOf(selectedPriceFilter))) {
                    count++;
                }
            }
            return count;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView priceTextView;
        public TextView addressTextView;

        public TextView beds;
        public TextView baths;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.houseNameSearch);
            priceTextView = itemView.findViewById(R.id.housePriceSearch);

            beds = itemView.findViewById(R.id.bedCountSearch);
            baths = itemView.findViewById(R.id.bathCountSearch);
            imageView = itemView.findViewById(R.id.searchImg);
        }
    }
}
