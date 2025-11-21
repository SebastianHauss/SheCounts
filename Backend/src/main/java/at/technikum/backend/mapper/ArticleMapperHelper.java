package at.technikum.backend.mapper;

import at.technikum.backend.entity.Comment;
import at.technikum.backend.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ArticleMapperHelper {
    private final CommentRepository commentRepository;

    @Named("getUUIDListFromComments")
    public List<UUID> getUUIDListFromComments(List<Comment> commentList){
        List<UUID> commentUUIDList = new ArrayList<>();
        commentUUIDList = commentList.stream()
                .map(Comment::getId)
                .toList();
        return  commentUUIDList;
    }

    @Named("getCommentListFromUUIDs")
    public List<Comment> getCommentListFromUUIDs(List<UUID> commentUUIDList){

        return commentRepository.findAllById(commentUUIDList);

    }
}
