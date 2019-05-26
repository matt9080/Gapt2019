package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StepsFragment extends Fragment {

    private TextView textcontent;
    private TextView textlabel;
    private ImageView imageview;

    public StepsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        String steps_text = getArguments().getString("steps_text");
        switch(getArguments().getString("steps_type")) {
            //populating ui if the next how to stage is of type step
            case "step":
                view = inflater.inflate(R.layout.fragment_steps, container, false);
                imageview = view.findViewById(R.id.image_howto);
                textlabel = view.findViewById(R.id.text_label);
                textcontent = view.findViewById(R.id.text_content);
                String[] howto = (getArguments().getString("steps_text")).split(":");
                if(howto[0].toLowerCase().contains("items")){
                    textcontent.setText("");
                    for(int i =1; i<howto.length;i++){
                        textcontent.setText(textcontent.getText()+"• "+howto[i]+"\n");
                    }
                }else{

                    textcontent.setText(howto[1]);
                }
                textlabel.setText(howto[0]);
                Picasso.get().load(getArguments().getString("steps_image")).into(imageview);
                break;
            //populating ui if the next how to stage is of type fact
            case "fact":
                view = inflater.inflate(R.layout.fragment_facts, container, false);
                textlabel = view.findViewById(R.id.text_label);
                textcontent = view.findViewById(R.id.text_content);
                String[] fact = (getArguments().getString("steps_text")).split(":");
                textlabel.setText(fact[0]);
                textcontent.setText(fact[1]);

                break;
            //populating ui if the next how to stage is of type questions
            case "questions":
                view = inflater.inflate(R.layout.fragment_questions, container, false);
                List<String> questions = HomeFragment.curr_activity.getQuestions();

                TextView question1 = view.findViewById(R.id.q1);
                TextView question2 = view.findViewById(R.id.q2);
                TextView question3 = view.findViewById(R.id.q3);
                RadioButton rb1 = view.findViewById(R.id.radioButton1);
                RadioButton rb2 = view.findViewById(R.id.radioButton2);
                RadioButton rb3 = view.findViewById(R.id.radioButton3);
                RadioButton rb4 = view.findViewById(R.id.radioButton4);
                RadioButton rb5 = view.findViewById(R.id.radioButton5);
                RadioButton rb6 = view.findViewById(R.id.radioButton6);
                RadioButton rb7 = view.findViewById(R.id.radioButton7);
                RadioButton rb8 = view.findViewById(R.id.radioButton8);
                RadioButton rb9 = view.findViewById(R.id.radioButton9);
                Button button = view.findViewById(R.id.completelesson);


                final String[] q1 = (questions.get(0)).split(":");
                final String[] q2 = (questions.get(1)).split(":");
                final String[] q3 = (questions.get(2)).split(":");
                textlabel = view.findViewById(R.id.text_label);
                textlabel.setText("Questions");
                question1.setText(q1[0]);
                question2.setText(q2[0]);
                question3.setText(q3[0]);

                Integer[] array = { 1, 2, 3 };
                List<Integer> list = Arrays.asList(array);
                Collections.shuffle(list);
                rb1.setText(q1[list.get(0)]);
                rb2.setText(q1[list.get(1)]);
                rb3.setText(q1[list.get(2)]);
                rb4.setText(q2[list.get(0)]);
                rb5.setText(q2[list.get(1)]);
                rb6.setText(q2[list.get(2)]);
                rb7.setText(q3[list.get(0)]);
                rb8.setText(q3[list.get(1)]);
                rb9.setText(q3[list.get(2)]);

                final View finalView = view;
                //creating the on click event for the 'complete lesson' button
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        RadioGroup group1  = (RadioGroup) finalView.findViewById(R.id.radioGroup1);
                        RadioGroup group2  = finalView.findViewById(R.id.radioGroup2);
                        RadioGroup group3  = finalView.findViewById(R.id.radioGroup3);
                        //checking if all answers are answered
                        if(group1.getCheckedRadioButtonId() == -1 || group2.getCheckedRadioButtonId() == -1 || group3.getCheckedRadioButtonId() == -1){
                            Toast.makeText(getActivity(), "All questions need to be answered", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        RadioButton answer1  = (RadioButton) finalView.findViewById(group1.getCheckedRadioButtonId());
                        RadioButton answer2  = (RadioButton) finalView.findViewById(group2.getCheckedRadioButtonId());
                        RadioButton answer3  = (RadioButton) finalView.findViewById(group3.getCheckedRadioButtonId());
                        int rightans = 0;
                        //checking if answers are correct
                        if (answer1.getText().equals(q1[1])){
                            rightans++;
                        }
                        if (answer2.getText().equals(q2[1])){
                            rightans++;
                        }
                        if (answer3.getText().equals(q3[1])){
                            rightans++;
                        }
                        //if all are correct the lesson is added to the users completed lessons
                        if (rightans == 3){
                            Toast.makeText(getActivity(), "Lesson complete", Toast.LENGTH_SHORT).show();
                            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference userupdate = db.collection("users").document(user.getUid());
                            userupdate.update("lessonscompleted", FieldValue.arrayUnion(HomeFragment.curr_activity.getID()));
                            getActivity().finish();
                            Intent myIntent1 = new Intent(getActivity(), MainFunctionsActivity.class);
                            getActivity().startActivity(myIntent1);
                        }else{
                            Toast.makeText(getActivity(), rightans+"/3 answers right. Try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            default:

        }


        return view;
    }
}
