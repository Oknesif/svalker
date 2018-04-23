package com.zzzombiecoder.codegenapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.zzzombiecoder.code.generator.Code
import com.zzzombiecoder.code.generator.generateCode
import net.glxn.qrgen.android.QRCode

class CodeGenActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var textCodeView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById(R.id.image_view)
        textCodeView = findViewById(R.id.text_code_view)

        findViewById<View>(R.id.vodka_button).setClickListener(Code.VODKA)
        findViewById<View>(R.id.heal_button).setClickListener(Code.HEAL)
        findViewById<View>(R.id.antirad_button).setClickListener(Code.ANTI_RAD)
        findViewById<View>(R.id.psi_button).setClickListener(Code.PSY_BLOCK)
        findViewById<View>(R.id.enable_button).setClickListener(Code.ACTIVATE_RECEIVER)
        findViewById<View>(R.id.disable_button).setClickListener(Code.DEACTIVATE_RECEIVER)

    }

    private fun View.setClickListener(code: Code) {
        this.setOnClickListener {
            val stringCode =  code.char.generateCode()
            val qrBitmap = QRCode.from(stringCode).bitmap()
            textCodeView.text = stringCode
            imageView.setImageBitmap(qrBitmap)
        }
    }
}
