package com.oleg.oskfin.data;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FireDatabase {
    private static FirebaseDatabase db;
    private static FirebaseAuth auth;
    private static DatabaseReference myRef;
    private static FirebaseUser fUser;
    private static FirebaseStorage storage;
    private static StorageReference storageRef;

    static {
        auth = FirebaseAuth.getInstance();
        fUser = auth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public static void getData(final MyCallBack myCallBack) {

        final Users[] userD = new Users[1];

        myRef = db.getReference("Users/" + fUser.getUid());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userD[0] = dataSnapshot.getValue(Users.class);
                assert userD[0] != null;
                myCallBack.onCallBack(userD[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    public static void getPoints(final MyCallBackArray myCallBackArray) {
        myRef = db.getReference("Points");
        String[] list;
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int iter = 0;

                for (DataSnapshot storeSnapshot :
                        dataSnapshot.getChildren()) {
                    iter++;
                }

                for (DataSnapshot arraySnapshot :
                        dataSnapshot.getChildren()) {
                    myCallBackArray.onCallBackArray(arraySnapshot.getValue(String.class), iter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void getProductList(final MyCallBackStore myCallBackStore) {
        myRef = db.getReference("Store");
        final ArrayList<Store> store = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            Store check;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int iter = 0;

                for (DataSnapshot storeSnapshot :
                        dataSnapshot.getChildren()) {
                    iter++;
                }

                for (DataSnapshot storeSnapshot :
                        dataSnapshot.getChildren()) {
                    check = storeSnapshot.getValue(Store.class);

                    myCallBackStore.onCallBack(check, iter);

                    store.add(check);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static Users findData(final String phone, final MyCallBack myCallBack) {
        myRef = db.getReference("Users");
        final Users[] user = new Users[1];
        myRef.addValueEventListener(new ValueEventListener() {
            Users check;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot :
                        dataSnapshot.getChildren()) {
                    check = userSnapshot.getValue(Users.class);
                    if (check.getPhoneNumber().equals(phone.replaceAll("[^0-9]", ""))) {
                        user[0] = check;
                        myCallBack.onCallBack(user[0]);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return user[0];
    }

    public static void setData(Users user) {
        myRef = db.getReference("Users/" + fUser.getUid());
        if (user.getPassword() != null)
            fUser.updatePassword(user.getPassword());
        if (user.getMail() != null)
            fUser.updateEmail(user.getPassword());
        myRef.setValue(user);
    }

    public static void setOtherData(Users user) {
        myRef = db.getReference("Users/" + user.getId());
        myRef.setValue(user);
    }

    public static void addItem(Store store) {
        myRef = db.getReference("Store");
        myRef.push().setValue(store);
    }

    public static void addPoint(String point) {
        myRef = db.getReference("Points");
        myRef.push().setValue(point);
    }

    public static void removeOrder(final long orderTime) {
        myRef = db.getReference("Orders");
        myRef.addValueEventListener(new ValueEventListener() {
            Orders check;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot :
                        dataSnapshot.getChildren()) {
                    check = userSnapshot.getValue(Orders.class);
                    if (check.getOrderTime() == orderTime) {
                        userSnapshot.getRef().removeValue();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void removeUser(final String userPhone) {
        myRef = db.getReference("Users");
        myRef.addValueEventListener(new ValueEventListener() {
            Users check;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot :
                        dataSnapshot.getChildren()) {
                    check = userSnapshot.getValue(Users.class);
                    if (check.getPhoneNumber().equals(userPhone.replaceAll("[^0-9]", ""))) {
                        userSnapshot.getRef().removeValue();
                        fUser.delete();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void removeItem(final Store store) {
        myRef = db.getReference("Store");
        myRef.addValueEventListener(new ValueEventListener() {
            Store check;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot :
                        dataSnapshot.getChildren()) {
                    check = userSnapshot.getValue(Store.class);
                    if (check.getImageOfProduct() == store.getImageOfProduct()) {
                        userSnapshot.getRef().removeValue();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        storageRef = storage.getReferenceFromUrl(store.getImageOfProduct());
        storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                Log.d(TAG, "FireDatabase remove item onSuccess: deleted file");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                Log.d(TAG, "FireDatabase remove item onFailure: did not delete file");
            }
        });
    }

    public static void removePoint(final String nameOfPoint) {
        myRef = db.getReference("Points");
        myRef.addValueEventListener(new ValueEventListener() {
            String check;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot :
                        dataSnapshot.getChildren()) {
                    check = userSnapshot.getValue(String.class);
                    if (check.equals(nameOfPoint)) {
                        userSnapshot.getRef().removeValue();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void purchase(Users user, Store store, String point) {
        myRef = db.getReference("Users/" + user.getId() + "/money");
        myRef.setValue(user.getMoney());
        myRef = db.getReference("Orders");
        Orders orders = new Orders();
        orders.setItem(store.getNameOfProduct());
        orders.setPoint(point);
        myRef.push().setValue(orders);
    }

    public static void editItem(final Store store) {
        myRef = db.getReference("Store");
        myRef.addValueEventListener(new ValueEventListener() {
            Store check;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot :
                        dataSnapshot.getChildren()) {
                    check = userSnapshot.getValue(Store.class);
                    if (check.getImageOfProduct() == store.getImageOfProduct()) {
                        userSnapshot.getRef().setValue(store);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void editPoint(final String nameOfItem, final String newName) {
        myRef = db.getReference("Points");
        myRef.addValueEventListener(new ValueEventListener() {
            String check;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot :
                        dataSnapshot.getChildren()) {
                    check = userSnapshot.getValue(String.class);
                    if (check.equals(nameOfItem)) {
                        userSnapshot.getRef().setValue(newName);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void fillAllUsers(final MyCallBackUsers myCallBack) {
        myRef = db.getReference("Users");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            Users check;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int iter = 0;

                for (DataSnapshot storeSnapshot :
                        dataSnapshot.getChildren()) {
                    iter++;
                }

                for (DataSnapshot storeSnapshot :
                        dataSnapshot.getChildren()) {
                    check = storeSnapshot.getValue(Users.class);
                    myCallBack.onCallBack(check, iter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}