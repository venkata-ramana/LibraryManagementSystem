package edu.sjsu.cmpe275.project.controller;

import edu.sjsu.cmpe275.project.model.Book;
import edu.sjsu.cmpe275.project.model.BookCopy;
import edu.sjsu.cmpe275.project.model.User;
import edu.sjsu.cmpe275.project.service.AlertService;
import edu.sjsu.cmpe275.project.service.BookCopyService;
import edu.sjsu.cmpe275.project.service.BookService;
import edu.sjsu.cmpe275.project.service.UserService;
import edu.sjsu.cmpe275.project.util.CustomTimeService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class LibrarianControllerTest
{
    @Mock
    private AlertService alertServiceMock;

    @Mock
    private BookCopyService bookCopyServiceMock;

    @Mock
    private UserService userServiceMock;

    @Mock
    private BookService bookServiceMock;

    @Mock
    private CustomTimeService myTimeServiceMock;

    @InjectMocks
    private LibrarianControllerStub librarianController;

    @Before
    public void beforeAllTests()
    {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testRenderIndex()
    {
        ModelMap modelMap = mock(ModelMap.class);
        List<Book> books = new ArrayList<Book>();
        books.add(new Book());
        books.add(new Book());
        when(bookServiceMock.findAllBooks()).thenReturn(books);
        User user = new User();
        user.setFirstName("abc");
        when(userServiceMock.findByEmail("abc@xyz.com")).thenReturn(user);
        Date date = new Date();
        when(myTimeServiceMock.getDate()).thenReturn(date);
        librarianController.renderIndex(modelMap);
        verify(modelMap, times(1)).addAttribute("books", books);
        verify(modelMap, times(1)).addAttribute("user", "abc");
        verify(modelMap, times(1)).addAttribute("dateTime", date);

    }

    @Test
    public void testRenderBookRegistration(){
        ModelMap modelMap = mock(ModelMap.class);
        librarianController.renderBookRegistration(modelMap);
        verify(modelMap, times(1)).addAttribute(eq("book"), any(Book.class));
        verify(myTimeServiceMock, times(1)).setDate(any(Date.class));
    }

    @Test
    public void testRenderSearchResults(){
        ModelMap modelMap = mock(ModelMap.class);
        String txtSearch = "Books";
        List<Book> books = new ArrayList<Book>();
        books.add(new Book());
        books.add(new Book());
        when(bookServiceMock.findByTitle(txtSearch)).thenReturn(books);

        User user = new User();
        user.setFirstName("abc");
        when(userServiceMock.findByEmail("abc@xyz.com")).thenReturn(user);
        Date date = new Date();
        when(myTimeServiceMock.getDate()).thenReturn(date);
        librarianController.renderSearchResults(txtSearch, modelMap);

        verify(modelMap, times(1)).addAttribute("books", books);
        verify(modelMap, times(1)).addAttribute("user", "abc");
        verify(modelMap, times(1)).addAttribute("dateTime", date);

    }

    @Test
    public void testRegisterNewBookWithValidCopiesAndValidBookId(){
        Book book = new Book();
        when(bookServiceMock.saveBook(book)).thenReturn(10);
        when(bookServiceMock.findById("10")).thenReturn(book);
        librarianController.registerNewBook(book, null, null, "5");
        verify(bookCopyServiceMock, times(5)).saveCopy(any(BookCopy.class));
    }
    @Test
    public void testRegisterNewBookWithInvalidCopiesAndValidBookId(){
        Book book = new Book();
        when(bookServiceMock.saveBook(book)).thenReturn(10);
        when(bookServiceMock.findById("10")).thenReturn(book);
        librarianController.registerNewBook(book, null, null, "invalidStringValue");
        verify(bookCopyServiceMock, times(1)).saveCopy(any(BookCopy.class));
    }
    @Test
    public void testRegisterNewBookWithInvalidBookId(){
        Book book = new Book();
        when(bookServiceMock.saveBook(book)).thenReturn(-1);
        when(bookServiceMock.findById("-1")).thenReturn(book);
        String returnValue = librarianController.registerNewBook(book, null, null, "5");
        verify(bookCopyServiceMock, times(0)).saveCopy(any(BookCopy.class));
        assertEquals("Error", returnValue);

    }

}

class LibrarianControllerStub extends LibrarianController
{

    @Override
    protected String getPrincipal() {
        return "abc@xyz.com";
    }
}

//package edu.sjsu.cmpe275.project.controller;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithUserDetails;
//import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
//import org.springframework.security.web.FilterChainProxy;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import edu.sjsu.cmpe275.project.configuration.HibernateConfigTest;
//import edu.sjsu.cmpe275.project.security.CustomUserDetailsService;
//
///**
// * @author Onkar Ganjewar
// */
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = HibernateConfigTest.class)
//@WebAppConfiguration
//public class LibrarianControllerTest {
//
//	static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
//
//	@Autowired
//	private WebApplicationContext wac;
//
//	private MockMvc springMvc;
//
//	@Autowired
//	private FilterChainProxy springSecurityFilterChain;
//
//	@Before
//	public void setup() {
//		this.springMvc = MockMvcBuilders.webAppContextSetup(wac).addFilters(springSecurityFilterChain)
//				.apply(SecurityMockMvcConfigurers.springSecurity()).build();
//	}
//
//	/**
//	 * Test for librarian home page
//	 * @throws Exception
//	 */
//	@Test
//	@WithUserDetails(value="onkar.ganjewar@sjsu.edu", userDetailsServiceBeanName="customUserDetailsService")
//    public void renderIndexTest() throws Exception {
//        this.springMvc.perform(get("/librarian/home")
//        		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//        		.accept(MediaType.ALL)).andDo(print())
//        		.andExpect(MockMvcResultMatchers.status().isOk())
//        		.andExpect(MockMvcResultMatchers.view().name("admin"))
//        		.andDo(print());
//	}
//
//	/**
//	 * Test to check if the new book registration page is returned or not.
//	 * @throws Exception
//	 */
//	@Test
//	@WithUserDetails(value="onkar.ganjewar@sjsu.edu", userDetailsServiceBeanName="customUserDetailsService")
//    public void renderBookRegistrationTest() throws Exception {
//		this.springMvc.perform(get("/librarian/new-book")
//				.accept(MediaType.ALL))
//				.andDo(print()).andExpect(status().isOk())
//				.andDo(print());
//    }
//
//	/**
//	 * Test to fetch the search results for a book based on the title
//	 * @throws Exception
//	 */
//	@Test
//	@WithUserDetails(value="onkar.ganjewar@sjsu.edu", userDetailsServiceBeanName="customUserDetailsService")
//    public void renderSearchResultsTest() throws Exception {
//        this.springMvc.perform(get("/librarian/search-book-testBook")
//        		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//        		.accept(MediaType.ALL)).andDo(print())
//        		.andExpect(MockMvcResultMatchers.status().isOk())
//        		.andExpect(MockMvcResultMatchers.view().name("searchResults"))
//        		.andDo(print());
//	}
//
//	/**
//	 * Test to delete the book by id
//	 * @throws Exception
//	 */
//	@Test
//	@WithUserDetails(value="onkar.ganjewar@sjsu.edu", userDetailsServiceBeanName="customUserDetailsService")
//    public void deleteBookTest() throws Exception {
//        this.springMvc.perform(get("/librarian/delete-book-10")
//        		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//        		.accept(MediaType.ALL)).andDo(print())
//        		.andExpect(MockMvcResultMatchers.status().isOk())
//        		.andExpect(MockMvcResultMatchers.view().name("admin"))
//        		.andDo(print());
//	}
//
//	/**
//	 * Test to GET the book details by ISBN
//	 * @throws Exception
//	 */
//	@Test
//	@WithUserDetails(value="onkar.ganjewar@sjsu.edu", userDetailsServiceBeanName="customUserDetailsService")
//    public void renderBookByISBNTest() throws Exception {
//        this.springMvc.perform(get("/librarian/bookInfo-0735619670")
//        		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//        		.accept(MediaType.ALL)).andDo(print())
//        		.andExpect(MockMvcResultMatchers.status().isOk())
//        		.andExpect(MockMvcResultMatchers.view().name("newBook"))
//        		.andDo(print());
//	}
//
//}


