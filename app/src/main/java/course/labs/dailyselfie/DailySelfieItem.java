package course.labs.dailyselfie;

public class DailySelfieItem {

    private String mFullPhotoPath = new String();
    private String mImageFileName = new String();
    private long mId;

    public DailySelfieItem() {
    }

    public DailySelfieItem(String mFullPhotoPath, String mImageFileName) {
        this();
        this.mFullPhotoPath = mFullPhotoPath;
        this.mImageFileName = mImageFileName;
    }

    public String getFullPhotoPath() {
        return mFullPhotoPath;
    }

    public void setFullPhotoPath(String mFullPhotoPath) {
        this.mFullPhotoPath = mFullPhotoPath;
    }

    public String getImageFileName() {
        return mImageFileName;
    }

    public void setImageFileName(String mImageFileName) {
        this.mImageFileName = mImageFileName;
    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }
}
