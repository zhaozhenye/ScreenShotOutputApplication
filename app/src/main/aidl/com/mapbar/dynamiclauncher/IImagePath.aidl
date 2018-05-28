// IImagePath.aidl
package com.mapbar.dynamiclauncher;

// Declare any non-default types here with import statements

     interface IImagePath {
     Bitmap getBitmap();
     int getNaviState();
     void setNaviState(int state);
}
