package edu.sjsu.cmpe275.project.dao;

import edu.sjsu.cmpe275.project.model.Book;
import edu.sjsu.cmpe275.project.model.BookCopy;
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

public class BookCopyDaoImplTest {
    @Mock
    private SessionFactory sessionFactory;
    @Mock
    private Session session;
    @Mock
    private Criteria criteria;
    @InjectMocks
    private BookCopyDaoImpl bookCopyDaoImpl;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(sessionFactory.getCurrentSession())
                .thenReturn(session);
    }

    @Test
    public void save() {
        // Setup
        BookCopy bookCopy = new BookCopy();
        when(session.merge(any()))
                .thenReturn(bookCopy);
        // Act
        bookCopyDaoImpl.save(bookCopy);

        // Arrange
        verify(session, times(1)).merge(bookCopy);
    }

    @Test
    public void findByBook() {
        // Setup
        List<BookCopy> bookCopyList = new ArrayList<>();
        BookCopy bookCopy = new BookCopy();
        bookCopyList.add(bookCopy);

        when(session.createCriteria((Class) anyObject()))
                .thenReturn(criteria);
        when(criteria.list()).thenReturn(bookCopyList);

        // Act
        bookCopyDaoImpl.findByBook(new Book());

        // Assert
        verify(criteria, times(1)).list();
    }

    @Test
    public void remove() {
        // Setup
        BookCopy bookCopy = new BookCopy();

        // Act
        bookCopyDaoImpl.remove(bookCopy);

        // Assert
        verify(session, times(1)).delete(bookCopy);
    }
}