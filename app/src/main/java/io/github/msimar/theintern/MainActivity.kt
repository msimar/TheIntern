package io.github.msimar.theintern

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val theInternView = findViewById<TheInternView>(R.id.theInternView)

    theInternView.setOnClickListener {
      theInternView.magic()
    }
  }
}
