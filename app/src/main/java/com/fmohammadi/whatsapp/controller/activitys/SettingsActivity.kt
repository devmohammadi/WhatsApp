package com.fmohammadi.whatsapp.controller.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TableRow
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.fmohammadi.whatsapp.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.StorageRegistrar
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.popup_update_status.*
import kotlinx.android.synthetic.main.popup_update_status.view.*

class SettingsActivity : AppCompatActivity() {

    var mDatabase: DatabaseReference? = null
    var mUser: FirebaseUser? = null
    var mStorage: StorageRegistrar? = null
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

        mDatabase!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }
            override fun onDataChange(snapshot: DataSnapshot) {
                var name = snapshot.child("name").value
                var status = snapshot.child("status").value
                var image = snapshot.child("image").value
                var email = snapshot.child("email").value
                var thumb_image = snapshot.child("thumb_image").value

                default_name.text = name.toString()
                default_about.text = status.toString()
                default_email.text = email.toString()
            }
        })

        change_profile.setOnClickListener{
            var galleryIntent = Intent()
            galleryIntent.type = "image/*"
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(galleryIntent,"choose image"),GALLERY_ID)
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
            var childPath: String? = null
            if (title == "About") childPath = "status"
            if (title == "Name") childPath = "name"
            if (childPath != null) {
                mDatabase!!.child(childPath).setValue(updated)
                    .addOnCompleteListener { task: Task<Void> ->
                        if (task.isSuccessful)
                            Toast.makeText(this, "Update $title is Successfully", Toast.LENGTH_LONG)
                                .show()
                        else
                            Toast.makeText(this, "Update $title is Not Successfully try again", Toast.LENGTH_LONG)
                                .show()
                    }
            }
            alertDialog!!.dismiss()
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}