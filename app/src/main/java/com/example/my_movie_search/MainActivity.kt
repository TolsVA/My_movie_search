package com.example.my_movie_search

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private val listName = listOf("Пётя", "Федя", "Вова", "Коля", "Маша", "Миша", "Сергей")
    private var user: User? = null
    private var tvCount: TextView? = null
    private var name: TextView? = null
    private var photo: ImageView? = null
    private var count: Int = 0

    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        name = findViewById(R.id.tv_name)

        photo = findViewById(R.id.photo)

        tvCount = findViewById(R.id.tv_count)

        val bt = findViewById<Button>(R.id.bt_add)
        bt.setOnClickListener {
            val fragment = MyDialogFragmentImageView.newInstance()
            fragment.show(supportFragmentManager, MyDialogFragmentImageView.TAG)
        }

        val btStart = findViewById<Button>(R.id.bt_count)
        btStart.setOnClickListener {
            if (btStart.text == resources.getString(R.string.start)) {
                startStop = true
                startTimer(tvCount!!)
                btStart.text = resources.getString(R.string.stop)
            } else {
                startStop = false
                btStart.text = resources.getString(R.string.start)
                tvCount?.text = count.toString()
            }
        }

        val btAge = findViewById<Button>(R.id.bt_age)

        btAge.setOnClickListener {
            if (user != null) {
                if (name?.text.toString().startsWith("Имя: Unknown person")) {
                    photo?.setImageResource(user!!.imageId)
                    name?.text = collectText(user!!)
                    btAge.text = getString(R.string.change)
                } else {
                    val userCopy = user!!.copy()
                    userCopy.age = user!!.age + 2
                    userCopy.title = "Unknown person"
                    val icons = resources.obtainTypedArray(R.array.avatar)
                    val resourceId = icons.getResourceId(icons.length() - 1, 0)
                    userCopy.imageId = resourceId
                    photo?.setImageResource(resourceId)
                    name?.text = collectText(userCopy)
                    btAge.text = getString(R.string.return_user)
                }
            } else {
                Toast.makeText(this, "null", Toast.LENGTH_SHORT).show()
            }
        }
        val tvNameChange = findViewById<TextView>(R.id.tv_name_change)
        val btName = findViewById<Button>(R.id.bt_name_change)
        btName.setOnClickListener {
            val handler = Handler()
            Thread {
                for ((index, name) in listName.withIndex()) {
                    Thread.sleep(500)
                    count++
                    handler.post {
                        tvNameChange.text = name
                        tvCount!!.text = index.toString()
                    }
                }
            }.start()

        }
    }

    private fun collectText(user: User): CharSequence = "Имя: ${user.title} -> age: ${user.age}"

    fun create(user: User) {
        this.user = user
        photo?.setImageResource(user.imageId)
        name?.text = collectText(user)
    }

    private fun startTimer(tvCount: TextView) {
        val handler = Handler()
        Thread {
            while (startStop) {
                Thread.sleep(100)
                count++
                handler.post { tvCount.text = count.toString() }
            }
        }.start()
    }

    companion object {
        var startStop = true
    }
}