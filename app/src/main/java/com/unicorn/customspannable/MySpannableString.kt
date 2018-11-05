package com.unicorn.customspannable

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.view.View
import android.widget.TextView


/**
 * Created by cyc on 2018/11/5.
 */
class MySpannableString(private val context: Context, private val text: CharSequence) : SpannableString(text) {


    private val spanMode = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    private val rangeList = mutableListOf<Pair<Int, Int>>()

    private var textColor: Int = 0

    init {
        rangeList.add(Pair(0, text.length))
    }


    /**
     *  匹配出现的第一个目标子串，并记录开始和结束的index
     */
    fun first(target: String): MySpannableString {
        rangeList.clear()
        val index = toString().indexOf(target)
        val range = Pair(index, index + target.length)
        rangeList.add(range)
        return this
    }

    /**
     *  匹配出现的最后一个目标子串，并记录开始和结束的index
     */
    fun last(target: String): MySpannableString {
        rangeList.clear()
        val index = toString().lastIndexOf(target)
        val range = Pair(index, index + target.length)
        rangeList.add(range)
        return this
    }

    /**
     *   匹配出现的所有目标子串，并记录开始和结束的index
     */
    fun all(target: String): MySpannableString {
        rangeList.clear()
        val indexes = indexesOf(toString(), target)
        for (index in indexes) {
            val range = Pair(index, index + target.length)
            rangeList.add(range)
        }
        return this
    }

    /**
     *  记录src中目标子串出现的位置
     */
    fun indexesOf(src: String, target: String): MutableList<Int> {
        val positions = mutableListOf<Int>()
        var index = src.indexOf(target)
        while (index >= 0) {
            positions.add(index)
            index = src.indexOf(target, index + 1)
        }
        return positions
    }

    /**
     * 手动输入一个起点和终点的index
     */
    fun range(from: Int, to: Int): MySpannableString {
        rangeList.clear()
        val range = Pair(from, to + 1)
        rangeList.add(range)
        return this
    }

    /**
     * 手动输入所有起点和终点的index
     */
    fun ranges(ranges: MutableList<Pair<Int, Int>>): MySpannableString {
        rangeList.clear()
        rangeList.addAll(ranges)
        return this
    }

    /**
     * 手动输入起点和终点的子串
     */
    fun between(startText: String, endText: String): MySpannableString {
        rangeList.clear()
        val startIndex = toString().indexOf(startText) + startText.length + 1
        val endIndex = toString().lastIndexOf(endText) - 1
        val range = Pair(startIndex, endIndex)
        rangeList.add(range)
        return this
    }

    fun size(dp: Int): MySpannableString {
        for (range in rangeList) {
            setSpan(AbsoluteSizeSpan(dp, true), range.first, range.second, spanMode)
        }
        return this
    }

    fun scaleSize(proportion: Int): MySpannableString {
        for (range in rangeList) {
            setSpan(RelativeSizeSpan(proportion.toFloat()), range.first, range.second, spanMode)
        }
        return this
    }

    fun bold(): MySpannableString {
        for (range in rangeList) {
            setSpan(StyleSpan(Typeface.BOLD), range.first, range.second, spanMode)
        }
        return this
    }

    fun italic(): MySpannableString {
        for (range in rangeList) {
            setSpan(StyleSpan(Typeface.ITALIC), range.first, range.second, spanMode)
        }
        return this
    }

    fun normal(): MySpannableString {
        for (range in rangeList) {
            setSpan(StyleSpan(Typeface.NORMAL), range.first, range.second, spanMode)
        }
        return this
    }

    fun font(font: String): MySpannableString {
        for (range in rangeList) {
            setSpan(TypefaceSpan(font), range.first, range.second, spanMode)
        }
        return this
    }

    fun strikethrough(): MySpannableString {
        for (range in rangeList) {
            setSpan(StrikethroughSpan(), range.first, range.second, spanMode)
        }
        return this
    }

    fun underline(): MySpannableString {
        for (range in rangeList) {
            setSpan(UnderlineSpan(), range.first, range.second, spanMode)
        }
        return this
    }

    fun bullet(dp: Int, @ColorRes colorRes: Int?): MySpannableString {
        for (range in rangeList) {
            setSpan(BulletSpan(dp, colorRes ?: textColor), range.first, range.second, spanMode)
        }
        return this
    }


    fun textColor(@ColorRes colorRes: Int): MySpannableString {
        textColor = ContextCompat.getColor(context, colorRes)
        for (range in rangeList) {
            setSpan(ForegroundColorSpan(textColor), range.first, range.second, spanMode)
        }
        return this
    }


    fun subscript(): MySpannableString {
        for (range in rangeList) {
            setSpan(SubscriptSpan(), range.first, range.second, spanMode)
        }
        return this
    }

    fun superscript(): MySpannableString {
        for (range in rangeList) {
            setSpan(SuperscriptSpan(), range.first, range.second, spanMode)
        }
        return this
    }


    fun onClick(textView: TextView, onTextClickListener: () -> Unit): MySpannableString {
        for (range in rangeList) {
            val span = object : ClickableSpan() {
                override fun onClick(widget: View?) {
                    onTextClickListener.invoke()
                }
            }
            setSpan(span, range.first, range.second, spanMode)
        }

        textView.highlightColor = Color.TRANSPARENT
        textView.movementMethod = LinkMovementMethod.getInstance()
        return this
    }

}