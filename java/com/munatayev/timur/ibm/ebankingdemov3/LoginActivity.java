package com.munatayev.timur.ibm.ebankingdemov3;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassifier;
import com.munatayev.timur.ibm.ebankingdemov3.Cloud.PostTask;
import com.munatayev.timur.ibm.ebankingdemov3.Cloud.SpeachService;
import com.munatayev.timur.ibm.ebankingdemov3.Particles.ParticleView;
import com.munatayev.timur.ibm.ebankingdemov3.Utile.APIProperties;
import com.munatayev.timur.ibm.ebankingdemov3.Utile.ConversationMessage;
import com.munatayev.timur.ibm.ebankingdemov3.ViewPack.FancyButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity implements ServiceConnection {

    private static final String TAG = "LoginActivity";

    private ParticleView mPv3;

    private EditText _emailText;
    private EditText _passwordText;
    private ImageButton faceRecognition;
    private LinearLayout _background;
    private FancyButton login_button;
    private boolean mIsBound = false;
    private SpeachService mServ;
    private File photoRec;
    private static final int CAMERA_REQUEST = 1888;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkPermission();

        _emailText = (EditText) findViewById(R.id.input_email);
        _passwordText = (EditText) findViewById(R.id.input_password);
        login_button = (FancyButton) findViewById(R.id.btn3);
        _background = (LinearLayout)findViewById(R.id.background);
        faceRecognition = (ImageButton)findViewById(R.id.ibFaceRec);
        faceRecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        //load Properties
        try {
            new APIProperties(this);
        } catch (IOException e) {
            Log.d(TAG,"Properties not loaded");
        }

        //IBM logo
        mPv3 = (ParticleView) findViewById(R.id.pv_3);

        doBindService();

        _background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideNavBar();
            }
        });
        hideNavBar();
        mPv3.startAnim();

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideNavBar();
                if (view instanceof  FancyButton)
                {
                    //starting background service
                    Intent ser = new Intent(LoginActivity.this, SpeachService.class);//
                    startService(ser);

                    if (((FancyButton)view).isExpanded())
                        ((FancyButton)view).collapse();
                    login_button.setEnabled(false);
                }
                login(view);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap photo = (Bitmap) data.getExtras().get("data");
        photoRec = persistImage(photo);
        _passwordText.setText("face-recognition");
    }

    private File persistImage(Bitmap bitmap) {
        File imageFile = null;
        try {
            imageFile = createImageFile();
            FileOutputStream os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return imageFile;
    }

    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName,".jpg",storageDir);
    }

    public void login(View view) {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            login_button.setText("Try again");
            login_button.expand();
            login_button.setEnabled(true);
        } else {

            new android.os.Handler().postDelayed(new Runnable() {
                public void run() {
                    onLoginSuccess();
                }
            }, 3000);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        mServ.start();

        startActivity(new Intent(getBaseContext(), MainActivity.class));
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
    }

    public boolean validate() {

        boolean valid = false;
        ConversationMessage login = null;
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        try {
            if(!password.equals("face-recognition")) {
                login = new PostTask().execute(new ConversationMessage(email + " " + password, null, "login with password")).get();
            } else {
                login = new PostTask().execute(new ConversationMessage(email, null, "login with face recognition")).get();
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        if(login != null) {
            if (login.getCommand().equals("accept with password")) {
                valid = true;

            } else if (login.getCommand().equals("accept with face recognition")){
                try {
                    String result = new photoRecognition().execute(photoRec).get();
                    Log.d("NAMESURNAME",login.getMessage());
                    if(login.getMessage().equals(result)){
                        valid = true;
                    } else {
                        valid = false;
                    }

                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            } else{
                valid = false;
            }
        }

        return valid;
    }

    private void hideNavBar() {
        if (Build.VERSION.SDK_INT >= 19) {
            View v = getWindow().getDecorView();
            v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        mServ = ((SpeachService.ServiceBinder) binder).getService();//
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mServ = null;
    }

    public void doBindService(){
        Intent intent = new Intent(this, SpeachService.class);//
        bindService(intent, this, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    public void doUnbindService(){
        if(mIsBound)
        {
            unbindService(this);
            mIsBound = false;
        }
    }

    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        doUnbindService();
    }

    class photoRecognition extends AsyncTask<File, Integer, String> {

        @Override
        protected String doInBackground(File... files) {
            
            String class_ = null;
            double score = 0;

            VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
            service.setApiKey("5682aaba60a2431770765c45e062ba1586ae1b45");

            ClassifyImagesOptions options = new ClassifyImagesOptions.Builder()
                    .classifierIds("users_1578821698")
                    .threshold(0.1)
                    .images(files[0])
                    .build();
            VisualClassification result = service.classify(options).execute();
            for(VisualClassifier.VisualClass clas : result.getImages().get(0).getClassifiers().get(0).getClasses()){
                if(clas.getScore() >= score){
                    class_ = clas.getName();
                }
            }
            Log.d("RESULT NAME", class_);
            return class_;
        }
    }
}
