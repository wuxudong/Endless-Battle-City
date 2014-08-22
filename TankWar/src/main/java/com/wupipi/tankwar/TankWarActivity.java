package com.wupipi.tankwar;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wupipi.tankwar.model.Obstacle;
import com.wupipi.tankwar.model.ObstacleAdaptor;
import com.wupipi.tankwar.model.Scene;
import com.wupipi.tankwar.model.SceneManager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class TankWarActivity extends Activity {

    private TankWarView tankWarView;

    private JoystickView joystickView;

    private View fireContainer;

    private SceneManager sceneManager;

    private Gson gson;
    private TextView highestScoreView;
    private TextView currentScoreView;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// full screen

        setContentView(R.layout.activity_main);
        tankWarView = (TankWarView) findViewById(R.id.map);

        joystickView = (JoystickView) findViewById(R.id.joystick);

        joystickView.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener() {
            @Override
            public void onValueChanged(Direction direction) {
                sceneManager.getScene().actPlayerMove(direction);
            }
        });

        handler = new Handler(getMainLooper());



        highestScoreView = (TextView) findViewById(R.id.highestScore);

        currentScoreView = (TextView) findViewById(R.id.currentScore);

        fireContainer = findViewById(R.id.fireContainer);
        fireContainer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {

                        sceneManager.getScene().actPlayerFire(true);

                        return true;
                    }

                    case MotionEvent.ACTION_UP: {
                        sceneManager.getScene().actPlayerFire(false);
                        return true;
                    }
                }
                return false;
            }


        });

        sceneManager = new SceneManager();
        tankWarView.setSceneManager(sceneManager);

        sceneManager.setScoreListener(new ScoreListener());


        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Obstacle.class, new ObstacleAdaptor());
        gson = gsonBuilder.create();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onPause() {
        tankWarView.stopGame();

        try {
            FileOutputStream out = openFileOutput("state", MODE_PRIVATE);
            String json = gson.toJson(sceneManager.getScene());
            Log.d("tank", "write json length " + json.length() +
                    ":" + json);
            OutputStreamWriter writer = new OutputStreamWriter(out);
            writer.write(json);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        super.onPause();
    }

    @Override
    protected void onResume() {
        FileInputStream in = null;
        try {
            in = openFileInput("state");
            String json = CharStreams.toString(new InputStreamReader(in));
            Log.d("tank", "saved json length " + json.length());
            Log.d("tank", "saved scene:" + json);
            Scene scene = gson.fromJson(json, Scene.class);
            scene.setSceneManager(sceneManager);
            sceneManager.setScene(scene);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            Scene scene = new Scene();
            scene.setSceneManager(sceneManager);
            sceneManager.setScene(scene);
        }

        super.onResume();
    }

    public class ScoreListener {
        public void onScoreChange(final int maxScore, final int currentScore) {

            TankWarActivity.this.handler.post(new Runnable() {
                @Override
                public void run() {
                    highestScoreView.setText("Highest: " + maxScore);
                    currentScoreView.setText("current: " + currentScore);
                }
            });

        }
    }
}
