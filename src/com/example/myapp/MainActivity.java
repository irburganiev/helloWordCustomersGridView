package com.example.myapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;


public class MainActivity extends Activity implements PopupMenu.OnMenuItemClickListener {
    private GridView gridView;
    private BaseAdapter customGridViewAdapter;
    private ArrayList<Customer> customers;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        gridView = (GridView) findViewById(R.id.gridView);
        LoadData();
        customGridViewAdapter = new CustomGridViewAdapter(customers, this);/* new ArrayAdapter<Customer>(getApplicationContext(), android.R.layout.simple_list_item_1, customers);*/
        gridView.setAdapter(customGridViewAdapter);


    }

    private void LoadData() {
        customers = new ArrayList();
        for (Integer i = 1; i < 40; i++) {
            customers.add(new Customer("Ivan_" + i.toString(), "Ivanov_" + i.toString(), i + 1, i));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public static final String CreateNewPerson = "com.example.myapp.createNewPerson";
    public static final String EditNewPerson = "com.example.myapp.editNewPerson";

    public static final String PersonExtra = "com.example.myapp.editNewPerson.person";


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (data.getAction() == EditNewPerson) {
            Customer customer = (Customer) data.getSerializableExtra(PersonExtra);
            customers.set(customers.indexOf(customer), customer);
            customGridViewAdapter.notifyDataSetChanged();
        }
        if (data.getAction() == CreateNewPerson) {
            customers.add((Customer) data.getSerializableExtra(PersonExtra));
            customGridViewAdapter.notifyDataSetChanged();
        }

    }

    /**
     * This method will be invoked when a menu item is clicked if the item itself did
     * not already handle the event.
     *
     * @param item {@link android.view.MenuItem} that was clicked
     * @return <code>true</code> if the event was handled, <code>false</code> otherwise.
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.viewMenuItem:
                EditPerson((Customer) gridView.getSelectedItem());
                return true;


        }
        return false;
    }

    public class CustomGridViewAdapter extends BaseAdapter {
        private ArrayList<Customer> customers;
        private Context context;

        public CustomGridViewAdapter(ArrayList<Customer> customers, Context context) {
            this.customers = customers;
            this.context = context;
        }

        public int getCount() {
            return customers.size();
        }

        public Object getItem(int i) {
            return customers.get(i);
        }


        public Customer getCustomer(int i) {
            return customers.get(i);
        }

        public long getItemId(int i) {
            return i;
        }

        public View getView(final int position, final View convertView, final ViewGroup parent) {
            Log.d("ViewItem", "Begin");
            View grid;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                grid = inflater.inflate(R.layout.gridviewitem, null);

            } else {
                grid = convertView;
            }

            TextView textViewName = (TextView) grid.findViewById(R.id.textViewName);
            TextView textViewSurname = (TextView) grid.findViewById(R.id.textViewSurname);
            textViewName.setText(this.getCustomer(position).get_name());
            textViewSurname.setText(this.getCustomer(position).get_surName());
            ((TextView) grid.findViewById(R.id.TextViewAge)).setText(getCustomer(position).get_age().toString());

            ViewGroup layoutView = ((ViewGroup) ((ViewGroup) grid).getChildAt(0));

            for (Integer i = 0; i < layoutView.getChildCount(); i++) {
                layoutView.getChildAt(i).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v);
                        gridView.requestFocusFromTouch();
                        gridView.setSelection(position);
                        popupMenu.inflate(R.menu.grid_context_menu);
                        popupMenu.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener) gridView.getContext());
                        popupMenu.show();
                        return true;
                    }
                });
            }

            grid.findViewById(R.id.buttonDelete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Удалить запись?");
                    builder.setTitle("Внимание");
                    builder.setCancelable(true);
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            customers.remove(position);
                            customGridViewAdapter.notifyDataSetChanged();
                        }
                    });
                    builder.show();
                }
            });
            ((Button) grid.findViewById(R.id.buttonViewDetail)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Customer customer = (Customer) gridView.getAdapter().getItem(position);
                    EditPerson(customer);
                }
            });

            Log.d("ViewItem", "End");
            return grid;

        }
    }

    private void EditPerson(Customer customer) {
        Intent intent = new Intent(EditNewPerson, Uri.EMPTY, getApplicationContext(), ShowChild.class);
        intent.putExtra(PersonExtra, customer);
        startActivityForResult(intent, 0);
    }


}
