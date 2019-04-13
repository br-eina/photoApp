package com.example.photoapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final int CAMERA_REQUEST_CODE = 322;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 1337;
    public static final int IMAGE_GALLERY_REQUEST = 11;
    Integer ff;
    private ImageView imgPicture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get a reference to the image view
        imgPicture = findViewById(R.id.imgPicture);

    }

    /**
     * This method will be called when the Take Photo button is clicked
     * @param view
     */
    public void btnTakePhotoClicked(View view) {
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            invokeCamera();
        } else {
            // let's request permission
            String[] permissionRequest = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissionRequest, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            // we have heard back from our request for camera and write external storage permission
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                invokeCamera();
            } else {
                Toast.makeText(this, getString(R.string.cannotopencamera), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void invokeCamera() {
        // get a file reference
        Uri pictureUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", createImageFile());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // tell the camera where to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
        // tell the camera to request right permission
        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);

    }

    private File createImageFile() {
        // the public picture directory
        File picturesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        // timestamp makes an unique name
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timeStamp = sdf.format(new Date());
        // put together the directory and the timestamp
        File imageFile = new File(picturesDirectory, "picture" + timeStamp + ".jpg");
        return imageFile;
    }





    public void btnOpenGalleryClicked(View view) {
        // invoke the image gallery using the implicit intent
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        // where do we want to find a data
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();

        // get a URI representation
        Uri data = Uri.parse(pictureDirectoryPath);

        // set the data and type. Get all image types
        photoPickerIntent.setDataAndType(data, "image/*");

        // invoke this activity and get back smth from it
        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                Toast.makeText(this, "Image Saved.", Toast.LENGTH_LONG).show();
            }

            if (requestCode == IMAGE_GALLERY_REQUEST) {

                // the address of the image on the SD card
                Uri imageUri = data.getData();

                // declare a stream to read the image data from the SD card
                InputStream inputStream;

                // get an input stream, based on URI of the image
                try {
                    inputStream = getContentResolver().openInputStream(imageUri);

                    // get a bitmap from the stream
                    Bitmap image = BitmapFactory.decodeStream(inputStream);

                    //rotateBitmap(image);

                    // show the image to the user

                    imgPicture.setImageBitmap(image);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    // if image is unavailable
                    Toast.makeText(this, "Unable to open the image", Toast.LENGTH_LONG).show();
                }

            }
        }





    }

    public void btnRotate(View view) {

        Bitmap bm=((BitmapDrawable)imgPicture.getDrawable()).getBitmap();

        imgPicture.setImageBitmap(rotateBitmap(bm));



    }

    private Bitmap rotateBitmap(Bitmap bInput) {


        float degrees = 90;
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees);
        Bitmap bOutput = Bitmap.createBitmap(bInput, 0, 0, bInput.getWidth(), bInput.getHeight(), matrix, true);

        return bOutput;
    }






//    Bitmap bInput/*your input bitmap*/, bOutput;
//    float degrees = 45;//rotation degree
//    Matrix matrix = new Matrix();
//matrix.setRotate(degrees);
//    bOutput = Bitmap.createBitmap(bInput, 0, 0, bInput.getWidth(), bInput.getHeight(), matrix, true);





}











//             if we are here, everything processed successfully.
//            if (requestCode == IMAGE_GALLERY_REQUEST) {
//                // if we are here, we are hearing back from the image gallery.
//
//                // the address of the image on the SD Card.
//                Uri imageUri = data.getData();
//
//                // declare a stream to read the image data from the SD Card.
//                InputStream inputStream;
//
//                // we are getting an input stream, based on the URI of the image.
//                try {
//                    inputStream = getContentResolver().openInputStream(imageUri);
//
//                    // get a bitmap from the stream.
//                    Bitmap image = BitmapFactory.decodeStream(inputStream);
//
//
//                    // show the image to the user
//                    imgPicture.setImageBitmap(image);
//
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                    // show a message to the user indictating that the image is unavailable.
//                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
//                }
//
//            }

//        public static final int IMAGE_GALLERY_REQUEST = 20;
//        private ImageView img;
//        img = findViewById(R.id.img);

//        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        String pictureName = getPictureName();
//        File imageFile = new File(pictureDirectory, pictureName);
//        Uri pictureUri = Uri.fromFile(imageFile);
//        // 1 arg - want to save, 2 arg - where to save
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
//        startActivityForResult(cameraIntent, CAMERA_REQUEST);

//        private String getPictureName() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
//        String timeStamp = sdf.format(new Date());
//        return "photo22" + timeStamp + ".jpg";
//
//    }
