package com.durui.feat.software_interface.ui.exercise;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Size;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.camera.core.CameraInfoUnavailableException;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.durui.feat.computer_vision.pose_detector.PoseDetectorProcessor;
import com.durui.feat.computer_vision.preference.PreferenceUtils;
import com.durui.feat.computer_vision.vision_base.GraphicOverlay;
import com.durui.feat.computer_vision.vision_base.VisionImageProcessor;
import com.durui.feat.software_interface.data.db.WorkoutDatabase;
import com.durui.feat.software_interface.data.table.LiveRecord;
import com.durui.feat.software_interface.ui.BaseActivity;
import com.durui.feat.software_interface.ui.R;
import com.durui.feat.software_interface.ui.TopWindowUtils;
import com.durui.feat.software_interface.ui.databinding.ActivityLivePreviewBinding;
import com.durui.feat.software_interface.ui.evaluation.RecordActivity;
import com.durui.feat.software_interface.ui.login.LoginViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.mlkit.common.MlKitException;
import com.google.mlkit.vision.pose.PoseDetectorOptionsBase;

import timber.log.Timber;

/**
 * 核心改造：分离视图层和数据层，分离组件生命周期依赖
 * 核心框架：向用户提供预览或录制视频以及分析图片流
 */
public final class LivePreviewActivity extends BaseActivity {
    //数据库
    WorkoutDatabase db;
    LiveRecord liveRecord = new LiveRecord();

    //视图绑定
    private ActivityLivePreviewBinding binding;
    private PreviewView previewView;
    private GraphicOverlay graphicOverlay;
    //数据模型驱动界面
    private MyCameraXViewModel myCameraXViewModel;
    @Nullable
    private ProcessCameraProvider cameraProvider;
    //语音播报
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_live_preview);
        previewView = binding.previewView;
        graphicOverlay = binding.graphicOverlay;

        myCameraXViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
                .get(MyCameraXViewModel.class);
        myCameraXViewModel.observe(this.getLifecycle());

        MyCameraXViewModel.Reps.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                binding.textView.setText(integer.toString());
            }
        });


