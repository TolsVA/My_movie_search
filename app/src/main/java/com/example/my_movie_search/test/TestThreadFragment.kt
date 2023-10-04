package com.example.my_movie_search.test

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.my_movie_search.R
import com.example.my_movie_search.databinding.FragmentTestThreadBinding
import java.lang.Thread.currentThread
import java.util.*
import java.util.concurrent.TimeUnit

const val TEST_BROADCAST_INTENT_FILTER = "TEST BROADCAST INTENT FILTER"
const val THREADS_FRAGMENT_BROADCAST_EXTRA = "THREADS_FRAGMENT_EXTRA"

class TestThreadFragment : Fragment() {
    private var _binding: FragmentTestThreadBinding? = null
    private val binding
        get() = _binding!!

    private var counterThread = 0

    private val handlerThread: HandlerThread by lazy {
        //создаем именованный поток с looper
        HandlerThread(getString(R.string.my_handler_thread))
    }

    companion object {
        fun newInstance() = TestThreadFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTestThreadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        context?.registerReceiver(
//            testReceiver,
//            IntentFilter(TEST_BROADCAST_INTENT_FILTER)
//        )
        context?.let {
            LocalBroadcastManager
                .getInstance(it)
                .registerReceiver(
                    testReceiver,
                    IntentFilter(TEST_BROADCAST_INTENT_FILTER
                    )
                )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // расчет в потоке -> Thread вывод через activity?.runOnUiThread
        calcThreadBtnClick()

        //стартуем его
        handlerThread.start()

        // присоединяем через looper Handler(handlerThread.looper) к потоку handlerThread
        val handler = Handler(handlerThread.looper)

        // расчет в потоке -> Handler(handlerThread.looper) вывод через binding.mainContainer.post
        calcThreadHandlerClick(handlerThread, handler)

        initServiceButton()

        initServiceWithBroadcastButton()
    }

    //Создаём свой BroadcastReceiver (получатель широковещательного сообщения)
    private val testReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            //Достаём данные из интента
            intent.getStringExtra(THREADS_FRAGMENT_BROADCAST_EXTRA)?.let { _text ->
                binding.mainContainer.addView(
                    AppCompatTextView(context).apply {
                        text = _text
                        textSize = resources.getDimension(R.dimen.main_container_text_size)
                    })
            }
        }
    }

    private fun initServiceWithBroadcastButton() {
        binding.serviceWithBroadcastButton.setOnClickListener {
            context?.let {
                it.startService(Intent(it, MainService::class.java).apply {
                    putExtra(
                        MAIN_SERVICE_INT_EXTRA,
                        binding.editText.text.toString()
                    )
                })
            }
        }
    }

    private fun initServiceButton() {
        binding.serviceButton.setOnClickListener {
            context?.let {
                it.startService(
                    Intent(it, MainService::class.java).apply {
                        putExtra(
                            MAIN_SERVICE_STRING_EXTRA,
                            getString(R.string.hello_from_thread_fragment)
                        )
                    }
                )
            }
        }
    }

    private fun calcThreadHandlerClick(handlerThread: HandlerThread, handler: Handler) {

        binding.calcThreadHandler.setOnClickListener {
            binding.mainContainer.addView(
                AppCompatTextView(it.context).apply {
                    text = String.format(
                        getString(R.string.calculate_in_thread),
                        handlerThread.name
                    )
                    textSize = resources.getDimension(R.dimen.main_container_text_size)
                })

            handler.post {
                val result = startCalculations(binding.editText.text.toString().toInt())
                // передаем данные в mainContainer через собственный handler
                binding.mainContainer.post {
                    binding.mainContainer.addView(
                        AppCompatTextView(it.context).apply {
                            text = String.format("${currentThread().name} - $result ")
                            textSize = resources.getDimension(R.dimen.main_container_text_size)
                        })
                }
            }
        }
    }

    private fun calcThreadBtnClick() {
        binding.calcThreadBtn.setOnClickListener {
            Thread {
                counterThread++
                val calculatedText = startCalculations(
                    binding.editText.text.toString().toInt()
                )
                activity?.runOnUiThread {
                    binding.textView.text = calculatedText
                    binding.mainContainer.addView(AppCompatTextView(it.context).apply {
                        text = String.format(
                            getString(R.string.from_thread),
                            counterThread
                        )
                        textSize = resources.getDimension(R.dimen.main_container_text_size)
                    })
                }
            }.start()
        }
    }

    private fun startCalculations(seconds: Int): String {
        val date = Date()
        var diffInSec: Long

        do {
            val currentDate = Date()
            val diffInMs: Long = currentDate.time - date.time
            diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs)
        } while (diffInSec < seconds)

        return diffInSec.toString()
    }

    override fun onDestroy() {
//        context?.unregisterReceiver(testReceiver)
        context?.let {
            LocalBroadcastManager
                .getInstance(it)
                .unregisterReceiver(testReceiver)
        }

        super.onDestroy()
        _binding = null
        handlerThread.looper.quit()
    }
}