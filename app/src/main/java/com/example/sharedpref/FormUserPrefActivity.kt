package com.example.sharedpref

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import com.example.sharedpref.databinding.ActivityFormUserPrefBinding
import com.example.sharedpref.model.UserModel
import com.google.android.material.snackbar.Snackbar

class FormUserPrefActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityFormUserPrefBinding
    private lateinit var userModel: UserModel

    companion object {
        const val EXTRA_TYPE_FORM = "extra_type_form"
        const val EXTRA_RESULT = "extra_result"
        const val RESULT_CODE = 101

        const val TYPE_ADD = 1
        const val TYPE_EDIT = 2

        private const val FIELD_REQUIRED = "Field tidak boleh kosong"
        private const val FIELD_DIGIT_ONLY = "Hanya boleh terisi numerik"
        private const val FIELD_IS_NOT_VALID = "Email tidak valid"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFormUserPrefBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener(this)

        userModel = intent.getParcelableExtra<UserModel>("USER") as UserModel
        val formType = intent.getIntExtra(EXTRA_TYPE_FORM, 0)

        var actionBarTitle = ""
        var btnTitle = ""

        when(formType){
            TYPE_ADD -> {
                actionBarTitle = "Tambah Baru"
                btnTitle = "Simpan"
            }
            TYPE_EDIT -> {
                actionBarTitle = "Ubah"
                btnTitle = "Update"
                showPreferenceInForm()
            }
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnSave.text = btnTitle
    }

    private fun showPreferenceInForm() {
        binding.apply {
            binding.edtName.setText(userModel.name)
            binding.edtEmail.setText(userModel.email)
            binding.edtAge.setText(userModel.age.toString())
            binding.edtPhone.setText(userModel.phoneNumber)
            if (userModel.isLoveMU){
                binding.rbYes.isChecked = true
            } else {
                binding.rbNo.isChecked = true
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View?) {

        if (view!!.id == R.id.btn_save) {
            binding.apply {
                val name = edtName.text.toString().trim()
                val email = edtEmail.text.toString().trim()
                val age = edtAge.text.toString().trim()
                val phoneNumber = edtPhone.text.toString().trim()
                val isLoveMU = rgLoveMu.checkedRadioButtonId == R.id.rb_yes



                if (name.isEmpty()) {
                    edtName.error = FIELD_REQUIRED
                    return
                }

                if (email.isEmpty()) {
                    edtEmail.error = FIELD_REQUIRED
                    return
                }

                if (!isValidEmail(email)) {
                    edtEmail.error = FIELD_IS_NOT_VALID
                    return
                }

               if (!TextUtils.isDigitsOnly(phoneNumber)){
                    edtPhone.error = FIELD_DIGIT_ONLY
                    return
                   }

                if (phoneNumber.isEmpty()) {
                    edtPhone.error = FIELD_REQUIRED
                    return
                }

                saveUser(name, email, age, phoneNumber, isLoveMU)

                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_RESULT, userModel)
                setResult(RESULT_CODE, resultIntent)

                finish()



            }


        }


    }

    private fun isValidEmail(email: CharSequence): Boolean {
        return  android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun saveUser(name: String, email: String, age: String, phoneNumber: String, isLoveMU: Boolean){
        val userPreference = UserPreference(this)

        userModel.name = name
        userModel.email = email
        userModel.age = Integer.parseInt(age)
        userModel.phoneNumber = phoneNumber
        userModel.isLoveMU = isLoveMU

        userPreference.setUser(userModel)
        Snackbar.make(binding.btnSave, "Data saved", Snackbar.LENGTH_SHORT).show()
    }
}