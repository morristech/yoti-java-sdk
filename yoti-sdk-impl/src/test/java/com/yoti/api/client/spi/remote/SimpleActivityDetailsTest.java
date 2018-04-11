package com.yoti.api.client.spi.remote;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bouncycastle.util.encoders.Base64;
import org.junit.Test;
import org.mockito.Mockito;

import com.yoti.api.client.Attribute;
import com.yoti.api.client.Profile;

public class SimpleActivityDetailsTest {
    private static final String USER_ID = "YmFkYWRhZGEtZGFkYWJhZGEK";
    private static final Profile USER_PROFILE = Mockito.mock(Profile.class);
    private static final Profile APP_PROFILE = Mockito.mock(Profile.class);
    private static final Profile USER_PROFILE_WRAPPER = HumanProfileAdapter.wrap(USER_PROFILE);
    private static final Profile APP_PROFILE_WRAPPER = ApplicationProfileAdapter.wrap(APP_PROFILE);
    private static final byte[] RECEIPT_ID = { 1, 2, 3, 4, 5, 6, 7, 8 };
    private static final String RECEIPT_ID_STRING = Base64.toBase64String(RECEIPT_ID);
    private static final Date TIMESTAMP = new Date();

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailConstructionForNullRememberMeId() {
        new SimpleActivityDetails(null, USER_PROFILE, APP_PROFILE, TIMESTAMP, RECEIPT_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailConstructionForNullUserProfile() {
        new SimpleActivityDetails(USER_ID, null, APP_PROFILE, TIMESTAMP, RECEIPT_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailConstructionForNullAppProfile() {
        new SimpleActivityDetails(USER_ID, USER_PROFILE, null, TIMESTAMP, RECEIPT_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailConstructionForNullTimestamp() {
        new SimpleActivityDetails(USER_ID, USER_PROFILE, APP_PROFILE, null, RECEIPT_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailConstructionForNullReceiptId() {
        new SimpleActivityDetails(USER_ID, USER_PROFILE, APP_PROFILE, TIMESTAMP, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailConstructionForNullProfile() {
        new SimpleActivityDetails(USER_ID, null, APP_PROFILE, TIMESTAMP, RECEIPT_ID);
    }

    @Test
    public void shouldReturnUserId() {
        SimpleActivityDetails s = new SimpleActivityDetails(USER_ID, USER_PROFILE, APP_PROFILE, TIMESTAMP, RECEIPT_ID);
        assertEquals(USER_ID, s.getUserId());
    }

    @Test
    public void shouldReturnUserProfile() {
        SimpleActivityDetails s = new SimpleActivityDetails(USER_ID, USER_PROFILE, APP_PROFILE, TIMESTAMP, RECEIPT_ID);
        assertEquals(USER_PROFILE_WRAPPER, s.getUserProfile());
    }

    @Test
    public void shouldReturnAppProfile() {
        SimpleActivityDetails s = new SimpleActivityDetails(USER_ID, APP_PROFILE, APP_PROFILE, TIMESTAMP, RECEIPT_ID);
        assertEquals(APP_PROFILE_WRAPPER, s.getApplicationProfile());
    }

    @Test
    public void shouldReturnReceiptId() {
        SimpleActivityDetails s = new SimpleActivityDetails(USER_ID, USER_PROFILE, APP_PROFILE, TIMESTAMP, RECEIPT_ID);
        assertEquals(RECEIPT_ID_STRING, s.getReceiptId());
    }
    
    @Test
    public void shouldReturnBase64SelfieIfSelfieSet() {
        List<Attribute> attrList = new ArrayList<Attribute>();
        Attribute selfie = new Attribute("selfie", new JpegAttributeValue("selfieTestVal".getBytes()), null);
        attrList.add(selfie);
    		
        SimpleActivityDetails s = new SimpleActivityDetails(USER_ID, new SimpleProfile(attrList), APP_PROFILE, TIMESTAMP, RECEIPT_ID);
        String selfieData = "data:image/jpeg;base64," + Base64.toBase64String(s.getUserProfile().getSelfie().getContent());
        
        assertEquals(selfieData, s.getBase64Selfie());
    }
    
    @Test
    public void shouldReturnBlankBase64SelfieIfSelfieNotSet() {
        List<Attribute> attrList = new ArrayList<Attribute>();
        Attribute familyName = new Attribute("family_name", "Smith", null);
        attrList.add(familyName);
    		
        SimpleActivityDetails s = new SimpleActivityDetails(USER_ID, new SimpleProfile(attrList), APP_PROFILE, TIMESTAMP, RECEIPT_ID);
        
        assertEquals("", s.getBase64Selfie());
    }
}
