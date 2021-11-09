/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.contrib.meeting.test.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xwiki.contrib.meeting.test.ui.po.MeetingEntryInlinePage;
import org.xwiki.contrib.meeting.test.ui.po.MeetingEntryPage;
import org.xwiki.contrib.meeting.test.ui.po.MeetingHomePage;
import org.xwiki.test.docker.junit5.UITest;
import org.xwiki.test.ui.TestUtils;
import org.xwiki.test.ui.po.CreatePagePage;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * UI tests for the Meeting application feature.
 * 
 * @since 1.13
 */
@UITest
public class MeetingIT
{
    private GreenMail mail;

    @BeforeEach
    public void startMail()
    {
        this.mail = new GreenMail(ServerSetupTest.SMTP);
        this.mail.start();
    }

    @AfterEach
    public void stopMail()
    {
        if (this.mail != null) {
            this.mail.stop();
        }
    }

    @Test
    public void sendMeetingInvitation(TestUtils setup) throws Exception
    {
        setup.loginAsSuperAdmin();
        
        // Configure the SMTP host/port for the wiki so that it points to GreenMail.
        setup.addObject("Mail", "MailConfig", "Mail.SendMailConfigClass");
        setup.updateObject("Mail", "MailConfig", "Mail.SendMailConfigClass", 0, "port", 3025);
        setup.updateObject("Mail", "MailConfig", "Mail.SendMailConfigClass", 0, "host", "localhost");

        // Create some participants to meetings
        setup.createUser("participant1", "password", setup.getURLToNonExistentPage(), "email",
            "participant1@xwiki.org", "first_name", "participant1", "last_name", "Doe");
        setup.createUser("participant2", "password", setup.getURLToNonExistentPage(), "email",
            "participant2@xwiki.org", "first_name", "participant2", "last_name", "Doe");

        // Create user to be Leader of meeting
        setup.createUserAndLogin("JohnDoe", "password", "email", "john@xwiki.org", "first_name", "John",
            "last_name", "Doe");

        // Remove existing meeting
        setup.deletePage("Meeting", "Meeting 01");

        // Create new meeting
        MeetingHomePage meetingHomePage = MeetingHomePage.gotoPage();
        
        CreatePagePage createPage = meetingHomePage.createPage();
        createPage.getDocumentPicker().setTitle("Meeting 01");
        createPage.setTemplate("Meeting.Code.MeetingTemplateProvider");
        createPage.clickCreate();

        MeetingEntryInlinePage meetingEntryInlinePage = new MeetingEntryInlinePage();
        meetingEntryInlinePage.setStartDate(getDateTomorrow());
        meetingEntryInlinePage.setEndDate(getEndDate());
        meetingEntryInlinePage.setPlace("Paris");
        meetingEntryInlinePage.setDescription("First meeting in paris");
        meetingEntryInlinePage.setNotes("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
        meetingEntryInlinePage.setStatus("In preparation");
        meetingEntryInlinePage.setLeader("John Doe");

        ArrayList<String> participants = new ArrayList<String>();
        participants.add("participant1 Doe");
        participants.add("participant2 Doe");
        meetingEntryInlinePage.setParticipants(participants);

        meetingEntryInlinePage.clickSaveAndView();

        MeetingEntryPage meetingEntryPage = new MeetingEntryPage();

        // Send invitation message
        meetingEntryPage.setMessage("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy " +
            "eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua");
        meetingEntryPage.sendMessage();

        // Verify that the mail has been sent.
        assertTrue(meetingEntryPage.getNotification()
            .contains("A notification email has successfully been sent to the participants."));
        
        // Verify that the mail has been received.
        this.mail.waitForIncomingEmail(10000L, 2);
        assertEquals(2, this.mail.getReceivedMessages().length);
        assertEquals("You are invited to participate in a meeting: Meeting 01",
            this.mail.getReceivedMessages()[0].getSubject());
        assertEquals("You are invited to participate in a meeting: Meeting 01",
            this.mail.getReceivedMessages()[1].getSubject());
    }

    private String getDateTomorrow()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return dateFormat.format(calendar.getTime());
    }

    private String getEndDate()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 2);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return dateFormat.format(calendar.getTime());
    }
}
