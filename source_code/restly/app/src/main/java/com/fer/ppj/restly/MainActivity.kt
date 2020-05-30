package com.fer.ppj.restly

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var manager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFragments()
        showFragmentHome()

        navigationView.selectedItemId = R.id.navigation_home

        val mOnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navigation_health -> {
                        showFragmentHealth()
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_home -> {
                        showFragmentHome()
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_history -> {
                        showFragmentHistory()
                        return@OnNavigationItemSelectedListener true
                    }
                }
                false
            }

        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Izlazak iz aplikacije")
        builder.setMessage("Jeste li sigurni da želite izaći iz aplikacije?")

        builder.setPositiveButton("Da") { _, _ ->
            finish()
        }

        builder.setNegativeButton("Ne") { _, _ ->
        }

        builder.show()
    }

    private fun initFragments() {
        val transaction = manager.beginTransaction()
        transaction.add(R.id.container, HealthFragment(), "health")
        transaction.add(R.id.container, HistoryFragment(), "history")
        transaction.add(R.id.container, HomeFragment(), "home")
        transaction.commitNow()
    }

    private fun showFragmentHealth() {
        val transaction = manager.beginTransaction()
        manager.findFragmentByTag("home")?.let { transaction.hide(it) }
        manager.findFragmentByTag("history")?.let { transaction.hide(it) }
        manager.findFragmentByTag("health")?.let { transaction.show(it) }
        transaction.commit()
    }

    private fun showFragmentHome() {
        val transaction = manager.beginTransaction()
        manager.findFragmentByTag("health")?.let { transaction.hide(it) }
        manager.findFragmentByTag("history")?.let { transaction.hide(it) }
        manager.findFragmentByTag("home")?.let { transaction.remove(it) }
        transaction.add(R.id.container, HomeFragment(), "home")
        transaction.commit()
    }

    private fun showFragmentHistory() {
        val transaction = manager.beginTransaction()
        manager.findFragmentByTag("home")?.let { transaction.hide(it) }
        manager.findFragmentByTag("health")?.let { transaction.hide(it) }
        manager.findFragmentByTag("history")?.let { transaction.remove(it) }
        transaction.add(R.id.container, HistoryFragment(), "history")
        transaction.commit()
    }

}