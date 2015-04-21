package course.labs.dailyselfie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


public class PreviewImageActivity extends Activity {

    public static final String IMAGE_PATH = "imagePath";

    private ImageView mPreviewImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_image);

        ImageView previewImage = (ImageView) findViewById(R.id.previewImage);
        previewImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Create an intent stating which Activity you would like to start
                Intent intent = new Intent(PreviewImageActivity.this, DailySelfieActivity.class);

                // Use the Intent to start Google Maps application using Activity.startActivity()
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        mPreviewImage = (ImageView) findViewById(R.id.previewImage);

        setPic(mPreviewImage, getIntent().getStringExtra(IMAGE_PATH));
    }

    private void setPic(ImageView imageView, String fullPhotoPath) {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fullPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        //int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        // todo: imageView height/width is 0 here.  Where should this happen?
        int scaleFactor = 1;

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(fullPhotoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }



}