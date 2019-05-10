package com.example.floatingview;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class FloatingViewService extends Service implements View.OnClickListener, View.OnTouchListener {

    private View floatingWidget;
    private WindowManager windowManager;
    private View collapsedView;
    private View expandedView;
    private View rootView;
    private WindowManager.LayoutParams parameters;

    int startXPos ;
    int startYPos ;
    float startTouchX ;
    float startTouchY ;

    @Override
    public void onCreate() {
        super.onCreate();

        floatingWidget = LayoutInflater.from(FloatingViewService.this).inflate(R.layout.float_view_layout,null);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        parameters = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        parameters.gravity = Gravity.TOP | Gravity.LEFT;
        parameters.x = 200;
        parameters.y = 200;

        windowManager.addView(floatingWidget,parameters);

        collapsedView = floatingWidget.findViewById(R.id.collapsed_view);
        ImageView collapsedCloseButton = (ImageView) floatingWidget.findViewById(R.id.collapsed_closed_button);
        collapsedCloseButton.setOnClickListener(this);

        expandedView = floatingWidget.findViewById(R.id.expanded_view);

        ImageView lionImage = floatingWidget.findViewById(R.id.lionImage);
        lionImage.setOnClickListener(this);

        ImageView previousButton = floatingWidget.findViewById(R.id.btnPrevious);
        previousButton.setOnClickListener(this);

        ImageView leopardImage = floatingWidget.findViewById(R.id.leopardImage);
        leopardImage.setOnClickListener(this);

        ImageView nextButton = floatingWidget.findViewById(R.id.btnNext);
        nextButton.setOnClickListener(this);

        ImageView expandedCloseButton = floatingWidget.findViewById(R.id.close_button_expanded);
        expandedCloseButton.setOnClickListener(this);

        ImageView openButton = floatingWidget.findViewById(R.id.open_button);
        openButton.setOnClickListener(this);

        rootView = floatingWidget.findViewById(R.id.root_view);
        rootView.setOnTouchListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.collapsed_closed_button:
                stopSelf();
                break;
            case R.id.lionImage:
                Toast.makeText(FloatingViewService.this,"Lion is tapped!!",Toast.LENGTH_SHORT).show();

                break;

            case R.id.btnPrevious:
                Toast.makeText(FloatingViewService.this,"previous button is tapped!!",Toast.LENGTH_SHORT).show();

                break;

            case R.id.leopardImage:
                Toast.makeText(FloatingViewService.this,"leopard image is tapped!!",Toast.LENGTH_SHORT).show();

                break;

            case R.id.btnNext:
                Toast.makeText(FloatingViewService.this,"next button is tapped!!",Toast.LENGTH_SHORT).show();

                break;

            case R.id.close_button_expanded:
                Toast.makeText(FloatingViewService.this,"expanded close button is tapped!!",Toast.LENGTH_SHORT).show();
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
                break;

            case R.id.open_button:
                Toast.makeText(FloatingViewService.this,"open button is tapped!!",Toast.LENGTH_SHORT).show();
                Intent openAppIntent = new Intent(FloatingViewService.this,MainActivity.class);
                openAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(openAppIntent);
                stopSelf();
                break;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:

                //access the start position of widget

                startXPos = parameters.x;
                startYPos = parameters.y;

                //access the start touching position of widget
                startTouchX = event.getRawX();
                startTouchY = event.getRawY();

               return true;

            case MotionEvent.ACTION_UP:

                int startToEndXDifference = (int)(event.getRawX() - startTouchX);
                int startToEndYDifference = (int)(event.getRawY() - startTouchY);

                if(startToEndXDifference < 5 && startToEndYDifference < 5){

                    if(isWidgetCollapsed()){

                        collapsedView.setVisibility(View.GONE);
                        expandedView.setVisibility(View.VISIBLE);
                    }
                }
                return true;

            case MotionEvent.ACTION_MOVE:

                parameters.x = startXPos + (int)(event.getRawX() - startTouchX);
                parameters.y = startYPos + (int)(event.getRawY() - startTouchY);
                windowManager.updateViewLayout(floatingWidget,parameters);
                return true;
        }
        return false;
    }

    private boolean isWidgetCollapsed() {
        return collapsedView.getVisibility() == View.VISIBLE;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(floatingWidget != null){

            windowManager.removeView(floatingWidget);
        }
    }
}
