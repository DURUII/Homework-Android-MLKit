package com.durui.feat.software_interface.ui.info;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;

import com.durui.feat.software_interface.ui.BaseActivity;
import com.durui.feat.software_interface.ui.R;
import com.durui.feat.software_interface.ui.databinding.ActivityInfoBinding;
import com.durui.feat.software_interface.ui.home.HomeActivity;

public class InfoActivity extends BaseActivity {

    ActivityInfoBinding binding;
    InfoViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_info);
        viewModel = new ViewModelProvider(this).get(InfoViewModel.class);


        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_info);
        NavController controller = navHostFragment.getNavController();
        //点击iceCard
        boolean indexOnlyFlag = getIntent().getBooleanExtra("index_only", false);
        if (viewModel.hasFilled() && !indexOnlyFlag) {
            Intent intent = new Intent(InfoActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        if (indexOnlyFlag) {
            controller.navigate(R.id.action_avatarFragment_to_heightFragment);
        }
        controller.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, NavDestination navDestination, @Nullable Bundle bundle) {
                switch (navDestination.getId()) {
                    case R.id.avatarFragment:
                        binding.progressInfo.setProgressCompat(0, true);
                        break;
                    case R.id.sexFragment:
                        binding.progressInfo.setProgressCompat(25, true);
                        break;
                    case R.id.birthFragment:
                        binding.progressInfo.setProgressCompat(50, true);
                        break;
                    case R.id.heightFragment:
                        binding.progressInfo.setProgressCompat(75, true);
                        break;
                    case R.id.weightFragment:
                        binding.progressInfo.setProgressCompat(100, true);
                        break;
                }
            }
        });
    }
}