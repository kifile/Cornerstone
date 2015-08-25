package com.kifile.android.cornerstone.impl;

import junit.framework.TestCase;

/**
 * Testcase for {@link Cornerstone}.
 *
 * @author kifile
 */
public class CornerstoneTest extends TestCase {

    public static final String KEY_TEST_PROVIDER = "key_test_provider";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Cornerstone manager = Cornerstone.getInstance();
        manager.register(KEY_TEST_PROVIDER, CombinedDataProvider.class);
    }

    public void testRegister() throws Exception {
        CombinedDataProvider provider =
                (CombinedDataProvider) Cornerstone.getInstance().obtainProvider(KEY_TEST_PROVIDER);
        Cornerstone.getInstance().releaseProvider(KEY_TEST_PROVIDER);
    }

}