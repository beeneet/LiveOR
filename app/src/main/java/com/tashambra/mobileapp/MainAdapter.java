package com.tashambra.mobileapp;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    ArrayList<Drink> drinks;

    public MainAdapter(ArrayList<Drink> drinks) {
        this.drinks = drinks;
    }

    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(MainAdapter.ViewHolder holder, int position) {
        String drinkAlcohol = String.valueOf(drinks.get(position).getAlcoholPercent());
        String drinkVolume = String.valueOf(drinks.get(position).getVolume());
        holder.mDrinkName.setText(drinks.get(position).getName());
        Log.i("DAlcohol", drinkAlcohol + " " + drinkVolume);
        holder.mDrinkAlcohol.append(drinkAlcohol);
        holder.mDrinkVolume.append(drinkVolume);
        holder.mDrinkVolume.append(" oz");
    }

    @Override
    public int getItemCount() {
        return drinks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mDrinkName;
        public TextView mDrinkAlcohol;
        public TextView mDrinkVolume;

        public ViewHolder(View itemView) {
            super(itemView);
            mDrinkName = itemView.findViewById(R.id.user_drinkname);
            mDrinkAlcohol = itemView.findViewById(R.id.user_alcoholpercent);
            mDrinkVolume = itemView.findViewById(R.id.user_volume);

        }
    }
}
