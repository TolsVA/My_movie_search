package com.example.my_movie_search.view

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.my_movie_search.R
import com.example.my_movie_search.app.App
import com.example.my_movie_search.databinding.ActivityMainBinding
import com.example.my_movie_search.model.sqlite.SQLiteHelper
import com.example.my_movie_search.model.sqlite.SQLiteManager
import com.example.my_movie_search.test.TestThreadFragment
import com.example.my_movie_search.view.main.MainFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val receiver = MainBroadcastReceiver()

    companion object {
        private val handlerThread: HandlerThread by lazy {
            HandlerThread(App.appInstance.getString(R.string.my_handler_thread))
        }

        fun getHandler(): Handler {
            return Handler(handlerThread.looper)
        }

        private val db: SQLiteHelper by lazy {
            SQLiteHelper(App.appInstance.applicationContext)
        }

        private val managerSQLite: SQLiteManager by lazy {
            SQLiteManager(db.writableDatabase)
        }

        fun sqLiteManager(): SQLiteManager = managerSQLite
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_screen_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handlerThread.start()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commit()
        }

        registerReceiver(receiver, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_threads -> {
            supportFragmentManager.apply {
            beginTransaction()
                .add(R.id.container, TestThreadFragment.newInstance())
                .addToBackStack("")
                .commitAllowingStateLoss()
            }
            true
        }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        handlerThread.looper.quit()
        db.close()
        super.onDestroy()
    }
}