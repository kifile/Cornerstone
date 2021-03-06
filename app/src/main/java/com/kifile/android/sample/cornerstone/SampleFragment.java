package com.kifile.android.sample.cornerstone;

import android.app.Activity;
import android.app.Fragment;

import com.kifile.android.cornerstone.impl.Cornerstone;
import com.kifile.android.sample.cornerstone.data.SampleDataProvider;

/**
 * The sample of using DataProvider in Fragment.
 *
 * @author kifile
 */
public class SampleFragment extends Fragment {

    private SampleDataProvider mProvider;

//    private DataObserver<JSONObject> mObserver = new DataObserver<JSONObject>() {
//        @Override
//        public void onDataChanged(JSONObject jsonObject) {
//            getActivity().setTitle(jsonObject.toString());
//        }
//    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (mProvider == null) {
            mProvider =
                    (SampleDataProvider) Cornerstone.obtainProvider(SampleDataProvider.KEY);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        mProvider.registerDataObserver(mObserver);
    }

    @Override
    public void onPause() {
//        mProvider.unregisterDataObserver(mObserver);
        super.onPause();
    }

    @Override
    public void onDetach() {
        if (mProvider != null) {
            Cornerstone.releaseProvider(SampleDataProvider.KEY);
            mProvider = null;
        }
        super.onDestroy();
    }
}
