package com.example.hapinesssurvey;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class ThirdFragment extends Fragment {

    float rating = 0;
    ArrayList<Integer> ratings = new ArrayList<>();
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        float ratingsfloat = getArguments().getFloat("message");
        ratings = getArguments().getIntegerArrayList("ratings");
        System.out.println(ratingsfloat);
        rating = ratingsfloat;
        return inflater.inflate(R.layout.fragment_third, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textView = view.findViewById(R.id.textview_second);
        TextView confidenceTV = view.findViewById(R.id.ConfidenceTextView);
        TextView recommandations = view.findViewById(R.id.Reccomendations_textview);
        textView.setText("U bent: " + ConfidenceToRating(rating, view));
        confidenceTV.setText("Het model is hier: " + rating * 100 + "% zeker van");
        recommandations.setText(getRecommendations(ratings));
        view.setBackgroundColor(Color.WHITE);
    }

    private String getRecommendations(ArrayList<Integer> ratings) {
        int amount = 0;
        String rec = "";
        if (ratings.get(0) <3) {
            rec = rec + "-Try to contact the local information points for more information about your city. \n";
            amount++;
        }
        if(ratings.get(1) < 3){
            rec = rec + "-Contact your bank to see how you can lower your cost for housing. \n";
            amount++;
        }
        if(ratings.get(2) < 3){
            rec = rec + "-Contact the school for a appointment to say your complaints. \n";
            amount++;
        }
        if(ratings.get(3) < 3 && amount <= 3){
            rec = rec + "-Try to set up a neighbourhood watch. \n";
            amount++;
        }
        if(ratings.get(4) < 3 && amount <= 3){
            rec = rec + "-Contact the local commune to set your complaint. \n";
            amount++;
        }
        if(ratings.get(5) < 3 && amount <= 3){
            rec = rec + "-Try to find some social events in the local area to attend. \n";
            amount++;
        }
        return rec;
    }

    public String ConfidenceToRating(float confidence, View view) {
        ImageView imageView = view.findViewById(R.id.imageView);
        if (confidence > 0.5) {
            imageView.setImageResource(R.drawable.positive);
            return "Tevreden";
        } else{
            imageView.setImageResource(R.drawable.negative);
            return "Ontevreden";

        }
    }
}
