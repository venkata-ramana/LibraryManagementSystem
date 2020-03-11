package edu.sjsu.cmpe275.project.dao;


import edu.sjsu.cmpe275.project.model.BookCart;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.Mockito.*;

public class BookCartDaoImplTest {
    @Mock
    private SessionFactory sessionFactory;
    @Mock
    private Session session;
    @InjectMocks
    private BookCartDaoImpl bookCartDaoImpl;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInsert() {
     when(sessionFactory.getCurrentSession()).thenReturn(session);
     BookCart cart=new BookCart();
     bookCartDaoImpl.insert(cart);

     verify(session,times(1)).save(any());
    }

 /*   public void testRemove() {
    }

    public void testFindByUserId() {
    }


  */
}
