package com.wupipi.battlecity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class JoystickView extends View {

    private Paint mainCircle;

    private Paint button;

    private Paint horizontalLine;

    private Paint verticalLine;

    private int xPosition = 0; // Touch x position
    private int yPosition = 0; // Touch y position

    private int buttonRadius;

    private int joystickRadius;

    private double centerX = 0; // Center view x position
    private double centerY = 0; // Center view y position

    private OnJoystickMoveListener onJoystickMoveListener; // Listener

    public JoystickView(Context context) {
        super(context);
        initJoystickView();
    }

    public JoystickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initJoystickView();

    }

    public JoystickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initJoystickView();

    }

    protected void initJoystickView() {
        mainCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mainCircle.setColor(Color.WHITE);
        mainCircle.setStyle(Paint.Style.FILL_AND_STROKE);

        verticalLine = new Paint();
        verticalLine.setStrokeWidth(5);
        verticalLine.setColor(Color.RED);

        horizontalLine = new Paint();
        horizontalLine.setStrokeWidth(2);
        horizontalLine.setColor(Color.BLACK);

        button = new Paint(Paint.ANTI_ALIAS_FLAG);
        button.setColor(Color.RED);
        button.setStyle(Paint.Style.FILL);

        centerX = (getWidth()) / 2;
        centerY = (getHeight()) / 2;

        xPosition = (int) centerX;
        yPosition = (int) centerY;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // setting the measured values to resize the view to a certain width and
        // height
        int d = Math.min(measure(widthMeasureSpec), measure(heightMeasureSpec));

        setMeasuredDimension(d, d);

        // before measure, get the center of view
        xPosition = (int) getWidth() / 2;
        yPosition = (int) getWidth() / 2;

        buttonRadius = (int) (d / 2 * 0.25);
        joystickRadius = (int) (d / 2 * 0.75);
    }

    private int measure(int measureSpec) {
        int result = 0;

        // Decode the measurement specifications.
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.UNSPECIFIED) {
            // Return a default size of 200 if no bounds are specified.
            result = 200;
        } else {
            // As you want to fill the available space
            // always return the full available bounds.
            result = specSize;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);
        centerX = (getWidth()) / 2;
        centerY = (getHeight()) / 2;

        // painting the main circle
        canvas.drawCircle((int) centerX, (int) centerY, joystickRadius,
                mainCircle);
        // paint lines
        canvas.drawLine((float) centerX, (float) centerY, (float) centerX,
                (float) (centerY - joystickRadius), verticalLine);
        canvas.drawLine((float) (centerX - joystickRadius), (float) centerY,
                (float) (centerX + joystickRadius), (float) centerY,
                horizontalLine);
        canvas.drawLine((float) centerX, (float) (centerY + joystickRadius),
                (float) centerX, (float) centerY, horizontalLine);

        // painting the move button
        canvas.drawCircle(xPosition, yPosition, buttonRadius, button);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        xPosition = (int) event.getX();
        yPosition = (int) event.getY();
        double abs = Math.sqrt((xPosition - centerX) * (xPosition - centerX)
                + (yPosition - centerY) * (yPosition - centerY));
        if (abs > joystickRadius) {
            xPosition = (int) ((xPosition - centerX) * joystickRadius / abs + centerX);
            yPosition = (int) ((yPosition - centerY) * joystickRadius / abs + centerY);
        }
        invalidate();
        if (event.getAction() == MotionEvent.ACTION_UP) {
            xPosition = (int) centerX;
            yPosition = (int) centerY;
            onJoystickMoveListener.onValueChanged(getDirection(this, event));

            return true;
        }
        if (onJoystickMoveListener != null
                && (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE)) {
            onJoystickMoveListener.onValueChanged(getDirection(this, event));

            return true;
        }
        return false;
    }

    public static interface OnJoystickMoveListener {
        public void onValueChanged(Direction direction);
    }

    public void setOnJoystickMoveListener(OnJoystickMoveListener onJoystickMoveListener) {
        this.onJoystickMoveListener = onJoystickMoveListener;
    }

    private Direction getDirection(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {

            // Normalize x,y between 0 and 1
            float x = event.getX() / v.getWidth();
            float y = event.getY() / v.getHeight();

            // Direction will be [0,1,2,3] depending on quadrant
            int direction = 0;
            direction = (x > y) ? 1 : 0;
            direction |= (x > 1 - y) ? 2 : 0;

            switch (direction) {
                case 1:
                    return Direction.NORTH;
                case 2:
                    return Direction.SOUTH;
                case 0:
                    return Direction.WEST;
                case 3:
                    return Direction.EAST;
            }
        }
        return null;
    }
}