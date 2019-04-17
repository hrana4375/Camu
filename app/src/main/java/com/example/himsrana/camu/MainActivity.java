package com.example.himsrana.camu;

import android.content.Intent;
import android.media.Image;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static  final String TAG = MainActivity.class.getSimpleName();

    public static final int REQUEST_TAKE_PHOTO = 0;
    public static final int REQUEST_PICK_PHOTO = 1;

    private Uri mMediaUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView takePhoto = findViewById(R.id.takePhoto);
        final ImageView pickPhoto = findViewById(R.id.pickPhoto);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mMediaUri = getOutputMediaFileUri();

                if(mMediaUri == null ) {
                    Toast.makeText(MainActivity.this,
                            "There is a problem in accesing media file",
                                Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                    startActivityForResult(takePhotoIntent, REQUEST_TAKE_PHOTO);
                }
            }
        });

        pickPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                pickPhotoIntent.setType("image/*");
                startActivityForResult(pickPhotoIntent, REQUEST_PICK_PHOTO);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if(requestCode == REQUEST_TAKE_PHOTO || requestCode == REQUEST_PICK_PHOTO) {

                Intent intent = new Intent(this,ViewPhotoActivity.class);

                if(data != null) {
                    mMediaUri = data.getData();
                }

                intent.setData(mMediaUri);
                startActivity(intent);
            }
        }
        else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Sorry, there was an error!", Toast.LENGTH_SHORT).show();
        }
    }

    public Uri getOutputMediaFileUri() {

        if(isExternalStorageAvailable()) {

            File mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            String fileName = "";
            String fileType = "";
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            fileName = "IMG_"+ timeStamp;
            fileType = ".jpg";

            File mediaFile;

            try {
                mediaFile = File.createTempFile(fileName, fileType, mediaStorageDir);
                Log.i(TAG, "File" + Uri.fromFile(mediaFile));

                return FileProvider.getUriForFile(
                        this,
                        this.getApplicationContext()
                                .getPackageName() + ".provider", mediaFile);

            }
            catch (IOException e) {
                Log.e(TAG, "Error creating file: " +
                        mediaStorageDir.getAbsolutePath() + fileName + fileType);
            }
        }

        return null;
    }

    public boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}

