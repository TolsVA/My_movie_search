package com.example.my_movie_search.view

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.example.my_movie_search.R
import com.example.my_movie_search.app.App
import com.example.my_movie_search.contract.CustomAction
import com.example.my_movie_search.contract.HasCustomAction
import com.example.my_movie_search.contract.HasCustomActionBottomNavigation
import com.example.my_movie_search.contract.HasCustomTitle
import com.example.my_movie_search.contract.Navigator
import com.example.my_movie_search.contract.ResultListener
import com.example.my_movie_search.databinding.ActivityMainBinding
import com.example.my_movie_search.model.Movie
import com.example.my_movie_search.model.Persons
import com.example.my_movie_search.utils.hide
import com.example.my_movie_search.utils.show
import com.example.my_movie_search.view.contentprovider.ContentProviderFragment
import com.example.my_movie_search.view.details.DetailMovieFragment
import com.example.my_movie_search.view.details.DetailPersonsFragment
import com.example.my_movie_search.view.main.MainFragment

class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding

    companion object {

        @JvmStatic private val KEY_RESULT = "KEY_RESULT"

        private lateinit var handlerThread: HandlerThread

        fun getHandler(): Handler {
            return Handler(handlerThread.looper)
        }
    }

    private val currentFragment: Fragment
        get() = supportFragmentManager.findFragmentById(R.id.container)!!

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            updateUi()
            updateUiBottomNavigationView()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handlerThread = HandlerThread(
            App.appInstance.getString(R.string.my_handler_thread)
        ).also { it.start() }

        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        setSupportActionBar(binding.toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, MainFragment(), MainFragment.TAG)
                .commit()
        }

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, false)
//        binding.bottomNavigation.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        updateUi()
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        updateUiBottomNavigationView()
        updateUi()
        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
        updateUiBottomNavigationView()
        updateUi()
    }

    private fun updateUi() {
        val fragment = currentFragment

        if (fragment is HasCustomTitle) {
            binding.toolbar.title = "${getString(fragment.getTitleRes())} ${fragment.getTitle()}"
        } else {
            binding.toolbar.title = getString(R.string.movie_search)
        }

        if (supportFragmentManager.backStackEntryCount > 0) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setDisplayShowHomeEnabled(false)
        }

        if (fragment is HasCustomAction) {
            createCustomToolbarAction(fragment.getCustomAction())
        } else {
            binding.toolbar.menu.clear()
        }

        updateUiBottomNavigationView()
    }

    private fun updateUiBottomNavigationView() {
        val fragment = currentFragment

        if (fragment is HasCustomActionBottomNavigation) {
            createCustomActionBottomNavigation(fragment.getCustomActionBottomNavigation())
            binding.bottomNavigation.show()
        } else {
            binding.bottomNavigation.hide()
        }
    }

    private fun createCustomActionBottomNavigation(customActions: MutableList<CustomAction>) {
        binding.bottomNavigation.menu.clear()

        for (action in customActions) {
            val iconDrawable =
                DrawableCompat.wrap(ContextCompat.getDrawable(this, action.iconRes)!!)
            iconDrawable.setTint(Color.WHITE)

            val menuItem = binding.bottomNavigation.menu.add(action.textRes)
            menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            menuItem.icon = iconDrawable

            menuItem.setOnMenuItemClickListener {
                action.onCustomAction.run()
                menuItem.isChecked = true
                return@setOnMenuItemClickListener true
            }
        }
    }

    private fun createCustomToolbarAction(actions: MutableList<CustomAction>) {
        binding.toolbar.menu.clear()

        for (action in actions) {
            val iconDrawable =
                DrawableCompat.wrap(ContextCompat.getDrawable(this, action.iconRes)!!)
            iconDrawable.setTint(Color.WHITE)

            val menuItem = binding.toolbar.menu.add(action.textRes)
            menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            menuItem.icon = iconDrawable

            menuItem.setOnMenuItemClickListener {
                action.onCustomAction.run()
                return@setOnMenuItemClickListener true
            }
        }
    }

    override fun onDestroy() {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
        handlerThread.looper.quit()
//        App.getDb().close()
        super.onDestroy()

    }

    override fun showDetailPersonsScreen(persons: Persons, tag: String) {
        launchFragment(DetailPersonsFragment.newInstance(persons), tag)
    }

    override fun showDetailMovieScreen(movie: Movie, tag: String) {
        launchFragment(DetailMovieFragment.newInstance(movie), tag)
    }

    override fun showContentProviderFragment(tag: String) {
        launchFragment(ContentProviderFragment.newInstance(), tag)
    }


    override fun goBack() {
        onBackPressedDispatcher.onBackPressed()
        updateUi()
    }

    override fun goToMenu() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    override fun <T : Parcelable> publishResult(result: T) {
        supportFragmentManager.setFragmentResult(
            result.javaClass.name,
            bundleOf(
                KEY_RESULT to result
            )
        )
    }

    @Suppress("DEPRECATION")
    override fun <T : Parcelable> listenResult(
        clazz: Class<T>,
        owner: LifecycleOwner,
        listener: ResultListener<T>
    ) {
        supportFragmentManager.setFragmentResultListener(
            clazz.name,
            owner
        ) { _, bundle ->
            listener.invoke(bundle.getParcelable(KEY_RESULT)!!)
        }
    }

    private fun launchFragment(fragment: Fragment, tag: String) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            .hide(currentFragment)
            .addToBackStack("")
            .add(R.id.container, fragment, tag)
            .commit()
    }
}
