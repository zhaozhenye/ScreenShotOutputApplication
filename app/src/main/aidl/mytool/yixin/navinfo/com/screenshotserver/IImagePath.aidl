// IImagePath.aidl
package mytool.yixin.navinfo.com.screenshotserver;
//import android.graphics.Bitmap;

// Declare any non-default types here with import statements

interface IImagePath {
      Bitmap getBitmap();

        int getNaviState();

        void setNaviState(int state);


}
