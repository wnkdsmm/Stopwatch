package com.hfad.stopwatch

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.hfad.stopwatch.databinding.ActivityMainBinding
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer

class MainActivity : AppCompatActivity() {
    // управление секундомером
    lateinit var stopwatch: Chronometer // Хронометр
    var running = false // хронометр работает?
    var offset: Long = 0 // базовое смещение

    // добавление строк для ключей, используемых с Bundle
    val OFFSET_KEY = "offset"
    val RUNNING_KEY = "running"
    val BASE_KEY = "base"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // получение ссылки на секундомер
        stopwatch = findViewById<Chronometer>(R.id.stopwatch)

        // восстановление предыдущего состояния
        if (savedInstanceState != null) {
            offset = savedInstanceState.getLong(OFFSET_KEY)
            running = savedInstanceState.getBoolean(RUNNING_KEY)
            if (running) {
                stopwatch.base = savedInstanceState.getLong(BASE_KEY)
                stopwatch.start()
            } else setBaseTime()
        }

        // кнопка запускает секундомер, если он не работал
        val startButton = findViewById<Button>(R.id.start_button)
        startButton.setOnClickListener{
            if (!running) {
                setBaseTime()
                stopwatch.start()
                running=true
            }
        }
        // кнопка останавливает секундомер, если он работал
        val pauseButton = findViewById<Button>(R.id.pause_button)
        pauseButton.setOnClickListener {
            // сохранить время на секундомере и оставить его
            if (running) {
                saveOffset()
                stopwatch.stop()
                running=false
            }
        }
        // кнопка обнуляет offset и базовое время
        val resetButton = findViewById<Button>(R.id.reset_button)
        resetButton.setOnClickListener {
            offset = 0
            setBaseTime() // обнулить показания секундомера
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putLong(OFFSET_KEY, offset)
        savedInstanceState.putBoolean(RUNNING_KEY, running)
        savedInstanceState.putLong(BASE_KEY, stopwatch.base)

    }

    private fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - stopwatch.base
    }

    private fun setBaseTime() {
        stopwatch.base = SystemClock.elapsedRealtime() - offset
    }

    override fun onStop() {
        super.onStop()
        //код, выполняемый при остановке активности
        if (running) {
            saveOffset()
            stopwatch.stop()
        }
    }
    override fun onStart() {
        super.onStart()
        //код, выполняемый при запуске активности
    }

    override fun onRestart() {
        super.onRestart()
        //код, выполняемый при перезапуске активности
        if (running) {
            setBaseTime()
            stopwatch.start()
            offset=0
        }
    }


}