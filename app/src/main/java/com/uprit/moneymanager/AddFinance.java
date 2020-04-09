package com.uprit.moneymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

public class AddFinance extends AppCompatActivity {
    private EditText title, price;
    private TextView date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_finance);
        setupDateButton();
        setupSubmitButton();
    }

    private void setupDateButton(){
        Button datePicker  = findViewById(R.id.datePicker);
        this.date = findViewById(R.id.dateView);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar;
                newCalendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddFinance.this, new DatePickerDialog.OnDateSetListener() {
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

    private void setupSubmitButton(){
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
                    Toast.makeText(AddFinance.this,"Data task kurang lengkap.",Toast.LENGTH_SHORT).show();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("title", sTitle);
                    bundle.putString("price", sPrice);
                    bundle.putString("date", sDate);
                    Intent moveIntent = new Intent();
                    moveIntent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, moveIntent);
                    finish();
                }
            }
        });
    }
}
