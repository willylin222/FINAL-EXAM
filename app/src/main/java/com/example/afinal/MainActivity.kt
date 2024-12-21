package com.example.afinal

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.example.afinal.databinding.ActivityMainBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class MainActivity : AppCompatActivity() , LocationListener , SensorEventListener {
    private lateinit var bd : ActivityMainBinding
    private lateinit var loc : LocationManager
    private lateinit var ssmg : SensorManager
    private lateinit var mysr : Sensor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bd = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bd.root)

        //---🗺️ 位置資訊
        loc = getSystemService(LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ){
            val LOCATION_PERMISSION_REQUEST_CODE =0
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }else{
            loc.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this)
        }
        loc.requestLocationUpdates(LocationManager.GPS_PROVIDER)

        //---🏋️ 動態監控
        ssmg = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        mysr = ssmg.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) as Sensor


        bd.button.setOnClickListener {
            // 定義步數與活動距離的數據
            val barEntries = ArrayList<BarEntry>().apply {
                add(BarEntry(1f, 222f))   // 星期一：步數
                add(BarEntry(2f, 716f))   // 星期二：步數
                add(BarEntry(3f, 1107f))   // 星期三：步數
                add(BarEntry(4f, 886f))   // 星期四：步數
                add(BarEntry(5f, 996f))   // 星期五：步數
                add(BarEntry(6f, 2024f))  // 星期六：步數
                add(BarEntry(7f, 2003f))  // 星期日：步數
            }

            // 設定數據
            val dataSet = BarDataSet(barEntries, "寵物每日步數")
            dataSet.valueTextSize = 15f //數據文字大小
            dataSet.valueTextColor = Color.BLACK //數據文字顏色
            dataSet.colors = listOf(
                Color.BLUE, Color.GREEN, Color.YELLOW,
                Color.MAGENTA, Color.CYAN, Color.RED,
                Color.LTGRAY
            ) //長條圖顏色

            // 將數據加到 BarData
            val petActivity = BarData(dataSet)
            petActivity.barWidth = 0.6f // 調整柱子的寬度

            // 指定圖表數據
            bd.barchart.data = petActivity

            // 美化 X 軸 星期名稱
            val X = bd.barchart.xAxis
            X.position = XAxis.XAxisPosition.BOTTOM // X軸底部增加日期
            X.valueFormatter = IndexAxisValueFormatter(
                arrayOf(" ","星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日")
            )
            X.textSize = 12f
            X.setDrawGridLines(false) // 移除網格線

            // 美化 Y 軸
            val left = bd.barchart.axisLeft
            left.isEnabled = false // 隱藏左側 Y 軸

            val right = bd.barchart.axisRight
            right.axisMinimum = 0f // 最小值為 0
            right.textSize = 12f

            // 設定圖表描述與動畫
            bd.barchart.description.text = "寵物每週步數統計"
            bd.barchart.description.textSize = 12f
            bd.barchart.animateY(1000)

            // 更新圖表
            bd.barchart.invalidate()

            val totalSteps = barEntries.sumBy { it.y.toInt() }
            val averageDistancePerStep = 0.0008
            val totalDistance = totalSteps * averageDistancePerStep
            bd.chart.text = "步數：一周共移動了 $totalSteps 步數 \n活動距離：約 ${String.format("%.2f", totalDistance)} 公里"
        }
    }



    override fun onLocationChanged(location: Location) {
        //TODO("Not yet implemented")
        val latitude = location.latitude
        val longitude = location.longitude
        // 顯示經緯度資訊或其他相關操作
        bd.loc.text = "目前位置：\n $latitude\n$longitude"
    }


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }

    override fun onResume() {
        super.onResume()
        mysr.also {
            ssmg.registerListener(this,it,SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        mysr.also {
            ssmg.unregisterListener(this)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        //TODO("Not yet implemented")
        when(event?.sensor?.type){
            Sensor.TYPE_ACCELEROMETER -> {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                var act = ""
                if (x > -8 && x < 8 && y > 8 && z > -8 && z < 8){
                    act = "寵物目前可能站著或正在移動"
                }else{
                    act = "寵物目前可能躺著"
                }
                bd.acc.text="陀螺儀偵測：$act \n (X:$x) (Y:$y) (Z:$z)"
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //TODO("Not yet implemented")
    }

}
private fun LocationManager.requestLocationUpdates(gpsProvider: String) {

}
