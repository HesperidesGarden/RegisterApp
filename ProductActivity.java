package com.example.testdatabaselogtag;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.view.inputmethod.InputMethodManager;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ProductActivity extends AppCompatActivity {
    public static final String LOG_TAG = ProductActivity.class.getSimpleName();
    private ProductDataSource dataSource;
private ListView listViewProduct;
    final Context context = this;
    String productName;
    String productPriceString;
    String productDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product);
        getSupportActionBar().setTitle("Products");
        Log.d(LOG_TAG, "Das Datenquellen-Objekt wird angelegt.");
        dataSource = new ProductDataSource(this);

        activateAddButton();


    }
    //@Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "Die Datenquelle wird geöffnet.");
        dataSource.open();
        Log.d(LOG_TAG, "Folgende Einträge sind in der Datenbank vorhanden:");
        //showAllListEntries();
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
        dataSource.close();
    }
    private void activateAddButton() {
        Button buttonAddProduct = (Button) findViewById(R.id.Product_addBtn);

        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // get product dialog.xml view
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.product_dialog, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInputProductName = (EditText) promptsView.findViewById(R.id.Product_dialog_productname_editTxt);
                final EditText userInputProductPrice = (EditText) promptsView.findViewById(R.id.Product_dialog_productprice_editTxt);
                final EditText userInputProductDesc = (EditText) promptsView.findViewById(R.id.Product_dialog_productdesc_editTxt);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Create New Product!",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        productName = userInputProductName.getText().toString();
                                        productPriceString = userInputProductPrice.getText().toString();
                                        productDesc = userInputProductDesc.getText().toString();
                                        if(TextUtils.isEmpty(productName)|TextUtils.isEmpty(productPriceString)|TextUtils.isEmpty(productDesc)) {
                                            Toast.makeText(ProductActivity.this, "Incomplete Product!", Toast.LENGTH_SHORT).show();
                                            dialog.cancel();
                                        }
                                        int productPrice = Integer.parseInt(productPriceString);

                                        dataSource.createProduct(productName, productPrice, productDesc);
                                        InputMethodManager inputMethodManager;
                                        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                        if(getCurrentFocus() != null) {
                                            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                                        }
                                        showAllListEntries();

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

                showAllListEntries();




            }
        });
    }
    private void showAllListEntries () {
        List<Product> productList = dataSource.getAllProducts();
        ArrayAdapter<Product> productsArrayAdapter = new ArrayAdapter<> (
                this,
                android.R.layout.simple_list_item_multiple_choice,
                productList);
        ListView productListView = (ListView) findViewById(R.id.Product_list);
                productListView.setAdapter(productsArrayAdapter);
    }

}