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


public class MainActivity extends Activity {
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
        customers = new ArrayList();
        for (Integer i = 1; i < 40; i++) {
            customers.add(new Customer("Ivan_" + i.toString(), "Ivanov_" + i.toString(), i + 1, i));
        }

        gridView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
                popupMenu.inflate(R.menu.grid_context_menu);
                popupMenu.show();
                return true;
            }
        });
        gridView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                // Inflate the menu for the CAB
                MenuInflater inflater = actionMode.getMenuInflater();
                inflater.inflate(R.menu.grid_context_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.deleteMenuItem:
                        actionMode.finish();
                        return true;
                    case R.id.viewMenuItem:
                        EditPerson((Customer) gridView.getAdapter().getItem(gridView.getCheckedItemPosition()));
                        actionMode.finish();
                        return true;

                    case R.id.createMenuItem:
                        actionMode.finish();
                    default:
                        return false;
                }

            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }


        });
        customGridViewAdapter = new CustomGridViewAdapter(customers, this);/* new ArrayAdapter<Customer>(getApplicationContext(), android.R.layout.simple_list_item_1, customers);*/
        gridView.setAdapter(customGridViewAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
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
            customGridViewAdapter.notifyDataSetChanged();
            Customer customer = (Customer) data.getSerializableExtra(PersonExtra);
            customers.set(customers.indexOf(customer), customer);
            customGridViewAdapter.notifyDataSetChanged();
        }

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

        public View getView(final int position, View convertView, ViewGroup parent) {
            Log.d("sd", "Begin");
            View grid;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                grid = new View(context);
                grid = inflater.inflate(R.layout.gridviewitem, null);

            } else {
                grid = (View) convertView;
            }
            TextView textViewName = (TextView) grid.findViewById(R.id.textViewName);
            TextView textViewSurname = (TextView) grid.findViewById(R.id.textViewSurname);
            textViewName.setText(this.getCustomer(position).get_name());
            textViewSurname.setText(this.getCustomer(position).get_surName());
            ((TextView) grid.findViewById(R.id.TextViewAge)).setText(getCustomer(position).get_age().toString());

            grid.findViewById(R.id.buttonDelete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Удалить запись?");
                    builder.setTitle("!!!");
                    builder.setCancelable(true);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            customers.remove(position);
                            customGridViewAdapter.notifyDataSetChanged();
                        }
                    });
                    AlertDialog alertDialog = builder.show();
                }
            });
            final View finalGrid = grid;
            ((Button) grid.findViewById(R.id.buttonViewDetail)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*finalGrid.requestFocusFromTouch();
                    gridView.setSelection(position);*/
                    Customer customer = (Customer) gridView.getAdapter().getItem(position);
                    EditPerson(customer);
                }
            });

            Log.d("sd", "End");
            return grid;

        }
    }

    private void EditPerson(Customer customer) {
        Intent intent = new Intent(EditNewPerson, Uri.EMPTY, getApplicationContext(), ShowChild.class);
        intent.putExtra(PersonExtra, customer);
        startActivityForResult(intent, 0);
    }


    public void OnClick(View view) {
        Toast.makeText(getApplicationContext(), R.string.toast_message, Toast.LENGTH_SHORT).show();
    }


}
