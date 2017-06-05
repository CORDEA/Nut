package jp.cordea.nut

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner

class MainActivity : AppCompatActivity() {

    private val sender = NotificationSender(this)

    private val spinner by lazy {
        findViewById(R.id.spinner) as Spinner
    }

    private val titleEditText by lazy {
        findViewById(R.id.title_edit_text) as EditText
    }

    private val textEditText by lazy {
        findViewById(R.id.text_edit_text) as EditText
    }

    private val fab by lazy {
        findViewById(R.id.fab) as FloatingActionButton
    }

    private val toolbar by lazy {
        findViewById(R.id.toolbar) as Toolbar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                arrayListOf(
                        getString(R.string.notification_type_simple),
                        getString(R.string.notification_type_action),
                        getString(R.string.notification_type_custom)
                ))
        spinner.adapter = adapter

        fab.setOnClickListener {
            val type = NotificationType
                    .valueOf((spinner.selectedItem as String).toUpperCase())

            titleEditText.text.notBlank { title ->
                textEditText.text.notBlank { text ->
                    sender
                            .apply {
                                this.title = title.toString()
                                this.text = text.toString()
                            }
                            .send(type)
                }
            }
        }
    }

    private inline fun CharSequence.notBlank(block: (CharSequence) -> Unit) {
        if (!this.isBlank()) {
            block(this)
        }
    }
}
