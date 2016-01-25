<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/video_container">

<com.samsung.retailexperience.retailhero.view.VideoTextureView
        android:id="@+id/video_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:looped="false"
        app:autoPlay="false"
        app:isShowVideoFrame="true"
        app:scaleToVideoSize="true"/>

<ImageView
android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/tap_camera_icon"
        android:src="@drawable/tap_camera_icon1"
        android:visibility="gone"/>


<!-- Camera Preview Layout-->
<RelativeLayout
android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/camera_layout"
        android:visibility="gone">

<RelativeLayout
android:layout_width="match_parent"
        android:layout_height="match_parent">

<RelativeLayout
android:id="@+id/camera_view_test"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_below="@+id/top_fragment_test"
        android:layout_above="@+id/bottom_fragment_test"
        android:background="#000000"
        android:clickable="false">

</RelativeLayout>

<fragment
android:id="@+id/top_fragment_test"
        android:layout_width="match_parent"
        android:layout_height="@dimen/camera_top_menubar_height"
        android:layout_alignParentTop="true"
        android:name="com.samsung.retailexperience.retailhero.ui.fragment.camera_app.TopMenuBarFragment" />

<fragment
android:id="@+id/bottom_fragment_test"
        android:layout_width="match_parent"
        android:layout_height="@dimen/camera_bottom_menubar_height"
        android:layout_alignParentBottom="true"
        android:name="com.samsung.retailexperience.retailhero.ui.fragment.camera_app.BottomMenuBarFragment" />
</RelativeLayout>


<!-- Interactive Icons-->

<ImageView
android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:id="@+id/capture_super"
        android:clickable="false"
        android:src="@drawable/capture_super"
        android:layout_centerHorizontal="true"
        android:paddingBottom="35dp"
        android:paddingRight="3dp" />

<ImageButton
android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:id="@+id/capture_button"
        android:src="@drawable/camera_button"
        android:layout_centerHorizontal="true"
        android:background="@android:color/transparent"
        android:baselineAlignBottom="false"
        android:layout_alignParentBottom="true"
        android:clickable="false"
        android:paddingBottom="35dp" />

</RelativeLayout>

</FrameLayout>