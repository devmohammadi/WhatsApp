package com.fmohammadi.whatsapp.controller.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TableRow
import androidx.appcompat.app.AlertDialog
import com.fmohammadi.whatsapp.R
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
    }

    fun settingClick(view: View) {
        view as TableRow
        when (view.id) {
            tr_name.id -> createAlertDialog(default_name.text.toString(),"Name")
            tr_about.id -> createAlertDialog(default_about.text.toString(), "About")
        }
    }

    fun createAlertDialog(valueDefault: String , title:String){
        var view = layoutInflater.inflate(R.layout.popup_update_status,null)
        alertDialogBuilder = AlertDialog.Builder(this).setView(view)
        alertDialog = alertDialogBuilder!!.create()
        alertDialog!!.show()

        if(valueDefault == null) view.default_popUpdate.hint = "Please Enter Your $title"
        else  view.default_popUpdate.setText(valueDefault)
        view.btn_popChange.text = "Update $title"
        view.popTitle.text = title


    }
}