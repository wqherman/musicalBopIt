package html.eduwhermanhome.stanford.httpsccrma.musicalbopit;

import android.content.Context;
import android.graphics.Color;
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
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import java.util.Calendar;
import java.util.Random;

//our faust library
import com.grame.dsp_faust.dsp_faust;

public class MainActivity extends ActionBarActivity implements SensorEventListener{

    //classes to control the sensor manager and accelerometer
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    //should the game start
    private boolean startGame = false;
    //random number generator
    private Random r = new Random();

    //booleans used to tell if an action has happened
    boolean screenTap = false;          //for the tap button on screen
    boolean shaken = false;             //for when the phone is shaken
    boolean clapTrigger = false;        //for when you must clap
    boolean yellTrigger = false;        //for when you have to make constant noise for the duration of the command
    boolean ampAbove = false;           //has the amplitude stayed above a set value for the yell command

    //keeps the last time the accelerometer data was checked
    long lastUpdate;

    //holds last accelerometer data
    float lastx, lasty, lastz;
    float amplitude;                    //holds our amplitude tracker values from faust

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set up our sensor manager and accelerometer
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //register a listener for out accelerometer
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);

        // Audio engine is started and audio process launched
        int samplingRate = 44100;
        int bufferLength = 512;
        dsp_faust.init(samplingRate,bufferLength);
        dsp_faust.start();

        //Get elements from the UI that we'll need to change
        final Button startbutt = (Button) this.findViewById(R.id.startbutton);
        final TextView commands = (TextView) this.findViewById(R.id.commandtext);
        final Button resample = (Button) this.findViewById(R.id.button);
        final Button playbackButt = (Button) this.findViewById(R.id.playback);

        //this class implements a thread that will generate commands over a specified time without locking up our interface
        //thread
        class commandGenerator extends Thread{
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                long waitTime = 1304;
                long endTime = startTime + 15000;
                long startWait;

                while(System.currentTimeMillis() < endTime) {
                    //set text of command, must do this on ui thread since its the only one with
                    //access to the ui elements
                    final int newCommand = r.nextInt(4);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (newCommand == 0) {
                                commands.setText("TAP IT!");
                            } else if (newCommand == 1) {
                                commands.setText("SHAKE IT!");
                            } else if (newCommand == 2) {
                                commands.setText("CLAP IT!");
                            } else if (newCommand == 3) {
                                commands.setText("YELL IT!");
                            }
                        }
                    });
                    //wait for 2 seconds for the user to execute the command otherwise shut down the
                    //thread
                    startWait = System.currentTimeMillis();
                    while(System.currentTimeMillis()-startWait < waitTime){
                        if(newCommand == 0 && screenTap){
                            screenTap = false;
                            dsp_faust.setParam("/bopIt/hornTime",1f);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    commands.setTextColor(Color.GREEN);
                                }
                            });
                            break;
                        } else if(newCommand == 0 && shaken){
                            shaken = false;
                        } else if(newCommand == 0 && clapTrigger){
                            clapTrigger = false;
                        } else if(newCommand == 0 && yellTrigger){
                            yellTrigger = false;
                        } else if(newCommand == 1 && shaken){
                            shaken = false;
                            dsp_faust.setParam("/bopIt/scratchTime",1f);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    commands.setTextColor(Color.GREEN);
                                }
                            });
                            break;
                        } else if(newCommand == 1 && screenTap){
                            screenTap = false;
                        } else if(newCommand == 1 && clapTrigger){
                            clapTrigger = false;
                        } else if(newCommand == 1 && yellTrigger){
                            yellTrigger = false;
                        } else if(newCommand == 2 && screenTap){
                            screenTap = false;
                        } else if(newCommand == 2 && shaken){
                            shaken = false;
                        } else if(newCommand == 2 && clapTrigger){
                            clapTrigger = false;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    commands.setTextColor(Color.GREEN);
                                }
                            });
                            break;
                        } else if(newCommand == 2 && yellTrigger){
                            yellTrigger = false;
                        } else if(newCommand == 3 && screenTap){
                            screenTap = false;
                            break;
                        } else if(newCommand == 3 && shaken){
                            shaken = false;
                        } else if(newCommand == 3 && clapTrigger){
                            clapTrigger = false;
                        } else if(newCommand == 3 && yellTrigger){
                            if(amplitude < 0.6)                     //since for yell you need to make noise for the entire wait time
                            {                                       //so once the command has started we check to see if the amplitude dips
                                yellTrigger = false;                //below a certain value
                                ampAbove = false;
                                break;
                            } else {
                                ampAbove = true;
                            }
                        }
                    }

                    if(ampAbove){   //if we had to yell and we did, we're good
                        ampAbove = false;
                    } else if(System.currentTimeMillis() - startWait >= waitTime){   //otherwise if we went longer than we had, we failed
                        startGame = false;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                commands.setText("Loser!");
                            }
                        });
                        dsp_faust.setParam("/bopIt/startGame", 0f);
                        return;
                    } else {        //otherwise finish waiting for the rest of the wait time to be done

                    }

                    //pause for a fraction of a second to give things like the amplitude tracker and
                    //accelerometer time to settle
                    long pauseStart = System.currentTimeMillis();
                    long pauseTime = 250;
                    while(System.currentTimeMillis() - pauseStart < pauseTime){}
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            commands.setTextColor(Color.WHITE);
                        }
                    });
                }
                //game has finished!
                //set all triggers to false just in case and display done on the screen
                screenTap = false;
                shaken = false;
                clapTrigger = false;
                yellTrigger = false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        commands.setText("You Win!");
                    }
                });
                startGame = false;
                dsp_faust.setParam("/bopIt/startGame", 0f);
            }
        };

        //the start button beings running a thread that will generate new commands without locking
        //up the interface
        startbutt.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(startGame == false){
                        commandGenerator commandThread = new commandGenerator();
                        commandThread.start();
                        startGame = true;
                        dsp_faust.setParam("/bopIt/startGame", 1f);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                }
                return false;
            }
        });

        playbackButt.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(startGame == false){
                        dsp_faust.setParam("/bopIt/startPlayback", 1f);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        dsp_faust.setParam("/bopIt/startPlayback", 0f);
                }
                return false;
            }
        });

        resample.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    //set a variable to tell our command thread that we pressed the button
                    screenTap = true;
                    //send faust an indication that we need the effect associated with this to happen
                } else if(event.getAction() == MotionEvent.ACTION_UP){
                    //probably do nothing, or tell faust to start the decay envelope of our effect
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
        int shakenThreshold = 500;
        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long currentTime = System.currentTimeMillis();
            if (currentTime - lastUpdate > 100) {
                long diffTime = currentTime - lastUpdate;
                lastUpdate = currentTime;
                //calculate total speed of device as total change in acceleration
                float speed = Math.abs(x + y + z - lastx - lasty - lastz)/diffTime * 10000;

                if(speed > shakenThreshold){
                    shaken = true;
                }

                //set previous values
                lastx = x;
                lasty = y;
                lastz = z;
            }

            //accelerometer is changed most frequently so we'll poll our faust envelope tracker here
            amplitude = dsp_faust.getParam("/bopIt/amp");
            if(amplitude > 0.9){
                clapTrigger = true;
                yellTrigger = true;
            }
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
    protected void onDestroy(){
        super.onDestroy();
        dsp_faust.stop();
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
