package edu.sjsu.cmpe275.project.dao;

import edu.sjsu.cmpe275.project.model.Checkout;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class CheckoutDaoImplTest {
    @Mock
    private SessionFactory sessionFactory;
    @Mock
    private Session session;
    @Mock
    private Criteria criteria;
    @InjectMocks
    private CheckoutDaoImpl checkoutDaoImpl;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(sessionFactory.getCurrentSession())
                .thenReturn(session);
        when(session.createCriteria((Class) anyObject()))
                .thenReturn(criteria);
    }

    @Test
    public void insert() {
        // Act
        checkoutDaoImpl.insert(new Checkout());

        // Assert
        verify(session, times(1)).save(any());

    }

    @Test
    public void remove() {
        // Act
        checkoutDaoImpl.remove(new Checkout());

        // Assert
        verify(session, times(1)).delete(any());
    }

    @Test
    public void findByBookId() {

    }

    @Test
    public void findByUserId() {
    }

    @Test
    public void modify() {
        // Act
        checkoutDaoImpl.modify(new Checkout());

        // Assert
        verify(session, times(1)).update(any());
    }

    @Test
    public void findAllCopies() {
        // Setup
        List<Checkout> checkOutList = new ArrayList<>();
        checkOutList.add(new Checkout());
        when(criteria.list()).thenReturn(checkOutList);

        // Act
        List<Checkout> result = checkoutDaoImpl.findAllCopies();

        // Assert
        verify(criteria, times(1)).list();
        assertEquals(checkOutList, result);
    }
}