package course.labs.dailyselfie;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DailySelfieActivity extends ListActivity {

    static final int REQUEST_TAKE_PHOTO = 1;

    // properties for the alarm notification
    private static final long INITIAL_ALARM_DELAY = 2 * 60 * 1000L;

    private DailySelfieAdapter mAdapter;

    private DailySelfieItem mDailySelfieItem;

    private AlarmManager mAlarmManager;
    private Intent mNotificationReceiverIntent;
    private PendingIntent mNotificationReceiverPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Create a new DailySelfieAdapter for this DailySelfieActivity's ListView
        mAdapter = new DailySelfieAdapter(getApplicationContext());

        // Attach the adapter to this ListActivity's ListView
        this.getListView().setAdapter(mAdapter);

        // set up the notification for taking the next selfie
        initializeNotifications();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_daily_selfie, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_camera:
                dispatchTakePictureIntent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            // since there's no data returned in the data intent, and result ok,
            // assuming it was saved at the file location.  Adding the DailySelfieItem
            // to the adapter.
            if(mDailySelfieItem != null) {
                mAdapter.add(mDailySelfieItem);
                mDailySelfieItem = null;
            }
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        super.onListItemClick(l, v, position, id);
        // Create an intent stating which Activity you would like to start
        Intent intent = new Intent(this, PreviewImageActivity.class);
        DailySelfieItem dailySelfieItem = mAdapter.getItem(position);
        intent.putExtra(PreviewImageActivity.IMAGE_PATH, dailySelfieItem.getFullPhotoPath());

        // Use the Intent to start Google Maps application using Activity.startActivity()
        startActivity(intent);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        Date now = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(now);
        // todo: store name as simple value for sorting purposes, and prettify
        // todo: on list item display
        String prettyName = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.LONG, SimpleDateFormat.SHORT).format(now);
        String imageFileName = "SELFIE_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a DailySelfieItem object with the file details
        mDailySelfieItem = new DailySelfieItem(image.getAbsolutePath(),
                prettyName);

        // return generated File object
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(this, R.string.file_creation_error, Toast.LENGTH_LONG).show();
                finish();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void initializeNotifications() {

        // Get the AlarmManager Service
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Create an Intent to broadcast to the DailySelfieNotificationReceiver
        mNotificationReceiverIntent = new Intent(DailySelfieActivity.this,
                DailySelfieNotificationReceiver.class);

        // Create an PendingIntent that holds the NotificationReceiverIntent
        mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(
                this, 0, mNotificationReceiverIntent, 0);

        // Set inexact repeating alarm
        mAlarmManager.setRepeating(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + INITIAL_ALARM_DELAY,
                INITIAL_ALARM_DELAY,
                mNotificationReceiverPendingIntent);
    }

}
