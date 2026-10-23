package com.example.lab13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<Student> arrayList;
    private StudentAdapter adapter;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list);
        dbHelper = new DBHelper(this);
        arrayList = dbHelper.getAllStudent();
        adapter = new StudentAdapter(this, R.layout.layout_row, arrayList);
        listView.setAdapter(adapter);

        registerForContextMenu(listView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mnu_add) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Insert student");
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.layout_input, null);
            builder.setView(view);

            EditText edtName = view.findViewById(R.id.edtName);
            EditText edtAddress = view.findViewById(R.id.edtAddress);
            RadioGroup rdgGender = view.findViewById(R.id.rdgGender);
            RadioButton rdbMale = view.findViewById(R.id.rdbMale);
            RadioButton rdbFemale = view.findViewById(R.id.rdbFemale);
            ImageView image = view.findViewById(R.id.image);

            // Default
            rdbMale.setChecked(true);
            image.setImageResource(R.drawable.male);

            rdgGender.setOnCheckedChangeListener((group, checkedId) -> {
                if (checkedId == R.id.rdbMale) {
                    image.setImageResource(R.drawable.male);
                } else if (checkedId == R.id.rdbFemale) {
                    image.setImageResource(R.drawable.female);
                }
            });

            builder.setPositiveButton("Yes", (dialog, which) -> {
                String name = edtName.getText().toString();
                String address = edtAddress.getText().toString();
                int gender = rdbMale.isChecked() ? 1 : 0;
                Student student = new Student(0, name, address, gender);
                dbHelper.insertStudent(student);
                arrayList.add(student);
                adapter.notifyDataSetChanged();
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        Student student = arrayList.get(position);

        if (item.getItemId() == R.id.mnu_update) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Update student");
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.layout_input, null);
            builder.setView(view);

            EditText edtName = view.findViewById(R.id.edtName);
            EditText edtAddress = view.findViewById(R.id.edtAddress);
            RadioGroup rdgGender = view.findViewById(R.id.rdgGender);
            RadioButton rdbMale = view.findViewById(R.id.rdbMale);
            RadioButton rdbFemale = view.findViewById(R.id.rdbFemale);
            ImageView image = view.findViewById(R.id.image);

            edtName.setText(student.getName());
            edtAddress.setText(student.getAddress());
            if (student.getGender() == 1) {
                rdbMale.setChecked(true);
                image.setImageResource(R.drawable.male);
            } else {
                rdbFemale.setChecked(true);
                image.setImageResource(R.drawable.female);
            }

            rdgGender.setOnCheckedChangeListener((group, checkedId) -> {
                if (checkedId == R.id.rdbMale) {
                    image.setImageResource(R.drawable.male);
                } else if (checkedId == R.id.rdbFemale) {
                    image.setImageResource(R.drawable.female);
                }
            });

            builder.setPositiveButton("Yes", (dialog, which) -> {
                student.setName(edtName.getText().toString());
                student.setAddress(edtAddress.getText().toString());
                student.setGender(rdbMale.isChecked() ? 1 : 0);
                dbHelper.updateStudent(student);
                adapter.notifyDataSetChanged();
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            builder.create().show();

        } else if (item.getItemId() == R.id.mnu_delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Delete student");
            builder.setMessage("Are you sure delete \"" + student.getName() + "\"?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                dbHelper.deleteStudent(student);
                arrayList.remove(position);
                adapter.notifyDataSetChanged();
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        }

        return super.onContextItemSelected(item);
    }
}
