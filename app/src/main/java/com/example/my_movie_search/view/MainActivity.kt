package com.example.my_movie_search.view

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Parcelable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.example.my_movie_search.R
import com.example.my_movie_search.app.App
import com.example.my_movie_search.contract.Navigator
import com.example.my_movie_search.contract.ResultListener
import com.example.my_movie_search.databinding.ActivityMainBinding
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.Persons
import com.example.my_movie_search.model.sqlite.SQLiteHelper
import com.example.my_movie_search.model.sqlite.SQLiteManager
import com.example.my_movie_search.view.main.MainFragment

class MainActivity : AppCompatActivity(), Navigator {
    private lateinit var binding: ActivityMainBinding

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

    private val currentFragment: Fragment
        get() = supportFragmentManager.findFragmentById(R.id.container)!!

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
//            updateUi()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handlerThread.start()

        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        setSupportActionBar(binding.toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, MainFragment())
                .commit()
        }

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, false)
    }

    override fun onDestroy() {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
        handlerThread.looper.quit()
        db.close()
        super.onDestroy()

    }

    //    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.menu_threads -> {
//            supportFragmentManager.apply {
//            beginTransaction()
//                .add(R.id.container, TestThreadFragment.newInstance())
//                .addToBackStack("")
//                .commitAllowingStateLoss()
//            }
//            true
//        }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    override fun showDetailPersonsScreen(persons: Persons) {
        TODO("Not yet implemented")
    }

    override fun showDetailMovieScreen(movie: Movie) {
        TODO("Not yet implemented")
    }

    override fun goBack() {
        onBackPressedDispatcher.onBackPressed()
    }

    override fun goToMenu() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    override fun <T : Parcelable> publishResult(result: T) {
        TODO("Not yet implemented")
    }

    override fun <T : Parcelable> listenResult(
        clazz: Class<T>,
        owner: LifecycleOwner,
        listener: ResultListener<T>
    ) {
        TODO("Not yet implemented")
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            .addToBackStack(null)
            .replace(R.id.container, fragment)
            .commit()
    }
}