package com.maoqis.testcase.feature;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;

import com.github.maoqis.android.base.component.BaseFragment;
import com.maoqis.testcase.R;

public class RecreateActivityFragment extends BaseFragment {
    private static final String TAG = "StartActivityFragment";

    @Override
    protected int getRLayout() {
        return R.layout.fragment_recreate;
    }

    @Override
    protected void onInitView(View rootView) {
        Log.d(TAG, "onInitView: getActivity=" + getActivity());


        rootView.findViewById(R.id.tb_livedata_register_activity).setOnClickListener(v -> {
            FragmentActivity activity = getActivity();

            ViewModelStore viewModelStore = activity.getViewModelStore();
            Log.d(TAG, "register to activity: viewModelStore=" + viewModelStore);
            ViewModelProvider viewModelProvider = new ViewModelProvider(viewModelStore, getDefaultViewModelProviderFactory());
            MyViewModel myViewModel = viewModelProvider.get(MyViewModel.class);
            Log.d(TAG, "register to activity: myViewModel=" + myViewModel);
            final MediatorLiveData<String> liveData = myViewModel.test;
            Log.d(TAG, "register to activity: liveData=" + liveData);


            liveData.observe(this, t -> {
                Log.i(TAG, "onChanged() called with: liveData last data= [" + t + "] getActivity=" + getActivity().toString());

                Toast.makeText(getActivity(), "onChanged: liveData last data=" + liveData + "\n getActivity=" + getActivity().toString(), Toast.LENGTH_SHORT).show();
            });

        });

        rootView.findViewById(R.id.tb_livedata_post_activity).setOnClickListener(v -> {
            FragmentActivity activity = getActivity();


            ViewModelStore viewModelStore = activity.getViewModelStore();
            Log.d(TAG, "post to activity: viewModelStore=" + viewModelStore);
            ViewModelProvider viewModelProvider = new ViewModelProvider(viewModelStore, getDefaultViewModelProviderFactory());
            MyViewModel myViewModel = viewModelProvider.get(MyViewModel.class);
            Log.d(TAG, "post to activity: myViewModel=" + myViewModel);
            final MediatorLiveData<String> liveData = myViewModel.test;
            liveData.postValue(activity.toString());
            Log.d(TAG, "post to activity: postValue=" + activity.toString());
            Toast.makeText(activity, activity.toString() + "\n to " + liveData, Toast.LENGTH_SHORT).show();
        });


        rootView.findViewById(R.id.tb_livedata_register_fragment).setOnClickListener(v -> {
            FragmentActivity activity = getActivity();

            ViewModelStore viewModelStore = this.getViewModelStore();
            Log.d(TAG, "register to fragment: viewModelStore=" + viewModelStore);
            ViewModelProvider viewModelProvider = new ViewModelProvider(viewModelStore, getDefaultViewModelProviderFactory());
            MyViewModel myViewModel = viewModelProvider.get(MyViewModel.class);
            Log.d(TAG, "register to fragment: myViewModel=" + myViewModel);
            final MediatorLiveData<String> liveData = myViewModel.test;
            Log.d(TAG, "register to fragment: liveData=" + liveData);


            liveData.observe(this, t -> {
                Log.i(TAG, "onChanged() called with: liveData last data= [" + t + "] getActivity=" + getActivity().toString());

                Toast.makeText(getActivity(), "onChanged: liveData last data=" + liveData + "\n getActivity=" + getActivity().toString(), Toast.LENGTH_SHORT).show();
            });

        });

        rootView.findViewById(R.id.tb_livedata_post_fragment).setOnClickListener(v -> {
            FragmentActivity activity = getActivity();


            ViewModelStore viewModelStore = this.getViewModelStore();
            Log.d(TAG, "post to fragment: viewModelStore=" + viewModelStore);
            ViewModelProvider viewModelProvider = new ViewModelProvider(this);
            MyViewModel myViewModel = viewModelProvider.get(MyViewModel.class);
            Log.d(TAG, "post to fragment: myViewModel=" + myViewModel);
            final MediatorLiveData<String> liveData = myViewModel.test;
            liveData.postValue(activity.toString());
            Log.d(TAG, "post to fragment: postValue=" + activity.toString());
            Toast.makeText(activity, activity.toString() + "\n to " + liveData, Toast.LENGTH_SHORT).show();
        });

        rootView.findViewById(R.id.tb_vm_savedstatehandle_register).setOnClickListener(v -> {
            FragmentActivity activity = getActivity();

            ViewModelStore viewModelStore = activity.getViewModelStore();
            Log.d(TAG, "register to fragment: viewModelStore=" + viewModelStore);
            ViewModelProvider viewModelProvider = new ViewModelProvider(viewModelStore, this.getDefaultViewModelProviderFactory());
            SaveViewModel saveViewModel = viewModelProvider.get(SaveViewModel.class);
            Log.d(TAG, "register to fragment: saveViewModel=" + saveViewModel);
            final MediatorLiveData<String> liveData = saveViewModel.test;
            Log.d(TAG, "register to fragment: liveData=" + liveData);

            saveViewModel.getData("tb_vm_savedstatehandle_register");


            liveData.observe(this, t -> {
                Log.i(TAG, "onChanged() called with: liveData last data= [" + t + "] getActivity=" + getActivity().toString());

                Toast.makeText(getActivity(), "onChanged: liveData last data=" + liveData + "\n getActivity=" + getActivity().toString(), Toast.LENGTH_SHORT).show();
            });
        });

        rootView.findViewById(R.id.tb_vm_savedstatehandle_post).setOnClickListener(v -> {
            FragmentActivity activity = getActivity();

            ViewModelStore viewModelStore = activity.getViewModelStore();
            Log.d(TAG, "post to fragment: viewModelStore=" + viewModelStore);
            ViewModelProvider viewModelProvider = new ViewModelProvider(viewModelStore, this.getDefaultViewModelProviderFactory());
            SaveViewModel saveViewModel = viewModelProvider.get(SaveViewModel.class);
            Log.d(TAG, "post to fragment: saveViewModel=" + saveViewModel);
            final MediatorLiveData<String> liveData = saveViewModel.test;
            Log.d(TAG, "post to fragment: liveData=" + liveData);

            liveData.postValue(activity.toString());
            Log.d(TAG, "post to activity: postValue=" + activity.toString());

            saveViewModel.saveData("SavedStateHandle=" + activity, "tb_vm_savedstatehandle_post");
            Toast.makeText(activity, activity.toString() + "\n to " + liveData, Toast.LENGTH_SHORT).show();
        });


    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewStateRestored: 恢复时候2次调用问题");
        super.onViewStateRestored(savedInstanceState);
//        saveViewModel.getData("onViewStateRestored");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: ");
//        saveViewModel.saveData("SavedStateHandle=" + activity);
    }

