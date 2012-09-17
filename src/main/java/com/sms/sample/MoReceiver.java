/*
 * FILENAME
 *     MoReceiver.java
 *
 * FILE LOCATION
 *     $Source$
 *
 * VERSION
 *     $Id$
 *         @version       $Revision$
 *         Check-Out Tag: $Name$
 *         Locked By:     $Lockers$
 *
 * FORMATTING NOTES
 *     * Lines should be limited to 78 characters.
 *     * Files should contain no tabs.
 *     * Indent code using four-character increments.
 *
 * COPYRIGHT
 *     Copyright (C) 2007 Genix Ventures Pty. Ltd. All rights reserved.
 *     This software is the confidential and proprietary information of
 *     Genix Ventures ("Confidential Information"). You shall not
 *     disclose such Confidential Information and shall use it only in
 *     accordance with the terms of the licence agreement you entered into
 *     with Genix Ventures.
 */

package com.sms.sample;

import hms.kite.samples.api.SdpException;
import hms.kite.samples.api.sms.MoSmsListener;
import hms.kite.samples.api.sms.SmsRequestSender;
import hms.kite.samples.api.sms.messages.MoSmsReq;
import hms.kite.samples.api.sms.messages.MtSmsReq;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

//
// IMPORTS
// NOTE: Import specific classes without using wildcards.
//

public class MoReceiver implements MoSmsListener
{
    private static final Logger LOGGER = Logger.getLogger(MoReceiver.class.getName());
    private SmsRequestSender requestSender;

    @Override
    public void init()
    {

    }

    @Override
    public void onReceivedSms(final MoSmsReq moSmsReq)
    {

        String message = moSmsReq.getMessage();
        LOGGER.log(Level.ALL, "New message reviced " + moSmsReq.getMessage());
        String responseMessage = findGender(message);

        MtSmsReq mtSmsReq = new MtSmsReq();
        mtSmsReq.setMessage(responseMessage);
        mtSmsReq.setApplicationId(moSmsReq.getApplicationId());
        mtSmsReq.setPassword("password");
        mtSmsReq.setDestinationAddresses(Arrays.asList(moSmsReq.getSourceAddress()));

        try
        {
            requestSender = new SmsRequestSender(new URL("http://127.0.0.1:7000/sms/send"));
            requestSender.sendSmsRequest(mtSmsReq);
        }
        catch (SdpException e)
        {
            LOGGER.log(Level.ALL, "MT message sending error " + e);
        }
        catch (MalformedURLException e)
        {
            LOGGER.log(Level.ALL, "URL error " + e);
        }
    }

    private String findGender(final String message)
    {
        try
        {
            String[] messgages = message.split(" ");

            String idNumber = messgages[1];
            int number = Integer.valueOf(idNumber.substring(2, 5));
            if (number < 500)
                return "Your gender is MALE";
            else
                return "Your gender is FEMALE";
        }
        catch (Exception exception)
        {
            return "Invalid message format";
        }
    }
}
