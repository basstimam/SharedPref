package com.example.sharedpref

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.sharedpref.databinding.ActivityMainBinding
import com.example.sharedpref.model.UserModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mUserPreference: UserPreference
    private var isPreferenceEmpty = false
    private lateinit var userModel: UserModel

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()

    ) {
        result ->
        if(result.data != null && result.resultCode == FormUserPrefActivity.RESULT_CODE){
            userModel = result.data?.getParcelableExtra<UserModel>(FormUserPrefActivity.EXTRA_RESULT) as UserModel
            populateView(userModel)
            checkForm(userModel)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "My User Preference"

        mUserPreference = UserPreference(this)

        showExistingPreference()
    }

    private fun showExistingPreference(){
        userModel = mUserPreference.getUser()

    }

    private fun populateView(userModel: UserModel){
        binding.apply {
            tvName.text = if(userModel.name.toString().isEmpty()) "Tidak Ada" else userModel.name
            tvAge.text = if(userModel.age.toString().isEmpty()) "Tidak Ada" else userModel.age.toString()
            tvIsLoveMu.text = if(userModel.isLoveMU.toString().isEmpty()) "Tidak Ada" else userModel.isLoveMU.toString()
            tvEmail.text = if(userModel.email.toString().isEmpty()) "Tidak Ada" else userModel.email
            tvPhone.text = if(userModel.phoneNumber.toString().isEmpty()) "Tidak Ada" else userModel.phoneNumber
        }

    }

    private fun checkForm(userModel: UserModel){
        when{
            userModel.name.toString().isEmpty() -> {
                binding.btnSave.text = getString(R.string.change)
                isPreferenceEmpty = false
            }
            else -> {
                binding.btnSave.text = getString(R.string.save)
                isPreferenceEmpty = true
            }
        }
    }
}

