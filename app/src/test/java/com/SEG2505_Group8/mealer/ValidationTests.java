package com.SEG2505_Group8.mealer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.text.Editable;
import android.widget.EditText;

import com.SEG2505_Group8.mealer.UI.Activities.Utils.FieldValidator;

/**
 * Test validation class for proper form validation.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ValidationTests {

    @Mock
    private Context mockContext;

    @Before
    public void setupTests() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        when(mockContext.getString(anyInt())).thenReturn("mocked string");
    }

    @Test
    public void requiredEmpty() {
        FieldValidator validator = new FieldValidator(mockContext);

        // Mock an EditText instance
        EditText editText = mock(EditText.class);

        // Create argument captor to record interactions with mocked editText
        ArgumentCaptor<Editable> captor = ArgumentCaptor.forClass(Editable.class);

        // Capture value of setError call
        Mockito.doNothing().when(editText).setError(captor.capture());

        // Mock a length of 0 when checking length of editText
        Mockito.when(editText.length()).thenAnswer(invocation -> 0);

        // Call method to test
        validator.required(editText);

        // Assert that an error was captured
        verify(editText).setError(any());
    }

    @Test
    public void requiredNotEmpty() {
        FieldValidator validator = new FieldValidator(mockContext);

        EditText editText = mock(EditText.class);
        Mockito.when(editText.length()).thenAnswer(invocation -> 1);

        validator.required(editText);

        verify(editText, atLeastOnce()).setError(isNull());
    }

    @Test
    public void creditCardInvalid() {
        FieldValidator validator = new FieldValidator(mockContext);

        // Mock an EditText instance
        EditText editText = mock(EditText.class);

        // Create argument captor to record interactions with mocked editText
        ArgumentCaptor<Editable> captor = ArgumentCaptor.forClass(Editable.class);

        // Capture value of setError call
        Mockito.doNothing().when(editText).setError(captor.capture());

        // Mock a credit card with 9 digits when checking length of editText
        Mockito.when(editText.length()).thenAnswer(invocation -> 9);

        // Call method to test
        validator.creditCard(editText);

        // Assert that an error was captured
        verify(editText).setError(any());
    }

    @Test
    public void creditCardValid() {
        FieldValidator validator = new FieldValidator(mockContext);

        EditText editText = mock(EditText.class);
        Mockito.when(editText.length()).thenAnswer(invocation -> 16);

        validator.creditCard(editText);

        verify(editText, atLeastOnce()).setError(isNull());
    }
}