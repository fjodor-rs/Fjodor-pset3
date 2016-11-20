package com.fjodor.fjodor_pset3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.fjodor.fjodor_pset3.GetPoster.GetPoster;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.fjodor.fjodor_pset3.MESSAGE";
    private TextView movieData;

    public static final String MyPREFERENCES ="MyPrefs";
    public static final String mT = "movieKey";
    String movieTitle;
    String year;
    String poster;
    JSONObject parentObject;
    private ImageView iv;

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImageView) findViewById(R.id.image_view);
        final EditText edittext = (EditText) findViewById(R.id.edittext);
        Button btnSearch = (Button) findViewById(R.id.btnSearch);
        movieData = (TextView) findViewById(R.id.text_view);
        Button btnList = (Button) findViewById(R.id.btnList);
        Button btnAdd = (Button) findViewById(R.id.btnAdd);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            public Bitmap data_poster;

            @Override
            public void onClick(View v) {
                String input = String.valueOf(edittext.getText());
                edittext.setText("");

                input = input.replaceAll("\\s+", "+");

                String data = null;
                try {
                    data = new GetMovieInfo().execute(input).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                try {
                    parentObject = new JSONObject(data);
                    movieTitle = parentObject.getString("Title");
                    year = parentObject.getString("Year");
                    poster = parentObject.getString("Poster");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    data_poster = new GetPoster().execute(poster).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                iv.setImageBitmap(data_poster);
                String result = movieTitle + ", " + year;
                movieData.setText(result);


            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MovieActivity.class);

                intent.putExtra(EXTRA_MESSAGE, movieTitle);

                startActivity(intent);
            }
        });

        SharedPreferences.Editor spEditor = sharedpreferences.edit();
        spEditor.putString(mT, movieTitle);
        spEditor.commit();

//        btnList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MovieActivity.class);
//
//
//
//                startActivity(intent);
//            }
//        });
    }
}