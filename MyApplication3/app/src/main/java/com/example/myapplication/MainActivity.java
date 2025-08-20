package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    
    private Button objectRecognitionBtn;
    private Button bookReadingBtn;
    private Button artAppreciationBtn;
    private Button emergencyBtn;
    
    private TextToSpeech textToSpeech;
    private GestureDetector gestureDetector;

    // 用于处理单击和双击的Handler
    private Handler handler = new Handler();
    private boolean isSingleClick = false;
    private static final long DOUBLE_CLICK_TIME_DELTA = 300; // 双击时间间隔

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        try {
            initViews();
            initTextToSpeech();
        } catch (Exception e) {
            Log.e(TAG, "应用启动失败", e);
            Toast.makeText(this, "应用启动失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initViews() {
        try {
            // 初始化功能按钮
            objectRecognitionBtn = findViewById(R.id.btn_object_recognition);
            bookReadingBtn = findViewById(R.id.btn_book_reading);
            artAppreciationBtn = findViewById(R.id.btn_art_appreciation);
            emergencyBtn = findViewById(R.id.btn_emergency);

            // 拍照识物按钮事件
            if (objectRecognitionBtn != null) {
                objectRecognitionBtn.setOnClickListener(v -> {
                    speakText("双击进入拍照识物功能，可以识别周围物体");
                });
                
                objectRecognitionBtn.setOnLongClickListener(v -> {
                    try {
                        startActivity(new Intent(MainActivity.this, ObjectRecognitionActivity.class));
                    } catch (Exception e) {
                        Log.e(TAG, "启动拍照识物功能失败", e);
                        Toast.makeText(MainActivity.this, "启动拍照识物功能失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    return true;
                });
                
                // 添加触摸监听器以支持单击和双击
                objectRecognitionBtn.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            if (isSingleClick) {
                                // 双击
                                isSingleClick = false;
                                handler.removeCallbacksAndMessages(null);
                                try {
                                    startActivity(new Intent(MainActivity.this, ObjectRecognitionActivity.class));
                                } catch (Exception e) {
                                    Log.e(TAG, "启动拍照识物功能失败", e);
                                    Toast.makeText(MainActivity.this, "启动拍照识物功能失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                return true;
                            } else {
                                // 单击
                                isSingleClick = true;
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (isSingleClick) {
                                            isSingleClick = false;
                                            speakText("双击进入拍照识物功能，可以识别周围物体");
                                        }
                                    }
                                }, DOUBLE_CLICK_TIME_DELTA);
                            }
                        }
                        return true;
                    }
                });
            }

            // 典籍阅读按钮事件
            if (bookReadingBtn != null) {
                bookReadingBtn.setOnClickListener(v -> {
                    speakText("双击进入典籍阅读功能，可以阅读经典文学作品");
                });
                
                bookReadingBtn.setOnLongClickListener(v -> {
                    try {
                        // 直接跳转到指定URL
                        String url = "https://www.cdlvi.cn/allSearch/searchList?searchType=6&showType=1&pageNo=1";
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.e(TAG, "启动典籍阅读功能失败", e);
                        Toast.makeText(MainActivity.this, "启动典籍阅读功能失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    return true;
                });
                
                // 添加触摸监听器以支持单击和双击
                bookReadingBtn.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            if (isSingleClick) {
                                // 双击
                                isSingleClick = false;
                                handler.removeCallbacksAndMessages(null);
                                try {
                                    // 直接跳转到指定URL
                                    String url = "https://www.cdlvi.cn/allSearch/searchList?searchType=6&showType=1&pageNo=1";
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(url));
                                    startActivity(intent);
                                } catch (Exception e) {
                                    Log.e(TAG, "启动典籍阅读功能失败", e);
                                    Toast.makeText(MainActivity.this, "启动典籍阅读功能失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                return true;
                            } else {
                                // 单击
                                isSingleClick = true;
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (isSingleClick) {
                                            isSingleClick = false;
                                            speakText("双击进入典籍阅读功能，可以阅读经典文学作品");
                                        }
                                    }
                                }, DOUBLE_CLICK_TIME_DELTA);
                            }
                        }
                        return true;
                    }
                });
            }

            // 艺术鉴赏按钮事件
            if (artAppreciationBtn != null) {
                artAppreciationBtn.setOnClickListener(v -> {
                    speakText("双击进入艺术鉴赏功能，可以欣赏艺术作品");
                });
                
                artAppreciationBtn.setOnLongClickListener(v -> {
                    try {
                        startActivity(new Intent(MainActivity.this, ArtAppreciationActivity.class));
                    } catch (Exception e) {
                        Log.e(TAG, "启动艺术鉴赏功能失败", e);
                        Toast.makeText(MainActivity.this, "启动艺术鉴赏功能失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    return true;
                });
                
                // 添加触摸监听器以支持单击和双击
                artAppreciationBtn.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            if (isSingleClick) {
                                // 双击
                                isSingleClick = false;
                                handler.removeCallbacksAndMessages(null);
                                try {
                                    startActivity(new Intent(MainActivity.this, ArtAppreciationActivity.class));
                                } catch (Exception e) {
                                    Log.e(TAG, "启动艺术鉴赏功能失败", e);
                                    Toast.makeText(MainActivity.this, "启动艺术鉴赏功能失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                return true;
                            } else {
                                // 单击
                                isSingleClick = true;
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (isSingleClick) {
                                            isSingleClick = false;
                                            speakText("双击进入艺术鉴赏功能，可以欣赏艺术作品");
                                        }
                                    }
                                }, DOUBLE_CLICK_TIME_DELTA);
                            }
                        }
                        return true;
                    }
                });
            }

            // 紧急求助按钮事件
            if (emergencyBtn != null) {
                emergencyBtn.setOnClickListener(v -> {
                    speakText("双击进入紧急求助功能，可以快速联系紧急联系人");
                });
                
                emergencyBtn.setOnLongClickListener(v -> {
                    try {
                        startActivity(new Intent(MainActivity.this, EmergencyActivity.class));
                    } catch (Exception e) {
                        Log.e(TAG, "启动紧急求助功能失败", e);
                        Toast.makeText(MainActivity.this, "启动紧急求助功能失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    return true;
                });
                
                // 添加触摸监听器以支持单击和双击
                emergencyBtn.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            if (isSingleClick) {
                                // 双击
                                isSingleClick = false;
                                handler.removeCallbacksAndMessages(null);
                                try {
                                    startActivity(new Intent(MainActivity.this, EmergencyActivity.class));
                                } catch (Exception e) {
                                    Log.e(TAG, "启动紧急求助功能失败", e);
                                    Toast.makeText(MainActivity.this, "启动紧急求助功能失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                return true;
                            } else {
                                // 单击
                                isSingleClick = true;
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (isSingleClick) {
                                            isSingleClick = false;
                                            speakText("双击进入紧急求助功能，可以快速联系紧急联系人");
                                        }
                                    }
                                }, DOUBLE_CLICK_TIME_DELTA);
                            }
                        }
                        return true;
                    }
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "初始化视图失败", e);
            Toast.makeText(this, "初始化视图失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initTextToSpeech() {
        try {
            textToSpeech = new TextToSpeech(this, status -> {
                try {
                    if (status == TextToSpeech.SUCCESS) {
                        // 尝试设置中文语言
                        int result = textToSpeech.setLanguage(Locale.SIMPLIFIED_CHINESE);

                        // 如果简体中文不支持，尝试繁体中文
                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                            result = textToSpeech.setLanguage(Locale.TRADITIONAL_CHINESE);
                        }

                        // 如果中文都不支持，尝试系统默认语言
                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                            result = textToSpeech.setLanguage(Locale.getDefault());
                        }

                        // 如果仍然不支持，显示提示但不强制退出
                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                            runOnUiThread(() -> Toast.makeText(MainActivity.this, "当前设备TTS引擎不支持中文，将使用默认语言", Toast.LENGTH_LONG).show());
                        }

                        // TTS初始化成功后播放欢迎语音
                        speakWelcomeMessage();
                    } else {
                        // TTS引擎初始化失败，仅显示Toast提示，不强制用户安装特定引擎
                        runOnUiThread(() -> Toast.makeText(MainActivity.this, "TTS引擎初始化失败，部分功能可能无法正常使用", Toast.LENGTH_LONG).show());
                        // 即使TTS初始化失败也播放欢迎语音（虽然不会发出声音）
                        speakWelcomeMessage();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "TTS初始化回调处理失败", e);
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "TTS初始化处理失败: " + e.getMessage(), Toast.LENGTH_LONG).show());
                }
            });
        } catch (Exception e) {
            // 发生异常，仅显示Toast提示
            Toast.makeText(this, "TTS初始化失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
            // 即使TTS初始化失败也播放欢迎语音（虽然不会发出声音）
            speakWelcomeMessage();
        }
    }

    private void speakText(String text) {
        try {
            if (textToSpeech != null) {
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "");
            }
        } catch (Exception e) {
            Log.e(TAG, "语音播放失败: " + e.getMessage(), e);
        }
    }

    private void speakWelcomeMessage() {
        try {
            speakText("欢迎使用盲人辅助应用。本应用包含四个主要功能：拍照识物、典籍阅读、艺术鉴赏和紧急求助。" +
                    "请通过单击按钮听取功能说明，双击按钮进入相应功能。");
        } catch (Exception e) {
            Log.e(TAG, "播放欢迎消息失败: " + e.getMessage(), e);
        }
    }

    @Override
    protected void onDestroy() {
        try {
            if (textToSpeech != null) {
                textToSpeech.stop();
                textToSpeech.shutdown();
            }
            // 移除所有待处理的回调
            handler.removeCallbacksAndMessages(null);
        } catch (Exception e) {
            Log.e(TAG, "TTS资源释放失败: " + e.getMessage(), e);
        }
        super.onDestroy();
    }
}