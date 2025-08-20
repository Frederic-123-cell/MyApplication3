package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class ObjectRecognitionActivity extends AppCompatActivity {
    private static final String TAG = "ObjectRecognition";
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    
    private TextToSpeech textToSpeech;
    private ActivityResultLauncher<Intent> takePictureLauncher;
    private Button takePictureButton;
    private ImageView previewImageView;
    private TextView resultTextView;
    private Bitmap capturedImage;

    // 通义千问API相关常量
    private static final String QWEN_API_URL = "https://dashscope.aliyuncs.com/api/v1/services/aigc/multimodal-generation/generation";
    private static final String QWEN_API_KEY = "sk-3baedbf0bc594f0e9361248ed22d573a"; // 换回原始API密钥

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_object_recognition);
            initViews();
            initTextToSpeech();
            setupTakePictureLauncher();
            checkCameraPermission();
        } catch (Exception e) {
            Log.e(TAG, "拍照识物功能启动失败", e);
            Toast.makeText(this, "拍照识物功能启动失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initViews() {
        try {
            takePictureButton = findViewById(R.id.btn_capture);
            previewImageView = findViewById(R.id.iv_preview);
            resultTextView = findViewById(R.id.tv_result);

            if (takePictureButton != null) {
                takePictureButton.setOnClickListener(v -> {
                    speakText("正在拍照");
                    dispatchTakePictureIntent();
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "初始化视图失败", e);
            Toast.makeText(this, "初始化视图失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initTextToSpeech() {
        try {
            textToSpeech = new TextToSpeech(this, status -> {
                try {
                    if (status == TextToSpeech.SUCCESS) {
                        int result = textToSpeech.setLanguage(Locale.SIMPLIFIED_CHINESE);
                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                            runOnUiThread(() -> Toast.makeText(ObjectRecognitionActivity.this, "TTS不支持中文", Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        runOnUiThread(() -> Toast.makeText(ObjectRecognitionActivity.this, "TTS初始化失败", Toast.LENGTH_SHORT).show());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "TTS初始化回调处理失败", e);
                    runOnUiThread(() -> Toast.makeText(ObjectRecognitionActivity.this, "TTS初始化处理失败: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "TTS初始化失败", e);
            Toast.makeText(this, "TTS初始化失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void speakText(String text) {
        try {
            if (textToSpeech != null) {
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "OBJECT_RECOGNITION");
            }
        } catch (Exception e) {
            Log.e(TAG, "语音播放失败: " + e.getMessage(), e);
        }
    }

    private void setupTakePictureLauncher() {
        try {
            takePictureLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            try {
                                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                                    Bundle extras = result.getData().getExtras();
                                    if (extras != null) {
                                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                                        if (imageBitmap != null) {
                                            previewImageView.setImageBitmap(imageBitmap);
                                            capturedImage = imageBitmap;
                                            speakText("照片拍摄完成，正在识别物体...");
                                            // 在后台线程中进行API调用
                                            new Thread(() -> recognizeObject(imageBitmap)).start();
                                        }
                                    }
                                } else {
                                    speakText("拍照取消或失败");
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "处理拍照结果失败", e);
                                speakText("处理拍照结果失败");
                                runOnUiThread(() -> Toast.makeText(ObjectRecognitionActivity.this, "处理拍照结果失败: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "设置拍照启动器失败", e);
            Toast.makeText(this, "设置拍照启动器失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void dispatchTakePictureIntent() {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                takePictureLauncher.launch(takePictureIntent);
            } else {
                speakText("无法启动相机");
                Toast.makeText(this, "无法启动相机应用", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "启动相机失败", e);
            speakText("启动相机失败");
            Toast.makeText(this, "启动相机失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void checkCameraPermission() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) 
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, 
                        new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            }
        } catch (Exception e) {
            Log.e(TAG, "检查相机权限失败", e);
            Toast.makeText(this, "检查相机权限失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            if (requestCode == REQUEST_CAMERA_PERMISSION) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    speakText("相机权限已获取");
                } else {
                    speakText("相机权限被拒绝，部分功能可能无法使用");
                    Toast.makeText(this, "需要相机权限才能使用拍照功能", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "处理权限请求结果失败", e);
            Toast.makeText(this, "处理权限请求结果失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void recognizeObject(Bitmap bitmap) {
        try {
            // 将Bitmap转换为Base64字符串
            String base64Image = bitmapToBase64(bitmap);
            
            // 检查Base64字符串是否有效
            if (base64Image == null || base64Image.isEmpty()) {
                runOnUiThread(() -> {
                    resultTextView.setText("图像转换失败");
                    speakText("图像转换失败");
                    Toast.makeText(ObjectRecognitionActivity.this, "图像转换为Base64失败", Toast.LENGTH_LONG).show();
                });
                return;
            }
            
            // 构建JSON请求体 - 使用data URI格式
            String dataUri = "data:image/jpeg;base64," + base64Image;
            String jsonPayload = "{\n" +
                    "  \"model\": \"qwen-vl-plus\",\n" +
                    "  \"input\": {\n" +
                    "    \"messages\": [\n" +
                    "      {\n" +
                    "        \"role\": \"user\",\n" +
                    "        \"content\": [\n" +
                    "          {\n" +
                    "            \"image\": \"" + dataUri + "\"\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"text\": \"请识别这张图片中的物体是什么，并用中文简要描述。\"\n" +
                    "          }\n" +
                    "        ]\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  },\n" +
                    "  \"parameters\": {\n" +
                    "    \"max_tokens\": 200\n" +
                    "  }\n" +
                    "}";

            // 创建带有超时设置的OkHttpClient
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();
            
            MediaType JSON = MediaType.get("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(jsonPayload, JSON);
            Request request = new Request.Builder()
                    .url(QWEN_API_URL)
                    .addHeader("Authorization", "Bearer " + QWEN_API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("X-DashScope-SSE", "disable") // 禁用SSE以获得标准JSON响应
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseData = response.body().string();
                    Log.d(TAG, "API响应: " + responseData);
                    
                    // 解析响应并提取识别结果
                    String result = parseRecognitionResult(responseData);
                    
                    // 在主线程更新UI
                    runOnUiThread(() -> {
                        try {
                            resultTextView.setText(result);
                            speakText("识别结果：" + result);
                        } catch (Exception e) {
                            Log.e(TAG, "更新UI失败", e);
                            Toast.makeText(ObjectRecognitionActivity.this, "更新UI失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    String error = "API请求失败，状态码: " + response.code();
                    Log.e(TAG, error);
                    if (response.body() != null) {
                        String errorBody = response.body().string();
                        Log.e(TAG, "错误详情: " + errorBody);
                        // 尝试解析错误响应
                        String errorMessage = parseErrorResult(errorBody);
                        runOnUiThread(() -> {
                            resultTextView.setText("识别失败: " + errorMessage);
                            speakText("物体识别失败: " + errorMessage);
                            Toast.makeText(ObjectRecognitionActivity.this, "API请求失败: " + errorMessage, Toast.LENGTH_LONG).show();
                        });
                    } else {
                        runOnUiThread(() -> {
                            resultTextView.setText("识别失败，请重试");
                            speakText("物体识别失败，请重试");
                            Toast.makeText(ObjectRecognitionActivity.this, "API请求失败，状态码: " + response.code(), Toast.LENGTH_LONG).show();
                        });
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "物体识别过程中发生错误", e);
            runOnUiThread(() -> {
                resultTextView.setText("识别出错，请重试: " + e.getMessage());
                speakText("物体识别出错，请重试: " + e.getMessage());
                Toast.makeText(ObjectRecognitionActivity.this, "识别出错: " + e.getMessage(), Toast.LENGTH_LONG).show();
            });
        }
    }

    private String bitmapToBase64(Bitmap bitmap) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            // 尝试使用更高的质量压缩图像
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, outputStream);
            byte[] imageBytes = outputStream.toByteArray();
            // 使用NO_WRAP选项避免添加换行符，这可能导致URL解析问题
            String base64String = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
            Log.d(TAG, "Base64编码完成，长度: " + base64String.length());
            return base64String;
        } catch (Exception e) {
            Log.e(TAG, "图片转换Base64失败", e);
            return "";
        }
    }

    private String parseRecognitionResult(String responseData) {
        try {
            Log.d(TAG, "开始解析响应数据: " + responseData);
            
            // 使用Android内置的JSON库解析响应数据
            JSONObject jsonObject = new JSONObject(responseData);
            
            // 尝试解析通义千问API的不同响应格式
            if (jsonObject.has("output")) {
                JSONObject output = jsonObject.getJSONObject("output");
                Log.d(TAG, "找到output字段");
                
                // 格式1: 直接包含text字段
                if (output.has("text")) {
                    String text = output.getString("text");
                    Log.d(TAG, "找到text字段: " + text);
                    return text;
                }
                
                // 格式2: 包含choices数组
                if (output.has("choices")) {
                    Log.d(TAG, "找到choices字段");
                    JSONArray choices = output.getJSONArray("choices");
                    if (choices.length() > 0) {
                        JSONObject choice = choices.getJSONObject(0);
                        Log.d(TAG, "处理choice: " + choice.toString());
                        
                        // 检查message字段
                        if (choice.has("message")) {
                            Object messageObj = choice.get("message");
                            if (messageObj instanceof JSONObject) {
                                JSONObject message = (JSONObject) messageObj;
                                Log.d(TAG, "message是JSONObject: " + message.toString());
                                if (message.has("content")) {
                                    String content = message.getString("content");
                                    Log.d(TAG, "找到content字段: " + content);
                                    return content;
                                }
                            } else if (messageObj instanceof String) {
                                // 如果message是字符串，直接返回
                                String messageStr = (String) messageObj;
                                Log.d(TAG, "message是字符串: " + messageStr);
                                return messageStr;
                            }
                        }
                        
                        // 检查content字段（直接在choice中）
                        if (choice.has("content")) {
                            String content = choice.getString("content");
                            Log.d(TAG, "找到content字段(在choice中): " + content);
                            return content;
                        }
                        
                        // 检查text字段（直接在choice中）
                        if (choice.has("text")) {
                            String text = choice.getString("text");
                            Log.d(TAG, "找到text字段(在choice中): " + text);
                            return text;
                        }
                    }
                }
                
                // 格式3: result字段
                if (output.has("result")) {
                    String result = output.getString("result");
                    Log.d(TAG, "找到result字段: " + result);
                    return result;
                }
            }
            
            // 如果以上都找不到，尝试直接查找content字段
            if (jsonObject.has("content")) {
                String content = jsonObject.getString("content");
                Log.d(TAG, "找到content字段(根级别): " + content);
                return content;
            }
            
            // 如果还是找不到，返回包含详细信息的提示
            Log.d(TAG, "无法解析响应，返回原始数据预览");
            // 返回前200个字符作为预览
            String preview = responseData.length() > 200 ? responseData.substring(0, 200) + "..." : responseData;
            return "识别完成，但无法解析详细结果。响应预览: " + preview;
        } catch (JSONException e) {
            Log.e(TAG, "解析识别结果时出错: " + e.getMessage(), e);
            // 返回更详细的错误信息
            return "识别结果解析失败，JSON解析错误: " + e.getMessage();
        } catch (Exception e) {
            Log.e(TAG, "解析识别结果时发生未知错误: " + e.getMessage(), e);
            return "识别结果解析异常: " + e.getMessage();
        }
    }
    
    private String parseErrorResult(String errorData) {
        try {
            Log.d(TAG, "解析错误响应: " + errorData);
            JSONObject jsonObject = new JSONObject(errorData);
            if (jsonObject.has("message")) {
                return jsonObject.getString("message");
            } else if (jsonObject.has("error") && jsonObject.getJSONObject("error").has("message")) {
                return jsonObject.getJSONObject("error").getString("message");
            } else if (jsonObject.has("code")) {
                // 返回错误码和可能的描述
                String code = jsonObject.getString("code");
                String message = jsonObject.optString("message", "未知错误");
                return "错误码: " + code + ", 信息: " + message;
            }
        } catch (JSONException e) {
            Log.e(TAG, "解析错误响应失败", e);
        }
        return "未知错误，请检查网络连接和API密钥";
    }

    @Override
    protected void onDestroy() {
        try {
            if (textToSpeech != null) {
                textToSpeech.stop();
                textToSpeech.shutdown();
            }
        } catch (Exception e) {
            Log.e(TAG, "TTS资源释放失败", e);
        }
        super.onDestroy();
    }
}