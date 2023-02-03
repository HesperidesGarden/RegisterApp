package com.example.testdatabaselogtag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class BillActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        getSupportActionBar().setTitle("Bill");
    }

    //Create ActionBar Menu
    public boolean onCreateOptionsMenu (Menu menu){

        //Inflate menu items
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //When menu item is selected

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id== R.id.menu_product){
            Intent intent = new Intent(BillActivity.this, ProductActivity.class);
            startActivity(intent);
        } else if(id == R.id.menu_productCats){
            Intent intent = new Intent(BillActivity.this, CategoryActivity.class);
            startActivity(intent);
        }else if(id == R.id.menu_account){
            Intent intent = new Intent(BillActivity.this, AccountActivity.class);
            startActivity(intent);
        }else if(id == R.id.menu_bill){
            Intent intent = new Intent(BillActivity.this, BillActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}