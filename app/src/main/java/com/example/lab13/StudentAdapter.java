package com.example.lab13;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class StudentAdapter extends ArrayAdapter<Student> {
    private Activity context;
    private int resource;
    private ArrayList<Student> objects;

    public StudentAdapter(@NonNull Activity context, int resource, @NonNull ArrayList<Student> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View view = inflater.inflate(this.resource, null);

        ImageView image = view.findViewById(R.id.image);
        TextView txtName = view.findViewById(R.id.txtName);
        TextView txtAddress = view.findViewById(R.id.txtAddress);

        Student student = this.objects.get(position);

        txtName.setText(student.getName());
        txtAddress.setText(student.getAddress());
        if (student.getGender() == 1) {
            image.setImageResource(R.drawable.male);
        } else {
            image.setImageResource(R.drawable.female);
        }

        return view;
    }
}
