package com.uprit.moneymanager;

import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class FinanceViewHolder extends RecyclerView.ViewHolder{
    TextView title, price, date;
    FinanceViewHolder(View itemView) {
        super(itemView);
        CardView cardView = (CardView) itemView.findViewById(R.id.cardView);
        title = (TextView) itemView.findViewById(R.id.title);
        price = (TextView) itemView.findViewById(R.id.price);
        date = (TextView) itemView.findViewById(R.id.date);
    }
}
