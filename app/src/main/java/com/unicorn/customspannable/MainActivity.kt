package com.unicorn.customspannable

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val content1 = MySpannableString(this, "前往下一步即表示您已阅读并接受Booking.com之条款及细则和隐私条款")
            .first("条款及细则").size(25).onClick(tvContent) { showToast(this, "条款及细则") }.textColor(R.color.color_main)
            .underline()
            .first("隐私条款").size(25).onClick(tvContent) { showToast(this, "隐私条款") }.textColor(R.color.color_main)
            .underline()
            .first("下一步").strikethrough().bold().scaleSize(2)

        tvContent.text = content1


        val content2 = MySpannableString(this, "Booking.com之条款及细则和隐私条款")
            .bullet(40, R.color.black)
        textView.text = content2
    }

    fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

}
