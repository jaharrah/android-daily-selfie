package course.labs.dailyselfie;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DailySelfieAdapter extends BaseAdapter {

    private final List<DailySelfieItem> mItems = new ArrayList<DailySelfieItem>();
    private final Context mContext;

    private static final String TAG = "DailySelfie";

    public DailySelfieAdapter(Context context) {

        mContext = context;

    }

    // Add a ToDoItem to the adapter
    // Notify observers that the data set has changed

    public void add(DailySelfieItem item) {

        mItems.add(item);
        notifyDataSetChanged();

    }

    // Clears the list adapter of all items.

    public void clear() {

        mItems.clear();
        notifyDataSetChanged();

    }

    // Returns the number of ToDoItems

    @Override
    public int getCount() {

        return mItems.size();

    }

    // Retrieve the number of ToDoItems

    @Override
    public DailySelfieItem getItem(int pos) {

        return mItems.get(pos);

    }

    // Get the ID for the ToDoItem
    // In this case it's just the position

    @Override
    public long getItemId(int pos) {

        return pos;

    }

    // Create a View for the DailySelfieItem at specified position
    // Remember to check whether convertView holds an already allocated View
    // before created a new View.

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        // Get the current DailySelfieItem
        final DailySelfieItem dailySelfieItem = this.getItem(position);

        if(convertView == null) {
            // Inflate the View for this ToDoItem from todo_item.xml.
            convertView = LayoutInflater.from(mContext).inflate(R.layout.daily_selfie_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.imageName = (TextView) convertView.findViewById(R.id.imageName);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);

            // store the holder with the view.
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        // Fill in specific DailySelfieItem data
        // Remember that the data that goes in this View
        // corresponds to the user interface elements defined
        // in the layout file

        // Populate ImageView with scaled bitmap from full photo path
        setPic(viewHolder.imageView, dailySelfieItem.getFullPhotoPath());

        // Display Image Name in TextView
        viewHolder.imageName.setText(dailySelfieItem.getImageFileName());

        // Return the View you just created
        return convertView;

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
        int scaleFactor = 5;

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(fullPhotoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }

    static class ViewHolder {
        ImageView imageView;
        TextView imageName;
    }
}
