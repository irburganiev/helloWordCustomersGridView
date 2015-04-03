package com.example.myapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Created by ilnur on 09.03.2015.
 */
public class ShowChild extends Activity {

    private Customer customer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showelement);
        customer = (Customer) getIntent().getSerializableExtra(MainActivity.PersonExtra);
        ((EditText) this.findViewById(R.id.NameTxt)).setText(customer.get_name());
        ((EditText) this.findViewById(R.id.SurNameTxt)).setText(customer.get_surName());
        ((EditText) this.findViewById(R.id.AgeTxt)).setText(customer.get_age().toString());
    }


    public void OnOkClick(View view) {
        EditCustomer();
        this.finish();
    }

    private void EditCustomer() {
        customer.set_name(((EditText) this.findViewById(R.id.NameTxt)).getText().toString());
        customer.set_surName(((EditText) this.findViewById(R.id.SurNameTxt)).getText().toString());
        customer.set_age(Integer.parseInt(((EditText) this.findViewById(R.id.AgeTxt)).getText().toString()));
        Intent newIntent = new Intent(MainActivity.EditNewPerson);
        setIntent(newIntent);
        newIntent.putExtra(MainActivity.PersonExtra, customer);
        setResult(0,newIntent);
    }
}