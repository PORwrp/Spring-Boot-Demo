package com.ascend.springbootdemo.controllers;

import com.ascend.springbootdemo.entities.Author;
import com.ascend.springbootdemo.entities.Post;
import com.ascend.springbootdemo.services.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by BiG on 6/17/2017 AD.
 */
public class PostControllerTest {
    @InjectMocks
    private PostController controller;
    @Mock
    private PostService postService;
    private MockMvc mockMvc;
    private Author author1;
    private Author author2;
    private Post post1;
    private Post post2;

    private ObjectMapper mapper = new ObjectMapper();

    private final String content1 = "content1";
    private final String content2 = "content2";

    @Before
    public void beforeEach() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        author1 = new Author();
        author1.setId(1L);

        author2 = new Author();
        author2.setId(2L);


        post1 = new Post();
        post1.setAuthor(author1);
        post1.setContent(content1);
        post1.setId(1L);
        post2 = new Post();
        post2.setAuthor(author1);
        post2.setContent(content2);
        post2.setId(2L);
    }

    @Test
    public void shouldReturnPostWhenCreatePostSuccessfully() throws Exception {
        when(postService.createPost(anyLong(), Matchers.any(Post.class))).thenReturn(post1);

        mockMvc.perform(post("/api/v1/authors/1/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(post1)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.content", is(content1)))
                .andExpect(status().isCreated());

        verify(postService).createPost(anyLong(), Matchers.any(Post.class));
    }

    @Test
    public void shouldReturnPostWhenGetExistingPostSuccessfully() throws Exception {
        when(postService.getPostById(anyLong())).thenReturn(post1);

        mockMvc.perform(get("/api/v1/authors/posts/1"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.content", is(content1)))
                .andExpect(status().isOk());

        verify(postService).getPostById(anyLong());
    }
}
