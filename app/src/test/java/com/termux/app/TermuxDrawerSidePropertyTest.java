package com.termux.app;

import com.termux.shared.termux.settings.properties.TermuxPropertyConstants;
import com.termux.shared.termux.settings.properties.TermuxSharedProperties;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Tests for the `terminal-drawer-side` termux.properties key parsing and its default behaviour.
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 33)
public class TermuxDrawerSidePropertyTest {

    private String parse(String value) {
        return (String) TermuxSharedProperties.getTerminalDrawerSideInternalPropertyValueFromValue(value);
    }

    @Test
    public void testDrawerSideDefault() {
        // The drawer must open from the start side like before the property existed
        Assert.assertEquals(TermuxPropertyConstants.IVALUE_TERMINAL_DRAWER_SIDE_START,
            TermuxPropertyConstants.DEFAULT_IVALUE_TERMINAL_DRAWER_SIDE);
        Assert.assertEquals(TermuxPropertyConstants.IVALUE_TERMINAL_DRAWER_SIDE_START, parse(null));
    }

    @Test
    public void testDrawerSideValidValues() {
        Assert.assertEquals(TermuxPropertyConstants.IVALUE_TERMINAL_DRAWER_SIDE_START, parse("start"));
        Assert.assertEquals(TermuxPropertyConstants.IVALUE_TERMINAL_DRAWER_SIDE_END, parse("end"));
    }

    @Test
    public void testDrawerSideCaseInsensitive() {
        Assert.assertEquals(TermuxPropertyConstants.IVALUE_TERMINAL_DRAWER_SIDE_END, parse("END"));
        Assert.assertEquals(TermuxPropertyConstants.IVALUE_TERMINAL_DRAWER_SIDE_START, parse("StArT"));
        Assert.assertEquals(TermuxPropertyConstants.IVALUE_TERMINAL_DRAWER_SIDE_END, parse("End"));
        Assert.assertEquals(TermuxPropertyConstants.IVALUE_TERMINAL_DRAWER_SIDE_START, parse("Start"));
    }

    @Test
    public void testDrawerSideInvalidValuesFallBackToDefault() {
        Assert.assertEquals(TermuxPropertyConstants.DEFAULT_IVALUE_TERMINAL_DRAWER_SIDE, parse(""));
        Assert.assertEquals(TermuxPropertyConstants.DEFAULT_IVALUE_TERMINAL_DRAWER_SIDE, parse("left"));
        Assert.assertEquals(TermuxPropertyConstants.DEFAULT_IVALUE_TERMINAL_DRAWER_SIDE, parse("right"));
        Assert.assertEquals(TermuxPropertyConstants.DEFAULT_IVALUE_TERMINAL_DRAWER_SIDE, parse("middle"));
        Assert.assertEquals(TermuxPropertyConstants.DEFAULT_IVALUE_TERMINAL_DRAWER_SIDE, parse("0"));
    }

}