//        myCameraXViewModel.getCurrentTime().observe(this, newTimeMillis -> {
//            binding.timeText.setText(DateUtils.formatElapsedTime(newTimeMillis));
//        });
//
//        myCameraXViewModel.getEventTaskFinished().observe(this, hasFinished -> {
//            if (hasFinished) {
//                taskFinished();
//                myCameraXViewModel.onTaskFinished();
//            }
//            //buzz(myCameraXViewModel.GAME_OVER_BUZZ_PATTERN);
//        });

        //TODO 没有什么是真的，一切皆有可能发生 ————Bruce Eckel
        myCameraXViewModel.getCameraProvider().observe(this, newCameraProvider -> {
            Timber.tag("durui").d("CameraProvider GOT");
            cameraProvider = newCameraProvider;
            bindAllCameraUseCases();
        });

        binding.settingsButton.setOnClickListener(buttonView -> {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            intent.putExtra(
                    SettingsActivity.EXTRA_LAUNCH_SOURCE,
                    SettingsActivity.LaunchSource.CAMERAX_LIVE_PREVIEW);
            startActivity(intent);
        });

        binding.facingSwitch.setOnClickListener(buttonView -> {
            //binding.progressLivePreview.setVisibility(View.VISIBLE);
            if (cameraProvider == null) return;

            int newLensFacing = myCameraXViewModel.newLensFacing();
            CameraSelector newCameraSelector = new CameraSelector.Builder().requireLensFacing(newLensFacing).build();
            try {
                if (cameraProvider.hasCamera(newCameraSelector)) {

                    myCameraXViewModel.setLensFacing(newLensFacing);
                    myCameraXViewModel.setCameraSelector(newCameraSelector);
                    bindAllCameraUseCases();

                    TopWindowUtils.show(this, "镜头切换成功", true);
                    binding.progressLivePreview.setVisibility(View.GONE);
                    return;
                }
            } catch (CameraInfoUnavailableException e) {
                // Falls through
            }
            Timber.d("This device does not have lens with facing: %s", newLensFacing);
        });

        db = WorkoutDatabase.getInstance(this);
        binding.stopBtn.setOnClickListener(btnView -> {
            Intent intent = new Intent(LivePreviewActivity.this, RecordActivity.class);
            liveRecord.live_user_id = LoginViewModel.phone;
            liveRecord.live_sports_id = 2L;
            liveRecord.live_sports_num = Long.valueOf(MyCameraXViewModel.reps);
            //android.database.sqlite.SQLiteConstraintException: UNIQUE constraint failed: live_record_table.live_id (code 1555 SQLITE_CONSTRAINT_PRIMARYKEY)
            db.liveRecordDao().insert(liveRecord);
            startActivity(intent);
            finish();
        });

        initTTS();
    }

    void bindAllCameraUseCases() {
        if (cameraProvider != null) {
            // As required by CameraX API, unbinds all use cases before trying to re-bind any of them.
            cameraProvider.unbindAll();
            bindPreviewUseCase();
            bindAnalysisUseCase();
        }
    }

    //TODO 强烈建议打开预览
    private void bindPreviewUseCase() {
        //请在设置中检查检查，是否开启预览
        if (!PreferenceUtils.isCameraLiveViewportEnabled(this)) {
            return;
        }
        if (cameraProvider == null) {
            return;
        }
        if (myCameraXViewModel.getPreviewUseCase() != null) {
            cameraProvider.unbind(myCameraXViewModel.getPreviewUseCase());
        }

        Preview.Builder builder = new Preview.Builder();
        Size targetResolution = PreferenceUtils.getCameraXTargetResolution(this, myCameraXViewModel.getLensFacing());
        if (targetResolution != null) {
            builder.setTargetResolution(targetResolution);
        }
        myCameraXViewModel.setPreviewUseCase(builder.build());
        myCameraXViewModel.getPreviewUseCase().setSurfaceProvider(previewView.getSurfaceProvider());

        cameraProvider.bindToLifecycle(/* lifecycleOwner= */ this,
                myCameraXViewModel.getCameraSelector(),
                myCameraXViewModel.getPreviewUseCase());
    }

    private void bindAnalysisUseCase() {
        if (cameraProvider == null) {
            return;
        }
        if (myCameraXViewModel.getAnalysisUseCase() != null) {
            cameraProvider.unbind(myCameraXViewModel.getAnalysisUseCase());
        }
        if (myCameraXViewModel.getImageProcessor() != null) {
            myCameraXViewModel.getImageProcessor().stop();
        }

        try {
            //样例中包含多种处理机制，这里只选择一种（人体关键点识别）
            PoseDetectorOptionsBase poseDetectorOptions =
                    PreferenceUtils.getPoseDetectorOptionsForLivePreview(this);
            boolean shouldShowInFrameLikelihood =
                    PreferenceUtils.shouldShowPoseDetectionInFrameLikelihoodLivePreview(this);
            boolean visualizeZ = PreferenceUtils.shouldPoseDetectionVisualizeZ(this);
            boolean rescaleZ = PreferenceUtils.shouldPoseDetectionRescaleZForVisualization(this);
            boolean runClassification = PreferenceUtils.shouldPoseDetectionRunClassification(this);
            VisionImageProcessor newImageProcessor =
                    new PoseDetectorProcessor(
                            this,
                            poseDetectorOptions,
                            shouldShowInFrameLikelihood,
                            visualizeZ,
                            rescaleZ,
                            runClassification,
                            /* isStreamMode = */ true);    //指定检测方式：STREAM_MODE/SINGLE_IMAGE_MODE

            myCameraXViewModel.setImageProcessor(newImageProcessor);

        } catch (Exception e) {
            Timber.d("Can not create image processor: %s", e.getLocalizedMessage());
            return;
        }

        //将分析器（图像使用方）连接到 CameraX（图像生成方）并在可视化层显示（结果输出方）
        ImageAnalysis.Builder builder = new ImageAnalysis.Builder();
        Size targetResolution = PreferenceUtils.getCameraXTargetResolution(this, myCameraXViewModel.getLensFacing());
        if (targetResolution != null) {
            builder.setTargetResolution(targetResolution);
        }
        myCameraXViewModel.setAnalysisUseCase(builder.build());

        /**
         * 提供元数据输出，以便了解图片缓冲区坐标相对于显示坐标的位置
         * 每创建/继续时，构建一次分析场景；每构建一次分析场景，更新一次镜头与方向
         */
        myCameraXViewModel.setNeedUpdateGraphicOverlayImageSourceInfo(true);
        myCameraXViewModel.getAnalysisUseCase().setAnalyzer(
                // imageProcessor.processImageProxy will use another thread to run the detection underneath,
                // thus we can just runs the analyzer itself on main thread.
                ContextCompat.getMainExecutor(this),
                imageProxy -> {
                    if (myCameraXViewModel.isNeedUpdateGraphicOverlayImageSourceInfo()) {
                        boolean isImageFlipped = myCameraXViewModel.getLensFacing() == CameraSelector.LENS_FACING_FRONT;
                        /**
                         * 用例需要了解旋转角度才能以正确的方向进行面部检测
                         * 此值表示需要将图片顺时针旋转多少度才能与 ImageAnalysis 的目标旋转角度保持一致
                         * 在 Android 应用中，ImageAnalysis 的目标旋转角度通常与屏幕的方向一致
                         */
                        int rotationDegrees = imageProxy.getImageInfo().getRotationDegrees();
                        if (rotationDegrees == 0 || rotationDegrees == 180) {
                            graphicOverlay.setImageSourceInfo(
                                    imageProxy.getWidth(), imageProxy.getHeight(), isImageFlipped);
                        } else {
                            graphicOverlay.setImageSourceInfo(
                                    imageProxy.getHeight(), imageProxy.getWidth(), isImageFlipped);
                        }
                        myCameraXViewModel.setNeedUpdateGraphicOverlayImageSourceInfo(false);
                    }
                    try {
                        //imageProcessor.processImageProxy will use another thread to run the detection underneath, thus we can just runs the analyzer itself on main thread.
                        myCameraXViewModel.getImageProcessor().processImageProxy(imageProxy, graphicOverlay);
                    } catch (MlKitException e) {
                        Timber.e("Failed to process image. Error: %s", e.getLocalizedMessage());
                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT)
                                .show();
                    }
                });

        // 何时开关？哪颗镜头？用它干嘛？
        cameraProvider.bindToLifecycle(/* lifecycleOwner= */ this,
                myCameraXViewModel.getCameraSelector(),
                myCameraXViewModel.getAnalysisUseCase());
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindAllCameraUseCases();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //中断当前话语
        textToSpeech.stop();
        //释放资源
        textToSpeech.shutdown();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { //判断是否是返回键
            //从 AlertDialog 迁移至 Material 3
            new MaterialAlertDialogBuilder(this)
                    // Add customization options here
                    .setTitle("确定终止运动吗？")
                    .setMessage("锲而舍之，朽木不折；锲而不舍，金石可镂。天将降大任于是人也，必先苦其心志，劳其筋骨，饿其体肤，空乏其身，行拂乱其所为。")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }
        return false;
    }

    /**
     * 作者：花落归零
     * 链接：https://juejin.cn/post/7069404709861195807
     * 来源：稀土掘金
     */
    void initTTS() {
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                //初始化成功
                if (status == TextToSpeech.SUCCESS) {
                    Timber.d("init success");
                } else {
                    Timber.d("init fail");
                }
            }
        });
        //设置语言
        //        int result = textToSpeech.setLanguage(Locale.CHINESE);
        //        if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE
        //                && result != TextToSpeech.LANG_AVAILABLE) {
        //            Toast.makeText(this, "不支持语音的朗读！",
        //                    Toast.LENGTH_SHORT).show();
        //        }
        //设置音调
        textToSpeech.setPitch(1.0f);
        //设置语速，1.0为正常语速
        textToSpeech.setSpeechRate(1.5f);
    }

    public TextToSpeech getTextToSpeech() {
        return textToSpeech;
    }

    void taskFinished() {
        Toast.makeText(this, "任务已完成", Toast.LENGTH_SHORT).show();
    }

    private void buzz(long[] pattern) {
        Vibrator buzzer = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

        if (buzzer != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1));
                //deprecated in API 26 buzzer.vibrate(pattern, -1)
            }

        }
    }
}