package org.crazyit.sensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class MainActivity extends Activity
		implements SensorEventListener
{
	// 定义Sensor管理器
	private SensorManager mSensorManager;
	EditText etOrientation;
	EditText etGyro;
	EditText etMagnetic;
	EditText etGravity;
	EditText etLinearAcc;
	EditText etTemerature;
	EditText etLight;
	EditText etPressure;
	private RandomAccessFile rafOrientation;
	private RandomAccessFile rafGyro;
	private RandomAccessFile rafMagnetic;
	private RandomAccessFile rafGravity;
	private RandomAccessFile rafLinearAcc;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// 获取界面上的EditText组件
		etOrientation = (EditText) findViewById(R.id.etOrientation);
		etGyro = (EditText) findViewById(R.id.etGyro);
		etMagnetic = (EditText) findViewById(R.id.etMagnetic);
		etGravity = (EditText) findViewById(R.id.etGravity);
		etLinearAcc = (EditText) findViewById(R.id.etLinearAcc);
		etTemerature = (EditText) findViewById(R.id.etTemerature);
		etLight = (EditText) findViewById(R.id.etLight);
		etPressure = (EditText) findViewById(R.id.etPressure);
		// 获取传感器管理服务
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);  // ①

		String sFilePath = getSDPath() + File.separator + "SENSOR" + File.separator;
		File filePath = new File(sFilePath);
		if(!filePath.exists()) {
			filePath.mkdir();
		}
		File fileOrientation = new File(sFilePath + "Orientation.txt");
		try {
			rafOrientation = new RandomAccessFile(fileOrientation, "rwd");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		File fileGyro = new File(sFilePath + "Gyro.txt");
		try {
			rafGyro = new RandomAccessFile(fileGyro, "rwd");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		File fileMagnetic = new File(sFilePath + "Magnetic.txt");
		try {
			rafMagnetic = new RandomAccessFile(fileMagnetic, "rwd");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		File fileGravity = new File(sFilePath + "Gravity.txt");
		try {
			rafGravity = new RandomAccessFile(fileGravity, "rwd");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		File fileLinearAcc = new File(sFilePath + "LinearAcc.txt");
		try {
			rafLinearAcc = new RandomAccessFile(fileLinearAcc, "rwd");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		if(!fileOrientation.exists()) {
			try {
				fileOrientation.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(!fileGravity.exists()) {
			try {
				fileGravity.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(!fileGyro.exists()) {
			try {
				fileGyro.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(!fileLinearAcc.exists()) {
			try {
				fileLinearAcc.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(!fileMagnetic.exists()) {
			try {
				fileMagnetic.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getSDPath(){
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState()
				.equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
		if (sdCardExist)
		{
			sdDir = Environment.getExternalStorageDirectory();//获取根目录
		}
		return sdDir != null ? sdDir.toString() : null;
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		// 为系统的方向传感器注册监听器
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_GAME);
		// 为系统的陀螺仪传感器注册监听器
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
				SensorManager.SENSOR_DELAY_GAME);
		// 为系统的磁场传感器注册监听器
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_GAME);
		// 为系统的重力传感器注册监听器
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
				SensorManager.SENSOR_DELAY_GAME);
		// 为系统的线性加速度传感器注册监听器
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
				SensorManager.SENSOR_DELAY_GAME);
		// 为系统的温度传感器注册监听器
		boolean retTYPE_AMBIENT_TEMPERATURE = mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
				SensorManager.SENSOR_DELAY_GAME);
		// 为系统的光传感器注册监听器
		boolean retTYPE_LIGHT = mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
				SensorManager.SENSOR_DELAY_GAME);
		// 为系统的压力传感器注册监听器
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE),
				SensorManager.SENSOR_DELAY_GAME);
	}
	@Override
	protected void onStop()
	{
		try {
			rafLinearAcc.close();
			rafGravity.close();
			rafMagnetic.close();
			rafGyro.close();
			rafOrientation.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 程序退出时取消注册传感器监听器
		mSensorManager.unregisterListener(this);
		super.onStop();
	}
	@Override
	protected void onPause()
	{
		// 程序暂停时取消注册传感器监听器
		mSensorManager.unregisterListener(this);
		super.onPause();
	}
	// 以下是实现SensorEventListener接口必须实现的方法
	@Override
	// 当传感器精度改变时回调该方法。
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{
	}
	@Override
	public void onSensorChanged(SensorEvent event)
	{
		float[] values = event.values;
		// 获取触发event的传感器类型
		int sensorType = event.sensor.getType();
		StringBuilder sb = null;
		// 判断是哪个传感器发生改变
		switch (sensorType)
		{
			// 方向传感器
			case Sensor.TYPE_ORIENTATION:
				sb = new StringBuilder();
				sb.append("绕Z轴转过的角度：");
				sb.append(values[0]);
				sb.append("\n绕X轴转过的角度：");
				sb.append(values[1]);
				sb.append("\n绕Y轴转过的角度：");
				sb.append(values[2]);
				etOrientation.setText(sb.toString());
				String strOrientation = values[0] + ", " + values[1] + ", " + values[2] +"\r\n";
				try {
					rafOrientation.write(strOrientation.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			// 陀螺仪传感器
			case Sensor.TYPE_GYROSCOPE:
				sb = new StringBuilder();
				sb.append("绕X轴旋转的角速度：");
				sb.append(values[0]);
				sb.append("\n绕Y轴旋转的角速度：");
				sb.append(values[1]);
				sb.append("\n绕Z轴旋转的角速度：");
				sb.append(values[2]);
				etGyro.setText(sb.toString());
				String sGyro = values[0] + ", " + values[1] + ", " + values[2]  + "\r\n";
				try {
					rafGyro.write(sGyro.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			// 磁场传感器
			case Sensor.TYPE_MAGNETIC_FIELD:
				sb = new StringBuilder();
				sb.append("X轴方向上的磁场强度：");
				sb.append(values[0]);
				sb.append("\nY轴方向上的磁场强度：");
				sb.append(values[1]);
				sb.append("\nZ轴方向上的磁场强度：");
				sb.append(values[2]);
				etMagnetic.setText(sb.toString());
				String sMagnetic =  values[0] + ", " + values[1] + ", " + values[2] +"\r\n";
				try {
					rafMagnetic.write(sMagnetic.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			// 重力传感器
			case Sensor.TYPE_GRAVITY:
				sb = new StringBuilder();
				sb.append("X轴方向上的重力：");
				sb.append(values[0]);
				sb.append("\nY轴方向上的重力：");
				sb.append(values[1]);
				sb.append("\nZ方向上的重力：");
				sb.append(values[2]);
				etGravity.setText(sb.toString());
				String sGravity = values[0] + ", " + values[1] + ", " + values[2] +"\r\n";
				try {
					rafGravity.write(sGravity.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			// 线性加速度传感器
			case Sensor.TYPE_LINEAR_ACCELERATION:
				sb = new StringBuilder();
				sb.append("X轴方向上的线性加速度：");
				sb.append(values[0]);
				sb.append("\nY轴方向上的线性加速度：");
				sb.append(values[1]);
				sb.append("\nZ轴方向上的线性加速度：");
				sb.append(values[2]);
				etLinearAcc.setText(sb.toString());
				String sLinearAcc = values[0] + ", " + values[1] + ", " + values[2] +"\r\n";
				try {
					rafLinearAcc.write(sLinearAcc.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			// 温度传感器
			case Sensor.TYPE_AMBIENT_TEMPERATURE:
				sb = new StringBuilder();
				sb.append("当前温度为：");
				sb.append(values[0]);
				etTemerature.setText(sb.toString());
				break;
			// 光传感器
			case Sensor.TYPE_LIGHT:
				sb = new StringBuilder();
				sb.append("当前光的强度为：");
				sb.append(values[0]);
				etLight.setText(sb.toString());
				break;
			// 压力传感器
			case Sensor.TYPE_PRESSURE:
				sb = new StringBuilder();
				sb.append("当前压力为：");
				sb.append(values[0]);
				etPressure.setText(sb.toString());
				break;
		}
	}
}

