package com.vanshika.tasktrack

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.vanshika.tasktrack.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var binding : ActivityMainBinding ?= null
    var navController : NavController ?= null
    var appBarConfiguration : AppBarConfiguration ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPref = getSharedPreferences(resources.getString(R.string.app_name), MODE_PRIVATE)
        if (sharedPref.getBoolean("dark",false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        navController = findNavController(R.id.host)
       /* appBarConfiguration = navController?.graph?.let {
            AppBarConfiguration(it)
        }*/
        appBarConfiguration = AppBarConfiguration(setOf(R.id.mainFragment, R.id.calendarFragment, R.id.profileFragment, R.id.progressFragment, R.id.rewardAddedFragment))
        setupActionBarWithNavController(navController!!,appBarConfiguration!!)
        binding?.bottomNavigation?.setOnItemSelectedListener {
            when(it.itemId){
                R.id.navTasks -> navController?.navigate(R.id.mainFragment)
                R.id.navCalendar -> navController?.navigate(R.id.calendarFragment)
                R.id.navProfile -> navController?.navigate(R.id.profileFragment)
                R.id.navProgress -> navController?.navigate(R.id.progressFragment)
            }
            return@setOnItemSelectedListener true
        }
        navController?.addOnDestinationChangedListener{navController, destination, arguments ->
            when(destination.id){
                R.id.mainFragment ->{
                    supportActionBar?.title = resources.getString(R.string.all_tasks)
                    binding?.bottomNavigation?.menu?.findItem(R.id.navTasks)?.isChecked = true
                }
                R.id.calendarFragment ->{
                    supportActionBar?.title = resources.getString(R.string.calendar)
                    binding?.bottomNavigation?.menu?.findItem(R.id.navCalendar)?.isChecked = true
                }
                R.id.progressFragment ->{
                    supportActionBar?.title = resources.getString(R.string.progress)
                    binding?.bottomNavigation?.menu?.findItem(R.id.navProgress)?.isChecked = true
                }
                R.id.profileFragment ->{
                    supportActionBar?.title = resources.getString(R.string.profile)
                    binding?.bottomNavigation?.menu?.findItem(R.id.navProfile)?.isChecked = true
                }
                R.id.taskAdditionFragment ->{
                    supportActionBar?.title = resources.getString(R.string.add_new_tasks)
                }
                R.id.taskUpdationFragment ->{
                    supportActionBar?.title = resources.getString(R.string.update_task)
                }
                R.id.categoryFragment ->{
                    supportActionBar?.title = resources.getString(R.string.add_new_categories)
                }
                R.id.rewardsFragment ->{
                    supportActionBar?.title = resources.getString(R.string.rewards)
                }
                R.id.rewardAddedFragment ->{
                    supportActionBar?.title = resources.getString(R.string.rewards_added)
                }
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() || navController!!.popBackStack()
    }
}
