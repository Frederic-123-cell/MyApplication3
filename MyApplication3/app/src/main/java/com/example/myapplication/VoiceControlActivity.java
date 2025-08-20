package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Locale;

public class VoiceControlActivity extends AppCompatActivity {
    private static final String TAG = "VoiceControlActivity";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 100;
    
    private TextToSpeech textToSpeech;
    private SpeechRecognizer speechRecognizer;
    private TextView statusTextView;
    private Button voiceControlButton;
    private boolean isListening = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_control);
        
        initViews();
        initTextToSpeech();
        initSpeechRecognizer();
        checkPermissions();
        
        speakText("欢迎使用语音控制功能。点击按钮开始语音识别。");
    }
    
    private void initViews() {
        statusTextView = findViewById(R.id.tv_status);
        voiceControlButton = findViewById(R.id.btn_voice_control);
        
        if (voiceControlButton != null) {
            voiceControlButton.setOnClickListener(v -> {
                if (isListening) {
                    stopListening();
                } else {
                    startListening();
                }
            });
        }
    }
    
    private void initTextToSpeech() {
        try {
            textToSpeech = new TextToSpeech(this, status -> {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.SIMPLIFIED_CHINESE);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(VoiceControlActivity.this, "TTS不支持中文", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VoiceControlActivity.this, "TTS初始化失败", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "TTS初始化失败", e);
            Toast.makeText(this, "TTS初始化失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private void initSpeechRecognizer() {
        try {
            if (SpeechRecognizer.isRecognitionAvailable(this)) {
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
                speechRecognizer.setRecognitionListener(new RecognitionListener() {
                    @Override
                    public void onReadyForSpeech(Bundle params) {
                        runOnUiThread(() -> {
                            if (statusTextView != null) {
                                statusTextView.setText("请说话...");
                            }
                            if (voiceControlButton != null) {
                                voiceControlButton.setText("停止识别");
                            }
                        });
                    }

                    @Override
                    public void onBeginningOfSpeech() {
                        runOnUiThread(() -> {
                            if (statusTextView != null) {
                                statusTextView.setText("正在识别...");
                            }
                        });
                    }

                    @Override
                    public void onRmsChanged(float rmsdB) {
                        // 可以在这里根据声音大小做可视化效果
                    }

                    @Override
                    public void onBufferReceived(byte[] buffer) {
                        // 收到语音缓冲区数据
                    }

                    @Override
                    public void onEndOfSpeech() {
                        runOnUiThread(() -> {
                            if (statusTextView != null) {
                                statusTextView.setText("识别结束，正在处理...");
                            }
                        });
                    }

                    @Override
                    public void onError(int error) {
                        String errorMessage = getErrorMessage(error);
                        Log.e(TAG, "语音识别错误: " + errorMessage);
                        runOnUiThread(() -> {
                            if (statusTextView != null) {
                                statusTextView.setText("识别错误: " + errorMessage);
                            }
                            if (voiceControlButton != null) {
                                voiceControlButton.setText("开始语音识别");
                            }
                            speakText("语音识别出错: " + errorMessage);
                            isListening = false;
                        });
                    }

                    @Override
                    public void onResults(Bundle results) {
                        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                        if (matches != null && !matches.isEmpty()) {
                            String recognizedText = matches.get(0);
                            Log.d(TAG, "识别结果: " + recognizedText);
                            runOnUiThread(() -> {
                                if (statusTextView != null) {
                                    statusTextView.setText("识别结果: " + recognizedText);
                                }
                                if (voiceControlButton != null) {
                                    voiceControlButton.setText("开始语音识别");
                                }
                                speakText("您说的是: " + recognizedText);
                                isListening = false;
                            });
                        } else {
                            runOnUiThread(() -> {
                                if (statusTextView != null) {
                                    statusTextView.setText("未识别到有效语音");
                                }
                                if (voiceControlButton != null) {
                                    voiceControlButton.setText("开始语音识别");
                                }
                                speakText("未识别到有效语音，请重试");
                                isListening = false;
                            });
                        }
                    }

                    @Override
                    public void onPartialResults(Bundle partialResults) {
                        // 部分识别结果
                    }

                    @Override
                    public void onEvent(int eventType, Bundle params) {
                        // 事件回调
                    }
                });
            } else {
                Toast.makeText(this, "设备不支持语音识别", Toast.LENGTH_LONG).show();
                Log.e(TAG, "设备不支持语音识别");
            }
        } catch (Exception e) {
            Log.e(TAG, "语音识别初始化失败", e);
            Toast.makeText(this, "语音识别初始化失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, 
                    new String[]{Manifest.permission.RECORD_AUDIO}, 
                    REQUEST_RECORD_AUDIO_PERMISSION);
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "录音权限已获取", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "需要录音权限才能使用语音识别功能", Toast.LENGTH_LONG).show();
            }
        }
    }
    
    private void startListening() {
        try {
            if (speechRecognizer == null) {
                Toast.makeText(this, "语音识别未初始化", Toast.LENGTH_SHORT).show();
                return;
            }
            
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "请说话...");
            
            speechRecognizer.startListening(intent);
            isListening = true;
        } catch (Exception e) {
            Log.e(TAG, "启动语音识别失败", e);
            Toast.makeText(this, "启动语音识别失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            isListening = false;
        }
    }
    
    private void stopListening() {
        try {
            if (speechRecognizer != null && isListening) {
                speechRecognizer.stopListening();
                isListening = false;
                if (voiceControlButton != null) {
                    voiceControlButton.setText("开始语音识别");
                }
                if (statusTextView != null) {
                    statusTextView.setText("语音识别已停止");
                }
                speakText("语音识别已停止");
            }
        } catch (Exception e) {
            Log.e(TAG, "停止语音识别失败", e);
            Toast.makeText(this, "停止语音识别失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private void speakText(String text) {
        try {
            if (textToSpeech != null) {
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "VOICE_CONTROL");
            }
        } catch (Exception e) {
            Log.e(TAG, "语音播放失败: " + e.getMessage(), e);
        }
    }
    
    private String getErrorMessage(int errorCode) {
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                return "音频录制错误";
            case SpeechRecognizer.ERROR_CLIENT:
                return "客户端错误";
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                return "权限不足";
            case SpeechRecognizer.ERROR_NETWORK:
                return "网络错误";
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                return "网络超时";
            case SpeechRecognizer.ERROR_NO_MATCH:
                return "未匹配到结果";
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                return "识别器忙";
            case SpeechRecognizer.ERROR_SERVER:
                return "服务器错误";
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                return "语音输入超时";
            default:
                return "未知错误";
        }
    }
    
    @Override
    protected void onDestroy() {
        try {
            if (speechRecognizer != null) {
                speechRecognizer.destroy();
            }
            if (textToSpeech != null) {
                textToSpeech.stop();
                textToSpeech.shutdown();
            }
        } catch (Exception e) {
            Log.e(TAG, "资源释放失败", e);
        }
        super.onDestroy();
    }
}