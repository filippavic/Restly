package com.fer.ppj.restly

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.sql.Timestamp


class MainActivity : AppCompatActivity() {

    var isFragmentHealthLoaded = true
    var manager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_health -> {
                    ShowFragmentHealth()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_home -> {
                    ShowFragmentHome()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_history -> {
                    ShowFragmentHistory()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    fun ShowFragmentHealth() {
        val transaction = manager.beginTransaction()
        val fragment = HealthFragment()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
        isFragmentHealthLoaded = true
    }

    fun ShowFragmentHome() {
        val transaction = manager.beginTransaction()
        val fragment = HomeFragment()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
        isFragmentHealthLoaded = false
    }

    fun ShowFragmentHistory() {
        val transaction = manager.beginTransaction()
        val fragment = HistoryFragment()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
        isFragmentHealthLoaded = false
    }



 

}