package com.durui.feat.software_interface.ui.home;

import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;

import com.durui.feat.software_interface.ui.BaseActivity;
import com.durui.feat.software_interface.ui.R;
import com.durui.feat.software_interface.ui.databinding.ActivityHomeBinding;


public class HomeActivity extends BaseActivity {
    ActivityHomeBinding binding;
    int previousId = R.id.nav_item_workout;
    Boolean homeFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        binding.btnNavigationView.setSelectedItemId(previousId);

        //FIXME NavController controller = Navigation.findNavController(this, R.id.anchor_workout);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController controller = navHostFragment.getNavController();

        controller.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, NavDestination navDestination, @Nullable Bundle bundle) {
                homeFlag = navDestination.getId() == navController.getGraph().getStartDestinationId();
            }
        });

        //导航接管Fragment目的地显示
        binding.btnNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_item_workout:
                    if (previousId == R.id.nav_item_statistics)
                        controller.navigate(R.id.action_dataFragment_to_workoutFragment);
                    if (previousId == R.id.nav_item_me)
                        controller.navigate(R.id.action_meFragment_to_workoutFragment);
                    previousId = R.id.nav_item_workout;
                    return true;
                case R.id.nav_item_statistics:
                    if (previousId == R.id.nav_item_workout)
                        controller.navigate(R.id.action_workoutFragment_to_dataFragment);
                    if (previousId == R.id.nav_item_me)
                        controller.navigate(R.id.action_meFragment_to_dataFragment);
                    previousId = R.id.nav_item_statistics;
                    return true;
                case R.id.nav_item_me:
                    if (previousId == R.id.nav_item_workout)
                        controller.navigate(R.id.action_workoutFragment_to_meFragment);
                    if (previousId == R.id.nav_item_statistics)
                        controller.navigate(R.id.action_dataFragment_to_meFragment);
                    previousId = R.id.nav_item_me;
                    return true;
            }
            return false;
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //当用户处于非主页时，第一次返回到主页，第二次返回桌面
        if (keyCode == KeyEvent.KEYCODE_BACK && !homeFlag) {
            binding.btnNavigationView.setSelectedItemId(R.id.nav_item_workout);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}