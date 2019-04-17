package com.example.photoapp;

import android.Manifest;
import android.annotation.SuppressLint;
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
import com.example.photoapp.Helper.BitmapHelper;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public static final int CAMERA_REQUEST_CODE = 322;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 1337;
    public static final int IMAGE_GALLERY_REQUEST = 11;
    private ImageView imgPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get a reference to the image view
        imgPicture = findViewById(R.id.imgPicture);
    }


    public void btnTakePhotoClicked(View view) {
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            invokeCamera();
        } else {
            // let's request permission
            String[] permissionRequest = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissionRequest, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    public void btnSave(View view) {
        Bitmap bitmap = ((BitmapDrawable)imgPicture.getDrawable()).getBitmap();
        saveTempBitmap(bitmap);
        Toast.makeText(this, "Image saved", Toast.LENGTH_LONG).show();
    }


    public void btnSend(View view) {
        Bitmap bitmap = ((BitmapDrawable)imgPicture.getDrawable()).getBitmap();
        BitmapHelper.getInstance().setBitmap(bitmap);

        if (BitmapHelper.getInstance().getBitmap() == null) {
            Toast.makeText(this, "Bitmap is null", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, postGet.class);
            startActivity(intent);
        }
    }

    public void showProcessed(View view) {
        imgPicture.setImageBitmap(BitmapHelper.getInstance().getBitmap());
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
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timeStamp = sdf.format(new Date());

        // put together the directory and the timestamp
        return new File(picturesDirectory, "picture" + timeStamp + ".jpg");
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

        // invoke this activity and get back something from it
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

                if (imageUri != null){
                    try {
                        inputStream = getContentResolver().openInputStream(imageUri);

                        // get a bitmap from the stream
                        Bitmap image = BitmapFactory.decodeStream(inputStream);

                        // Calculate size of bitmap
                        int width;
                        int height;
                        if (image.getWidth() > image.getHeight()) {
                            width = 1920;
                            height = 1444;
                        } else {
                            width = 1444;
                            height = 1920;
                        }

                        // Displaying bitmap in ImageView
                        Picasso.get()
                                .load(imageUri)
                                .resize(width, height)  //Todo: take size from bitmap and scale with ratio
                                //.centerInside()
                                .into(imgPicture);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        // if image is unavailable
                        Toast.makeText(this, "Unable to open the image", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(this, "Failed to load data", Toast.LENGTH_LONG).show();
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
        return Bitmap.createBitmap(bInput, 0, 0, bInput.getWidth(), bInput.getHeight(), matrix, true);
    }

    public void saveTempBitmap(Bitmap bitmap) {
        if (isExternalStorageWritable()) {
            saveImage(bitmap);
        }else{
            Toast.makeText(this, "External storage is unavailable", Toast.LENGTH_LONG).show();
        }
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void saveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();

        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "IMG_"+ timeStamp +".jpg";

        File file = new File(myDir, fileName);
        if (file.exists()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

}

















// GET THE FILE PATH FROM URI (WORKING ONLY IN OREO)
//                    File file = new File(imageUri.getPath());
//                    final String[] split = file.getPath().split(":");
//                    String filePath = split[1];
//
//                    File file = new File(filePath);



// OnRequestPermissionResult
//        if (requestCode == SAVE_TO_STORAGE_REQUEST_CODE) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//
//                //saveToInternalStorage(largeBitmap);
//
//            }
//        }



//public static final int SAVE_TO_STORAGE_REQUEST_CODE = 2133;
//private Bitmap bitmap1;
//private Bitmap compressedBitmap;
//private Bitmap largeBitmap;
//largeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.to_be_aligned);



//    public void btnPicasso(View view) {
//
//        String root = Environment.getExternalStorageDirectory().toString();
//        File file = new File(root + "/saved_images", "to_resize.jpg");
//        Picasso.get()
//                .load(file)
//                .resize(1444, 1920)
//                //.centerInside()
//                .into(imgPicture);
//    }


//    private void reducingImageSize(String path) {
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeResource(getResources(), R.drawable.to_be_aligned, options);
//        int imageHeight = options.outHeight;
//        int imageWidth = options.outWidth;
//        String imageType = options.outMimeType;
//
//        imgPicture.setImageBitmap(
//                decodeSampledBitmapFromResource(getResources(), R.drawable.to_be_aligned, 350, 500));
//    }
//
//
//    public static byte[] getBytesFromBitmap(Bitmap bitmap, int quality) {
//
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
//        return stream.toByteArray();
//
//    }
//


//    public Uri getImageUri(Context inContext, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
//        return Uri.parse(path);
//    }


//    public void btnCompress(View view) {
//        Uri path = Uri.parse("android.resource://com.example.photoapp/" + R.drawable.to_be_aligned);
//        try{
//            Bitmap imageBitmap = SiliCompressor.with(this).getCompressBitmap("android.resource://com.example.photoapp/" + R.drawable.to_be_aligned);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


//    Bitmap bInput/*your input bitmap*/, bOutput;
//    float degrees = 45;//rotation degree
//    Matrix matrix = new Matrix();
//matrix.setRotate(degrees);
//    bOutput = Bitmap.createBitmap(bInput, 0, 0, bInput.getWidth(), bInput.getHeight(), matrix, true);


//    public void btnShowIm(View view) {
//
//        // bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.to_be_aligned);
//
//
//        // byte[] bytes = null;
//        //  bytes = getBytesFromBitmap(bitmap1, 50);
//
//        // compressedBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//
//
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeResource(getResources(), R.drawable.to_be_aligned, options);
//        int imageHeight = options.outHeight;
//        int imageWidth = options.outWidth;
//        String imageType = options.outMimeType;
//
//        imgPicture.setImageBitmap(
//                decodeSampledBitmapFromResource(getResources(), R.drawable.to_be_aligned, 350, 500));
//
//    }


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
//                    // show a message to the user indicating that the image is unavailable.
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


//    public String getURLForResource (int resourceId) {
//        return Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" +resourceId).toString();
//    }



//    String imageUrl = getURLForResource(R.drawable.to_be_aligned);
//
//    File imageFile = new File(imageUrl);
//    FileInputStream fis = null;
//
//        try {
//                fis = new FileInputStream(imageFile);
//                } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                }
//


//                bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.to_be_aligned);
//
//                compressedBitmap = new Compressor(this).compressToBitmap(imageFile);



//    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
//                                                         int reqWidth, int reqHeight) {
//
//        // First decode with inJustDecodeBounds=true to check dimensions
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeResource(res, resId, options);
//
//        // Calculate inSampleSize
//        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//
//        // Decode bitmap with inSampleSize set
//        options.inJustDecodeBounds = false;
//        return BitmapFactory.decodeResource(res, resId, options);
//    }


//    public static int calculateInSampleSize(
//            BitmapFactory.Options options, int reqWidth, int reqHeight) {
//        // Raw height and width of image
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//        int inSampleSize = 1;
//
//        if (height > reqHeight || width > reqWidth) {
//
//            final int halfHeight = height / 2;
//            final int halfWidth = width / 2;
//
//            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
//            // height and width larger than the requested height and width.
//            while ((halfHeight / inSampleSize) >= reqHeight
//                    && (halfWidth / inSampleSize) >= reqWidth) {
//                inSampleSize *= 2;
//            }
//        }
//        return inSampleSize;
//    }