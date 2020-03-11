package edu.sjsu.cmpe275.project.dao;


import edu.sjsu.cmpe275.project.model.BookCart;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

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
        MockitoAnnotations.initMocks(this);
        Logger logger = PowerMockito.mock(Logger.class);

        PowerMockito.mockStatic(LoggerFactory.class);
        PowerMockito.when(LoggerFactory.getLogger(Example.class)).thenReturn(logger);


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
    }

    @Test
    public void testRemove() {
        BookCart cart = new BookCart();
        bookCartDaoImpl.remove(cart);

        verify(session, times(1)).delete(any());
    }

    @Test
    public void testVerifySuccessWhenFindByUserId() {
        when(session.createCriteria((Class) anyObject())).thenReturn(criteria);
        BookCart cart1 = new BookCart();
        List<BookCart> bookCartList = new ArrayList<>();
        bookCartList.add(cart1);
        when(criteria.list()).thenReturn(bookCartList);
        bookCartDaoImpl.findByUserId(123);

        verify(criteria, times(1)).list();
    }

    /*@Test
    public void testFindByUserId() {
        when(session.createCriteria((Class) anyObject())).thenReturn(criteria);
        BookCart cart1 = new BookCart();
        List<BookCart> bookCartList = new ArrayList<>();
        bookCartList.add(cart1);
        when(criteria.list()).thenReturn(bookCartList);
        bookCartDaoImpl.findByUserId(123);

        verify(criteria, times(1)).list();
    }*/


}
