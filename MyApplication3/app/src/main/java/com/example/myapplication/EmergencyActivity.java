package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

public class EmergencyActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "EmergencyActivity";
    private static final String PREFS_NAME = "EmergencyPrefs";
    private static final String EMERGENCY_CONTACT_KEY = "emergency_contact";
    private static final int REQUEST_CALL_PERMISSION = 1;
    private static final int REQUEST_SENSOR_PERMISSION = 2;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 3;
    
    // 传感器相关
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private Sensor magnetometerSensor;
    private Sensor pressureSensor;
    private Sensor gyroscopeSensor;
    
    // 传感器数据
    private float[] gravity = new float[3];
    private float[] geomagnetic = new float[3];
    private float[] gyro = new float[3];
    private float altitude = 0;
    
    // 摔倒检测参数
    private static final float FALL_THRESHOLD_ACCELERATION = 15.0f; // 加速度阈值
    private static final float GYRO_THRESHOLD = 3.0f; // 陀螺仪阈值 (旋转速度)
    private static final float ALTITUDE_DROP_THRESHOLD = 2.0f; // 海拔下降阈值 (米)
    private static final long FALL_TIME_THRESHOLD = 1000; // 时间阈值（毫秒）
    private long lastUpdate = 0;
    private boolean fallDetected = false;
    private float lastAltitude = 0;
    
    // 障碍物检测相关
    private static final String OBSTACLE_API_KEY = "sk-55262c50d6fb42778570e36047260177"; // 障碍物检测API密钥
    private MediaRecorder mediaRecorder;
    private boolean isObstacleDetectionRunning = false;
    
    // 倒计时
    private CountDownTimer countDownTimer;
    
    // UI组件
    private EditText emergencyContactEditText;
    private TextView fallStatusTextView;
    private TextView obstacleStatusTextView;
    private String emergencyContact = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        
        initViews();
        initSensors();
        loadEmergencyContact();
        checkPermissions();
    }
    
    private void initViews() {
        emergencyContactEditText = findViewById(R.id.et_emergency_contact);
        fallStatusTextView = findViewById(R.id.tv_fall_status);
        obstacleStatusTextView = findViewById(R.id.tv_obstacle_status);
        Button saveContactButton = findViewById(R.id.btn_save_contact);
        Button simulateFallButton = findViewById(R.id.btn_simulate_fall);
        Button startObstacleDetectionButton = findViewById(R.id.btn_start_obstacle_detection);
        Button stopObstacleDetectionButton = findViewById(R.id.btn_stop_obstacle_detection);
        
        if (saveContactButton != null) {
            saveContactButton.setOnClickListener(v -> saveEmergencyContact());
        }
        
        if (simulateFallButton != null) {
            simulateFallButton.setOnClickListener(v -> simulateFallDetection());
        }
        
        if (startObstacleDetectionButton != null) {
            startObstacleDetectionButton.setOnClickListener(v -> startObstacleDetection());
        }
        
        if (stopObstacleDetectionButton != null) {
            stopObstacleDetectionButton.setOnClickListener(v -> stopObstacleDetection());
        }
    }
    
    private void initSensors() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        
        if (sensorManager != null) {
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
            gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            
            if (accelerometerSensor == null) {
                Toast.makeText(this, "设备不支持加速度传感器", Toast.LENGTH_LONG).show();
            }
            
            if (magnetometerSensor == null) {
                Toast.makeText(this, "设备不支持磁场传感器", Toast.LENGTH_LONG).show();
            }
            
            if (pressureSensor == null) {
                Toast.makeText(this, "设备不支持气压传感器", Toast.LENGTH_LONG).show();
            }
            
            if (gyroscopeSensor == null) {
                Toast.makeText(this, "设备不支持陀螺仪传感器", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    private void loadEmergencyContact() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        emergencyContact = prefs.getString(EMERGENCY_CONTACT_KEY, "");
        if (!emergencyContact.isEmpty() && emergencyContactEditText != null) {
            emergencyContactEditText.setText(emergencyContact);
        }
    }
    
    private void saveEmergencyContact() {
        if (emergencyContactEditText != null) {
            emergencyContact = emergencyContactEditText.getText().toString().trim();
            if (!emergencyContact.isEmpty()) {
                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(EMERGENCY_CONTACT_KEY, emergencyContact);
                editor.apply();
                
                Toast.makeText(this, "紧急联系人已保存", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "请输入有效的紧急联系人号码", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private void checkPermissions() {
        // 检查电话权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        }
        
        // 检查传感器权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                    new String[]{Manifest.permission.BODY_SENSORS}, REQUEST_SENSOR_PERMISSION);
        }
        
        // 检查录音权限（用于障碍物检测）
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                    new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        switch (requestCode) {
            case REQUEST_CALL_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "电话权限已授予", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "需要电话权限才能使用紧急呼叫功能", Toast.LENGTH_LONG).show();
                }
                break;
                
            case REQUEST_SENSOR_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "传感器权限已授予", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "需要传感器权限才能使用摔倒检测功能", Toast.LENGTH_LONG).show();
                }
                break;
                
            case REQUEST_RECORD_AUDIO_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "录音权限已授予", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "需要录音权限才能使用障碍物检测功能", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        // 注册传感器监听器
        if (sensorManager != null) {
            if (accelerometerSensor != null) {
                sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
            
            if (magnetometerSensor != null) {
                sensorManager.registerListener(this, magnetometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
            
            if (pressureSensor != null) {
                sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
            
            if (gyroscopeSensor != null) {
                sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        
        // 注销传感器监听器
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
        
        // 停止障碍物检测
        stopObstacleDetection();
    }
    
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (fallDetected) return; // 如果已经检测到摔倒，不再处理传感器数据
        
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                System.arraycopy(event.values, 0, gravity, 0, 3);
                break;
                
            case Sensor.TYPE_MAGNETIC_FIELD:
                System.arraycopy(event.values, 0, geomagnetic, 0, 3);
                break;
                
            case Sensor.TYPE_PRESSURE:
                altitude = android.hardware.SensorManager.getAltitude(
                    android.hardware.SensorManager.PRESSURE_STANDARD_ATMOSPHERE, event.values[0]);
                break;
                
            case Sensor.TYPE_GYROSCOPE:
                System.arraycopy(event.values, 0, gyro, 0, 3);
                break;
        }
        
        // 检查是否发生摔倒
        checkFallDetection(System.currentTimeMillis());
    }
    
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // 传感器精度改变时的处理
    }
    
    private void checkFallDetection(long currentTime) {
        try {
            if ((currentTime - lastUpdate) < FALL_TIME_THRESHOLD) {
                return;
            }
            
            lastUpdate = currentTime;
            
            if (gravity == null || gravity.length < 3) {
                Log.e(TAG, "无效的加速度数据");
                return;
            }
            
            // 计算总加速度
            float acceleration = (float) Math.sqrt(gravity[0] * gravity[0] + 
                                                  gravity[1] * gravity[1] + 
                                                  gravity[2] * gravity[2]);
            
            // 检查加速度是否超过阈值
            boolean accelerationTrigger = acceleration > FALL_THRESHOLD_ACCELERATION;
            
            // 检查陀螺仪数据（旋转速度）
            boolean gyroTrigger = false;
            if (gyro != null && gyro.length >= 3) {
                float rotationSpeed = (float) Math.sqrt(gyro[0] * gyro[0] + 
                                                       gyro[1] * gyro[1] + 
                                                       gyro[2] * gyro[2]);
                gyroTrigger = rotationSpeed > GYRO_THRESHOLD;
            }
            
            // 检查海拔变化
            boolean altitudeDropTrigger = false;
            if (lastAltitude != 0) {
                float altitudeChange = Math.abs(altitude - lastAltitude);
                altitudeDropTrigger = altitudeChange > ALTITUDE_DROP_THRESHOLD;
            }
            lastAltitude = altitude;
            
            // 检测是否摔倒（任一条件满足）
            if (accelerationTrigger || gyroTrigger || altitudeDropTrigger) {
                fallDetected = true;
                triggerFallAlert();
            }
        } catch (Exception e) {
            Log.e(TAG, "摔倒检测失败", e);
        }
    }
    
    private void triggerFallAlert() {
        try {
            runOnUiThread(() -> {
                if (fallStatusTextView != null) {
                    fallStatusTextView.setText("检测到摔倒！正在启动紧急呼叫...");
                }
                
                showFallConfirmationDialog();
            });
        } catch (Exception e) {
            Log.e(TAG, "触发摔倒警报失败", e);
            runOnUiThread(() -> Toast.makeText(EmergencyActivity.this, "紧急警报触发失败", Toast.LENGTH_SHORT).show());
        }
    }
    
    private void showFallConfirmationDialog() {
        try {
            runOnUiThread(() -> {
                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EmergencyActivity.this);
                    builder.setTitle("紧急求助");
                    builder.setMessage("检测到可能的摔倒事件！是否需要拨打120急救电话和紧急联系人？");
                    
                    builder.setPositiveButton("是", (dialog, which) -> {
                        try {
                            callEmergencyServices();
                        } catch (Exception e) {
                            Log.e(TAG, "紧急呼叫处理失败", e);
                            runOnUiThread(() -> Toast.makeText(EmergencyActivity.this, "紧急呼叫处理失败", Toast.LENGTH_SHORT).show());
                        }
                    });
                    
                    builder.setNegativeButton("否", (dialog, which) -> {
                        try {
                            fallDetected = false;
                            if (fallStatusTextView != null) {
                                fallStatusTextView.setText("未检测到摔倒");
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "取消紧急呼叫处理失败", e);
                            runOnUiThread(() -> Toast.makeText(EmergencyActivity.this, "操作取消处理失败", Toast.LENGTH_SHORT).show());
                        }
                    });
                    
                    AlertDialog dialog = builder.create();
                    dialog.setCancelable(false); // 禁止通过返回键取消
                    dialog.setCanceledOnTouchOutside(false); // 禁止点击外部区域取消
                    
                    // 启动倒计时
                    startCountdownTimer(dialog);
                    dialog.show();
                } catch (Exception e) {
                    Log.e(TAG, "显示确认对话框失败", e);
                    runOnUiThread(() -> Toast.makeText(EmergencyActivity.this, "显示对话框失败", Toast.LENGTH_SHORT).show());
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "UI线程执行失败", e);
        }
    }
    
    private void startCountdownTimer(AlertDialog dialog) {
        try {
            if (countDownTimer != null) {
                countDownTimer.cancel();
                countDownTimer = null;
            }
            
            countDownTimer = new CountDownTimer(10000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    try {
                        runOnUiThread(() -> {
                            long secondsLeft = millisUntilFinished / 1000;
                            if (dialog.isShowing()) {
                                dialog.setMessage("检测到可能的摔倒事件！是否需要拨打120急救电话和紧急联系人？\n\n将在 " + secondsLeft + " 秒后自动拨打...");
                            }
                        });
                    } catch (Exception e) {
                        Log.e(TAG, "倒计时更新失败", e);
                    }
                }
                
                @Override
                public void onFinish() {
                    try {
                        // 倒计时结束后自动拨打紧急电话
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                            callEmergencyServices();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "倒计时完成处理失败", e);
                    }
                }
            };
            
            countDownTimer.start();
        } catch (Exception e) {
            Log.e(TAG, "启动倒计时失败", e);
            Toast.makeText(this, "启动倒计时失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private void cancelCountdownTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }
    
    private void callEmergencyServices() {
        try {
            // 播放报警声音
            playAlertSound();
            
            // 拨打120急救电话
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) 
                    == PackageManager.PERMISSION_GRANTED) {
                // 实际拨打120急救电话
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:120"));
                try {
                    startActivity(callIntent);
                    Toast.makeText(this, "正在拨打120急救电话", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.e(TAG, "拨打120失败", e);
                    Toast.makeText(this, "拨打120失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                // 如果没有权限，尝试使用ACTION_DIAL（需要用户手动点击拨打）
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:120"));
                try {
                    startActivity(dialIntent);
                    Toast.makeText(this, "请手动拨打120急救电话", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.e(TAG, "打开拨号界面失败", e);
                    Toast.makeText(this, "无法打开拨号界面: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            
            // 拨打紧急联系人
            if (!emergencyContact.isEmpty()) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) 
                        == PackageManager.PERMISSION_GRANTED) {
                    // 实际拨打紧急联系人
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + emergencyContact));
                    try {
                        startActivity(callIntent);
                        Toast.makeText(this, "正在拨打紧急联系人: " + emergencyContact, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e(TAG, "拨打紧急联系人失败", e);
                        Toast.makeText(this, "拨打紧急联系人失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    // 如果没有权限，尝试使用ACTION_DIAL（需要用户手动点击拨打）
                    Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                    dialIntent.setData(Uri.parse("tel:" + emergencyContact));
                    try {
                        startActivity(dialIntent);
                        Toast.makeText(this, "请手动拨打紧急联系人: " + emergencyContact, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e(TAG, "打开拨号界面失败", e);
                        Toast.makeText(this, "无法打开拨号界面: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(this, "未设置紧急联系人", Toast.LENGTH_SHORT).show();
            }
            
            // 重置状态
            fallDetected = false;
            if (fallStatusTextView != null) {
                fallStatusTextView.setText("未检测到摔倒");
            }
        } catch (Exception e) {
            Log.e(TAG, "执行紧急呼叫失败", e);
            Toast.makeText(this, "执行紧急呼叫失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private void playAlertSound() {
        try {
            // 使用RingtoneManager来播放系统默认通知铃声
            // 这种方法比直接引用特定资源更可靠
            Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone ringtone = RingtoneManager.getRingtone(this, notificationUri);
            
            if (ringtone != null) {
                ringtone.play();
                // 注意：Ringtone会在播放完成后自动释放资源
            } else {
                // 如果通知铃声不可用，尝试使用闹钟铃声
                Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                Ringtone alarmRingtone = RingtoneManager.getRingtone(this, alarmUri);
                
                if (alarmRingtone != null) {
                    alarmRingtone.play();
                } else {
                    // 最后的备选方案：使用系统默认铃声
                    Uri defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                    Ringtone defaultRingtone = RingtoneManager.getRingtone(this, defaultUri);
                    
                    if (defaultRingtone != null) {
                        defaultRingtone.play();
                    } else {
                        // 如果所有系统铃声都不可用，使用MediaPlayer播放默认资源
                        try {
                            Uri notificationUri2 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            Ringtone ringtone2 = RingtoneManager.getRingtone(this, notificationUri2);
                            if (ringtone2 != null) {
                                ringtone2.play();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "播放默认通知声音失败", e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "播放报警声音失败", e);
            
            // 最后的备选方案：尝试播放系统默认声音
            try {
                Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                MediaPlayer mediaPlayer = MediaPlayer.create(this, notificationUri);
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(mp -> {
                        try {
                            mp.release();
                        } catch (Exception ex) {
                            Log.e(TAG, "释放MediaPlayer资源失败", ex);
                        }
                    });
                }
            } catch (Exception ex) {
                Log.e(TAG, "使用MediaPlayer播放报警声音失败", ex);
            }
        }
    }
    
    private void startObstacleDetection() {
        // 障碍物检测功能实现
        Toast.makeText(this, "开始障碍物检测", Toast.LENGTH_SHORT).show();
        isObstacleDetectionRunning = true;
        
        if (obstacleStatusTextView != null) {
            obstacleStatusTextView.setText("障碍物检测运行中...");
        }
    }
    
    private void stopObstacleDetection() {
        // 停止障碍物检测功能
        isObstacleDetectionRunning = false;
        
        if (obstacleStatusTextView != null) {
            obstacleStatusTextView.setText("障碍物检测已停止");
        }
        
        Toast.makeText(this, "停止障碍物检测", Toast.LENGTH_SHORT).show();
    }
    
    private void simulateFallDetection() {
        // 模拟摔倒检测
        Toast.makeText(this, "模拟摔倒检测", Toast.LENGTH_SHORT).show();
        triggerFallAlert();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelCountdownTimer();
        stopObstacleDetection();
    }
}