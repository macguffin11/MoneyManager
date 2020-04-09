package com.uprit.moneymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DetailFinance extends AppCompatActivity {
    EditText title, price;
    TextView date;
    DBAdapter db = new DBAdapter(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_finance);
        title = findViewById(R.id.editTitle);
        price = findViewById(R.id.editPrice);
        date = findViewById(R.id.dateView);
        assert bundle != null;
        title.setText(bundle.getString("title"));
        price.setText(bundle.getString("price"));
        date.setText(bundle.getString("date"));
        setupDateButton();
        setupSubmitButton(bundle.getLong("rowId"));
    }

    private void setupDateButton(){
        Button datePicker  = findViewById(R.id.datePicker);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(DetailFinance.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MMM/yyyy", Locale.US);
                        date.setText(dateFormatter.format(newDate.getTime()).toUpperCase());
                    }

                },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(newCalendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });
    }

    private void setupSubmitButton(final long srowId){
        Button btn = findViewById(R.id.submitData);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Extract data.
                title = findViewById(R.id.editTitle);
                price = findViewById(R.id.editPrice);
                String sTitle  = title.getText().toString();
                String sPrice  = price.getText().toString();
                String sDate  = date.getText().toString();
                // Pass data.
                if(sTitle.equals("") || sPrice.equals("") || sDate.equals("")){
                    Toast.makeText(DetailFinance.this,"GAGAL : Data Kurang Lengkap.",Toast.LENGTH_SHORT).show();
                } else {
                    db.open();
                    boolean test = db.updateFinance(srowId, sTitle, sPrice, sDate);
                    db.close();
                    if (test){
                        Toast.makeText(DetailFinance.this,"SUKSES : Data Diperbaharui.",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(DetailFinance.this,"SUKSES : Data Diperbaharui.?",Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(DetailFinance.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
