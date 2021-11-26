package com.taipt.runningapp.other

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.taipt.runningapp.R
import com.taipt.runningapp.db.Run
import java.text.SimpleDateFormat
import java.util.*

class CustomMarkerView : MarkerView {

    val runs: List<Run>

    private var tvDate: TextView
    private var tvDuration: TextView
    private var tvAvgSpeed: TextView
    private var tvDistance: TextView
    private var tvCaloriesBurned: TextView

    constructor(runs: List<Run>, context: Context, layoutId: Int) : super(context, layoutId) {
        this.runs = runs
        this.tvDate = findViewById(R.id.tvDate)
        this.tvDuration = findViewById(R.id.tvDuration)
        this.tvAvgSpeed = findViewById(R.id.tvAvgSpeed)
        this.tvDistance = findViewById(R.id.tvDistance)
        this.tvCaloriesBurned = findViewById(R.id.tvCaloriesBurned)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-width / 2f, -height.toFloat())
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)
        if (e == null) {
            return
        }
        val currentRunId = e.x.toInt()
        val run = runs[currentRunId]

        val calender = Calendar.getInstance().apply {
            timeInMillis = run.timestamp
        }
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        tvDate.text = dateFormat.format(calender.time)

        val avgSpeed = "${run.avgSpeedInKMH}km/H"
        tvAvgSpeed.text = avgSpeed

        val distanceInKm = "${run.distanceInMeters / 1000f}km"
        tvDistance.text = distanceInKm

        tvDuration.text = TrackingUtility.getFormattedStopWatchTime(run.timeInMillis)

        val caloriesBurned = "${run.caloriesBurned}kcal"
        tvCaloriesBurned.text = caloriesBurned
    }
}