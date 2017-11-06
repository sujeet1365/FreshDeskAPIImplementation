package com.example.sujeet.freshdeskintegration;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sujeet on 04-11-2017.
 */

public class ArrayAdapter_Custom extends android.widget.ArrayAdapter<String> {
    LayoutInflater inflater;
    Context context;
    TextView elements;
    String[] data;

    public ArrayAdapter_Custom(@NonNull Context context, @LayoutRes int resource, String[] data) {
        super(context, resource);
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        vi = inflater.inflate(R.layout.conversation, null);
        elements = (TextView) vi.findViewById(R.id.elements);
        elements.setText(data[position]);
        return vi;
    }
}
