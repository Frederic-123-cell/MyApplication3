package com.example.myapplication;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ArtAppreciationActivity extends AppCompatActivity {
    private static final String TAG = "ArtAppreciation";
    
    private TextToSpeech textToSpeech;
    private GestureDetector gestureDetector;
    
    // UI组件
    private Button prevButton;
    private Button nextButton;
    private TextView artworkDescriptionTextView;
    
    // 艺术作品数据
    private final List<Artwork> artworks = new ArrayList<>();
    private int currentArtworkIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            // 直接设置布局资源
            setContentView(R.layout.activity_art_appreciation);
            
            initViews();
            initTextToSpeech();
            loadSampleArtworks();
            
            // 进入功能后的语音提示
            speakText("已进入艺术鉴赏功能。您可以通过按钮或手势滑动浏览艺术作品。");
        } catch (Exception e) {
            Log.e(TAG, "创建Activity失败", e);
            Toast.makeText(ArtAppreciationActivity.this, "创建Activity失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        } catch (Error e) {
            Log.e(TAG, "创建Activity时发生严重错误", e);
            Toast.makeText(ArtAppreciationActivity.this, "创建Activity时发生严重错误: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }
    
    private void initViews() {
        try {
            artworkDescriptionTextView = findViewById(R.id.tv_artwork_description);
            prevButton = findViewById(R.id.btn_prev);
            nextButton = findViewById(R.id.btn_next);
            
            if (prevButton != null) {
                prevButton.setOnClickListener(v -> showPreviousArtwork());
                prevButton.setOnLongClickListener(v -> {
                    speakCurrentArtwork();
                    return true;
                });
            }
            
            if (nextButton != null) {
                nextButton.setOnClickListener(v -> showNextArtwork());
                nextButton.setOnLongClickListener(v -> {
                    speakCurrentArtwork();
                    return true;
                });
            }
            
            // 设置手势识别
            gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    try {
                        float diffX = e1.getX() - e2.getX();
                        float diffY = e1.getY() - e2.getY();
                        
                        // 检查是否是水平滑动
                        if (Math.abs(diffX) > Math.abs(diffY)) {
                            if (Math.abs(diffX) > 100 && Math.abs(velocityX) > 100) {
                                if (diffX > 0) {
                                    // 向左滑动，显示下一张作品
                                    showNextArtwork();
                                    return true;
                                } else {
                                    // 向右滑动，显示上一张作品
                                    showPreviousArtwork();
                                    return true;
                                }
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "手势识别失败", e);
                    }
                    return false;
                }
            });
            
            // 设置触摸监听器
            if (artworkDescriptionTextView != null) {
                artworkDescriptionTextView.setOnTouchListener((v, event) -> 
                    gestureDetector.onTouchEvent(event));
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
                            runOnUiThread(() -> Toast.makeText(ArtAppreciationActivity.this, "当前设备TTS引擎不支持中文，将使用默认语言", Toast.LENGTH_LONG).show());
                        }
                    } else {
                        // TTS引擎初始化失败，仅显示Toast提示，不强制用户安装特定引擎
                        runOnUiThread(() -> Toast.makeText(ArtAppreciationActivity.this, "TTS引擎初始化失败，部分功能可能无法正常使用", Toast.LENGTH_LONG).show());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "TTS初始化回调处理失败", e);
                    runOnUiThread(() -> Toast.makeText(ArtAppreciationActivity.this, "TTS初始化处理失败: " + e.getMessage(), Toast.LENGTH_LONG).show());
                }
            });
        } catch (Exception e) {
            // 发生异常，仅显示Toast提示
            Toast.makeText(this, "TTS初始化失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadSampleArtworks() {
        try {
            artworks.clear();
            
            // 添加10幅预设的艺术作品
            artworks.add(new Artwork("蒙娜丽莎", "《蒙娜丽莎》是意大利文艺复兴时期画家列奥纳多·达·芬奇创作的油画，现收藏于法国卢浮宫博物馆。画中人物带着神秘的微笑，背景是山水风景。"));
            artworks.add(new Artwork("星夜", "《星夜》是荷兰后印象派画家文森特·梵高于1889年在法国圣雷米创作的油画。画面中充满了旋转的星空和明亮的月亮，展现了梵高独特的艺术风格。"));
            artworks.add(new Artwork("向日葵", "《向日葵》是梵高的代表作之一，创作于1888年。这幅画以鲜艳的黄色和粗犷的笔触展现了向日葵的生机与活力。"));
            artworks.add(new Artwork("最后的晚餐", "《最后的晚餐》是列奥纳多·达·芬奇为米兰圣玛利亚感恩教堂创作的壁画，描绘了耶稣与十二门徒共进最后一次晚餐的场景。"));
            artworks.add(new Artwork("呐喊", "《呐喊》是挪威画家爱德华·蒙克1893年创作的表现主义画作。画面中的人物在血红色的天空下发出尖叫，表达了现代人的焦虑和恐惧。"));
            artworks.add(new Artwork("夜巡", "《夜巡》是荷兰黄金时代画家伦勃朗·范·莱因1642年创作的油画，原名《弗兰斯·班宁克·科克上尉与威廉·范·鲁伊坦伯格中尉的连队》。"));
            artworks.add(new Artwork("格尔尼卡", "《格尔尼卡》是西班牙画家巴勃罗·毕加索1937年创作的巨型油画，控诉了纳粹德国和意大利法西斯轰炸西班牙格尔尼卡小镇的暴行。"));
            artworks.add(new Artwork("维纳斯的诞生", "《维纳斯的诞生》是意大利画家桑德罗·波提切利约1485年创作的蛋彩画，描绘了罗马神话中爱与美的女神维纳斯从海中诞生的场景。"));
            artworks.add(new Artwork("亚维农少女", "《亚维农少女》是西班牙画家巴勃罗·毕加索1907年创作的油画，被认为是立体主义的开山之作。"));
            artworks.add(new Artwork("睡莲", "《睡莲》是法国印象派画家克劳德·莫奈晚年创作的系列油画，描绘了他在吉维尼花园中的睡莲池塘。"));
            
            // 显示第一幅作品
            if (!artworks.isEmpty() && artworkDescriptionTextView != null) {
                artworkDescriptionTextView.setText(artworks.get(currentArtworkIndex).toString());
            }
        } catch (Exception e) {
            Log.e(TAG, "加载艺术作品失败", e);
            Toast.makeText(this, "加载艺术作品失败: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void showPreviousArtwork() {
        try {
            if (artworks.isEmpty()) return;
            
            currentArtworkIndex--;
            if (currentArtworkIndex < 0) {
                currentArtworkIndex = artworks.size() - 1;
            }
            
            if (artworkDescriptionTextView != null) {
                artworkDescriptionTextView.setText(artworks.get(currentArtworkIndex).toString());
            }
            
            speakCurrentArtwork();
        } catch (Exception e) {
            Log.e(TAG, "显示上一幅作品失败", e);
            Toast.makeText(this, "显示上一幅作品失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private void showNextArtwork() {
        try {
            if (artworks.isEmpty()) return;
            
            currentArtworkIndex++;
            if (currentArtworkIndex >= artworks.size()) {
                currentArtworkIndex = 0;
            }
            
            if (artworkDescriptionTextView != null) {
                artworkDescriptionTextView.setText(artworks.get(currentArtworkIndex).toString());
            }
            
            speakCurrentArtwork();
        } catch (Exception e) {
            Log.e(TAG, "显示下一幅作品失败", e);
            Toast.makeText(this, "显示下一幅作品失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private void speakCurrentArtwork() {
        try {
            if (!artworks.isEmpty()) {
                Artwork currentArtwork = artworks.get(currentArtworkIndex);
                speakText("作品名称：" + currentArtwork.getTitle() + "。" + currentArtwork.getDescription());
            }
        } catch (Exception e) {
            Log.e(TAG, "朗读当前作品信息失败", e);
            Toast.makeText(this, "朗读作品信息失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
        } catch (Exception e) {
            Log.e(TAG, "触摸事件处理失败", e);
            return super.onTouchEvent(event);
        }
    }

    @Override
    protected void onDestroy() {
        try {
            if (textToSpeech != null) {
                textToSpeech.stop();
                textToSpeech.shutdown();
            }
        } catch (Exception e) {
            Log.e(TAG, "TTS资源释放失败: " + e.getMessage(), e);
        }
        super.onDestroy();
    }
    
    // 艺术作品内部类
    private static class Artwork {
        private final String title;
        private final String description;
        
        public Artwork(String title, String description) {
            this.title = title;
            this.description = description;
        }
        
        public String getTitle() {
            return title;
        }
        
        public String getDescription() {
            return description;
        }
        
        @Override
        public String toString() {
            return title + "\n\n" + description;
        }
    }
}