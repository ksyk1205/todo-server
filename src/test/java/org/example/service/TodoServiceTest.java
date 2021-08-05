package org.example.service;

import org.example.model.TodoEntity;
import org.example.model.TodoRequest;
import org.example.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {
    @Mock //직접적인 Mock 객체
    private TodoRepository todoRepository;

    @InjectMocks //Mock을 주입받아서 사용할
    private TodoService todoService;

    @Test
    void add() {
        when(this.todoRepository.save(any(TodoEntity.class)))
                .then(AdditionalAnswers.returnsFirstArg());
        TodoRequest expected = new TodoRequest();
        expected.setTitle("Test Title");

        TodoEntity actual = this.todoService.add(expected);

        assertEquals(expected.getTitle(),actual.getTitle());
    }

    @Test
    void searchById() {
        TodoEntity todo = new TodoEntity();
        todo.setId(123L);
        todo.setTitle("TITLE");
        todo.setOrder(0L);
        todo.setCompleted(false);
        Optional<TodoEntity> optional = Optional.of(todo);

        given(this.todoRepository.findById(anyLong()))
                .willReturn(optional);
        TodoEntity actual = this.todoService.searchById(123L);

        TodoEntity expected = optional.get();

        assertEquals(expected.getId(),actual.getId());
        assertEquals(expected.getOrder(),actual.getOrder());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getCompleted(),actual.getCompleted());
    }

    @Test
    public void searchByIdFailed(){
        given(this.todoRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () ->{
           this.todoService.searchById(123L);
        });
    }


}