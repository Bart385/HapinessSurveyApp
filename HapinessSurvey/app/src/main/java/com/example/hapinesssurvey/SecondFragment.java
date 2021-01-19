package com.example.hapinesssurvey;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class SecondFragment extends Fragment {
        public int questionNumber = 1;
        @Override
        public View onCreateView(
                LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState
    ) {
            return inflater.inflate(R.layout.fragment_second, container, false);
        }

        public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            questionAsker(view);
        }



        public void questionAsker(View view){
            view.setBackgroundColor(Color.WHITE);
            ArrayList<String> questions = new ArrayList<>();
            ArrayList<Integer> ratings = new ArrayList<>();
            questions.add("the availability of information about the city services");
            questions.add("the cost of housing");
            questions.add("the overall quality of public schools");
            questions.add("your trust in the local police");
            questions.add("the maintenance of streets and sidewalks");
            questions.add("the availability of social community events");

            TextView textView = view.findViewById(R.id.textview_question);
            RatingBar rating = view.findViewById(R.id.ratingBar);
            textView.setText(questions.get(0));

            view.findViewById(R.id.button_first).setOnClickListener(view1 -> {
                if (questionNumber != 6) {

                    ratings.add(rating.getProgress());
                    rating.setRating(0);
                    textView.setText(questions.get(questionNumber));
                    questionNumber++;
                }
                else if (questionNumber  == 6) {
                    ratings.add(rating.getProgress());
                    ThirdFragment thirdFragment = new ThirdFragment();
                    Bundle bundle=new Bundle();
                    bundle.putFloat("message", giveHap(ratings));
                    bundle.putIntegerArrayList("ratings", ratings);
                    thirdFragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(this.getId(),thirdFragment).addToBackStack(null).commit();
                }

            });

        }

        public float giveHap(ArrayList<Integer> ratings){
            float[] input = {ratings.get(0),ratings.get(1),ratings.get(2),ratings.get(3),ratings.get(4),ratings.get(5)};
            float[] output1 = {1};
            float[][] output = {output1};
            try{
                MappedByteBuffer tfliteModel
                        = FileUtil.loadMappedFile(this.getContext(),
                        "model.tflite");
                Interpreter tflite = new Interpreter(tfliteModel);

                if(null != tflite) {
                    tflite.run(input,output );
                }
            } catch (IOException e){
                Log.e("tfliteSupport", "Error reading model", e);
            }

            float outfloat = output[0][0];
            return outfloat;
        }
}