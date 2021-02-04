package com.twolak.springframework.authwebapp.services.impl;

import com.twolak.springframework.authwebapp.domain.Comment;
import com.twolak.springframework.authwebapp.domain.Post;
import com.twolak.springframework.authwebapp.domain.User;
import com.twolak.springframework.authwebapp.exceptions.NotFoundException;
import com.twolak.springframework.authwebapp.repository.PostRepository;
import com.twolak.springframework.authwebapp.services.PostService;
import com.twolak.springframework.authwebapp.services.SecurityService;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.hamcrest.CoreMatchers.equalTo;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author twolak
 */
@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {
    
    private static final long ID_1 = 1L;
    private static final long ID_2 = 2L;
    private static final long ID_3 = 3L;
    private static final String USERNAME = "thomas";
    
    @Mock
    private PostRepository postRepository;
    
    @Mock
    private SecurityService securityService;
    
    private PostService postService;
    
    @BeforeEach
    public void setUp() {
        this.postService = new PostServiceImpl(postRepository, securityService);
    }

    @Test
    public void testFindPaginated() {
        Pageable pageable = PageRequest.of(0, 3);
        Post post1 = Post.builder().id(ID_1).topic("topic1").message("message1").build();
        Post post2 = Post.builder().id(ID_2).topic("topic2").message("message2").build();
        Post post3 = Post.builder().id(ID_3).topic("topic3").message("message3").build();
        Page<Post> posts = new PageImpl<>(List.of(post1, post2, post3), pageable, 3);
        
        Mockito.when(this.postRepository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(posts);
        
        Page<Post> foundPosts = this.postService.findPaginated(pageable);
        
        Assertions.assertNotNull(foundPosts);
        Assertions.assertEquals(3, foundPosts.getNumberOfElements());
        Assertions.assertEquals(3, foundPosts.getSize());
        Assertions.assertEquals(1, foundPosts.getTotalPages());
        Assertions.assertEquals(3, foundPosts.getTotalElements());
        Mockito.verify(this.postRepository, Mockito.times(1)).findAll(ArgumentMatchers.any(Pageable.class));
        Mockito.verifyNoMoreInteractions(this.postRepository);
    }

    @Test
    public void testFindPostByIdBDD() {
        Post post = Post.builder().id(ID_1).topic("topic1").message("message1").build();
        
        BDDMockito.given(this.postRepository.findById(ArgumentMatchers.anyLong())).willReturn(Optional.of(post));
        
        Post foundPost = this.postService.findPostById(ID_1);
        
        BDDMockito.then(this.postRepository).should(Mockito.times(1)).findById(ArgumentMatchers.anyLong());
        
        MatcherAssert.assertThat(foundPost, Matchers.notNullValue());
        MatcherAssert.assertThat(foundPost.getId(), Matchers.is(equalTo(ID_1)));
    }
    
    @Test
    public void testFindPostById() {
        Post post = Post.builder().id(ID_1).topic("topic1").message("message1").build();
        
        Mockito.when(this.postRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(post));
        
        Post foundPost = this.postService.findPostById(ID_1);
        
        Assertions.assertNotNull(foundPost);
        Assertions.assertEquals(ID_1, foundPost.getId());
        
        Mockito.verify(this.postRepository, Mockito.times(1)).findById(ArgumentMatchers.anyLong());
        Mockito.verifyNoMoreInteractions(this.postRepository);
    }
    
    @Test
    public void testFindPostByIdNotFound() {
        Mockito.when(this.postRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> this.postService.findPostById(ID_1));
        
        Mockito.verify(this.postRepository, Mockito.times(1)).findById(ArgumentMatchers.anyLong());
        Mockito.verifyNoMoreInteractions(this.postRepository);
    }

    @Test
    public void testSavePost() {
        Post post = Post.builder().id(ID_1).topic("topic1").message("message1").build();
        Post postToSave = Post.builder().topic("topic1").message("message1").build();
        
        Mockito.when(this.postRepository.save(ArgumentMatchers.any(Post.class))).thenReturn(post);
        
        Post savedPost = this.postService.savePost(postToSave);
        
        Assertions.assertNotNull(savedPost);
        Assertions.assertEquals(ID_1, savedPost.getId());
        Mockito.verify(this.postRepository, Mockito.times(1)).save(ArgumentMatchers.any(Post.class));
        Mockito.verifyNoMoreInteractions(this.postRepository);
    }

    @Test
    public void testDeletePost() {
        this.postService.deletePost(ID_1);
        
        Mockito.verify(this.postRepository, Mockito.times(1)).deleteById(ArgumentMatchers.anyLong());
        Mockito.verifyNoMoreInteractions(this.postRepository);
    }

    @Test
    public void testSaveComment() {
        Post post = Post.builder().id(ID_1).topic("topic1").message("message1").comments(new HashSet<>()).build();
        User user = User.builder().id(ID_1).userName(USERNAME).build();
        Comment comment = Comment.builder().message("comm message1").build();
        Post savedPost = Post.builder().id(ID_1).topic("topic1").message("message1").comments(Set.of(comment)).build();
        
        Mockito.when(this.postRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(post));
        Mockito.when(this.securityService.getAuthenticatedUser()).thenReturn(user);
        Mockito.when(this.postRepository.save(ArgumentMatchers.any(Post.class))).thenReturn(savedPost);
        
        Post returnedPost = this.postService.saveComment(ID_1, comment);
        
        Assertions.assertNotNull(returnedPost);
        Assertions.assertEquals(1, returnedPost.getComments().size());
        
        Mockito.verify(this.postRepository, Mockito.times(1)).findById(ArgumentMatchers.anyLong());
        Mockito.verify(this.postRepository, Mockito.times(1)).save(ArgumentMatchers.any(Post.class));
        Mockito.verify(this.securityService, Mockito.times(1)).getAuthenticatedUser();
        Mockito.verifyNoMoreInteractions(this.postRepository);
        Mockito.verifyNoMoreInteractions(this.securityService);
    }
    
}
