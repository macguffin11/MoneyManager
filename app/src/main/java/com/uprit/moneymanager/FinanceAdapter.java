package com.uprit.moneymanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

public class FinanceAdapter extends RecyclerView.Adapter<FinanceViewHolder> {
    Context c;
    ArrayList<Finance> dataList;
    SwipeRefreshLayout swipeRefreshLayout;

    public FinanceAdapter(Context c, ArrayList<Finance> dataList, SwipeRefreshLayout swipeRefreshLayout) {
        this.c = c;
        this.dataList = dataList;
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    public FinanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_list, parent, false);
        return new FinanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FinanceViewHolder holder, int position) {
        final Finance data = dataList.get(position);
        holder.title.setText(data.getTitle());
        holder.price.setText(data.getPrice());
        holder.date.setText(data.getDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putLong("rowId", data.getRowId());
                bundle.putString("title", data.getTitle());
                bundle.putString("price", data.getPrice());
                bundle.putString("date", data.getDate());
                Intent moveIntent = new Intent(v.getContext(), DetailFinance.class);
                moveIntent.putExtras(bundle);
                v.getContext().startActivity(moveIntent);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUpdates();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    private void getUpdates()
    {
        Log.d(MainActivity.TAG, "Refresh");
        dataList.clear();

        DBAdapter db = new DBAdapter(c);
        db.open();
        dataList = db.getAllFinance();
        if(dataList == null) {
            Log.d(MainActivity.TAG, "Error");
        }
        db.close();

        swipeRefreshLayout.setRefreshing(false);
        this.notifyDataSetChanged();
    }
}