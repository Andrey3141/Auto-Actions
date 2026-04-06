package com.printer.information

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout

class MaterialTextInputReplica @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    lateinit var editText: EditText
        private set
    private lateinit var startIconView: ImageView
    private lateinit var endIconView: ImageView
    private lateinit var boxBackground: RelativeLayout

    init {
        LayoutInflater.from(context).inflate(R.layout.custom_text_input, this, true)

        editText = findViewById(R.id.etPhoneNumber)
        startIconView = findViewById(R.id.startIcon)
        endIconView = findViewById(R.id.endIcon)
        boxBackground = findViewById(R.id.boxBackground)

        startIconView.setColorFilter(0xFF6200EE.toInt())
        endIconView.setColorFilter(0xFF6200EE.toInt())

        endIconView.setOnClickListener {
            editText.text.clear()
            editText.requestFocus()
        }

        editText.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                val hasText = !s.isNullOrEmpty()
                endIconView.visibility = if (hasText) View.VISIBLE else View.GONE
            }
        })

        editText.setOnFocusChangeListener { _, hasFocus ->
            boxBackground.isSelected = hasFocus
        }
    }

    fun getText(): String = editText.text.toString()
}