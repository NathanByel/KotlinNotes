package ru.nbdev.kotlingb

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initListeners()
    }

    private fun initListeners() {
        button_toast.setOnClickListener {
            Toast.makeText(applicationContext, "Some message", Toast.LENGTH_SHORT).show()
        }
    }
}
