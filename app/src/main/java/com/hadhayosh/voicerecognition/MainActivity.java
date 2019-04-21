package com.hadhayosh.voicerecognition;

import android.app.Dialog;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_recognize)
    Button recognize;
    @BindView(R.id.tv_result)
    TextView result;
    private static final int REQUEST_CODE = 1001;
    Dialog match_dialog;
    ListView list_matches;
    ArrayList<String> matches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        recognize.setOnClickListener(view -> {
            if(NetworkUtility.isConnected(MainActivity.this)){
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fa-IR");
                startActivityForResult(intent, REQUEST_CODE);
            }
            else{
                Toast.makeText(MainActivity.this, R.string.connectivity_warning, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            match_dialog = new Dialog(MainActivity.this);
            match_dialog.setContentView(R.layout.dialog_matches_frag);
            match_dialog.setTitle(R.string.choose);

            matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, matches);
            list_matches = match_dialog.findViewById(R.id.lst_matches);
            list_matches.setAdapter(adapter);
            list_matches.setOnItemClickListener((parent, view, position, id) -> {
                result.setText(getString(R.string.say) + matches.get(position));
                match_dialog.hide();
            });

            match_dialog.show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
