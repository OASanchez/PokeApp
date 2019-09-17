package com.example.pokepals;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class BasicPokeFragment extends Fragment {
    public View view;
    private TextView nameTxt, typeTxt;
    private ImageView imgView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.basic_poke_fragment, container, false);
        nameTxt = view.findViewById(R.id.nameTxt);
        typeTxt = view.findViewById(R.id.typeTxt);
        imgView = view.findViewById(R.id.imageView);
        // Inflate the layout for this fragment
        return view;
    }

    public void changeName(String name){
        nameTxt.setText(name);
    }

    public void changeType(String type){
        typeTxt.setText(type);
    }

    public void changeSprite(String spriteLink)
    {
        if (spriteLink != "null")
            Picasso.get().load(spriteLink).into(imgView);
        else
            Picasso.get().load("https://sainfoinc.com/wp-content/uploads/2018/02/image-not-available.jpg").into(imgView);
    }
}
