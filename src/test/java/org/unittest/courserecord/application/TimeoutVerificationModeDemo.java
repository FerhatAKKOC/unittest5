package org.unittest.courserecord.application;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.unittest.courserecord.model.Student;
import org.unittest.courserecord.model.StudentRepository;

public class TimeoutVerificationModeDemo {

    @Mock
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    void readStudent() {

        when(studentRepository.findById("id1")).thenReturn(Optional.of(new Student("", "", "")));

        new Thread(() -> {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            studentRepository.findById("id1");
        }).start();
        verify(studentRepository, timeout(2)).findById("id1");
    }
}
