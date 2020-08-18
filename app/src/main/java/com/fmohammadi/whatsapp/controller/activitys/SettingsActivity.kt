package com.fmohammadi.whatsapp.controller.activitys

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.TableRow
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.fmohammadi.whatsapp.R
import com.fmohammadi.whatsapp.controller.adapter.UsersAdapter
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageRegistrar
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.popup_show_image_profile.view.*
import kotlinx.android.synthetic.main.popup_update_status.*
import kotlinx.android.synthetic.main.popup_update_status.view.*
import java.io.ByteArrayOutputStream
import java.io.File

class SettingsActivity : AppCompatActivity() {

    var mDatabase: DatabaseReference? = null
    var mUser: FirebaseUser? = null
    var mStorage: StorageReference? = null
    var GALLERY_ID: Int = 1

    var alertDialogBuilder: AlertDialog.Builder? = null
    var alertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar!!.title = "Settings"

        mUser = FirebaseAuth.getInstance().currentUser
        var uid = mUser!!.uid

        mDatabase = FirebaseDatabase.getInstance().reference
            .child("Users").child(uid)

        mStorage = FirebaseStorage.getInstance().reference

        mDatabase!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                var name = snapshot.child("userName").value.toString()
                var email = snapshot.child("userEmail").value.toString()
                var status = snapshot.child("userStatus").value.toString()
                var image = snapshot.child("userImage").value.toString()
                var thumbImage = snapshot.child("userThumbImage").value.toString()

                default_name.text = name
                default_about.text = status
                default_email.text = email

                if(image!! != "default"){
                    Picasso.with(this@SettingsActivity)
                        .load(image)
                        .placeholder(R.drawable.profile)
                        .into(image_profile)
                }

                image_profile.setOnClickListener{
                    var viewImage = layoutInflater.inflate(R.layout.popup_show_image_profile,null)
                    alertDialogBuilder = AlertDialog.Builder(this@SettingsActivity).setView(viewImage)
                    alertDialog = alertDialogBuilder!!.create()
                    alertDialog!!.show()
                    Picasso.with(this@SettingsActivity)
                        .load(image)
                        .placeholder(R.drawable.profile)
                        .into(viewImage.popImage)
                }
            }
        })

        change_profile.setOnClickListener {
            var galleryIntent = Intent()
            galleryIntent.type = "image/*"
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(galleryIntent, "choose image"), GALLERY_ID)
        }
    }




    fun settingClick(view: View) {
        view as TableRow
        when (view.id) {
            tr_name.id -> createAlertDialog(default_name.text.toString(), "Name")
            tr_about.id -> createAlertDialog(default_about.text.toString(), "About")
        }
    }

    fun createAlertDialog(valueDefault: String, title: String) {
        var view = layoutInflater.inflate(R.layout.popup_update_status, null)
        alertDialogBuilder = AlertDialog.Builder(this).setView(view)
        alertDialog = alertDialogBuilder!!.create()
        alertDialog!!.show()

        if (valueDefault == null) view.default_popUpdate.hint = "Please Enter Your $title"
        else view.default_popUpdate.setText(valueDefault)
        view.btn_popChange.text = "Update $title"
        view.popTitle.text = title

        view.btn_popChange.setOnClickListener {
            var updated = view.default_popUpdate.text.toString().trim()
            if (TextUtils.isEmpty(updated)) {
                Toast.makeText(this, "Please Enter Your $title", Toast.LENGTH_LONG)
                    .show()
            } else {
                var childPath: String? = null
                if (title == "About") childPath = "userStatus"
                if (title == "Name") childPath = "userName"
                if (childPath != null) {
                    mDatabase!!.child(childPath).setValue(updated)
                        .addOnCompleteListener { task: Task<Void> ->
                            if (task.isSuccessful)
                                Toast.makeText(
                                    this,
                                    "Update $title is Successfully",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            else
                                Toast.makeText(
                                    this,
                                    "Update $title is Not Successfully try again",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                        }
                }
                alertDialog!!.dismiss()
            }
        }
    }


    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GALLERY_ID && resultCode == Activity.RESULT_OK) {
            var image: Uri = data!!.data!!

            CropImage.activity(image)
                .setAspectRatio(1, 1)
                .start(this)
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            var result = CropImage.getActivityResult(data)

            if (resultCode == Activity.RESULT_OK) {
                var imageUri: Uri = result.uri
                var userId = mUser!!.uid
                var thumbFile = File(imageUri.path)

                var thumbBitMap = Compressor(this)
                    .setMaxWidth(200)
                    .setMaxHeight(200)
                    .setQuality(50)
                    .compressToBitmap(thumbFile)

                // upload image to server ( firebase )
                var byteArray: ByteArrayOutputStream = ByteArrayOutputStream()
                thumbBitMap.compress(Bitmap.CompressFormat.PNG, 100, byteArray)

                var thumbByteArray: ByteArray = byteArray.toByteArray()

                var filePath = mStorage!!.child("chat_profile_images")
                    .child("$userId.png")

                var thumbFilePath = mStorage!!.child("chat_profile_images")
                    .child("thumbs")
                    .child("$userId.png")

                filePath.putFile(imageUri)
                    .addOnCompleteListener { task: Task<UploadTask.TaskSnapshot> ->
                        if (task.isSuccessful) {
                            filePath.downloadUrl.addOnSuccessListener { uri: Uri? ->
                                if (uri != null) {
                                    var downloadUrl = uri.toString()

                                    var uploadTask: UploadTask =
                                        thumbFilePath.putBytes(thumbByteArray)

                                    uploadTask.addOnCompleteListener { task: Task<UploadTask.TaskSnapshot> ->
                                        thumbFilePath.downloadUrl.addOnSuccessListener { uri: Uri? ->
                                            if (uri != null) {
                                                var thumbUrl = uri.toString()

                                                if (task.isSuccessful) {
                                                    var objectUpdated = HashMap<String, Any>()
                                                    objectUpdated.put("userImage", downloadUrl)
                                                    objectUpdated.put("userThumbImage", thumbUrl)

                                                    mDatabase!!.updateChildren(objectUpdated)
                                                        .addOnCompleteListener { task: Task<Void> ->
                                                            if (task.isSuccessful) {
                                                                Toast.makeText(
                                                                    this,
                                                                    "profile image saved",
                                                                    Toast.LENGTH_LONG
                                                                )
                                                                    .show()
                                                            }
                                                        }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
            }
        }
        //super.onActivityResult(requestCode, resultCode, data)
    }
}