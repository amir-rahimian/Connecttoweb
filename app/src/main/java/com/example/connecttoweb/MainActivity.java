package com.example.connecttoweb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txt ;
    private EditText edtUrl , edtimageurl , edtKey1,edtKey2,edtVal1,edtVal2;
    private Button btnGet;
    private ImageView khaby ;

    private boolean close = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt = findViewById(R.id.txt);
        khaby = findViewById(R.id.KhabyImg);
        edtUrl = findViewById(R.id.edtUrl);
        edtKey1 = findViewById(R.id.edtK1);
        edtKey2 = findViewById(R.id.edtK2);
        edtVal1 = findViewById(R.id.edtV1);
        edtVal2 = findViewById(R.id.edtV2);
        edtimageurl = findViewById(R.id.edtUrlImg);
        btnGet = findViewById(R.id.btnGet);
        btnGet.setOnClickListener(this);


        edtKey1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtVal1.setHint(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtKey2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtVal2.setHint(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    public void onClick(View v) {

        hidekeyboard(MainActivity.this);

       String url = Request.getFullUrlFrom(edtUrl.getText().toString(),new Request(edtKey1.getText().toString(),edtVal1.getText().toString())
               ,new Request(edtKey2.getText().toString(),edtVal2.getText().toString()));

        new GetImages(edtimageurl.getText().toString()).execute(khaby);

        new RequestTask().execute(url);


    }

    private void hidekeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtUrl.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        hidekeyboard(MainActivity.this);
        if (close) {
            super.onBackPressed();
        }else {
            close = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    close=false;
                }
            },500);
        }
    }


    class RequestTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    responseString = out.toString();
                    out.close();
                } else {
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Do anything with response..
            txt.setText(result);

            khaby.animate().alpha(1).translationY(0).setDuration(200);
            txt.animate().translationY(0).setDuration(500);
        }
    }

    private class GetImages extends AsyncTask<Object, Object, Object> {
        private String requestUrl;
        private ImageView view;
        private GetImages(String requestUrl) {
            this.requestUrl = requestUrl;
        }

        @Override
        protected Object doInBackground(Object... objects) {
            view = (ImageView ) objects[0];
            try {
                URL url = new URL(requestUrl);
                URLConnection conn = url.openConnection();
                return BitmapFactory.decodeStream(conn.getInputStream());
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(Object o) {
            if (o == null) {
                Toast.makeText(MainActivity.this,"something went wrong!",Toast.LENGTH_SHORT).show();
            } else {
                view.setImageBitmap((Bitmap) o);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(MainActivity.this,ReverseAvititty.class));
        return super.onOptionsItemSelected(item);
    }
}