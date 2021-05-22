package com.omnyom.yumyum.ui.useroption

import android.util.Log.d
import android.widget.SeekBar
import android.widget.Switch
import com.omnyom.yumyum.R
import com.omnyom.yumyum.databinding.ActivityEurekaOptionBinding
import com.omnyom.yumyum.helper.PreferencesManager
import com.omnyom.yumyum.ui.base.BaseBindingActivity

class EurekaOptionActivity : BaseBindingActivity<ActivityEurekaOptionBinding>(R.layout.activity_eureka_option) {
    override fun extraSetupBinding() { }

    override fun setup() {
        binding.sbEurekaDistance.setOnSeekBarChangeListener( object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.tvEurekaDistance.text = "${progress+1}Km"
                PreferencesManager.setLong(this@EurekaOptionActivity, "eurekaDistance", progress.toString().toLong())
                PreferencesManager.eurekaDistance = progress.toString().toLong()

            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        binding.swEurekaOnOff.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                PreferencesManager.setLong(this@EurekaOptionActivity, "pushOn",  1)
                PreferencesManager.pushOn = 1
                binding.swEurekaOnOff.text = "켜짐"
                d("switch", "$isChecked")
                d("switch", "${PreferencesManager.pushOn}")
            } else {
                PreferencesManager.setLong(this@EurekaOptionActivity, "pushOn",  0)
                PreferencesManager.pushOn = 0
                binding.swEurekaOnOff.text = "꺼짐"
                d("switch", "$isChecked")
                d("switch", "${PreferencesManager.pushOn}")
            }
        }
    }

    override fun setupViews() {
        supportActionBar?.hide()
        binding.sbEurekaDistance.progress = PreferencesManager.eurekaDistance.toString().toInt()
        if (PreferencesManager.pushOn.toString().toInt() == 1) {
            binding.swEurekaOnOff.isChecked = true
        } else {
            binding.swEurekaOnOff.isChecked = false
        }
    }

    override fun onSubscribe() { }

    override fun release() { }

}