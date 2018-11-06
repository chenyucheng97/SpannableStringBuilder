# SpannableStringBuilder
参考自：https://github.com/jaychang0917/SimpleText

&nbsp;

Kotlin编写，封装常用的Span，简化SpannableString的编写

&nbsp;

⚠️注意：由于SpannableString同时设置了ClickableSpan和ForegroundColorSpan后，发现ForegroundColorSpan不生效，
所以先设置点击事件onClick（即先设置ClickableSpan），再设置textColor (即ForegroundColorSpan）

&nbsp;
### 调用比较简单：

``` kotlin
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
```

其中first（“XXX”）代表第一个出现的目标子串，后续的onClick() size()等都是对该目标子串的设置

![效果图：](https://github.com/chenyucheng97/SpannableStringBuilder/blob/master/app/WechatIMG195.jpeg)



### 支持的Span：
&nbsp;
- AbsoluteSizeSpan
- RelativeSizeSpan
- StyleSpan
- TypefaceSpan
- StrikethroughSpan
- UnderlineSpan
- BulletSpan
- ForegroundColorSpan
- SubscriptSpan
- SuperscriptSpan
- ClickableSpan

