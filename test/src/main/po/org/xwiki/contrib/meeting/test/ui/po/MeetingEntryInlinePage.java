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
package org.xwiki.contrib.meeting.test.ui.po;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.xwiki.test.ui.po.InlinePage;
import org.xwiki.test.ui.po.SuggestInputElement;

/**
 * Represents the meeting entry inline page.
 * 
 * @version $Id$
 */
public class MeetingEntryInlinePage extends InlinePage
{
    /**
     * Value attribute name.
     */
    public static final String VALUE_ATTRIBUTE_NAME = "Value";

    /**
     * The meeting entry start date input field.
     */
    @FindBy(id = "Meeting.Code.MeetingClass_0_startDate")
    private WebElement startDateInput;

    /**
     * The meeting entry end date input field.
     */
    @FindBy(id = "Meeting.Code.MeetingClass_0_endDate")
    private WebElement endDateInput;

    /**
     * The meeting entry place input field.
     */
    @FindBy(id = "Meeting.Code.MeetingClass_0_place")
    private WebElement placeInput;

    /**
     * The meeting entry description input field.
     */
    @FindBy(id = "Meeting.Code.MeetingClass_0_description")
    private WebElement descriptionInput;

    /**
     * The meeting entry notes input field.
     */
    @FindBy(id = "content")
    private WebElement notesInput;

    /**
     * The meeting entry status input field.
     */
    @FindBy(id = "Meeting.Code.MeetingClass_0_status")
    private WebElement statusInput;

    /**
     * The meeting entry leader input field.
     */
    @FindBy(id = "Meeting.Code.MeetingClass_0_leader")
    private WebElement leaderInput;

    /**
     * The meeting entry participants input field.
     */
    @FindBy(id = "Meeting.Code.MeetingClass_0_participants")
    private WebElement participantsInput;

    /**
     * @return the value of the start date input field
     */
    public String getStartDate()
    {
        return startDateInput.getAttribute(VALUE_ATTRIBUTE_NAME);
    }

    /**
     * Sets the value of the start date input field.
     *
     * @param startDate the new meeting entry start date
     */
    public void setStartDate(String startDate)
    {
        startDateInput.clear();
        startDateInput.sendKeys(startDate);
    }

    /**
     * @return the value of the end date input field
     */
    public String getEndDate()
    {
        return endDateInput.getAttribute(VALUE_ATTRIBUTE_NAME);
    }

    /**
     * Sets the value of the end date input field.
     *
     * @param endDate the new meeting entry start date
     */
    public void setEndDate(String endDate)
    {
        endDateInput.clear();
        endDateInput.sendKeys(endDate);
    }

    /**
     * @return the value of the place input field
     */
    public String getPlace()
    {
        return placeInput.getAttribute(VALUE_ATTRIBUTE_NAME);
    }

    /**
     * Sets the value of the place input field.
     *
     * @param place the new meeting entry place
     */
    public void setPlace(String place)
    {
        placeInput.clear();
        placeInput.sendKeys(place);
    }

    /**
     * @return the value of the description input field
     */
    public String getDescription()
    {
        return placeInput.getAttribute(VALUE_ATTRIBUTE_NAME);
    }

    /**
     * Sets the value of the description input field.
     *
     * @param description the new meeting entry description
     */
    public void setDescription(String description)
    {
        descriptionInput.clear();
        descriptionInput.sendKeys(description);
    }

    /**
     * @return the value of the notes input field
     */
    public String getNotes()
    {
        return notesInput.getAttribute(VALUE_ATTRIBUTE_NAME);
    }

    /**
     * Sets the value of the notes input field.
     *
     * @param notes the new meeting entry notes
     */
    public void setNotes(String notes)
    {
        notesInput.clear();
        notesInput.sendKeys(notes);
    }

    /**
     * @return the value of the notes input field
     */
    public String getStatus()
    {
        Select notesSelect = new Select(notesInput);
        return notesSelect.getFirstSelectedOption().getText();
    }

    /**
     * Sets the value of the status input field.
     *
     * @param status the new meeting entry status
     */
    public void setStatus(String status)
    {
        Select statusSelect = new Select(statusInput);
        statusSelect.selectByVisibleText(status);
    }

    /**
     * Sets the value of the leader input field.
     *
     * @param leader the new meeting entry leader
     */
    public void setLeader(String leader)
    {
        new SuggestInputElement(this.leaderInput).clearSelectedSuggestions().sendKeys(leader).waitForSuggestions()
            .selectByVisibleText(leader).hideSuggestions();
    }

    /**
     * Sets the value of the leader input field.
     *
     * @param participants the new meeting entry participants
     */
    public void setParticipants(List<String> participants)
    {
        SuggestInputElement participantsSuggestInput = new SuggestInputElement(this.participantsInput);
        participantsSuggestInput.clearSelectedSuggestions();
        for (String participant : participants) {
            participantsSuggestInput.sendKeys(participant).waitForSuggestions().selectByVisibleText(participant);
        }
        participantsSuggestInput.hideSuggestions();
    }
}
