package com.kifile.android.cornerstone.core;

import java.util.HashMap;
import java.util.Map;

import com.kifile.android.cornerstone.BuildConfig;

import android.util.Log;

/**
 * AbstractDataProviderManager provides a easy way to manage data.
 * <h3>Introduce</h3>
 * <p>We use Automatic Counting to manage data, release the resources only if nobody use it.</p>
 * <h3>How To Use</h3>
 * <p><strong>Notice: </strong>You should release the data provider by yourself.</p>
 * <p>In most cases, we use data provider in Activity or Fragment.</p>
 * <ul>
 * <li>For Activity:
 * <pre class="prettyprint">
 * public class SampleActivity extends Activity {
 *      public static final String KEY = "SampleDataProvider";
 *      private SampleDataProvider mProvider;
 *
 *      protected void onCreate(Bundle savedInstanceState) {
 *          super.onCreate(savedInstanceState);
 *          if (mProvider == null) {
 *              mProvider = (SampleDataProvider) GlobalDataProviderManager.getInstance().obtainProvider(KEY);
 *          }
 *      }
 *
 *      protected void onDestroy() {
 *          if (mProvider != null) {
 *              GlobalDataProviderManager.getInstance().releaseProvider(KEY);
 *              mProvider = null;
 *          }
 *          super.onDestroy();
 *      }
 *
 *      public static class SampleDataProvider extends AbstractDataProvider {

 *          public void refresh() {
 *          }
 *      }
 * }
 * </pre>
 * </li>
 * <li>For Fragment:
 * <pre class="prettyprint">
 * public class SampleFragment extends Fragment {
 *      public static final String KEY = "SampleDataProvider";
 *      private SampleDataProvider mProvider;
 *
 *      protected void onAttach(Activity activity) {
 *          super.onAttach(Activity activity);
 *          if (mProvider == null) {
 *              mProvider = (SampleDataProvider) GlobalDataProviderManager.getInstance().obtainProvider(KEY);
 *          }
 *      }
 *
 *      protected void onDetach() {
 *          if (mProvider != null) {
 *              GlobalDataProviderManager.getInstance().releaseProvider(KEY);
 *              mProvider = null;
 *          }
 *          super.onDetach();
 *      }
 *
 *      public static class SampleDataProvider extends AbstractDataProvider {

 *          public void refresh() {
 *          }
 *      }
 * }
 * </pre>
 * </li>
 * </ul>
 * You should call {@link #register(String, Class)} before you use a {@link DataProvider}
 */
public abstract class AbstractDataProviderManager {

    public static final String TAG = AbstractDataProviderManager.class.getSimpleName();

    private final Map<String, Class<? extends DataProvider>> mDataProviderClasses = new HashMap<>();

    private final Map<String, ProviderCounter> mDataProviders = new HashMap<>();

    /**
     * Called when initialize the manager.
     *
     * @param key
     * @param providerClazz
     */
    public void register(String key, Class<? extends DataProvider> providerClazz) {
        if (key == null) {
            throw new NullPointerException("The key cannot be null.");
        }
        if (providerClazz == null) {
            throw new NullPointerException("The provider class cannot be null");
        }
        mDataProviderClasses.put(key, providerClazz);
    }

    /**
     * Obtain your {@link DataProvider} from you registered in initialize.
     *
     * @param key
     *
     * @return
     */
    public synchronized DataProvider obtainProvider(String key) {
        DataProvider provider = null;
        ProviderCounter providerCounter = mDataProviders.get(key);
        if (providerCounter != null) {
            providerCounter = mDataProviders.get(key);
            providerCounter.increase();
            provider = providerCounter.getProvider();
        }
        if (provider == null) {
            Class<? extends DataProvider> providerClazz = mDataProviderClasses.get(key);
            if (providerClazz != null) {
                try {
                    provider = providerClazz.newInstance();
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "Init new provider: " + providerClazz.getSimpleName());
                    }
                    ProviderCounter counter = new ProviderCounter(provider);
                    counter.increase();
                    mDataProviders.put(key, counter);
                } catch (InstantiationException e) {
                    throw new RuntimeException("Cannot create a new Instance:" + providerClazz.getName());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Cannot create a new Instance:" + providerClazz.getName());
                }
            } else {
                throw new RuntimeException("Provider class not registered.");
            }
        }
        return provider;
    }

    /**
     * Release the {@link DataProvider}, and recycle resources if possible.
     *
     * @param key
     */
    public synchronized void releaseProvider(String key) {
        ProviderCounter providerCounter = mDataProviders.get(key);
        if (providerCounter != null) {
            providerCounter = mDataProviders.get(key);
            providerCounter.decrease();
            if (providerCounter.empty()) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "ReleaseProvider: " + key);
                }
                providerCounter.getProvider().recycle();
                mDataProviders.remove(key);
            }
        }
    }

    /**
     * ProviderCounter is a helper class to compute the use count of a DataProvider.
     * <p/>
     * The DataProviderManager will release sources only the {@link #mCounter} is zero.
     */
    public class ProviderCounter {

        // The count of provider been used.
        private int mCounter;

        // The provider which is been watched.
        private final DataProvider mProvider;

        public ProviderCounter(DataProvider provider) {
            mProvider = provider;
            mCounter = 0;
        }

        public void increase() {
            mCounter++;
        }

        public void decrease() {
            mCounter--;
        }

        public DataProvider getProvider() {
            return mProvider;
        }

        public boolean empty() {
            return mCounter <= 0;
        }
    }
}
