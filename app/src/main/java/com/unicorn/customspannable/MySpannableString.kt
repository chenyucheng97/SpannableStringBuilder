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
 *
 * 常用Span封装，不支持点击时文字颜色和背景色的设置（使用场景少，暂不添加）
 * 支持设置字体颜色、大小、下划线、删除线、点击事件 等，
 * 可以对一个字符串中的多个目标子串进行设置Span
 *
 */
class MySpannableString(private val context: Context,text: CharSequence) : SpannableString(text) {

    @PublishedApi
    internal val spanMode = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    // 初始时，待处理的索引范围为全部字符串
    @PublishedApi
    internal val rangeList = mutableListOf(Pair(0, text.length))
    private var textColor: Int = 0


    /**
     *  匹配出现的第一个目标子串[target]，并记录开始和结束的index
     */
    fun first(target: String) = apply {
        rangeList.clear()
        val index = toString().indexOf(target)
        val range = Pair(index, index + target.length)
        rangeList.add(range)
    }

    /**
     *  匹配出现的最后一个目标子串[target]，并记录开始和结束的index
     */
    fun last(target: String) = apply {
        rangeList.clear()
        val index = toString().lastIndexOf(target)
        val range = Pair(index, index + target.length)
        rangeList.add(range)
    }

    /**
     *   匹配出现的所有目标子串[target]，并记录开始和结束的index
     */
    fun all(target: String) = apply {
        rangeList.clear()
        val indexes = indexesOf(toString(), target)
        indexes.map { rangeList.add(Pair(it, it + target.length)) }
    }

    /**
     *  记录源字符串[src]中目标子串 [target]出现的索引位置
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
     * 手动输入一个起点索引[from]和终点索引[to]
     */
    fun range(from: Int, to: Int) = apply {
        rangeList.clear()
        val range = Pair(from, to + 1)
        rangeList.add(range)
    }

    /**
     * 手动输入所有起点和终点的索引范围[ranges]
     */
    fun ranges(ranges: MutableList<Pair<Int, Int>>)= apply {
        rangeList.clear()
        rangeList.addAll(ranges)
    }

    /**
     * 计算两个字符串[startText] 和 [endText]之间的字符串的索引，加入到待处理的集合中，后续的Span设置都是对该索引范围内的字串进行的
     */
    fun between(startText: String, endText: String) = apply {
        rangeList.clear()
        val startIndex = toString().indexOf(startText) + startText.length + 1
        val endIndex = toString().lastIndexOf(endText) - 1
        val range = Pair(startIndex, endIndex)
        rangeList.add(range)
    }

    /**
     * 给target字串设置文字绝对大小为[dp]
     */
    fun size(dp: Int) = applySpan(AbsoluteSizeSpan(dp, true))

    /**
     * 给target字串设置文字相对大小，指相对于文本设定的大小的相对比例为[proportion]
     */
    fun scaleSize(proportion: Int) = applySpan(RelativeSizeSpan(proportion.toFloat()))

    /**
     * 给target字串设置样式（粗体）
     */
    fun bold() = applySpan(Typeface.BOLD)

    /**
     * 给target字串设置样式（斜体）
     */
    fun italic() = applySpan(Typeface.ITALIC)

    /**
     * 给target字串设置样式（正常）
     */
    fun normal() = applySpan(Typeface.NORMAL)

    /**
     * 给target字串设置样式（粗斜体）
     */
    fun bold_italic() = applySpan(Typeface.BOLD_ITALIC)

    private fun MySpannableString.applySpan(span: Int) = apply {
        rangeList.map { setSpan(StyleSpan(span), it.first, it.second, spanMode) }
    }

    public fun MySpannableString.applySpan(span: Any) = apply {
        rangeList.map { setSpan(span, it.first, it.second, spanMode) }
    }

    /**
     * 字体样式，可以设置不同的字体，比如系统自带的SANS_SERIF、MONOSPACE和SERIF
     */
    fun font(font: String) = applySpan(TypefaceSpan(font))

    /**
     * 给target字串添加删除线
     */
    fun strikethrough() = applySpan(StrikethroughSpan())

    /**
     * 给target字串添加下划线
     */
    fun underline() = applySpan(UnderlineSpan())

    /**
     * 类似于HTML中的<li>标签的圆点效果,[dp]表示圆点和字体的间距，[colorRes]表示圆点的颜色
     */
    fun bullet(dp: Int, @ColorRes colorRes: Int?) = applySpan(BulletSpan(dp, colorRes ?: textColor))

    /**
     * 字体颜色 [colorRes]表示target字串的字体颜色
     */
    fun textColor(@ColorRes colorRes: Int) = applySpan(ForegroundColorSpan(ContextCompat.getColor(context, colorRes)))

    /**
     * 将target字串作为下标
     */
    fun subscript() = applySpan(SubscriptSpan())

    /**
     * 将target字串作为上标
     */
    fun superscript() = applySpan(SuperscriptSpan())

    /**
     * 给[textView]设置一个点击事件[onTextClickListener]
     */
    inline fun onClick(textView: TextView, crossinline onTextClickListener: () -> Unit) = apply {
        val span = object : ClickableSpan() {
            override fun onClick(widget: View?) {
                onTextClickListener.invoke()
            }
        }
        applySpan(span)

        textView.highlightColor = Color.TRANSPARENT
        textView.movementMethod = LinkMovementMethod.getInstance()
    }

}