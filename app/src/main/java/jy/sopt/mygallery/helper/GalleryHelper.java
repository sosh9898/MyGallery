package jy.sopt.mygallery.helper;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jy.sopt.mygallery.GalleryItem;

/**
 * Created by jyoung on 2018. 4. 15..
 */

public class GalleryHelper {

    private Context mContext;

    public GalleryHelper(Context context) {
        mContext = context;
    }

    public List<GalleryItem> getAllImagePathList() {

        List<GalleryItem> galleryItemList = new ArrayList<>();

        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA , MediaStore.MediaColumns.DISPLAY_NAME };

        String sortOrder = MediaStore.Images.Media._ID+ " DESC";

        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, sortOrder);


        while (cursor.moveToNext()) {
            GalleryItem galleryItem = new GalleryItem(cursor.getString(0)
                    , cursor.getString(1));
            galleryItemList.add(galleryItem);
        }

        cursor.close();

        return galleryItemList;
    }


    public List<GalleryItem> getDateImagePathList(int startYear, int startMonth, int startDay,int endYear, int endMonth, int endDay) {
        Calendar fromDateVar = Calendar.getInstance();
        fromDateVar.set(endYear, endMonth-1, endDay, 24,0);

        Calendar toDateVar = Calendar.getInstance();
        toDateVar.set(startYear, startMonth-1, startDay);

        List<GalleryItem> galleryItemList = new ArrayList<>();

        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA , MediaStore.MediaColumns.DISPLAY_NAME };

        String selection = MediaStore.Images.Media.DATE_TAKEN + ">? and "
                + MediaStore.Images.Media.DATE_TAKEN + "<?";

        String[] selectionArgs = {toDateVar.getTimeInMillis() + "", fromDateVar.getTimeInMillis() + ""};

        String sortOrder = MediaStore.Images.Media._ID+ " DESC";

        Cursor cursor = mContext.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);

        while (cursor.moveToNext()) {
            GalleryItem galleryItem = new GalleryItem(cursor.getString(0)
                    , cursor.getString(1));
            galleryItemList.add(galleryItem);
        }
        cursor.close();
        return galleryItemList;
    }


}
