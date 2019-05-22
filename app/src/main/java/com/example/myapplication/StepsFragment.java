package com.example.myapplication;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepsFragment extends Fragment {

    private TextView textview;
    private ImageView imageview;
    public StepsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_steps, container, false);
        textview = view.findViewById(R.id.text_howto);
        imageview = view.findViewById(R.id.image_howto);
        String message = getArguments().getString("steps_text");
        textview.setText(message);
        Picasso.get().load(getArguments().getString("steps_image")).into(imageview);
        //imageview.setImageDrawable(LoadImageFromWebOperations()));
        // Inflate the layout for this fragment
        return view;
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

}
