/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.durui.feat.software_interface.ui.exercise;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.durui.feat.computer_vision.vision_base.VisionImageProcessor;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

import timber.log.Timber;

/**
 * ViewModel containing all the logic needed to run the game
 */
public class MyCameraXViewModel extends AndroidViewModel implements LifecycleEventObserver {
    /**
     * Vibration is controlled by passing in an array representing the number of milliseconds each interval of buzzing and non-buzzing takes.
     * So the array [0, 200, 100, 300] will wait 0 milliseconds, then buzz for 200ms, then wait 100ms, then buzz fo 300ms.
     */
//    public final long[] CORRECT_BUZZ_PATTERN = new long[]{100L, 100L, 100L, 100L, 100L, 100L};
//    public final long[] PANIC_BUZZ_PATTERN = new long[]{0L, 200L};
//    public final long[] GAME_OVER_BUZZ_PATTERN = new long[]{0L, 2000L};
//    public final long[] NO_BUZZ_PATTERN = new long[]{0L};

//    // This is when the game is over
//    final Long DONE = 0L;
//    // This is the number of milliseconds in a second
//    final Long ONE_SECOND = 1000L;
//    // This is the total time of the game
//    final Long COUNTDOWN_TIME = 10000L;
//    CountDownTimer timer;
//    MutableLiveData<Long> _currentTime;//可以使用Transformations.map + XML Binding Expression

    public static Integer reps = 0;
    public static MutableLiveData<Integer> Reps = new MutableLiveData<>();

    //记录事件
    MutableLiveData<Boolean> _eventTaskFinished;

    //异步申请
    MutableLiveData<ProcessCameraProvider> _cameraProvider;
    //变量迁移
    CameraSelector cameraSelector;
    int lensFacing = CameraSelector.LENS_FACING_BACK;//镜头标号
    @Nullable
    Preview previewUseCase;
    @Nullable
    ImageAnalysis analysisUseCase;
    @Nullable
    VisionImageProcessor imageProcessor;
    boolean needUpdateGraphicOverlayImageSourceInfo;

    // Create an instance which interacts with the camera service via the given application context.
    // TODO 其他参数需要 委托自建 ViewModelFactory 执行
    public MyCameraXViewModel(@NonNull Application application) {
        super(application);
        cameraSelector = new CameraSelector.Builder().requireLensFacing(lensFacing).build();
        Reps.setValue(0);

    }

    public static void setReps(int repsAfter) {
        reps=repsAfter;
    }

    public static Integer getReps() {
        return reps;
    }

    // FIXME setValue MutableLiveData.setValue(java.lang.Object)' on a null object reference
//        _eventTaskFinished = new MutableLiveData<>();
//        _currentTime = new MutableLiveData<>();
//        timer = new CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                // TODO implement what should happen each tick of the timer
//                _currentTime.setValue((millisUntilFinished / ONE_SECOND));
//            }
//
//            @Override
//            public void onFinish() {
//                // TODO implement what should happen when the timer finishes
//                _currentTime.setValue(DONE);
//                _eventTaskFinished.setValue(true);
//            }
//        };
//
//        timer.start();
//    }

    // setter
    public void setCameraSelector(CameraSelector cameraSelector) {
        this.cameraSelector = cameraSelector;
    }

    public void setLensFacing(int lensFacing) {
        this.lensFacing = lensFacing;
    }

    public void setPreviewUseCase(@Nullable Preview previewUseCase) {
        this.previewUseCase = previewUseCase;
    }

    public void setAnalysisUseCase(@Nullable ImageAnalysis analysisUseCase) {
        this.analysisUseCase = analysisUseCase;
    }

    public void setImageProcessor(@Nullable VisionImageProcessor imageProcessor) {
        this.imageProcessor = imageProcessor;
    }

    public void setNeedUpdateGraphicOverlayImageSourceInfo(
            boolean needUpdateGraphicOverlayImageSourceInfo) {
        this.needUpdateGraphicOverlayImageSourceInfo = needUpdateGraphicOverlayImageSourceInfo;
    }

    // getter
    public CameraSelector getCameraSelector() {
        return cameraSelector;
    }

    public int getLensFacing() {
        return lensFacing;
    }

    @Nullable
    public Preview getPreviewUseCase() {
        return previewUseCase;
    }

    @Nullable
    public ImageAnalysis getAnalysisUseCase() {
        return analysisUseCase;
    }

    @Nullable
    public VisionImageProcessor getImageProcessor() {
        return imageProcessor;
    }

    public boolean isNeedUpdateGraphicOverlayImageSourceInfo() {
        return needUpdateGraphicOverlayImageSourceInfo;
    }

//    public LiveData<Long> getCurrentTime() {
//        return _currentTime;
//    }

    public LiveData<Boolean> getEventTaskFinished() {
        return _eventTaskFinished;
    }

    //durui outside the view model, it is exposed as live data that is not mutable
    public LiveData<ProcessCameraProvider> getCameraProvider() {
        if (_cameraProvider == null) {
            _cameraProvider = new MutableLiveData<>();
            ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this.getApplication());
            cameraProviderFuture.addListener(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Camera provider is now guaranteed to be available
                        // durui inside the view model, live data is mutable
                        _cameraProvider.setValue(cameraProviderFuture.get());
                    } catch (ExecutionException | InterruptedException e) {
                        // Handle any errors (including cancellation) here.
                        Timber.e(e, "Unhandled exception");
                    }
                }
            }, ContextCompat.getMainExecutor(getApplication()));
        }
        return _cameraProvider;
    }

    //请求新镜头
    public int newLensFacing() {
        return lensFacing == CameraSelector.LENS_FACING_FRONT
                ? CameraSelector.LENS_FACING_BACK
                : CameraSelector.LENS_FACING_FRONT;
    }

    //设置观察对象
    public void observe(Lifecycle lifecycle) {
        lifecycle.addObserver(this);
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        switch (event) {
            case ON_CREATE:
                Timber.d("CREATED");
                break;
            case ON_START:
                Timber.d("STARTED");
                break;
            case ON_RESUME:
                Timber.d("RESUMED");
                break;
            case ON_PAUSE:
                Timber.d("PAUSED");
                break;
            case ON_STOP:
                Timber.d("STOPPED");
                if (imageProcessor != null) imageProcessor.stop();
                break;
            case ON_DESTROY:
                Timber.d("DESTROYED");
                if (imageProcessor != null) imageProcessor.stop();
                break;
        }
    }

    /**
     * Methods for completed events
     **/

    @Override
    protected void onCleared() {
        super.onCleared();
        //timer.cancel();
    }

//    public void onTaskFinished() {
//        _eventTaskFinished.setValue(false);
//    }
}
