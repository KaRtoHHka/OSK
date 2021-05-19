package com.oleg.oskfin.ui.admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.oleg.oskfin.R;
import com.oleg.oskfin.data.FireDatabase;
import com.oleg.oskfin.data.Store;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.IOException;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class DialogAddItem extends DialogFragment implements View.OnClickListener {

    private MaterialEditText nameOfItem, costOfItem, aboutOfItem;
    private Uri filePath;
    private ImageView imageOfItem;
    private final int PICK_IMAGE_REQUEST = 71;
    Bundle bundle;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_item, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        bundle = getArguments();

        nameOfItem = view.findViewById(R.id.add_item_item_name);
        costOfItem = view.findViewById(R.id.add_item_cost_of_item);
        aboutOfItem = view.findViewById(R.id.add_item_about_of_item);
        imageOfItem = view.findViewById(R.id.add_item_image_of_item);
        Button chooseButton = view.findViewById(R.id.add_item_choose_image);
        Button cancelButton = view.findViewById(R.id.add_item_cancel_button);
        Button addButton = view.findViewById(R.id.add_item_add_button);

        if (bundle != null) {
            Store store = (Store) bundle.getSerializable("item");
            nameOfItem.setText(store.getNameOfProduct());
            costOfItem.setText(store.getCostOfProduct() + "");
            aboutOfItem.setText(store.getAboutProduct());
            Glide.with(getActivity()).load(store.getImageOfProduct()).into(imageOfItem);
        }

        chooseButton.setOnClickListener(DialogAddItem.this);
        cancelButton.setOnClickListener(DialogAddItem.this);
        addButton.setOnClickListener(DialogAddItem.this);


        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.add_item_choose_image):
                chooseImage();
                break;

            case (R.id.add_item_cancel_button):
                requireArguments().clear();
                dismiss();
                break;

            case (R.id.add_item_add_button):
                if (!TextUtils.isEmpty(Objects.requireNonNull(nameOfItem.getText()).toString())
                        && !TextUtils.isEmpty(Objects.requireNonNull(costOfItem.getText()).toString())
                        && !TextUtils.isEmpty(Objects.requireNonNull(aboutOfItem.getText()).toString()))
                    try {
                        Integer.parseInt(costOfItem.getText().toString());
                        if (bundle == null)
                            uploadImage();
                        else editImage();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), R.string.not_int, Toast.LENGTH_LONG).show();
                    }
                else
                    Toast.makeText(getActivity(), R.string.enter_the_name_and_cost, Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), filePath);
                imageOfItem.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            final String name = nameOfItem.getText().toString();

            StorageReference ref = storageReference.child("items/" + name);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), R.string.uploaded, Toast.LENGTH_SHORT).show();
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful()) ;
                            Uri downloadUrl = urlTask.getResult();

                            Store store = new Store();
                            store.setNameOfProduct(name);
                            store.setCostOfProduct(Integer.parseInt(costOfItem.getText().toString()));
                            store.setAboutProduct(aboutOfItem.getText().toString());
                            store.setImageOfProduct(downloadUrl.toString());

                            FireDatabase.addItem(store);

                            dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        } else {
            Toast.makeText(getActivity(), R.string.choose_image, Toast.LENGTH_LONG).show();
        }
    }

    private void editImage() {
        final String name = nameOfItem.getText().toString();
        final Store store = new Store();

        Bundle storeBundle = getArguments();

        store.setNameOfProduct(name);
        store.setCostOfProduct(Integer.parseInt(costOfItem.getText().toString()));
        store.setAboutProduct(aboutOfItem.getText().toString());
        store.setImageOfProduct(((Store) storeBundle.getSerializable("item")).getImageOfProduct());

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference ref = storageReference.child("items/" + name);
            FireDatabase.removeItem((Store) storeBundle.getSerializable("item"));
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), R.string.uploaded, Toast.LENGTH_SHORT).show();
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful()) ;
                            Uri downloadUrl = urlTask.getResult();
                            store.setImageOfProduct(downloadUrl.toString());

                            FireDatabase.addItem(store);

                            getArguments().clear();
                            dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        } else {
            FireDatabase.editItem(store);
            dismiss();
        }
    }

}