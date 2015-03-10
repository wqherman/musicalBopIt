package html.eduwhermanhome.stanford.httpsccrma.musicalbopit;

import android.app.DownloadManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.grame.dsp_faust.dsp_faust;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import html.eduwhermanhome.stanford.httpsccrma.musicalbopit.R;

public class PlaybackPost extends ActionBarActivity {

    public static String user;
    public static String trackTitle;
    public float[] theRecording = new float[20*44100]; //our audio to save...hard coding = bad
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback_post);

        //find our ui elements so we can implement interaction
        final Button playbackButt = (Button) this.findViewById(R.id.playback);
        final Button postNew = (Button) this.findViewById(R.id.post);
        final Button addToDownload = (Button) this.findViewById(R.id.addto);
        final EditText userName = (EditText) this.findViewById(R.id.username);
        final EditText trackName = (EditText) this.findViewById(R.id.trackname);

        //pull our audio recording from faust
        //TODO: figure out a way to pass back the recording length
        for(int i = 0; i < 20*44100; i++) {
            theRecording[i] = dsp_faust.getRecordingBuffer(i);
        }
        playbackButt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(MainActivity.startGame == false){
                        dsp_faust.setParam("/bopIt/startPlayback", 1f);
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    dsp_faust.setParam("/bopIt/startPlayback", 0f);
                }
                return false;
            }
        });

        postNew.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //get the user and track names as strings
                    user = userName.getText().toString();
                    trackTitle = trackName.getText().toString();
                    //convert our recording float array to bytes and store in text file
                    ByteBuffer trackBuf = ByteBuffer.allocate(20*44100*4);
                    trackBuf.clear();
                    trackBuf.asFloatBuffer().put(theRecording);
                    String filename = trackTitle + ".txt";
                    final File file = new File(getApplicationContext().getFilesDir(), filename);
                    try {
                        FileOutputStream stream = new FileOutputStream(file);
                        OutputStream out = new BufferedOutputStream(stream);
                        out.write(trackBuf.array());
                        out.close();
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //upload three fields as new post
                    RequestParams params = new RequestParams();
                    try {
                        params.put("name", user);
                        params.put("description", trackTitle);
                        params.put("lat", 123);
                        params.put("long", 456);
                        params.put("udid", "bopTracks");
                        params.put("soundfile", file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.post("http://ding.stanford.edu:8081/soundshare/sound", params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            userName.setText("successful post!");
                            if(file.exists()){
                                file.delete();      //for now we'll delete the file once we post just so we can save some space
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            userName.setText("failure!");
                        }
                    });
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //probably do nothing
                }
                return false;
            }
        });

        addToDownload.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //add our recording and the downloaded track together
                    for(int i = 0; i < ViewServerActivity.downloadTrackLength; i++) {
                        theRecording[i] += ViewServerActivity.downloadTrack[i];
                    }
                    //convert our recording float array to bytes and store in text file
                    ByteBuffer trackBuf = ByteBuffer.allocate(20*44100*4);
                    trackBuf.clear();
                    trackBuf.asFloatBuffer().put(theRecording);
                    String filename = trackTitle + ".txt";
                    final File file = new File(getApplicationContext().getFilesDir(), filename);
                    try {
                        FileOutputStream stream = new FileOutputStream(file);
                        OutputStream out = new BufferedOutputStream(stream);
                        out.write(trackBuf.array());
                        out.close();
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //upload our combined recording as a new post, must delete the old post that
                    //the downloaded recording comes from
                    RequestParams params = new RequestParams();
                    try {
                        params.put("name", ViewServerActivity.downloadName);        //keep same name and track title as the downloaded track
                        params.put("description", ViewServerActivity.downloadTitle);
                        params.put("lat", 123);
                        params.put("long", 456);
                        params.put("udid", "bopTracks");
                        params.put("likes", ViewServerActivity.downloadLikes);      //transfer the downloaded likes to here
                        params.put("soundfile", file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.post("http://ding.stanford.edu:8081/soundshare/sound", params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            userName.setText("successful post!");
                            if(file.exists()){
                                file.delete();      //for now we'll delete the file once we post just so we can save some space
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            userName.setText("failure!");
                        }
                    });
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    //probably do nothing
                }
                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_playback_post, menu);
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
