package jy.sopt.mygallery;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

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

            Toast.makeText(MainActivity.this, galleryAdapter.getGalleryItemList().get(position).getImagePath(), Toast.LENGTH_SHORT).show();
        }
    };


}
