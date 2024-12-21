package com.example.afinal

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
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
