package html.eduwhermanhome.stanford.httpsccrma.musicalbopit;

import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.grame.dsp_faust.dsp_faust;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;


import html.eduwhermanhome.stanford.httpsccrma.musicalbopit.R;


/*-----------View Server Activity--------------------------
    This class will handle all communication with the server.
    It will display a list of the newest and most popular recordings
    and let the users upvote recordings they like similar to reddit.
*/
public class ViewServerActivity extends ActionBarActivity {
    //declare static variables for our downloaded track info so we can add to it if we want
    //in the playback post activity
    static float[] downloadTrack;
    static String downloadName;
    static String downloadTitle;
    static int downloadLikes;
    static int downloadTrackLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_server);

        //number of entries to display for the view server activity. it's hard coded for now and for
        //demo purposes but it shouldn't be
        final int numRows = 5;

        //files to store our retreived recordings
        final File[] serverRecordings = new File[numRows];

        //Declare UI elements so we can populate the phone table with data
        //from the server
        final TextView[] rowNames = new TextView[numRows];
        final Button[] rowTitle = new Button[numRows];
        final TextView[] rowLikes = new TextView[numRows];

        //arrays to store all the data we need
        final String[] unames = new String[numRows];
        final String[] tracknames = new String[numRows];
        final Integer[] likes = new Integer[numRows];

        //initialize our row ui elements, is there a cleaner way to do this? probably
        rowNames[0] = (TextView) this.findViewById(R.id.row1user);
        rowTitle[0] = (Button) this.findViewById(R.id.row1title);
        rowLikes[0] = (TextView) this.findViewById(R.id.row1votes);
        rowNames[1] = (TextView) this.findViewById(R.id.row2user);
        rowTitle[1] = (Button) this.findViewById(R.id.row2title);
        rowLikes[1] = (TextView) this.findViewById(R.id.row2votes);
        rowNames[2] = (TextView) this.findViewById(R.id.row3user);
        rowTitle[2] = (Button) this.findViewById(R.id.row3title);
        rowLikes[2] = (TextView) this.findViewById(R.id.row3votes);
        rowNames[3] = (TextView) this.findViewById(R.id.row4user);
        rowTitle[3] = (Button) this.findViewById(R.id.row4title);
        rowLikes[3] = (TextView) this.findViewById(R.id.row4votes);
        rowNames[4] = (TextView) this.findViewById(R.id.row5user);
        rowTitle[4] = (Button) this.findViewById(R.id.row5title);
        rowLikes[4] = (TextView) this.findViewById(R.id.row5votes);

        //Send a get request to the server and fill our table with the info from the server
        //will this run continuously or just once?
        final AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://ding.stanford.edu:8081/soundshare/sounds", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                try {
                    String uname = response.getString("name");
                    String trackname = response.getString("description");
                    rowNames[0].setText(uname);
                    rowTitle[0].setText(trackname);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response){

                try {
                    for(int i = 0; i < numRows; i++){
                        if(i < response.length()) {
                            JSONObject entry = response.getJSONObject(i);
                            JSONObject fields = entry.getJSONObject("fields");
                            unames[i] = fields.getString("name");
                            tracknames[i] = fields.getString("description");
                            likes[i] = fields.getInt("likes");
                        }
                    }
                    //display our data in the textviews
                    for(int i = 0; i < numRows; i++) {
                        if(i < response.length()) {
                            rowNames[i].setText(unames[i]);
                            rowTitle[i].setText(tracknames[i]);
                            rowLikes[i].setText(likes[i].toString());
                        }
                        else{
                            rowNames[i].setText("Empty Post");
                            rowTitle[i].setText("Empty Post");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        //if a button for one of the rows is pressed, download the appropriate recording
        rowTitle[4].setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    client.cancelAllRequests(true);
                    String trackURL = "http://ding.stanford.edu:8081/soundshare/sounds/" + tracknames[4] + ".txt";
                    downloadName = unames[4];
                    downloadTitle = tracknames[4];
                    downloadLikes = likes[4];
                    client.get(trackURL, new FileAsyncHttpResponseHandler(getApplicationContext()) {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {

                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, File file) {
                            try {
                                //get our data form the file as a byte[] array
                                FileInputStream fin = new FileInputStream(file);
                                byte[] fileData = new byte[(int) file.length()];
                                fin.read(fileData);
                                //wrap our data in a byte buffer, convert to float buffer and then get the array
                                ByteBuffer downloadByteBuf = ByteBuffer.wrap(fileData);
                                FloatBuffer downloadFBuf = downloadByteBuf.asFloatBuffer();
                                downloadTrack = new float[downloadFBuf.limit()];
                                downloadTrackLength = downloadFBuf.limit();
                                downloadFBuf.get(downloadTrack);
                                //here we'll need to pass this float array to faust, and then have faust play, will probably have to make some faust hackery
                                for(int i = 0; i < downloadFBuf.limit(); i++)
                                {
                                    dsp_faust.setDownloadBuffer(downloadTrack[i], i);
                                }
                                dsp_faust.setParam("/bopIt/downloadStart", 1f);
                                fin.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                }
                return false;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_server, menu);
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
