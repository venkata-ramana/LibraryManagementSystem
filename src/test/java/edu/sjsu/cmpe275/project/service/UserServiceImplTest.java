package edu.sjsu.cmpe275.project.service;

import edu.sjsu.cmpe275.project.dao.UserDao;
import edu.sjsu.cmpe275.project.dao.VerificationTokenDao;
import edu.sjsu.cmpe275.project.model.User;
import edu.sjsu.cmpe275.project.model.VerificationToken;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private UserDao dao;

    @Mock
    private VerificationTokenDao tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    User chris;

    Calendar cal;


    @Before
    public void SetUp() {
        MockitoAnnotations.initMocks(this);

        cal = Calendar.getInstance();
        cal.setTime(new Date());

        chris = new User();
        chris.setFirstName("Chris");
        chris.setLastName("Hemsworth");
    }

    @Test
    public void testfindByValidIdReturnsValidUser(){
        when(dao.findById(10)).thenReturn(chris);
        User result = userService.findById(10);

        assertEquals(chris.getFirstName(), result.getFirstName());
        assertEquals(chris.getLastName(), result.getLastName());
    }

    @Test
    public void testfindByValidEmailReturnsValidUser(){
        when(dao.findByEmail("chris@gmail.com")).thenReturn(chris);
        User result = userService.findByEmail("chris@gmail.com");

        assertEquals(chris.getFirstName(), result.getFirstName());
        assertEquals(chris.getLastName(), result.getLastName());
    }

    @Test
    public void testVerificationToken_WithInvalidToken_ReturnsInvalidTokenMessage() {
        when(tokenRepository.findByToken("testtoken")).thenReturn(null);
        String result = userService.validateVerificationToken("testtoken");
        assertEquals("invalidToken", result);
    }

    @Test
    public void testVerificationToken_WithExpiredToken_ReturnsExpiredTokenMessage() {
        // Arrange
        cal.add(Calendar.DATE, -1);

        VerificationToken token = new VerificationToken();
        token.setToken("testtoken");
        token.setExpiryDate(cal.getTime());
        token.setUser(chris);
        when(tokenRepository.findByToken("testtoken")).thenReturn(token);

        // Act
        String result = userService.validateVerificationToken("testtoken");

        // Assert
        assertEquals("expired", result);
    }

    @Test
    public void testVerificationToken_WithValidToken_ReturnsValidTokenMessage() {
        // Arrange
        cal.add(Calendar.DATE, 1);

        VerificationToken token = new VerificationToken();
        token.setToken("testtoken");
        token.setExpiryDate(cal.getTime());
        token.setUser(chris);
        when(tokenRepository.findByToken("testtoken")).thenReturn(token);

        // Act
        String result = userService.validateVerificationToken("testtoken");

        // Assert
        assertEquals("valid", result);
    }

    @Test
    public void testFindByTokenWithValidToken() {
        // Arrange
        cal.add(Calendar.DATE, 1);
        VerificationToken token = new VerificationToken();
        token.setToken("testtoken");
        token.setExpiryDate(cal.getTime());
        token.setUser(chris);
        when(tokenRepository.findByToken("testtoken")).thenReturn(token);

        // Act
        User result = userService.findByToken("testtoken");

        // Assert
        assertEquals(chris.getFirstName(), result.getFirstName());
        assertEquals(chris.getLastName(), result.getLastName());
    }

    @Test
    public void testFindByTokenWithInValidToken() {
        // Act
        User result = userService.findByToken("testtoken");

        // Assert
        assertNull(result);
    }
}
