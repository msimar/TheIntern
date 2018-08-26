package io.github.msimar.theintern

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem


class MainActivity : AppCompatActivity() {

  private lateinit var theInternView: TheInternView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    theInternView = findViewById(R.id.theInternView)

    theInternView.setOnClickListener {
      theInternView.execute()
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    val inflater = menuInflater
    inflater.inflate(R.menu.menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return when (item?.itemId) {
      R.id.reset -> {
        theInternView.reset()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }
}
