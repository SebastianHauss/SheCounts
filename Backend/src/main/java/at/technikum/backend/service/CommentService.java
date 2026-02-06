package at.technikum.backend.service;

import at.technikum.backend.entity.Comment;
import at.technikum.backend.entity.Article;
import at.technikum.backend.entity.User;
import at.technikum.backend.exceptions.EntityNotFoundException;
import at.technikum.backend.exceptions.UnauthorizedException;
import at.technikum.backend.mapper.CommentMapper;
import at.technikum.backend.repository.ArticleRepository;
import at.technikum.backend.repository.CommentRepository;
import at.technikum.backend.dto.CommentDto;
import at.technikum.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final ArticleRepository articleRepository;
	private final UserRepository userRepository;
	private final CommentMapper mapper;

	public List<CommentDto> findAll() {
		return commentRepository.findAll().stream()
				.map(mapper::toDto)
				.collect(Collectors.toList());
	}

	public List<CommentDto> findByArticleId(UUID articleId) {
		List<Comment> comments = commentRepository.findByArticleId(articleId);
		return comments.stream()
				.map(mapper::toDto)
				.collect(Collectors.toList());
	}

	public List<CommentDto> findByUserEmail(String userEmail) {
		User user = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new EntityNotFoundException("User not found"));

		List<Comment> comments = commentRepository.findByUserId(user.getId());
		return comments.stream()
				.map(mapper::toDto)
				.collect(Collectors.toList());
	}

	public List<CommentDto> findByUserId(UUID userId) {
		List<Comment> comments = commentRepository.findByUserId(userId);
		return comments.stream()
				.map(mapper::toDto)
				.collect(Collectors.toList());
	}

	public CommentDto create(CommentDto dto, String userEmail) {
		Comment comment = mapper.toEntity(dto);

		Article article = articleRepository.findById(dto.getArticleId())
				.orElseThrow(() -> new EntityNotFoundException("Article not found"));

		User user = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new EntityNotFoundException("User not found"));

		comment.setArticle(article);
		comment.setUser(user);

		Comment saved = commentRepository.save(comment);
		return mapper.toDto(saved);
	}

	public CommentDto update(UUID id, CommentDto dto, String userEmail) {
		Comment existing = commentRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Comment not found"));

		User user = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new EntityNotFoundException("User not found"));

		if (!existing.getUser().getId().equals(user.getId()) && !user.isAdmin()) {
			throw new UnauthorizedException("You can only update your own comments");
		}

		existing.setTitle(dto.getTitle());
		existing.setText(dto.getText());

		Comment updated = commentRepository.save(existing);
		return mapper.toDto(updated);
	}

	public void delete(UUID id, String userEmail) {
		Comment existing = commentRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Comment not found"));

		User user = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new EntityNotFoundException("User not found"));

		if (!existing.getUser().getId().equals(user.getId()) && !user.isAdmin()) {
			throw new UnauthorizedException("You can only delete your own comments");
		}

		commentRepository.delete(existing);
	}
}
