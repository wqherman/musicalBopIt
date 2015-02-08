package html.eduwhermanhome.stanford.httpsccrma.musicalbopit;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.View.OnTouchListener;

import java.util.Calendar;
import java.util.Random;

public class MainActivity extends ActionBarActivity implements SensorEventListener{

    //classes to control the sensor manager and accelerometer
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    //should the game start
    private boolean startGame = false;
    //random number generator
    private Random r = new Random();
    //variables for timing
    int startTime;
    int endTime;
    int currTime;
    boolean threadStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set up our sensor manager and accelerometer
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //register a listener for out accelerometer
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);

        //Get elements from the UI that we'll need to change
        final Button startbutt = (Button) this.findViewById(R.id.startbutton);
        final TextView commands = (TextView) this.findViewById(R.id.commandtext);
        final Button resample = (Button) this.findViewById(R.id.button);

        //this class implements a thread that will generate commands over a specified time without locking up our interface
        //thread
        class commandGenerator extends Thread{
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                long endTime = startTime + 15000;
                long waitTime = 2000;
                int numCommands = 0;
                while(System.currentTimeMillis() < endTime) {
                    //set text of command
                    final int newCommand = r.nextInt(3);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (newCommand == 0) {
                                commands.setText("TAP IT!");
                            } else if (newCommand == 1) {
                                commands.setText("SHAKE IT!");
                            } else if (newCommand == 2) {
                                commands.setText("BLOW IT!");
                            }
                        }
                    });
                    numCommands += 1;
                    while(System.currentTimeMillis() < startTime + waitTime*numCommands){}
                }
                startGame = false;
            }
        };

        startbutt.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(startGame == false){
                        commandGenerator commandThread = new commandGenerator();
                        commandThread.start();
                        startGame = true;
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                }
                return false;
            }
        });
    }


    //override methods from sensor event listener
    //what happens when we get new sensor data
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
        }
    }
    //what happens when precision of sensor is changed
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }

    //we want to unregister or sensor listener when the app is paused
    @Override
    protected void onPause(){
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    //when app is resumed, re-register our listener
    @Override
    protected void onResume(){
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, senSensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
