package com.example.ecoenvir;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import org.w3c.dom.Text;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class EditProfileDetails extends Fragment {
    private CircleImageView profilePic;
    private Uri imageUri;
    private StorageReference storageProfilePicRef;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;
    ActivityResultLauncher<Intent> intentActivityResultLauncher;

    public void openGalleryForResult(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT, null);
        galleryIntent.setType("image/*");

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Intent chooser = new Intent(Intent.ACTION_CHOOSER);
        chooser.putExtra(Intent.EXTRA_INTENT, galleryIntent);
        chooser.putExtra(Intent.EXTRA_TITLE, "Select from:");

        Intent [] intentArray = {cameraIntent};
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
        intentActivityResultLauncher.launch(chooser);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            imageUri = data.getData();
                            profilePic.setImageURI(imageUri);
                        }
                    }
                });

        storageProfilePicRef = FirebaseStorage.getInstance("gs://ecoenvir-66d60.appspot.com").getReference();
        EditText name = view.findViewById(R.id.et_profileName);
        EditText username = view.findViewById(R.id.et_profileUsername);
        EditText phone = view.findViewById(R.id.et_profilePhone);
        profilePic = view.findViewById(R.id.iv_profile_picture);
        Button edit = view.findViewById(R.id.btn_editProfile);
        TextView changeProfile = view.findViewById(R.id.tv_changeProfile);
        ImageView backBut = view.findViewById(R.id.but_back);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance("https://ecoenvir-66d60-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("user");

        backBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                    Direct to profile fragment
                Fragment next_fragment = new Profile();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,next_fragment).commit();
            }
        });

        if(currentUser != null)
        {
            Glide.with(getContext()).load(currentUser.getPhotoUrl()).into(profilePic);
            name.setText(currentUser.getDisplayName());

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String currentUserID = currentUser.getUid();
                        String txt_username = dataSnapshot.child(currentUserID).child("username").getValue(String.class);
                        String txt_phone = dataSnapshot.child(currentUserID).child("phoneNo").getValue(String.class);
                        username.setText(txt_username);
                        phone.setText(txt_phone);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            changeProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openGalleryForResult(getView());
                }
            });
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String txt_name = name.getText().toString();
                    String txt_username = username.getText().toString();
                    String txt_phone = phone.getText().toString();
//                  add @ if the first character isn't
                    if(txt_username.charAt(0) != '@')
                    {
                        txt_username = "@"+txt_username;
                    }
                    UserProfileChangeRequest profileChangeRequest;
                    if(imageUri != null){
                        profileChangeRequest = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(imageUri).setDisplayName(txt_name).build();
                    }
                    else{
                        profileChangeRequest = new UserProfileChangeRequest.Builder()
                                .setDisplayName(txt_name).build();
                    }

                    currentUser.updateProfile(profileChangeRequest)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //                  Edit data in database
                                    uploadImageToFirebase(currentUser.getPhotoUrl());
                                    databaseReference.child(currentUser.getUid()).child("name").setValue(txt_name);
                                    databaseReference.child(currentUser.getUid()).child("username").setValue(username.getText().toString());
                                    databaseReference.child(currentUser.getUid()).child("phoneNo").setValue(txt_phone);
                                    Toast.makeText(getActivity(),"Profile updated",Toast.LENGTH_SHORT).show();
                                    //                    Direct to profile fragment
                                    Fragment next_fragment = new Profile();
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,next_fragment).commit();
                                }
                            });

                }
            });
        }
    }

    private void uploadImageToFirebase(Uri uri){
        StorageReference fileRef = storageProfilePicRef.child("users/"+currentUser.getUid()+"/profile.jpg");
        if(uri != null){
            fileRef.putFile(uri);
        }

    }




}