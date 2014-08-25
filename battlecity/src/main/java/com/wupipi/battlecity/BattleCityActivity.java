package com.wupipi.battlecity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.wupipi.battlecity.model.Scene;
import com.wupipi.battlecity.model.SceneManager;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class BattleCityActivity extends Activity {

    private BattleCityView battleCityView;

    private JoystickView joystickView;

    private View fireContainer;

    private SceneManager sceneManager;

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
        battleCityView = (BattleCityView) findViewById(R.id.map);

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
        battleCityView.setSceneManager(sceneManager);

        sceneManager.setScoreListener(new ScoreListener());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onPause() {
        battleCityView.stopGame();

        try {
            final ObjectOutputStream outputStream = new ObjectOutputStream(openFileOutput("state", MODE_PRIVATE));
            outputStream.writeObject(sceneManager.getScene());
            outputStream .close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        super.onPause();
    }

    @Override
    protected void onResume() {
        try {
            final ObjectInputStream objectInputStream = new ObjectInputStream(openFileInput("state"));
            Scene scene = (Scene) objectInputStream.readObject();
            scene.setSceneManager(sceneManager);
            sceneManager.setScene(scene);
            objectInputStream.close();
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

            BattleCityActivity.this.handler.post(new Runnable() {
                @Override
                public void run() {
                    highestScoreView.setText("Highest: " + maxScore);
                    currentScoreView.setText("current: " + currentScore);
                }
            });

        }
    }
}
