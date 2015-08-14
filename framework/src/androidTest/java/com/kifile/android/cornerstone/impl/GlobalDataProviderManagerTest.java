package com.kifile.android.cornerstone.impl;

import junit.framework.TestCase;

/**
 * Testcase for {@link GlobalDataProviderManager}.
 *
 * @author kifile
 */
public class GlobalDataProviderManagerTest extends TestCase {

    public static final String KEY_TEST_PROVIDER = "key_test_provider";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        GlobalDataProviderManager manager = GlobalDataProviderManager.getInstance();
        manager.register(KEY_TEST_PROVIDER, CombinedDataProvider.class);
    }

    public void testRegister() throws Exception {
        CombinedDataProvider provider =
                (CombinedDataProvider) GlobalDataProviderManager.getInstance().obtainProvider(KEY_TEST_PROVIDER);
        GlobalDataProviderManager.getInstance().releaseProvider(KEY_TEST_PROVIDER);
    }

}