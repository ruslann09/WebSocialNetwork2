package com.reflecttoyou.makesomefriend.selectyourdream;

import android.app.Application;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AppModule extends Application {

    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    public void onCreate() {
        super.onCreate();

//        FirebaseApp.initializeApp(getApplicationContext());
//
//        // Создание расширенной конфигурации библиотеки.
//        YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder("").build();
//        // Инициализация AppMetrica SDK.
//        YandexMetrica.activate(getApplicationContext(), config);
//        // Отслеживание активности пользователей.
//        YandexMetrica.enableActivityAutoTracking(this);
//
//        YandexMetricaPush.init(getApplicationContext());
//
//        storage = FirebaseStorage.getInstance();
//        storageReference = storage.getReference();
//
//        StorageReference ref = storageReference.child("avatars/");

//        ref.child("images/stars.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                // Got the download URL for 'users/me/profile.png'
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//            }
//        });

//        File file = new File(Environment.getExternalStorageDirectory() + "/"
//                + getResources().getString(R.string.app_name) + "/avatars");
//
//        if (!file.exists())
//            file.mkdirs();
//
//        storage = FirebaseStorage.getInstance();
//
//        final StorageReference storageRef = storage.getReference();
//
//        getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshotUser) {
//                for (DataSnapshot dataSnapshot : dataSnapshotUser.getChildren()) {
//                    final File fileImage = new File(Environment.getExternalStorageDirectory() + "/"
//                            + getResources().getString(R.string.app_name) + "/avatars/" + dataSnapshot.getValue() + ".jpg");
//
//                    if (!fileImage.exists())
//                        storageRef.child("avatars/" + dataSnapshot.getValue()).getFile(fileImage);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        startService(new Intent(getApplicationContext(), PushNotificationsReceiver.class));
    }
}
