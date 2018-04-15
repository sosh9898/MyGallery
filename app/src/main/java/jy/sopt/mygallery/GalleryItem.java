package jy.sopt.mygallery;

/**
 * Created by jyoung on 2018. 4. 16..
 */

public class GalleryItem {

    private String imagePath;
    private String imageLoc;

    public GalleryItem(String imagePath, String imageLoc){
        this.imagePath = imagePath;
        this.imageLoc = imageLoc;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getImageLoc() {
        return imageLoc;
    }
}
