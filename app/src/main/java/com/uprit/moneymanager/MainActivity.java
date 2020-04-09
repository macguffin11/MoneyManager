package com.uprit.moneymanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ADD = 1;
    public static final String TAG = "SQLiteDebug";
    DBAdapter db = new DBAdapter(this);
    private ArrayList<Finance> list = new ArrayList<>();
    FloatingActionButton addButton;
    private RecyclerView recyclerView;
    FinanceAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectAllFromDatabase();

        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swiper);
        adapter = new FinanceAdapter(this, list, swipeRefreshLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        deleteData();

        setupAddButton();
    }


    // Swipe delete
    private void deleteData(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final String aTitle = list.get(position).getTitle();
                final String aPrice = list.get(position).getPrice();
                final String aDate = list.get(position).getDate();

                db.open();
                db.deleteFinance(list.get(position).getRowId());
                db.close();


                list.remove(position);
                Toast.makeText(MainActivity.this, "Item Removed" + position, Toast.LENGTH_SHORT).show();
                adapter.notifyItemRemoved(position);


                Snackbar.make(recyclerView, aTitle, Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Finance aFinance = null;
                                try {
                                    @SuppressLint("SimpleDateFormat")
                                    Date aFDate = new SimpleDateFormat("dd/MMM/yyyy").parse(aDate);
                                    aFinance = new Finance(aTitle, aPrice, aFDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                db.open();
                                db.insertFinance(aTitle, aPrice, aDate);
                                db.close();

                                list.add(position, aFinance);
                                adapter.notifyItemInserted(position);
                            }
                        }).show();
            }
        }).attachToRecyclerView(recyclerView);


    }

    private void setupAddButton(){
        addButton = findViewById(R.id.fab);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveIntent = new Intent(MainActivity.this, AddFinance.class);
                startActivityForResult(moveIntent, REQUEST_CODE_ADD);
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_ADD:
                if (resultCode == Activity.RESULT_OK) {
                    assert data != null;
                    Bundle bundle = data.getExtras();
                    assert bundle != null;
                    String title = bundle.getString("title");
                    String price = bundle.getString("price");
                    String date = bundle.getString("date");
                    Date aDate = null;
                    try {
                        assert date != null;
                        aDate = new SimpleDateFormat("dd/MMM/yyyy").parse(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Finance aFinance = new Finance(title, price, aDate);
                    // Coba ke DB.
                    insertToDatabase(aFinance);
                    list.add(aFinance);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Data berhasil disimpan.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Tambah Task dibatalkan.", Toast.LENGTH_LONG).show();
                }
        }
    }

    // Insert data Task ke dalam Database.
    private void insertToDatabase(Finance aFinance){
        db.open();
        String dateTask;
        dateTask = aFinance.getDate();
        db.insertFinance(aFinance.getTitle(), aFinance.getPrice(), dateTask);
        db.close();
    }

    private void selectAllFromDatabase(){
        db.open();
        list = db.getAllFinance();
        db.close();
    }
}
