package com.example.binlookup

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.binlookup.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.net.toUri

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: BinViewModel by viewModels()
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.finderBtn.shrink()

        viewModel.countryCode.observe(this) {
            Glide
                .with(this)
                .load(viewModel.getUrlForFlag())
                .centerCrop()
                .into(binding.countryFlag)
        }
        viewModel.inputBinCode.observe(this) {
            if (viewModel.checkValidNumbers()) {
                binding.finderBtn.extend()
            } else {
                binding.finderBtn.shrink()
            }
        }
        binding.finderBtn.setOnClickListener {
            if (viewModel.checkValidNumbers()) {

                viewModel.getBinInfo()
            } else {
                Toast.makeText(
                    this,
                    "Create first 6 or 8 numbers of your card",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.callBtn.setOnClickListener {
            val phoneNumber = viewModel.bankPhone.value
            if (phoneNumber == "" || phoneNumber == null || phoneNumber == "Not available") {
                Toast.makeText(this, "Phone number is not available", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(Intent.ACTION_DIAL) // ✅ safer; no permission needed
                intent.data = "tel:${viewModel.bankPhone.value}".toUri()
                startActivity(intent)
            }
        }

        binding.locationBtn.setOnClickListener {
            val longitude = viewModel.longitude.value
            if (longitude == 0 || longitude == null) {
                Toast.makeText(this, "Location is not available", Toast.LENGTH_SHORT).show()
            } else {
                val url =
                    "https://www.google.com/maps/search/?api=1&query=${viewModel.latitude.value},${viewModel.longitude.value}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }
        binding.webBtn.setOnClickListener {
            val url = viewModel.bankWebsite.value
            if (url == "" || url == null || url == "Not available") {
                Toast.makeText(this, "Web site is not available", Toast.LENGTH_SHORT).show()
            } else {
                val url = "https://${viewModel.bankWebsite.value}"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = url.toUri()
                startActivity(intent)
            }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.history, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("TAG", "onOptionsItemSelected: ishlavott")
        return if (item.itemId == R.id.history && viewModel.history.value?.isNotEmpty() == true || viewModel.history.value != null) {
            BottomSheet().show(supportFragmentManager, "BottomSheet")
            true
        } else {
            Toast.makeText(this, "You have never searched yet", Toast.LENGTH_SHORT).show()
            super.onOptionsItemSelected(item)
        }
    }
}
