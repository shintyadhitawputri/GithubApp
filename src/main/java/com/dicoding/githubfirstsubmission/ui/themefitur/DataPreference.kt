package com.dicoding.githubfirstsubmission.ui.themefitur

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.dicoding.githubfirstsubmission.databinding.ActivityDataPreferenceBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DataPreference : AppCompatActivity() {
    private lateinit var binding: ActivityDataPreferenceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val pref = SettingPreferences.getInstance(application.dataStore)

        val themePreference = binding.switchTheme
        themePreference.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            lifecycleScope.launch {
                pref.setTheme(isChecked)

                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
        themePreference.isChecked = runBlocking { pref.getTheme().first() }
    }
}
