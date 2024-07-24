package com.artilearn.happygolfgo.ui.home.record.model

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.artilearn.happygolfgo.R
import com.artilearn.happygolfgo.data.exam.golfpower.GolfPower
import com.artilearn.happygolfgo.util.ext.dp
import java.lang.Math.*
//
//enum class CharacteristicType(val value: String) {
//    putting("Putting力"),
//    iron("Iron力"),
//    s("S/G力"),
//    driver("Driver力"),
//    w("W/U力")
//}
//
//data class RadarChartData(
//    val type: CharacteristicType,
//    val value: Int
//)

class RadarChartView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var dataList: List<GolfPower>? = null

    private val paint = Paint().apply {
        isAntiAlias = true
    }
    private val textPaint = TextPaint().apply {
        textSize = 14.dp()
        textAlign = Paint.Align.CENTER
        color = getContext().getColor(R.color.color_black)
    }
    private val subTextPaint = TextPaint().apply {
        textSize = 12.dp()
        textAlign = Paint.Align.CENTER
        color = getContext().getColor(R.color.gray500)
    }
    private var path = Path()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return
        dataList ?: return

        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        val radian = PI.toFloat() * 2 / 5 // 360도를 5분할한 각만큼 회전시키 위해
        val step = 5 // 데이터 가이드 라인은 5단계로 표시한다
        val heightMaxValue = height / 2 * 0.7f // RadarChartView영역내에 모든 그림이 그려지도록 max value가 그려질 높이
        val heightStep = heightMaxValue / step // 1단계에 해당하는 높이
        val cx = width / 2f
        val cy = height / 2f
        // 1. 단계별 가이드라인(5각형) 그리기
        for (i in 0..step) {
            var startX = cx
            var startY = (cy - heightMaxValue) + heightStep * i
            repeat(dataList!!.size) {
                // 중심좌표를 기준으로 점(startX,startY)를 radian만큼씩 회전시킨 점(stopX, stopY)을 계산한다.
                val stopPoint = transformRotate(radian, startX, startY, cx, cy)
                val path = Path()
                path.moveTo(startX, startY)
                path.lineTo(stopPoint.x, stopPoint.y)
                val dashPattern = floatArrayOf(5f, 5f) // 첫 번째 값은 실선의 길이, 두 번째 값은 공백의 길이
                paint.pathEffect = DashPathEffect(dashPattern, 0f)
                canvas.drawPath(path, paint)
                startX = stopPoint.x
                startY = stopPoint.y
            }
        }

        // 2. 중심으로부터 5각형의 각 꼭지점까지 잇는 라인 그리기
        var startX = cx
        var startY = cy - heightMaxValue
        repeat(dataList!!.size) {
            val stopPoint = transformRotate(radian, startX, startY, cx, cy)
            canvas.drawLine(cx, cy, stopPoint.x, stopPoint.y, paint)

            startX = stopPoint.x
            startY = stopPoint.y
        }

        // 3. 각 꼭지점 부근에 각 특성 문자열 표시하기
        startX = cx
        startY = (cy - heightMaxValue) * 0.7f
        var r = 0f
        path.reset()
        dataList!!.forEachIndexed { index, golfPower ->
            subTextPaint.textAlign = Paint.Align.CENTER
            val point = transformRotate(r, startX, startY, cx, cy)
            canvas.drawText(
                (dataList?.get(index)?.score ?: 0).toString(),
                point.x,
                textPaint.fontMetrics.getBaseLine(point.y),
                textPaint
            )
            when(index){
                0 -> {
                    canvas.drawText(
                        golfPower.title,
                        point.x,
                        subTextPaint.fontMetrics.getBaseLine(point.y - 50),
                        subTextPaint
                    )
                }
                1,2 -> {
                    subTextPaint.textAlign = Paint.Align.LEFT
                    canvas.drawText(
                        golfPower.title,
                        point.x-25,
                        subTextPaint.fontMetrics.getBaseLine(point.y + 50),
                        subTextPaint
                    )
                }
                else -> {
                    subTextPaint.textAlign = Paint.Align.RIGHT
                    canvas.drawText(
                        golfPower.title,
                        point.x+25,
                        subTextPaint.fontMetrics.getBaseLine(point.y + 50),
                        subTextPaint
                    )
                }
            }
            // 전달된 데이터를 표시하는 path 계산
            dataList!!.firstOrNull { it.title == golfPower.title }?.score?.let { value ->
                val conValue = heightMaxValue * value / 100 // 차트크기에 맞게 변환
                val valuePoint = transformRotate(r, startX, cy - conValue, cx, cy)
                if (path.isEmpty) {
                    path.moveTo(valuePoint.x, valuePoint.y)
                } else {
                    path.lineTo(valuePoint.x, valuePoint.y)
                }
            }

            r += radian
        }

        // 4. 전달된 데에터를 표시하기
        path.close()
        paint.color = 0x3326A9E0
        paint.style = Paint.Style.FILL
        canvas.drawPath(path, paint)


        // 동그라미를 그릴 때 사용할 Paint 설정
        paint.color = context.getColor(R.color.blue500)
        paint.style = Paint.Style.FILL
        val circleRadius = 8f

        // 선을 그릴 때 사용할 Paint 설정
        val linePaint = Paint()
        linePaint.color = context.getColor(R.color.blue500)
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeWidth = 1.dp.toFloat()

        // 각 꼭지점을 연결하는 선을 그리는 Path 객체
        val linePath = Path()

        // 전달된 데이터의 각 포인트에 대해서 원 그리기 및 선 그리기
        dataList!!.forEachIndexed { index, golfPower ->
            dataList!!.firstOrNull { it.title == golfPower.title }?.score?.let { value ->
                val conValue = heightMaxValue * value / 100 // 차트크기에 맞게 변환
                val valuePoint = transformRotate(radian * index, startX, cy - conValue, cx, cy)
                canvas.drawCircle(valuePoint.x, valuePoint.y, circleRadius, paint) // 원 그리기

                // 선을 그리기 위한 Path 설정
                if (linePath.isEmpty) {
                    linePath.moveTo(valuePoint.x, valuePoint.y)
                } else {
                    linePath.lineTo(valuePoint.x, valuePoint.y)
                }
            }
        }
        linePath.close() // Path를 닫아 처음과 마지막 꼭지점 연결
        canvas.drawPath(linePath, linePaint) // 선 그리기
    }

    fun setDataList(dataList: List<GolfPower>) {
        if (dataList.isEmpty()) {
            return
        }
        this.dataList = dataList
        invalidate()
    }

    // 점(x, y)를 특정 좌표(cx, cy)를 중심으로 radian만큼 회전시킨 점의 좌표를 반환
    private fun transformRotate(radian: Float, x: Float, y: Float, cx: Float, cy: Float): PointF {
        val stopX = cos(radian.toDouble()) * (x - cx) - sin(radian.toDouble()) * (y - cy) + cx
        val stopY = sin(radian.toDouble()) * (x - cx) + cos(radian.toDouble()) * (y - cy) + cy

        return PointF(stopX.toFloat(), stopY.toFloat())
    }
}

// y좌표가 중심이 오도록 문자열을 그릴수 있도록하는 baseline좌표를 반환
fun Paint.FontMetrics.getBaseLine(y: Float): Float {
    val halfTextAreaHeight = (bottom - top) / 2
    return y - halfTextAreaHeight - top
}