    public static class MyViewModel extends ViewModel {
        public MediatorLiveData<String> test = new MediatorLiveData<>();
    }

    public static class SaveViewModel extends ViewModel {
        public static final String KEY_SAVE_DATA = "KEY_SAVE_DATA";
        private final SavedStateHandle savedStateHandle;
        public MediatorLiveData<String> test = new MediatorLiveData<>();


        public SaveViewModel(SavedStateHandle savedStateHandle) {
            this.savedStateHandle = savedStateHandle;
            init();
        }

        private void init() {
            getData("init");
        }


        public void saveData(String data, String from) {
            Log.d(TAG, "saveData() called with: data = [" + data + "], from = [" + from + "]");
            savedStateHandle.set(KEY_SAVE_DATA, data);
        }

        public String getData(String from) {

            if (savedStateHandle == null) {
                Log.d(TAG, from + ": savedStateHandle=" + null);
                return null;
            }

            if (!savedStateHandle.contains(KEY_SAVE_DATA)) {
                Log.i(TAG, from + ": not contains KEY_SAVE_DATA");
                return null;
            }

            String ret = savedStateHandle.get(KEY_SAVE_DATA);
            Log.i(TAG, from + ": contains KEY_SAVE_DATA=" + ret);
            return ret;
        }
    }


}
