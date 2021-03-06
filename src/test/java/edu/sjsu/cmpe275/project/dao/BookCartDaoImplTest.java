package edu.sjsu.cmpe275.project.dao;


import edu.sjsu.cmpe275.project.model.BookCart;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.*;
import static org.slf4j.LoggerFactory.*;

import org.slf4j.Logger;

public class BookCartDaoImplTest {
    @Mock
    private SessionFactory sessionFactory;
    @Mock
    private Session session;
    @Mock
    private Criteria criteria;
    @InjectMocks
    private BookCartDaoImpl bookCartDaoImpl;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    public void testInsert() {
        BookCart cart = new BookCart();
        bookCartDaoImpl.insert(cart);

        verify(session, times(1)).save(any());
    }

    @Test
    public void testInsertReturnsException() {
        BookCart cart = new BookCart();
        when(session.save(any())).thenReturn(new Exception("Exception"));
        bookCartDaoImpl.insert(cart);

        verify(session, times(1)).save(any());
        // TODO(rmk): 2020-03-11  Verify the logger error method is called.
    }

    @Test
    public void testRemove() {
        // Setup
        BookCart cart = new BookCart();

        // Act
        bookCartDaoImpl.remove(cart);

        // Assert
        verify(session, times(1)).delete(any());
    }

    @Test
    public void testVerifySuccessWhenFindByUserId() {
        // Setup
        BookCart cart1 = new BookCart();
        List<BookCart> bookCartList = new ArrayList<>();
        bookCartList.add(cart1);

        when(session.createCriteria((Class) anyObject())).thenReturn(criteria);
        when(criteria.list()).thenReturn(bookCartList);

        // Act
        List<BookCart> result = bookCartDaoImpl.findByUserId(123);

        // Verify
        verify(criteria, times(1)).list();
        assertEquals(bookCartList, result);
    }

    // TODO(rmk): 2020-03-11  Add test to verify logger message for FindByUserId when list is null.
}
