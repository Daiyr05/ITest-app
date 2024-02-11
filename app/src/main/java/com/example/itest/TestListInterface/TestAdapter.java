package com.example.itest.TestListInterface;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.itest.R;
import com.example.itest.modelsOftest.Test;

import java.util.ArrayList;
import java.util.List;

public class TestAdapter extends ArrayAdapter<Test> {

    private Context context;
    private int resource;
    private ArrayList<Test> objects;

    public TestAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Test> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(this.context).inflate(this.resource,null);
        Test t = this.objects.get(position);
        TextView lblSubject = view.findViewById(R.id.testName);
        TextView lblTheme = view.findViewById(R.id.testTheme);
        lblSubject.setText(t.getSubject()+" : ");
        lblTheme.setText(t.getTheme());

        return view;
    }
}
