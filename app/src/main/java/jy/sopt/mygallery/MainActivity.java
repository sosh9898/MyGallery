package jy.sopt.mygallery;

import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jy.sopt.mygallery.helper.GalleryHelper;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.galleryRcv)
    RecyclerView rcvGallery;
    private GalleryHelper mGalleryManager;
    private GalleryAdapter galleryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        checkPermission();
        init();

    }

    private void checkPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("MyGallery App 을 사용하기 위해 다음의 권한이 필요합니다.")
                .setDeniedMessage("왜 거부하셨어요...\n하지만 [설정] > [권한] 에서 권한을 허용할 수 있어요.")
                .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    private void init() {
        initRecyclerGallery();
    }

    private List<GalleryItem> initGalleryPathList() {

        mGalleryManager = new GalleryHelper(getApplicationContext());
//        return mGalleryManager.getAllImagePathList();
        return mGalleryManager.getDateImagePathList(2018,1,4,2018,4,13);
    }

    private void showExif(ExifInterface exif) {

        StringBuilder myAttribute = new StringBuilder();
        myAttribute.append("[Exif information] \n\n")
                .append(getTagString(ExifInterface.TAG_DATETIME, exif))
                .append(getTagString(ExifInterface.TAG_FLASH, exif))
                .append(getTagString(ExifInterface.TAG_GPS_LATITUDE, exif))
                .append(getTagString(ExifInterface.TAG_GPS_LATITUDE_REF, exif))
                .append(getTagString(ExifInterface.TAG_GPS_LONGITUDE, exif))
                .append(getTagString(ExifInterface.TAG_GPS_LONGITUDE_REF, exif))
                .append(getTagString(ExifInterface.TAG_IMAGE_LENGTH, exif))
                .append(getTagString(ExifInterface.TAG_IMAGE_WIDTH, exif))
                .append(getTagString(ExifInterface.TAG_MAKE, exif))
                .append(getTagString(ExifInterface.TAG_MODEL, exif))
                .append(getTagString(ExifInterface.TAG_ORIENTATION, exif))
                .append(getTagString(ExifInterface.TAG_WHITE_BALANCE, exif));

        Toast.makeText(this, myAttribute.toString(), Toast.LENGTH_SHORT).show();
    }

    private String getTagString(String tag, ExifInterface exif) {
        return (tag + " : " + exif.getAttribute(tag) + "\n");
    }

    private void initRecyclerGallery() {

        galleryAdapter = new GalleryAdapter(initGalleryPathList(), onClickListener);
        rcvGallery.setAdapter(galleryAdapter);
        rcvGallery.setLayoutManager(new GridLayoutManager(this, 4));
        rcvGallery.setItemAnimator(new DefaultItemAnimator());
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final int position = rcvGallery.getChildAdapterPosition(view);
            //Exif 정보
            try {
                ExifInterface exif = new ExifInterface(galleryAdapter.getGalleryItemList().get(position).getImagePath());
                showExif(exif);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Error : " + e.toString(), Toast.LENGTH_LONG).show();
            }

            //파일 경로
//            Toast.makeText(MainActivity.this, galleryAdapter.getGalleryItemList().get(position).getImagePath(), Toast.LENGTH_SHORT).show();
        }
    };


}
